package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;


@Entity
@Table(name = "TB_BLookup")
@NamedQueries({
        @NamedQuery(
                name = "BLookup.list",
                query = "select b from BLookup b WHERE b.deleted='0'"
        ),
        @NamedQuery(
                name = "BLookup.findById",
                query = "select b from BLookup b where b.id=:id and b.deleted='0'"
        ),
        @NamedQuery(
                name = "BLookup.findByTitle",
                query = "select b from BLookup b where b.title=:title and b.deleted='0'"
        ),
        @NamedQuery(
                name = "BLookup.findByLookup",
                query = "select b from BLookup b where b.lookup.title=:lookupTitle and b.deleted='0'"
        ),
        @NamedQuery(
                name = "BLookup.findByLookupId",
                query = "select b from BLookup b where b.lookup.id=:lookupId and b.deleted='0'"
        ), @NamedQuery(
        name = "BLookup.maximum",
        query = "select max(b.id) from BLookup b"
)
})
public class BLookup extends BaseEntity {


   @Transient
    public static final String CARD_STATUS_STOLEN = "CARD_STATUS_STOLEN";   //
    @Transient
    public static final String CARD_STATUS_LOST = "CARD_STATUS_LOST";         //
    @Transient
    public static final String CARD_STATUS_CLOSED = "CARD_STATUS_CLOSED";  //
    @Transient
    public static final String CARD_IS_INVALID = "CARD_IS_INVALID";       //
    @Transient
    public static final String CARD_IS_OPEN = "CARD_IS_OPEN";       //
    @Transient
    public static final String CARD_SPECIAL = "CARD_SPECIAL";       //?
    @Transient
    public static final String CARD_NORMAL = "CARD_NORMAL";      //?

    @Column(name = "id")
    @Id
    @JsonProperty
    private long id;
    @Column(name = "title")
    @JsonProperty
    private String title;
    @Column(name = "code")
    @JsonProperty
    private String code;
    @ManyToOne
    private Lookup lookup;
    @Transient
    @JsonProperty
    private String titleText;


    public BLookup() {
    }

    public BLookup(long id, String title, Lookup lookup) {
        this.id = id;
        this.code = title;
        this.lookup = lookup;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Lookup getLookup() {
        return lookup;
    }

    public void setLookup(Lookup lookup) {
        this.lookup = lookup;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
