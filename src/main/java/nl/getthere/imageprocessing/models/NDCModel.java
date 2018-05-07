package nl.getthere.imageprocessing.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * The coupling model between the crawled ads and the NDC ads
 *
 * @author Nick Oosterhuis
 */
@Entity(name = "NDC_data")
public class NDCModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "sys_created_at")
    private Timestamp sysCreatedAt;

    @Column(nullable = false, name = "sys_modified_at")
    private Timestamp sysModifiedAt;

    @Column(name = "sys_imported_at")
    private Timestamp sysImportedAt;

    @Column(nullable = false, name = "edition_code")
    private String editionCode;

    @Column(nullable = false, name = "issuename")
    private String issueName;

    @Column(name = "sectiongroup_name")
    private String sectionGroupName;

    @Column(name = "grouping_code")
    private String groupingCode;

    @Column(nullable = false, name = "edition_id")
    private long editionId;

    @Column(nullable = false, name = "issue_id")
    private long issueId;

    @Column
    private String publication;

    @Column(name = "publication_date")
    private Date publicationDate;

    @Column(name = "order_id")
    private long orderId;

    @Column(nullable = false, name = "orderitem_id")
    private long orderItemId;

    @Column(name = "material_id")
    private long materialId;

    @Column
    private String name;

    @Column
    private String note;

    @Column(name = "section_id")
    private long sectionId;

    @Column(name = "section_code")
    private String sectionCode;

    @Column(name = "adstatus_id")
    private long adStatusId;

    @Column(name = "adstatus")
    private String adStatus;

    @Column(name = "adcustomstatus_id")
    private long adCustomStatusId;

    @Column(name = "adcustomstatus")
    private String adCustomStatus;

    @Column(name = "placedonpage")
    private int placedOnPage;

    @Column(name = "color_id")
    private long colorId;

    @Column(name = "colorname")
    private String colorname;

    @Column(name = "orderitem_created")
    private Timestamp orderItemCreated;

    @Column(name = "orderitem_changed")
    private Timestamp orderItemChanged;

    @Column(name = "materialname")
    private String materialName;

    @Column(name = "adsize_id")
    private long adSizeId;

    @Column(name = "sizename")
    private String sizeName;

    @Column(name = "adtype_id")
    private long adTypeId;

    @Column(name = "adtypename")
    private String adTypeName;

    @Column(name = "width_col")
    private int widthCol;

    @Column(name = "width_mm")
    private BigDecimal widthMm;

    @Column(name = "height_mm")
    private BigDecimal heightMm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getSysCreatedAt() {
        return sysCreatedAt;
    }

    public void setSysCreatedAt(Timestamp sysCreatedAt) {
        this.sysCreatedAt = sysCreatedAt;
    }

    public Timestamp getSysModifiedAt() {
        return sysModifiedAt;
    }

    public void setSysModifiedAt(Timestamp sysModifiedAt) {
        this.sysModifiedAt = sysModifiedAt;
    }

    public Timestamp getSysImportedAt() {
        return sysImportedAt;
    }

    public void setSysImportedAt(Timestamp sysImportedAt) {
        this.sysImportedAt = sysImportedAt;
    }

    public String getEditionCode() {
        return editionCode;
    }

    public void setEditionCode(String editionCode) {
        this.editionCode = editionCode;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getSectionGroupName() {
        return sectionGroupName;
    }

    public void setSectionGroupName(String sectionGroupName) {
        this.sectionGroupName = sectionGroupName;
    }

    public String getGroupingCode() {
        return groupingCode;
    }

    public void setGroupingCode(String groupingCode) {
        this.groupingCode = groupingCode;
    }

    public long getEditionId() {
        return editionId;
    }

    public void setEditionId(long editionId) {
        this.editionId = editionId;
    }

    public long getIssueId() {
        return issueId;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getSectionId() {
        return sectionId;
    }

    public void setSectionId(long sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public long getAdStatusId() {
        return adStatusId;
    }

    public void setAdStatusId(long adStatusId) {
        this.adStatusId = adStatusId;
    }

    public String getAdStatus() {
        return adStatus;
    }

    public void setAdStatus(String adStatus) {
        this.adStatus = adStatus;
    }

    public long getAdCustomStatusId() {
        return adCustomStatusId;
    }

    public void setAdCustomStatusId(long adCustomStatusId) {
        this.adCustomStatusId = adCustomStatusId;
    }

    public String getAdCustomStatus() {
        return adCustomStatus;
    }

    public void setAdCustomStatus(String adCustomStatus) {
        this.adCustomStatus = adCustomStatus;
    }

    public int getPlacedOnPage() {
        return placedOnPage;
    }

    public void setPlacedOnPage(int placedOnPage) {
        this.placedOnPage = placedOnPage;
    }

    public long getColorId() {
        return colorId;
    }

    public void setColorId(long colorId) {
        this.colorId = colorId;
    }

    public String getColorname() {
        return colorname;
    }

    public void setColorname(String colorname) {
        this.colorname = colorname;
    }

    public Timestamp getOrderItemCreated() {
        return orderItemCreated;
    }

    public void setOrderItemCreated(Timestamp orderItemCreated) {
        this.orderItemCreated = orderItemCreated;
    }

    public Timestamp getOrderItemChanged() {
        return orderItemChanged;
    }

    public void setOrderItemChanged(Timestamp orderItemChanged) {
        this.orderItemChanged = orderItemChanged;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public long getAdsizeId() {
        return adSizeId;
    }

    public void setAdsizeId(long adsizeId) {
        this.adSizeId = adsizeId;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public long getAdTypeId() {
        return adTypeId;
    }

    public void setAdTypeId(long adTypeId) {
        this.adTypeId = adTypeId;
    }

    public String getAdTypeName() {
        return adTypeName;
    }

    public void setAdTypeName(String adTypeName) {
        this.adTypeName = adTypeName;
    }

    public int getWidthCol() {
        return widthCol;
    }

    public void setWidthCol(int widthCol) {
        this.widthCol = widthCol;
    }

    public BigDecimal getWidthMm() {
        return widthMm;
    }

    public void setWidthMm(BigDecimal widthMm) {
        this.widthMm = widthMm;
    }

    public BigDecimal getHeightMm() {
        return heightMm;
    }

    public void setHeightMm(BigDecimal heightMm) {
        this.heightMm = heightMm;
    }
}
