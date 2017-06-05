package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.SystemConfiguration;
import ir.university.toosi.tms.model.entity.SystemParameterType;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class SystemConfigurationDAOImpl extends BaseDAOImpl<SystemConfiguration> {


    public SystemConfiguration findById(String id) {
        try {
            return (SystemConfiguration) em.createNamedQuery("SystemConfiguration.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public SystemConfiguration findByParameter(SystemParameterType parameter) {
        try {
            return (SystemConfiguration) em.createNamedQuery("SystemConfiguration.findByParameter")
                    .setParameter("parameter", parameter)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}