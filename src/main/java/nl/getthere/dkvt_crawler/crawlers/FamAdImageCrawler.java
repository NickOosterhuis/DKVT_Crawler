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
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;

import static nl.getthere.dkvt_crawler.crawlers.WebCrawlerConfig.*;

/**
 * @author Nick Oosterhuis
 */
@Component
@Order(4)
public class FamAdImageCrawler {

    @Autowired
    FamPageRepository famPageRepo;

    private static final Logger logger = LoggerFactory.getLogger(FamAdImageCrawler.class);

    /**
     * Crawler method for getting the fam ads JPG url's
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

            String imgLink = image.getAttribute("src");

            imageModel.setUrl(imgLink);
            famAd.getFamAdPropertyModel().setImage(imageModel);

//            logger.info("Image saved: fam ad =  " + famAd.getName() + ", with url = " + famAd.getFamAdPropertyModel().getImage().getUrl());
//            downloadImage(imgLink, famAd.getName());
//            famPageRepo.save(famAd);

            if(!isDuplicate(famAd)) {
                logger.info("Image saved: fam ad =  " + famAd.getName() + ", with url = " + famAd.getFamAdPropertyModel().getImage().getUrl());
                downloadImage(imgLink, famAd.getName());
                famPageRepo.save(famAd);
            }
            logger.info("DB: Image " + famAd.getName() + " already exists in database.");
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

        return driver.findElement(By.tagName("img"));
    }

    /**
     * Download the images to a specific folder on the computer
     *
     * @param imgLink url of the image
     * @param name of the advertisement
     */
    private void downloadImage(String imgLink, String name) {

        try{
            URL img = new URL(imgLink);
            BufferedImage saveImage = ImageIO.read(img);

            ImageIO.write(saveImage, "jpg", new File("D:\\DKVT_IMGS\\" + name + ".jpg"));
            logger.info("DOWNLOAD: Download completed image url = " + imgLink + ", Image Name = " + name);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if model is already exists in database
     * @param model of fam images
     * @return boolean
     */
    private boolean isDuplicate(FamAdPageModel model) {
        List<FamAdPageModel> models = (List<FamAdPageModel>) famPageRepo.findAll();

        for(FamAdPageModel c : models) {
            if(model.getFamAdPropertyModel().getImage().equals(c.getFamAdPropertyModel().getImage()))
                return true;
        }
        return false;
    }
}
