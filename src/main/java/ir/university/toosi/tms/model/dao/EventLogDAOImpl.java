package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.EventLog;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
public class EventLogDAOImpl extends BaseDAOImpl<EventLog> {

    public EventLog findById(String id) {
        try {
            return (EventLog) em.createNamedQuery("EventLog.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<EventLog> findByTable(String tableName) {
        try {
            return (List<EventLog>) em.createNamedQuery("EventLog.findByTable")
                    .setParameter("tableName", tableName + "%")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<EventLog> findEventInDuration(String startDate, String endDate) {
        try {
            return (List<EventLog>) em.createNamedQuery("EventLog.findEventInDuration")
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<EventLog> findEventBeforeDate(String endDate) {
        try {
            return (List<EventLog>) em.createNamedQuery("EventLog.findEventBeforeDate")
                    .setParameter("endDate", endDate)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<EventLog> findEventAfterDate(String startDate) {
        try {
            return (List<EventLog>) em.createNamedQuery("EventLog.findEventAfterDate")
                    .setParameter("startDate", startDate)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}