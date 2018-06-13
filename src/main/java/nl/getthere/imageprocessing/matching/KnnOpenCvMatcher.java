package nl.getthere.imageprocessing.matching;

import nl.getthere.dkvt_crawler.models.FamAdNdcDataModel;
import nl.getthere.dkvt_crawler.models.FamAdModel;
import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import nl.getthere.imageprocessing.models.NDCModel;
import nl.getthere.imageprocessing.repositories.NDCRepository;
import nl.getthere.helpers.PdfToImgConvertor;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.*;
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

/**
 * Class to match the Crawled Images with the NDC Images and save that coupling in the database
 *
 * @author Nick Oosterhuis
 */
@Component
public class KnnOpenCvMatcher {

    @Autowired
    private FamAdRepository famAdRepository;

    @Autowired
    private NDCRepository ndcRepository;

    @Autowired
    private PdfToImgConvertor pdfToImg;

    private static final Logger logger = LoggerFactory.getLogger(KnnOpenCvMatcher.class);

    private String adName;
    private String ndcFileName;
    private FamAdModel currentFamAd;

    private Mat objectImage;
    private Mat sceneImage;
    private String ndcFileDir;

    //drawing variables
    private MatOfKeyPoint objectKeyPoints;
    private MatOfKeyPoint sceneKeyPoints;
    private Scalar newKeypointColor;
    private Mat outputImage;

    public void match() {
        List<FamAdModel> famAds = famAdRepository.findAllByFamAdNdcDataModelAlgorithmCategory(1);

        matchAds(famAds);
    }

    private void matchAds(List<FamAdModel> famAds) {
        if (famAds.isEmpty()) {
            coupleDB();
            famAds = famAdRepository.findAllByFamAdNdcDataModelAlgorithmCategory(1);
        }

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        for (FamAdModel famAd : famAds) {
            String abbreviation = famAd.getNewAbbreviation();
            String pageNumber = famAd.getPageNumber();
            String date = famAd.getDate();
            adName = famAd.getName();
            currentFamAd = famAd;

            if(date.equals("20171228")) {
                date = "20171227";
            }

            String krantVanToenDir = "D:\\FamAds\\" + abbreviation + "\\" + date + "\\" + pageNumber + "\\" + "Krant Van Toen" + "\\" + adName + ".jpg";
            String ndcDir = "D:\\FamAds\\" + abbreviation + "\\" + date + "\\" + pageNumber + "\\" + "NDC\\";

            File ndcFolder = new File(ndcDir);

            List<File> ndcFiles = pdfToImg.addFilesToList(ndcFolder);

            for (File ndcFile : ndcFiles) {
                ndcFileName = ndcFile.getName();
                ndcFileDir = ndcDir + ndcFileName;

                if(currentFamAd.getFamAdNdcDataModel().getAlgorithmCategory() == 5) {
                    currentFamAd.getFamAdNdcDataModel().setAlgorithmCategory(1);
                }

                logger.info("Matching: Matcher Started");

                objectImage = imread(krantVanToenDir, CV_LOAD_IMAGE_COLOR);
                sceneImage = imread(ndcFileDir, CV_LOAD_IMAGE_COLOR);

                logger.info("Matching: " + krantVanToenDir + " VS " + ndcFileDir);

                AlgorithmSettings(currentFamAd);
            }
        }
    }

    private void coupleDB() {
        List<FamAdModel> models = famAdRepository.findAll();

        for (FamAdModel model : models) {
            FamAdNdcDataModel ndcDataModel = new FamAdNdcDataModel();
            ndcDataModel.setAlgorithmCategory(1);
            ndcDataModel.setMatched(false);
            model.setFamAdNdcDataModel(ndcDataModel);

            famAdRepository.save(model);
        }
        logger.info("Model Coupling: Models are set!");
    }

    private void AlgorithmSettings(FamAdModel famAd) {

        int algorithmCategory = famAd.getFamAdNdcDataModel().getAlgorithmCategory();

        if (algorithmCategory == 1)
            AkazeSettings();
        else if (algorithmCategory == 2)
            PyramidAkazeSettings();
        else if (algorithmCategory == 3)
            DynamicAkazeSettings();
        else if (algorithmCategory == 4) {
            logger.warn("Matching: Image already matched!");
            System.out.println("");
        }
        else if (algorithmCategory == 5) {
            logger.error("Matching: Not able to matchAds image with current algorithms!");
            System.out.println("");
        }
    }

