package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.BLookup;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
public class BLookupDAOImpl extends BaseDAOImpl<BLookup> {

    public BLookup findById(String id) {
        try {
            return (BLookup) em.createNamedQuery("BLookup.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public BLookup findByTitle(String title) {
        try {
            return (BLookup) em.createNamedQuery("BLookup.findByTitle")
                    .setParameter("title", title)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<BLookup> findByLookup(String lookupTitle) {
        try {
            return (List<BLookup>) em.createNamedQuery("BLookup.findByLookup")
                    .setParameter("lookupTitle", lookupTitle)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<BLookup> findByLookupId(long lookupId) {
        try {
            return (List<BLookup>) em.createNamedQuery("BLookup.findByLookupId")
                    .setParameter("lookupId", lookupId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }


}