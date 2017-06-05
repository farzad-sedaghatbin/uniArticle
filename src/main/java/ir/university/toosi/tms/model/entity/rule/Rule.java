package ir.university.toosi.tms.model.entity.rule;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BaseEntity;
import ir.university.toosi.tms.model.entity.calendar.DayType;

import javax.persistence.*;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
@Entity
@Table(name = "tb_Rule")
@NamedQueries({
        @NamedQuery(
                name = "Rule.list",
                query = "select r from Rule r where r.deleted='0' "
        ),
        @NamedQuery(
                name = "Rule.findById",
                query = "select r from Rule r where r.id=:id"

        ),
        @NamedQuery(
                name = "Rule.findByRulePackageId",
                query = "select r from Rule r where r.rulePackage.id=:rulePackageId and r.deleted ='0'"

        ), @NamedQuery(
        name = "Rule.maximum",
        query = "select max(r.id) from Rule r"
),
        @NamedQuery(
                name = "Rule.findByRulePackageIdAndType",
                query = "select r from Rule r where r.rulePackage.id=:rulePackageId and r.dayType.id =:typeId and r.deleted ='0' order by r.deny desc"

        ),
        @NamedQuery(
                name = "Rule.findByType",
                query = "select r from Rule r where r.dayType.id =:typeId"

        )
})
public class Rule extends BaseEntity {

    @Id
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @ManyToOne
    private DayType dayType;
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
    @ManyToOne
    private RulePackage rulePackage;

    public Rule() {

    }

    public Rule(long id, DayType dayType, String startTime, String endTime, String entranceCount, String exitCount, RulePackage rulePackage, boolean deny) {
        this.id = id;
        this.dayType = dayType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.entranceCount = entranceCount;
        this.exitCount = exitCount;
        this.rulePackage = rulePackage;
        this.deny = deny;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DayType getDayType() {
        return dayType;
    }

    public void setDayType(DayType dayType) {
        this.dayType = dayType;
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

    public RulePackage getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(RulePackage rulePackage) {
        this.rulePackage = rulePackage;
    }

    public boolean isDeny() {
        return deny;
    }

    public void setDeny(boolean deny) {
        this.deny = deny;
    }
}