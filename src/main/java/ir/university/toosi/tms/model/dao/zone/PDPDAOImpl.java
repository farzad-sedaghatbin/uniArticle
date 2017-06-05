package ir.university.toosi.tms.model.dao.zone;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.zone.PDP;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class PDPDAOImpl extends BaseDAOImpl<PDP> {

    public PDP findById(long id) {
        try {
            return (PDP) em.createNamedQuery("PDP.findById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<PDP> findByGatewayId(String id) {
        try {
            return (List<PDP>) em.createNamedQuery("PDP.findByGatewayId")
                    .setParameter("id", Long.valueOf(id))
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    } public List<PDP> getAllPdpbyIDs(List<Long> id) {
        try {
            return (List<PDP>) em.createNamedQuery("PDP.findByGatewayId")
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<PDP> findByCameraId(long id) {
        try {
            return (List<PDP>) em.createNamedQuery("PDP.findByCameraId")
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }


    public PDP findByIp(String ip) {
        try {
            return (PDP) em.createNamedQuery("PDP.findByIp")
                    .setParameter("ip", ip + "%")
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public boolean exist(String ip,long id) {
        try {
            List<PDP> pdp = (List<PDP>) em.createNamedQuery("PDP.exist")
                    .setParameter("ip", ip)
                    .setParameter("id", id)
                    .getResultList();
            if (pdp.size() != 0)
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }    public boolean existNotId(String ip) {
        try {
            List<PDP> pdp = (List<PDP>) em.createNamedQuery("PDP.existNotId")
                    .setParameter("ip", ip)
                    .getResultList();
            if (pdp.size() != 0)
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

}