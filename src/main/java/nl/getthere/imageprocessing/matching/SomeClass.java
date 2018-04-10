package nl.getthere.imageprocessing.matching;

import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import nl.getthere.imageprocessing.models.NDCModel;
import nl.getthere.imageprocessing.repositories.NDCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SomeClass {

    @Autowired
    private NDCRepository ndcRepository;

    @Autowired
    private FamAdRepository famAdRepository;

    @PostConstruct
    public void someMethod() {
        NDCModel model = ndcRepository.findByMaterialId(440693);

        System.out.println(model.getMaterialName());
    }
}
