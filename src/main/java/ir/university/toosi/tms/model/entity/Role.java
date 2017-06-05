package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Set;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
@Entity
@Table(name = "tb_role")
@NamedQueries({
        @NamedQuery(
                name = "Role.list",
                query = "select r from Role r where r.deleted='0' "
        ),
        @NamedQuery(
                name = "Role.listForWorkgroupEdit",
                query = "select r from Role r where r.deleted='0' and r not in :roles"
        ),
        @NamedQuery(
                name = "Role.findById",
                query = "select r from Role r where r.id=:id"

        ), @NamedQuery(
        name = "Role.maximum",
        query = "select max(r.id) from Role r"
)
})
public class Role extends BaseEntity {

    @Id
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "description")
    private String description;
    @Column(name = "enabled")
    @JsonProperty
    private boolean enabled;
    @JsonProperty
    @Column(name = "name")
    private String name;
    @Transient
    @JsonProperty
    private boolean selected;
    @Transient
    @JsonProperty
    private String descText;
    @JsonProperty
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "tb_operation_role", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "operation_id", referencedColumnName = "id", nullable = false)})
    private Set<Operation> operations;
    @JsonProperty
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "tb_permission_role", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "permission_id", referencedColumnName = "id", nullable = false)})
    private Set<Permission> permissions;

    @Transient
    private String descShow;

    public Role() {
    }

    public Role(long id, String description, boolean enabled, Set<Operation> operations, Set<Permission> permissions) {
        this.description = description;
        this.id = id;
        this.enabled = enabled;
        this.operations = operations;
        this.permissions = permissions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Set<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    public String getDescText() {
        return descText;
    }

    public void setDescText(String descText) {
        this.descText = descText;
    }

    public String getDescShow() {
        return descShow;
    }

    public void setDescShow(String descShow) {
        this.descShow = descShow;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}