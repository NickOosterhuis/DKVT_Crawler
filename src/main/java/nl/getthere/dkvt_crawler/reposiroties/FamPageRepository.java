package nl.getthere.dkvt_crawler.reposiroties;

import nl.getthere.dkvt_crawler.models.FamAdPageModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * @author Nick Oosterhuis
 */
public interface FamPageRepository extends CrudRepository<FamAdPageModel, Long> {
}
