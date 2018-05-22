package nl.getthere.svggenerator.components;

import java.awt.*;

public class Rect {

    private Rectangle rectangle;
    private int column;
    private int height;
    private int x;
    private int y;
    private int id;
    private String name;

    public Rect(int column, int height) {
        this.column = column;
        this.height = height;

        createRect(column, height);
    }

    public Rect(int column, int height, int id, String name) {
        this.column = column;
        this.height = height;
        this.id = id;
        this.name = name;

        createRect(column, height);
    }

    public Rectangle createRect(int column, int height) {
        rectangle = new Rectangle((column), (height));

        return rectangle;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
