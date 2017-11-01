import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Scraper {
    public void initialize() {
        try {
            scrape();
            cleanUp();
        } catch(Exception e) {
            System.out.println("Something went horribly wrong");
        }
    }

    private void scrape() throws IOException {
        String path = "/wiki/Woodworking";
        URL url = new URL("https://en.wikipedia.org" + path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    private void cleanUp() {
        // clean up scraped contents
    }
}
