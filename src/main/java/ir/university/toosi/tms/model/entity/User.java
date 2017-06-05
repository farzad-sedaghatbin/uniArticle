package ir.university.toosi.tms.model.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.personnel.Person;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
@Entity
@Table(name = "tb_user")
@NamedQueries({
        @NamedQuery(
                name = "User.findByUsernameAndPassword",
                query = "select u from User u where u.username=:username and u.password=:password and u.deleted='0'"
        ),
        @NamedQuery(
                name = "User.findById",
                query = "select u from User u where u.id=:id"
        ),
        @NamedQuery(
                name = "User.findByPersonId",
                query = "select u from User u where u.person.id=:id and u.deleted = '0'"
        ),
        @NamedQuery(
                name = "User.findByWorkGroupId",
                query = "select u from User u join u.workGroups w where w.id in:workGroupId and u.deleted = '0'"
        ),
        @NamedQuery(
                name = "User.findByPcId",
                query = "select u from User u join u.pcs p where p.id in:pcId and u.deleted = '0'"
        ),
        @NamedQuery(
                name = "User.list",
                query = "select u from User u where u.deleted = '0'"
        ),
        @NamedQuery(
                name = "User.exist",
                query = "select u from User u where u.username=:username and u.deleted='0'"
        ),
        @NamedQuery(
                name = "User.findByUsername",
                query = "select u from User u where u.username=:username and u.deleted = '0'"
        ), @NamedQuery(
        name = "User.maximum",
        query = "select max(u.id) from User u"
)
})
public class User extends BaseEntity {

    @Id
    @JsonProperty
    @GeneratedValue
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "firstname")
    private String firstname;
    @JsonProperty
    @Column(name = "lastname")
    private String lastname;
    @JsonProperty
    @Column(name = "username")
    private String username;
    @JsonProperty
    @Column(name = "password")
    private String password;
    @JsonProperty
    @Column(name = "enable")
    private String enable;
    @JsonProperty
    @Column(name = "failedLoginCount")
    private String failedLoginCount;
    @JsonProperty
    @Column(name = "lastLoginDate")
    private String lastLoginDate;
    @JsonProperty
    @Column(name = "lastLoginIP")
    private String lastLoginIP;
    @JsonProperty
    @Column(name = "onlineuser")
    private boolean online;
    @JsonProperty
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "tb_user_workGroup", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "workGroup_id", referencedColumnName = "id", nullable = false)})
    private Set<WorkGroup> workGroups;
    @JsonProperty
    @Column(name = "skin")
    private String skin;
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
    @OneToOne
    private Person person;
    @Lob
    private byte[] userSign;

    @JsonProperty
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "tb_user_pc", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "pc_id", referencedColumnName = "id", nullable = false)})
    private Set<PC> pcs;

    public User() {
    }

    public User(Set<PC> pcs, String username, String password, String enable, boolean online, Set<WorkGroup> workGroups, Person person) {
        this.pcs = pcs;
        this.username = username;
        this.password = password;
        this.enable = enable;
        this.online = online;
        this.workGroups = workGroups;
        this.person = person;
    }

    public User(long id) {
        this.id = id;
    }

    public User(Set<PC> pcs) {
        this.pcs = pcs;
    }

    public User(String username, String password, String enable) {
        this.username = username;
        this.password = password;
        this.enable = enable;
    }

    public String getFailedLoginCount() {
        return failedLoginCount;
    }

    public void setFailedLoginCount(String failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<WorkGroup> getWorkGroups() {
        return workGroups;
    }

    public void setWorkGroups(Set<WorkGroup> workGroups) {
        this.workGroups = workGroups;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<PC> getPcs() {
        if (pcs == null)
            return new HashSet<>();
        return pcs;
    }

    public void setPcs(Set<PC> pcs) {
        this.pcs = pcs;
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

    public byte[] getUserSign() {
        return userSign;
    }

    public void setUserSign(byte[] userSign) {
        this.userSign = userSign;
    }
}
