package nl.getthere;

import nl.getthere.dkvt_crawler.crawlers.FamAdImageCrawler;
import nl.getthere.dkvt_crawler.crawlers.FullNewspaperIdCrawler;
import nl.getthere.dkvt_crawler.crawlers.NewspaperAbbreviationCrawler;
import nl.getthere.dkvt_crawler.crawlers.NewspaperScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Nick Oosterhuis
 */
@SpringBootApplication
public class Application {

	@Autowired
	private NewspaperAbbreviationCrawler abbreviationCrawler;

	@Autowired
	private FullNewspaperIdCrawler newspaperIdCrawler;

	@Autowired
	private NewspaperScanner newspaperScanner;

	@Autowired
	private FamAdImageCrawler famAdImageCrawler;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Bean
	public CommandLineRunner startCrawling() {
		return (args) -> {
			//abbreviationCrawler.crawl();
			//newspaperIdCrawler.crawl();
			newspaperScanner.crawl();
			//famAdImageCrawler.crawl();
		};

	}
}