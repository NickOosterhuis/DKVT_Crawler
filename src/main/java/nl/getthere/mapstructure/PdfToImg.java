package nl.getthere.mapstructure;

import nl.getthere.imageprocessing.models.NDCModel;
import nl.getthere.imageprocessing.repositories.NDCRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class PdfToImg {

    @Autowired
    private NDCRepository ndcRepository;

    private final File directory = new File("D:\\testset familieberichten GKA_FBN_GEM_2017\\testset familieberichten");

    public void makeMapStructure() {

        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        List<File> files = addFilesToList(directory);
        Long filenameNumber;
        Format formatter = new SimpleDateFormat("YYYMMdd");

        for (File file : files) {
            String fileName = file.getName();
            filenameNumber = parseFileName(fileName);

            //gathering from database
            NDCModel model = ndcRepository.findByMaterialId(filenameNumber);
            String abbreviation = model.getEditionCode();
            String date = formatter.format(model.getPublicationDate());
            String adStatus = model.getAdStatus();
            int pageNumber = model.getPlacedOnPage();
            String pageNumberString  = String.format("%03d", pageNumber);

            if(!adStatus.equals("Comfirmed") && pageNumber!= 0) {
                File mapStructure = new File("D:\\FamAds\\" + abbreviation + "\\" + date + "\\" + pageNumberString + "\\NDC");

                if(mapStructure.exists()) {
                    System.out.println("Folder Deleted ->" + mapStructure.getAbsolutePath());
                    mapStructure.delete();
                }

                if (!mapStructure.exists()) {
                    mapStructure.mkdirs();
                    System.out.println("Folder Created ->" + mapStructure.getAbsolutePath());
                }

                try {
                    if (filenameNumber == model.getMaterialId()) {
                        PDDocument document = PDDocument.load(file);
                        PDFRenderer pdfRenderer = new PDFRenderer(document);

                        for (int page = 0; page < document.getNumberOfPages(); page++ ) {
                            BufferedImage image = pdfRenderer.renderImageWithDPI(page, 100);
                            File output = new File(mapStructure + "\\" + filenameNumber.toString() + ".jpg");
                            ImageIO.write(image, "jpg", output);
                        }
                        System.out.println("Image saved at -> " + mapStructure.getAbsolutePath());
                        document.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<File> addFilesToList(File directory) {
        List<File> files = new ArrayList<>();

        try{
            File[] arrayFiles = directory.listFiles();

            for (File file : arrayFiles) {
                if (file.isDirectory()) {
                    files.addAll(addFilesToList(file));
                } else if (file.getName().endsWith(".pdf") || file.getName().endsWith(".jpg")) {
                    files.add(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    private Long parseFileName(String fileName) {

        if (fileName.endsWith(".pdf")) {
            fileName = fileName.replaceAll(".pdf", "");
        }
        return Long.parseLong(fileName);
    }
}
