package nl.getthere.costfunction.tests;

import nl.getthere.svggenerator.components.Page;
import org.junit.Test;

import static nl.getthere.costfunction.testdata.AdCollection.getRectList;
import static nl.getthere.costfunction.CriteriaFunctions.isIntersecting;
import static nl.getthere.costfunction.GeneralizedFunctions.makeFamAdPage;
import static org.junit.Assert.assertFalse;

public class AdOverlapTest {

    @Test
    public void overlapUnitTest() {
        Page page = makeFamAdPage(getRectList());
        boolean result = isIntersecting(page);
        assertFalse(result);

    }
}
