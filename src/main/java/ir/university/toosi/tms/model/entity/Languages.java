package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "TB_Languages")
@NamedQueries({
        @NamedQuery(
                name = "Languages.list",
                query = "select l from Languages l where l.deleted = '0'"
        ),
        @NamedQuery(
                name = "Languages.findById",
                query = "select l from Languages l where l.id=:id and l.deleted='0'"
        ),
        @NamedQuery(
                name = "Languages.findByName",
                query = "select l from Languages l where l.name=:name and l.deleted='0'"
        )
})

public class Languages extends BaseEntity {

    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "name")
    private String name;
    @JsonProperty
    @Column(name = "rtl")
    private boolean rtl;
    @JsonProperty
    @Column(name = "defaulted")
    private boolean defaulted;


    public Languages() {
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

    public boolean isRtl() {
        return rtl;
    }

    public void setRtl(boolean rtl) {
        this.rtl = rtl;
    }

    public boolean isDefaulted() {
        return defaulted;
    }

    public void setDefaulted(boolean defaulted) {
        this.defaulted = defaulted;
    }
}
