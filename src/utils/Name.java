package utils;

public class Name {
    public static String get(String url) {
        return url.substring(url.lastIndexOf("/"), url.length());
    }
}
