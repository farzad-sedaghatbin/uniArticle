package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.LanguageKeyManagement;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
public class LanguageKeyManagementDAOImpl extends BaseDAOImpl<LanguageKeyManagement> {

    public LanguageKeyManagement findById(String id) {
        try {
            return (LanguageKeyManagement) em.createNamedQuery("LanguageKeyManagement.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public LanguageKeyManagement findByDescKey(String descriptionKey) {
        try {
            return (LanguageKeyManagement) em.createNamedQuery("LanguageKeyManagement.findByDescKey")
                    .setParameter("descriptionKey", descriptionKey)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


}