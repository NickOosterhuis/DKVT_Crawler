package nl.getthere.helpers;

import nl.getthere.dkvt_crawler.models.FamAdPageModel;
import nl.getthere.dkvt_crawler.repositories.FamAdRepository;
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

    public void formatData() {
        List<FamAdPageModel> famAds = famAdRepository.findAllByFamAdNdcDataModelAlgorithmCategory(4);

        for(FamAdPageModel famAd: famAds) {
            String note = famAd.getFamAdNdcDataModel().getNote();
            String[] splittedNote = formatNote(note);

            famAd.getFamAdNdcDataModel().setFamilyMemberName(splittedNote[0]);
            famAd.getFamAdNdcDataModel().setRanking(splittedNote[1]);

            famAdRepository.save(famAd);
        }
    }

    private String[] formatNote(String note) {
        String[] splitted = note.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        return splitted;
    }
}
