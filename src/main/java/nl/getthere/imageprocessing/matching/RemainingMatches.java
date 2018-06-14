package nl.getthere.imageprocessing.matching;

import nl.getthere.dkvt_crawler.models.FamAdModel;
import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import nl.getthere.helpers.PdfToImgConvertor;
import org.opencv.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

import static nl.getthere.helpers.FamAdHelper.ndcDir;
import static nl.getthere.helpers.FamAdHelper.setDirs;

@Component
public class RemainingMatches {

    @Autowired
    private FamAdRepository famAdRepository;

    @Autowired
    private PdfToImgConvertor pdfToImg;

    private static final Logger logger = LoggerFactory.getLogger(RemainingMatches.class);

    private String ndcFileName;

    public void match() {
        List<FamAdModel> famAds = famAdRepository.findAllByFamAdNdcDataModelAlgorithmCategory(5);

        matchAds(famAds);
    }

    public void matchAds(List<FamAdModel> famAds) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        setDirs(famAds);

        File ndcFolder = new File(ndcDir);

        List<File> ndcFiles = pdfToImg.addFilesToList(ndcFolder);
        List<Integer> materialIds = new ArrayList<>();

        for (File ndcFile : ndcFiles) {
            ndcFileName = ndcFile.getName();

            int materialId = Integer.parseInt(ndcFileName.replace(".jpg", ""));
            materialIds.add(materialId);
        }
            List<Integer> toMatch = getToBeMatchedIds(materialIds);

            for(int materialid : toMatch) {
                System.out.println("Ad Size: " + toMatch.size() + "\n Folder: " + ndcDir + "\n Material ID: " + materialid);
            }
    }



   private List<Integer> getToBeMatchedIds(List<Integer> materialIds) {

        List<Integer> toMatch = new ArrayList<>();

        for(int materialId : materialIds) {
            boolean materialIdExists = famAdRepository.existsByFamAdNdcDataModelMaterialId(materialId);

            if(!materialIdExists) {
                toMatch.add(materialId);
            }
        }
        return toMatch;
    }
}
