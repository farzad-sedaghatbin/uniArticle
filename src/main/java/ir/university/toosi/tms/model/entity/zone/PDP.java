package ir.university.toosi.tms.model.entity.zone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BaseEntity;

import javax.persistence.*;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;

@Entity
@Table(name = "tb_PDP")
@NamedQueries({
        @NamedQuery(
                name = "PDP.list",
                query = "select p from PDP p where p.deleted='0' "
        ),
        @NamedQuery(
                name = "PDP.findById",
                query = "select p from PDP p where p.id=:id and p.deleted = '0'"
        )
        ,
        @NamedQuery(
                name = "PDP.findByIp",
                query = "select p from PDP p where p.ip like :ip and p.deleted = '0'"
        ),
        @NamedQuery(
                name = "PDP.findByGatewayId",
                query = "select p from PDP p where p.id IN :id and p.deleted = '0'"
        ),
        @NamedQuery(
                name = "PDP.findByCameraId",
                query = "select p from PDP p where p.camera.id=:id and p.deleted = '0'"
        ),
        @NamedQuery(
                name = "PDP.exist",
                query = "select p from PDP p where p.ip=:ip and p.deleted='0'and p.id <> :id "
        ),
        @NamedQuery(
                name = "PDP.existNotId",
                query = "select p from PDP p where p.ip=:ip and p.deleted='0' "
        )
})


public class PDP extends BaseEntity implements TreeNode {
    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "name")
    private String name;
    @Column(name = "enabled")
    @JsonProperty
    private boolean enabled;
    @Column(name = "onlinePDP")
    @JsonProperty
    private boolean online;
    @Column(name = "entrance")
    @JsonProperty
    private boolean entrance;
    @JsonProperty
    @Column(name = "ip")
    private String ip;
    @OneToOne
    @JsonProperty
    private Camera camera;
    @OneToOne
    @JsonProperty
    private Gateway gateway;
    @JsonProperty
    @Column(name = "selected")
    private boolean selected;
    @JsonProperty
    @Column(name = "description")
    private String description;
    @JsonProperty
    @Column(name = "descText")
    private String descText;
    @Column(name = "nameText")
    @JsonProperty
    private String nameText;
    @Column(name = "updateDate")
    @JsonProperty
    private String updateDate;
    @Column(name = "SUCCESS")
    @JsonProperty
    private boolean success = false;
    @JsonIgnore
    @Transient
    private Gateway parentGateway;

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }


    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescText() {
        return descText;
    }

    public void setDescText(String descText) {
        this.descText = descText;
    }

    public String getNameText() {
        return nameText;
    }

    public void setNameText(String nameText) {
        this.nameText = nameText;
    }

    public boolean isEntrance() {
        return entrance;
    }

    public void setEntrance(boolean entrance) {
        this.entrance = entrance;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Gateway getParentGateway() {
        return parentGateway;
    }

    public void setParentGateway(Gateway parentGateway) {
        this.parentGateway = parentGateway;

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return null;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public TreeNode getParent() {
        return null;
    }

    @Override
    public int getIndex(TreeNode node) {
        return 0;
    }

    @Override
    public boolean getAllowsChildren() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Enumeration children() {
        return null;
    }
}
