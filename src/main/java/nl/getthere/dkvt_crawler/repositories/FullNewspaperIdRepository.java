package nl.getthere.dkvt_crawler.repositories;

import nl.getthere.dkvt_crawler.models.FullNewspaperIdModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Nick Oosterhuis
 */
public interface FullNewspaperIdRepository extends JpaRepository<FullNewspaperIdModel, Long> {
}
