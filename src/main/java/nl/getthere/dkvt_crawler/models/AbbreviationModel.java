package nl.getthere.dkvt_crawler.models;

import javax.persistence.*;

/**
 * The abbreviation model
 *
 * @author Nick Oosterhuis
 */
@Entity(name = "Abbreviations")
public class AbbreviationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "newspaper_abbreviation", unique = true, nullable = false)
    private String newspaperName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNewspaperName() {
        return newspaperName;
    }

    public void setNewspaperName(String newspaperName) {
        this.newspaperName = newspaperName;
    }
}
