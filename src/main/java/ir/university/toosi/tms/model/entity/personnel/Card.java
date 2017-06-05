package ir.university.toosi.tms.model.entity.personnel;


import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.guest.entity.Guest;
import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.BaseEntity;

import javax.persistence.*;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Entity
@Table(name = "tb_card")
@NamedQueries({
        @NamedQuery(
                name = "Card.list",
                query = "select c from Card c where c.deleted <> '1' and c.visible=true"
        ),
        @NamedQuery(
                name = "Card.active.list",
                query = "select c from Card c where c.deleted <> '1' and c.visible=true "
        ),        @NamedQuery(
                name = "Card.guest.list",
                query = "select c from Card c where c.deleted <> '1' and c.visible=true and c.forGuest=true and c.guest is null"
        ),
        @NamedQuery(
                name = "Card.findById",
                query = "select c from Card c where c.id=:id"
        ),
        @NamedQuery(
                name = "Card.findByPersonId",
                query = "select c from Card c where c.person.id=:id and c.deleted='0'"
        ),       @NamedQuery(
                name = "Card.findByGuestId",
                query = "select c from Card c where c.guest.id=:id and c.deleted='0'"
        ),
        @NamedQuery(
                name = "Card.findByCode",
                query = "select c from Card c where c.code=:code and c.deleted <> '1'"
        ),
        @NamedQuery(
                name = "Card.invisible",
                query = "select c from Card c where c.visible=false and c.deleted <> '1'"
        ),
        @NamedQuery(
                name = "Card.assign",
                query = "select c from Card c where c.deleted <> '1' and c.startDate between :startDate and :endDate "
        ),
        @NamedQuery(
                name = "Card.lost",
                query = "select c from Card c where c.deleted <> '1' and c.cardStatus.code='CARD_STATUS_LOST' "
        ),
        @NamedQuery(
                name = "Card.closed",
                query = "select c from Card c where c.deleted <> '1' and c.cardStatus.code='CARD_STATUS_CLOSED' "
        ),
        @NamedQuery(
                name = "Card.open",
                query = "select c from Card c where c.deleted <> '1' and c.cardStatus.code='CARD_IS_OPEN' "
        ),
        @NamedQuery(
                name = "Card.stolen",
                query = "select c from Card c where c.deleted <> '1' and c.cardStatus.code='CARD_STATUS_STOLEN' "
        )
})
public class Card extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    @JsonProperty
    private long id;
    @JsonProperty
    @Column(name = "name")
    private String name;
    @JsonProperty
    @Column(name = "code")
    private String code;
    @JsonProperty
    @ManyToOne
    private BLookup cardType;
    @JsonProperty
    @ManyToOne
    private BLookup cardStatus;
    @JsonProperty
    @Column(name = "visible")
    private boolean visible;

    @JsonProperty
    @Column(name = "forGuest")
    private boolean forGuest=false;
    @OneToOne
    private Guest guest;
    @JsonProperty
    @ManyToOne
    private Person person;
    @JsonProperty
    @Column(name = "startDate")
    private String startDate;
    @JsonProperty
    @Column(name = "expirationDate")
    private String expirationDate;
    @Transient
    private boolean selected;
    @Transient
    public static final String CARD_STATUS_STOLEN = "CARD_STATUS_STOLEN";
    @Transient//
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

    public Card() {
    }

    public Card(String name, String code, BLookup cardType, BLookup cardStatus, boolean visible, Person person) {
        this.name = name;
        this.code = code;
        this.cardType = cardType;
        this.cardStatus = cardStatus;
        this.visible = visible;
        this.person = person;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BLookup getCardType() {
        if (cardType == null)
            cardType = new BLookup();
        return cardType;
    }

    public void setCardType(BLookup cardType) {
        this.cardType = cardType;
    }

    public BLookup getCardStatus() {
        if (cardStatus == null)
            cardStatus = new BLookup();
        return cardStatus;
    }

    public void setCardStatus(BLookup cardStatus) {
        this.cardStatus = cardStatus;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public boolean isForGuest() {
        return forGuest;
    }

    public void setForGuest(boolean forGuest) {
        this.forGuest = forGuest;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}