package nl.getthere.dkvt_crawler.reposiroties;

import nl.getthere.dkvt_crawler.models.FamAdPageModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FamPageRepository extends CrudRepository<FamAdPageModel, Long> {
    List<FamAdPageModel> findAllByNewspaperAbbreviation(String abbriviation);
    List<FamAdPageModel> findAllByName(String name);
}
