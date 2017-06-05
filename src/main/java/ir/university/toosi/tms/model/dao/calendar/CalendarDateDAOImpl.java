package ir.university.toosi.tms.model.dao.calendar;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.calendar.CalendarDate;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
public class CalendarDateDAOImpl extends BaseDAOImpl<CalendarDate> {

    public List<CalendarDate> findByCalendarID(long calendarId) {
        try {
            return (List<CalendarDate>) em.createNamedQuery("CalendarDate.findByCalendarId")
                    .setParameter("id", calendarId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CalendarDate> findByDayType(String dayTypeId) {
        try {
            return (List<CalendarDate>) em.createNamedQuery("CalendarDate.findByDayType")
                    .setParameter("id", Long.valueOf(dayTypeId))
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CalendarDate findByCalendarIdAndIndex(String calendarId, String index) {
        try {
            List<CalendarDate> list = em.createNamedQuery("CalendarDate.findByCalendarId")
                    .setParameter("id", Long.valueOf(calendarId))
                    .getResultList();
            for (CalendarDate calendarDate : list) {
                if (calendarDate.getStartDate() <= Integer.valueOf(index) && calendarDate.getEndDate() >= Integer.valueOf(index))
                    return calendarDate;
            }

        } catch (Exception e) {
            return null;
        }
        return null;
    }


}