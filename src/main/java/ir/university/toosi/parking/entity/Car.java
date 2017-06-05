package ir.university.toosi.parking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BaseEntity;
import ir.university.toosi.tms.model.entity.personnel.Person;

import javax.persistence.*;


@Entity
@Table(name = "tb_Car")
@NamedQueries({
        @NamedQuery(
                name = "Car.list",
                query = "select t from Car t where t.deleted='0' "
        ),
        @NamedQuery(
                name = "Car.findById",
                query = "select t from Car t where t.id=:id"
        ), @NamedQuery(
                name = "Car.findByNumber",
                query = "select t from Car t where t.number=:number"
        ), @NamedQuery(
        name = "Car.maximum",
        query = "select max(t.id) from Car t"
)
})
public class Car extends BaseEntity {

    @Column(name = "id")
    @GeneratedValue
    @Id
    @JsonProperty
    private long id;

    @Column(name = "number")
    private String number;
    @OneToOne
    private Person person;

    public Car() {
    }


    public Car(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}