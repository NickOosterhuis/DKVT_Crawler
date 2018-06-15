package nl.getthere.dkvt_crawler.crawlers;

import nl.getthere.dkvt_crawler.models.FamAdModel;
import nl.getthere.dkvt_crawler.models.FamAdPropertyModel;
import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import nl.getthere.imageprocessing.models.NDCModel;
import nl.getthere.imageprocessing.repositories.NDCRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.getthere.dkvt_crawler.configuration.WebCrawlerConfig.*;

/**
 * Class to crawl family advertisements, modify them and save them in a db
 *
 * @author Nick Oosterhuis
 */
@Component
public class FamAdCrawler {

    @Autowired
    private NDCRepository ndcRepository;

    @Autowired
    private FamAdRepository famAdRepository;

    private static final Logger logger = LoggerFactory.getLogger(FamAdCrawler.class);

    public void crawl() {
        Set<String> baseUrls = getBaseUrls();
        setupDriver();

        for(String url : baseUrls) {
            String[] urlParts = url.split("@");
            String pageNumber = urlParts[1];
            url = urlParts[0];

            String nextPage = flipPageAndReturnUrl(url);

            if(nextPage!=null) {
                String id[] = getIdArray(nextPage);
                String nextUrl = formatNextUrl(id, pageNumber);
                scanForFamAdverts(nextUrl);
            }
            logger.error("There is no url to crawl!");
        }
        quitDriver();
    }

    /**
     * Gathers the base urls needed to crawl the website
     *
     * @return Set of Strings
     */
    private Set<String> getBaseUrls() {
        Set<String> urls = new HashSet<>();
        List<NDCModel> ndcModels = ndcRepository.findAll();
        Format formatter = new SimpleDateFormat("YYYMMdd");

        for(NDCModel ndcModel : ndcModels) {
            String ndcAbbreviation = ndcModel.getEditionCode();
            String ndcPublicationDate = formatter.format(ndcModel.getPublicationDate());
            int ndcPageNumber = ndcModel.getPlacedOnPage();
            String pageNumberString  = String.format("%03d", ndcPageNumber);

            ndcAbbreviation = formatAbbreviations(ndcAbbreviation);

            String url = "https://www.dekrantvantoen.nl/vw/edition.do?dp=" + ndcAbbreviation + "&altd=true&date=" + ndcPublicationDate + "&ed=00&v2=true@" + pageNumberString;
            urls.add(url);
        }
        return urls;
    }

    /**
     * Because the url to navigate through the newspaper is different, we need to flip one page, get the url and modify it
     *
     * @param id an array of the full abbreviation id
     * @return the next url to crawl
     */
    private String formatNextUrl(String[] id, String pageNumber) {
        System.out.println(pageNumber);

        String url = "https://www.dekrantvantoen.nl/vw/page.do?id=" + id[0] +
                    "-" + id[1] + "-" +
                    pageNumber + "-" +
                    id[3] + "&ed=00&v2=true";

        return url;
    }

    /**
     * Method to split the next url
     *
     * @param nextUrl string of the next url to crawl
     * @return array of the chopped url
     */
    private String[] getIdArray(String nextUrl) {
        String[] splittedUrl = nextUrl.split("&");
        String[] idSplit = splittedUrl[0].split("=");

        return idSplit[1].split("-");
    }

    /**
     * Method to convert the new abbreviation of the ndc database to the old abbreviation of the crawled database
     *
     * @param ndcAbbreviation new abbreviation
     * @return String of the old abbreviation
     */
    public String formatAbbreviations (String ndcAbbreviation) {
        switch (ndcAbbreviation) {
            case "GKA":
                ndcAbbreviation = "KSK";
                break;
            case "FBN":
                ndcAbbreviation = "BNH";
                break;
            default:
                ndcAbbreviation = "EMS";
                break;
        }
        return  ndcAbbreviation;
    }

    /**
     * Flip the page of the newspaper
     * @return webelement of the "Volgende" navigation button
     */
    private String flipPageAndReturnUrl(String url) {
        setBaseUrl(url);

        boolean isPresent = driver.findElements(By.xpath("//a[contains(@href, '/vw/page.do')]")).size() > 0;
        logger.info("Pagination: next button on page is found = " + isPresent);

        if(isPresent) {
            List<WebElement> navigationButtons = driver.findElements((By.xpath("//a[contains(@href, '/vw/page.do')]")));

            for(WebElement button : navigationButtons) {
                if (button.getText().equals("Volgende")) {
                    button.click();
                    return driver.getCurrentUrl();
                }
            }
        }
        return null;
    }

