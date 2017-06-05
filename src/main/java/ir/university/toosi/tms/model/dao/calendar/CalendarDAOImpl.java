package ir.university.toosi.tms.model.dao.calendar;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.calendar.Calendar;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class CalendarDAOImpl extends BaseDAOImpl<Calendar> {

    public Calendar findById(String id) {
        try {
            return (Calendar) em.createNamedQuery("Calendar.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Calendar> findByCode(String code) {
        try {
            return (List<Calendar>) em.createNamedQuery("Calendar.findByCode")
                    .setParameter("code", code + "%")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Calendar> findByName(String name) {
        try {
            return (List<Calendar>) em.createNamedQuery("Calendar.findByName")
                    .setParameter("name", name + "%")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Calendar> findDefault() {
        try {
            return (List<Calendar>) em.createNamedQuery("Calendar.findDefault")
                    .setParameter("defaultCalendar", true)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}