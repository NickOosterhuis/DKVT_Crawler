package nl.getthere.dkvt_crawler;

import nl.getthere.dkvt_crawler.crawlers.NewspaperIdCrawler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DKVTCrawlerApplication {

	public static void main(String[] args) {
		NewspaperIdCrawler newspaperCrawler = new NewspaperIdCrawler();


		SpringApplication.run(DKVTCrawlerApplication.class, args);
	}


}