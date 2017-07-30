package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.Paper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author :  Farzad
 * @version : 0.8
 */

@Stateless
@LocalBean

public class PaperDAOImpl extends BaseDAOImpl<Paper> {

    public Paper findById(long id) {
        try {
            return (Paper) em.createNamedQuery("Paper.findById")
                    .setParameter("id",id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Paper> findByName(String name) {
        try {
            return (List<Paper>) em.createNamedQuery("Paper.findByName")
                    .setParameter("name", name + "%")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Paper findByIp(String ip) {
        try {
            return (Paper) em.createNamedQuery("Paper.findByIp")
                    .setParameter("ip", ip + "%")
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean exist(String ip,long id) {
        try {
            List<Paper> paper = (List<Paper>) em.createNamedQuery("Paper.exist")
                    .setParameter("ip", ip)
                    .setParameter("id", id)
                    .getResultList();
            if (paper.size() != 0)
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }    public boolean existNotId(String ip) {
        try {
            List<Paper> paper = (List<Paper>) em.createNamedQuery("Paper.existNotId")
                    .setParameter("ip", ip)
                    .getResultList();
            if (paper.size() != 0)
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}