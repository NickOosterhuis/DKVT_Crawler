package nl.getthere.costfunction.tests;

import nl.getthere.svggenerator.components.Page;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static nl.getthere.costfunction.CriteriaFunctions.isAcceptableWhiteSpace;
import static nl.getthere.costfunction.GeneralizedFunctions.makeFamAdPage;
import static nl.getthere.costfunction.testdata.AdCollection.getRectList;

public class AdWhiteSpaceTest {

    @Test
    public void whiteSpaceTest() {
        Page page = makeFamAdPage(getRectList());
        boolean result = isAcceptableWhiteSpace(page);
        assertTrue(result);
    }
}
