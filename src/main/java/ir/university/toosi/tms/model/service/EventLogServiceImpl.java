package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.EventLogDAOImpl;
import ir.university.toosi.tms.model.entity.EventLog;

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

public class EventLogServiceImpl<T extends EventLog> {

    @EJB
    private EventLogDAOImpl eventLogDAO;


    public T findById(String id) {
        try {
            return (T) eventLogDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByTable(EventLog eventLog) {
        try {
            return (List<T>) eventLogDAO.findByTable(eventLog.getTableName());
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllEventLog() {
        try {
            return (List<T>) eventLogDAO.findAll("EventLog.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteEventLog(T entity) {
        try {
            eventLogDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createEventLog(T entity) {
        try {
            return (T) eventLogDAO.create(entity);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findEventInDuration(String startDate, String endDate) {
        try {
            List<EventLog> eventLogs = eventLogDAO.findEventInDuration(startDate, endDate);
            return (List<T>) eventLogs;
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findEventBeforeDate(String endDate) {
        try {
            List<EventLog> eventLogs = eventLogDAO.findEventBeforeDate(endDate);
            return (List<T>) eventLogs;
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findEventAfterDate(String startDate) {
        try {
            List<EventLog> eventLogs = eventLogDAO.findEventAfterDate(startDate);
            return (List<T>) eventLogs;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean editEventLog(T entity) {
        try {
            eventLogDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public EventLogDAOImpl getEventLogDAO() {
        return eventLogDAO;
    }
}