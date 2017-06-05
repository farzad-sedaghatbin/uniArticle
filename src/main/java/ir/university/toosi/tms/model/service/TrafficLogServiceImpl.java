package ir.university.toosi.tms.model.service;

import ir.university.toosi.tms.model.dao.TrafficLogDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.ReportEntity;
import ir.university.toosi.tms.model.entity.SystemParameterType;
import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.zone.PDP;
import ir.university.toosi.tms.model.service.personnel.CardServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.zone.PDPServiceImpl;
import ir.university.toosi.tms.model.service.zone.VirdiServiceImpl;
import ir.university.toosi.tms.util.EventLogManager;
import ir.university.toosi.tms.util.LangUtil;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
public class TrafficLogServiceImpl<T extends TrafficLog> {

    @EJB
    private TrafficLogDAOImpl TrafficLogDAO;

    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private PDPServiceImpl pdpService;
    @EJB
    private VirdiServiceImpl virdiService;
    @EJB
    private PersonServiceImpl personService;
    @EJB
    private CardServiceImpl cardService;
    @EJB
    private SystemConfigurationServiceImpl systemConfigurationService;

    public void test(String param) {
        System.out.println("777>>>>" + param);
    }


    public long getMaximumId() {
        try {
            return TrafficLogDAO.maximumId("TrafficLog.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }


    public T findById(long id) {
        try {
            return (T) TrafficLogDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer findByPersonIdInDuration(Long personId, String startTime, String endTime, Boolean exit, String date) {
        try {
            Integer count = 0;
            List<TrafficLog> trafficLogs = TrafficLogDAO.findByPersonIdInDuration(personId, exit, date);
            for (TrafficLog trafficLog : trafficLogs) {
                if (time2long(trafficLog.getTraffic_time()) > Long.valueOf(startTime) && time2long(trafficLog.getTraffic_time()) < Long.valueOf(endTime))
                    count++;
            }
            return count;
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrafficLog> findInDuration(long startTime, long endTime, String date) {
        try {
            List<TrafficLog> returnTrafficLogs = new ArrayList<>();
            List<TrafficLog> trafficLogs = TrafficLogDAO.findInDuration(date);
            for (TrafficLog trafficLog : trafficLogs) {
                if (time2long(trafficLog.getTraffic_time()) > startTime && time2long(trafficLog.getTraffic_time()) < endTime)
                    returnTrafficLogs.add(trafficLog);
            }
            return returnTrafficLogs;
        } catch (Exception e) {
            return null;
        }
    }

    public long time2long(String time) {
        String[] d = time.split(":");
        String s = (d[0].length() == 2 ? d[0] : '0' + d[0]) + (d[1].length() == 2 ? d[1] : '0' + d[1]) + (d[2].length() == 2 ? d[2] : '0' + d[2]);
        return Long.valueOf(s);
    }

    public List<T> findByPersonLocationInDuration(Long personId, String startTime, String endTime, String date) {
        try {
            List<TrafficLog> validTrafficLogs = new ArrayList<>();
            List<TrafficLog> trafficLogs = TrafficLogDAO.findByPersonLocationInDuration(personId, date);
            for (TrafficLog trafficLog : trafficLogs) {
                if (time2long(trafficLog.getTraffic_time()) > time2long(startTime) && time2long(trafficLog.getTraffic_time()) < time2long(endTime))
                    validTrafficLogs.add(trafficLog);
            }
            return (List<T>) validTrafficLogs;
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findTrafficInDuration(String startDate, String endDate) {
        try {
            List<TrafficLog> trafficLogs = TrafficLogDAO.findTrafficInDuration(startDate, endDate);
            return (List<T>) trafficLogs;
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByPersonAndGate(Long personId, Long gatewayId, String date) {
        try {
            return (List<T>) TrafficLogDAO.findByPersonAndGate(personId, gatewayId, date);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer findByPersonAndGateExOrEn(Long personId, Long gatewayId, String date, boolean ex) {
        try {
            return (Integer) TrafficLogDAO.findByPersonAndGateExOrEn(personId, gatewayId, date, ex);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByGate(Long gatewayId, String date) {
        try {
            return (List<T>) TrafficLogDAO.findByGate(gatewayId, date);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByPDP(Long pdpID, String date) {
        try {

            return (List<T>) TrafficLogDAO.findByPDP(pdpID, date, systemConfigurationService.findByParameter(SystemParameterType.SENTRY_COUNT));
        } catch (Exception e) {
            return null;
        }
    }
    public List<T> findByVirdi(Long virdiID, String date) {
        try {

            return (List<T>) TrafficLogDAO.findByVirdi(virdiID, date, systemConfigurationService.findByParameter(SystemParameterType.SENTRY_COUNT));
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByPerson(Long personId, String date) {
        try {
            return (List<T>) TrafficLogDAO.findByPerson(personId, date);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByPersonInDuration(Long personId, String date,String toDate) {
        try {
            return (List<T>) TrafficLogDAO.findByPersonInDuration(personId, date, toDate);
        } catch (Exception e) {
            return null;
        }
    }
    public List<T> findByGateInDuration(Long gateId, String date,String toDate) {
        try {
            return (List<T>) TrafficLogDAO.findByGateInDuration(gateId, date, toDate);
        } catch (Exception e) {
            return null;
        }
    }   public List<T> findByVirdiInDuration(Long gateId, String date,String toDate) {
        try {
            return (List<T>) TrafficLogDAO.findByVirdiInDuration(gateId, date, toDate);
        } catch (Exception e) {
            return null;
        }
    }  public List<T> findByOrganInDuration(Long organId, String date,String toDate) {
        try {
            return (List<T>) TrafficLogDAO.findByOrganInDuration(organId, date, toDate);
        } catch (Exception e) {
            return null;
        }
    }

    public T findLastByPerson(Long personId, String date) {
        try {
            return (T) TrafficLogDAO.findLastByPerson(personId, date);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllTrafficLog() {
        try {
            return (List<T>) TrafficLogDAO.findAll(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getTrafficByQuery(String query) {
        try {
            return (List<T>) TrafficLogDAO.getTrafficByQuery(query);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ReportEntity> queryView(String query) {
        try {
            return (List<ReportEntity>) TrafficLogDAO.queryView(query);
        } catch (Exception e) {
            return null;
        }
    }

    public Long queryCountView(String query) {
        try {
            return (Long) TrafficLogDAO.queryCountView(query);
        } catch (Exception e) {
            return 0l;
        }
    }


    public String deleteTrafficLog(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), TrafficLog.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            TrafficLogDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createTrafficLog(T entity) {
        try {
            entity = (T) TrafficLogDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), TrafficLog.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return entity;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editTrafficLog(T entity) {
        try {
            TrafficLogDAO.update(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), TrafficLog.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public TrafficLog offline(String pdpIP, String personId, String cmd, String date) {
        PDP pdp = pdpService.findByIp(pdpIP);
        Person person = personService.getPersonByPersonOtherId(personId);
        if (person == null || pdp == null)
            return null;
        Card card = (Card) cardService.findByPersonId((person.getId())).get(0);
        String photos = "/" + pdp.getName() + card.getName() + new Date().getTime();

        TrafficLog trafficLog = new TrafficLog();


        if (cmd.equalsIgnoreCase("101")) {
            trafficLog.setStatus("fail");
            trafficLog.setExit(false);
            trafficLog.setValid(false);
        } else if (cmd.equalsIgnoreCase("102")) {
            trafficLog.setStatus("fail");
            trafficLog.setValid(false);
            trafficLog.setExit(true);
        } else if (cmd.equalsIgnoreCase("104")) {
            trafficLog.setStatus("ok");
            trafficLog.setValid(true);
            trafficLog.setExit(false);
        } else if (cmd.equalsIgnoreCase("105")) {
            trafficLog.setStatus("ok");
            trafficLog.setValid(true);
            trafficLog.setExit(true);
        }
        System.out.println("---------- 1");
        trafficLog.setPictures(null);
        trafficLog.setCard(null);
        trafficLog.setFinger(false);
        trafficLog.setOffline(true);
        trafficLog.setGateway(pdp.getGateway());
        trafficLog.setPdp(pdp);
        trafficLog.setZone(pdp.getGateway().getZone());
        trafficLog.setPerson(card.getPerson());
        trafficLog.setOrgan(card.getPerson().getOrganRef());
        trafficLog.setTraffic_date(LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(CalendarUtil.getDate(date.substring(6, 8) + "/" + date.substring(4, 6) + "/" + date.substring(0, 4)), new Locale("fa"), "YYYYMMdd")));
        trafficLog.setTraffic_time(date.substring(8, 10) +   ":" + date.substring(10, 12) + ":" + date.substring(12));

        return trafficLog;
    }

    public TrafficLogDAOImpl getTrafficLogDAO() {
        return TrafficLogDAO;
    }

    public List<Object[]> searchForChart(String fromTime,String toTime,String fromDate,String toDate,boolean valid) {
        return TrafficLogDAO.searchForChart(fromTime,toTime,fromDate,toDate,valid);
    }
}
