package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
@Entity
@Table(name = "tb_operation")
@NamedQueries({
        @NamedQuery(
                name = "Operation.list",
                query = "select o from Operation o where o.deleted='0'"
        ),
        @NamedQuery(
                name = "Operation.findById",
                query = "select o from Operation o where o.id=:id"
        ),
        @NamedQuery(
                name = "Operation.findByName",
                query = "select o from Operation o where o.name like :name"
        ),
        @NamedQuery(
                name = "Operation.exist",
                query = "select o from Operation o where o.name=:name and o.deleted='0'"
        ), @NamedQuery(
        name = "Operation.maximum",
        query = "select max(o.id) from Operation o"
)
})

public class Operation extends BaseEntity {

    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "name")
    private String name;
    @JsonProperty
    @Column(name = "description")
    private String description;
    @Column(name = "enabled")
    @JsonProperty
    private boolean enabled;
    @JsonProperty
    @ManyToOne
    private Operation parent;
    @Transient
    @JsonProperty
    private boolean selected;

    public Operation() {
    }

    public Operation(long id, String name, String description, boolean enabled, Operation parent) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
        this.parent = parent;
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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Operation getParent() {
        return parent;
    }

    public void setParent(Operation parent) {
        this.parent = parent;
    }
}