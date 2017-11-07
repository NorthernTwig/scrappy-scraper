import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;
import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Cleaner {
    static String getHtmlRender(String html) {
        Source source = new Source(html);
        Renderer render = new Renderer(source);
        return render.toString();
    }

    static String getWords(String html) throws IOException {
        Pattern pattern = Pattern.compile("[a-zA-Z]+((-)?[a-zA-Z]+)*");
        StringBuilder builder = new StringBuilder();
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            builder.append(matcher.group(0)).append(" ");
        }
        return builder.toString();
    }

    static HashSet<String> getHashLinks(String html) throws IOException {
        Pattern hrefPattern = Pattern.compile("href=\"(.*?)\"");
        HashSet<String> links = new HashSet<>();
        Matcher matcher = hrefPattern.matcher(html);
        while (matcher.find()) {
            if (checkValidLink(matcher.group(1))) {
                links.add(matcher.group(1));
            }
        }
        return links;
    }

    static String getLinks(HashSet<String> links) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (String link : links) {
            builder.append(link).append("\n");
        }
        return builder.toString();
    }

    private static boolean checkValidLink(String linkUrl) {
        Matcher wikiPattern = Pattern.compile("^/wiki/..*").matcher(linkUrl);
        return wikiPattern.find() && !linkUrl.contains(":") && !linkUrl.contains("#");
    }

    static String preprocessText(String text) {
        text = text.toLowerCase();
        text = text.replaceAll("\r", " ");
        text = text.replaceAll("\n", " ");
        text = text.replaceAll("\\<.*?>", "");
        text = text.replaceAll("\\[.*?\\]", "");
        text = text.replaceAll("\\d{4}-\\d{2}-\\d{2}", "");
        text = text.replaceAll("\\.", "");
        text = text.replaceAll(",", "");
        text = text.replaceAll("\\?", "");
        text = text.replaceAll(";", "");
        text = text.replaceAll("\"", "");
        text = text.replaceAll(":", "");
        text = text.replaceAll("\\(", "");
        text = text.replaceAll("\\*", "");
        text = text.replaceAll("_", "");
        text = text.replaceAll("!", "");
        text = text.replaceAll("#", "");
        text = text.replaceAll("\\)", "");

        return text;
    }
}
