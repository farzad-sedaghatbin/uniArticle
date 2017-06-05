package ir.university.toosi.tms.model.entity.zone;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "tb_Camera")
@NamedQueries({
        @NamedQuery(
                name = "Camera.list",
                query = "select c from Camera c where c.deleted='0' "
        ),
        @NamedQuery(
                name = "Camera.findById",
                query = "select c from Camera c where c.id=:id"
        ),
        @NamedQuery(
                name = "Camera.existNotId",
                query = "select c from Camera c where c.ip=:ip and c.deleted='0'"
        ),
        @NamedQuery(
                name = "Camera.exist",
                query = "select c from Camera c where c.ip=:ip and c.deleted='0' and c.id <> :id "
        )
})
public class Camera extends BaseEntity {
    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "name")
    private String name;
    @JsonProperty
    @Column(name = "password")
    private String password;
    @JsonProperty
    @Column(name = "userName")
    private String userName;

    @Column(name = "enabled")
    @JsonProperty
    private boolean enabled;
    @Transient
    @JsonProperty
    private boolean selected;
    @Transient
    @JsonProperty
    private boolean connected;
    @JsonProperty
    @Column(name = "description")
    private String description;
    @Transient
    @JsonProperty
    private String descText;
    @Transient
    @JsonProperty
    private String nameText;
    @JsonProperty
    @Column(name = "ip")
    private String ip;
    @JsonProperty
    @Column(name = "frame")
    private long frames;

    public Camera() {
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

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getFrames() {
        return frames;
    }

    public void setFrames(long frames) {
        this.frames = frames;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
