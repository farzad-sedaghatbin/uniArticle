package ir.university.toosi.parking.service;

import ir.university.toosi.parking.dao.CarDAOImpl;
import ir.university.toosi.parking.entity.Car;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.ReportEntity;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.model.service.SystemConfigurationServiceImpl;
import ir.university.toosi.tms.model.service.personnel.CardServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.zone.PDPServiceImpl;
import ir.university.toosi.tms.model.service.zone.VirdiServiceImpl;
import ir.university.toosi.tms.util.EventLogManager;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
public class CarServiceImpl<T extends Car> {

    @EJB
    private CarDAOImpl CarDAO;

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
            return CarDAO.maximumId("Car.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }


    public T findById(long id) {
        try {
            return (T) CarDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }



    public long time2long(String time) {
        String[] d = time.split(":");
        String s = (d[0].length() == 2 ? d[0] : '0' + d[0]) + (d[1].length() == 2 ? d[1] : '0' + d[1]) + (d[2].length() == 2 ? d[2] : '0' + d[2]);
        return Long.valueOf(s);
    }


    public List<T> findByNumber(String number) {
        try {
            List<Car> Cars = CarDAO.findByNumber(number);
            return (List<T>) Cars;
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllCar() {
        try {
            return (List<T>) CarDAO.findAll(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getParkingByQuery(String query) {
        try {
            return (List<T>) CarDAO.getParkingByQuery(query);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ReportEntity> queryView(String query) {
        try {
            return (List<ReportEntity>) CarDAO.queryView(query);
        } catch (Exception e) {
            return null;
        }
    }

    public Long queryCountView(String query) {
        try {
            return (Long) CarDAO.queryCountView(query);
        } catch (Exception e) {
            return 0l;
        }
    }


    public String deleteCar(T entity) {
        try {
//            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Car.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            CarDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createCar(T entity) {
        try {
            entity = (T) CarDAO.create(entity);
//            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Car.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return entity;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editCar(T entity) {
        try {
            CarDAO.update(entity);
//            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Car.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public CarDAOImpl getCarDAO() {
        return CarDAO;
    }

}
