package nl.getthere.svggenerator;

import nl.getthere.dkvt_crawler.models.FamAdModel;
import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import nl.getthere.helpers.FamAdHelper;
import nl.getthere.svggenerator.components.Page;
import nl.getthere.svggenerator.components.Rect;
import nl.getthere.svggenerator.generator.SvgGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static nl.getthere.helpers.FamAdHelper.abbreviation;
import static nl.getthere.helpers.FamAdHelper.pageNumber;
import static nl.getthere.helpers.FamAdHelper.date;
import static nl.getthere.svggenerator.constants.ConstantsNDC.*;

@Component
public class GenerateSvgFiles {

    @Autowired
    private FamAdRepository famAdRepository;

    @Autowired
    private SvgGenerator svgGenerator;

    @Autowired
    private FamAdHelper famAdHelper;

    private void generate() {
        List<FamAdModel> famAds = famAdRepository.findAllByNewAbbreviationAndDateAndPageNumberAndFamAdNdcDataModelAlgorithmCategory(
                abbreviation, date, pageNumber, 4);

        Page page = new Page();
        page.createPage();

        ArrayList<Page> pages = new ArrayList<Page>();
        pages.add(page);

        ArrayList<Rect> supply = new ArrayList<Rect>();

        int id = 0;
        for (FamAdModel famAd : famAds) {
            int width = famAd.getFamAdPropertyModel().getWidth();
            int height = famAd.getFamAdPropertyModel().getHeight();
            int x = famAd.getFamAdPropertyModel().getX();
            int y = famAd.getFamAdPropertyModel().getY();
            String adNumber = famAd.getName();
            id++;

            supply.add(createAd(width, height, adNumber, y, x, id));
        }
        page.setSupply(supply);
        svgGenerator.write(pages, abbreviation, date, pageNumber);
    }

    /**
     * List all subdirectories
     * @param directoryName to be listed
     */
    public void listSubDirectories(String directoryName){
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList){
            if(file.isDirectory()) {
                famAdHelper.directoryMapping(file);
                listSubDirectories(file.getAbsolutePath());
            }
            generate();
        }
    }
}
