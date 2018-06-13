package nl.getthere.helpers;

import nl.getthere.dkvt_crawler.models.FamAdModel;
import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class formats the strings saved in the db after image matching
 *
 * @author Nick Oosterhuis
 */
@Component
public class FormatNdcData {

    @Autowired
    private FamAdRepository famAdRepository;

    private static final Logger logger = LoggerFactory.getLogger(FormatNdcData.class);

    public void formatData() {
        List<FamAdModel> famAds = famAdRepository.findAllByFamAdNdcDataModelAlgorithmCategory(4);

        for(FamAdModel famAd: famAds) {
            String note = famAd.getFamAdNdcDataModel().getNote();
            String sectionCode = famAd.getFamAdNdcDataModel().getSectionCode();
            String[] splittedNote = formatNote(note);

            if (splittedNote.length >= 2 && sectionCode.equals("Overlijden")) {
                famAd.getFamAdNdcDataModel().setFamilyMemberName(splittedNote[0]);
                famAd.getFamAdNdcDataModel().setRanking(splittedNote[1]);
                famAdRepository.save(famAd);
            } else {
                logger.warn("Not the right section code!");
            }

        }
    }

    private String[] formatNote(String note) {
        String splitted[] = note.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        logger.info("array size = " + splitted.length);

        return splitted;
    }
}
