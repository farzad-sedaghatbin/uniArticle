package ir.university.toosi.parking.service;

import ir.university.toosi.parking.dao.ParkingLogDAOImpl;
import ir.university.toosi.parking.entity.ParkingLog;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.ReportEntity;
import ir.university.toosi.tms.model.entity.SystemParameterType;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.zone.PDP;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.model.service.SystemConfigurationServiceImpl;
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
public class ParkingLogServiceImpl<T extends ParkingLog> {

    @EJB
    private ParkingLogDAOImpl ParkingLogDAO;

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
            return ParkingLogDAO.maximumId("ParkingLog.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }


    public T findById(long id) {
        try {
            return (T) ParkingLogDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }


    public List<ParkingLog> findInDuration(long startTime, long endTime, String date) {
        try {
            List<ParkingLog> returnParkingLogs = new ArrayList<>();
            List<ParkingLog> ParkingLogs = ParkingLogDAO.findInDuration(date);
            for (ParkingLog ParkingLog : ParkingLogs) {
                if (time2long(ParkingLog.getTraffic_time()) > startTime && time2long(ParkingLog.getTraffic_time()) < endTime)
                    returnParkingLogs.add(ParkingLog);
            }
            return returnParkingLogs;
        } catch (Exception e) {
            return null;
        }
    }

    public long time2long(String time) {
        String[] d = time.split(":");
        String s = (d[0].length() == 2 ? d[0] : '0' + d[0]) + (d[1].length() == 2 ? d[1] : '0' + d[1]) + (d[2].length() == 2 ? d[2] : '0' + d[2]);
        return Long.valueOf(s);
    }


    public List<T> findParkingInDuration(String startDate, String endDate) {
        try {
            List<ParkingLog> ParkingLogs = ParkingLogDAO.findParkingInDuration(startDate, endDate);
            return (List<T>) ParkingLogs;
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findParkingInDurationTime(String startTime, String endTime, String pelak) {
        try {
            List<ParkingLog> ParkingLogs = ParkingLogDAO.findParkingInDurationTime(startTime, endTime,pelak);
            return (List<T>) ParkingLogs;
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllParkingLog() {
        try {
            return (List<T>) ParkingLogDAO.findAll(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getParkingByQuery(String query) {
        try {
            return (List<T>) ParkingLogDAO.getParkingByQuery(query);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ReportEntity> queryView(String query) {
        try {
            return (List<ReportEntity>) ParkingLogDAO.queryView(query);
        } catch (Exception e) {
            return null;
        }
    }

    public Long queryCountView(String query) {
        try {
            return (Long) ParkingLogDAO.queryCountView(query);
        } catch (Exception e) {
            return 0l;
        }
    }


    public String deleteParkingLog(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), ParkingLog.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            ParkingLogDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createParkingLog(T entity) {
        try {
            entity = (T) ParkingLogDAO.create(entity);
//            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), ParkingLog.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return entity;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editParkingLog(T entity) {
        try {
            ParkingLogDAO.update(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), ParkingLog.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public ParkingLogDAOImpl getParkingLogDAO() {
        return ParkingLogDAO;
    }

}
