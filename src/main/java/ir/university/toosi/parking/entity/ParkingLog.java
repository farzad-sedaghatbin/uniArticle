package ir.university.toosi.parking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BaseEntity;

import javax.persistence.*;


@Entity
@Table(name = "tb_ParkingLog")
@NamedQueries({
        @NamedQuery(
                name = "ParkingLog.list",
                query = "select t from ParkingLog t where t.deleted='0' and t.traffic_date =:trafficDate "
        ),
        @NamedQuery(
                name = "ParkingLog.findById",
                query = "select t from ParkingLog t where t.id=:id"
        ),
        @NamedQuery(
                name = "ParkingLog.findParkingInDuration",
                query = "select t from ParkingLog t where t.traffic_date between :startDate and :endDate  order by t.traffic_time desc"
        ),
        @NamedQuery(
                name = "ParkingLog.findParkingInDurationTime",
                query = "select t from ParkingLog t where t.traffic_time between :startTime and :endTime and t.number=:pelak"
        ),
        @NamedQuery(
                name = "ParkingLog.findInDuration",
                query = "select t from ParkingLog t where t.traffic_date =:date"
        ), @NamedQuery(
        name = "ParkingLog.findByNumberInDuration",
        query = "select t from ParkingLog t where t.number=:number  and (t.traffic_date between :fromDate and :toDate ) order by t.traffic_time desc"
), @NamedQuery(
        name = "ParkingLog.maximum",
        query = "select max(t.id) from ParkingLog t"
)
})
public class ParkingLog extends BaseEntity {

    @Column(name = "id")
    @GeneratedValue
    @Id
    @JsonProperty
    private long id;
    @JsonProperty
    @Column(name = "traffic_time")
    private String traffic_time;
    @JsonProperty
    @Column(name = "traffic_date")
    private String traffic_date;
    @Column(name = "pictures")
    @JsonProperty
    private String pictures;
    @Column(name = "number")
    private String number;
    @Transient
    private boolean hasCar;

    public ParkingLog() {
    }


    public ParkingLog(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTraffic_time() {
        return traffic_time;
    }

    public void setTraffic_time(String traffic_time) {
        this.traffic_time = traffic_time;
    }

    public String getTraffic_date() {
        return traffic_date;
    }

    public void setTraffic_date(String traffic_date) {
        this.traffic_date = traffic_date;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getNumber() {
        if (number != null)
            return number.replace("N", "");
        return "";
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isHasCar() {
        return hasCar;
    }

    public void setHasCar(boolean hasCar) {
        this.hasCar = hasCar;
    }
}