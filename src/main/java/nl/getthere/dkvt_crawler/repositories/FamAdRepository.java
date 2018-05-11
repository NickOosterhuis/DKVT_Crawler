package nl.getthere.dkvt_crawler.repositories;

import nl.getthere.dkvt_crawler.models.FamAdPageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Nick Oosterhuis
 */
public interface FamAdRepository extends JpaRepository<FamAdPageModel, Long> {
    List<FamAdPageModel> findAllByFamAdNdcDataModelAlgorithmCategory(int category);
    List<FamAdPageModel> findAllByFamAdNdcDataModelMaterialIdNotNull();

    boolean existsByFamAdNdcDataModelMaterialId(long materialId);

}
