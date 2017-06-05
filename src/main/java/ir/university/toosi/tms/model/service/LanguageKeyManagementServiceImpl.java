package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.LanguageKeyManagementDAOImpl;
import ir.university.toosi.tms.model.entity.LanguageKeyManagement;
import ir.university.toosi.tms.model.entity.LanguageManagement;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.HashSet;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class LanguageKeyManagementServiceImpl<T extends LanguageKeyManagement> {

    @EJB
    private LanguageKeyManagementDAOImpl languageDAO;


    public T findById(String id) {
        try {
            return (T) languageDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public T findByDescKey(String descKey) {
        try {
            return (T) languageDAO.findByDescKey(descKey);
        } catch (Exception e) {
            return null;
        }
    }

    public long getMaximumId() {
        try {
            return languageDAO.maximumId("LanguageKeyManagement.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public List<T> getAllLanguageKeyManagement() {
        try {
            return (List<T>) languageDAO.findAll("LanguageKeyManagement.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public String deleteLanguageKeyManagement(T entity) {
        try {
            languageDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createLanguageKeyManagement(T entity) {
        try {
            entity.setId(getMaximumId());
            LanguageKeyManagement languageKeyManagement = new LanguageKeyManagement(entity.getId(), entity.getDescriptionKey(), new HashSet<LanguageManagement>());
            languageKeyManagement = (T) languageDAO.create(languageKeyManagement);
            languageKeyManagement.getLanguageManagements().clear();
            languageKeyManagement.getLanguageManagements().addAll(entity.getLanguageManagements());
            editLanguageKeyManagement((T) languageKeyManagement);
            return (T) languageKeyManagement;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editLanguageKeyManagement(T entity) {
        try {
            languageDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}