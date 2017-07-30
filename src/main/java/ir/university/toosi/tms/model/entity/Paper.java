package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * Created by farzad on 7/30/2017.
 */
@Entity
@Table(name = "TB_Paper")
@NamedQueries({
        @NamedQuery(
                name = "Paper.list",
                query = "select p from Paper p where p.deleted='0'"
        ),
        @NamedQuery(
                name = "Paper.findById",
                query = "select p from Paper p where p.id=:id and p.deleted='0'"
        ),
        @NamedQuery(
                name = "Paper.findByName",
                query = "select p from Paper p where p.name like :name and p.deleted='0'"
        ),
        @NamedQuery(
                name = "Paper.findByIp",
                query = "select p from Paper p where p.ip like :ip and p.deleted='0' "
        ),
        @NamedQuery(
                name = "Paper.exist",
                query = "select p from Paper p where p.ip=:ip and p.deleted='0'and p.id <> :id "
        ),@NamedQuery(
        name = "Paper.existNotId",
        query = "select p from Paper p where p.ip=:ip and p.deleted='0' "
), @NamedQuery(
        name = "Paper.maximum",
        query = "select max(p.id) from Paper p"
)
})
public class Paper extends BaseEntity{
    @Column(name = "id")
    @GeneratedValue
    @Id
    @JsonProperty
    private long id;
    @Column(name = "name")
    @JsonProperty
    private String name;

    @Column(name = "author")
    @JsonProperty
    private String author;


    @OneToOne
    User user;

    @Column(name = "description")
    @JsonProperty
    private String description;


    @Column(name = "download")
    @JsonProperty
    private long download;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDownload() {
        return download;
    }

    public void setDownload(long download) {
        this.download = download;
    }
}
