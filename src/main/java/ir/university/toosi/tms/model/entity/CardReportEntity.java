package ir.university.toosi.tms.model.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Card_View")
public class CardReportEntity extends BaseEntity {
    @JsonProperty
    @Id
    @Column
    private long id;
    @JsonProperty
    private String personName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String code;
    @JsonProperty
    private String name;
    @JsonProperty
    private String startDate;
    @JsonProperty
    private String expirationDate;
    @JsonProperty
    private String cardStatus_id;
    @JsonProperty
    private String cardType_id;


    public CardReportEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCardStatus_id() {
        return cardStatus_id;
    }

    public void setCardStatus_id(String cardStatus_id) {
        this.cardStatus_id = cardStatus_id;
    }

    public String getCardType_id() {
        return cardType_id;
    }

    public void setCardType_id(String cardType_id) {
        this.cardType_id = cardType_id;
    }
}


