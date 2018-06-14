package nl.getthere;

import nl.getthere.dkvt_crawler.crawlers.FamAdCrawler;
import nl.getthere.dkvt_crawler.crawlers.FamAdImageCrawler;
import nl.getthere.dkvt_crawler.crawlers.NewspaperAbbreviationCrawler;
import nl.getthere.dkvt_crawler.crawlers.NewspaperIdCrawler;
import nl.getthere.helpers.FormatNdcData;
import nl.getthere.helpers.PdfToImgConvertor;
import nl.getthere.imageprocessing.matching.KnnOpenCvMatcher;
import nl.getthere.imageprocessing.matching.MatchManualMaterialId;
import nl.getthere.imageprocessing.matching.RGBMatchingAlgorithm;
import nl.getthere.imageprocessing.matching.RemainingMatches;
import nl.getthere.svggenerator.GenerateSvgFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.Scanner;

@org.springframework.stereotype.Controller
public class ConsoleApp implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleApp.class);

    @Autowired
    private NewspaperAbbreviationCrawler abbreviationCrawler;

    @Autowired
    private NewspaperIdCrawler newspaperIdCrawler;

    @Autowired
    private FamAdCrawler famAdCrawler;

    @Autowired
    private FamAdImageCrawler famAdImageCrawler;

    @Autowired
    private PdfToImgConvertor pdfToImg;

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

    @Autowired
    private GenerateSvgFiles generateSvgFiles;

    @Override
    public void run(String... args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            logger.info("Please type the corresponding number to start an application part: ");
            logger.info("1 = Crawler Menu");
            logger.info("2 = Convert pdf to image and make folder structure");
            logger.info("3 = Matching Menu");
            logger.info("4 = Generate SVG files");
            logger.info("q = quit app");

            String text = scanner.nextLine();

            switch (text) {
                case "1": crawlerMenu();
                    break;
                case "2": pdfToImg.makeDirectoryStructure();
                    break;
                case "3": matcherMenu();
                    break;
                case "4": generateSvgFiles.listSubDirectories("D:\\FamAds"); //set to root location of the familyAd collection
                    break;
                case "q": System.exit(404);
                default: run();
            }
        }
    }

    private void crawlerMenu() throws Exception {
        try(Scanner scanner = new Scanner(System.in)) {
            logger.info("Please type the corresponding number to crawl dekrantvantoen.nl");
            logger.info("1 = Abbreviations");
            logger.info("2 = Full abbreviations");
            logger.info("3 = Family Advertisements");
            logger.info("4 = Images");
            logger.info("q = back");

            String text = scanner.nextLine();

            switch (text) {
                case "q": run();
                    break;
                case "1": abbreviationCrawler.crawl();
                    break;
                case "2": newspaperIdCrawler.crawl();
                    break;
                case "3": famAdCrawler.crawl();
                    break;
                case "4": famAdImageCrawler.crawl();
                    break;
                default: crawlerMenu();
            }
        }
    }

    private void matcherMenu() throws Exception {
        try(Scanner scanner = new Scanner(System.in)) {
            logger.info("Please type the corresponding number to match images");
            logger.info("1 = Bruteforce matching");
            logger.info("2 = Feature matching");
            logger.info("3 = Format matched data");
            logger.info("4 = Show unmatched family advertisements");
            logger.info("5 = Complete data of manual matched images");
            logger.info("q = back");

            String text = scanner.nextLine();

            switch (text) {
                case "q": run();
                    break;
                case "1": rgbMatchingAlgorithm.match();
                    break;
                case "2": knnOpenCvMatcher.match();
                    break;
                case "3": formatNdcData.formatData();
                    break;
                case "4": remainingMatches.match();
                    break;
                case "5": manualMatcher.match();
                    break;
                default: matcherMenu();
                    break;
            }
        }
    }
}
