package ir.university.toosi.guest.entity;

import ir.university.toosi.tms.model.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by M_Danapour on 04/05/2015.
 */
@Entity
@Table(name = "tb_guest")
@NamedQueries({
        @NamedQuery(
                name = "Guest.findById",
                query = "select u from Guest u where u.id=:id"
        )     , @NamedQuery(
                name = "Guest.today",
                query = "select u from Guest u where u.date=:date"
        )
        , @NamedQuery(
        name = "Guest.duration",
        query = "select u from Guest u where u.date between :from and :to "
)

})
public class Guest extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "email")
    private String email;
    @Column(name = "nationalCode")
    private String nationalCode;
    @Column(name = "fatherName")
    private String fatherName;
    @Column(name = "phonNumber")
    private String phonNumber;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "address")
    private String address;
    @Column(name = "date")
    private String date;
    @Column(name = "time")
    private String time;
    @Column(name = "Vname")
    private String vName;
    @Column(name = "vFamily")
    private String vFamily;
    @Column(name = "vOrgan")
    private String vOrgan;
    @Column(name = "authenticateType")
    private String authenticateType;
    @Column(name = "picture")
    @Lob
    private byte[] picture;
    @Column(name = "hasCard")
    private boolean hasCard;
    @Column(name = "guestSize")
    private int guestSize;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Guest> guestSet;

    @Column(name = "pelak")
    private String pelak;
    @Column(name = "exitTime")
    private String exitTime;

    public Guest() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getPhonNumber() {
        return phonNumber;
    }

    public void setPhonNumber(String phonNumber) {
        this.phonNumber = phonNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public boolean isHasCard() {
        return hasCard;
    }

    public void setHasCard(boolean hasCard) {
        this.hasCard = hasCard;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getvFamily() {
        return vFamily;
    }

    public void setvFamily(String vFamily) {
        this.vFamily = vFamily;
    }

    public Set<Guest> getGuestSet() {
        return guestSet;
    }

    public void setGuestSet(Set<Guest> guestSet) {
        this.guestSet = guestSet;
    }

    public int getGuestSize() {
        return guestSize;
    }

    public void setGuestSize(int guestSize) {
        this.guestSize = guestSize;
    }

    public String getAuthenticateType() {
        return authenticateType;
    }

    public void setAuthenticateType(String authenticateType) {
        this.authenticateType = authenticateType;
    }

    public String getPelak() {
        return pelak;
    }

    public void setPelak(String pelak) {
        this.pelak = pelak;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getvOrgan() {
        return vOrgan;
    }

    public void setvOrgan(String vOrgan) {
        this.vOrgan = vOrgan;
    }
}
