package nl.getthere.dkvt_crawler.crawlers;

import nl.getthere.dkvt_crawler.models.NewspaperAbbreviationModel;
import nl.getthere.dkvt_crawler.reposiroties.NewspaperAbbreviationRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

import static nl.getthere.dkvt_crawler.constants.NewspapersToDelete.newspapersToDelete;
import static nl.getthere.dkvt_crawler.crawlers.WebCrawlerConfig.*;

/**
 * @author Nick Oosterhuis
 */
@Component
@Order(1)
public class NewspaperAbbreviationCrawler {

    @Autowired
    private NewspaperAbbreviationRepository repo;

    private static final Logger logger = LoggerFactory.getLogger(NewspaperAbbreviationCrawler.class);

    /**
     * Method to crawl the dropdown menu of newspapers
     * @return List of Newspaper types
     */
    //@PostConstruct
    public void crawlAbbreviations() {
        setupDriver();
        String url = "https://www.dekrantvantoen.nl/vw/edition.do?dp=NVHN&altd=true&date=20180207&ed=00&v2=true";
        setBaseUrl(url);

        WebElement newspaperSelect = driver.findElement(By.id("sel_pub"));

        List<WebElement> newspaperTypes = newspaperSelect.findElements(By.tagName("option"));

        for(WebElement newspaperAbbreviation : newspaperTypes) {
            String abbreviation = newspaperAbbreviation.getAttribute("value");
            NewspaperAbbreviationModel newspaper = new NewspaperAbbreviationModel();
            newspaper.setNewspaperName(abbreviation);

            if(!isDuplicate(newspaper)) {
                repo.save(newspaper);
                logger.info("Abbreviation: " + abbreviation);
            }
        }
        deleteAbbreviations();
        quitDriver();
    }

    /**
     * Method to delete the newspapers which are too old from the DB
     */
    public void deleteAbbreviations() {
        List<String> abbreviations = newspapersToDelete();

        for (String abbreviation : abbreviations) {
            NewspaperAbbreviationModel model = repo.findByNewspaperName(abbreviation);
            repo.delete(model);
        }
    }

    /**
     * Checks if model is already in database
     * @param model of fam adverts
     * @return boolean
     */
    private boolean isDuplicate(NewspaperAbbreviationModel model) {
        List<NewspaperAbbreviationModel> models = (List<NewspaperAbbreviationModel>) repo.findAll();

        for(NewspaperAbbreviationModel c : models) {
            if(model.getNewspaperName().equals(c.getNewspaperName()))
                return true;
        }
        return false;
    }
}
