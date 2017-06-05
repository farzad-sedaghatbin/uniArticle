package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "tb_EventLog")
@NamedQueries({
        @NamedQuery(
                name = "EventLog.list",
                query = "select e from EventLog e where e.deleted='0' order by e.date,e.time desc"
        ),
        @NamedQuery(
                name = "EventLog.findById",
                query = "select e from EventLog e where e.id=:id and e.deleted='0'"
        ),
        @NamedQuery(
                name = "EventLog.findByTable",
                query = "select e from EventLog e where e.tableName like :tableName and e.deleted='0'"
        ),
        @NamedQuery(
                name = "EventLog.findEventInDuration",
                query = "select e from EventLog e where e.deleted='0'and e.date between :startDate and :endDate "
        ),
        @NamedQuery(
                name = "EventLog.findEventBeforeDate",
                query = "select e from EventLog e where e.deleted='0'and e.date <= :endDate"
        ),
        @NamedQuery(
                name = "EventLog.findEventAfterDate",
                query = "select e from EventLog e where e.deleted='0'and e.date >= :startDate"
        )
})
public class EventLog extends BaseEntity {

    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    private EventLogType operation;
    @JsonProperty
    @Column(name = "objectId")
    private String objectId;
    @JsonProperty
    @Column(name = "tableName")
    private String tableName;
    @JsonProperty
    @Column(name = "username")
    private String username;
    @JsonProperty
    @Column(name = "event_date")
    private String date;
    @JsonProperty
    @Column(name = "event_time")
    private String time;

    public EventLog() {
    }

    public EventLog(EventLogType operation, String objectId, String tableName, String username, String date, String time) {
        this.operation = operation;
        this.objectId = objectId;
        this.tableName = tableName;
        this.username = username;
        this.date = date;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EventLogType getOperation() {
        return operation;
    }

    public void setOperation(EventLogType operation) {
        this.operation = operation;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
