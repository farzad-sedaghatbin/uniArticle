package ir.university.toosi.tms.model.service.zone;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.zone.ZoneDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.Permission;
import ir.university.toosi.tms.model.entity.PermissionType;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.Zone;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.model.service.PermissionServiceImpl;
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

public class ZoneServiceImpl<T extends Zone> {

    @EJB
    private ZoneDAOImpl ZoneDAO;
    @EJB
    private GatewayServiceImpl gatewayService;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private PermissionServiceImpl permissionService;


    public T findById(long id) {
        try {
            return (T) ZoneDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByRulePackageId(Long id) {
        try {
            return (List<T>) ZoneDAO.findByRulePackageId(id);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllZone() {
        try {
            return (List<T>) ZoneDAO.findAll("Zone.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteZone(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Zone.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());

            List<Gateway> gateways = gatewayService.findByZone(entity);
            if (gateways != null && gateways.size() != 0)
                return "there.are.gateways.for.this.zone";
            ZoneDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createZone(T entity) {
        try {
            T t = (T) ZoneDAO.create(entity);
            Permission permission = new Permission(String.valueOf(t.getId()), t.getName(), PermissionType.ZONE);
            permissionService.createPermission(permission);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), Zone.class.getSimpleName(), EventLogType.ADD, t.getEffectorUser());

            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editZone(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Zone.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());

            Zone oldZone = findById(entity.getId());
            if (entity.getRulePackage() != null) {
                if ((oldZone.getRulePackage() == null) || oldZone.getRulePackage().getId() != entity.getRulePackage().getId()) {
                    List<Gateway> gateways = gatewayService.findByZoneAndRulePackage(String.valueOf(entity.getId()), String.valueOf(entity.getRulePackage().getId()));
                    for (Gateway gateway : gateways) {
                        gateway=gatewayService.findById(gateway.getId());
                        gateway.setRulePackage(entity.getRulePackage());
                        gatewayService.editGateway(gateway);
                    }
                }
            }
            Zone newZone = new Zone();
            newZone.setDescription(oldZone.getDescription());
            newZone.setEnabled(oldZone.isEnabled());
            newZone.setName(oldZone.getName());
            newZone.setRulePackage(oldZone.getRulePackage());
            newZone.setStatus("o," + entity.getEffectorUser());
            ZoneDAO.createOld(newZone);

            ZoneDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}