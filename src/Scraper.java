import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

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
        fileHandler.createRoot();
        fileHandler.setup();
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
                    fileHandler.createHTML(getPage(link), name);
                    visited.add(link);
                    displayProgress();
                }
            } catch(FileNotFoundException exception) {
                System.out.println("Could not find page");
            }
        }
    }

    private void displayProgress() {
        System.out.println("Searched: " + amount++ + " of " + links.size() + " on level: " + level);
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

    private void readFiles() throws IOException {
        List<Path> filePaths = fileHandler.read();

        for (Path filePath : filePaths) {
            if (!visited.contains(filePath.toString())) {
                visited.add(filePath.toString());
                generateFiles(filePath);
            }
        }
    }

    private void generateFiles(Path filePath) throws IOException {
        String html = Files.readAllLines(filePath).toString();
        HashSet<String> hashLinks = Cleaner.getHashLinks(html);
        String htmlRender = Cleaner.preprocessText(Cleaner.getHtmlRender(html));
        String subLinks = Cleaner.getLinks(hashLinks);
        String words = Cleaner.getWords(htmlRender);
        addLinksToVisit(hashLinks);

        fileHandler.createNoTags(htmlRender, filePath.toString());
        fileHandler.createWordBag(words, filePath.toString());
        fileHandler.createLink(subLinks, filePath.toString());
    }

    private void addLinksToVisit(HashSet<String> hashLinks) {
        links.addAll(hashLinks);
    }
}
