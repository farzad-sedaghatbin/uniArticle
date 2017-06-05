package ir.university.toosi.tms.model.entity.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "TB_CalendarDate")
@NamedQueries({
        @NamedQuery(
                name = "CalendarDate.list",
                query = "select d from CalendarDate d where d.deleted = '0'"
        ),
        @NamedQuery(
                name = "CalendarDate.findById",
                query = "select d from CalendarDate d where d.id=:id and d.deleted = '0'"
        ),
        @NamedQuery(
                name = "CalendarDate.findByCalendarId",
                query = "select d from CalendarDate d where d.calendar.id=:id and d.deleted = '0'"
        ),
        @NamedQuery(
                name = "CalendarDate.findByDayType",
                query = "select d from CalendarDate d where d.dayType.id=:id and d.deleted = '0'"
        ),
        @NamedQuery(
                name = "CalendarDate.findByCalendarIdAndIndex",
                query = "select d from CalendarDate d where d.calendar.id=:id and  d.startDate >=:dateIndex and d.endDate<=:dateIndex and d.deleted = '0'"
        )
})

public class CalendarDate extends BaseEntity {

    @Column(name = "id")
    @GeneratedValue
    @Id
    @JsonProperty
    private long id;
    @Column(name = "startDate")
    @JsonProperty
    private int startDate;
    @Column(name = "endDate")
    @JsonProperty
    private int endDate;
    @ManyToOne
    private DayType dayType;
    @JsonProperty
    @ManyToOne
    private Calendar calendar;

    public CalendarDate() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DayType getDayType() {
        return dayType;
    }

    public void setDayType(DayType dayType) {
        this.dayType = dayType;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Integer getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }
}
