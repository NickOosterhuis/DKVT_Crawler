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
    private String pageNumber;
    private String ranking;
    private String famMemberName;

    public Rect(int column, int height) {
        this.column = column;
        this.height = height;

        createRect(column, height);
    }

    public Rect(int column, int height, int x, int y) {
        this.column = column;
        this.height = height;
        this.x = x;
        this.y = y;

        createRect(column, height, x ,y);
    }

    public Rect(int column, int height, int id, String name) {
        this.column = column;
        this.height = height;
        this.id = id;
        this.name = name;

        createRect(column, height);
    }

    private Rectangle createRect(int column, int height) {
        rectangle = new Rectangle((column), (height));

        return rectangle;
    }

    private Rectangle createRect(int column, int height, int x , int y) {
        rectangle = new Rectangle(x, y, column, height);

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

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getRanking() {
        return ranking;
    }

    public String getFamMemberName() {
        return famMemberName;
    }

    public void setFamMemberName(String famMemberName) {
        this.famMemberName = famMemberName;
    }
}
