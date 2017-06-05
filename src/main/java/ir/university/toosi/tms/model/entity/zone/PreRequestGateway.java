package ir.university.toosi.tms.model.entity.zone;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BaseEntity;

import javax.persistence.*;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Entity
@Table(name = "tb_prereq_gateway")
@NamedQueries({
        @NamedQuery(
                name = "PreRequestGateway.list",
                query = "select g from PreRequestGateway g where g.deleted='0' "
        ),
        @NamedQuery(
                name = "PreRequestGateway.findById",
                query = "select g from PreRequestGateway g where g.id=:id and g.deleted='0'"
        ),
        @NamedQuery(
                name = "PreRequestGateway.exist",
                query = "select g from PreRequestGateway g where g.gateway.id=:gatewayId and g.preGateway.id=:preGateId and g.deleted ='0'"
        ),
        @NamedQuery(
                name = "PreRequestGateway.findByGateway",
                query = "select g from PreRequestGateway g where g.gateway.id =:gateId and g.deleted='0'"
        ),
        @NamedQuery(
                name = "PreRequestGateway.findByGatewayAndPreGateway",
                query = "select g from PreRequestGateway g where g.gateway.id =:gateId and g.preGateway.id =:preGateId and g.deleted='0'"
        )
})
public class PreRequestGateway extends BaseEntity {

    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @ManyToOne
    private Gateway gateway;
    @ManyToOne
    private Gateway preGateway;

    public PreRequestGateway() {
    }

    public PreRequestGateway(long id, Gateway gateway, Gateway preGateway) {
        this.id = id;
        this.gateway = gateway;
        this.preGateway = preGateway;
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

    public Gateway getPreGateway() {
        return preGateway;
    }

    public void setPreGateway(Gateway preGateway) {
        this.preGateway = preGateway;
    }
}