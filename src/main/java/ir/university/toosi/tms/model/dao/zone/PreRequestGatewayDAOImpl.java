package ir.university.toosi.tms.model.dao.zone;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.PreRequestGateway;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class PreRequestGatewayDAOImpl extends BaseDAOImpl<PreRequestGateway> {


    public PreRequestGateway findById(long id) {
        try {
            return (PreRequestGateway) em.createNamedQuery("PreRequestGateway.findById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<PreRequestGateway> findByGateway(Gateway gateway) {
        try {
            return (List<PreRequestGateway>) em.createNamedQuery("PreRequestGateway.findByGateway")
                    .setParameter("gateId", gateway.getId())
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public PreRequestGateway findByGatewayAndPreGateway(Long gateId, Long preGateId) {
        try {
            return (PreRequestGateway) em.createNamedQuery("PreRequestGateway.findByGatewayAndPreGateway")
                    .setParameter("gateId", gateId)
                    .setParameter("preGateId", preGateId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}