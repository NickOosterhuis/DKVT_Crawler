package nl.getthere.costfunction;

import nl.getthere.svggenerator.components.Rect;

import java.util.ArrayList;

import static nl.getthere.svggenerator.constants.ConstantsNDC.createAd;

public class AdCollection {
    private static Rect ad1 = createAd(5, 5, 1, 1, "01", "1");
    private static Rect ad2 = createAd(5, 5, 10, 10, "02", "1");
    private static Rect ad3 = createAd(5, 5, 20, 20, "03", "1");
    private static Rect ad4 = createAd(5, 5, 30, 30, "04", "1");
    private static Rect ad5 = createAd(5, 5, 40, 40, "05", "1");
    private static Rect ad6 = createAd(5, 5, 50, 50, "06", "1");
    private static Rect ad7 = createAd(5, 5, 60, 60, "07", "1");
    private static Rect ad8 = createAd(5, 5, 70, 70, "08", "1");
    private static Rect ad9 = createAd(5, 5, 80, 80, "09", "1");
    private static Rect ad10 = createAd(5, 5, 90, 90, "10", "1");

    private static ArrayList<Rect> adCollection = new ArrayList<>();

    public static ArrayList<Rect> getRectList() {
        adCollection.add(ad1);
        adCollection.add(ad2);
        adCollection.add(ad3);
        adCollection.add(ad4);
        adCollection.add(ad5);
        adCollection.add(ad6);
        adCollection.add(ad7);
        adCollection.add(ad8);
        adCollection.add(ad9);
        adCollection.add(ad10);

        return adCollection;
    }
}
