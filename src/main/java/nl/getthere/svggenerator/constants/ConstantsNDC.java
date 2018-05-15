package nl.getthere.svggenerator.constants;

import nl.getthere.svggenerator.components.Rect;

import java.math.BigDecimal;

public class ConstantsNDC {
    public static int C1 = 41;
    public static int C2 = 86;
    public static int C3 = 131;
    public static int C4 = 176;
    public static int C5 = 221;
    public static int C6 = 266;

    public static int R1 = 45;
    public static int R2 = 94;
    public static int R3 = 143;
    public static int R4 = 192;
    public static int R5 = 241;
    public static int R6 = 290;
    public static int R7 = 339;
    public static int R8 = 388;

    public static Rect createAd(int column, int height, String id, int x, int y) {
        Rect ad = new Rect(column, height, id);
        ad.setX(x);
        ad.setY(y);
        ad.setName(id);
        return ad;
    }

    public static Rect createBlocker(int column, int height, int x, int y) {
        Rect r = new Rect(column, height);
        r.setX(x);
        r.setY(y);
        return r;
    }
}
