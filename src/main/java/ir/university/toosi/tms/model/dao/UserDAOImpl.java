package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.User;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class UserDAOImpl extends BaseDAOImpl<User> {

    public boolean exist(String username) {
        try {
            List<User> user = (List<User>) em.createNamedQuery("User.exist")
                    .setParameter("username", username)
                    .getResultList();
            if (user.size() != 0)
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public User authenticate(String username, String password) {
        try {
            return (User) em.createNamedQuery("User.findByUsernameAndPassword")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public User findById(long id) {
        try {
            return (User) em.createNamedQuery("User.findById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public User findByUsername(String username) {
        try {
            return (User) em.createNamedQuery("User.findByUsername")
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> findAllByUsername(String username) {
        try {
            return (List<User>) em.createNamedQuery("User.findByUsername")
                    .setParameter("username", username)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> findByPerson(Long personId) {
        try {
            return (List<User>) em.createNamedQuery("User.findByPersonId")
                    .setParameter("id", personId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> findByWorkGroup(Long workGroupId) {
        try {
            return (List<User>) em.createNamedQuery("User.findByWorkGroupId")
                    .setParameter("workGroupId", workGroupId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> findByPc(Long pcId) {
        try {
            return (List<User>) em.createNamedQuery("User.findByPcId")
                    .setParameter("pcId", pcId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}