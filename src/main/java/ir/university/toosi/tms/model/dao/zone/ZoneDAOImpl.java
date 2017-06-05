package ir.university.toosi.tms.model.dao.zone;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.zone.Zone;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class ZoneDAOImpl extends BaseDAOImpl<Zone> {


    public Zone findById(long id) {
        try {
            return (Zone) em.createNamedQuery("Zone.findById")
                    .setParameter("id",id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Zone> findByRulePackageId(Long id) {
        try {
            return (List<Zone>) em.createNamedQuery("Zone.findByRulePackageId")
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}