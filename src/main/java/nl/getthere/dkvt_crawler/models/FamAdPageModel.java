package nl.getthere.dkvt_crawler.models;

import javax.persistence.*;

/**
 * @author Nick Oosterhuis
 */
@Entity(name = "FamAds")
public class FamAdPageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "fam_advert_code", nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false, name = "abbreviation")
    private String newspaperAbbreviation;

    @Column(name = "new_abbreviation")
    private String newNewspaperAbbreviation;

    @Column(nullable = false, name = "page_number")
    private String pageNumber;

    @Column(nullable = false, name = "publication_number")
    private String publicationNumber;

    @Column(nullable = false, name = "advert_number")
    private String advertNumber;

    @Column(nullable = false, name = "column_id")
    private String columnId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="FamAdProperties")
    private FamAdPropertyModel famAdPropertyModel;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="NdcData")
    private FamAdNdcDataModel famAdNdcDataModel;

    public FamAdNdcDataModel getFamAdNdcDataModel() {
        return famAdNdcDataModel;
    }

    public void setFamAdNdcDataModel(FamAdNdcDataModel famAdNdcDataModel) {
        this.famAdNdcDataModel = famAdNdcDataModel;
    }

    public FamAdPropertyModel getFamAdPropertyModel() {
        return famAdPropertyModel;
    }

    public void setFamAdPropertyModel(FamAdPropertyModel famAdPropertyModel) {
        this.famAdPropertyModel = famAdPropertyModel;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

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

    public String getNewspaperAbbreviation() {
        return newspaperAbbreviation;
    }

    public void setNewspaperAbbreviation(String newspaperAbbreviation) {
        this.newspaperAbbreviation = newspaperAbbreviation;
    }

    public String getNewNewspaperAbbreviation() {
        return newNewspaperAbbreviation;
    }

    public void setNewNewspaperAbbreviation(String newNewspaperAbbreviation) {
        this.newNewspaperAbbreviation = newNewspaperAbbreviation;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPublicationNumber(String publicationNumber) {
        this.publicationNumber = publicationNumber;
    }

    public String getPublicationNumber() {
        return publicationNumber;
    }

    public void setAdvertNumber(String advertNumber) {
        this.advertNumber = advertNumber;
    }

    public String getAdvertNumber() {
        return advertNumber;
    }
}
