package nl.getthere.svggenerator.generator;

import nl.getthere.svggenerator.components.Rect;
import nl.getthere.svggenerator.components.Page;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;

@Component
public class SvgGenerator {

    public void write(ArrayList<Page> pages, String abbreviation, String date, String pageNumber) {
        try{
            for(Page page: pages) {

                System.out.println("Advertenties gevonden: " + page.getSupply().size());

                File svg = new File("D:\\FamAdSVGS\\" + abbreviation + "\\" + date + "\\" + pageNumber + "\\" + "opgemaakt.svg");

                if(!svg.exists())
                    svg.getParentFile().mkdirs();

                Writer writer = new BufferedWriter(new FileWriter(svg));

                writer.write("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">\n");
                writer.write("<rect width=\"" + page.getWidth() + "\" height=\"" + page.getHeight() + "\" style=\"fill:gray\"/>\n");
                addParts(page.getSupply(), writer);
                writer.write("</svg>");
                writer.close();

                System.out.println("File printed on location: " + svg.getAbsolutePath());
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

            writer.write("<g>" + "<rect x=\"" + (x + padding) + "\" y=\"" + (y + padding) + "\" width=\"" + ((rect.getRectangle().getWidth() - padding)) + "\" height=\"" + ((rect.getRectangle().getHeight() - padding))
                    + "\" style=\"fill:" + "blue" + ";stroke-width:" + padding + ";stroke:black\" ></rect>" + "<text x=\"" + (x + padding) + "\" y=\"" + (y + 20) + "\" fill=\"snow\" >"
                    + rect.getName() + "</text>" + "</g>\n");
        }
    }
}
