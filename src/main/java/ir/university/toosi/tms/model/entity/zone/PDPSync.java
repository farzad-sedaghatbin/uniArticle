package ir.university.toosi.tms.model.entity.zone;


import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.personnel.Person;

import java.io.Serializable;
import java.util.Set;

public class PDPSync implements Serializable {
    @JsonProperty
    private Set<PDP> pdpList;
    @JsonProperty
    private boolean finger;
    @JsonProperty
    private Person person;
    @JsonProperty
    private boolean db;
    @JsonProperty
    private boolean schedule;
    @JsonProperty
    private boolean picture;

    public Set<PDP> getPdpList() {
        return pdpList;
    }

    public void setPdpList(Set<PDP> pdpList) {
        this.pdpList = pdpList;
    }

    public boolean isFinger() {
        return finger;
    }

    public void setFinger(boolean finger) {
        this.finger = finger;
    }

    public boolean isDb() {
        return db;
    }

    public void setDb(boolean db) {
        this.db = db;
    }

    public boolean isSchedule() {
        return schedule;
    }

    public void setSchedule(boolean schedule) {
        this.schedule = schedule;
    }

    public boolean isPicture() {
        return picture;
    }

    public void setPicture(boolean picture) {
        this.picture = picture;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
