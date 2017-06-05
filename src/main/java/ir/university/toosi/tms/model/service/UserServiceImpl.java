package ir.university.toosi.tms.model.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.UserDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.User;
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
public class UserServiceImpl<T extends User> {

    @EJB
    private UserDAOImpl userDAO;
    @EJB
    private RoleServiceImpl roleService;
    @EJB
    private WorkGroupServiceImpl workGroupService;
    @EJB
    private PCServiceImpl pcService;
    @EJB
    private EventLogServiceImpl eventLogService;

    public boolean exist(String username) {
        try {
            return userDAO.exist(username);
        } catch (Exception e) {
            return false;
        }
    }


    public T authenticate(String username, String password) {
        try {
            return (T) userDAO.authenticate(username, password);
        } catch (Exception e) {
            return null;
        }
    }


    public T findById(long id) {
        try {
            return (T) userDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public T findByUsername(String user) {
        try {
            return (T) userDAO.findByUsername(user);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findAllByUsername(User user) {
        try {
            return (List<T>) userDAO.findAllByUsername(user.getUsername());
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByPerson(Long personId) {
        try {
            return (List<T>) userDAO.findByPerson(personId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByWorkGroup(Long workGroupId) {
        try {
            return (List<T>) userDAO.findByWorkGroup(workGroupId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByPc(Long pcId) {
        try {
            return (List<T>) userDAO.findByPc(pcId);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean modifyPassword(T entity) {
        try {
            userDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public List<T> getAllUser() {
        try {
            return (List<T>) userDAO.findAll("User.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public long getMaximumId() {
        try {
            return userDAO.maximumId("User.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }


    public List<T> getAllPendingUser() {
        try {
            return (List<T>) userDAO.findAll("User.pendingList", true);
        } catch (Exception e) {
            return null;
        }
    }

    public T createUser(T entity) {
        try {
            entity=(T) userDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), User.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return entity;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean create(T user) {
        try {
            user = createUser(user);
            EventLogManager.eventLog(eventLogService, String.valueOf(user.getId()), User.class.getSimpleName(), EventLogType.ADD, user.getEffectorUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String deleteUser(T entity) {
        try {
            entity.getPcs().clear();
            entity.getWorkGroups().clear();
            editUser(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), User.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            userDAO.delete(findById(entity.getId()));

            return "operation.occurred";
        } catch (Exception e) {
            return "false";
        }
    }


    public boolean editUser(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), User.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            userDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public UserDAOImpl getUserDAO() {
        return userDAO;
    }
}