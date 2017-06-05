package ir.university.toosi.guest.dao;


import ir.university.toosi.guest.entity.Log;
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
public class LogDaoImpl extends BaseDAOImpl<Log> {
    public Log findById(String id) {
        try {
            return (Log) em.createNamedQuery("Log.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public List<Log> todayList() {
        try {
            return (List<Log>) em.createNamedQuery("Log.today")
                    .setParameter("date", CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")))
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }    public List<Log> durationList(String from, String to, String fromH, String toH) {
        try {
            return (List<Log>) em.createNamedQuery("Log.duration")
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .setParameter("fromH", fromH)
                    .setParameter("toH", toH)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
