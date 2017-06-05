package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.SavedQuery;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class SavedQueryDAOImpl extends BaseDAOImpl<SavedQuery> {

    public SavedQuery findById(String id) {
        try {
            return (SavedQuery) em.createNamedQuery("SavedQuery.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public SavedQuery findByTitle(String title) {
        try {
            return (SavedQuery) em.createNamedQuery("SavedQuery.findByTitle")
                    .setParameter("title", title)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<SavedQuery> findScheduled() {
        try {
            return (List<SavedQuery>) em.createNamedQuery("SavedQuery.findScheduled")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}