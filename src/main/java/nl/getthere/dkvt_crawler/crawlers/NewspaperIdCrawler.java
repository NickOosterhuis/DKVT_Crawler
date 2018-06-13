package nl.getthere.dkvt_crawler.crawlers;


import nl.getthere.dkvt_crawler.models.AbbreviationModel;
import nl.getthere.dkvt_crawler.models.FullNewspaperIdModel;
import nl.getthere.dkvt_crawler.repositories.NewspaperAbbreviationRepository;
import nl.getthere.dkvt_crawler.repositories.FullNewspaperIdRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;


import static nl.getthere.dkvt_crawler.crawlers.WebCrawlerConfig.*;

/**
 * Class to gather the full id of every newspaper, these are saved into the DB
 *
 * @author Nick Oosterhuis
 */
@Component
@Order(2)
public class NewspaperIdCrawler {

    @Autowired
    private NewspaperAbbreviationRepository abbreviationRepo;

    @Autowired
    private FullNewspaperIdRepository idRepo;

    private static final Logger logger = LoggerFactory.getLogger(NewspaperIdCrawler.class);

    /**
     * Save full newspaper id to database
     */
    public void crawl() {
        Set<String> formattedStrings = formatNewsPaperUrl();

        for (String string : formattedStrings) {
            String[] formatted = string.split("(\\d)");
            String firstElement = formatted[0];

            if(firstElement.charAt(firstElement.length() - 1) == '-') {
                firstElement = firstElement.substring(0, firstElement.length() -1);
                formatted[0] = firstElement;
            }

            FullNewspaperIdModel newspaperIdModel = new FullNewspaperIdModel();
            newspaperIdModel.setName(firstElement);

            logger.info("Crawled newspaper is: " + firstElement);
            idRepo.save(newspaperIdModel);
        }
    }

    /**
     * Crawl all image urls to split on full newapaper id
     * @return Set of urls
     */
    private Set<String> crawlNextUrl() {

        List<AbbreviationModel> abbreviations = abbreviationRepo.findAll();
        Set<String> urls = new HashSet<>();

        setupDriver();

        for (AbbreviationModel abbreviation : abbreviations) {
            String baseUrl = "https://www.dekrantvantoen.nl/vw/edition.do?dp=" + abbreviation.getNewspaperName() + "&altd=true&date=20180210&ed=00&v2=true";
            setBaseUrl(baseUrl);

            boolean isPresent = driver.findElements(By.id("pageImage")).size() > 0;
            if(isPresent) {
                WebElement element = driver.findElement(By.id("pageImage"));
                String link = element.getAttribute("src");
                urls.add(link);
            }
        }
        quitDriver();
        return urls;
    }

    /**
     * Format the url's form the list to gather the full newspaper ids
     * @return Set of formatted newspaper image urls
     */
    private Set<String> formatNewsPaperUrl() {
        Set<String> urls = crawlNextUrl();
        Set<String> formattedStrings = new HashSet<>();

        for (String url : urls) {

            String[] formatted = url.split("/" );
            String lastElement = formatted[formatted.length-1];
            formattedStrings.add(lastElement);
        }
        return formattedStrings;
    }
}
