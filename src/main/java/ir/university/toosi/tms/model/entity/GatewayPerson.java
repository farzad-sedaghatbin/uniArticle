package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.zone.Gateway;

import javax.persistence.*;


@Entity
@Table(name = "TB_Gateway_Person")
@NamedQueries({
        @NamedQuery(
                name = "GatewayPerson.list",
                query = "select g from GatewayPerson g where g.deleted='0'"
        ),
        @NamedQuery(
                name = "GatewayPerson.findByGatewayId",
                query = "select g from GatewayPerson g where g.gateway.id=:id and g.deleted='0'"
        ),
        @NamedQuery(
                name = "GatewayPerson.findPersonByGatewayId",
                query = "select g.person from GatewayPerson g where g.gateway.id=:id and g.deleted='0'"
        )
        ,
        @NamedQuery(
                name = "GatewayPerson.findByPersonId",
                query = "select g from GatewayPerson g where g.person.id=:id and g.deleted='0'"
        ),
        @NamedQuery(
                name = "GatewayPerson.findByPersonIdAndGatewayId",
                query = "select g from GatewayPerson g where g.person.id=:personId and g.gateway.id =:gatewayId and g.deleted='0'"
        )
})
public class GatewayPerson extends BaseEntity {
    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @ManyToOne
    private Gateway gateway;
    @JsonProperty
    @ManyToOne
    private Person person;


    public GatewayPerson() {
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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
