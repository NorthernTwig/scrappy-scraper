import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

class Folder {
    private String root = "./data";
    private String name = "";

    Folder(String n) { name = n; }

    void setup() {
        new File(root + "/" + name).mkdir();
        new File(root + "/" + name + "/links").mkdir();
        new File(root + "/" + name + "/words").mkdir();
        new File(root + "/" + name + "/html").mkdir();
    }

    void createRoot() {
        new File(root).mkdir();
    }

    void createFiles(String link, String html, HashSet<String> links) throws IOException {
        String pageName = link.substring(link.lastIndexOf("/"), link.length());
        BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(root + "/" + name + "/html/" + pageName));
        htmlWriter.write(html);
        htmlWriter.close();

        BufferedWriter linkWriter = new BufferedWriter(new FileWriter(root + "/" + name + "/links/" + pageName));
        StringBuilder builder = new StringBuilder();
        for (String s : links) {
            builder.append(s).append("\n");
        }
        linkWriter.write(builder.toString());
        linkWriter.close();
    }
}
