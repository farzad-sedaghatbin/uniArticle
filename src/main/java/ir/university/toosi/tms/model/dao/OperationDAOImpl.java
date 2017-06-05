package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.Operation;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class OperationDAOImpl extends BaseDAOImpl<Operation> {

    public List<Operation> findByName(String name) {
        try {
            return (List<Operation>) em.createNamedQuery("Operation.findByName")
                    .setParameter("name", name + "%")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean exist(Operation entity) {
        try {
            Operation operation = (Operation) em.createNamedQuery("Operation.exist")
                    .setParameter("name", entity.getName())
                    .getSingleResult();
            return true;
        } catch (Exception nre) {
            return false;
        }
    }

    public Operation findById(String id) {
        try {
            return (Operation) em.createNamedQuery("Operation.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}