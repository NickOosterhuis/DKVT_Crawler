package nl.getthere.dkvt_crawler.crawlers;

import nl.getthere.dkvt_crawler.models.FamAdModel;
import nl.getthere.dkvt_crawler.models.ImageModel;
import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;

import static nl.getthere.dkvt_crawler.crawlers.WebCrawlerConfig.*;

/**
 * Class to gather the saved familyAds form the DB, download the images to disk and crop them to a certain size
 *
 * @author Nick Oosterhuis
 */
@Component
public class FamAdImageCrawler {

    @Autowired
    private FamAdRepository famPageRepo;

    private static final Logger logger = LoggerFactory.getLogger(FamAdImageCrawler.class);

    /**
     * Crawler method for getting the fam ads JPG url's
     */
    public void crawl() {
        setupDriver();

        List<FamAdModel> famAds = famPageRepo.findAll();

        for(FamAdModel famAd : famAds) {

            String url = "https://www.dekrantvantoen.nl/vw/article.do?code=" + famAd.getAbbreviation()+ "&date="
                    + famAd.getDate()+"&v2=true&id=" + famAd.getName();

            ImageModel imageModel = new ImageModel();

            logger.info("Crawler: Started on url: " + url);
            WebElement image = saveImageUrl(url);

            String imgLink = image.getAttribute("src");

            if(!isDuplicate(famAd)) {
                imageModel.setUrl(imgLink);
                famAd.getFamAdPropertyModel().setImage(imageModel);
                downloadImage(famAd);
                logger.info("Image saved: fam ad =  " + famAd.getName() + ", with url = " + famAd.getFamAdPropertyModel().getImage().getUrl());
                famPageRepo.save(famAd);
            } else {
                logger.info("DB: Image " + famAd.getName() + " already exists in database.");
            }
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
     * @param famAd model
     */
    private void downloadImage(FamAdModel famAd) {

        String imgLink = famAd.getFamAdPropertyModel().getImage().getUrl();
        String name = famAd.getName();
        String abbreviation = famAd.getNewAbbreviation();
        String pageNumber = famAd.getPageNumber();
        String date = famAd.getDate();

        try{
            //set to wished path
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
    private boolean isDuplicate(FamAdModel model) {
        List<FamAdModel> models = famPageRepo.findAll();

        String abbreviation = model.getNewAbbreviation();
        String date = model.getDate();
        String pageNumber = model.getPageNumber();
        String name = model.getName();

        //set to wished path
        File dir = new File("D:\\FamAds\\" + abbreviation + "\\" + date +  "\\" + pageNumber + "\\Krant Van Toen" + "\\" + name + ".jpg");
        logger.info(dir.getAbsolutePath());

        for(FamAdModel c : models) {
            if(model.getFamAdPropertyModel().getImage() != null && c.getFamAdPropertyModel().getImage() != null) {
                if (model.getFamAdPropertyModel().getImage().getUrl().equals(c.getFamAdPropertyModel().getImage().getUrl()) && dir.exists())
                    return true;
            }
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
