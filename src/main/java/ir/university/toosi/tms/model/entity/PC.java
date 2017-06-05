package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;


@Entity
@Table(name = "TB_PC")
@NamedQueries({
        @NamedQuery(
                name = "PC.list",
                query = "select p from PC p where p.deleted='0'"
        ),
        @NamedQuery(
                name = "PC.findById",
                query = "select p from PC p where p.id=:id and p.deleted='0'"
        ),
        @NamedQuery(
                name = "PC.findByName",
                query = "select p from PC p where p.name like :name and p.deleted='0'"
        ),
        @NamedQuery(
                name = "PC.findByIp",
                query = "select p from PC p where p.ip like :ip and p.deleted='0' "
        ),
        @NamedQuery(
                name = "PC.exist",
                query = "select p from PC p where p.ip=:ip and p.deleted='0'and p.id <> :id "
        ),@NamedQuery(
                name = "PC.existNotId",
                query = "select p from PC p where p.ip=:ip and p.deleted='0' "
        ), @NamedQuery(
        name = "PC.maximum",
        query = "select max(p.id) from PC p"
)
})
public class PC extends BaseEntity {

    @Column(name = "id")
    @GeneratedValue
    @Id
    @JsonProperty
    private long id;
    @Column(name = "name")
    @JsonProperty
    private String name;
    @Column(name = "ip")
    @JsonProperty
    private String ip;
    @JsonProperty
    @ManyToOne
    private BLookup location;
    @Transient
    @JsonProperty
    private boolean selected;

    public PC() {
    }


    public PC(long id) {
        this.id = id;
    }

    public PC(String name, String ip, BLookup location) {
        this.name = name;
        this.ip = ip;
        this.location = location;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public BLookup getLocation() {
        return location;
    }

    public void setLocation(BLookup location) {
        this.location = location;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
