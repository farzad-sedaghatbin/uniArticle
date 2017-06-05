package ir.university.toosi.tms.model.entity.personnel;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Iterators;
import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.BaseEntity;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import org.primefaces.model.TreeNode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Entity
@Table(name = "tb_organ")
@NamedQueries({
        @NamedQuery(
                name = "Organ.exist",
                query = "select count(o) from Organ o where  o.code = :code and o.deleted='0'"
        ),
        @NamedQuery(
                name = "Organ.list",
                query = "select o from Organ o  where o.deleted <> '1'"
        ),
        @NamedQuery(
                name = "Organ.active.list",
                query = "select o from Organ o where o.deleted <> '1' and o.parentOrgan is null"
        ),
        @NamedQuery(
                name = "Organ.active.by.parent.list",
                query = "select o from Organ o where o.deleted <> '1' and o.parentOrgan is not null and o.parentOrgan.id = :parentId"
        ),
        @NamedQuery(
                name = "Organ.findById",
                query = "select o from Organ o where o.id=:id"
        ),
        @NamedQuery(
                name = "Organ.findByRulePackageId",
                query = "select o from Organ o where o.rulePackage.id=:id and o.deleted <> '1'"
        )
})
public class Organ extends BaseEntity implements TreeNode {

    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "name")
    private String name;
    @JsonProperty
    @Column(name = "code")
    private String code;
    @JsonProperty
    @Column(name = "title")
    private String title;
    @JsonProperty
    @ManyToOne
    private BLookup organType;
    @JsonProperty
    @ManyToOne
    private Organ parentOrgan;
    @JsonIgnore
    @OneToMany(mappedBy = "parentOrgan", fetch = FetchType.EAGER)
    private Set<Organ> childOrgans;
    @JsonProperty
    @Column(name = "inheritance")
    private boolean inheritance;
    @JsonProperty
    @ManyToOne
    private RulePackage rulePackage;
    @JsonIgnore
    @Transient
    private List<TreeNode> children = new ArrayList<>();

    public Organ() {
    }

    public Organ(String name, String code, String title, BLookup organType) {
        this.name = name;
        this.code = code;
        this.title = title;
        this.organType = organType;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BLookup getOrganType() {
        return organType;
    }

    public void setOrganType(BLookup organType) {
        this.organType = organType;
    }

    public Organ getParentOrgan() {
        return parentOrgan;
    }

    public void setParentOrgan(Organ parentOrgan) {
        this.parentOrgan = parentOrgan;
    }

    public Set<Organ> getChildOrgans() {
        return childOrgans;
    }

    public void setChildOrgans(Set<Organ> childOrgans) {
        this.childOrgans = childOrgans;
    }

    public RulePackage getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(RulePackage rulePackage) {
        this.rulePackage = rulePackage;
    }



    public static List<Organ> prepareHierarchy(List<Organ> organs){
        List<Organ> finalOrgans = new ArrayList<>();
        for(Organ parentOrgan : organs){
            for(Organ childOrgan : organs){
                if(childOrgan.getParentOrgan() != null && childOrgan.getParentOrgan().getId() == parentOrgan.getId()){
                    childOrgan.setParent(parentOrgan);
                    parentOrgan.getChildren().add(childOrgan);

                }
            }
            if(parentOrgan.getParentOrgan() == null){
                finalOrgans.add(parentOrgan);
            }
        }
        return finalOrgans;
    }

    @Override
    public void setParent(org.primefaces.model.TreeNode treeNode) {
        this.parentOrgan= (Organ) treeNode;
    }

    @Override
    public boolean isExpanded() {
        return false;
    }

    @Override
    public void setExpanded(boolean b) {

    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public boolean isLeaf() {
        return children.size()==0;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void setSelected(boolean b) {

    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public void setSelectable(boolean b) {

    }

    @Override
    public boolean isPartialSelected() {
        return false;
    }

    @Override
    public void setPartialSelected(boolean b) {

    }

    @Override
    public void setRowKey(String s) {

    }

    @Override
    public String getRowKey() {
        return null;
    }

    @Override
    public void clearParent() {

    }

    @Override
    public String getType() {
        return "organ";
    }

    @Override
    public void setType(String s) {

    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public List<org.primefaces.model.TreeNode> getChildren() {
        return children;
    }

    @Override
    public TreeNode getParent() {
        return null;
    }

    public boolean isInheritance() {
        return inheritance;
    }

    public void setInheritance(boolean inheritance) {
        this.inheritance = inheritance;
    }


}