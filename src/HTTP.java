import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

public class HTTP {
    static String get(String link) throws IOException {
        URL url = new URL("https://en.wikipedia.org" + link);
        Scanner scanner = new Scanner(new InputStreamReader(url.openStream()));
        StringBuilder pageStringBuilder = new StringBuilder();

        while(scanner.hasNextLine()) {
            pageStringBuilder.append(scanner.nextLine());
        }

        scanner.close();

        return pageStringBuilder.toString();
    }
}
