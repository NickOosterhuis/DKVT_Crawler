package nl.getthere.dkvt_crawler;

import nl.getthere.dkvt_crawler.crawlers.FullNewspaperIdCrawler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DKVTCrawlerApplication {

	public static void main(String[] args) {
		FullNewspaperIdCrawler newspaperCrawler = new FullNewspaperIdCrawler();


		SpringApplication.run(DKVTCrawlerApplication.class, args);
	}


}