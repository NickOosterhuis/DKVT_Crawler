package nl.getthere.dkvt_crawler.models;

import javax.persistence.*;

@Entity(name = "FamAdvertId")
public class NewspaperFamAdvertIdModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "fam_advert_code", nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false, name = "abbriviation")
    private String newspaperAbbriviation;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNewspaperAbbriviation() {
        return newspaperAbbriviation;
    }

    public void setNewspaperAbbriviation(String newspaperAbbriviation) {
        this.newspaperAbbriviation = newspaperAbbriviation;
    }
}
