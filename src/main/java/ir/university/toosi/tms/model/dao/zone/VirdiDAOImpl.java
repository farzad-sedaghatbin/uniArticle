package ir.university.toosi.tms.model.dao.zone;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.zone.Virdi;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class VirdiDAOImpl extends BaseDAOImpl<Virdi> {

    public Virdi findById(long id) {
        try {
            return (Virdi) em.createNamedQuery("Virdi.findById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public Virdi findByTerminalId(int id) {
        try {
            return (Virdi) em.createNamedQuery("Virdi.findByTerminalId")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Virdi> findByGatewayId(String id) {
        try {
            return (List<Virdi>) em.createNamedQuery("Virdi.findByGatewayId")
                    .setParameter("id", Long.valueOf(id))
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    } public List<Virdi> getAllVirdibyIDs(List<Long> id) {
        try {
            return (List<Virdi>) em.createNamedQuery("Virdi.findByGatewayId")
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Virdi> findByCameraId(long id) {
        try {
            return (List<Virdi>) em.createNamedQuery("Virdi.findByCameraId")
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }


    public Virdi findByIp(String ip) {
        try {
            return (Virdi) em.createNamedQuery("Virdi.findByIp")
                    .setParameter("ip", ip + "%")
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public boolean exist(String ip,long id) {
        try {
            List<Virdi> virdi = (List<Virdi>) em.createNamedQuery("Virdi.exist")
                    .setParameter("ip", ip)
                    .setParameter("id", id)
                    .getResultList();
            if (virdi.size() != 0)
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }    public boolean existNotId(String ip) {
        try {
            List<Virdi> virdi = (List<Virdi>) em.createNamedQuery("Virdi.existNotId")
                    .setParameter("ip", ip)
                    .getResultList();
            if (virdi.size() != 0)
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

}