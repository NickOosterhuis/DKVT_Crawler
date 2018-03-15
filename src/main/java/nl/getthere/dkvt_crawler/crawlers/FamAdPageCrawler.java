package nl.getthere.dkvt_crawler.crawlers;

import nl.getthere.dkvt_crawler.models.NewspaperFamAdvertIdModel;
import nl.getthere.dkvt_crawler.reposiroties.NewspaperFamAdvertIdRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nl.getthere.dkvt_crawler.crawlers.WebCrawlerConfig.quitDriver;
import static nl.getthere.dkvt_crawler.crawlers.WebCrawlerConfig.setBaseUrl;
import static nl.getthere.dkvt_crawler.crawlers.WebCrawlerConfig.setupDriver;

@Component
@Order(4)
public class FamAdPageCrawler {

    @Autowired
    private NewspaperFamAdvertIdRepo famAdvertIdRepo;

    //@PostConstruct
    private void crawl() {
        Set<String> urls = saveTempUrls();
        setupDriver();

        for(String url : urls) {
            System.out.println(url);
            setBaseUrl(url);
        }
        quitDriver();
    }

    private Set<String> saveTempUrls() {
        Set<String> tempUrls = new HashSet<>();
        List<NewspaperFamAdvertIdModel> famAdvertIdModels = (List<NewspaperFamAdvertIdModel>) famAdvertIdRepo.findAll();

        for (NewspaperFamAdvertIdModel model : famAdvertIdModels) {
            String abbreviation = model.getNewspaperAbbreviation();
            String publicationNumber = model.getPublicationNumber();
            String pageNumber = model.getPageNumber();
            String date = model.getDate();

            String url = "https://www.dekrantvantoen.nl/vw/page.do?id="+
                    abbreviation + "-" +
                    publicationNumber + "-" +
                    pageNumber+ "-" +
                    date+ "&v2=true";

            tempUrls.add(url);
        }
        return tempUrls;
    }
}
