package ir.university.toosi.tms.model.dao.rule;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.rule.Rule;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class RuleDAOImpl extends BaseDAOImpl<Rule> {


    public Rule findById(String id) {
        try {
            return (Rule) em.createNamedQuery("Rule.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Rule> findByRulePackageId(Long rulePackageId) {
        try {
            return (List<Rule>) em.createNamedQuery("Rule.findByRulePackageId")
                    .setParameter("rulePackageId", rulePackageId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Rule> findByRulePackageIdAndType(Long rulePackageId, Long dayTypeId) {
        try {
            return (List<Rule>) em.createNamedQuery("Rule.findByRulePackageIdAndType")
                    .setParameter("rulePackageId", rulePackageId)
                    .setParameter("typeId", dayTypeId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Rule> findBType(Long dayTypeId) {
        try {
            return (List<Rule>) em.createNamedQuery("Rule.findByType")
                    .setParameter("typeId", dayTypeId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}