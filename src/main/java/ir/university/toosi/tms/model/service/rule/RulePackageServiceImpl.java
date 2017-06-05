package ir.university.toosi.tms.model.service.rule;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.rule.RulePackageDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.calendar.Calendar;
import ir.university.toosi.tms.model.entity.calendar.CalendarDate;
import ir.university.toosi.tms.model.entity.calendar.DayType;
import ir.university.toosi.tms.model.entity.personnel.Organ;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.rule.Rule;
import ir.university.toosi.tms.model.entity.rule.RuleException;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.Zone;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.model.service.calendar.CalendarDateServiceImpl;
import ir.university.toosi.tms.model.service.personnel.OrganServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
import ir.university.toosi.tms.model.service.zone.ZoneServiceImpl;
import ir.university.toosi.tms.util.EventLogManager;
import ir.university.toosi.tms.util.Initializer;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class RulePackageServiceImpl<T extends RulePackage> {

    @EJB
    private RulePackageDAOImpl rulePackageDAO;
    @EJB
    private RuleServiceImpl ruleService;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private CalendarDateServiceImpl calendarDateService;
    @EJB
    private RuleExceptionServiceImpl ruleExceptionService;
    @EJB
    private OrganServiceImpl organService;
    @EJB
    private PersonServiceImpl personService;
    @EJB
    private GatewayServiceImpl gatewayService;
    @EJB
    private ZoneServiceImpl zoneService;


    public T findById(String id) {
        try {
            return (T) rulePackageDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByCalendarID(long calendarID) {
        try {
            return (List<T>) rulePackageDAO.findByCalendarID(calendarID);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllRulePackage() {
        try {
            return (List<T>) rulePackageDAO.findAll("RulePackage.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteRulePackage(T entity) {
        try {

            List<Organ> organs = organService.findByRulePackageId(entity.getId());
            if (organs != null && organs.size() != 0)
                return new ObjectMapper().writeValueAsString("REL_RULEPACKAGE_ORGAN");

            List<Person> persons = personService.findByRulePackage(entity.getId());
            if (persons != null && persons.size() != 0)
                return new ObjectMapper().writeValueAsString("REL_RULEPACKAGE_PERSON");

            List<Gateway> gateways = gatewayService.findByRulePackage(entity.getId());
            if (gateways != null && gateways.size() != 0)
                return new ObjectMapper().writeValueAsString("REL_RULEPACKAGE_GATEWAY");

            List<Zone> zones = zoneService.findByRulePackageId(entity.getId());
            if (zones != null && zones.size() != 0)
                return new ObjectMapper().writeValueAsString("REL_RULEPACKAGE_ZONE");

            List<Rule> rules = ruleService.getByRulePackageId(entity.getId());
            for (Rule rule : rules) {
                ruleService.deleteRule(rule);
            }

            List<RuleException> ruleExceptions = ruleExceptionService.getByRulePackageId(entity.getId());
            for (RuleException ruleException : ruleExceptions) {
                List<RulePackage> rulePackages = new ArrayList();
                for (RulePackage rulePackage : ruleException.getRulePackage()) {
                    if (rulePackage.getId() != entity.getId())
                        rulePackages.add(rulePackage);
                }
                ruleException.getRulePackage().clear();
                ruleExceptionService.editRuleException(ruleException);
                ruleException.getRulePackage().addAll(rulePackages);
                ruleExceptionService.editRuleException(ruleException);
            }

            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), RulePackage.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            rulePackageDAO.delete(findById(String.valueOf(entity.getId())));
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createRulePackage(T entity) {
        try {
            List<Rule> rules = new ArrayList<>();

            RulePackage rulePackage = new RulePackage(entity.getId(), entity.getName(), entity.getCalendar(), entity.isAniPassBack(), entity.isAllowExit(), entity.isAllowExitGadget());
            rulePackage = (T) rulePackageDAO.create(rulePackage);
//            for (Rule rule : entity.getRules()) {
//                ruleService.createRule(rule);
//                rules.add(rule);
//            }
//            rulePackage = rulePackageDAO.findById(String.valueOf(rulePackage.getId()));
//            rulePackage.getRules().clear();
//            rulePackage.getRules().addAll(rules);
//            rulePackageDAO.update(rulePackage);

            EventLogManager.eventLog(eventLogService, String.valueOf(rulePackage.getId()), RulePackage.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return (T) rulePackage;
        } catch (Exception e) {
            return null;
        }
    }

    public long getMaximumId() {
        try {
            return rulePackageDAO.maximumId("RulePackage.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public boolean pureEditRulePackage(T entity) {
        rulePackageDAO.update(entity);
        return true;
    }

    public boolean editRulePackage(T entity) {
        try {
            RulePackage oldRulePackage = rulePackageDAO.findById(String.valueOf(entity.getId()));
            RulePackage newRulePackage = new RulePackage();
            newRulePackage.setStatus("o," + entity.getEffectorUser());
            newRulePackage.setName(oldRulePackage.getName());
            newRulePackage.setCalendar(oldRulePackage.getCalendar());
            newRulePackage.setAllowExit(oldRulePackage.isAllowExit());
            newRulePackage.setAllowExitGadget(oldRulePackage.isAllowExitGadget());
            newRulePackage.setAniPassBack(oldRulePackage.isAniPassBack());
            rulePackageDAO.createOld(newRulePackage);

//            List<Rule> rules = ruleService.getByRulePackageId(entity.getId());
//            for (Rule rule : rules) {
//                rule.setDeleted("1");
//                rule.setRulePackage(newRulePackage);
//                ruleService.editRule(rule);
//            }

            oldRulePackage.setName(entity.getName());
            oldRulePackage.setCalendar(entity.getCalendar());
            oldRulePackage.setAllowExit(entity.isAllowExit());
            oldRulePackage.setAllowExitGadget(entity.isAllowExitGadget());
            oldRulePackage.setAniPassBack(entity.isAniPassBack());
            rulePackageDAO.update(oldRulePackage);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), RulePackage.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void fillRulePackageHashTable() {

//        String todayIndex = CalendarUtil.getPersianDateIndexInYear(new Locale("fa"));
//        Initializer.dateRuleHashTables = new Hashtable<>();
//        List<RulePackage> rulePackages = (List<RulePackage>) getAllRulePackage();
//        if (rulePackages == null || rulePackages.size() == 0)
//            return;
//        for (RulePackage rulePackage : rulePackages) {
//            Calendar calendar = rulePackage.getCalendar();
//            if (calendar == null)
//                continue;
//            Hashtable<String, DayType> calendarDateHashtable = new Hashtable<>();
//            Hashtable<String, List<Rule>> ruleListHashtable = new Hashtable<>();
//            Hashtable<String, List<RuleException>> ruleExcptionListHashtable = new Hashtable<>();
//            for (int i = 0; i < 6; i++) {
//                Integer index = Integer.valueOf(Integer.valueOf(todayIndex) + i);
//                List<RuleException> ruleExceptions = ruleExceptionService.getByRulePackageIdAndIndex(rulePackage.getId(), index);
//                if (ruleExceptions != null && ruleExceptions.size() != 0) {
//                    ruleExcptionListHashtable.put(String.valueOf(index), ruleExceptions);
//                    continue;
//                }
//                CalendarDate calendarDate = calendarDateService.findByCalendarIdAndIndex(String.valueOf(calendar.getId()), String.valueOf(index));
//                if (calendarDate != null)
//                    calendarDateHashtable.put(String.valueOf(index), calendarDate.getDayType());
//            }
//            for (String s : calendarDateHashtable.keySet()) {
//                DayType dayType = calendarDateHashtable.get(s);
//                List<Rule> rules = ruleService.findByRulePackageIdAndType(rulePackage.getId(), dayType.getId());
//                ruleListHashtable.put(s, rules);
//            }
//
//            Initializer.dateRuleExceptionHashTables.put(rulePackage.getId(), ruleExcptionListHashtable);
//            Initializer.dateRuleHashTables.put(rulePackage.getId(), ruleListHashtable);
//        }
    }
}
