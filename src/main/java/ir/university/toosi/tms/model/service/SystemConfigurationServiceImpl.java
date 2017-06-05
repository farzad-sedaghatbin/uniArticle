package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.SystemConfigurationDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.SystemConfiguration;
import ir.university.toosi.tms.model.entity.SystemParameterType;
import ir.university.toosi.tms.model.entity.TrafficLog;
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

public class SystemConfigurationServiceImpl<T extends SystemConfiguration> {

    @EJB
    private SystemConfigurationDAOImpl systemConfigurationDAO;

    @EJB
    private EventLogServiceImpl eventLogService;


    public long getMaximumId() {
        try {
            return systemConfigurationDAO.maximumId("SystemConfiguration.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }


    public T findById(String id) {
        try {
            return (T) systemConfigurationDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public T findByParameter(SystemParameterType parameter) {
        try {
            return (T) systemConfigurationDAO.findByParameter(parameter);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllConfiguration() {
        try {
            return (List<T>) systemConfigurationDAO.findAll("SystemConfiguration.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteConfiguration(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), TrafficLog.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            systemConfigurationDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createConfiguration(T entity) {
        try {
            entity = (T) systemConfigurationDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), TrafficLog.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return entity;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editConfiguration(T entity) {
        try {
            systemConfigurationDAO.update(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), TrafficLog.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
