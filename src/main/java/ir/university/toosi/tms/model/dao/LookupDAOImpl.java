package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.Lookup;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class LookupDAOImpl extends BaseDAOImpl<Lookup> {

    public Lookup findById(String id) {
        try {
            return (Lookup) em.createNamedQuery("Lookup.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Lookup findByTitle(String title) {
        try {
            return (Lookup) em.createNamedQuery("Lookup.findByTitle")
                    .setParameter("title", title)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Lookup> findDefinable() {
        try {
            return (List<Lookup>) em.createNamedQuery("Lookup.findDefinable")
                    .setParameter("definable", true)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }


}