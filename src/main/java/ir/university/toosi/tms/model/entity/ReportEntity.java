package ir.university.toosi.tms.model.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TrafficLog_View")
public class ReportEntity extends BaseEntity {
    @JsonProperty
    @Id
    @Column
    private long TRAFFICLOGID;
    @JsonProperty
    private String name;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String cardCode;
    @JsonProperty
    private String organName;
    @JsonProperty
    private String pdpName;
    @JsonProperty
    private String zoneName;
    @JsonProperty
    private String gateName;
    @JsonProperty
    private String trafficDate;
    @JsonProperty
    private String trafficTime;
    @JsonProperty
    private String success;
    @JsonProperty
    private String entryType;

    public ReportEntity() {
    }

    public long getTRAFFICLOGID() {
        return TRAFFICLOGID;
    }

    public void setTRAFFICLOGID(long TRAFFICLOGID) {
        this.TRAFFICLOGID = TRAFFICLOGID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getPdpName() {
        return pdpName;
    }

    public void setPdpName(String pdpName) {
        this.pdpName = pdpName;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getGateName() {
        return gateName;
    }

    public void setGateName(String gateName) {
        this.gateName = gateName;
    }

    public String getTrafficDate() {
        return trafficDate;
    }

    public void setTrafficDate(String trafficDate) {
        this.trafficDate = trafficDate;
    }

    public String getTrafficTime() {
        return trafficTime;
    }

    public void setTrafficTime(String trafficTime) {
        this.trafficTime = trafficTime;
    }
}


