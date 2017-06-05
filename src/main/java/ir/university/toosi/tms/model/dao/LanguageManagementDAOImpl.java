package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.LanguageManagement;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class LanguageManagementDAOImpl extends BaseDAOImpl<LanguageManagement> {

    public LanguageManagement findById(String id) {
        try {
            return (LanguageManagement) em.createNamedQuery("LanguageManagement.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


}