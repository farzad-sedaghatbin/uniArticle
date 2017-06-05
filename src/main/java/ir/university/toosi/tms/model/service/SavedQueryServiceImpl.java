package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.SavedQueryDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.SavedQuery;
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

public class SavedQueryServiceImpl<T extends SavedQuery> {

    @EJB
    private SavedQueryDAOImpl savedQueryDAO;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private LanguageManagementServiceImpl languageManagementService;
    @EJB
    private LanguageKeyManagementServiceImpl languageKeyManagementService;

    public T findById(String id) {
        try {
            return (T) savedQueryDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public T findByTitle(SavedQuery savedQuery) {
        try {
            EventLogManager.eventLog(eventLogService, null, SavedQuery.class.getSimpleName(), EventLogType.SEARCH, savedQuery.getEffectorUser());
            return (T) savedQueryDAO.findByTitle(savedQuery.getTitle());
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findScheduled() {
        try {
            return (List<T>) savedQueryDAO.findScheduled();
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllSavedQuery() {
        try {
            return (List<T>) savedQueryDAO.findAll("SavedQuery.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteSavedQuery(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), SavedQuery.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            savedQueryDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }

    public long getMaximumId() {
        try {
            return savedQueryDAO.maximumId("SavedQuery.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public T createSavedQuery(T entity) {
        try {
            entity.setId(getMaximumId());
             /**/

            SavedQuery savedQuery = new SavedQuery(entity.getId(),entity.getTitle(), entity.getQuery(), entity.getSchedule(), entity.getCount(), entity.getExportType(), entity.getType());
            savedQuery = (T) savedQueryDAO.create(savedQuery);

            EventLogManager.eventLog(eventLogService, String.valueOf(savedQuery.getId()), SavedQuery.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return (T) savedQuery;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editSavedQuery(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), SavedQuery.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            savedQueryDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}