package nl.getthere.imageprocessing.matching;

import nl.getthere.dkvt_crawler.models.FamAdNdcDataModel;
import nl.getthere.dkvt_crawler.models.FamAdPageModel;
import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import nl.getthere.helpers.PdfToImg;
import nl.getthere.imageprocessing.models.NDCModel;
import nl.getthere.imageprocessing.repositories.NDCRepository;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_COLOR;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.line;

@Component
public class RemainingMatches {

    @Autowired
    private FamAdRepository famAdRepository;

    @Autowired
    private NDCRepository ndcRepository;

    @Autowired
    private PdfToImg pdfToImg;

    @Autowired
    private KnnOpenCvMatcher knnOpenCvMatcher;

    private static final Logger logger = LoggerFactory.getLogger(RemainingMatches.class);

    private String adName;
    private String ndcFileName;
    private FamAdPageModel currentFamAd;
    private String ndcFileDir;
    private String ndcDir;
    private Mat objectImage;
    private Mat sceneImage;

    //drawing variables
    private MatOfKeyPoint objectKeyPoints;
    private MatOfKeyPoint sceneKeyPoints;
    private Scalar newKeypointColor;
    private Mat outputImage;
    private String krantVanToenDir;

    public void match() {
        List<FamAdPageModel> famAds = famAdRepository.findAllByFamAdNdcDataModelAlgorithmCategory(5);

        matchAds(famAds);
    }

    public void matchAds(List<FamAdPageModel> famAds) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        for (FamAdPageModel famAd : famAds) {
            String abbreviation = famAd.getNewNewspaperAbbreviation();
            String pageNumber = famAd.getPageNumber();
            String date = famAd.getDate();
            adName = famAd.getName();
            currentFamAd = famAd;

            if(date.equals("20171228")) {
                date = "20171227";
            }
            ndcDir = "D:\\FamAds\\" + abbreviation + "\\" + date + "\\" + pageNumber + "\\" + "NDC\\";
            krantVanToenDir = "D:\\FamAds\\" + abbreviation + "\\" + date + "\\" + pageNumber + "\\" + "Krant Van Toen" + "\\" + adName + ".jpg";

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
