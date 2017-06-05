package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.GatewayPersonDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.GatewayPerson;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
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

public class GatewayPersonServiceImpl<T extends GatewayPerson> {

    @EJB
    private GatewayPersonDAOImpl gatewayPersonDAO;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private PersonServiceImpl personService;
    @EJB
    private GatewayServiceImpl gatewayService;

    public List<T> findByPersonId(long id) {
        try {
            return (List<T>) gatewayPersonDAO.findByPersonId(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByGatewayId(long id) {
        try {
            return (List<T>) gatewayPersonDAO.findByGatewayId(id);
        } catch (Exception e) {
            return null;
        }
    }
    public List<Person> findPersonByGatewayId(long id) {
        try {
            return (List<Person>) gatewayPersonDAO.findPersonByGatewayId(id);
        } catch (Exception e) {
            return null;
        }
    }

    public T findByPersonIdAndGatewayId(long personId, long gatewayId) {
        try {
            return (T) gatewayPersonDAO.findByPersonIdAndGatewayId(personId, gatewayId);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteGatewayPerson(long personID,long gatewayID) {
        try {
//            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), GatewayPerson.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            gatewayPersonDAO.delete(findByPersonIdAndGatewayId(personID, gatewayID));
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }

    public String deleteGatewayPerson(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), GatewayPerson.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            gatewayPersonDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createGatewayPerson(T entity) {
        try {
            entity.setPerson(personService.findById(entity.getPerson().getId()));
            entity.setGateway(gatewayService.findById(entity.getGateway().getId()));
            T t = (T) gatewayPersonDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), GatewayPerson.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editGatewayPerson(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), GatewayPerson.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            gatewayPersonDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
