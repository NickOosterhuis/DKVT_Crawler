package nl.getthere;

import javafx.scene.web.HTMLEditorSkin;
import nl.getthere.dkvt_crawler.crawlers.*;
import nl.getthere.helpers.FormatNdcData;
import nl.getthere.imageprocessing.matching.KnnOpenCvMatcher;
import nl.getthere.imageprocessing.matching.MatchManualMaterialId;
import nl.getthere.imageprocessing.matching.RGBMatchingAlgorithm;
import nl.getthere.helpers.PdfToImg;
import nl.getthere.imageprocessing.matching.RemainingMatches;
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
	private NewspaperIdCrawler newspaperIdCrawler;

	@Autowired
	private FamAdImageCrawler famAdImageCrawler;

	@Autowired
    private FamAdCrawler famAdCrawler;

	@Autowired
	private RemainingAdCrawler remainingAdCrawler;

	@Autowired
    private PdfToImg pdfToImg;

	@Autowired
    private RGBMatchingAlgorithm rgbMatchingAlgorithm;

	@Autowired
	private KnnOpenCvMatcher knnOpenCvMatcher;

	@Autowired
	private FormatNdcData formatNdcData;

	@Autowired
	private RemainingMatches remainingMatches;

	@Autowired
	private MatchManualMaterialId manualMatcher;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

//	@Bean
//	public CommandLineRunner startCrawling() {
//		return (args) -> {
//			abbreviationCrawler.crawl();
//			newspaperIdCrawler.crawl();
//            famAdCrawler.crawl();
//			remainingAdCrawler.crawl();
//			famAdImageCrawler.crawl();
//		};
//	}

//	@Bean
//    public CommandLineRunner createFolderStructure() {
//	    return(args) -> pdfToImg.makeFolderStructure();
//    }

//    @Bean
//    public CommandLineRunner bruteForceMatching() {
//	    return(args) -> rgbMatchingAlgorithm.match();
//    }

//	@Bean
//	public CommandLineRunner KnnMatching() {
//		return(args) -> knnOpenCvMatcher.match();
//	}

//	@Bean
//	public CommandLineRunner checkMaterialIds() {
//		return(args) -> remainingMatches.match();
//	}

//	@Bean
//	public CommandLineRunner doManualMatching() {
//		return (args) -> manualMatcher.match();
//	}

	@Bean
	public CommandLineRunner formatNote() {
		return(args) -> formatNdcData.formatData();
	}
}