package nl.getthere.dkvt_crawler.models;

import javax.persistence.*;

@Entity(name = "FamAdProperties")
public class FamAdPropertyModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "x_coordinate", nullable = false)
    private int x;

    @Column(name = "y_coordinate", nullable = false)
    private int y;

    @Column(nullable = false)
    private int height;

    @Column(nullable = false)
    private int width;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="FamAdImages")
    private ImageModel image;

    public FamAdPropertyModel() {};

    public FamAdPropertyModel(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
