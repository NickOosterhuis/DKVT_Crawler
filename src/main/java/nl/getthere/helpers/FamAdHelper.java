package nl.getthere.helpers;

import nl.getthere.dkvt_crawler.models.FamAdModel;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class FamAdHelper {
    public static String abbreviation;
    public static String date;
    public static String pageNumber;
    public static String krantVanToenDir;
    public static String ndcDir;
    public static String adName;
    public static FamAdModel currentFamAd;

    public void directoryMapping(File file) {
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
    }

    public static void setDirs(List<FamAdModel> famAds) {

        for(FamAdModel famAd : famAds) {
            String abbr = famAd.getNewAbbreviation();
            String pageNum = famAd.getPageNumber();
            String date = famAd.getDate();
            adName = famAd.getName();
            currentFamAd = famAd;

            if(date.equals("20171228")) {
                date = "20171227";
            }

            //set to wished path
            krantVanToenDir = "D:\\FamAds\\" + abbr + "\\" + date + "\\" + pageNum + "\\" + "Krant Van Toen" + "\\" + adName + ".jpg";
            ndcDir = "D:\\FamAds\\" + abbreviation + "\\" + date + "\\" + pageNumber + "\\" + "NDC\\";
        }
    }
}
