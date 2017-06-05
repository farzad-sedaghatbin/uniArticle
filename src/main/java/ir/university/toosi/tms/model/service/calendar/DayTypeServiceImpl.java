package ir.university.toosi.tms.model.service.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.calendar.DayTypeDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.calendar.CalendarDate;
import ir.university.toosi.tms.model.entity.calendar.DayType;
import ir.university.toosi.tms.model.entity.rule.Rule;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.model.service.rule.RuleServiceImpl;
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

public class DayTypeServiceImpl<T extends DayType> {

    @EJB
    private DayTypeDAOImpl dayTypeDAO;

    @EJB
    private EventLogServiceImpl eventLogService;

    @EJB
    private CalendarDateServiceImpl calendarDateService;

    @EJB
    private RuleServiceImpl ruleService;


    public T findById(long id) {
        try {
            return (T) dayTypeDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllDayType() {
        try {
            return (List<T>) dayTypeDAO.findAll("DayType.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteDayType(T entity) {
        try {
            List<CalendarDate> calendarDates = calendarDateService.findByDayType(String.valueOf(entity.getId()));
            if (calendarDates != null && calendarDates.size() != 0)
                return new ObjectMapper().writeValueAsString("REL_DAYTYPE_CALENDARDATE");
            List<Rule> rules = ruleService.findByType(entity.getId());
            if (rules != null && rules.size() != 0)
                return new ObjectMapper().writeValueAsString("REL_DAYTYPE_RULE");
            EventLogManager.eventLog(eventLogService, null, DayType.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            dayTypeDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }

    public String deleteDayTypes(List<T> entities) {
        try {
            for (T entity : entities) {
                List<CalendarDate> calendarDates = calendarDateService.findByDayType(String.valueOf(entity.getId()));
                if (calendarDates != null && calendarDates.size() != 0)
                    continue;
                List<Rule> rules = ruleService.findByType(entity.getId());
                if (rules != null && rules.size() != 0)
                    continue;
                EventLogManager.eventLog(eventLogService, null, DayType.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
                dayTypeDAO.delete(entity);
            }
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }

    public String deleteDayTypes(List<T> entities, String userName) {
        try {
            for (T entity : entities) {
                List<CalendarDate> calendarDates = calendarDateService.findByDayType(String.valueOf(entity.getId()));
                if (calendarDates != null && calendarDates.size() != 0)
                    continue;
                List<Rule> rules = ruleService.findByType(entity.getId());
                if (rules != null && rules.size() != 0)
                    continue;
                EventLogManager.eventLog(eventLogService, null, DayType.class.getSimpleName(), EventLogType.DELETE, userName);
                dayTypeDAO.delete(entity);
            }
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createDayType(T entity) {
        try {
            entity.setId(getMaximumId());
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), DayType.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return (T) dayTypeDAO.create(entity);
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editDayType(T entity) {
        try {
            DayType oldDayType = dayTypeDAO.findById(entity.getId());
            DayType newDayType = new DayType();
            newDayType.setStatus("o," + entity.getEffectorUser());
            newDayType.setId(getMaximumId());
            newDayType.setColor(oldDayType.getColor());
            newDayType.setDescription(oldDayType.getDescription());
            newDayType.setTitle(oldDayType.getTitle());
            dayTypeDAO.createOld(newDayType);

            EventLogManager.eventLog(eventLogService, null, DayType.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            dayTypeDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getMaximumId() {
        try {
            return dayTypeDAO.maximumId("DayType.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }
}