package ir.university.toosi.tms.model.service.zone;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.ConsoleClientSocket;
import ir.university.toosi.tms.model.dao.zone.GatewayDAOImpl;
import ir.university.toosi.tms.model.entity.*;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.PDP;
import ir.university.toosi.tms.model.entity.zone.PreRequestGateway;
import ir.university.toosi.tms.model.entity.zone.Zone;
import ir.university.toosi.tms.model.service.*;
import ir.university.toosi.tms.readerwrapper.ReaderWrapper;
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
public class GatewayServiceImpl<T extends Gateway> {

    @EJB
    private GatewayDAOImpl GatewayDAO;
    @EJB
    private PDPServiceImpl pdpService;
    @EJB
    private VirdiServiceImpl virdiService;
    @EJB
    private PermissionServiceImpl permissionService;
    @EJB
    private OperationServiceImpl operationService;
    @EJB
    private GatewaySpecialStateServiceImpl gatewaySpecialStateService;
    @EJB
    private PreRequestGatewayServiceImpl preRequestGatewayService;
    @EJB
    private GatewayPersonServiceImpl gatewayPersonService;
    @EJB
    private EventLogServiceImpl eventLogService;


    public T findById(long id) {
        try {
            Gateway gateway = GatewayDAO.findById(id);
            gateway.setPreRequestGateways(new ArrayList<Long>());
            gateway.getPreRequestGateways().addAll(preRequestGatewayService.findByGateway(gateway));
            return (T) gateway;
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByZone(Zone zone) {
        try {
            List<Gateway> gateways = new ArrayList<>();
            for (Gateway gateway : GatewayDAO.findByZone(zone)) {
                gateway.setPreRequestGateways(new ArrayList<Long>());
                gateway.getPreRequestGateways().addAll(preRequestGatewayService.findByGateway(gateway));
                gateways.add(gateway);
            }
            return (List<T>) gateways;
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByCamera(Long cameraId) {
        try {
            List<Gateway> gateways = new ArrayList<>();
            for (Gateway gateway : GatewayDAO.findByCamera(cameraId)) {
                gateway.setPreRequestGateways(new ArrayList<Long>());
                gateway.getPreRequestGateways().addAll(preRequestGatewayService.findByGateway(gateway));
                gateways.add(gateway);
            }
            return (List<T>) gateways;
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllGateway() {
        try {
            List<Gateway> gateways = new ArrayList<>();
            for (Gateway gateway : GatewayDAO.findAll("Gateway.list", true)) {
                gateway.setPreRequestGateways(new ArrayList<Long>());
                gateway.getPreRequestGateways().addAll(preRequestGatewayService.findByGateway(gateway));
                gateways.add(gateway);
            }
            return (List<T>) gateways;
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllEnableGateway() {
        try {
            List<Gateway> gateways = new ArrayList<>();
            for (Gateway gateway : GatewayDAO.findAll("Gateway.enableList", true)) {
                gateway.setPreRequestGateways(new ArrayList<Long>());
                gateway.getPreRequestGateways().addAll(preRequestGatewayService.findByGateway(gateway));
                gateways.add(gateway);
            }
            return (List<T>) gateways;
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteGateway(T entity) {
        try {
            List<PDP> pdp = pdpService.findByGatewayId(String.valueOf(entity.getId()));
            if (pdp != null && pdp.size() != 0)
                return new ObjectMapper().writeValueAsString("REL_GATEWAY_PDP");

            List<GatewaySpecialState> gatewaySpecialStates = gatewaySpecialStateService.findByGatewayId(entity.getId());
            for (GatewaySpecialState gatewaySpecialState : gatewaySpecialStates) {
                gatewaySpecialStateService.deleteGatewaySpecialState(gatewaySpecialState);
            }
            List<GatewayPerson> gatewayPersons = gatewayPersonService.findByGatewayId(entity.getId());
            for (GatewayPerson gatewayPerson : gatewayPersons) {
                gatewayPersonService.deleteGatewayPerson(gatewayPerson);
            }
            entity.getCameras().clear();
            editGateway(entity);
            GatewayDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createGateway(T entity) {
        try {
            Operation operation = new Operation();
            operation.setName(entity.getName());
            operation.setDescription("Gateway" + operationService.getAllOperation().size());
            operationService.createOperation(operation);
            T t = (T) GatewayDAO.create(entity);
            Permission permission = new Permission(String.valueOf(t.getId()), t.getName(), PermissionType.GATEWAY);
            permissionService.createPermission(permission);
            for (Long preGatewayId : entity.getPreRequestGateways()) {
                PreRequestGateway preRequestGateway = new PreRequestGateway();
                preRequestGateway.setGateway(t);
                preRequestGateway.setPreGateway(GatewayDAO.findById(preGatewayId));
                preRequestGatewayService.createGateway(preRequestGateway);
            }
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), GatewayPerson.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());

            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editGateway(T entity) {
        try {
            Gateway old = findById(entity.getId());
            Gateway newGateway = new Gateway();
            newGateway.setRulePackage(old.getRulePackage());
            newGateway.setDescription(old.getDescription());
            newGateway.setEnabled(old.isEnabled());
            newGateway.setName(old.getName());
            newGateway.setZone(old.getZone());
            newGateway.setStatus("o," + entity.getEffectorUser());
            newGateway.setCameras(old.getCameras());
            GatewayDAO.createOld(newGateway);

            List<Long> oldPreRequestGatewayIds = preRequestGatewayService.findByGateway(old);
            if (oldPreRequestGatewayIds != null)
                for (Long oldPreRequestGatewayId : oldPreRequestGatewayIds) {
                    PreRequestGateway oldPreRequestGateway = preRequestGatewayService.findByGatewayAndPreGateway(old.getId(), oldPreRequestGatewayId);
                    preRequestGatewayService.deleteGateway(oldPreRequestGateway);
                }
            for (Long preGatewayId : entity.getPreRequestGateways()) {
                PreRequestGateway preRequestGateway = new PreRequestGateway();
                preRequestGateway.setGateway(entity);
                preRequestGateway.setPreGateway(GatewayDAO.findById(preGatewayId));
                preRequestGatewayService.createGateway(preRequestGateway);
            }
            GatewayDAO.update(entity);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<T> findByZoneAndRulePackage(String zoneId, String rulePackageId) {
        try {
            List<Gateway> gateways = new ArrayList<>();
            for (Gateway gateway : GatewayDAO.findByZoneAndRulePackage(zoneId, rulePackageId)) {
                gateway.setPreRequestGateways(new ArrayList<Long>());
                gateway.getPreRequestGateways().addAll(preRequestGatewayService.findByGateway(gateway));
                gateways.add(gateway);
            }
            return (List<T>) gateways;
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByRulePackage(Long rulePackageId) {
        try {
            List<Gateway> gateways = new ArrayList<>();
            for (Gateway gateway : GatewayDAO.findByRulePackage(rulePackageId)) {
                gateway.setPreRequestGateways(new ArrayList<Long>());
                gateway.getPreRequestGateways().addAll(preRequestGatewayService.findByGateway(gateway));
                gateways.add(gateway);
            }
            return (List<T>) gateways;
        } catch (Exception e) {
            return null;
        }
    }

    public void forceOpen(String ip) {

        byte[] message = new byte[]{0x40, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x0A};
        ConsoleClientSocket.sendMessage(message, ip);
    }

    public void forceOpenVirdi(String ip) {

        ReaderWrapper readerWrapper = new ReaderWrapper();
        readerWrapper.ForceOpenDoor((int) virdiService.findByIp(ip).getId());
    }

    public void lockDoor(String ip) {

        ReaderWrapper readerWrapper = new ReaderWrapper();
        readerWrapper.LockDoor((int) virdiService.findByIp(ip).getId());
    }

    public void lockVirdi(String ip) {

        ReaderWrapper readerWrapper = new ReaderWrapper();
        readerWrapper.LockTerminal((int) virdiService.findByIp(ip).getId());
    }

    public void unlockDoor(String ip) {

        ReaderWrapper readerWrapper = new ReaderWrapper();
        readerWrapper.UnLockDoor((int) virdiService.findByIp(ip).getId());
    }

    public void unlockVirdi(String ip) {

        ReaderWrapper readerWrapper = new ReaderWrapper();
        readerWrapper.UnLockTerminal((int) virdiService.findByIp(ip).getId());
    }

    public List<Gateway> getAllGatewayForZone(List<Gateway> gatewayList) {
        return GatewayDAO.getAllGatewayForZone(gatewayList);
    }

    public List<Gateway> getAllGatewayExceptThese(List<Gateway> gateways) {
        return GatewayDAO.getAllGatewayExceptThese(gateways);
    }
}