package nl.getthere.dkvt_crawler.models;

import javax.persistence.*;

/**
 * The image model
 *
 * @author Nick Oosterhuis
 */
@Entity(name = "FamAdImages")
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
