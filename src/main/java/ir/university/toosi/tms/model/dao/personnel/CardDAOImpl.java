package ir.university.toosi.tms.model.dao.personnel;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.personnel.Card;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class CardDAOImpl extends BaseDAOImpl<Card> {


    public Card findById(long id) {
        try {
            return (Card) em.createNamedQuery("Card.findById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Card> findByPersonId(long id) {
        try {
            return (List<Card>) em.createNamedQuery("Card.findByPersonId")
                    .setParameter("id",id)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }    public List<Card> findByGuestId(long id) {
        try {
            return (List<Card>) em.createNamedQuery("Card.findByGuestId")
                    .setParameter("id",id)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Card> invisible() {
        try {
            return (List<Card>) em.createNamedQuery("Card.invisible")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Card> closed() {
        try {
            return (List<Card>) em.createNamedQuery("Card.closed")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Card> open() {
        try {
            return (List<Card>) em.createNamedQuery("Card.open")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Card> lost() {
        try {
            return (List<Card>) em.createNamedQuery("Card.lost")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Card> stolen() {
        try {
            return (List<Card>) em.createNamedQuery("Card.stolen")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Card> assign(String startDate, String endDate) {
        try {
            return (List<Card>) em.createNamedQuery("Card.assign").setParameter("startDate", startDate).setParameter("endDate", endDate)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Card findByCode(String code) {
        try {
            return (Card) em.createNamedQuery("Card.findByCode")
                    .setParameter("code", code)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}