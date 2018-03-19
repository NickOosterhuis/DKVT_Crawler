package nl.getthere.dkvt_crawler.reposiroties;

import nl.getthere.dkvt_crawler.models.NewspaperAbbreviationModel;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Nick Oosterhuis
 */
public interface NewspaperAbbreviationRepository extends CrudRepository<NewspaperAbbreviationModel, Long> {
    NewspaperAbbreviationModel findByNewspaperName(String name);
}
