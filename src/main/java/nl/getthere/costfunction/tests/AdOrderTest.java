package nl.getthere.costfunction.tests;

import nl.getthere.svggenerator.components.Page;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static nl.getthere.costfunction.CriteriaFunctions.evaluateRanking;
import static nl.getthere.costfunction.CriteriaFunctions.isAdInCorrectRankingOrder;
import static nl.getthere.costfunction.GeneralizedFunctions.makeFamAdPage;
import static nl.getthere.costfunction.testdata.AdCollection.getRectList;

public class AdOrderTest {

    private static final Logger logger = LoggerFactory.getLogger(AdOrderTest.class);

    @Test
    public void adOrderUnitTest() {
        Page page = makeFamAdPage(getRectList());
        boolean result = isAdInCorrectRankingOrder(page);

        if (!result) {
            int score = partialOrderTest();

            if(score >= 10)
                result = true;
        }

        assertTrue(result);
    }

    private int partialOrderTest() {
        int score = evaluateRanking(10);
        logger.info("Score is: " + score);

        return score;
    }
}
