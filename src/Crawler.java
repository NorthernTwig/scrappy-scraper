import utils.FileHandler;

class Crawler {
    static void start(String name) {

        // Creates folder structure for provided category
        FileHandler fileHandler = new FileHandler(name);
        fileHandler.createRoot();
        fileHandler.setup();

        new Scraper(name).initialize();
    }
}
