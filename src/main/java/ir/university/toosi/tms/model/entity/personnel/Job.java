package ir.university.toosi.tms.model.entity.personnel;


import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.BaseEntity;

import javax.persistence.*;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Entity
@Table(name = "tb_job")
@NamedQueries({
        @NamedQuery(
                name = "Job.list",
                query = "select j from Job j where j.deleted='0'"
        ),
        @NamedQuery(
                name = "Job.findById",
                query = "select j from Job j where j.id=:id and j.deleted='0'"
        ),
        @NamedQuery(
                name = "Job.findByPersonId",
                query = "select j from Job j where j.person.id=:id and j.deleted='0'"
        )
})
public class Job extends BaseEntity {

    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "employNo")
    private String employNo;
    @JsonProperty
    @ManyToOne
    private BLookup employType;
    @JsonProperty
    @Column(name = "folderNo")
    private String folderNo;
    @JsonProperty
    @Column(name = "internalTel")
    private String internalTel;
    @ManyToOne
    @JsonProperty
    private BLookup assistType;
    @ManyToOne
    @JsonProperty
    private BLookup postType;
    @JsonProperty
    @Column(name = "description")
    private String description;

    @JsonProperty
    @OneToOne
    private Person person;

    public Job() {
    }

    public Job(String employNo, BLookup employType, String folderNo, String internalTel, BLookup assistType, BLookup postType, String description, Organ organ) {
        this.employNo = employNo;
        this.employType = employType;
        this.folderNo = folderNo;
        this.internalTel = internalTel;
        this.assistType = assistType;
        this.postType = postType;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmployNo() {
        return employNo;
    }

    public void setEmployNo(String employNo) {
        this.employNo = employNo;
    }

    public BLookup getEmployType() {
        if (employType == null)
            employType = new BLookup();
        return employType;
    }

    public void setEmployType(BLookup employType) {
        this.employType = employType;
    }


    public String getFolderNo() {
        return folderNo;
    }

    public void setFolderNo(String folderNo) {
        this.folderNo = folderNo;
    }

    public String getInternalTel() {
        return internalTel;
    }

    public void setInternalTel(String internalTel) {
        this.internalTel = internalTel;
    }

    public BLookup getAssistType() {
        if (assistType == null)
            assistType = new BLookup();
        return assistType;
    }

    public void setAssistType(BLookup assistType) {
        this.assistType = assistType;
    }


    public BLookup getPostType() {
        if (postType == null)
            postType = new BLookup();
        return postType;
    }

    public void setPostType(BLookup postType) {
        this.postType = postType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}