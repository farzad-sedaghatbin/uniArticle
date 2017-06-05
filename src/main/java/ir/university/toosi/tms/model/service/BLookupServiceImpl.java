package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.BLookupDAOImpl;
import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.LanguageKeyManagement;
import ir.university.toosi.tms.model.entity.LanguageManagement;
import ir.university.toosi.tms.util.EventLogManager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class BLookupServiceImpl<T extends BLookup> {

    @EJB
    private BLookupDAOImpl bLookupDAO;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private LanguageManagementServiceImpl languageManagementService;
    @EJB
    private LanguageKeyManagementServiceImpl languageKeyManagementService;

    public T findById(String id) {
        try {
            return (T) bLookupDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public T findByTitle(BLookup bLookup) {
        try {
            EventLogManager.eventLog(eventLogService, null, BLookup.class.getSimpleName(), EventLogType.SEARCH, bLookup.getEffectorUser());
            return (T) bLookupDAO.findByTitle(bLookup.getTitle());
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllBLookup() {
        try {
            return (List<T>) bLookupDAO.findAll("BLookup.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getByLookup(String lookupCode) {
        try {
            return (List<T>) bLookupDAO.findByLookup(lookupCode);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getByLookupId(long lookupId) {
        try {
            return (List<T>) bLookupDAO.findByLookupId(lookupId);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteBLookup(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), BLookup.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            bLookupDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }

    public long getMaximumId() {
        try {
            return bLookupDAO.maximumId("BLookup.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public T createBLookup(T entity) {
        try {
            entity.setId(getMaximumId());
             /**/
//            LanguageManagement languageManagement = new LanguageManagement();
//            languageManagement.setTitle(entity.getTitleText() == null ? "" : entity.getTitleText());
//            languageManagement.setType(entity.getCurrentLang());
//            languageManagementService.createLanguageManagement(languageManagement);
//
//            Set list = new HashSet();
//            list.add(languageManagement);
//
//            LanguageKeyManagement languageKeyManagement = new LanguageKeyManagement();
//            languageKeyManagement.setDescriptionKey(entity.getId() + BLookup.class.getSimpleName());
//            languageKeyManagement.setLanguageManagements(list);
//            entity.setTitle(entity.getId() + BLookup.class.getSimpleName());
//            languageKeyManagementService.createLanguageKeyManagement(languageKeyManagement);

            /**/

            BLookup bLookup = new BLookup(entity.getId(), entity.getTitleText(), entity.getLookup());
            bLookup = (T) bLookupDAO.create(bLookup);

            EventLogManager.eventLog(eventLogService, String.valueOf(bLookup.getId()), BLookup.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return (T) bLookup;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editBLookup(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), BLookup.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            bLookupDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}