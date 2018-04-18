package nl.getthere.imageprocessing.matching;

import nl.getthere.dkvt_crawler.models.FamAdPageModel;
import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import nl.getthere.mapstructure.PdfToImg;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_COLOR;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.line;

@Component
public class OpenCvTest2 {

    @Autowired
    private FamAdRepository famAdRepository;

    @Autowired
    private PdfToImg pdfToImg;

    public void match() throws InterruptedException {

        List<FamAdPageModel> famAds = famAdRepository.findAll();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        for(FamAdPageModel famAd : famAds) {
            String abbreviation = famAd.getNewNewspaperAbbreviation();
            String pageNumber = famAd.getPageNumber();
            String date = famAd.getDate();
            String adName = famAd.getName();

            String krantVanToenDir = "D:\\FamAds\\" + abbreviation + "\\" + date + "\\" + pageNumber + "\\" + "Krant Van Toen" + "\\" + adName + ".jpg";
            String ndcDir = "D:\\FamAds\\" + abbreviation + "\\" + date + "\\" + pageNumber + "\\" + "NDC\\";

            File ndcFolder = new File(ndcDir);
            List<File> ndcFiles = pdfToImg.addFilesToList(ndcFolder);

            for (File ndcFile : ndcFiles) {
                String ndcName = ndcFile.getName();

                ndcDir += ndcName;

                System.out.println("Started....");
                System.out.println("Loading images...");
                Mat objectImage = imread(krantVanToenDir, CV_LOAD_IMAGE_COLOR);
                Mat sceneImage = imread(ndcDir, CV_LOAD_IMAGE_COLOR);

                System.out.println(krantVanToenDir + " VS " + ndcDir);

                compareFeatures(objectImage, sceneImage, ndcDir);
            }
        }
    }

    private void compareFeatures(Mat objectImage, Mat sceneImage, String ndcDir) throws InterruptedException {
        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.AKAZE);
        System.out.println("Detecting key points...");
        featureDetector.detect(objectImage, objectKeyPoints);
        KeyPoint[] keypoints = objectKeyPoints.toArray();
        System.out.println(keypoints);

        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.AKAZE);
        System.out.println("Computing descriptors...");
        descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);

        // Create the matrix for output image.
        Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(), CV_LOAD_IMAGE_COLOR);
        Scalar newKeypointColor = new Scalar(255, 0, 0);

        System.out.println("Drawing key points on object image...");
        Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);

        // Match object image with the scene image
        MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
        MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
        System.out.println("Detecting key points in background image...");
        featureDetector.detect(sceneImage, sceneKeyPoints);
        System.out.println("Computing descriptors in background image...");
        descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);

        List<MatOfDMatch> matches = new LinkedList<>();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);
        System.out.println("Matching object and scene images...");
        descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);

        System.out.println("Calculating good match list...");
        LinkedList<DMatch> goodMatchesList = new LinkedList<>();

        float nndrRatio = 0.7f;

        for (int i = 0; i < matches.size(); i++) {
            MatOfDMatch matofDMatch = matches.get(i);
            DMatch[] dmatcharray = matofDMatch.toArray();
            DMatch m1 = dmatcharray[0];
            DMatch m2 = dmatcharray[1];

            if (m1.distance <= m2.distance * nndrRatio) {
                goodMatchesList.addLast(m1);
            }
        }
        transformAndDraw(goodMatchesList, objectKeyPoints, sceneKeyPoints, objectImage, ndcDir, sceneImage, newKeypointColor, outputImage);
    }

    private void transformAndDraw(LinkedList<DMatch> goodMatchesList, MatOfKeyPoint objectKeyPoints,
                                  MatOfKeyPoint sceneKeyPoints, Mat objectImage,String ndcDir,
                                  Mat sceneImage, Scalar newKeypointColor, Mat outputImage) throws InterruptedException {

        Mat matchOutput = new Mat(sceneImage.rows() * 2, sceneImage.cols() * 2, CV_LOAD_IMAGE_COLOR);
        Scalar matchesColor = new Scalar(0, 255, 0);

        System.out.println("Matched points: " + goodMatchesList.size());

        if (goodMatchesList.size() >= 35) {
            System.out.println("Object Found!!!");

            List<KeyPoint> objKeypointlist = objectKeyPoints.toList();
            List<KeyPoint> scnKeypointlist = sceneKeyPoints.toList();

            LinkedList<Point> objectPoints = new LinkedList<>();
            LinkedList<Point> scenePoints = new LinkedList<>();

            for (DMatch aGoodMatchesList : goodMatchesList) {
                objectPoints.addLast(objKeypointlist.get(aGoodMatchesList.queryIdx).pt);
                scenePoints.addLast(scnKeypointlist.get(aGoodMatchesList.trainIdx).pt);
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

            System.out.println("Transforming object corners to scene corners...");
            Core.perspectiveTransform(obj_corners, scene_corners, homography);

            Mat img = imread(ndcDir, CV_LOAD_IMAGE_COLOR);

            line(img, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 4);
            line(img, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 4);
            line(img, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 4);
            line(img, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)), new Scalar(0, 255, 0), 4);

            System.out.println("Drawing matches image...");
            MatOfDMatch goodMatches = new MatOfDMatch();
            goodMatches.fromList(goodMatchesList);

            Features2d.drawMatches(objectImage, objectKeyPoints, sceneImage, sceneKeyPoints, goodMatches, matchOutput, matchesColor, newKeypointColor, new MatOfByte(), 2);

            imwrite("D:\\Matches\\outputImage.jpg", outputImage);
            imwrite("D:\\Matches\\matchoutput.jpg", matchOutput);
            imwrite("D:\\Matches\\img.jpg", img);
        } else {
            System.out.println("Object Not Found");
        }
        System.out.println("Ended....");
    }
}