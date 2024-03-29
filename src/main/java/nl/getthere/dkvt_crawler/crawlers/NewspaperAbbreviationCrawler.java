package nl.getthere.dkvt_crawler.crawlers;

import nl.getthere.dkvt_crawler.models.AbbreviationModel;
import nl.getthere.dkvt_crawler.repositories.NewspaperAbbreviationRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static nl.getthere.dkvt_crawler.constants.NewspapersToDelete.newspapersToDelete;
import static nl.getthere.dkvt_crawler.configuration.WebCrawlerConfig.*;

/**
 * Class to save and crawl the abbreviations of every specified newspaper
 *
 * @author Nick Oosterhuis
 */
@Component
public class NewspaperAbbreviationCrawler {

    @Autowired
    private NewspaperAbbreviationRepository repo;

    private static final Logger logger = LoggerFactory.getLogger(NewspaperAbbreviationCrawler.class);

    /**
     * Method to crawl the dropdown menu of newspapers
     * @return List of Newspaper types
     */
    public void crawl() {
        setupDriver();
        String url = "https://www.dekrantvantoen.nl/vw/edition.do?dp=NVHN&altd=true&date=20180207&ed=00&v2=true";
        setBaseUrl(url);

        WebElement newspaperSelect = driver.findElement(By.id("sel_pub"));

        List<WebElement> newspaperTypes = newspaperSelect.findElements(By.tagName("option"));

        for(WebElement newspaperAbbreviation : newspaperTypes) {
            String abbreviation = newspaperAbbreviation.getAttribute("value");
            AbbreviationModel newspaper = new AbbreviationModel();
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
    private void deleteAbbreviations() {
        List<String> abbreviations = newspapersToDelete();

        for (String abbreviation : abbreviations) {
            AbbreviationModel model = repo.findByNewspaperName(abbreviation);
            repo.delete(model);
        }
    }

    /**
     * Checks if model is already in database
     * @param model of fam adverts
     * @return boolean
     */
    private boolean isDuplicate(AbbreviationModel model) {
        List<AbbreviationModel> models = repo.findAll();

        for(AbbreviationModel c : models) {
            if(model.getNewspaperName().equals(c.getNewspaperName()))
                return true;
        }
        return false;
    }
}
