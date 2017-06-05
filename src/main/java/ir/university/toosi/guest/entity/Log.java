package ir.university.toosi.guest.entity;

import ir.university.toosi.tms.model.entity.BaseEntity;
import ir.university.toosi.tms.model.entity.personnel.Card;

import javax.persistence.*;

/**
 * Created by M_Danapour on 04/05/2015.
 */
@Entity
@Table(name = "tb_log")
@NamedQueries({
        @NamedQuery(
                name = "Log.findById",
                query = "select u from Log u where u.id=:id"
        )     , @NamedQuery(
                name = "Log.today",
                query = "select u from Log u where u.date=:date"
        )
  , @NamedQuery(
                name = "Log.duration",
                query = "select u from Log u where u.date between :from and :to and u.time between :fromH and :toH"
        )

})
public class Log extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;
    @Column(name = "date")
    private String date;
    @Column(name = "time")
    private String time;
    @OneToOne
    Guest guest;
    @OneToOne
    Card card;
    private String type;

    public Log() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
