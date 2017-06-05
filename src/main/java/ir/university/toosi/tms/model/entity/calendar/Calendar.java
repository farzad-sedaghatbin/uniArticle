package ir.university.toosi.tms.model.entity.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BaseEntity;

import javax.persistence.*;
import java.util.Hashtable;
import java.util.List;

@Entity
@Table(name = "TB_Calendar")
@NamedQueries({
        @NamedQuery(
                name = "Calendar.list",
                query = "select c from Calendar c where c.deleted='0' "
        ),
        @NamedQuery(
                name = "Calendar.findById",
                query = "select c from Calendar c where c.id=:id"
        ),
        @NamedQuery(
                name = "Calendar.findByCode",
                query = "select c from Calendar c where c.code like :code and c.deleted='0'"
        ),
        @NamedQuery(
                name = "Calendar.findByName",
                query = "select c from Calendar c where c.name like :name and c.deleted='0'"
        )
})

public class Calendar extends BaseEntity {

    @Column(name = "id")
    @GeneratedValue
    @Id
    @JsonProperty
    private long id;

    @Column(name = "code")
    @JsonProperty
    private String code;

    @Column(name = "name")
    @JsonProperty
    private String name;

    @Column(name = "description")
    @JsonProperty
    private String description;

    @JsonProperty
    @Transient
    private Hashtable<String, Hashtable<Integer, List<String>>> calendarDatesTable = new Hashtable<>();

    @Lob
    @JsonProperty
    @Column(name = "content")
    private byte[] content;

    public Calendar() {
    }

    public Calendar(long id) {
        this.id = id;
    }

    public Calendar(String code, String name, String description, boolean defaultCalendar) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Hashtable<String, Hashtable<Integer, List<String>>> getCalendarDatesTable() {
        return calendarDatesTable;
    }

    public void setCalendarDatesTable(Hashtable<String, Hashtable<Integer, List<String>>> calendarDatesTable) {
        this.calendarDatesTable = calendarDatesTable;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
