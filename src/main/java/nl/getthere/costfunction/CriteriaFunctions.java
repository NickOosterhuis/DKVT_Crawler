package nl.getthere.costfunction;

import nl.getthere.svggenerator.components.Page;
import nl.getthere.svggenerator.components.Rect;

import java.awt.*;
import java.util.ArrayList;

/**
 *
 * These functions are used for a costfunction in order to evaluate FamAdPages on 14 different criteria
 *
 * @author Nick Oosterhuis
 */
public class CriteriaFunctions {

    /**
     * The function checks if FamAds are overlapping with each other
     *
     * @param page with a supply of advertisements
     * @return boolean
     */
    public static boolean isOverlapping(Page page) {
        ArrayList<Rect> rectangles = page.getSupply();

        for (int i = 0; i < rectangles.size(); i++) {
            for (int j = 1; j < rectangles.size(); j++) {
                if (i == j)
                    continue;

                Rectangle r1 = rectangles.get(i).getRectangle();
                Rectangle r2 = rectangles.get(j).getRectangle();

                System.out.println("X: coordinate: R1: " + r1.x + " R2: " + r2.x);
                System.out.println("Y: coordinate: R1: " + r1.y + " R2: " + r2.y);
                System.out.println("WIDTH: R1: " + r1.width + " R2" + r2.width);
                System.out.println("HEIGHT: R1 " + r1.height + " R2 " + r2.height);
                System.out.println();

                boolean result = r1.x < r2.x + r2.width && r1.x + r1.width > r2.x && r1.y < r2.y + r2.height && r1.y + r1.height > r2.y;

                if (result)
                    return result;
            }
        }
        return false;
    }

    /**
     * The function checks if a FamAd is transcending the newspaper page
     *
     * @param page with a supply of advertisements
     * @return boolean
     */
    public static boolean isAdPageTranscending(Page page) {
        ArrayList<Rect> rectangles = page.getSupply();
        System.out.println("Page size: WIDTH: " + page.getWidth() + " HEIGHT: " + page.getHeight());

        for (Rect rect : rectangles) {
            Rectangle r = rect.getRectangle();

            System.out.println("X coordinate: R1: " + r.x + " Y coordinate: " + r.y);
            System.out.println("R1 Size: WIDTH: " + r.width + " HEIGHT: " + r.height);

            System.out.println();

            boolean result = r.x > page.getWidth() || r.y > page.getHeight() || r.x + r.width < 0 || r.y + r.height < 0;

            if(result)
                return result;
        }
        return false;
    }

}
