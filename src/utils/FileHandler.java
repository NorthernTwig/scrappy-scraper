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

    public List<Path> read() throws IOException {
        return Files.walk(Paths.get(ROOT + "/" + name + HTML))
            .filter(Files::isRegularFile)
            .collect(Collectors.toList());
    }

    public void createHTML(String html, String link) throws IOException {
        Files.write(Paths.get(ROOT + "/" + name + HTML + "/" + link), html.getBytes());
    }

    public void createLink(String links, String link) throws IOException {
        String pageName = link.substring(link.lastIndexOf("/"), link.length());
        Files.write(Paths.get(ROOT + "/" + name + LINKS + "/" + pageName), links.getBytes());
    }

    public void createNoTags(String html, String link) throws IOException {
        String pageName = link.substring(link.lastIndexOf("/"), link.length());
        Files.write(Paths.get(ROOT + "/" + name + NOTAGS + "/" + pageName), html.getBytes());
    }

    public void createWordBag(String words, String link) throws IOException {
        String pageName = link.substring(link.lastIndexOf("/"), link.length());
        Files.write(Paths.get(ROOT + "/" + name + WORDS + "/" + pageName), words.getBytes());
    }
}
