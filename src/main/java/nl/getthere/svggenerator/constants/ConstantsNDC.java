package nl.getthere.svggenerator.constants;

import nl.getthere.svggenerator.components.Rect;

public class ConstantsNDC {

    public static Rect createAd(int column, int height, String name, int x, int y, int id) {
        Rect ad = new Rect(column, height, id, name);
        ad.setX(x);
        ad.setY(y);
        ad.setName(name);
        ad.setId(id);
        return ad;
    }

    public static Rect createAd(int column, int height, int x, int y, String ranking, String pageNumber) {
        Rect ad = new Rect(column, height, x, y);
        ad.setX(x);
        ad.setY(y);
        ad.setRanking(ranking);
        ad.setPageNumber(pageNumber);
        return ad;
    }

    public static Rect createAd(int column, int height, int x, int y) {
        Rect ad = new Rect(column, height);
        ad.setX(x);
        ad.setY(y);
        return ad;
    }

    public static Rect createBlocker(int column, int height, int x, int y) {
        Rect r = new Rect(column, height);
        r.setX(x);
        r.setY(y);
        return r;
    }
}
