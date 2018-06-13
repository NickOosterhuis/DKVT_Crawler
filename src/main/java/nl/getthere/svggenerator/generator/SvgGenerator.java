package nl.getthere.svggenerator.generator;

import nl.getthere.svggenerator.components.Rect;
import nl.getthere.svggenerator.components.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;

@Component
public class SvgGenerator {

    private static final Logger logger = LoggerFactory.getLogger(SvgGenerator.class);

    public void write(ArrayList<Page> pages, String abbreviation, String date, String pageNumber) {
        try{
            for(Page page: pages) {

                logger.info("Advertenties gevonden: " + page.getSupply().size());

                File svg = new File("D:\\FamAdSVGS\\" + abbreviation + "\\" + date + "\\" + pageNumber + "\\" + "opgemaakt.svg");

                if(!svg.exists())
                    svg.getParentFile().mkdirs();

                Writer writer = new BufferedWriter(new FileWriter(svg));

                writer.write("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">\n");
                writer.write("<rect width=\"" + page.getWidth() + "\" height=\"" + page.getHeight() + "\" style=\"fill:gray\"/>\n");
                addParts(page.getSupply(), writer);
                writer.write("</svg>");
                writer.close();

                logger.info("File printed on location: " + svg.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addParts(ArrayList<Rect> rects, Writer writer) throws IOException {
        int padding = 4;
        for (Rect rect: rects) {
            int x = (rect.getX());
            int y = (rect.getY());
            int id = (rect.getId());

            writer.write("<g>" + "<rect id=\"" + id + "\" x=\"" + (x + padding) + "\" y=\"" + (y + padding) + "\" width=\"" + ((rect.getRectangle().getWidth() - padding)) + "\" height=\"" + ((rect.getRectangle().getHeight() - padding))
                    + "\" style=\"fill:" + "blue" + ";stroke-width:" + padding + ";stroke:black\" ></rect>" + "<text x=\"" + (x + padding) + "\" y=\"" + (y + 20) + "\" fill=\"snow\" >"
                    + rect.getName() + "</text>" + "</g>\n");
        }
    }
}
