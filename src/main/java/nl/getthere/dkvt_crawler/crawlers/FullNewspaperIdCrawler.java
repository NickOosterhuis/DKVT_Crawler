package nl.getthere.dkvt_crawler.crawlers;


import nl.getthere.dkvt_crawler.models.NewspaperAbbreviationModel;
import nl.getthere.dkvt_crawler.models.FullNewspaperIdMordel;
import nl.getthere.dkvt_crawler.reposiroties.NewspaperAbbreviationRepository;
import nl.getthere.dkvt_crawler.reposiroties.FullNewspaperIdRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;


import static nl.getthere.dkvt_crawler.crawlers.WebCrawlerConfig.*;

@Component
@Order(2)
public class FullNewspaperIdCrawler {

    @Autowired
    private NewspaperAbbreviationRepository abbreviationRepo;

    @Autowired
    private FullNewspaperIdRepository idRepo;

    private static final Logger logger = LoggerFactory.getLogger(FullNewspaperIdCrawler.class);

    /**
     * Save full newspaper id to database
     */
    //@PostConstruct
    private void saveFullId() {
        Set<String> formattedStrings = formatNewsPaperUrl();

        for (String string : formattedStrings) {
            String[] formatted = string.split("(\\d)");
            String firstElement = formatted[0];

            if(firstElement.charAt(firstElement.length() - 1) == '-') {
                firstElement = firstElement.substring(0, firstElement.length() -1);
                formatted[0] = firstElement;
            }

            FullNewspaperIdMordel newspaperIdModel = new FullNewspaperIdMordel();
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

        List<NewspaperAbbreviationModel> abbreviations = (List<NewspaperAbbreviationModel>) abbreviationRepo.findAll();
        Set<String> urls = new HashSet<>();

        setupDriver();

        for (NewspaperAbbreviationModel abbreviation : abbreviations) {
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
