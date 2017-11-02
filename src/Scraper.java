import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Scraper {
    private String name = "";
    private HashSet<Page> pages = new HashSet<>();
    private HashSet<Page> visited = new HashSet<>();
    private HashSet<String> links = new HashSet<>();

    public Scraper(String u) { links.add("/wiki/" + u); name = u; }

    void initialize() {
        try {
            int level = 0;
            while (level < 2) {
                scrape();
                getLinks();
                level++;
            }
            write();
        } catch(Exception e) {
            System.out.println(e);
            System.out.println("Something went horribly wrong");
        }
    }

    private void scrape() throws IOException {
        for (String link : links) {
            URL url = new URL("https://en.wikipedia.org" + link);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            pages.add(new Page(link, builder.toString()));
        }
    }

    private void getLinks() throws IOException {
        links.clear();
        for (Page page : pages) {
            if (visited.contains(page)) continue;
            visited.add(page);
            Pattern pattern = Pattern.compile("href=\"(.*?)\"");
            Matcher matcher = pattern.matcher(page.html);
            while (!matcher.hitEnd()) {
                if (matcher.find()) {
                    String link = page.html.substring(matcher.start(), matcher.end());
                    String linkUrl = link.substring(link.indexOf("\"") + 1, link.lastIndexOf("\""));
                    if (checkValidLink(linkUrl)) {
                        page.addLink(linkUrl);
                        links.add(linkUrl);
                    }
                }
            }
        }
    }

    private boolean checkValidLink(String linkUrl) {
        Pattern wikiPattern = Pattern.compile("^\\/wiki\\/[a-zA-z]*");
        Matcher wikiMatcher= wikiPattern.matcher(linkUrl);
        Pattern colonPattern = Pattern.compile(":");
        Matcher colonMatcher= colonPattern.matcher(linkUrl);
        return wikiMatcher.find() && !colonMatcher.find();
    }

    private void write() throws IOException {
        Folder folder = new Folder(name);
        folder.createRoot();
        for (Page page : pages) {
            folder.setup();
            folder.createFiles(page.link, page.html, page.links);
        }
    }

    private class Page {
        String link;
        String html;
        HashSet<String> links = new HashSet<>();

        Page(String l, String h) { link = l; html = h; }
        void addLink(String l) {
            links.add(l);
        }
    }
}
