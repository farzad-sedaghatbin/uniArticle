package ir.university.toosi.parking.dao;


import ir.university.toosi.parking.entity.Car;
import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.ReportEntity;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class CarDAOImpl extends BaseDAOImpl<Car> {

    public Car findById(long id) {
        try {
            return (Car) em.createNamedQuery("Car.findById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


    public List<Car> findAll(String date) {
        try {
            return (List<Car>) em.createNamedQuery("Car.list")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Car> getParkingByQuery(String query) {
        try {
            return (List<Car>) em.createQuery(query).getResultList();
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


    public List<Car> findByNumber(String number) {
        try {

            List<Car> Cars = em.createNamedQuery("Car.findByNumber")
                        .setParameter("number", number)
                    .getResultList();
            if (Cars == null)
                return null;
            return Cars;
        } catch (Exception e) {
            return null;
        }
    }


}
