package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler {
    private String HTML = "/html";
    private String LINKS = "/links";
    private String NOTAGS = "/noTags";
    private String WORDS = "/words";
    private String ROOT = "data";
    private String name;

    public FileHandler(String n) {
        name = n;
    }

    public void setup() {
        createFolder("");
        createFolder(LINKS);
        createFolder(NOTAGS);
        createFolder(HTML);
        createFolder(WORDS);
    }

    private void createFolder(String type) {
        File folder = new File(ROOT + "/" + name + "/" + type);
        if (!folder.exists()) folder.mkdir();
    }

    public void createRoot() {
        new File(ROOT).mkdir();
    }

    /**
     * Reads all files in html folder
     * @return A list of paths to each file
     * @throws IOException if the structure is malformed
     */
    public List<Path> read() throws IOException {
        return Files.walk(Paths.get(ROOT + "/" + name + HTML))
            .filter(Files::isRegularFile)
            .collect(Collectors.toList());
    }

    public void createHTML(String html, String link) throws IOException {
        createFile(HTML, link, html);
    }

    public void createLink(String links, String link) throws IOException {
        createFile(LINKS, Name.get(link), links);
    }

    public void createNoTags(String html, String link) throws IOException {
        createFile(NOTAGS, Name.get(link), html);
    }

    public void createWordBag(String words, String link) throws IOException {
        createFile(WORDS, Name.get(link), words);
    }

    private void createFile(String type, String category, String content) throws IOException {
        Files.write(Paths.get(ROOT + "/" + name + type + "/" + category), content.getBytes());
    }
}
