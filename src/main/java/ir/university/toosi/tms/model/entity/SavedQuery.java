package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;


@Entity
@Table(name = "TB_SavedQuery")
@NamedQueries({
        @NamedQuery(
                name = "SavedQuery.list",
                query = "select s from SavedQuery s WHERE s.deleted='0' and s.schedule='0'"
        ),
        @NamedQuery(
                name = "SavedQuery.findById",
                query = "select s from SavedQuery s where s.id=:id and s.deleted='0' and s.schedule='0'"
        ),
        @NamedQuery(
                name = "SavedQuery.findByTitle",
                query = "select s from SavedQuery s where s.title=:title and s.deleted='0' and s.schedule='0'"
        ),
        @NamedQuery(
                name = "SavedQuery.findScheduled",
                query = "select s from SavedQuery s where s.deleted='0' and s.schedule='1'"
        ), @NamedQuery(
        name = "SavedQuery.maximum",
        query = "select max(s.id) from SavedQuery s"
)
})
public class SavedQuery extends BaseEntity {

    @Column(name = "id")
    @Id
    @JsonProperty
    private long id;

    @Column(name = "title")
    @JsonProperty
    private String title;
    @Column(name = "type")
    @JsonProperty
    private String type;
    @Column(name = "query")
    @JsonProperty
    private String query;
    @Column(name = "isSchedule")
    @JsonProperty
    private String schedule = "0";
    @Column(name = "count")
    @JsonProperty
    private Long count;
    @Column(name = "exportType")
    @JsonProperty
    private String exportType;

    public SavedQuery() {
    }

    public SavedQuery(long id, String title, String type, String query) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.query = query;
    }

    public SavedQuery(long id, String title, String type, String query, String schedule) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.query = query;
        this.schedule = schedule;
    }

    public SavedQuery(long id,String title, String query, String schedule, Long count, String exportType, String type) {
        this.id = id;
        this.title=title;
        this.query = query;
        this.schedule = schedule;
        this.count = count;
        this.type = type;
        this.exportType = exportType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }
}
