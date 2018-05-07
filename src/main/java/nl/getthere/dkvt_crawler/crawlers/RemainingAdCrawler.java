package nl.getthere.dkvt_crawler.crawlers;

import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import nl.getthere.imageprocessing.models.NDCModel;
import nl.getthere.imageprocessing.repositories.NDCRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nl.getthere.dkvt_crawler.crawlers.WebCrawlerConfig.quitDriver;
import static nl.getthere.dkvt_crawler.crawlers.WebCrawlerConfig.setupDriver;

/**
 * Class to crawl the famAds which weren't able to be crawled in the FamAdCrawler
 *
 * @author Nick Oosterhuis
 */
@Component
public class RemainingAdCrawler {

    @Autowired
    private NDCRepository ndcRepository;

    @Autowired
    private FamAdCrawler famAdCrawler;

    private static final Logger logger = LoggerFactory.getLogger(RemainingAdCrawler.class);

    public void crawl() {
        Set<String> famAdsUrls = getFamAdsUrls();
        setupDriver();

        for(String url : famAdsUrls) {
            famAdCrawler.scanForFamAdverts(url);
        }
        quitDriver();
    }

    private Set<String> getFamAdsUrls() {

        List<NDCModel> ndcModels = ndcRepository.findAll();
        Format formatter = new SimpleDateFormat("YYYMMdd");
        Set<String> urls = new HashSet<>();

        for (NDCModel ndcModel : ndcModels) {
            String abbreviation = ndcModel.getEditionCode();
            String date = formatter.format(ndcModel.getPublicationDate());
            int pageNumber = ndcModel.getPlacedOnPage();
            String pageNumberString  = String.format("%03d", pageNumber);

            String mapStructure = "D:\\FamAds\\" + abbreviation + "\\" + date + "\\" + pageNumberString + "\\" + "Krant Van Toen";
            File folder = new File(mapStructure);

            String newAbbreviation = famAdCrawler.formatAbbreviations(abbreviation);

            if (!folder.exists()) {
                logger.info("Folder doesn't exist: " + folder.getAbsolutePath());

                String url = "https://www.dekrantvantoen.nl/vw/page.do?id=" + newAbbreviation + "-" + "01" + "-" +
                        pageNumberString + "-" +
                        date + "&ed=00&v2=true";
                urls.add(url);
            } else {
                logger.info("Folder already exists: " + mapStructure);
            }
        }
        return urls;
    }
}
