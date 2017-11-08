import utils.Cleaner;
import utils.FileHandler;
import utils.HTTP;
import utils.Name;

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
            try {
                scrape();
                readFiles();
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * Scrapes the supplied links
     * @throws IOException if folder structure is malformed
     */
    private void scrape() throws IOException {
        level++;
        for (String link : links) {
            try {
                if (!visited.contains(link)) {
                    String name = Name.get(link);
                    if (name.length() <= 1) throw new FileNotFoundException();
                    String html = HTTP.get(link);
                    fileHandler.createHTML(html, name);
                    visited.add(link);
                    generateFiles(html, name);
                    displayProgress();
                }
            } catch(FileNotFoundException e) {

                // If wikipedia removes a page - status 404 will be returned
                String name = Name.get(e.getMessage());
                System.out.println("Could not find page: " + name);
            }
        }
    }

    /**
     * Generates all necessary files
     * @param html A text blob representing html
     * @param name The category
     * @throws IOException if folder structure is malformed
     */
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

    /**
     * Reads all files in the HTML folder with the supplied category name
     * @throws IOException If there is something wrong with the structure
     */
    private void readFiles() throws IOException {
        List<Path> filePaths = fileHandler.read();

        for (Path filePath : filePaths) {
            if (!visited.contains(filePath.toString())) {
                visited.add(filePath.toString());
                addLinksToVisit(filePath);
            }
        }
    }

    /**
     * Adds new links to visit from read file
     * @param filePath is the path to a file containing html
     * @throws IOException is there is something wrong with the structure
     */
    private void addLinksToVisit(Path filePath) throws IOException {
        String html = Files.readAllLines(filePath).toString();
        HashSet<String> hashLinks = Cleaner.getHashLinks(html);
        links.addAll(hashLinks);
    }
}
