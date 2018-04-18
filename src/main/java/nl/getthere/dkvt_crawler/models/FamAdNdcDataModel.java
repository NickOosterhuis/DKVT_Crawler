package nl.getthere.dkvt_crawler.models;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name="NdcData")
public class FamAdNdcDataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, name = "material_id")
    private long materialId;

    @Column(nullable = false, name = "note")
    private String note;

    private String ranking;

    @Column(name = "family_member_name")
    private String familyMemberName;

    @Column(name = "width_mm")
    private BigDecimal realWidth;

    @Column(name = "height_mm")
    private BigDecimal realHeight;
}
