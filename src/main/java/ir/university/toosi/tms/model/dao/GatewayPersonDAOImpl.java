package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.GatewayPerson;
import ir.university.toosi.tms.model.entity.personnel.Person;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
public class GatewayPersonDAOImpl extends BaseDAOImpl<GatewayPerson> {

    public List<GatewayPerson> findByGatewayId(long id) {
        try {
            return (List<GatewayPerson>) em.createNamedQuery("GatewayPerson.findByGatewayId")
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }    public List<Person> findPersonByGatewayId(long id) {
        try {
            return (List<Person>) em.createNamedQuery("GatewayPerson.findPersonByGatewayId")
                    .setParameter("id",id)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<GatewayPerson> findByPersonId(long id) {
        try {
            return (List<GatewayPerson>) em.createNamedQuery("GatewayPerson.findByPersonId")
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public GatewayPerson findByPersonIdAndGatewayId(long personId, long gatewayId) {
        try {
            return (GatewayPerson) em.createNamedQuery("GatewayPerson.findByPersonIdAndGatewayId")
                    .setParameter("personId", personId)
                    .setParameter("gatewayId", gatewayId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


}
