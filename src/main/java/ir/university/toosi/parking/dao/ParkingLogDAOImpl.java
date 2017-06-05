package ir.university.toosi.parking.dao;


import ir.university.toosi.parking.entity.ParkingLog;
import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.ReportEntity;
import ir.university.toosi.tms.model.entity.SystemConfiguration;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class ParkingLogDAOImpl extends BaseDAOImpl<ParkingLog> {

    public ParkingLog findById(long id) {
        try {
            return (ParkingLog) em.createNamedQuery("ParkingLog.findById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


    public List<ParkingLog> findAll(String date) {
        try {
            return (List<ParkingLog>) em.createNamedQuery("ParkingLog.list")
                    .setParameter("ParkingDate", date)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ParkingLog> getParkingByQuery(String query) {
        try {
            return (List<ParkingLog>) em.createQuery(query).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ReportEntity> queryView(String query) {
        try {
            return (List<ReportEntity>) em.createQuery(query).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Long queryCountView(String query) {
        try {
            return (Long) em.createQuery(query).getSingleResult();
        } catch (Exception e) {
            return 0L;
        }
    }


    public List<ParkingLog> findParkingInDuration(String startDate, String endDate) {
        try {
            return (List<ParkingLog>) em.createNamedQuery("ParkingLog.findParkingInDuration")
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ParkingLog> findParkingInDurationTime(String startTime, String endTime, String pelak) {
        try {
            return (List<ParkingLog>) em.createNamedQuery("ParkingLog.findParkingInDurationTime")
                    .setParameter("startTime", startTime)
                    .setParameter("endTime", endTime)
                    .setParameter("pelak", pelak)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }









    public List<ParkingLog> findInDuration(String date) {
        try {

            List<ParkingLog> ParkingLogs = em.createNamedQuery("ParkingLog.findInDuration")
                    .setParameter("date", date)
                    .getResultList();
            if (ParkingLogs == null)
                return null;
            return ParkingLogs;
        } catch (Exception e) {
            return null;
        }
    }


}
