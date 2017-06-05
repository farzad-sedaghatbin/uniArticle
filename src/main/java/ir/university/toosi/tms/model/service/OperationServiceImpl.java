package ir.university.toosi.tms.model.service;

import ir.university.toosi.tms.model.dao.OperationDAOImpl;
import ir.university.toosi.tms.model.entity.*;
import ir.university.toosi.tms.util.Configuration;
import ir.university.toosi.tms.util.EventLogManager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class OperationServiceImpl<T extends Operation> {

    @EJB
    private OperationDAOImpl operationDAO;

    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private LanguageManagementServiceImpl languageManagementService;
    @EJB
    private LanguageKeyManagementServiceImpl languageKeyManagementService;


    public void test(String param) {
        System.out.println("5555>>>>" + param);
    }

    public boolean exist(T entity) {
        try {
            return operationDAO.exist(entity);
        } catch (Exception e) {
            return false;
        }
    }

    public long getMaximumId() {
        try {
            return operationDAO.maximumId("Operation.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public List<T> findByName(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, null, Operation.class.getSimpleName(), EventLogType.SEARCH, entity.getEffectorUser());
            return (List<T>) operationDAO.findByName(entity.getName());
        } catch (Exception e) {
            return null;
        }
    }


    public T findById(String id) {
        try {
            return (T) operationDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllOperation() {
        try {
            return (List<T>) operationDAO.findAll("Operation.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteOperation(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Operation.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            operationDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createOperation(T entity) {
        try {
            entity = (T) operationDAO.create(entity);
            LanguageManagement languageManagement = new LanguageManagement();
            languageManagement.setTitle(entity.getDescription() == null ? "" : entity.getDescription());
            languageManagementService.createLanguageManagement(languageManagement);

            Set list = new HashSet();
            list.add(languageManagement);

            LanguageKeyManagement languageKeyManagement = new LanguageKeyManagement();
            languageKeyManagement.setDescriptionKey(entity.getId() + Role.class.getSimpleName());
            languageKeyManagement.setLanguageManagements(list);
            entity.setDescription(entity.getId() + Operation.class.getSimpleName());
            languageKeyManagementService.createLanguageKeyManagement(languageKeyManagement);

            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Operation.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return entity;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editOperation(T entity) {
        try {
            operationDAO.update(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Operation.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public void backupDerby() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(Configuration.getProperty("derbyUrl"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        java.text.SimpleDateFormat todaysDate = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String backupDirectory = Configuration.getProperty("backupDir") + todaysDate.format((java.util.Calendar.getInstance()).getTime());

        CallableStatement cs = connection.prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)");
        cs.setString(1, backupDirectory);
        cs.execute();
        cs.close();
        System.out.println("backed up database to " + backupDirectory);
    }
}