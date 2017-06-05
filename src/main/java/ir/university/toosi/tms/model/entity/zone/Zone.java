package ir.university.toosi.tms.model.entity.zone;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterators;
import ir.university.toosi.tms.model.entity.BaseEntity;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;

import javax.persistence.*;
import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Entity
@Table(name = "tb_Zone")
@NamedQueries({
        @NamedQuery(
                name = "Zone.list",
                query = "select z from Zone z where z.deleted='0'"
        ),
        @NamedQuery(
                name = "Zone.findById",
                query = "select z from Zone z where z.id=:id"
        ),
        @NamedQuery(
                name = "Zone.findByRulePackageId",
                query = "select z from Zone z where z.rulePackage.id=:id and z.deleted='0'"
        ),
        @NamedQuery(
                name = "Zone.exist",
                query = "select z from Zone z where z.name=:name and z.deleted='0'"
        )
})
public class Zone extends BaseEntity implements TreeNode {

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
    @JsonProperty
    @Column(name = "passBack")
    private boolean passBackControl;
    @JsonProperty
    @Column(name = "truePassControl")
    private boolean truePass;
    @Column(name = "enabled")
    @JsonProperty
    private boolean enabled;
    @Transient
    @JsonProperty
    private boolean selected;
    @Transient
    @JsonProperty
    private String descText;
    @JsonProperty
    @ManyToOne
    private RulePackage rulePackage;

@Transient
private List<Gateway> children = new ArrayList<>();
    public Zone() {
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

    public boolean isTruePass() {
        return truePass;
    }

    public void setTruePass(boolean truePass) {
        this.truePass = truePass;
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

    public String getDescText() {
        return descText;
    }

    public void setDescText(String descText) {
        this.descText = descText;
    }

    public RulePackage getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(RulePackage rulePackage) {
        this.rulePackage = rulePackage;
    }
    @JsonIgnore
    public void addChild(Gateway zone) {
        children.add(zone);
    }

    @JsonIgnore
    public static List<Zone> prepareHierarchy(List<Zone> Zones, UserManagementAction me) throws IOException {
        List<Zone> finalZones = new ArrayList<>();
        List<Gateway> gateways = new ArrayList<>();
        List<PDP> pdps = new ArrayList<>();
        for (Zone parentZone : Zones) {
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/findGatewayByZone");
            gateways = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(parentZone)), new TypeReference<List<Gateway>>() {
            });
            for (Gateway childGate : gateways) {
                parentZone.addChild(childGate);
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/findPdpByGatewayId");
                pdps = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(childGate.getId())), new TypeReference<List<PDP>>() {
                });
                for (PDP pdp : pdps) {
                    childGate.addChild(pdp);
                }
            }
            finalZones.add(parentZone);
        }
        return finalZones;
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
        return null;
    }

    @JsonIgnore

    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @JsonIgnore
    public boolean getAllowsChildren() {
        return true;
    }

    @JsonIgnore
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @JsonIgnore
    @Override
    public Enumeration children() {
        return Iterators.asEnumeration(children.iterator());
    }


}