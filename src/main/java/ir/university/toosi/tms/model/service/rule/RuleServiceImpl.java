package ir.university.toosi.tms.model.service.rule;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.rule.RuleDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.rule.Rule;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.util.EventLogManager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class RuleServiceImpl<T extends Rule> {

    @EJB
    private RuleDAOImpl ruleDAO;
    @EJB
    private RulePackageServiceImpl packageService;
    @EJB
    private EventLogServiceImpl eventLogService;


    public T findById(String id) {
        try {
            return (T) ruleDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllRule() {
        try {
            return (List<T>) ruleDAO.findAll("Rule.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getByRulePackageId(Long rulePackageId) {
        try {
            return (List<T>) ruleDAO.findByRulePackageId(rulePackageId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByRulePackageIdAndType(Long rulePackageId, Long typeId) {
        try {
            return (List<T>) ruleDAO.findByRulePackageIdAndType(rulePackageId, typeId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByType(Long typeId) {
        try {
            return (List<T>) ruleDAO.findBType(typeId);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteRule(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Rule.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            ruleDAO.delete(findById(String.valueOf(entity.getId())));
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }

    }

    public T createRule(T entity) {
        try {
            entity.setId(getMaximumId());
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Rule.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return (T) ruleDAO.create(new Rule(entity.getId(), entity.getDayType(), entity.getStartTime(), entity.getEndTime(), entity.getEntranceCount(), entity.getExitCount(), entity.getRulePackage(), entity.isDeny()));
        } catch (Exception e) {
            return null;
        }
    }

    public T editRule(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Rule.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            return (T) ruleDAO.update(entity);
        } catch (Exception e) {
            return null;
        }
    }

    public long getMaximumId() {
        try {
            return ruleDAO.maximumId("Rule.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }
}
