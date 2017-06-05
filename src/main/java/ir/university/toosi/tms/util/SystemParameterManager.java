package ir.university.toosi.tms.util;


import ir.university.toosi.tms.model.entity.SystemConfiguration;
import ir.university.toosi.tms.model.entity.SystemParameterType;
import ir.university.toosi.tms.model.service.SystemConfigurationServiceImpl;

import java.util.Hashtable;

/**
 * @author : Hamed Hatami , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
public class SystemParameterManager {

    private static Hashtable<SystemParameterType, String> systemConfigurationStringHashtable = new Hashtable<>();

    public static void fillSystemConfiguration(SystemConfigurationServiceImpl systemConfigurationService) {

        for (SystemParameterType systemParameterType : SystemParameterType.values()) {

            SystemConfiguration systemConfiguration = systemConfigurationService.findByParameter(systemParameterType);
            if (systemConfiguration != null) {
                systemConfigurationStringHashtable.put(systemParameterType, systemConfiguration.getValue());
                continue;
            }
        }
    }


    public static SystemConfiguration updateSystemConfiguration(SystemConfigurationServiceImpl systemConfigurationService, SystemConfiguration systemConfiguration) {

        systemConfigurationStringHashtable.put(systemConfiguration.getParameter(), systemConfiguration.getValue());
        SystemConfiguration oldSystemConfiguration = systemConfigurationService.findByParameter(systemConfiguration.getParameter());
        if (oldSystemConfiguration != null) {
            oldSystemConfiguration.setParameter(systemConfiguration.getParameter());
            oldSystemConfiguration.setValue(systemConfiguration.getValue());
            systemConfigurationService.editConfiguration(systemConfiguration);
            return systemConfiguration;
        } else {
            SystemConfiguration configuration = systemConfigurationService.createConfiguration(systemConfiguration);
            return configuration;
        }
    }

    public static Hashtable<SystemParameterType, String> getSystemConfigurationStringHashtable() {
        return systemConfigurationStringHashtable;
    }

    public static void setSystemConfigurationStringHashtable(Hashtable<SystemParameterType, String> systemConfigurationStringHashtable) {
        SystemParameterManager.systemConfigurationStringHashtable = systemConfigurationStringHashtable;
    }
}
