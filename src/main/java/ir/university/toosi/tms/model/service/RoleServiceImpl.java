package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.RoleDAOImpl;
import ir.university.toosi.tms.model.entity.*;
import ir.university.toosi.tms.util.EventLogManager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class RoleServiceImpl<T extends Role> {

    @EJB
    private RoleDAOImpl roleDAO;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private WorkGroupServiceImpl workGroupService;
    @EJB
    private LanguageManagementServiceImpl languageManagementService;
    @EJB
    private LanguageKeyManagementServiceImpl languageKeyManagementService;


    public T findById(String id) {
        try {
            return (T) roleDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllRole() {
        try {

            return (List<T>) roleDAO.findAll("Role.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllRoleForWorkgroupEdit(Set<Role> roles) {
        try {

            return (List<T>) roleDAO.findAllForWorkgroupEdit("Role.listForWorkgroupEdit", roles);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public String deleteRole(T entity) {
        try {
            List<WorkGroup> workGroups = workGroupService.findByRoleId(String.valueOf(entity.getId()));
            if (workGroups != null && workGroups.size() != 0)
                return new ObjectMapper().writeValueAsString("REL_ROLE_WORKGROUP");

            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Role.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            roleDAO.delete(findById(String.valueOf(entity.getId())));
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createRole(T entity) {
        try {
            entity.setId(getMaximumId());
             /**/

            Role role = (T) roleDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(role.getId()), Role.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return (T) role;
        } catch (Exception e) {
            return null;
        }
    }

    public long getMaximumId() {
        try {
            return roleDAO.maximumId("Role.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public boolean editRole(T entity) {
        try {
            List<Operation> parentOperation = getParentOperationList(entity.getOperations());
            entity.getOperations().addAll(parentOperation);
            roleDAO.update(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Role.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private List<Operation> getParentOperationList(Set<Operation> operations) {
        List<Operation> parentOperations = new ArrayList<>();
        for (Operation operation : operations) {
            if (operation.getParent() == null)
                continue;
            boolean hasParent = false;
            for (Operation innerOperation : operations) {
                if (innerOperation.getId() == operation.getParent().getId()) {
                    hasParent = true;
                    break;
                }
            }
            if (!hasParent)
                parentOperations.add(operation.getParent());
        }
        return parentOperations;
    }

    public boolean pureEditRole(T entity) {
        try {
            roleDAO.update(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Role.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
