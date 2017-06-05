package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.PCDAOImpl;
import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.PC;
import ir.university.toosi.tms.model.entity.User;
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

public class PCServiceImpl<T extends PC> {

    @EJB
    private PCDAOImpl pcdao;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private UserServiceImpl userService;

    public T findById(long id) {
        try {
            return (T) pcdao.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean exist(String ip,long id) {
        try {
            return pcdao.exist(ip,id);
        } catch (Exception e) {
            return false;
        }
    }    public boolean existNotId(String ip) {
        try {
            return pcdao.existNotId(ip);
        } catch (Exception e) {
            return false;
        }
    }


    public List<T> findByName(PC pc) {
        try {
            EventLogManager.eventLog(eventLogService, null, BLookup.class.getSimpleName(), EventLogType.SEARCH, pc.getEffectorUser());
            return (List<T>) pcdao.findByName(pc.getName());
        } catch (Exception e) {
            return null;
        }
    }

    public T findByIp(PC pc) {
        try {
            EventLogManager.eventLog(eventLogService, null, BLookup.class.getSimpleName(), EventLogType.SEARCH, pc.getEffectorUser());
            return (T) pcdao.findByIp(pc.getIp());
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllPCs() {
        try {
            return (List<T>) pcdao.findAll("PC.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public long getMaximumId() {
        try {
            return pcdao.maximumId("PC.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public void deletePC(T entity) {
        try {
            List<User> users = userService.findByPc(entity.getId());
            for (User user : users) {
                List<PC> pcs = new ArrayList<>();
                for (PC pc : user.getPcs()) {
                    if (pc.getId() == entity.getId())
                        pcs.add(pc);
                }
                user.getPcs().clear();
                userService.editUser(user);
                user.getPcs().addAll(pcs);
                userService.editUser(user);
            }

            EventLogManager.eventLog(eventLogService, null, BLookup.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            pcdao.delete(findById(entity.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public T createPC(T entity) {
        try {
            T t = (T) pcdao.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), BLookup.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editPC(T entity) {
        try {
            PC old = findById(entity.getId());
            PC newPC = new PC();
            newPC.setName(old.getName());
            newPC.setIp(old.getIp());
            newPC.setLocation(old.getLocation());
            newPC.setStatus("o," + entity.getEffectorUser());
            pcdao.createOld(newPC);


            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), BLookup.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            pcdao.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}