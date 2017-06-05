package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.WorkGroup;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class WorkGroupDAOImpl extends BaseDAOImpl<WorkGroup> {


    public WorkGroup findById(long id) {
        try {
            return (WorkGroup) em.createNamedQuery("Workgroup.findById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<WorkGroup> findByRoleId(String roleId) {
        try {
            return (List<WorkGroup>) em.createNamedQuery("Workgroup.findByRoleId")
                    .setParameter("roleId", Long.valueOf(roleId))
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }


}