package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.PermissionDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.Permission;
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

public class PermissionServiceImpl<T extends Permission> {

    @EJB
    private PermissionDAOImpl permissionDAO;

    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private LanguageManagementServiceImpl languageManagementService;
    @EJB
    private LanguageKeyManagementServiceImpl languageKeyManagementService;

    public boolean exist(T entity) {
        try {
            return permissionDAO.exist(entity);
        } catch (Exception e) {
            return false;
        }
    }

    public long getMaximumId() {
        try {
            return permissionDAO.maximumId("Permission.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public List<T> findByObjectId(T entity) {
        try {
            return (List<T>) permissionDAO.findByObjectId(entity.getObjectId(), entity.getPermissionType());
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByPermissionType(T entity) {
        try {
            return (List<T>) permissionDAO.findByPermissionType(entity.getPermissionType());
        } catch (Exception e) {
            return null;
        }
    }


    public T findById(String id) {
        try {
            return (T) permissionDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllOperation() {
        try {
            return (List<T>) permissionDAO.findAll("Permission.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deletePermisssion(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Permission.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            permissionDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createPermission(T entity) {
        try {
            entity = (T) permissionDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Permission.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return entity;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editPermission(T entity) {
        try {
            permissionDAO.update(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Permission.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}