    /**
     * Recursive function to scan for family advertisements and navigate through newspaper
     * @param url of the page needed to scan
     */
    public void scanForFamAdverts(String url) {
        logger.info("SCAN: URL to crawl =" + url);
        setBaseUrl(url);

        List<WebElement> categories = driver.findElements(By.xpath("//a[contains(@href, 'javascript:showArticle')]"));
        Set<WebElement> famAdverts;

        if (isFamPage(categories)) {
            famAdverts = new HashSet<>(categories);
            saveFamAdvertId(famAdverts);
        }
    }

    /**
     * Method to save the advertisements into the database
     * @param famAdverts a set of web elements crawled form a certain page
     */
    public void saveFamAdvertId(Set<WebElement> famAdverts) {
        logger.info("FamAd: Crawler found = " + famAdverts.size() + " advertisements on page");

        for (WebElement famAdvert : famAdverts) {
            String href = famAdvert.getAttribute("href");
            String mouseOver = famAdvert.getAttribute("onmousemove");

            Pattern pattern = Pattern.compile("'(.*?)'");
            Matcher matcher = pattern.matcher(href);
            Matcher matcherMouseElement = pattern.matcher(mouseOver);

            FamAdModel adModel = new FamAdModel();

            if(matcher.find() && matcherMouseElement.find()) {
                //strip javascript function to click advert
                String advertID = matcher.group(1);
                String[] splitAdvertId = advertID.split("-");

                String abbreviation = splitAdvertId[0];
                String date = splitAdvertId[1];

                if(date.equals("20171228")) {
                    date = "20171227";
                }

                String id = splitAdvertId[2];

                String[] numbers = id.split("");
                String pageNumber = numbers[2] + numbers[3] + numbers[4];
                String publicationNumber = numbers[0] + numbers[1];
                String advertNumber = numbers[5] + numbers[6] + numbers[7];

                //strip mouse over function
                String column = matcherMouseElement.group(1);
                String[] splitColumn = column.split("");
                column = splitColumn[0] + splitColumn[1];

                logger.info("FamAd: advert ID = " + advertID + ", abbreviation = " + abbreviation + ", date = " + date +
                        ", page number = " + pageNumber + ", publication number = " + publicationNumber +
                        ", advert number = " + advertNumber + ", Column = " + column);

                adModel.setName(advertID);
                adModel.setAbbreviation(abbreviation);
                adModel.setDate(date);
                adModel.setPageNumber(pageNumber);
                adModel.setPublicationNumber(publicationNumber);
                adModel.setAdvertNumber(advertNumber);
                adModel.setColumnId(column);

                switch (abbreviation) {
                    case "KSK":
                        adModel.setNewAbbreviation("GKA");
                        break;
                    case "BNH":
                        adModel.setNewAbbreviation("FBN");
                        break;
                    case "EMS":
                        adModel.setNewAbbreviation("GEM");
                        break;
                }

                coupleFamAdProperties(adModel);

                if(!isDuplicate(adModel)) {
                    logger.info("FamAd: Model is saved in DB");
                    famAdRepository.save(adModel);
                } else
                    logger.info("FamAd: Model already exists in DB");
            }
        }
    }

    /**
     * Save the family ad properties and bind them to the famAds
     *
     * @param famAd model
     */
    public void coupleFamAdProperties(FamAdModel famAd) {
        //Get x, y coordinates and width and height of an advertisement
        WebElement columnId = driver.findElement(By.id(famAd.getColumnId()));
        WebElement nestedElement = columnId.findElement(By.id("f0"));
        String style = nestedElement.getAttribute("style");

        String[] split = style.split(";");

        String x = split[1];
        String y = split[2];
        String width = split[3];
        String height = split[4];

        int xCoordinate = Integer.parseInt(x.replaceAll("[^\\d.]", ""));
        int yCoordinate = Integer.parseInt(y.replaceAll("[^\\d.]", ""));
        int widthSize = Integer.parseInt(width.replaceAll("[^\\d.]", ""));
        int heightSize = Integer.parseInt(height.replaceAll("[^\\d.]", ""));

        logger.info("Style: x coordinate = " + xCoordinate + ", y coordinate = " + yCoordinate + ", width = " + width + ", height = " + height);

        FamAdPropertyModel propertyModel = new FamAdPropertyModel(xCoordinate, yCoordinate, heightSize, widthSize);
        famAd.setFamAdPropertyModel(propertyModel);
    }

    /**
     * Check if a certain page in a newspaper is a famadvert page
     * @param categories a list of javascript:showArticle functions
     * @return true or false depending what the title of the advert is
     */
    private boolean isFamPage(List<WebElement> categories) {
        for (WebElement category : categories) {
            if (category.getText().equals("Familieberichten")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if model is already exists in database
     * @param model of fam adverts
     * @return boolean
     */
    private boolean isDuplicate(FamAdModel model) {
        List<FamAdModel> models = famAdRepository.findAll();

        for(FamAdModel c : models) {
            if(model.getName().equals(c.getName()))
                return true;
        }
        return false;
    }
}
