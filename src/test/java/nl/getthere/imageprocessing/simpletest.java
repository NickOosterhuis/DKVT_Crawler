package nl.getthere.imageprocessing;

import nl.getthere.configuration.CrawlerDbConfiguration;
import nl.getthere.configuration.NdcDbConfiguration;
import nl.getthere.dkvt_crawler.models.AbbreviationModel;
import nl.getthere.dkvt_crawler.repositories.NewspaperAbbreviationRepository;
import nl.getthere.imageprocessing.models.NDCModel;
import nl.getthere.imageprocessing.repositories.NDCRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { NdcDbConfiguration.class, CrawlerDbConfiguration.class })
@EnableTransactionManagement
public class simpletest {

    @Autowired
    private NDCRepository ndcRepository;

    @Autowired
    private NewspaperAbbreviationRepository abbreviationRepository;

    @Test
    @Transactional("ndcTransactionManager")
    public void getSomething() {

        List<NDCModel> set = ndcRepository.findAll();

        for(NDCModel model : set) {
            System.out.println(model.getMaterialId());
        }
        assertNull(ndcRepository.findByMaterialId(489112));
    }

    @Test
    @Transactional("crawlerTransactionManager")
    public void getFromCrawler() {

        AbbreviationModel model = new AbbreviationModel();
        model.setNewspaperName("ABCD");
        abbreviationRepository.save(model);

        assertNotNull("try to find all: ", abbreviationRepository.findAll());


    }
}
