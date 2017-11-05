import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

class Folder {
    private String root = "./data";
    private String name = "";

    Folder(String n) {
        name = n;
    }

    void setup() {
        new File(root + "/" + name).mkdir();
        new File(root + "/" + name + "/links").mkdir();
        new File(root + "/" + name + "/noTags").mkdir();
        new File(root + "/" + name + "/html").mkdir();
    }

    void createRoot() {
        new File(root).mkdir();
    }

    List<Path> readFiles() throws IOException {
        return Files.walk(Paths.get(root + "/" + name + "/html"))
            .filter(Files::isRegularFile)
            .collect(Collectors.toList());
    }

    void createHTML(String html, String link) throws IOException {
        Files.write(Paths.get(root + "/" + name + "/html/" + link), html.getBytes());
    }

    void createLink(String links, String link) throws IOException {
        String pageName = link.substring(link.lastIndexOf("/"), link.length());
        Files.write(Paths.get(root + "/" + name + "/links/" + pageName), links.getBytes());
    }

    void createNoTags(String html, String link) throws IOException {
        Source source = new Source(html);
        Renderer render = new Renderer(source);
        Files.write(Paths.get(root + "/" + name + "/noTags/" + link), render.toString().getBytes());
    }
}
