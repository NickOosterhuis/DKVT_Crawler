package nl.getthere.svggenerator.components;

import java.util.ArrayList;

public class Page {
    private final int width = 548;
    private final int height = 800;
    private ArrayList<Rect> supply;

    public ArrayList<Rect> getSupply() {
        return supply;
    }

    public void setSupply(ArrayList<Rect> supply) {
        this.supply = supply;
    }

    public void createPage() {
        new Rect(width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
