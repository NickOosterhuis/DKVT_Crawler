package nl.getthere.dkvt_crawler.repositories;

import nl.getthere.dkvt_crawler.models.NewspaperAbbreviationModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Nick Oosterhuis
 */
public interface NewspaperAbbreviationRepository extends JpaRepository<NewspaperAbbreviationModel, Long> {
    NewspaperAbbreviationModel findByNewspaperName(String name);
}
