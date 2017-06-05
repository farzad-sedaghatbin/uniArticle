package ir.university.toosi.tms.model.service;

import ir.university.toosi.tms.model.dao.EventLogDAOImpl;
import ir.university.toosi.tms.model.entity.GatewayPerson;
import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.rule.Rule;
import ir.university.toosi.tms.model.entity.rule.RuleException;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.service.rule.RuleExceptionServiceImpl;
import ir.university.toosi.tms.model.service.zone.PreRequestGatewayServiceImpl;
import ir.university.toosi.tms.util.Initializer;
import ir.university.toosi.tms.util.LangUtil;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
public class AuthenticateServiceImpl {

    @EJB
    private EventLogDAOImpl eventLogDAO;
    @EJB
    private RuleExceptionServiceImpl ruleExceptionService;
    @EJB
    private TrafficLogServiceImpl trafficLogService;
    @EJB
    private GatewayPersonServiceImpl gatewayPersonService;
    @EJB
    private PreRequestGatewayServiceImpl preRequestGatewayService;

    public boolean authenticate(Gateway gateway, Object[] person, Boolean exit, Card card, boolean finger) throws IOException {
        return !(gateway.getRulePackage() == null || person[1] == null) && checkPersonGateway((Long) person[0], gateway) && (finger || !cardCheck(card)) && isAllowed(gateway.getRulePackage().getId()) && isAllowed(((long) person[1]), exit, ((long) person[0])) && isAllowedPersonForGate(gateway.getRulePackage(), exit, ((long) person[0]), gateway);
    }


    private boolean checkPersonGateway(long person, Gateway gateway) {
        GatewayPerson gatewayPersons = gatewayPersonService.findByPersonIdAndGatewayId(person, gateway.getId());
        if (gatewayPersons != null)
            return true;
        System.out.println("checkPersonGateway9");
        return false;
    }

