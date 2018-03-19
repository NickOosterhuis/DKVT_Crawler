package nl.getthere.dkvt_crawler.crawlers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author Nick Oosterhuis
 */
public class WebCrawlerConfig {

    public static WebDriver driver;

    public static void setupDriver() {
        System.setProperty("webdriver.chrome.driver", "D:\\Chrome driver\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    public static void setBaseUrl(String url) {
        driver.get(url);
    }

    public static void quitDriver() {
        driver.quit();
    }
}
