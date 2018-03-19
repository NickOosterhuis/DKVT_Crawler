package nl.getthere.dkvt_crawler.models;

import javax.persistence.*;

/**
 * @author Nick Oosterhuis
 */
@Entity(name = "NewspaperId")
public class FullNewspaperIdModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_newspaper_id", unique = true, nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
