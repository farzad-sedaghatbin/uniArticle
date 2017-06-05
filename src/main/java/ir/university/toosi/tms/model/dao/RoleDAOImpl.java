package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.Role;
import org.apache.commons.lang.StringUtils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class RoleDAOImpl extends BaseDAOImpl<Role> {


    public Role findById(String id) {
        try {
            return (Role) em.createNamedQuery("Role.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Role> findAllForWorkgroupEdit(String s, Set<Role> roles) {
        try {
            return em.createNamedQuery(s).setParameter("roles", roles).getResultList();
        } catch (Exception e){
            return new ArrayList<>();
        }
    }
}