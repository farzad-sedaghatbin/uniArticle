package ir.university.toosi.tms.model.service.zone;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.zone.CameraDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.zone.Camera;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.PDP;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.util.EventLogManager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class CameraServiceImpl<T extends Camera> {

    @EJB
    private CameraDAOImpl cameraDAO;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private GatewayServiceImpl gatewayService;
    @EJB
    private PDPServiceImpl pdpService;

    public boolean exist(String ip,long id) {
        try {
            return cameraDAO.exist(ip,id);
        } catch (Exception e) {
            return false;
        }
    }    public boolean existNotId(String ip) {
        try {
            return cameraDAO.existNotId(ip);
        } catch (Exception e) {
            return false;
        }
    }

    public T findById(long id) {
        try {
            return (T) cameraDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllCamera() {
        try {
            return (List<T>) cameraDAO.findAll("Camera.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteCamera(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Camera.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());

            List<Gateway> gateways = gatewayService.findByCamera(entity.getId());
            for (Gateway gateway : gateways) {
                List<Camera> cameras = new ArrayList<>();
                for (Camera camera : gateway.getCameras()) {
                    if (camera.getId() != entity.getId())
                        cameras.add(camera);
                }
                gateway.getCameras().clear();
                gatewayService.editGateway(gateway);
                gateway.getCameras().addAll(cameras);
                gatewayService.editGateway(gateway);
            }
            List<PDP> pdps = pdpService.findByCameraId(entity.getId());
            for (PDP pdp : pdps) {
                if (pdp.getCamera().getId() == entity.getId()) {
                    pdp.setCamera(null);
                    pdpService.editPDP(pdp);
                }
            }
            cameraDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createCamera(T entity) {
        try {
            T t = (T) cameraDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), Camera.class.getSimpleName(), EventLogType.ADD, t.getEffectorUser());

            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editCamera(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Camera.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());

            Camera oldCamera = findById(entity.getId());
            Camera newCamera = new Camera();
            newCamera.setStatus("o," + entity.getEffectorUser());
            newCamera.setName(oldCamera.getName());
            newCamera.setDescription(oldCamera.getDescription());
            newCamera.setEnabled(oldCamera.isEnabled());
            newCamera.setFrames(oldCamera.getFrames());
            newCamera.setIp(oldCamera.getIp());
            newCamera.setNameText(oldCamera.getNameText());
            cameraDAO.createOld(newCamera);

            cameraDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}