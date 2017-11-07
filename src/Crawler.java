import utils.FileHandler;

class Crawler {
    static void start(String name) {
        FileHandler fileHandler = new FileHandler(name);
        fileHandler.createRoot();
        fileHandler.setup();

        new Scraper(name).initialize();
    }
}
