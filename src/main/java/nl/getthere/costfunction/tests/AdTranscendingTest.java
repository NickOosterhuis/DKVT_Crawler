package nl.getthere.costfunction.tests;

import nl.getthere.svggenerator.components.Page;
import org.junit.Test;

import static nl.getthere.costfunction.CriteriaFunctions.isAdPageTranscending;
import static nl.getthere.costfunction.GeneralizedFunctions.makeFamAdPage;
import static nl.getthere.costfunction.testdata.AdCollection.getRectList;
import static org.junit.Assert.assertFalse;

public class AdTranscendingTest {

    @Test
    public void transcendingAdsTest() {
        Page page = makeFamAdPage(getRectList());
        boolean result = isAdPageTranscending(page);
        assertFalse(result);
    }
}
