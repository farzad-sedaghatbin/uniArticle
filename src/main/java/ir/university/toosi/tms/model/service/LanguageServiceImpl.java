package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.LanguageDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.LanguageKeyManagement;
import ir.university.toosi.tms.model.entity.LanguageManagement;
import ir.university.toosi.tms.model.entity.Languages;
import ir.university.toosi.tms.util.EventLogManager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Hashtable;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class LanguageServiceImpl<T extends Languages> {

    @EJB
    private LanguageDAOImpl languageDAO;
    @EJB
    private EventLogServiceImpl eventLogService;

    @EJB
    private LanguageKeyManagementServiceImpl languageKeyManagementService;

    @EJB
    private LanguageManagementServiceImpl languageManagementService;


    public T findById(String id) {
        try {
            return (T) languageDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public T findByName(String name) {
        try {
            return (T) languageDAO.findByName(name);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllLanguage() {
        try {
            return (List<T>) languageDAO.findAll("Languages.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteLanguage(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Languages.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());

            languageDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createLanguage(T entity) {
        try {
            Languages newLanguages = languageDAO.create(entity);
            List<LanguageKeyManagement> allLanguageKeyManagement = languageKeyManagementService.getAllLanguageKeyManagement();

            for (LanguageKeyManagement languageKeyManagement : allLanguageKeyManagement) {
                LanguageManagement languageManagement = new LanguageManagement();
                languageManagement.setTitle("");
                languageManagement.setType(newLanguages);
                languageManagementService.createLanguageManagement(languageManagement);
                languageKeyManagement.getLanguageManagements().add(languageManagement);
                languageKeyManagementService.editLanguageKeyManagement(languageKeyManagement);
            }
            EventLogManager.eventLog(eventLogService, String.valueOf(newLanguages.getId()), Languages.class.getSimpleName(), EventLogType.ADD, newLanguages.getEffectorUser());

            return (T) newLanguages;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editLanguage(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Languages.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());

            languageDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Hashtable<String, LanguageManagement> loadLanguage(String locale) {

        List<LanguageKeyManagement> languageKeyManagements = languageKeyManagementService.getAllLanguageKeyManagement();
        Hashtable<String, LanguageManagement> lang = new Hashtable<>();
        String key = "";
        LanguageManagement value;

        for (LanguageKeyManagement languageManagement : languageKeyManagements) {
            value = new LanguageManagement();
            key = languageManagement.getDescriptionKey();
            for (LanguageManagement management : languageManagement.getLanguageManagements()) {
               if (management.getType().getName().equalsIgnoreCase(locale)) {
                    value = management;
                    break;
                }
            }
            if (value.getTitle() == null)
                value.setTitle("");
            lang.put(key, value);
        }
        return lang;
    }
}