package nl.getthere.dkvt_crawler.repositories;

import nl.getthere.dkvt_crawler.models.FamAdPageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamAdRepository extends JpaRepository<FamAdPageModel, Long> {
    List<FamAdPageModel> findAllByNewspaperAbbreviation(String abbriviation);
    List<FamAdPageModel> findAllByName(String name);
}
