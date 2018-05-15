package nl.getthere.svggenerator;

import nl.getthere.dkvt_crawler.models.FamAdPageModel;
import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import nl.getthere.svggenerator.components.Page;
import nl.getthere.svggenerator.components.Rect;
import nl.getthere.svggenerator.generator.SvgGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static nl.getthere.svggenerator.constants.ConstantsNDC.*;

@Component
public class Generate {

    @Autowired
    private FamAdRepository famAdRepository;

    @Autowired
    private SvgGenerator svgGenerator;

    private String abbreviation;
    private String date;
    private String pageNumber;

    private void generate() {
        if (date.equals("20171227"))
            date = "20171228";

        List<FamAdPageModel> famAds = famAdRepository.findAllByNewNewspaperAbbreviationAndDateAndPageNumberAndFamAdNdcDataModelAlgorithmCategory(
                abbreviation, date, pageNumber, 4);

        Page page = new Page();
        page.createPage();

        ArrayList<Page> pages = new ArrayList<Page>();
        pages.add(page);

        ArrayList<Rect> supply = new ArrayList<Rect>();

        for (FamAdPageModel famAd : famAds) {
            int width = famAd.getFamAdPropertyModel().getWidth();
            int height = famAd.getFamAdPropertyModel().getHeight();
            int x = famAd.getFamAdPropertyModel().getX();
            int y = famAd.getFamAdPropertyModel().getY();
            String adNumber = famAd.getName();

            supply.add(createAd(width, height, adNumber, y, x));
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
            if (file.isDirectory()){
                if (file.getName().startsWith("F") || file.getName().startsWith("G")) {
                    abbreviation = file.getName();
                    System.out.println(abbreviation);
                } else if (file.getName().startsWith("2")) {
                    date = file.getName();
                    System.out.println(date);
                } else if (file.getName().startsWith("0")) {
                    pageNumber = file.getName();
                    System.out.println(pageNumber);
                }
                listSubDirectories(file.getAbsolutePath());
                generate();
            }
        }
    }
}
