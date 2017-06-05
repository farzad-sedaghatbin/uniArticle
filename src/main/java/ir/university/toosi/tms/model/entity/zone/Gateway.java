package ir.university.toosi.tms.model.entity.zone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Iterators;
import ir.university.toosi.tms.model.entity.BaseEntity;
import ir.university.toosi.tms.model.entity.rule.RulePackage;

import javax.persistence.*;
import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Entity
@Table(name = "tb_Gateway")
@NamedQueries({
        @NamedQuery(
                name = "Gateway.list",
                query = "select g from Gateway g where g.deleted='0' "
        ),
        @NamedQuery(
                name = "Gateway.listExceptThese",
                query = "select g from Gateway g where g.deleted='0' and g not in (:gatewayIds)"
        ),
        @NamedQuery(
                name = "Gateway.listForZone",
                query = "select g from Gateway g where g.deleted='0' and g not in (:gateways)"
        ),
        @NamedQuery(
        name = "Gateway.enableList",
        query = "select g from Gateway g where g.deleted='0' and g.enabled=true "
),
        @NamedQuery(
                name = "Gateway.findById",
                query = "select g from Gateway g where g.id=:id and g.deleted='0'"
        ),
        @NamedQuery(
                name = "Gateway.findByZone",
                query = "select g from Gateway g where g.zone=:zone and g.deleted='0'"
        ),
        @NamedQuery(
                name = "Gateway.exist",
                query = "select g from Gateway g where g.name=:name and g.deleted='0'"
        ),
        @NamedQuery(
                name = "Gateway.findByZoneAndRulePackage",
                query = "select g from Gateway g where g.zone.id =:zoneId and g.rulePackage.id =:rulepackageId and g.deleted='0'"
        ),
        @NamedQuery(
                name = "Gateway.findByRulePackage",
                query = "select g from Gateway g where g.rulePackage.id =:rulepackageId and g.deleted='0'"
        ),
        @NamedQuery(
                name = "Gateway.findByCamera",
                query = "select g from Gateway g join g.cameras c where c.id in:cameraId and g.deleted='0'"
        )
})
public class Gateway extends BaseEntity implements TreeNode {

    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @Column(name = "enabled")
    @JsonProperty
    private boolean enabled;
    @JsonProperty
    @Column(name = "name")
    private String name;
    @JsonProperty
    @Column(name = "description")
    private String description;
    @JsonProperty
    @Column(name = "passBack")
    private boolean passBackControl;
    @Transient
    @JsonProperty
    private boolean selected;
    @JsonProperty
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "tb_gateway_camera", joinColumns = {
            @JoinColumn(name = "gateway_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "camera_id", referencedColumnName = "id", nullable = false)})
    private List<Camera> cameras;

    @Transient
    @JsonProperty
    private List<Long> preRequestGateways;

    @JsonProperty
    @ManyToOne
    private Zone zone;
    @JsonProperty
    @ManyToOne
    private RulePackage rulePackage;


    @JsonIgnore
    @Transient
    private Zone parentZone;

    @Transient
private List<PDP> children = new ArrayList<>();
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Gateway() {
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

    public boolean isPassBackControl() {
        return passBackControl;
    }

    public void setPassBackControl(boolean passBackControl) {
        this.passBackControl = passBackControl;
    }

    public List<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }


    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public RulePackage getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(RulePackage rulePackage) {
        this.rulePackage = rulePackage;
    }

    public List<Long> getPreRequestGateways() {
        return preRequestGateways;
    }

    public void setPreRequestGateways(List<Long> preRequestGateways) {
        this.preRequestGateways = preRequestGateways;
    }


    public List<PDP> getChildren() {
        return children;
    }

    public void setChildren(List<PDP> children) {
        this.children = children;
    }

    @JsonIgnore
    public void addChild(PDP pdp) {
        children.add(pdp);
    }

    @JsonIgnore
    public TreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }
    @JsonIgnore
 
    public int getChildCount() {
        return children.size();
    }
    @JsonIgnore
 
    public TreeNode getParent() {
        return parentZone;
    }
    @JsonIgnore
 
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }
    @JsonIgnore
 
    public boolean getAllowsChildren() {
        return false;
    }
    @JsonIgnore
 
    public boolean isLeaf() {
        return false;
    }
    @JsonIgnore

    public Enumeration children() {
        return Iterators.asEnumeration(children.iterator());
    }

    @JsonIgnore

    public Zone getParentZone() {
        return parentZone;
    }

    @JsonIgnore
    public void setParentZone(Zone parentZone) {
        this.parentZone = parentZone;
    }
    @JsonIgnore

    public String getType(){
        return "gateway";
    }

}