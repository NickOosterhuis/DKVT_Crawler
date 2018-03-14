package nl.getthere.dkvt_crawler.crawlers;

import nl.getthere.dkvt_crawler.models.NewspaperFamAdvertIdModel;
import nl.getthere.dkvt_crawler.models.NewspaperIdModel;
import nl.getthere.dkvt_crawler.reposiroties.NewspaperFamAdvertIdRepo;
import nl.getthere.dkvt_crawler.reposiroties.NewspaperIdRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
public class NewspaperPageCrawler {

    @Autowired
    private NewspaperIdRepository idRepo;

    @Autowired
    private NewspaperFamAdvertIdRepo famAdvertIdRepo;

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
    private List<String> saveTempUrls() {
        List<String> tempUrls = new ArrayList<>();
        List<NewspaperIdModel> newspaperIdModels = (List<NewspaperIdModel>) idRepo.findAll();
        List<String> dates = formatLocalDates();

        for (NewspaperIdModel model : newspaperIdModels) {
            String fullId = model.getName();

            for (String date : dates) {
                String baseUrl = "https://www.dekrantvantoen.nl/vw/edition.do?dp=" + fullId + "&altd=true&date=" + date + "&ed=00&v2=true";
                tempUrls.add(baseUrl);
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
        System.out.println("VOLGENDE KNOP IS GEVONDEN!: " + isPresent);
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
        System.out.println("BASE METHOD URL =" + currentUrl);
        setBaseUrl(currentUrl);

        List<WebElement> categories = driver.findElements(By.xpath("//a[contains(@href, 'javascript:showArticle')]"));
        Set<WebElement> famAdverts = new HashSet<>();

        for (WebElement category : categories) {
            if (category.getText().equals("Familieberichten")) {
                famAdverts.add(category);
            }
            else {
                WebElement nextPage = flipPage();

                if (nextPage != null) {
                    nextPage.click();
                    currentUrl = driver.getCurrentUrl();
                    scanForFamAdverts(currentUrl);
                }
                return;
            }
        }
        saveFamAdvertId(famAdverts);
    }

    /**\
     * Method to save the advertisements into the database
     * @param famAdverts a set of webelements crawled form a certain page
     */
    private void saveFamAdvertId(Set<WebElement> famAdverts) {

        for (WebElement famAdvert : famAdverts) {
            String href = famAdvert.getAttribute("href");
            System.out.println(href);

            Pattern pattern = Pattern.compile("'(.*?)'");
            Matcher matcher = pattern.matcher(href);

            NewspaperFamAdvertIdModel adModel = new NewspaperFamAdvertIdModel();

            if(matcher.find()) {
                String advertID = matcher.group(1);
                String[] splittedAdvertId = advertID.split("-");

                String abbriviation = splittedAdvertId[0];
                String date = splittedAdvertId[1];

                adModel.setName(advertID);
                adModel.setNewspaperAbbriviation(abbriviation);
                adModel.setDate(date);

                if(!isDuplicate(adModel))
                    famAdvertIdRepo.save(adModel);
            }
        }
    }

    /**
     * Checks if model is already in database
     * @param model of fam adverts
     * @return boolean
     */
    private boolean isDuplicate(NewspaperFamAdvertIdModel model) {
        List<NewspaperFamAdvertIdModel> models = (List<NewspaperFamAdvertIdModel>) famAdvertIdRepo.findAll();

        for(NewspaperFamAdvertIdModel c : models) {
            if(model.getName().equals(c.getName()))
                return true;
        }
        return false;
    }

    @PostConstruct
    private void crawl() {
        setupDriver();
        List<String> urls = saveTempUrls();

        for(String url : urls) {
            System.out.println("IK BEN HIER! " + url);
            scanForFamAdverts(url);
        }
        quitDriver();
    }



}
