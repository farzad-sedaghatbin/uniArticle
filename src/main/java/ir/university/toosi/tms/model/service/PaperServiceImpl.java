package ir.university.toosi.tms.model.service;

import ir.university.toosi.tms.model.dao.PaperDAOImpl;
import ir.university.toosi.tms.model.entity.Paper;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.Paper;
import ir.university.toosi.tms.util.EventLogManager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author :  Farzad
 * @version : 0.8
 */

@Stateless
@LocalBean

public class PaperServiceImpl<T extends Paper> {

    @EJB
    private PaperDAOImpl paperdao;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private UserServiceImpl userService;

    public T findById(long id) {
        try {
            return (T) paperdao.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean exist(String ip,long id) {
        try {
            return paperdao.exist(ip,id);
        } catch (Exception e) {
            return false;
        }
    }    public boolean existNotId(String ip) {
        try {
            return paperdao.existNotId(ip);
        } catch (Exception e) {
            return false;
        }
    }


    public List<T> findByName(Paper paper) {
        try {
            EventLogManager.eventLog(eventLogService, null, Paper.class.getSimpleName(), EventLogType.SEARCH, paper.getEffectorUser());
            return (List<T>) paperdao.findByName(paper.getName());
        } catch (Exception e) {
            return null;
        }
    }




    public List<T> getAllPapers() {
        try {
            return (List<T>) paperdao.findAll("Paper.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public long getMaximumId() {
        try {
            return paperdao.maximumId("Paper.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public void deletePaper(T entity) {
        try {

            EventLogManager.eventLog(eventLogService, null, Paper.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            paperdao.delete(findById(entity.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public T createPaper(T entity) {
        try {
            T t = (T) paperdao.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), Paper.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editPaper(T entity) {
        try {
            Paper old = findById(entity.getId());
            Paper newPaper = new Paper();
            newPaper.setName(old.getName());
            newPaper.setStatus("o," + entity.getEffectorUser());
            paperdao.createOld(newPaper);


            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Paper.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            paperdao.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}