    private boolean cardCheck(Card card) {

        if (!card.getCardStatus().getCode().equalsIgnoreCase("ACTIVE")) {
            return false;
        }
        if (card.getStartDate() == null || card.getStartDate().compareTo(LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd"))) > 0) {
            return false;
        }
        if (card.getExpirationDate() == null || card.getExpirationDate().compareTo(LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd"))) < 0) {
            return false;
        }
        return true;
    }


    private boolean isAllowed(Long rulePackageId) {

        String todayIndex = CalendarUtil.getPersianDateIndexInYear(new Locale("fa"));
        if (Initializer.dateRuleExceptionHashTables.get(rulePackageId) != null) {
            if (Initializer.dateRuleExceptionHashTables.get(rulePackageId).containsKey(todayIndex)) {
                List<RuleException> ruleExceptions = Initializer.dateRuleExceptionHashTables.get(rulePackageId).get(todayIndex);
                String nowTime = LangUtil.getEnglishNumber(CalendarUtil.getTime(new Date(), new Locale("fa")));
                nowTime = nowTime.replaceAll(":", "");
                for (RuleException ruleException : ruleExceptions) {
                    String startTime = ruleException.getStartTime().replaceAll(":", "");
                    String endTime = ruleException.getEndTime().replaceAll(":", "");
                    if (startTime.compareTo(nowTime) < 0 && endTime.compareTo(nowTime) > 0)
                        return true;
                }
                System.out.println("isAllowed6");
                return false;
            }
        }

        if (Initializer.dateRuleHashTables.get(rulePackageId) == null)

            return false;
        List<Rule> rules = Initializer.dateRuleHashTables.get(rulePackageId).get(todayIndex);
        if (rules == null || rules.size() == 0)
            return false;
        String nowTime = LangUtil.getEnglishNumber(CalendarUtil.getTime(new Date(), new Locale("fa")));
        nowTime = nowTime.replaceAll(":", "");
        for (Rule rule : rules) {
            String startTime = rule.getStartTime().replaceAll(":", "");
            String endTime = rule.getEndTime().replaceAll(":", "");
            if (startTime.compareTo(nowTime) < 0 && endTime.compareTo(nowTime) > 0) {
                if (rule.isDeny()) {
                    System.out.println("isAllowed7");
                    return false;
                }
                return true;
            }
        }
        System.out.println("isAllowed8");
        return false;
    }

    private boolean isAllowed(Long rulePackageId, Boolean exit, Long personId) {

        String todayIndex = CalendarUtil.getPersianDateIndexInYear(new Locale("fa"));
        if (Initializer.dateRuleExceptionHashTables.get(rulePackageId) != null) {
            if (Initializer.dateRuleExceptionHashTables.get(rulePackageId).containsKey(todayIndex)) {
                List<RuleException> ruleExceptions = Initializer.dateRuleExceptionHashTables.get(rulePackageId).get(todayIndex);
                String nowTime = LangUtil.getEnglishNumber(CalendarUtil.getTime(new Date(), new Locale("fa")));
                nowTime = nowTime.replaceAll(":", "");
                for (RuleException ruleException : ruleExceptions) {
                    String startTime = ruleException.getStartTime().replaceAll(":", "");
                    String endTime = ruleException.getEndTime().replaceAll(":", "");
                    if (startTime.compareTo(nowTime) < 0 && endTime.compareTo(nowTime) > 0) {

                        Integer count = trafficLogService.findByPersonIdInDuration(personId, startTime, endTime, exit, CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
                        count++;
                        if(exit){
                            continue;
                        }else if(!exit ){
                            continue;
                        }
                        if (exit   && Integer.parseInt(ruleException.getExitCount())!=-1 && count.compareTo(Integer.valueOf(ruleException.getExitCount())) > 0) {
                            System.out.println("isAllowed1");
                            return false;
                        } else if (!exit && Integer.parseInt(ruleException.getEntranceCount())!=-1 && count.compareTo(Integer.valueOf(ruleException.getEntranceCount())) > 0) {
                            System.out.println("isAllowed2");
                            return false;
                        }
                        return true;
                    }
                }

                return false;
            }
        }

        if (Initializer.dateRuleHashTables.get(rulePackageId) == null)
            return false;
        List<Rule> rules = Initializer.dateRuleHashTables.get(rulePackageId).get(todayIndex);
        if (rules == null || rules.size() == 0)
            return false;
        String nowTime = LangUtil.getEnglishNumber(CalendarUtil.getTime(new Date(), new Locale("fa")));
        nowTime = nowTime.replaceAll(":", "");
        for (Rule rule : rules) {
            String startTime = rule.getStartTime().replaceAll(":", "");
            String endTime = rule.getEndTime().replaceAll(":", "");
            if (startTime.compareTo(nowTime) < 0 && endTime.compareTo(nowTime) > 0) {
                if (rule.isDeny()) {
                    System.out.println("isAllowed3");
                    return false;
                }
                Integer count = trafficLogService.findByPersonIdInDuration(personId, startTime, endTime, exit, CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
                count++;

                if (exit && (Integer.parseInt(rule.getExitCount())!=-1)&& count.compareTo(Integer.valueOf(rule.getExitCount())) > 0) {
                    System.out.println("isAllowed4");
                    return false;
                } else if (!exit && Integer.parseInt(rule.getEntranceCount())!=-1 && count.compareTo(Integer.valueOf(rule.getEntranceCount())) > 0) {
                    System.out.println("isAllowed4");
                    return false;
                }
                return true;
            }
        }
        System.out.println("isAllowed5");
        return false;
    }

    private boolean isAllowedPersonForGate(RulePackage rulePackage, Boolean exit, Long personId, Gateway gateway) {

        TrafficLog lastTrafficLog = trafficLogService.findLastByPerson(personId, CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
        List<Long> preRequestGatewayIds = preRequestGatewayService.findByGateway(gateway);
        if (preRequestGatewayIds.size() != 0) {
            if (lastTrafficLog == null) {
                System.out.println("isAllowedPersonForGate1");
                return false;
            }
            boolean flag = false;
            for (Long preGateway : preRequestGatewayIds) {

                if (lastTrafficLog.getGateway().getId() == preRequestGatewayService.findById(preGateway).getPreGateway().getId()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                System.out.println("isAllowedPersonForGate2");
                return false;
            }
        }

        if (!rulePackage.isAniPassBack())
            return true;

        if ((lastTrafficLog == null) || (exit && !lastTrafficLog.isExit()) || (!exit && lastTrafficLog.isExit()))
            return true;
        System.out.println("isAllowedPersonForGate3");
        return false;
    }

}
