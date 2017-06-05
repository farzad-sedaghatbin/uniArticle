package ir.university.toosi.tms.model.service.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.calendar.CalendarDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.calendar.Calendar;
import ir.university.toosi.tms.model.entity.calendar.CalendarDate;
import ir.university.toosi.tms.model.entity.calendar.DayType;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.model.service.rule.RulePackageServiceImpl;
import ir.university.toosi.tms.util.EventLogManager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class CalendarServiceImpl<T extends Calendar> implements Serializable {

    @EJB
    private CalendarDAOImpl calendarDAO;

    @EJB
    private DayTypeServiceImpl dayTypeService;

    @EJB
    private EventLogServiceImpl eventLogService;

    @EJB
    private RulePackageServiceImpl rulePackageService;

    @EJB
    private CalendarDateServiceImpl calendarDateService;


    public T findById(String id) {
        try {
            return (T) calendarDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByCode(Calendar calendar) {
        try {
            EventLogManager.eventLog(eventLogService, null, Calendar.class.getSimpleName(), EventLogType.SEARCH, calendar.getEffectorUser());
            return (List<T>) calendarDAO.findByCode(calendar.getCode());
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByName(Calendar calendar) {
        try {
            EventLogManager.eventLog(eventLogService, null, Calendar.class.getSimpleName(), EventLogType.SEARCH, calendar.getEffectorUser());
            return (List<T>) calendarDAO.findByName(calendar.getName());
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllCalendar() {
        try {
            return (List<T>) calendarDAO.findAll("Calendar.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findDefault() {
        try {
            return (List<T>) calendarDAO.findDefault();
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteCalendar(T entity) {
        try {
            List<RulePackage> rulePackages = rulePackageService.findByCalendarID(entity.getId());
            if (rulePackages != null && rulePackages.size() != 0)
                return new ObjectMapper().writeValueAsString("REL_CALENDAR_RULEPACKAGE");
            List<CalendarDate> calendarDates = calendarDateService.findByCalendarID(entity.getId());
            for (CalendarDate calendarDate : calendarDates) {
                calendarDateService.deleteCalendarDate(calendarDate);
            }
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Calendar.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            calendarDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createCalendar(T entity) {
        try {
            T t = (T) calendarDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), Calendar.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editCalendar(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Calendar.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            calendarDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String newCalendar() {
        List<DayType> dayTypes = dayTypeService.getAllDayType();
        CalendarInfo calendarInfo = new CalendarInfo();
        calendarInfo.setStatus(true);
        CalendarInfo.Year a2013 = new CalendarInfo.Year();
        CalendarInfo.Year.selectBox[] list = new CalendarInfo.Year.selectBox[dayTypes.size()];
        for (int i = 0; i < dayTypes.size(); i++) {
            CalendarInfo.Year.selectBox a0 = new CalendarInfo.Year.selectBox();
            a0.set_id(String.valueOf(dayTypes.get(i).getId()));
            a0.setColor("#"+dayTypes.get(i).getColor());
            a0.setTitle(dayTypes.get(i).getTitle());
            a0.setTimestamp("4523456236-4343456236");
            list[i] = a0;
        }
        a2013.setselectBox(list);
        CalendarInfo.Year.Initialize[] listInit = new CalendarInfo.Year.Initialize[0];
        a2013.setInitialize(listInit);
        calendarInfo.setYear(a2013);
        try {
            String s = new ObjectMapper().writeValueAsString(calendarInfo);
            s = s.replace("year", "2017");
            s=s.replace("selectBox","SelectBox");
            s=s.replaceAll("title","Title");
            s=s.replaceAll("color","Color");
            System.out.println(s);
            return s;
          } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "NULL";
    }
}