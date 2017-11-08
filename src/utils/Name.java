package utils;

public class Name {
    /**
     * Gets the last part from url. Ex. en.wikipedia.org/wiki/*this_part*
     * @param url To convert to name
     * @return The category name
     */
    public static String get(String url) {
        return url.substring(url.lastIndexOf("/"), url.length());
    }
}
