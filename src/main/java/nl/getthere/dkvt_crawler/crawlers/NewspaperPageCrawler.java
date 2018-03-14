package nl.getthere.dkvt_crawler.crawlers;

import nl.getthere.dkvt_crawler.models.NewspaperIdModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static nl.getthere.dkvt_crawler.crawlers.WebCrawlerConfig.*;

@Component
@Order(3)
public class NewspaperPageCrawler {

    @Autowired
    private NewspaperIdRepository idRepo;

    /**
     * Get all dates in between a certain range
     * @return List of local date objects
     */
    private List<LocalDate> getDatesInRange() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = LocalDate.of(2018, Month.MARCH, 10);

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

    private void scanForFamAdverts(String url) {
        String currentUrl = url;
        System.out.println("BASE METHOD URL =" + currentUrl);
        setBaseUrl(currentUrl);

        List<WebElement> categories = driver.findElements(By.xpath("//a[contains(@href, 'javascript:showArticle')]"));

        for (WebElement catagory : categories) {
            if (catagory.getText().equals("Familieberichten")) {
                System.out.println("Famillieberichten categorie gevonden!");
            } else {
                WebElement nextPage = flipPage();
                if(nextPage != null) {
                    nextPage.click();
                    currentUrl = driver.getCurrentUrl();
                    scanForFamAdverts(currentUrl);
                }
                System.out.println("Geen familieberichten gevonden in deze krant");
                return;
            }
        }
    }

    @PostConstruct
    private void crawl() {
        setupDriver();
        List<String> urls = saveTempUrls();

        for(String url : urls) {
            System.out.println("IK BEN HIER!");
            scanForFamAdverts(url);
        }
        quitDriver();
    }



}
