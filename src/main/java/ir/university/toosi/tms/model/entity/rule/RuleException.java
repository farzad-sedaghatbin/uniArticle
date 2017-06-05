package ir.university.toosi.tms.model.entity.rule;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
@Entity
@Table(name = "tb_RuleException")
@NamedQueries({
        @NamedQuery(
                name = "RuleException.list",
                query = "select r from RuleException  r where r.deleted='0' and r.status like 'C%'"
        ),
        @NamedQuery(
                name = "RuleException.findById",
                query = "select r from RuleException r where r.id=:id"

        ),
        @NamedQuery(
                name = "RuleException.findByRulePackageId",
                query = "select r from RuleException r join r.rulePackage p where p.id in:rulePackageId and r.deleted = '0'"

        ),
        @NamedQuery(
                name = "RuleException.findByRulePackageIdAndId",
                query = "select r from RuleException r join r.rulePackage p where p.id in:rulePackageId and r.id =:id and r.deleted = '0'"

        ),
        @NamedQuery(
                name = "RuleException.findByRulePackageIdAndIndex",
                query = "select r from RuleException r join r.rulePackage p where p.id in:rulePackageId and  r.fromIndex >=:dateIndex and r.toIndex<=:dateIndex and r.deleted = '0'"

        ), @NamedQuery(
        name = "RuleException.maximum",
        query = "select max(r.id) from RuleException r"
)
})
public class RuleException extends BaseEntity {

    @Id
    @JsonProperty
    @GeneratedValue
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "fromIndex")
    private Integer fromIndex;
    @JsonProperty
    @Column(name = "toIndex")
    private Integer toIndex;
    @JsonProperty
    @Column(name = "name")
    private String name;
    @JsonProperty
    @Column(name = "fromDate")
    private String fromDate;
    @JsonProperty
    @Column(name = "toDate")
    private String toDate;
    @JsonProperty
    @Column(name = "startTime")
    private String startTime;
    @JsonProperty
    @Column(name = "endTime")
    private String endTime;
    @JsonProperty
    @Column(name = "entranceCount")
    private String entranceCount;
    @JsonProperty
    @Column(name = "exitCount")
    private String exitCount;
    @JsonProperty
    @Column(name = "deny")
    private boolean deny;
    @JsonProperty
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "tb_Package_exception", joinColumns = {
            @JoinColumn(name = "exception_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "Packag_id", referencedColumnName = "id", nullable = false)})
    private List<RulePackage> rulePackage;

    public RuleException() {
    }

    public RuleException(long id, String startTime, String endTime, String entranceCount, String exitCount) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.entranceCount = entranceCount;
        this.exitCount = exitCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(Integer fromIndex) {
        this.fromIndex = fromIndex;
    }

    public Integer getToIndex() {
        return toIndex;
    }

    public void setToIndex(Integer toIndex) {
        this.toIndex = toIndex;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEntranceCount() {
        return entranceCount;
    }

    public void setEntranceCount(String entranceCount) {
        this.entranceCount = entranceCount;
    }

    public String getExitCount() {
        return exitCount;
    }

    public void setExitCount(String exitCount) {
        this.exitCount = exitCount;
    }


    public List<RulePackage> getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(List<RulePackage> rulePackage) {
        this.rulePackage = rulePackage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeny() {
        return deny;
    }

    public void setDeny(boolean deny) {
        this.deny = deny;
    }
}