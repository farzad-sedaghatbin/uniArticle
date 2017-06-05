package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.guest.entity.Guest;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.personnel.Organ;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.PDP;
import ir.university.toosi.tms.model.entity.zone.Virdi;
import ir.university.toosi.tms.model.entity.zone.Zone;

import javax.persistence.*;


@Entity
@Table(name = "tb_TrafficLog")
@NamedQueries({
        @NamedQuery(
                name = "TrafficLog.list",
                query = "select t from TrafficLog t where t.deleted='0' and t.traffic_date =:trafficDate "
        ),
        @NamedQuery(
                name = "TrafficLog.searchForChart",
                query = "select t.gateway.id,count(t.person.id) from TrafficLog t where t.deleted='0' and t.valid=:valid and t.traffic_time between :fromTime and :toTime and t.traffic_date between :fromDate and :toDate group by t.gateway.id"
        ),
        @NamedQuery(
                name = "TrafficLog.findById",
                query = "select t from TrafficLog t where t.id=:id"
        ),
        @NamedQuery(
                name = "TrafficLog.findByPersonIdInDuration",
                query = "select t from TrafficLog t where t.person.id=:personId and t.valid = true and t.exit =:exit and t.traffic_date =:trafficDate"
        ),
        @NamedQuery(
                name = "TrafficLog.findByPersonLocationInDuration",
                query = "select t from TrafficLog t where t.person.id=:personId  and t.traffic_date =:trafficDate"
        ),
        @NamedQuery(
                name = "TrafficLog.findTrafficInDuration",
                query = "select t from TrafficLog t where t.traffic_date between :startDate and :endDate"
        ),
        @NamedQuery(
                name = "TrafficLog.findInDuration",
                query = "select t from TrafficLog t where t.traffic_date =:date"
        ),
        @NamedQuery(
                name = "TrafficLog.findByPersonAndGate",
                query = "select t from TrafficLog t where t.person.id=:personId and t.gateway.id =:gatewayTd and t.traffic_date =:trafficDate"
        ),
        @NamedQuery(
                name = "TrafficLog.findByPersonAndGateExOrEn",
                query = "select count(t) from TrafficLog t where t.person.id=:personId and t.gateway.id =:gatewayTd and t.traffic_date =:trafficDate and t.exit =:exit"
        ),
        @NamedQuery(
                name = "TrafficLog.findByGate",
                query = "select t from TrafficLog t where t.gateway.id =:gatewayTd and t.traffic_date =:trafficDate"
        ),
        @NamedQuery(
                name = "TrafficLog.findByPDP",
                query = "select t from TrafficLog t where t.pdp.id =:PDPId and t.traffic_date =:trafficDate order by t.id desc "
        ),  @NamedQuery(
                name = "TrafficLog.findByVirdi",
                query = "select t from TrafficLog t where t.virdi.id =:virdiId and t.traffic_date =:trafficDate order by t.id desc "
        ),
        @NamedQuery(
                name = "TrafficLog.findByPerson",
                query = "select t from TrafficLog t where t.person.id=:personId  and t.traffic_date =:trafficDate order by t.traffic_time desc"
        ), @NamedQuery(
                name = "TrafficLog.findByPersonInDuration",
                query = "select t from TrafficLog t where t.person.id=:personId  and (t.traffic_date between :fromDate and :toDate ) order by t.traffic_time desc"
        ),@NamedQuery(
                name = "TrafficLog.findByGateInDuration",
                query = "select t from TrafficLog t where t.gateway.id=:gateId  and (t.traffic_date between :fromDate and :toDate ) order by t.traffic_time desc"    )
        ,@NamedQuery(
                name = "TrafficLog.findByVirdiInDuration",
                query = "select t from TrafficLog t where t.virdi.id=:gateId  and (t.traffic_date between :fromDate and :toDate ) order by t.traffic_time desc"
        ),@NamedQuery(
                name = "TrafficLog.findByOrganInDuration",
                query = "select t from TrafficLog t where t.organ.id=:organId  and (t.traffic_date between :fromDate and :toDate ) order by t.traffic_time desc"
        ),
        @NamedQuery(
                name = "TrafficLog.findLastByPerson",
                query = "select t from TrafficLog t where t.person.id=:personId  and t.traffic_date =:trafficDate and t.last = true and t.deleted ='0'"
        ), @NamedQuery(
        name = "TrafficLog.maximum",
        query = "select max(t.id) from TrafficLog t"
)
})
public class TrafficLog extends BaseEntity {

    @Column(name = "id")
    @GeneratedValue
    @Id
    @JsonProperty
    private long id;
    @OneToOne
    @JsonProperty
    private Person person;
    @OneToOne
    @JsonProperty
    private Guest guest;
    @OneToOne
    @JsonProperty
    private PDP pdp;
    @OneToOne
    @JsonProperty
    private Virdi virdi;
    @OneToOne
    @JsonProperty
    private Organ organ;
    @OneToOne
    @JsonProperty
    private Zone zone;
    @OneToOne
    @JsonProperty
    private Card card;
    @OneToOne
    @JsonProperty
    private Gateway gateway;
    @JsonProperty
    @Column(name = "traffic_time")
    private String traffic_time;
    @JsonProperty
    @Column(name = "traffic_date")
    private String traffic_date;
    @Column(name = "pictures")
    @JsonProperty
    private String pictures;
    @Column(name = "video")
    @JsonProperty
    private String video;
    @Column(name = "valid")
    @JsonProperty
    private boolean valid;
    @Column(name = "exit")
    @JsonProperty
    private boolean exit;
    @JsonProperty
    @Column(name = "isLast")
    private boolean last;
    @JsonProperty
    @Column(name = "finger")
    private boolean finger;
    @JsonProperty
    @Column(name = "offlineTraffic")
    private boolean offline;
    @Lob
    private byte[] virdiPicture;


    public TrafficLog() {
    }


    public TrafficLog(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Person getPerson() {
        if (person == null)
            person = new Person();
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Gateway getGateway() {
        if (gateway == null)
            gateway = new Gateway();
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public String getTraffic_time() {
        return traffic_time;
    }

    public void setTraffic_time(String time) {
        this.traffic_time = time;
    }

    public String getTraffic_date() {
        return traffic_date;
    }

    public void setTraffic_date(String date) {
        this.traffic_date = date;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public PDP getPdp() {
        return pdp;
    }

    public void setPdp(PDP pdp) {
        this.pdp = pdp;
    }

    public Organ getOrgan() {
        return organ;
    }

    public void setOrgan(Organ organ) {
        this.organ = organ;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isFinger() {
        return finger;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public void setFinger(boolean finger) {
        this.finger = finger;

    }
    @JsonIgnore
    public String getTime() {
        return traffic_time;
    }

    @JsonIgnore
    public void setTime(String time) {
        this.traffic_time = time;
    }
    @JsonIgnore
    public String getDate() {
        return traffic_date.replace("/", "");
    }
    @JsonIgnore
    public void setDate(String date) {
        this.traffic_date = date;
    }

    public Virdi getVirdi() {
        return virdi;
    }

    public void setVirdi(Virdi virdi) {
        this.virdi = virdi;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public byte[] getVirdiPicture() {
        return virdiPicture;
    }

    public void setVirdiPicture(byte[] virdiPicture) {
        this.virdiPicture = virdiPicture;
    }
}
