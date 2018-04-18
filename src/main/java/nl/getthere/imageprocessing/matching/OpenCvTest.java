package nl.getthere.imageprocessing.matching;

import nl.getthere.dkvt_crawler.models.FamAdPageModel;
import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import nl.getthere.imageprocessing.repositories.NDCRepository;
import nl.getthere.mapstructure.PdfToImg;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class OpenCvTest {

    @Autowired
    private NDCRepository ndcRepository;

    @Autowired
    private FamAdRepository famAdRepository;

    @Autowired
    private PdfToImg pdfToImg;

    public void match() throws IOException {
        List<FamAdPageModel> famAds = famAdRepository.findAll();

        for(FamAdPageModel famAd : famAds) {
            String abbreviation = famAd.getNewNewspaperAbbreviation();
            String pageNumber = famAd.getPageNumber();
            String date = famAd.getDate();
            String adName = famAd.getName();

            String krantVanToenDir = "D:\\FamAds\\" + abbreviation + "\\" + date + "\\" + pageNumber + "\\" + "Krant Van Toen" + "\\" + adName + ".jpg";
            String ndcDir = "D:\\FamAds\\" + abbreviation + "\\" + date + "\\" + pageNumber + "\\" + "NDC";

            File ndcFolder = new File(ndcDir);
            List<File> ndcFiles = pdfToImg.addFilesToList(ndcFolder);

            for (File ndcFile : ndcFiles) {
                String ndcName = ndcFile.getName();

                int ret;
                ret = compareFeature(krantVanToenDir, ndcDir + "\\" + ndcName);

                if (ret > 0) {
                    System.out.println("Two images are same.");
                } else {
                    System.out.println("Two images are different.");
                }
            }
        }
    }

    /**
     * Compare that two images is similar using feature mapping
     * @param dkvtFile - the first image
     * @param ndcFile - the second image
     * @return integer - count that has the similarity within images
     */
    public static int compareFeature(String dkvtFile, String ndcFile) {
        int retVal = 0;
        long startTime = System.currentTimeMillis();

        System.out.println(dkvtFile + " VS " + ndcFile);

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Load images to compare
        Mat img1 = Imgcodecs.imread(dkvtFile, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        Mat img2 = Imgcodecs.imread(ndcFile, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        // Declare key point of images
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();

        // Definition of ORB key point detector and descriptor extractors
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.GRID_ORB);
        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.OPPONENT_ORB);

        // Detect key points
        detector.detect(img1, keypoints1);
        detector.detect(img2, keypoints2);

        // Extract descriptors
        extractor.compute(img1, keypoints1, descriptors1);
        extractor.compute(img2, keypoints2, descriptors2);

        // Definition of descriptor matcher
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);

        // Match points of two images
        MatOfDMatch matches = new MatOfDMatch();

        // Avoid to assertion failed
        if (descriptors2.cols() == descriptors1.cols()) {
            matcher.match(descriptors1, descriptors2 ,matches);

            // Check matches of key points
            DMatch[] match = matches.toArray();
            double max_dist = 0;
            double min_dist = 100;

            for (int i = 0; i < descriptors1.rows(); i++) {
                double dist = match[i].distance;
                if( dist < min_dist ) min_dist = dist;
                if( dist > max_dist ) max_dist = dist;
            }
            System.out.println("max_dist=" + max_dist + ", min_dist=" + min_dist);

            // Extract good images (distances are under 10)
            for (int i = 0; i < descriptors1.rows(); i++) {
                if (match[i].distance <= 10) {
                    retVal++;
                }
            }
            System.out.println("matching count=" + retVal);
        }

        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("estimatedTime=" + estimatedTime + "ms");

        return retVal;
    }
}