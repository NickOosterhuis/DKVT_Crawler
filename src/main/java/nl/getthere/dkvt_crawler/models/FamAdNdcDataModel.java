package nl.getthere.dkvt_crawler.models;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name="NdcData")
public class FamAdNdcDataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "material_id")
    private long materialId;

    @Column(name = "note")
    private String note;

    private String ranking;

    @Column(name = "family_member_name")
    private String familyMemberName;

    @Column(name = "width_mm")

    private BigDecimal realWidth;

    @Column(name = "height_mm")
    private BigDecimal realHeight;

    @Column(name = "section_code")
    private String sectionCode;

    @Column(name = "column_width")
    private int columnWidth;

    @Column(name = "is_matched", columnDefinition="BOOLEAN DEFAULT false", nullable = false)
    private boolean isMatched;

    @Column(name = "algorithm_category", columnDefinition = "INT DEFAULT '1'", nullable = false)
    private int algorithmCategory;

    public int getAlgorithmCategory() {
        return algorithmCategory;
    }

    public void setAlgorithmCategory(int algorithmCategory) {
        this.algorithmCategory = algorithmCategory;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getFamilyMemberName() {
        return familyMemberName;
    }

    public void setFamilyMemberName(String familyMemberName) {
        this.familyMemberName = familyMemberName;
    }

    public BigDecimal getRealWidth() {
        return realWidth;
    }

    public void setRealWidth(BigDecimal realWidth) {
        this.realWidth = realWidth;
    }

    public BigDecimal getRealHeight() {
        return realHeight;
    }

    public void setRealHeight(BigDecimal realHeight) {
        this.realHeight = realHeight;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }
}
