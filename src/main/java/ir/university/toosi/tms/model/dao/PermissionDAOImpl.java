package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.Permission;
import ir.university.toosi.tms.model.entity.PermissionType;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class PermissionDAOImpl extends BaseDAOImpl<Permission> {

    public List<Permission> findByObjectId(String objectId, PermissionType permissionType) {
        try {
            return (List<Permission>) em.createNamedQuery("Permission.findByObjectId")
                    .setParameter("objectId", objectId)
                    .setParameter("permissionType", permissionType)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Permission> findByPermissionType(PermissionType permissionType) {
        try {
            return (List<Permission>) em.createNamedQuery("Permission.findByPermission")
                    .setParameter("permissionType", permissionType)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean exist(Permission entity) {
        try {
            Permission permission = (Permission) em.createNamedQuery("Permission.exist")
                    .setParameter("objectId", entity.getObjectId())
                    .setParameter("permissionType", entity.getPermissionType())
                    .getSingleResult();
            return true;
        } catch (Exception nre) {
            return false;
        }
    }

    public Permission findById(String id) {
        try {
            return (Permission) em.createNamedQuery("Permission.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}