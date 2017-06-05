package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
@Entity
@Table(name = "tb_permission")
@NamedQueries({
        @NamedQuery(
                name = "Permission.list",
                query = "select p from Permission p where p.deleted='0'"
        ),
        @NamedQuery(
                name = "Permission.findById",
                query = "select p from Permission p where p.id=:id"
        ),
        @NamedQuery(
                name = "Permission.findByObjectId",
                query = "select p from Permission p where p.objectId=:objectId and p.permissionType=:permissionType and p.deleted='0'"
        ),
        @NamedQuery(
                name = "Permission.findByPermission",
                query = "select p from Permission p where p.permissionType=:permissionType and p.deleted='0'"
        ),
        @NamedQuery(
                name = "Permission.exist",
                query = "select p from Permission p where p.deleted='0' and p.objectId=:objectId and p.permissionType=:permissionType"
        ), @NamedQuery(
        name = "Permission.maximum",
        query = "select max(p.id) from Permission p"
)
})

public class Permission extends BaseEntity {

    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "objectId")
    private String objectId;
    @JsonProperty
    @Column(name = "objectName")
    private String objectName;
    @JsonProperty
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PermissionType permissionType;
    @Transient
    @JsonProperty
    private boolean selected;

    public Permission() {
    }

    public Permission(String objectId, String objectName, PermissionType permissionType) {
        this.objectId = objectId;
        this.objectName = objectName;
        this.permissionType = permissionType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}