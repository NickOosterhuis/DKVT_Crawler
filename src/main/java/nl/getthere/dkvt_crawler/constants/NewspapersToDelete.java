package nl.getthere.dkvt_crawler.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * When crawling every newspaper, it deletes the abbreviations from the database which are old newspapers
 *
 * @author Nick Oosterhuis
 */
public class NewspapersToDelete {

    private static final String ANB = "AMB";
    private static final String NVHN = "NVHN";
    private static final String BIJ = "BIJ";
    private static final String BRU = "BRU";
    private static final String KHN = "KHN";
    private static final String MOH = "MOH";
    private static final String SSP = "SSP";
    private static final String HGL = "HGL";
    private static final String HAH = "HAH";
    private static final String KVC = "KVC";
    private static final String KVF = "KVF";
    private static final String MCK = "MCK";
    private static final String MFH = "MFH";
    private static final String MDW = "MDW";
    private static final String NVP = "NVP";
    private static final String NOL = "NOL";
    private static final String SSN = "SSN";
    private static final String WEZ = "WEZ";

    public static List<String> newspapersToDelete() {
        List<String> newspapersToDelete = new ArrayList<>();
        newspapersToDelete.add(ANB);
        newspapersToDelete.add(NVHN);
        newspapersToDelete.add(BIJ);
        newspapersToDelete.add(BRU);
        newspapersToDelete.add(KHN);
        newspapersToDelete.add(MOH);
        newspapersToDelete.add(SSP);
        newspapersToDelete.add(HGL);
        newspapersToDelete.add(HAH);
        newspapersToDelete.add(KVC);
        newspapersToDelete.add(KVF);
        newspapersToDelete.add(MCK);
        newspapersToDelete.add(MFH);
        newspapersToDelete.add(MDW);
        newspapersToDelete.add(NVP);
        newspapersToDelete.add(NOL);
        newspapersToDelete.add(SSN);
        newspapersToDelete.add(WEZ);

        return newspapersToDelete;
    }
}
