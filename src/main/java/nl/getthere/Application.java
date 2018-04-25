package nl.getthere;

import nl.getthere.dkvt_crawler.crawlers.*;
import nl.getthere.imageprocessing.matching.OpenCvTest;
import nl.getthere.imageprocessing.matching.KnnOpenCvMatcher;
import nl.getthere.imageprocessing.matching.RGBMatchingAlgorithm;
import nl.getthere.mapstructure.PdfToImg;
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
	private FamAdImageCrawler famAdImageCrawler;

	@Autowired
    private FamAdCrawler famAdCrawler;

	@Autowired
	private LeftOverFamAdCrawler leftOverAdCrawler;

	@Autowired
    private PdfToImg pdfToImg;

	@Autowired
    private RGBMatchingAlgorithm rgbMatchingAlgorithm;

	@Autowired
	private OpenCvTest openCvTest;

	@Autowired
	private KnnOpenCvMatcher knnOpenCvMatcher;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

//	@Bean
//	public CommandLineRunner startCrawling() {
//		return (args) -> {
//			//abbreviationCrawler.crawl();
//			//newspaperIdCrawler.crawl();
//            //famAdCrawler.crawl();
//			//leftOverAdCrawler.crawl();
//			//famAdImageCrawler.crawl();
//		};
//	}

//	@Bean
//    public CommandLineRunner createMapStructure() {
//	    return(args) -> {
//              pdfToImg.makeMapStructure();
//        };
//    }

//    @Bean
//    public CommandLineRunner bruteForceMatching() {
//	    return(args) -> {
//			rgbMatchingAlgorithm.match();
//        };
//    }

//	@Bean
//	public CommandLineRunner openCvMatching() {
//		return(args) -> {
//			openCvTest.match();
//		};
//	}

	@Bean
	public CommandLineRunner openCvMatching1() {
		return(args) -> {
			knnOpenCvMatcher.match();
		};
	}
}