    private void AkazeSettings() {
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.AKAZE);
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.AKAZE);
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);
        int akazePoints = 100;
        float akazeRadius = 0.5f;

        logger.info("Matching method: Akaze");

        compareFeatures(featureDetector, descriptorExtractor, descriptorMatcher, akazeRadius, akazePoints);
    }

    private void PyramidAkazeSettings() {
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.PYRAMID_AKAZE);
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.AKAZE);
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);
        int pyramidAkazePoints = 30;
        float pyramidAkazeRadius = 0.5f;

        logger.info("Matching method: Pyramid Akaze");

        compareFeatures(featureDetector, descriptorExtractor, descriptorMatcher, pyramidAkazeRadius, pyramidAkazePoints);
    }

    private void DynamicAkazeSettings() {
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.FAST);
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);
        int dynamicAkazePoints = 300;
        float dynamicAkazeRadius = 0.5f;

        logger.info("Matching method: Dynamic Akaze");

        compareFeatures(featureDetector, descriptorExtractor, descriptorMatcher, dynamicAkazeRadius, dynamicAkazePoints);
    }

    public void compareFeatures(FeatureDetector featureDetector, DescriptorExtractor descriptorExtractor, DescriptorMatcher descriptorMatcher, float nndrRatio, int pointLimit) {
        objectKeyPoints = new MatOfKeyPoint();

        featureDetector.detect(objectImage, objectKeyPoints);
        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
        descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);

        // Create the matrix for output image.
        outputImage = new Mat(objectImage.rows(), objectImage.cols(), CV_LOAD_IMAGE_COLOR);
        newKeypointColor = new Scalar(255, 0, 0);

        if (!outputImage.empty()) {
            Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);
        } else {
            logger.error("Image Drawing: Output file is empty!");
        }

        // Match object image with the scene image
        sceneKeyPoints = new MatOfKeyPoint();
        MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
        featureDetector.detect(sceneImage, sceneKeyPoints);
        descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);

        List<MatOfDMatch> matches = new LinkedList<>();
        descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);
        LinkedList<DMatch> goodMatchesList = new LinkedList<>();

        for (MatOfDMatch matofDMatch : matches) {
            DMatch[] dMatches = matofDMatch.toArray();

            if (dMatches.length >= 2) {
                DMatch m1 = dMatches[0];
                DMatch m2 = dMatches[1];

                if (m1.distance <= m2.distance * nndrRatio) {
                    goodMatchesList.addLast(m1);
                }
            }
        }
        updateDatabase(goodMatchesList, pointLimit);
    }

    private void updateDatabase(LinkedList<DMatch> goodMatchesList, int pointLimit) {

        if (ndcFileName.endsWith(".jpg")) {
            ndcFileName = ndcFileName.replaceAll(".jpg", "");
        }

        long materialId = Long.parseLong(ndcFileName);

        //Gather current NDC AD Database info
        NDCModel currentNdcAd = ndcRepository.findByMaterialId(materialId);
        makeMatch(currentNdcAd, goodMatchesList, materialId, pointLimit);
    }

    private void makeMatch(NDCModel model, LinkedList<DMatch> goodMatchesList, long materialId, int pointLimit) {

        logger.info("Matched points: " + goodMatchesList.size());

        if (goodMatchesList.size() >= pointLimit && currentFamAd.getFamAdNdcDataModel().getAlgorithmCategory() <= 3 && !currentFamAd.getFamAdNdcDataModel().isMatched()) {

            //Gather current NDC ad info
            String note = model.getNote();
            BigDecimal realAdHeight = model.getHeightMm();
            BigDecimal realAdWidth = model.getWidthMm();
            String sectionCode = model.getSectionCode();
            int columnWidth = model.getWidthCol();

            //Create and set NDC <-> Crawled coupling model
            FamAdNdcDataModel famAdNdcDataModel = currentFamAd.getFamAdNdcDataModel();
            famAdNdcDataModel.setMaterialId(materialId);
            famAdNdcDataModel.setNote(note);
            famAdNdcDataModel.setSectionCode(sectionCode);
            famAdNdcDataModel.setRealHeight(realAdHeight);
            famAdNdcDataModel.setRealWidth(realAdWidth);
            famAdNdcDataModel.setColumnWidth(columnWidth);
            famAdNdcDataModel.setMatched(true);
            famAdNdcDataModel.setAlgorithmCategory(4);

            //Gather current Fam AD Database Info and set the ndc info
            currentFamAd.setFamAdNdcDataModel(famAdNdcDataModel);

            logger.info("Database Coupling: NDC info is saved on DKVT DB with Values: " +
                    "Material id: " + materialId +
                    " Note: " + note +
                    " Section code: " + sectionCode +
                    " Height: " + realAdHeight +
                    " Width: " + realAdWidth +
                    "Column Width: " + columnWidth);

            logger.info("Matching: Match is found");
            System.out.println("");

            //drawing image for testing
            transformAndDraw(goodMatchesList, objectKeyPoints, sceneKeyPoints, objectImage, ndcFileDir, sceneImage, newKeypointColor, outputImage);
            famAdRepository.save(currentFamAd);
        } else if (goodMatchesList.size() < pointLimit && currentFamAd.getFamAdNdcDataModel().getAlgorithmCategory() == 1 && !currentFamAd.getFamAdNdcDataModel().isMatched()) {
            logger.info("Matching: No matchAds found with Akaze algorithm, switching algorithms!");
            System.out.println("");

            currentFamAd.getFamAdNdcDataModel().setMatched(false);
            currentFamAd.getFamAdNdcDataModel().setAlgorithmCategory(2);

            famAdRepository.save(currentFamAd);
            AlgorithmSettings(currentFamAd);
        } else if (goodMatchesList.size() < pointLimit && currentFamAd.getFamAdNdcDataModel().getAlgorithmCategory() == 2 && !currentFamAd.getFamAdNdcDataModel().isMatched()) {
            logger.info("Matching: No matchAds found with Pyramid Akaze algorithm, switching algorithms!");
            System.out.println("");

            currentFamAd.getFamAdNdcDataModel().setMatched(false);
            currentFamAd.getFamAdNdcDataModel().setAlgorithmCategory(3);

            famAdRepository.save(currentFamAd);
            AlgorithmSettings(currentFamAd);
        } else if (goodMatchesList.size() < pointLimit && currentFamAd.getFamAdNdcDataModel().getAlgorithmCategory() == 3 && !currentFamAd.getFamAdNdcDataModel().isMatched()){
            logger.info("Matching: No matchAds found with Dynamic Akaze algorithm!");

            currentFamAd.getFamAdNdcDataModel().setMatched(false);
            currentFamAd.getFamAdNdcDataModel().setAlgorithmCategory(5);

            famAdRepository.save(currentFamAd);
            AlgorithmSettings(currentFamAd);
        }
    }

    private void transformAndDraw(LinkedList<DMatch> goodMatchesList, MatOfKeyPoint objectKeyPoints,
                                  MatOfKeyPoint sceneKeyPoints, Mat objectImage, String ndcDir,
                                  Mat sceneImage, Scalar newKeypointColor, Mat outputImage) {

        Mat matchOutput = new Mat(sceneImage.rows() * 2, sceneImage.cols() * 2, CV_LOAD_IMAGE_COLOR);
        Scalar matchesColor = new Scalar(0, 255, 0);


        List<KeyPoint> objKeypointlist = objectKeyPoints.toList();
        List<KeyPoint> scnKeypointlist = sceneKeyPoints.toList();

        LinkedList<Point> objectPoints = new LinkedList<>();
        LinkedList<Point> scenePoints = new LinkedList<>();

        for (DMatch aGoodMatch : goodMatchesList) {
            objectPoints.addLast(objKeypointlist.get(aGoodMatch.queryIdx).pt);
            scenePoints.addLast(scnKeypointlist.get(aGoodMatch.trainIdx).pt);
        }

        MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
        objMatOfPoint2f.fromList(objectPoints);
        MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
        scnMatOfPoint2f.fromList(scenePoints);

        Mat homography = Calib3d.findHomography(objMatOfPoint2f, scnMatOfPoint2f, Calib3d.RANSAC, 3);

        Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);
        Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);

        obj_corners.put(0, 0, 0, 0);
        obj_corners.put(1, 0, objectImage.cols(), 0);
        obj_corners.put(2, 0, objectImage.cols(), objectImage.rows());
        obj_corners.put(3, 0, 0, objectImage.rows());

        //logger.info("Transforming object: Cutting corners to matchAds scene corners");
        Core.perspectiveTransform(obj_corners, scene_corners, homography);

        Mat img = imread(ndcDir, CV_LOAD_IMAGE_COLOR);

        line(img, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 4);
        line(img, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 4);
        line(img, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 4);
        line(img, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)), new Scalar(0, 255, 0), 4);

        //logger.info("Drawing images");
        MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(goodMatchesList);

        Features2d.drawMatches(objectImage, objectKeyPoints, sceneImage, sceneKeyPoints, goodMatches, matchOutput, matchesColor, newKeypointColor, new MatOfByte(), 2);

        File dir = new File("D:\\Matches");

        if (!dir.exists()) {
            dir.mkdirs();
        }
        imwrite("D:\\Matches\\" + adName + "_matched" + ".jpg", matchOutput);
    }
}
