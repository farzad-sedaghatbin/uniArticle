package ir.university.toosi.tms.model.service.personnel;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.personnel.JobDAOImpl;
import ir.university.toosi.tms.model.entity.personnel.Job;
import ir.university.toosi.tms.model.entity.personnel.Person;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class JobServiceImpl<T extends Job> {

    @EJB
    private JobDAOImpl jobDAO;
    @EJB
    private PersonServiceImpl personService;

    public T findById(String id) {
        try {
            return (T) jobDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public T findByPersonId(long id) {
        try {
            return (T) jobDAO.findByPersonId(id);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllJob() {
        try {
            return (List<T>) jobDAO.findAll("Job.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteJob(T entity) {
        try {
            jobDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createJob(T entity) {
        try {
            Person person = personService.findById(entity.getPerson().getId());
            entity.setPerson(person);
            return (T) jobDAO.create(entity);
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editJob(T entity) {
        try {
            jobDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}