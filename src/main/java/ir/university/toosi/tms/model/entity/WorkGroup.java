package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Set;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
@Entity
@Table(name = "tb_workgroup")
@Cacheable(value = true)
@NamedQueries({
        @NamedQuery(
                name = "Workgroup.list",
                query = "select w from WorkGroup w where w.deleted='0'"
        ),
        @NamedQuery(
                name = "Workgroup.findById",
                query = "select w from WorkGroup w where w.id=:id"
        ),
        @NamedQuery(
                name = "Workgroup.findByRoleId",
                query = "select w from WorkGroup w join w.roles r where r.id in:roleId and w.deleted='0'"
        ), @NamedQuery(
        name = "WorkGroup.maximum",
        query = "select max(w.id) from WorkGroup w"
)
})
public class WorkGroup extends BaseEntity {

    @Id
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "name")
    private String name;
    @JsonProperty
    @Column(name = "description")
    private String description;
    @JsonProperty
    @Transient
    private String descText;
    @JsonProperty
    @Column(name = "enabled")
    private String enabled;
    @JsonProperty
    @Transient
    private boolean selected;
    @JsonProperty
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "tb_workGroup_role", joinColumns = {
            @JoinColumn(name = "workGroup_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)})
    private Set<Role> roles;
    @JsonProperty
    @Transient
    private String descShow;


    public WorkGroup() {
    }

    public WorkGroup(long id, String description, String enabled, Set<Role> roles) {
        this.id = id;
        this.description = description;
        this.enabled = enabled;
        this.roles = roles;
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

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}