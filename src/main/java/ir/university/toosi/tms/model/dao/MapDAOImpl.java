package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class MapDAOImpl extends BaseDAOImpl<Map> {

    public Map findById(String id) {
        try {
            return (Map) em.createNamedQuery("Map.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Map> findByCode(String code) {
        try {
            return (List<Map>) em.createNamedQuery("Map.findByCode")
                    .setParameter("code", code + "%")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}