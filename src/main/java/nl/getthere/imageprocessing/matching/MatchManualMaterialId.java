package nl.getthere.imageprocessing.matching;

import nl.getthere.dkvt_crawler.models.FamAdNdcDataModel;
import nl.getthere.dkvt_crawler.models.FamAdPageModel;
import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import nl.getthere.imageprocessing.models.NDCModel;
import nl.getthere.imageprocessing.repositories.NDCRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MatchManualMaterialId {

    @Autowired
    private FamAdRepository famAdRepository;

    @Autowired
    private NDCRepository ndcRepository;

    private static final Logger logger = LoggerFactory.getLogger(MatchManualMaterialId.class);

    public void match() {

        List<FamAdPageModel> famAdModels = famAdRepository.findAllByFamAdNdcDataModelMaterialIdNotNull();

        System.out.println(famAdModels.size());

        for(FamAdPageModel model : famAdModels) {
            long materialId = model.getFamAdNdcDataModel().getMaterialId();

            if(materialId > 0 && model.getFamAdNdcDataModel().getAlgorithmCategory() == 5) {
                NDCModel ndc = ndcRepository.findByMaterialId(materialId);

                //Gather current NDC ad info
                String note = ndc.getNote();
                BigDecimal realAdHeight = ndc.getHeightMm();
                BigDecimal realAdWidth = ndc.getWidthMm();
                String sectionCode = ndc.getSectionCode();
                int columnWidth = ndc.getWidthCol();

                //Create and set NDC <-> Crawled coupling model
                FamAdNdcDataModel famAdNdcDataModel = model.getFamAdNdcDataModel();
                famAdNdcDataModel.setMaterialId(materialId);
                famAdNdcDataModel.setNote(note);
                famAdNdcDataModel.setSectionCode(sectionCode);
                famAdNdcDataModel.setRealHeight(realAdHeight);
                famAdNdcDataModel.setRealWidth(realAdWidth);
                famAdNdcDataModel.setColumnWidth(columnWidth);
                famAdNdcDataModel.setMatched(true);
                famAdNdcDataModel.setAlgorithmCategory(4);

                //Gather current Fam AD Database Info and set the ndc info
                model.setFamAdNdcDataModel(famAdNdcDataModel);

                logger.info("Database Coupling: NDC info is saved on DKVT DB with Values: " +
                        "Material id: " + materialId +
                        " Note: " + note +
                        " Section code: " + sectionCode +
                        " Height: " + realAdHeight +
                        " Width: " + realAdWidth +
                        "Column Width: " + columnWidth);

                famAdRepository.save(model);
            }
        }
    }
}
