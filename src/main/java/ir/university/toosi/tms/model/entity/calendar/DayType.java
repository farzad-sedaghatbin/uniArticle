package ir.university.toosi.tms.model.entity.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "TB_DayType")
@NamedQueries({
        @NamedQuery(
                name = "DayType.list",
                query = "select d from DayType d where d.deleted='0'"
        ),
        @NamedQuery(
                name = "DayType.findById",
                query = "select d from DayType d where d.id=:id"
        ), @NamedQuery(
        name = "DayType.maximum",
        query = "select max(d.id) from DayType d"
)
})

public class DayType extends BaseEntity {

    @Column(name = "id")
    @Id
    @JsonProperty
    private long id;

    @JsonProperty
    @Column(name = "title")
    private String title;

    @JsonProperty
    @Column(name = "color")
    private String color;

    @JsonProperty
    @Column(name = "description")
    private String description;

    public DayType() {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
