public class Main {
    public static void main(String[] args) {
        Scraper whiskeyScraper = new Scraper("Whiskey");
        whiskeyScraper.initialize();
        Scraper WoodworkingScraper = new Scraper("Woodworking");
        WoodworkingScraper.initialize();
    }
}
