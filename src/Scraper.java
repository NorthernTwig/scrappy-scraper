import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Scraper {
    private String text;
    private HashSet<String> links = new HashSet<>();
    private int amount = 0;

    public Scraper(String u) { links.add(u); }

    void initialize() {
        try {
            int level = 0;
            while (level < 1) {
                scrape();
                getLinks();
                level++;
            }
            cleanUp();
        } catch(Exception e) {
            System.out.println("Something went horribly wrong");
        }
    }

    private void scrape() throws IOException {
        text = "";
        for (String link : links) {
            URL url = new URL("https://en.wikipedia.org" + link);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            text += builder.toString();
        }
    }

    private void getLinks() {
        Pattern pattern = Pattern.compile("href=\"(.*?)\"");
        Matcher matcher = pattern.matcher(text);
        while (!matcher.hitEnd()) {
            if (matcher.find()) {
                String link = text.substring(matcher.start(), matcher.end());
                String linkUrl = link.substring(link.indexOf("\"") + 1, link.lastIndexOf("\""));
                if (checkValidLink(linkUrl)) {
                    amount++;
                    links.add(linkUrl);
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

    private void cleanUp() {
        System.out.println(links.toString());
        System.out.println(amount);
    }
}
