package ir.university.toosi.tms.model.service.zone;

import ir.university.toosi.tms.model.dao.zone.PreRequestGatewayDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.PreRequestGateway;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.util.EventLogManager;

import javax.ejb.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PreRequestGatewayServiceImpl<T extends PreRequestGateway> {

    @EJB
    private PreRequestGatewayDAOImpl preRequestGatewayDAO;
    @EJB
    private EventLogServiceImpl eventLogService;


    public T findById(long id) {
        try {
            return (T) preRequestGatewayDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Long> findByGateway(Gateway gateway) {
        try {
            List<Long> gateIds = new ArrayList<>();
            List<PreRequestGateway> gateways = preRequestGatewayDAO.findByGateway(gateway);
            for (PreRequestGateway preRequestGateway : gateways) {
                gateIds.add(preRequestGateway.getId());
            }
            return gateIds;
        } catch (Exception e) {
            return null;
        }
    }


    public PreRequestGateway findByGatewayAndPreGateway(Long gatewayId, Long preGatewayId) {
        try {
            PreRequestGateway gateways = preRequestGatewayDAO.findByGatewayAndPreGateway(gatewayId, preGatewayId);
            return gateways;
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllPreGateway() {
        try {
            return (List<T>) preRequestGatewayDAO.findAll("PreRequestGateway.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public T createGateway(T entity) {
        try {
            T t = (T) preRequestGatewayDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), PreRequestGateway.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());

            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editGateway(T entity) {
        try {
            T t = (T) preRequestGatewayDAO.update(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), PreRequestGateway.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteGateway(T entity) {
        try {
            preRequestGatewayDAO.delete(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), PreRequestGateway.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}