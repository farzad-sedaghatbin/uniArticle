package ir.university.toosi.tms.model.service.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.calendar.CalendarDateDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.calendar.CalendarDate;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.util.EventLogManager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class CalendarDateServiceImpl<T extends CalendarDate> {

    @EJB
    private CalendarDateDAOImpl calendarDateDAO;

    @EJB
    private DayTypeServiceImpl dayTypeService;

    @EJB
    private EventLogServiceImpl eventLogService;


    public List<T> findByCalendarID(long calendarID) {
        try {
            return (List<T>) calendarDateDAO.findByCalendarID(calendarID);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByDayType(String dayTypeId) {
        try {
            return (List<T>) calendarDateDAO.findByDayType(dayTypeId);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllCalendarDate() {
        try {
            return (List<T>) calendarDateDAO.findAll("CalendarDate.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteCalendarDate(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), CalendarDate.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            calendarDateDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createCalendarDate(T entity) {
        try {
            T t = (T) calendarDateDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), CalendarDate.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editCalendarDate(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), CalendarDate.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            calendarDateDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public T findByCalendarIdAndIndex(String calendarID, String index) {
        try {
            return (T) calendarDateDAO.findByCalendarIdAndIndex(calendarID, index);
        } catch (Exception e) {
            return null;
        }
    }


}