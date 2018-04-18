package nl.getthere.dkvt_crawler.crawlers;

import javafx.scene.transform.Scale;
import nl.getthere.dkvt_crawler.models.FamAdPageModel;
import nl.getthere.dkvt_crawler.models.ImageModel;
import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import nl.getthere.imageprocessing.matching.RGBMatchingAlgorithm;
import nl.getthere.imageprocessing.repositories.NDCRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
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
    private FamAdRepository famPageRepo;

    private static final Logger logger = LoggerFactory.getLogger(FamAdImageCrawler.class);

    /**
     * Crawler method for getting the fam ads JPG url's
     */
    public void crawl() {
        setupDriver();

        List<FamAdPageModel> famAds = famPageRepo.findAll();

        for(FamAdPageModel famAd : famAds) {

            String url = "https://www.dekrantvantoen.nl/vw/article.do?code=" + famAd.getNewspaperAbbreviation()+ "&date="
                    + famAd.getDate()+"&v2=true&id=" + famAd.getName();

            ImageModel imageModel = new ImageModel();

            logger.info("Crawler: Started on url: " + url);
            WebElement image = saveImageUrl(url);

            String imgLink = image.getAttribute("src");

            if(!isDuplicate(famAd)) {
                logger.info("Image saved: fam ad =  " + famAd.getName() + ", with url = " + famAd.getFamAdPropertyModel().getImage().getUrl());
                imageModel.setUrl(imgLink);
                famAd.getFamAdPropertyModel().setImage(imageModel);
                downloadImage(famAd);
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
    public WebElement saveImageUrl(String url) {
        setBaseUrl(url);
        WebElement iFrame = driver.findElement(By.name("art_content"));
        driver.switchTo().frame(iFrame);

        return driver.findElement(By.tagName("img"));
    }

    /**
     * Download the images to a specific folder on the computer
     *
     * @param famAd model
     */
    public void downloadImage(FamAdPageModel famAd) {

        String imgLink = famAd.getFamAdPropertyModel().getImage().getUrl();
        String name = famAd.getName();
        String abbreviation = famAd.getNewNewspaperAbbreviation();
        String pageNumber = famAd.getPageNumber();
        String date = famAd.getDate();

        try{
            File dir = new File("D:\\FamAds\\" + abbreviation + "\\" + date +  "\\" + pageNumber + "\\Krant Van Toen");

            if(!dir.exists())
                dir.mkdirs();

            String fileName = dir + "\\" + name + ".jpg";
            File image = new File(fileName);

            if(!image.exists()) {
                URL img = new URL(imgLink);
                BufferedImage crawledImage = ImageIO.read(img);
                BufferedImage croppedImage = cropImage(crawledImage, name);
                ImageIO.write(croppedImage, "jpg", new File(fileName));
                logger.info("DOWNLOAD: Download completed image url = " + imgLink + ", Image Name = " + name + ".jpg");
            } else {
                logger.info("DOWNLOAD: Image already downloaded... Skipping...");
            }
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
        List<FamAdPageModel> models = famPageRepo.findAll();

        String abbreviation = model.getNewNewspaperAbbreviation();
        String date = model.getDate();
        String pageNumber = model.getPageNumber();
        String name = model.getName();

        File dir = new File("D:\\FamAds\\" + abbreviation + "\\" + date +  "\\" + pageNumber + "\\Krant Van Toen" + "\\" + name + ".jpg");
        logger.info(dir.getAbsolutePath());

        for(FamAdPageModel c : models) {
            if(model.getFamAdPropertyModel().getImage().equals(c.getFamAdPropertyModel().getImage()) && dir.exists())
                return true;
        }
        return false;
    }

    /**
     * Method to crop the images to their height and width from a constant starting point
     *
     * @param img a crawled img which needs to be cropped
     */
    private BufferedImage cropImage(BufferedImage img, String name) {

        logger.info("CROPPING: Image cropped!");

        img = img.getSubimage(0,25, img.getWidth(), img.getHeight() - 25);

        int heightToTrim = getTrimmedHeight(img);
        int widthToTrim = getTrimmedWidth(img);


        img = img.getSubimage(30,30, widthToTrim, heightToTrim);

        heightToTrim = getTrimmedHeight(img);
        widthToTrim = getTrimmedWidth(img);
        img = img.getSubimage(0,0, widthToTrim, heightToTrim);

        return img;
    }

    private int getTrimmedWidth(BufferedImage img) {
        int height       = img.getHeight();
        int width        = img.getWidth();
        int trimmedWidth = 0;

        for(int i = 0; i < height; i++) {
            for(int j = width - 1; j >= 0; j--) {
                if(img.getRGB(j, i) != Color.WHITE.getRGB() &&
                        j > trimmedWidth) {
                    trimmedWidth = j;
                    break;
                }
            }
        }
        return trimmedWidth;
    }

    private int getTrimmedHeight(BufferedImage img) {
        int width         = img.getWidth();
        int height        = img.getHeight();
        int trimmedHeight = 0;

        for(int i = 0; i < width; i++) {
            for(int j = height - 1; j >= 0; j--) {
                if(img.getRGB(i, j) != Color.WHITE.getRGB() &&
                        j > trimmedHeight) {
                    trimmedHeight = j;
                    break;
                }
            }
        }
        return trimmedHeight;
    }

}
