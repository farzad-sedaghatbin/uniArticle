package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.LanguageManagementDAOImpl;
import ir.university.toosi.tms.model.entity.LanguageManagement;

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

public class LanguageManagementServiceImpl<T extends LanguageManagement> {

    @EJB
    private LanguageManagementDAOImpl languageDAO;


    public T findById(String id) {
        try {
            return (T) languageDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public long getMaximumId() {
        try {
            return languageDAO.maximumId("LanguageManagement.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public List<T> getAllLanguageManagement() {
        try {
            return (List<T>) languageDAO.findAll("LanguageManagement.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public String deleteLanguageManagement(T entity) {
        try {
            languageDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createLanguageManagement(T entity) {
        try {
            entity.setId(getMaximumId());
            LanguageManagement languageManagement = new LanguageManagement(entity.getId(), entity.getTitle(), entity.getType());
            languageManagement = (T) languageDAO.create(languageManagement);
            return (T) languageManagement;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editLanguageManagement(T entity) {
        try {
            languageDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean editLanguageManagements(List<T> entities) {
        try {
            for (T entity : entities) {
                languageDAO.update(entity);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}