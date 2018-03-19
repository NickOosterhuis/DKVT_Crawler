package nl.getthere.dkvt_crawler.crawlers;

import nl.getthere.dkvt_crawler.models.FamAdPageModel;
import nl.getthere.dkvt_crawler.models.FamAdPropertyModel;
import nl.getthere.dkvt_crawler.models.FullNewspaperIdModel;
import nl.getthere.dkvt_crawler.reposiroties.FamPageRepository;
import nl.getthere.dkvt_crawler.reposiroties.FullNewspaperIdRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static nl.getthere.dkvt_crawler.crawlers.WebCrawlerConfig.*;

@Component
@Order(3)
public class NewspaperScanner {

    @Autowired
    private FullNewspaperIdRepository idRepo;

    @Autowired
    private FamPageRepository famAdvertIdRepo;

    private static final Logger logger = LoggerFactory.getLogger(NewspaperScanner.class);

    /**
     * Start crawling
     */
    @PostConstruct
    private void crawl() {
        setupDriver();
        Set<String> urls = saveTempUrls();

        for(String url : urls) {
            logger.info("Run method again on url: " + url);
            scanForFamAdverts(url);
        }
        quitDriver();
    }

    /**
     * Get all dates in between a certain range
     * @return List of local date objects
     */
    private List<LocalDate> getDatesInRange() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = LocalDate.of(2018, Month.MARCH, 1);

        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(startDate::plusDays)
                .collect(Collectors.toList());
    }

    /**
     * Format List of LocalDates to use for the parameterized base url
     * @return List of formatted dates
     */
    private List<String> formatLocalDates() {
        List<LocalDate> dates = getDatesInRange();
        List<String> convertedDates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYYMMdd");

        for(LocalDate date : dates) {
            String convertedDate = date.format(formatter);
            convertedDates.add(convertedDate);
        }
        return convertedDates;
    }

    /**
     * Temporarily save the url's of all newspapers
     *
     * @return List of urls
     */
    private Set<String> saveTempUrls() {
        Set<String> tempUrls = new HashSet<>();
        List<FullNewspaperIdModel> newspaperIdModels = (List<FullNewspaperIdModel>) idRepo.findAll();
        List<String> dates = formatLocalDates();

        for (FullNewspaperIdModel model : newspaperIdModels) {
            String fullId = model.getName();
            for (String date : dates) {
                String baseUrl = "https://www.dekrantvantoen.nl/vw/edition.do?dp=" + fullId + "&altd=true&date=" + date + "&ed=00&v2=true";
                tempUrls.add(baseUrl);

                if(date.equals(LocalDate.now().toString())) {
                    driver.quit();
                }
            }
        }
        return tempUrls;
    }

    /**
     * Flip the page of the newspaper
     * @return webelement of the "Volgende" navigation button
     */
    private WebElement flipPage() {
        boolean isPresent = driver.findElements(By.xpath("//a[contains(@href, '/vw/page.do')]")).size() > 0;
        logger.info("Pagination: next button on page is found = " + isPresent);

        if(isPresent) {
            List<WebElement> navigationButtons = driver.findElements((By.xpath("//a[contains(@href, '/vw/page.do')]")));

            for(WebElement button : navigationButtons) {
                if (button.getText().equals("Volgende"))
                    return button;
            }
        }
        return null;
    }

    /**
     * Recursive function to scan for family advertisements and navigate through newspaper
     * @param url of the page needed to scan
     */
    private void scanForFamAdverts(String url) {
        String currentUrl = url;
        logger.info("SCAN: URL to crawl =" + currentUrl);
        setBaseUrl(currentUrl);

        List<WebElement> categories = driver.findElements(By.xpath("//a[contains(@href, 'javascript:showArticle')]"));
        Set<WebElement> famAdverts;

        if (isFamPage(categories)) {
            famAdverts = new HashSet<>(categories);
            saveFamAdvertId(famAdverts);

            WebElement nextPage = flipPage();
            if (nextPage != null) {
                nextPage.click();
                currentUrl = driver.getCurrentUrl();
                scanForFamAdverts(currentUrl);
            }
        } else {
            logger.info("Flip Page: No Fam Adverts found on page: " + url);
            WebElement nextPage = flipPage();

            if (nextPage != null) {
                nextPage.click();
                currentUrl = driver.getCurrentUrl();
                scanForFamAdverts(currentUrl);
            }
            return;
        }
    }

    /**
     * Method to save the advertisements into the database
     * @param famAdverts a set of web elements crawled form a certain page
     */
    private void saveFamAdvertId(Set<WebElement> famAdverts) {
        logger.info("FamAd: Crawler found = " + famAdverts.size() + " advertisements on page");

        for (WebElement famAdvert : famAdverts) {
            String href = famAdvert.getAttribute("href");
            String mouseOver = famAdvert.getAttribute("onmousemove");

            Pattern pattern = Pattern.compile("'(.*?)'");
            Matcher matcher = pattern.matcher(href);
            Matcher matcherMouseElement = pattern.matcher(mouseOver);

            FamAdPageModel adModel = new FamAdPageModel();

            if(matcher.find() && matcherMouseElement.find()) {
                //strip javascript function to click advert
                String advertID = matcher.group(1);
                String[] splitAdvertId = advertID.split("-");

                String abbreviation = splitAdvertId[0];
                String date = splitAdvertId[1];
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
                adModel.setNewspaperAbbreviation(abbreviation);
                adModel.setDate(date);
                adModel.setPageNumber(pageNumber);
                adModel.setPublicationNumber(publicationNumber);
                adModel.setAdvertNumber(advertNumber);
                adModel.setColumnId(column);

                coupleFamAdProperties(adModel);

                if(!isDuplicate(adModel)) {
                    logger.info("FamAd: Model is saved in DB");
                    famAdvertIdRepo.save(adModel);
                } else
                logger.info("FamAd: Model already exists in DB");
            }
        }
    }

    private void coupleFamAdProperties(FamAdPageModel famAd) {

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
    private boolean isDuplicate(FamAdPageModel model) {
        List<FamAdPageModel> models = (List<FamAdPageModel>) famAdvertIdRepo.findAll();

        for(FamAdPageModel c : models) {
            if(model.getName().equals(c.getName()))
                return true;
        }
        return false;
    }
}
