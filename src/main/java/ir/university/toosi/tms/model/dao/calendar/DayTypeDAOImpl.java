package ir.university.toosi.tms.model.dao.calendar;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.calendar.DayType;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class DayTypeDAOImpl extends BaseDAOImpl<DayType> {

    public DayType findById(long id) {
        try {
            return (DayType) em.createNamedQuery("DayType.findById")
                    .setParameter("id",id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}