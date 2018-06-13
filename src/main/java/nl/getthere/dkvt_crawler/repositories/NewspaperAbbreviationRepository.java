package nl.getthere.dkvt_crawler.repositories;

import nl.getthere.dkvt_crawler.models.AbbreviationModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Nick Oosterhuis
 */
public interface NewspaperAbbreviationRepository extends JpaRepository<AbbreviationModel, Long> {
    AbbreviationModel findByNewspaperName(String name);
}
