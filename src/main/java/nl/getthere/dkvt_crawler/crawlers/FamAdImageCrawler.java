package nl.getthere.dkvt_crawler.crawlers;

import nl.getthere.dkvt_crawler.models.FamAdPageModel;
import nl.getthere.dkvt_crawler.models.ImageModel;
import nl.getthere.dkvt_crawler.reposiroties.FamPageRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nl.getthere.dkvt_crawler.crawlers.WebCrawlerConfig.*;

@Component
@Order(4)
public class FamAdImageCrawler {

    @Autowired
    FamPageRepository famPageRepo;

    private static final Logger logger = LoggerFactory.getLogger(FamAdImageCrawler.class);

    /**
     * Crawler method for getting the famad JPG url's
     */
    @PostConstruct
    private void crawl() {
        setupDriver();

        List<FamAdPageModel> famAds = (List<FamAdPageModel>) famPageRepo.findAll();

        for(FamAdPageModel famAd : famAds) {

            String url = "https://www.dekrantvantoen.nl/vw/article.do?code=" + famAd.getNewspaperAbbreviation()+ "&date="
                    + famAd.getDate()+"&v2=true&id=" + famAd.getName();

            ImageModel imageModel = new ImageModel();

            logger.info("Crawler: Started on url: " + url);
            WebElement image = saveImageUrl(url);
            imageModel.setUrl(image.getAttribute("src"));
            famAd.getFamAdPropertyModel().setImage(imageModel);

            logger.info("Image saved: fam ad =  " + famAd.getName() + ", with url = " + famAd.getFamAdPropertyModel().getImage().getUrl());
            famPageRepo.save(famAd);
        }
        quitDriver();
    }

    /**
     * Save image url to DB
     *
     * @param url of the family advert that's being crawled
     * @return WebElement with the contents of the image url
     */
    private WebElement saveImageUrl(String url) {
        setBaseUrl(url);
        WebElement iFrame = driver.findElement(By.name("art_content"));
        driver.switchTo().frame(iFrame);
        WebElement img = driver.findElement(By.tagName("img"));

        return img;
    }
}
