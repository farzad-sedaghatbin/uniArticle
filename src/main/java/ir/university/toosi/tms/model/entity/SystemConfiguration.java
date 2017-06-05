package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Entity
@Table(name = "tb_SystemConfiguration")
@NamedQueries({
        @NamedQuery(
                name = "SystemConfiguration.list",
                query = "select s from SystemConfiguration s where s.deleted='0'"
        ),
        @NamedQuery(
                name = "SystemConfiguration.findById",
                query = "select s from SystemConfiguration s where s.id=:id "
        ),
        @NamedQuery(
                name = "SystemConfiguration.findByParameter",
                query = "select s from SystemConfiguration s where s.parameter=:parameter and s.deleted='0'"
        ), @NamedQuery(
        name = "SystemConfiguration.maximum",
        query = "select max(s.id) from SystemConfiguration s"
)
})
public class SystemConfiguration extends BaseEntity {

    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Enumerated(EnumType.STRING)
    @Column(name = "parameter")
    private SystemParameterType parameter;
    @JsonProperty
    @Column(name = "value")
    private String value;
    @JsonProperty
    @Column(name = "Type")
    private String type;


    public SystemConfiguration() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SystemParameterType getParameter() {
        return parameter;
    }

    public void setParameter(SystemParameterType parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}