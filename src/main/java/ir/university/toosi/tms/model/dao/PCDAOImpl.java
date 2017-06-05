package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.PC;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class PCDAOImpl extends BaseDAOImpl<PC> {

    public PC findById(long id) {
        try {
            return (PC) em.createNamedQuery("PC.findById")
                    .setParameter("id",id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<PC> findByName(String name) {
        try {
            return (List<PC>) em.createNamedQuery("PC.findByName")
                    .setParameter("name", name + "%")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public PC findByIp(String ip) {
        try {
            return (PC) em.createNamedQuery("PC.findByIp")
                    .setParameter("ip", ip + "%")
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean exist(String ip,long id) {
        try {
            List<PC> pc = (List<PC>) em.createNamedQuery("PC.exist")
                    .setParameter("ip", ip)
                    .setParameter("id", id)
                    .getResultList();
            if (pc.size() != 0)
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }    public boolean existNotId(String ip) {
        try {
            List<PC> pc = (List<PC>) em.createNamedQuery("PC.existNotId")
                    .setParameter("ip", ip)
                    .getResultList();
            if (pc.size() != 0)
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}