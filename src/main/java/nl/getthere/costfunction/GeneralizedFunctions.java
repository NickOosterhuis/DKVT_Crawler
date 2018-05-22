package nl.getthere.costfunction;

import nl.getthere.svggenerator.components.Page;
import nl.getthere.svggenerator.components.Rect;

import java.util.ArrayList;

public class GeneralizedFunctions {

    public static Page makeFamAdPage(ArrayList<Rect> supply) {
        Page page = new Page();
        page.createPage();
        page.setSupply(supply);

        return page;
    }
}
