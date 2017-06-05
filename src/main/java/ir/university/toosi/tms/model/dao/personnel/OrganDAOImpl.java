package ir.university.toosi.tms.model.dao.personnel;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.personnel.Organ;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class OrganDAOImpl extends BaseDAOImpl<Organ> {


    public Boolean existOrgan(String code) {
        try {
            return (Long) em.createNamedQuery("Organ.exist")
                    .setParameter("code", code)
                    .getSingleResult() > 0 ? true : false;
        } catch (Exception e) {
            return null;
        }
    }

    public Organ findById(long id) {
        try {
            return (Organ) em.createNamedQuery("Organ.findById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Organ> findByRulePackageId(Long id) {
        try {
            return (List<Organ>) em.createNamedQuery("Organ.findByRulePackageId")
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Organ> findAllActiveOrganByParent(long id) {
        try {
            return (List<Organ>) em.createNamedQuery("Organ.active.by.parent.list")
                    .setParameter("parentId", Long.valueOf(id))
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}