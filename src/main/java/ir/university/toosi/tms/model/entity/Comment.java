package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "TB_Comment")
@NamedQueries({
        @NamedQuery(
                name = "Comment.authorizeList",
                query = "select c from Comment c where c.deleted='0' and c.authorize=false and c.reject =false "
        ), @NamedQuery(
        name = "Comment.list",
        query = "select c from Comment c where c.deleted='0'"
),
        @NamedQuery(
                name = "Comment.findById",
                query = "select c from Comment c where c.id=:id and c.deleted='0'"
        ),
        @NamedQuery(
                name = "Comment.findByEffectorUser",
                query = "select c from Comment c where c.effectorUser=:effectorUser and c.deleted='0'"
        ),
        @NamedQuery(
                name = "Comment.findByTrafficLog",
                query = "select c from Comment c where c.trafficLog.id=:id and c.deleted='0'and c.authorize=true "
        )
})

public class Comment extends BaseEntity {

    @Column(name = "id")
    @GeneratedValue
    @Id
    @JsonProperty
    private long id;

    @Column(name = "message")
    @JsonProperty
    private String message;

    @JsonProperty
    @OneToOne
    private TrafficLog trafficLog;

    @JsonProperty
    @Column(name = "authorize")
    private boolean authorize;
    @JsonProperty
    @Column(name = "reject")
    private boolean reject;
    @JsonProperty
    private String effectorUser;

    public Comment() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TrafficLog getTrafficLog() {
        return trafficLog;
    }

    public void setTrafficLog(TrafficLog trafficLog) {
        this.trafficLog = trafficLog;
    }

    public boolean isAuthorize() {
        return authorize;
    }

    public void setAuthorize(boolean authorize) {
        this.authorize = authorize;
    }

    public boolean isReject() {
        return reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }

    public String getEffectorUser() {
        return effectorUser;
    }

    public void setEffectorUser(String effectorUser) {
        this.effectorUser = effectorUser;
    }
}
