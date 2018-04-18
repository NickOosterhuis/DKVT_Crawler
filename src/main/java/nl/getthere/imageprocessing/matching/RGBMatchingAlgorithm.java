package nl.getthere.imageprocessing.matching;

import nl.getthere.dkvt_crawler.models.FamAdPageModel;
import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import nl.getthere.imageprocessing.repositories.NDCRepository;
import nl.getthere.mapstructure.PdfToImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Component
public class RGBMatchingAlgorithm {

    @Autowired
    private NDCRepository ndcRepository;

    @Autowired
    private FamAdRepository famAdRepository;

    @Autowired
    private PdfToImg pdfToImg;

    public BufferedImage match() throws IOException {

        List<FamAdPageModel> famAds = famAdRepository.findAll();
        BufferedImage winner = null;

        for(FamAdPageModel famAd : famAds) {

            HashMap<Double, BufferedImage> potentialWinners = new HashMap<>();

            String abbreviation = famAd.getNewNewspaperAbbreviation();
            String pageNumber = famAd.getPageNumber();
            String date = famAd.getDate();
            String adName = famAd.getName();

            String krantVanToenDir = "D:\\FamAds\\" + abbreviation + "\\" + date + "\\" + pageNumber + "\\" + "Krant Van Toen" + "\\" + adName + ".jpg";
            String ndcDir = "D:\\FamAds\\" + abbreviation + "\\" + date + "\\" + pageNumber + "\\" + "NDC";

            File dkvtFile = new File(krantVanToenDir);
            String dkvtName = dkvtFile.getName();
            BufferedImage dkvtImage = ImageIO.read(dkvtFile);

            File ndcFolder = new File(ndcDir);
            List<File> ndcFiles = pdfToImg.addFilesToList(ndcFolder);


            for (File ndcFile : ndcFiles) {
                String ndcName = ndcFile.getName();
                BufferedImage ndcImage = ImageIO.read(ndcFile);

                double percentage = Math.round(getDifferencePercent(dkvtImage, ndcImage, dkvtName, ndcName));
                potentialWinners.put(percentage, ndcImage);

                System.out.println(percentage);
            }

            winner = showWinner(potentialWinners);
            System.out.println(winner);
        }
        return winner;
    }

    private BufferedImage showWinner(HashMap<Double, BufferedImage> potentialWinners) {

        double minimalValue = Collections.min(potentialWinners.keySet());
        BufferedImage winner = potentialWinners.get(minimalValue);

        return winner;
    }

    private Double getDifferencePercent(BufferedImage img1, BufferedImage img2, String dkvtName, String ndcName) {
        img1 = scaleImage(img1);
        img2 = scaleImage(img2);

        int width1 = img1.getWidth();
        int height1 = img1.getHeight();
        int width2 = img2.getWidth();
        int height2 = img2.getHeight();

        if ((width1 != width2) || (height1 != height2)) {
            throw new IllegalArgumentException("Error: Images dimensions mismatch");
        }

        long diff = 0;
        for (int y = 0; y < height1; y++) {
            for (int x = 0; x < width1; x++) {
                diff += pixelDiff(img1.getRGB(x, y), img2.getRGB(x, y));
            }
        }
        long maxDiff = 3L * 255 * width1* height1;

        double percentage = 100.0 * diff / maxDiff;
        System.out.println("Difference = " + percentage + ", img name = " + dkvtName + " VS " + ndcName);

        return percentage;
    }

    private int pixelDiff(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >>  8) & 0xff;
        int b1 =  rgb1        & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >>  8) & 0xff;
        int b2 =  rgb2        & 0xff;
        return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
    }

    private BufferedImage scaleImage(BufferedImage img) {
        Image thumbnail = img.getScaledInstance(4, 4, Image.SCALE_AREA_AVERAGING);
        return toBufferedImage(thumbnail);
    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}
