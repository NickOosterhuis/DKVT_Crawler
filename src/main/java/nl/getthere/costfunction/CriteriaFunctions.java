package nl.getthere.costfunction;

import nl.getthere.svggenerator.components.Page;
import nl.getthere.svggenerator.components.Rect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 *
 * These functions are used for a costfunction in order to evaluate FamAdPages on 14 different criteria
 *
 * @author Nick Oosterhuis
 */
public class CriteriaFunctions {

    private final static double WHITESPACE_LIMIT = 15;

    private static HashMap<Rect, Rect> misPlacedAds;

    private static final Logger logger = LoggerFactory.getLogger(CriteriaFunctions.class);

    /**
     * This function checks if FamAds are intersecting with each other
     *
     * @param page with a supply of advertisements
     * @return boolean
     */
    public static boolean isIntersecting(Page page) {
        ArrayList<Rect> rectangles = page.getSupply();

        for (int i = 0; i < rectangles.size(); i++) {
            for (int j = 1; j < rectangles.size(); j++) {
                if (i == j)
                    continue;

                Rectangle r1 = rectangles.get(i).getRectangle();
                Rectangle r2 = rectangles.get(j).getRectangle();

                logger.info("X: coordinate: R1: " + r1.x + " R2: " + r2.x + "\n"
                            + "Y: coordinate: R1: " + r1.y + " R2: " + r2.y + "\n"
                            + "WIDTH: R1: " + r1.width + " R2" + r2.width + "\n"
                            + "HEIGHT: R1 " + r1.height + " R2 " + r2.height + "\n");

                boolean result = r1.x < r2.x + r2.width && r1.x + r1.width > r2.x && r1.y < r2.y + r2.height && r1.y + r1.height > r2.y;

                if (result)
                    return true;
            }
        }
        return false;
    }

    /**
     * This function checks if a FamAd is transcending the newspaper page
     *
     * @param page with a supply of advertisements
     * @return boolean
     */
    public static boolean isAdPageTranscending(Page page) {
        ArrayList<Rect> rectangles = page.getSupply();
        logger.info("Page size: WIDTH: " + page.getWidth() + " HEIGHT: " + page.getHeight());

        for (Rect rect : rectangles) {
            Rectangle r = rect.getRectangle();

            logger.info("X coordinate: R1: " + r.x + " Y coordinate: " + r.y + "\n"
                        + "R1 Size: WIDTH: " + r.width + " HEIGHT: " + r.height + "\n");

            boolean result = r.x > page.getWidth() || r.y > page.getHeight() || r.x + r.width < 0 || r.y + r.height < 0;

            if(result)
                return true;
        }
        return false;
    }

    /**
     * This function checks the space between rectangles and evaluates if this is acceptable
     *
     * @param page with a supply of advertisements
     * @return boolean
     */
    public static boolean isAcceptableWhiteSpace(Page page) {
        ArrayList<Rect> rectangles = page.getSupply();
        int x1, x2, y1, y2;
        double distance;

        for (Rect rect : rectangles) {
            Map<Rectangle, List<Double>> distanceToRectangleMap = new HashMap<>();
            List<Double> distances = new ArrayList<>();
            Rectangle r1 = rect.getRectangle();

            for (Rect rectangle : rectangles) {
                Rectangle r2 = rectangle.getRectangle();

                if (r1 != r2) {
                    x1 = r1.x; y1 = r1.y; x2 = r2.x; y2 = r2.y;
                    distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
                    distances.add(distance);
                    logger.info("Distance between (" + x1 + ", " + y1 + ") and (" + x2 + ", " + y2 + ") is: " + distance);
                }
            }
            System.out.println();
            distanceToRectangleMap.put(r1, distances);
            double min = minimalValue(distanceToRectangleMap);

            logger.info("Minimum distance: " + min);

            if (min >= WHITESPACE_LIMIT) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper function to get the minimal value of a list from a hash map
     *
     * @param distanceToRectangleMap hashmap with rectangles coupled distances
     * @return boolean
     */
    private static double minimalValue(Map<Rectangle,List<Double>> distanceToRectangleMap) {
       double min = 0;

        for (Object o : distanceToRectangleMap.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            List<Double> values = (List<Double>) pair.getValue();
            min = Collections.min(values);
        }
        return min;
    }

    /**
     * This method checks the order of the advertisements on family name, ranking and position where it is placed.
     *
     * @param page with a supply of advertisements
     * @return boolean
     */
    public static boolean isAdInCorrectRankingOrder(Page page) {
        ArrayList<Rect> rectangles = page.getSupply();
        List<Boolean> rankingOrders = new ArrayList<>();
        boolean isCorrectRankingOrder = false;
        misPlacedAds = new HashMap<>();

        for (Rect rect1 : rectangles) {
            String famName1 = rect1.getFamMemberName();
            int ranking1 = Integer.parseInt(rect1.getRanking());
            Rectangle r1 = rect1.getRectangle();
            int x1 = r1.x;
            int y1 = r1.y;

            for (Rect rect2 : rectangles) {
                if (rect1 != rect2) {
                    String famName2 = rect2.getFamMemberName();
                    int ranking2 = Integer.parseInt(rect2.getRanking());
                    Rectangle r2 = rect2.getRectangle();
                    int x2 = r2.x;
                    int y2 = r2.y;

                    if (famName1.equals(famName2)) {
                        logger.info("Fam names are the same: Ad1: " + famName1 + " & Ad2: " + famName2 + "\n"
                                    + "Ranking is : Ad 1: " + ranking1 + " & Ad2: " + ranking2 + "\n"
                                    + "Ad Position 1: (" + x1 + "," + y1 + ") Ad Position 2: (" + x2 + "," + y2 +")" + "\n" );

                        if ((ranking1 < ranking2 && x1 >= x2 && y1 >= y2) || (ranking1 > ranking2 && x1 <= x2 && y1 <= y2)) {
                            logger.info("Position is not right" + "\n");
                            misPlacedAds.put(rect1, rect2);
                            rankingOrders.add(false);
                        } else {
                            logger.info("Position is right" + "\n");
                        }
                    }
                }
            }
        }
        if (rankingOrders.isEmpty())
            isCorrectRankingOrder = true;

        return isCorrectRankingOrder;
    }

    public static int evaluateRanking(int points) {

        logger.info("Total misplaced ads: " + misPlacedAds.size());

        if (!misPlacedAds.isEmpty()) {
            for (Object o : misPlacedAds.entrySet()) {
                Map.Entry pair = (Map.Entry) o;
                Rect rect1 = (Rect) pair.getKey();
                Rect rect2 = (Rect) pair.getValue();

                int ranking1 = Integer.parseInt(rect1.getRanking());
                Rectangle r1 = rect1.getRectangle();
                int x1 = r1.x;
                int y1 = r1.y;

                int ranking2 = Integer.parseInt(rect2.getRanking());
                Rectangle r2 = rect2.getRectangle();
                int x2 = r2.x;
                int y2 = r2.y;

                if ((ranking1 < ranking2 && x1 >= x2 && y1 >= y2 && ranking1 < 5 && ranking2 < 5) || (ranking1 > ranking2 && x1 <= x2 && y1 <= y2 && ranking1 < 5)) {
                    logger.info("Ranking is off" + "Ranking AD1: " + ranking1 + " AD2: " + ranking2 + "\n"
                    + "Points decreased -1" + "\n");
                    points -= 1;
                }
            }
        }
        return points;
    }
}
