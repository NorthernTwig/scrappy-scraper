import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

class Scraper {
    private int MAX_LEVEL = 1;
    private FileHandler fileHandler;
    private HashSet<String> visited;
    private HashSet<String> links;
    private int level;
    private int amount = 0;

    Scraper(String subUrl) {
        fileHandler = new FileHandler(subUrl);
        links = new HashSet<>();
        visited = new HashSet<>();
        level = 0;
        links.add("/wiki/" + subUrl);
    }

    void initialize() {
        while (level <= MAX_LEVEL) {
            level++;
            try {
                scrape();
                readFiles();
            } catch(Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Something went horribly wrong");
            }
        }
    }

    private void scrape() throws IOException {
        for (String link : links) {
            try {
                if (!visited.contains(link)) {
                    String name = link.substring(link.lastIndexOf("/"), link.length());
                    if (name.length() <= 1) throw new FileNotFoundException();
                    String html = HTTP.get(link);
                    fileHandler.createHTML(html, name);
                    visited.add(link);
                    generateFiles(html, name);
                    displayProgress();
                }
            } catch(FileNotFoundException e) {
                String name = e.getMessage().substring(e.getMessage().lastIndexOf("/"), e.getMessage().length());
                System.out.println("Could not find page: " + name);
            }
        }
    }

    private void generateFiles(String html, String name) throws IOException {
        String htmlRender = Cleaner.preprocessText(Cleaner.getHtmlRender(html));
        String words = Cleaner.getWords(htmlRender);
        HashSet<String> hashLinks = Cleaner.getHashLinks(html);
        String subLinks = Cleaner.getLinks(hashLinks);
        fileHandler.createNoTags(htmlRender, name);
        fileHandler.createWordBag(words, name);
        fileHandler.createLink(subLinks, name);
    }

    private void displayProgress() {
        System.out.println("Searched: " + amount++ + " of " + links.size() + " on level: " + level);
    }

    private void readFiles() throws IOException {
        List<Path> filePaths = fileHandler.read();

        for (Path filePath : filePaths) {
            if (!visited.contains(filePath.toString())) {
                visited.add(filePath.toString());
                addLinksToVisit(filePath);
            }
        }
    }

    private void addLinksToVisit(Path filePath) throws IOException {
        String html = Files.readAllLines(filePath).toString();
        HashSet<String> hashLinks = Cleaner.getHashLinks(html);
        links.addAll(hashLinks);
    }
}
