package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "TB_Lookup")
@NamedQueries({
        @NamedQuery(
                name = "Lookup.list",
                query = "select l from Lookup l where l.deleted='0' "
        ),
        @NamedQuery(
                name = "Lookup.findById",
                query = "select l from Lookup l where l.id=:id and l.deleted='0'"
        ),
        @NamedQuery(
                name = "Lookup.findByTitle",
                query = "select l from Lookup l where l.title=:title and l.deleted='0'"
        ),
        @NamedQuery(
                name = "Lookup.findDefinable",
                query = "select l from Lookup l where l.definable=:definable and l.deleted='0'"
        )
})

public class Lookup extends BaseEntity {
    @Transient
    public static final Long ORGAN_TYPE_ID = 1l;
    @Transient
    public static final Long CARD_TYPE_ID = 2l;
    @Transient
    public static final Long CARD_STATUS_ID = 3l;
    @Transient
    public static final Long EMPLOYEE_TYPE_ID = 4l;
    @Transient
    public static final Long ASSIST_TYPE_ID = 5l;
    @Transient
    public static final Long POST_TYPE_ID = 6l;
    @Transient
    public static final Long PC_LOCATION_ID = 8l;



    @Column(name = "id")
    @Id
    @JsonProperty
    private long id;

    @Column(name = "title")
    @JsonProperty
    private String title;
    @Column(name = "definable")
    @JsonProperty
    private boolean definable;
    @Transient
    @JsonProperty
    private String titleText;

    public Lookup() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public boolean isDefinable() {
        return definable;
    }

    public void setDefinable(boolean definable) {
        this.definable = definable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }
}
