package nl.getthere.costfunction.testdata;

import nl.getthere.svggenerator.components.Rect;

import java.util.ArrayList;

import static nl.getthere.svggenerator.constants.ConstantsNDC.createAd;

public class AdCollection {
    private static Rect ad1 = createAd(5, 5, 1, 10, "01", "1", "Jan");
    private static Rect ad2 = createAd(5, 5, 1, 1, "06", "1", "Jan");
    private static Rect ad3 = createAd(5, 5, 1, 20, "03", "1", "Jan");
    private static Rect ad4 = createAd(5, 5, 1, 30, "01", "1", "Klaas");
    private static Rect ad5 = createAd(5, 5, 10, 40, "02", "1", "Klaas");
    private static Rect ad6 = createAd(5, 5, 10, 50, "01", "1", "Arnold");
    private static Rect ad7 = createAd(5, 5, 10, 60, "01", "1", "Piet");
    private static Rect ad8 = createAd(5, 5, 10, 70, "02", "1", "Piet");
    private static Rect ad9 = createAd(5, 5, 20, 80, "03", "1", "Kees");
    private static Rect ad10 = createAd(50, 50, 20, 90, "04", "1", "Kees");

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
