package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.ReportEntity;
import ir.university.toosi.tms.model.entity.SystemConfiguration;
import ir.university.toosi.tms.model.entity.TrafficLog;

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

public class TrafficLogDAOImpl extends BaseDAOImpl<TrafficLog> {

    public TrafficLog findById(long id) {
        try {
            return (TrafficLog) em.createNamedQuery("TrafficLog.findById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrafficLog> findByPersonIdInDuration(Long personId, Boolean exit, String date) {
        try {
            return (List<TrafficLog>) em.createNamedQuery("TrafficLog.findByPersonIdInDuration")
                    .setParameter("personId", personId)
                    .setParameter("exit", exit)
                    .setParameter("trafficDate", date)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrafficLog> findAll(String date) {
        try {
            return (List<TrafficLog>) em.createNamedQuery("TrafficLog.list")
                    .setParameter("trafficDate", date)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrafficLog> getTrafficByQuery(String query) {
        try {
            return (List<TrafficLog>) em.createQuery(query).getResultList();
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


    public List<TrafficLog> findByPersonLocationInDuration(Long personId, String date) {
        try {
            return (List<TrafficLog>) em.createNamedQuery("TrafficLog.findByPersonLocationInDuration")
                    .setParameter("personId", personId)
                    .setParameter("trafficDate", date)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrafficLog> findTrafficInDuration(String startDate, String endDate) {
        try {
            return (List<TrafficLog>) em.createNamedQuery("TrafficLog.findTrafficInDuration")
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrafficLog> findByPersonAndGate(Long personId, Long gatewayId, String date) {
        try {
            return (List<TrafficLog>) em.createNamedQuery("TrafficLog.findByPersonAndGate")
                    .setParameter("personId", personId)
                    .setParameter("gatewayTd", gatewayId)
                    .setParameter("trafficDate", date)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Integer findByPersonAndGateExOrEn(Long personId, Long gatewayId, String date, boolean exit) {
        try {
            return (Integer) em.createNamedQuery("TrafficLog.findByPersonAndGateExOrEn")
                    .setParameter("personId", personId)
                    .setParameter("gatewayTd", gatewayId)
                    .setParameter("trafficDate", date)
                    .setParameter("exit", exit)
                    .getFirstResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrafficLog> findByGate(Long gatewayId, String date) {
        try {
            return (List<TrafficLog>) em.createNamedQuery("TrafficLog.findByGate")
                    .setParameter("gatewayTd", gatewayId)
                    .setParameter("trafficDate", date)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrafficLog> findByPDP(Long PDPId, String date, SystemConfiguration systemConfiguration) {
        try {
            return (List<TrafficLog>) em.createNamedQuery("TrafficLog.findByPDP")
                    .setParameter("PDPId", PDPId)
                    .setParameter("trafficDate", date)
                    .setFirstResult(1)
                    .setMaxResults(Integer.parseInt(systemConfiguration.getValue()))
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrafficLog> findByVirdi(Long virdiId, String date, SystemConfiguration systemConfiguration) {
        try {
            return (List<TrafficLog>) em.createNamedQuery("TrafficLog.findByVirdi")
                    .setParameter("virdiId", virdiId)
                    .setParameter("trafficDate", date)
                    .setFirstResult(0)
                    .setMaxResults(Integer.parseInt(systemConfiguration.getValue()))
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrafficLog> findByPerson(Long personId, String date) {
        try {

            List<TrafficLog> trafficLogs = em.createNamedQuery("TrafficLog.findByPerson")
                    .setParameter("personId", personId)
                    .setParameter("trafficDate", date)
                    .getResultList();
            if (trafficLogs == null)
                return null;
            return trafficLogs;
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrafficLog> findByPersonInDuration(Long personId, String date, String toDate) {
        try {

            List<TrafficLog> trafficLogs = em.createNamedQuery("TrafficLog.findByPersonInDuration")
                    .setParameter("personId", personId)
                    .setParameter("fromDate", date)
                    .setParameter("toDate", toDate)
                    .getResultList();
            if (trafficLogs == null)
                return null;
            return trafficLogs;
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrafficLog> findByOrganInDuration(Long organId, String date, String toDate) {
        try {

            List<TrafficLog> trafficLogs = em.createNamedQuery("TrafficLog.findByOrganInDuration")
                    .setParameter("organId", organId)
                    .setParameter("fromDate", date)
                    .setParameter("toDate", toDate)
                    .getResultList();
            if (trafficLogs == null)
                return null;
            return trafficLogs;
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrafficLog> findByGateInDuration(Long gateId, String date, String toDate) {
        try {

            List<TrafficLog> trafficLogs = em.createNamedQuery("TrafficLog.findByGateInDuration")
                    .setParameter("gateId", gateId)
                    .setParameter("fromDate", date)
                    .setParameter("toDate", toDate)
                    .getResultList();
            if (trafficLogs == null)
                return null;
            return trafficLogs;
        } catch (Exception e) {
            return null;
        }
    }    public List<TrafficLog> findByVirdiInDuration(Long gateId, String date, String toDate) {
        try {

            List<TrafficLog> trafficLogs = em.createNamedQuery("TrafficLog.findByVirdiInDuration")
                    .setParameter("gateId", gateId)
                    .setParameter("fromDate", date)
                    .setParameter("toDate", toDate)
                    .getResultList();
            if (trafficLogs == null)
                return null;
            return trafficLogs;
        } catch (Exception e) {
            return null;
        }
    }

    public TrafficLog findLastByPerson(Long personId, String date) {
        try {

            return (TrafficLog) em.createNamedQuery("TrafficLog.findLastByPerson")
                    .setParameter("personId", personId)
                    .setParameter("trafficDate", date)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrafficLog> findInDuration(String date) {
        try {

            List<TrafficLog> trafficLogs = em.createNamedQuery("TrafficLog.findInDuration")
                    .setParameter("date", date)
                    .getResultList();
            if (trafficLogs == null)
                return null;
            return trafficLogs;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Object[]> searchForChart(String fromTime, String toTime,String fromDate,String toDate, boolean valid) {
        try {

            return em.createNamedQuery("TrafficLog.searchForChart")
                    .setParameter("fromTime", fromTime)
                    .setParameter("toTime", toTime)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("valid", valid)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
