package ir.university.toosi.tms.model.entity.personnel;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.BaseEntity;
import ir.university.toosi.tms.model.entity.rule.RulePackage;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Entity
@Table(name = "tb_person")
@NamedQueries({
        @NamedQuery(
                name = "Person.list",
                query = "select p from Person p where p.deleted = '0'"
        ), @NamedQuery(
        name = "Person.allModel",
        query = "select p.id,p.name,p.lastName,p.personnelNo from Person p where p.deleted = '0'"
), @NamedQuery(
        name = "Person.listID",
        query = "select p.id  from Person p where p.deleted = '0'"
), @NamedQuery(
        name = "Person.deleted",
        query = "select p from Person p where p.deleted = '1'"
),

        @NamedQuery(
                name = "Person.findById",
                query = "select p from Person p where p.id=:id and p.deleted = '0'"
        ),
        @NamedQuery(
                name = "Person.countOfAll",
                query = "select count(p.id) from Person p where p.deleted = '0'"
        ),
        @NamedQuery(
                name = "Person.findByName",
                query = "select p from Person p where p.name =:name and p.deleted = '0'"
        ),
        @NamedQuery(
                name = "Person.findByLastName",
                query = "select p from Person p where p.lastName  =:lastName and p.deleted = '0'"
        ),
        @NamedQuery(
                name = "Person.findByNationalCode",
                query = "select p from Person p where p.nationalCode =:nationalCode and p.deleted = '0'"
        ),
        @NamedQuery(
                name = "Person.findByPersonnelNo",
                query = "select p from Person p where p.personnelNo=:personnelNo and p.deleted = '0'"
        ),
        @NamedQuery(
                name = "Person.findByPersonOtherId",
                query = "select p from Person p where p.personOtherId=:personOtherId and p.deleted = '0'"
        ), @NamedQuery(
        name = "Person.findByPersonnelNumber",
        query = "select p.id, p.rulePackage.id ,p.personOtherId  from Person p where  p.deleted = '0'"
),
        @NamedQuery(
                name = "Person.findByOrganAndRulePackage",
                query = "select p from Person p where p.organRef.id =:organId and p.rulePackage.id =:rulepackageId and p.deleted='0'"
        ),
        @NamedQuery(
                name = "Person.findByRulePackage",
                query = "select p from Person p where p.rulePackage.id =:rulepackageId and p.deleted='0'"
        ),
        @NamedQuery(
                name = "Person.findByOrgan",
                query = "select p.id,p.name,p.lastName,p.personnelNo from Person p where p.organRef.id =:id and p.deleted='0'"
        ),
        @NamedQuery(
                name = "Person.exist",
                query = "select p from Person p where (p.personnelNo=:personnelNo or p.nationalCode=:nationalCode ) and p.deleted='0'"
        ), @NamedQuery(
        name = "Person.maximum",
        query = "select max(p.id) from Person p"
), @NamedQuery(
        name = "Person.withCard",
        query = "select p from Person p where p.deleted = '0' and p.hasCard=true"
), @NamedQuery(
        name = "Person.withOutCard",
        query = "select p from Person p where p.deleted = '0' and p.hasCard=false"
)
})
public class Person extends BaseEntity {

    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "name")
    private String name;
    @JsonProperty
    @Column(name = "lastName")
    private String lastName;
    @JsonProperty
    @Lob
    @Column(name = "picture")
    private byte[] picture;
    @JsonProperty
    @Lob
    @Column(name = "finger")
    private byte[] finger;
    @JsonProperty
    @Column(name = "personnelNo")
    private String personnelNo;
    @JsonProperty
    @Column(name = "personOtherId")
    private String personOtherId;
    @JsonProperty
    @Column(name = "nationalCode")
    private String nationalCode;
    @JsonProperty
    @Column(name = "pin")
    private String pin;
    @JsonProperty
    @ManyToOne(fetch = FetchType.EAGER)
    private RulePackage rulePackage;
    @JsonProperty
    @Column(name = "extraField1")
    private String extraField1;
    @JsonProperty
    @Column(name = "extraField2")
    private String extraField2;
    @JsonProperty
    @Column(name = "extraField3")
    private String extraField3;
    @JsonProperty
    @Column(name = "extraField4")
    private String extraField4;
    @JsonProperty
    @ManyToOne
    private BLookup personStatus;
    @JsonProperty
    @Column(name = "mobile")
    private String mobile;
    @JsonProperty
    @Column(name = "hasCard")
    private boolean hasCard;
    @JsonProperty
    @Column(name = "email")
    private String email;
    @JsonProperty
    @Column(name = "address")
    private String address;
    @JsonProperty
    @Column(name = "phone")
    private String phone;
    @JsonProperty
    @Column(name = "createDate")
    private String createDate;
    @JsonProperty
    @Column(name = "createTime")
    private String createTime;
    @JsonProperty
    @Column(name = "createBy")
    private String createBy;
    @JsonProperty
    @Column(name = "workStation")
    private String workStation;
    @JsonProperty
    @Column(name = "password")
    private String password;
    @JsonProperty
    @ManyToOne
    private Organ organRef;

    @Transient
    private boolean selected;
    public Person() {
    }

    public Person(String name, String lastName, byte[] picture, String personnelNo, String nationalCode, String pin, Organ organRef) {
        this.name = name;
        this.lastName = lastName;
        this.picture = picture;
        this.personnelNo = personnelNo;
        this.nationalCode = nationalCode;
        this.pin = pin;
        this.organRef = organRef;
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

    public Organ getOrganRef() {
        return organRef;
    }

    public void setOrganRef(Organ organRef) {
        this.organRef = organRef;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPersonnelNo() {
        return personnelNo;
    }

    public void setPersonnelNo(String personnelNo) {
        this.personnelNo = personnelNo;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getExtraField1() {
        return extraField1;
    }

    public void setExtraField1(String extraField1) {
        this.extraField1 = extraField1;
    }

    public String getExtraField2() {
        return extraField2;
    }

    public void setExtraField2(String extraField2) {
        this.extraField2 = extraField2;
    }

    public String getExtraField3() {
        return extraField3;
    }

    public void setExtraField3(String extraField3) {
        this.extraField3 = extraField3;
    }

    public String getExtraField4() {
        return extraField4;
    }

    public void setExtraField4(String extraField4) {
        this.extraField4 = extraField4;
    }

    public BLookup getPersonStatus() {
        return personStatus;
    }

    public void setPersonStatus(BLookup personStatus) {
        this.personStatus = personStatus;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getWorkStation() {
        return workStation;
    }

    public void setWorkStation(String workStation) {
        this.workStation = workStation;
    }

    public RulePackage getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(RulePackage rulePackage) {
        this.rulePackage = rulePackage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getFinger() {
        return finger;
    }

    public void setFinger(byte[] finger) {
        this.finger = finger;
    }

    public boolean isHasCard() {
        return hasCard;
    }

    public void setHasCard(boolean hasCard) {
        this.hasCard = hasCard;
    }

    @JsonIgnore
    public static List<Person> convert2Person(List<Object[]> lists) {
        List<Person> persons = new ArrayList<>();
        for (Object[] list : lists) {
            Person person = new Person();
            person.setId((Long) list[0]);
            person.setName((String) list[1]);
            person.setLastName((String) list[2]);
            person.setPersonnelNo((String) list[3]);
            persons.add(person);
        }
        return persons;

    }

    public String getPersonOtherId() {
        return personOtherId;
    }

    public void setPersonOtherId(String personOtherId) {
        this.personOtherId = personOtherId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}