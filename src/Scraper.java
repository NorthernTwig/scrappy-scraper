import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scraper {
    private HashSet<String> links = new HashSet<>();
    private HashSet<String> visited = new HashSet<>();
    private Folder folder;
    private int amount = 0;

    public Scraper(String u) {
        links.add("/wiki/" + u);
        folder = new Folder(u);
    }

    void initialize() {
        folder.createRoot();
        folder.setup();
        int level = 0;
        while (level <= 2) {
            level++;
            try {
                scrape();
                getLinks();
            } catch(Exception e) {
                System.out.println(e);
                System.out.println("Something went horribly wrong");
            }
        }
    }

    private void scrape() throws IOException {
        for (String link : links) {
            try {
                String name = link.substring(link.lastIndexOf("/"), link.length());
                if (name.length() <= 1) throw new FileNotFoundException();
                String html = getPage(link);
                folder.createHTML(html, name);
                folder.createNoTags(html, name);
                System.out.println("Searched: " + amount++ + " of " + links.size());
                visited.add(link);
            } catch(FileNotFoundException exception) {
                System.out.println("Den fanns inte");
            }
        }
    }

    private String getPage(String link) throws IOException {
        URL url = new URL("https://en.wikipedia.org" + link);
        InputStreamReader reader = new InputStreamReader(url.openStream());
        Scanner scanner = new Scanner(reader);
        StringBuilder pageStringBuilder = new StringBuilder();

        while(scanner.hasNextLine()) {
            pageStringBuilder.append(scanner.nextLine());
        }

        scanner.close();
        reader.close();

        return pageStringBuilder.toString();
    }

    private void getLinks() throws IOException {
        List<Path> filePaths = folder.readFiles();
        Pattern pattern = Pattern.compile("href=\"(.*?)\"");
        for (Path filePath : filePaths) {
            if (!visited.contains(filePath.toString())) {
                visited.add(filePath.toString());
                StringBuilder builder = new StringBuilder();
                Files.lines(filePath).forEach(line -> {
                    if (!visited.contains(line)) {
                        Matcher matcher = pattern.matcher(line);
                        while (matcher.find()) {
                            if (checkValidLink(matcher.group(1))) {
                                builder.append(matcher.group(1)).append("\n");
                                links.add(matcher.group(1));
                            }
                        }
                    }
                });
                folder.createLink(builder.toString(), filePath.toString());
            }
        }
    }

    private boolean checkValidLink(String linkUrl) {
        Matcher wikiPattern = Pattern.compile("^/wiki/..*").matcher(linkUrl);
        return wikiPattern.find() && !linkUrl.contains(":") && !linkUrl.contains("#");
    }
}
