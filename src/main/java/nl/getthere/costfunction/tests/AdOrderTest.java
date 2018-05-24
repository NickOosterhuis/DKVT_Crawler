package nl.getthere.costfunction.tests;

import nl.getthere.svggenerator.components.Page;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static nl.getthere.costfunction.CriteriaFunctions.evaluateRanking;
import static nl.getthere.costfunction.CriteriaFunctions.isAdInCorrectRankingOrder;
import static nl.getthere.costfunction.GeneralizedFunctions.makeFamAdPage;
import static nl.getthere.costfunction.testdata.AdCollection.getRectList;

public class AdOrderTest {

    @Test
    public void adOrderUnitTest() {
        Page page = makeFamAdPage(getRectList());
        boolean result = isAdInCorrectRankingOrder(page);

        if (!result)
            evaluateOrderUnitTest();

        assertTrue(result);
    }

    @Test
    public void evaluateOrderUnitTest() {
        int score = evaluateRanking(10);
        System.out.println("Score is: " + score);

        assertEquals(10, score);
    }
}
