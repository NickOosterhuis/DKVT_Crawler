package nl.getthere.costfunction.tests;

import nl.getthere.svggenerator.components.Page;
import nl.getthere.svggenerator.components.Rect;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static nl.getthere.costfunction.AdCollection.getRectList;
import static nl.getthere.costfunction.CriteriaFunctions.overlap;
import static nl.getthere.costfunction.GeneralizedFunctions.makeFamAdPage;
import static org.junit.Assert.assertFalse;

public class AdOverlapTest {

    @Test
    public void overlapUnitTest() {
        Page page = makeFamAdPage(getRectList());

        if(page != null) {
            ArrayList<Rect> rectangles = page.getSupply();

            for(int i = 0; i < rectangles.size(); i++) {
                for (int j = 1; j< rectangles.size(); j++) {
                    if(i == j)
                        continue;

                    Rectangle r1 = rectangles.get(i).getRectangle();
                    Rectangle r2 = rectangles.get(j).getRectangle();

                    boolean result = overlap(r1, r2);
                    assertFalse(result);
                }
            }
        }
    }
}
