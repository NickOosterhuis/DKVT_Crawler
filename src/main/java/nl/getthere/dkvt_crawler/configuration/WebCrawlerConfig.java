package nl.getthere.dkvt_crawler.configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Configuration class of the webcrawler for convenience
 *
 * @author Nick Oosterhuis
 */
public class WebCrawlerConfig {

    public static WebDriver driver;
    public static ChromeOptions options;

    public static void setupDriver() {

        //set path to chrome driver location on disk
        System.setProperty("webdriver.chrome.driver", "D:\\Chrome driver\\chromedriver.exe");
        options = new ChromeOptions();
        options.addArguments("--headless");

        driver = new ChromeDriver(options);
    }

    public static void setBaseUrl(String url) {
        driver.get(url);
    }

    public static void quitDriver() {
        driver.quit();
    }
}
