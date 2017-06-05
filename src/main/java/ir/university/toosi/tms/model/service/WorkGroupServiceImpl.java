package ir.university.toosi.tms.model.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.WorkGroupDAOImpl;
import ir.university.toosi.tms.model.entity.*;
import ir.university.toosi.tms.util.EventLogManager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class WorkGroupServiceImpl<T extends WorkGroup> {

    @EJB
    private WorkGroupDAOImpl workGroupDAO;

    @EJB
    private UserServiceImpl userService;

    @EJB
    private EventLogServiceImpl eventLogService;

    @EJB
    private LanguageManagementServiceImpl languageManagementService;

    @EJB
    private LanguageKeyManagementServiceImpl languageKeyManagementService;

    public T findById(long id) {
        try {
            return (T) workGroupDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByRoleId(String roleId) {
        try {
            return (List<T>) workGroupDAO.findByRoleId(roleId);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllWorkGroup() {
        try {
            return (List<T>) workGroupDAO.findAll("Workgroup.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteWorkGroup(T entity) {
        try {
            List<User> users = userService.findByWorkGroup(entity.getId());
            if (users != null && users.size() != 0)
                return "There is a user for this workgroup";
            entity.getRoles().clear();
            editWorkGroup(entity);
            EventLogManager.eventLog(eventLogService, null, WorkGroup.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            workGroupDAO.delete(findById(entity.getId()));
            return "Workgroup deleted successfully";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createWorkGroup(T entity) {
        try {
            entity.setId(getMaximumId());
              /**/
//            LanguageManagement languageManagement = new LanguageManagement();
//            languageManagement.setTitle(entity.getDescText() == null ? "" : entity.getDescText());
//            languageManagement.setType(entity.getCurrentLang());
//            languageManagementService.createLanguageManagement(languageManagement);
//
//            Set list = new HashSet();
//            list.add(languageManagement);
//
//            LanguageKeyManagement languageKeyManagement = new LanguageKeyManagement();
//            languageKeyManagement.setDescriptionKey(entity.getId() + WorkGroup.class.getSimpleName());
//            languageKeyManagement.setLanguageManagements(list);
//            entity.setDescription(entity.getId() + WorkGroup.class.getSimpleName());
//            languageKeyManagementService.createLanguageKeyManagement(languageKeyManagement);

            /**/
//            WorkGroup workGroup = new WorkGroup(entity.getId(), entity.getDescription(), entity.getEnabled(), new HashSet<Role>());
            WorkGroup workGroup = workGroupDAO.create(entity);
//            workGroup.getRoles().addAll(entity.getRoles());
//            workGroupDAO.update(workGroup);
//            EventLogManager.eventLog(eventLogService, String.valueOf(workGroup.getId()), WorkGroup.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return (T) workGroup;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean editWorkGroup(T entity) {
        try {
//            LanguageKeyManagement languageKeyManagement = languageKeyManagementService.findByDescKey(entity.getDescription());
//            if (languageKeyManagement != null) {
//                Set<LanguageManagement> list = languageKeyManagement.getLanguageManagements();
//                boolean hasDesc = false;
//                for (LanguageManagement languageManagement : list) {
//                    hasDesc = true;
//                    if (languageManagement.getType().getName().equalsIgnoreCase(entity.getCurrentLang().getName())) {
//                        languageManagement.setTitle(entity.getDescText() == null ? "" : entity.getDescText());
//                        languageManagementService.editLanguageManagement(languageManagement);
//                    }
//                }
//
//                if (!hasDesc) {
//
//                    LanguageManagement languageManagement = new LanguageManagement();
//                    languageManagement.setTitle(entity.getDescText() == null ? "" : entity.getDescText());
//                    languageManagement.setType(entity.getCurrentLang());
//                    languageManagementService.createLanguageManagement(languageManagement);
//
//                    list.add(languageManagement);
//                    languageKeyManagement.getLanguageManagements().clear();
//                    languageKeyManagementService.editLanguageKeyManagement(languageKeyManagement);
//                    languageKeyManagement.getLanguageManagements().addAll(list);
//                    languageKeyManagementService.editLanguageKeyManagement(languageKeyManagement);
//                }
//            } else {
//
//                LanguageManagement languageManagement = new LanguageManagement();
//                languageManagement.setTitle(entity.getDescText() == null ? "" : entity.getDescText());
//                languageManagement.setType(entity.getCurrentLang());
//                languageManagementService.createLanguageManagement(languageManagement);
//
//                Set list = new HashSet();
//                list.add(languageManagement);
//
//                languageKeyManagement = new LanguageKeyManagement();
//                languageKeyManagement.setDescriptionKey(entity.getId() + WorkGroup.class.getSimpleName());
//                languageKeyManagement.setLanguageManagements(list);
//                entity.setDescription(entity.getId() + WorkGroup.class.getSimpleName());
//                languageKeyManagementService.createLanguageKeyManagement(languageKeyManagement);
//            }
//
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), WorkGroup.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            workGroupDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getMaximumId() {
        try {
            return workGroupDAO.maximumId("WorkGroup.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }


}