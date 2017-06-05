package ir.university.toosi.tms.model.dao.rule;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.rule.RulePackage;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class RulePackageDAOImpl extends BaseDAOImpl<RulePackage> {
    public List<RulePackage> findByCalendarID(long calendarId) {
        try {
            return (List<RulePackage>) em.createNamedQuery("RulePackage.findByCalendarId")
                    .setParameter("id", calendarId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public RulePackage findById(String id) {
        try {
            return (RulePackage) em.createNamedQuery("RulePackage.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}