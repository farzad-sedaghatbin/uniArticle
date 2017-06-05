package ir.university.toosi.guest.dao;


import ir.university.toosi.guest.entity.Guest;
import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;
import java.util.Locale;

/**
 * Created by M_Danapour on 04/06/2015.
 */
@Stateless
@LocalBean
public class GuestDaoImpl extends BaseDAOImpl<Guest> {
    public Guest findById(long id) {
        try {
            return (Guest) em.createNamedQuery("Guest.findById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }    public List<Guest> todayList() {
        try {
            return (List<Guest>) em.createNamedQuery("Guest.today")
                    .setParameter("date", CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")))
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    public List<Guest> queryGuest(String query) {
        try {
            query=query + " and p.date like \'%" + CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")) + "%\' " ;
            return (List<Guest>) em.createQuery(query).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    public List<Guest> durationList(String from, String to) {
        try {
            return (List<Guest>) em.createNamedQuery("Guest.duration")
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
