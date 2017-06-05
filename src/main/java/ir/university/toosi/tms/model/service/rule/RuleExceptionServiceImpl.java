package ir.university.toosi.tms.model.service.rule;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.rule.RuleExceptionDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.Role;
import ir.university.toosi.tms.model.entity.rule.RuleException;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.util.EventLogManager;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class RuleExceptionServiceImpl<T extends RuleException> {

    @EJB
    private RuleExceptionDAOImpl ruleExceptionDAO;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private RulePackageServiceImpl rulePackageService;


    public T findById(String id) {
        try {
            return (T) ruleExceptionDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllRuleException() {
        try {
            return (List<T>) ruleExceptionDAO.findAll("RuleException.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getByRulePackageId(Long ruleExceptionId) {
        try {
            return (List<T>) ruleExceptionDAO.findByRulePackageId(ruleExceptionId);
        } catch (Exception e) {
            return null;
        }
    }

    public T getByRulePackageIdAndId(Long ruleExceptionId, Long id) {
        try {
            return (T) ruleExceptionDAO.findByRulePackageIdAndId(ruleExceptionId, id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getByRulePackageIdAndIndex(Long ruleExceptionId, Integer index) {
        try {
            return (List<T>) ruleExceptionDAO.findByRulePackageIdAndIndex(ruleExceptionId, index);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteRuleException(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Role.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            ruleExceptionDAO.delete(findById(String.valueOf(entity.getId())));
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createRuleException(T entity) {
        try {
            entity.setToIndex(CalendarUtil.getPersianDateIndexInYearWithoutSlash(entity.getToDate()));
            entity.setFromIndex(CalendarUtil.getPersianDateIndexInYearWithoutSlash(entity.getFromDate()));
            entity = (T) ruleExceptionDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), RuleException.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return (T) entity;
        } catch (Exception e) {
            return null;
        }
    }

    public long getMaximumId() {
        try {
            return ruleExceptionDAO.maximumId("RuleException.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public boolean editRuleException(T entity) {
        try {
            RuleException oldRuleException = findById(String.valueOf(entity.getId()));

            RuleException newRuleException = new RuleException();
            newRuleException.setRulePackage(new ArrayList<RulePackage>());
            newRuleException.getRulePackage().addAll(oldRuleException.getRulePackage());
            newRuleException.setStatus("o," + entity.getEffectorUser());
            newRuleException.setDeny(oldRuleException.isDeny());
            newRuleException.setEndTime(oldRuleException.getEndTime());
            newRuleException.setEntranceCount(oldRuleException.getEntranceCount());
            newRuleException.setExitCount(oldRuleException.getExitCount());
            newRuleException.setFromDate(oldRuleException.getFromDate());
            newRuleException.setFromIndex(oldRuleException.getFromIndex());
            newRuleException.setName(oldRuleException.getName());
            newRuleException.setStartTime(oldRuleException.getStartTime());
            newRuleException.setToDate(oldRuleException.getToDate());
            newRuleException.setToIndex(oldRuleException.getToIndex());
            ruleExceptionDAO.createOld(newRuleException);

            oldRuleException.setRulePackage(new ArrayList<RulePackage>());
            ruleExceptionDAO.update(oldRuleException);

            entity.setToIndex(CalendarUtil.getPersianDateIndexInYearWithoutSlash(entity.getToDate()));
            entity.setFromIndex(CalendarUtil.getPersianDateIndexInYearWithoutSlash(entity.getFromDate()));
            ruleExceptionDAO.update(entity);

            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), RuleException.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean editPureRuleException(T entity) {
        try {
            entity.setToIndex(CalendarUtil.getPersianDateIndexInYearWithoutSlash(entity.getToDate()));
            entity.setFromIndex(CalendarUtil.getPersianDateIndexInYearWithoutSlash(entity.getFromDate()));
            ruleExceptionDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
