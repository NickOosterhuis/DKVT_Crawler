package nl.getthere.dkvt_crawler.repositories;

import nl.getthere.dkvt_crawler.models.FamAdModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Nick Oosterhuis
 */
public interface FamAdRepository extends JpaRepository<FamAdModel, Long> {
    List<FamAdModel> findAllByFamAdNdcDataModelAlgorithmCategory(int category);
    List<FamAdModel> findAllByFamAdNdcDataModelMaterialIdNotNull();
    List<FamAdModel> findAllByNewAbbreviationAndDateAndPageNumberAndFamAdNdcDataModelAlgorithmCategory(String abbreviation, String date, String pageNumber, int category);

    boolean existsByFamAdNdcDataModelMaterialId(long materialId);

}
