package ir.university.toosi.parking.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BaseEntity;


public class ParkingLogDataModel extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private String number;
    @JsonProperty
    private String traffic_time;
    @JsonProperty
    private String traffic_date;
    @JsonProperty
    private String pictures;



    public ParkingLogDataModel() {
    }


    public ParkingLogDataModel(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getTime() {
        return traffic_time;
    }

    public void setTime(String time) {
        this.traffic_time = time;
    }

    public String getDate() {
        return traffic_date.replace("/","");
    }

    public void setDate(String date) {
        this.traffic_date = date;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
}
