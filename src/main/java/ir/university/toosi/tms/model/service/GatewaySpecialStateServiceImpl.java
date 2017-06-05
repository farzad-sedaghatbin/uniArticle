package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.GatewaySpecialStateDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.GatewayPerson;
import ir.university.toosi.tms.model.entity.GatewaySpecialState;
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

public class GatewaySpecialStateServiceImpl<T extends GatewaySpecialState> {

    @EJB
    private GatewaySpecialStateDAOImpl gatewaySpecialStateDAO;
    @EJB
    private GatewaySpecialStateScheduler gatewaySpecialStateScheduler;
    @EJB
    private EventLogServiceImpl eventLogService;

    public List<T> findByGatewayId(long id) {
        try {
            return (List<T>) gatewaySpecialStateDAO.findByGatewayId(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllGatewaySpecialState() {
        try {
            return (List<T>) gatewaySpecialStateDAO.findAll("GatewaySpecialState.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteGatewaySpecialState(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), GatewayPerson.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            gatewaySpecialStateDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }

    public T createGatewaySpecialState(T entity) {
        try {

            T t = (T) gatewaySpecialStateDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), GatewayPerson.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            gatewaySpecialStateScheduler.stopService();
            gatewaySpecialStateScheduler.startService();
            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editGatewaySpecialState(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), GatewayPerson.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            gatewaySpecialStateDAO.update(entity);
            gatewaySpecialStateScheduler.stopService();
            gatewaySpecialStateScheduler.startService();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public T findById(long id){
        return (T) gatewaySpecialStateDAO.findById(id);
    }
}
