package ir.university.toosi.tms.model.dao.zone;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.zone.Camera;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class CameraDAOImpl extends BaseDAOImpl<Camera> {

    public boolean exist(String ip,long id) {
        try {
            List<Camera> cameras = (List<Camera>) em.createNamedQuery("Camera.exist")
                    .setParameter("ip", ip)
                    .setParameter("id", id)
                    .getResultList();
            if (cameras.size() != 0)
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }    public boolean existNotId(String ip) {
        try {
            List<Camera> cameras = (List<Camera>) em.createNamedQuery("Camera.existNotId")
                    .setParameter("ip", ip)
                    .getResultList();
            if (cameras.size() != 0)
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    public Camera findById(long id) {
        try {
            return (Camera) em.createNamedQuery("Camera.findById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}