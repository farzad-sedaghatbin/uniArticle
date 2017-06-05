package ir.university.toosi.tms.model.dao.personnel;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.personnel.Job;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class JobDAOImpl extends BaseDAOImpl<Job> {


    public Job findById(String id) {
        try {
            return (Job) em.createNamedQuery("Job.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Job findByPersonId(long id) {
        try {
            return (Job) em.createNamedQuery("Job.findByPersonId")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}