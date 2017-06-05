package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.Languages;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
public class LanguageDAOImpl extends BaseDAOImpl<Languages> {

    public Languages findById(String id) {
        try {
            return (Languages) em.createNamedQuery("Languages.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Languages findByName(String name) {
        try {
            return (Languages) em.createNamedQuery("Languages.findByName")
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


}