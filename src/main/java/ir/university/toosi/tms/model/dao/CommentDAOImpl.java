package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.Comment;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
public class CommentDAOImpl extends BaseDAOImpl<Comment> {

    public Comment findById(String id) {
        try {
            return (Comment) em.createNamedQuery("Comment.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


    public List<Comment> findByEffectorUser(String userName) {
        try {
            return (List<Comment>) em.createNamedQuery("Comment.findByEffectorUser")
                    .setParameter("effectorUser", userName)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    public Comment findByTrafficLog(long id) {
        try {
            return (Comment) em.createNamedQuery("Comment.findByTrafficLog")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


}