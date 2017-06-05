package ir.university.toosi.tms.model.dao.rule;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.rule.RuleException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class RuleExceptionDAOImpl extends BaseDAOImpl<RuleException> {


    public RuleException findById(String id) {
        try {
            return (RuleException) em.createNamedQuery("RuleException.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<RuleException> findByRulePackageId(Long rulePackageId) {
        try {
            return (List<RuleException>) em.createNamedQuery("RuleException.findByRulePackageId")
                    .setParameter("rulePackageId", rulePackageId)
                    .getResultList();

        } catch (Exception e) {
            return null;
        }
    }

    public RuleException findByRulePackageIdAndId(Long rulePackageId, Long id) {
        try {
            return (RuleException) em.createNamedQuery("RuleException.findByRulePackageIdAndId")
                    .setParameter("id", id)
                    .setParameter("rulePackageId", rulePackageId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<RuleException> findByRulePackageIdAndIndex(Long rulePackageId, Integer index) {
        try {
            return (List<RuleException>) em.createNamedQuery("RuleException.findByRulePackageIdAndIndex")
                    .setParameter("dateIndex", index)
                    .setParameter("rulePackageId", rulePackageId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}