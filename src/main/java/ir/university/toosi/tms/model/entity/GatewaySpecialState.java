package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.zone.Gateway;

import javax.persistence.*;


@Entity
@Table(name = "TB_Gateway_Special_State")
@NamedQueries({
        @NamedQuery(
                name = "GatewaySpecialState.list",
                query = "select g from GatewaySpecialState g where g.deleted='0'"
        ),
        @NamedQuery(
                name = "GatewaySpecialState.findById",
                query = "select g from GatewaySpecialState g where g.id=:id"
        ),
        @NamedQuery(
                name = "GatewaySpecialState.findByGatewayId",
                query = "select g from GatewaySpecialState g where g.gateway.id=:id and g.deleted='0'"
        )
})
public class GatewaySpecialState extends BaseEntity {
    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "stateDate")
    private String date;
    @JsonProperty
    @Column(name = "time")
    private String time;
    @JsonProperty
    private String until;
    @JsonProperty
    @Column(name = "gatewayStatus")
    @Enumerated(EnumType.STRING)
    private GatewayStatus gateWayStatus;
    @JsonProperty
    @ManyToOne
    private Gateway gateway;


    public GatewaySpecialState() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
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

    public GatewayStatus getGateWayStatus() {
        return gateWayStatus;
    }

    public void setGateWayStatus(GatewayStatus gateWayStatus) {
        this.gateWayStatus = gateWayStatus;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }
}
