package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.CommentDAOImpl;
import ir.university.toosi.tms.model.entity.Comment;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.util.EventLogManager;

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
public class CommentServiceImpl<T extends Comment> {

    @EJB
    private CommentDAOImpl commentDAO;

    @EJB
    private EventLogServiceImpl eventLogService;

    public T findById(String id) {
        try {
            return (T) commentDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByEffectorUser(String userName) {
        try {
            return (List<T>) commentDAO.findByEffectorUser(userName);
        } catch (Exception e) {
            return null;
        }
    }    public T findByTrafficLog(long id) {
        try {
            return (T) commentDAO.findByTrafficLog(id);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllComment() {
        try {
            return (List<T>) commentDAO.findAll("Comment.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllAuthorizeComment() {
        try {
            return (List<T>) commentDAO.findAll("Comment.authorizeList", true);
        } catch (Exception e) {
            return null;
        }
    }

    public String deleteComment(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Comment.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            commentDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }

    public String rejectComment(T entity) {
        try {
            entity.setReject(true);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createComment(T entity) {
        try {
            T t = (T) commentDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), Comment.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public boolean editComment(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Comment.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            commentDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}