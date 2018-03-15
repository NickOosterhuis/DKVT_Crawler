package nl.getthere.dkvt_crawler.reposiroties;

import nl.getthere.dkvt_crawler.models.NewspaperAbbreviationModel;
import nl.getthere.dkvt_crawler.models.NewspaperFamAdvertIdModel;
import org.springframework.data.repository.CrudRepository;


public interface NewspaperAbbreviationRepository extends CrudRepository<NewspaperAbbreviationModel, Long> {
    NewspaperAbbreviationModel findByNewspaperName(String name);
}
