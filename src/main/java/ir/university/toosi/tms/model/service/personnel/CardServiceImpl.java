package ir.university.toosi.tms.model.service.personnel;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.personnel.CardDAOImpl;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.util.EventLogManager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
public class CardServiceImpl<T extends Card> {

    @EJB
    private CardDAOImpl cardDAO;
    @EJB
    private EventLogServiceImpl eventLogService;


    public T findById(long id) {
        try {
            return (T) cardDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByPersonId(long id) {
        try {
            return (List<T>) cardDAO.findByPersonId(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByGuestId(long id) {
        try {
            return (List<T>) cardDAO.findByGuestId(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> closed() {
        try {
            return (List<T>) cardDAO.closed();
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> open() {
        try {
            return (List<T>) cardDAO.open();
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> stolen() {
        try {
            return (List<T>) cardDAO.stolen();
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> lost() {
        try {
            return (List<T>) cardDAO.lost();
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> assign(String startDate, String endDate) {
        try {
            return (List<T>) cardDAO.assign(startDate, endDate);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllCard() {
        try {
            return (List<T>) cardDAO.findAll("Card.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllInvisible() {
        try {
            return (List<T>) cardDAO.invisible();
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllActiveCard() {
        try {
            return (List<T>) cardDAO.findAll("Card.active.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllGuestActiveCard() {
        try {
            return (List<T>) cardDAO.findAll("Card.guest.list", true);
        } catch (Exception e) {
            return null;
        }
    }


    public String deleteCard(T entity) {
        try {
            cardDAO.delete(entity);
            return "operation.occurred";
        } catch (Exception e) {
            return "FALSE";
        }
    }


    public T createCard(T entity) {
        try {
            T t = (T) cardDAO.create(entity);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), Card.class.getSimpleName(), EventLogType.ADD, t.getEffectorUser());

            return t;
        } catch (Exception e) {
            return null;
        }
    }


    public long editCard(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), Card.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());

            Card oldCard = findById(entity.getId());
            Card newCard = new Card();
            newCard.setCardStatus(oldCard.getCardStatus());
            newCard.setCardType(oldCard.getCardType());
            newCard.setCode(oldCard.getCode());
            newCard.setExpirationDate(oldCard.getExpirationDate());
            newCard.setName(oldCard.getName());
            newCard.setStatus("o," + entity.getEffectorUser());
            newCard.setVisible(oldCard.isVisible());
            newCard.setStartDate(oldCard.getStartDate());
            newCard.setPerson(oldCard.getPerson());
            newCard.setGuest(oldCard.getGuest());
            newCard = cardDAO.createOld(newCard);

            cardDAO.update(entity);

            return newCard.getId();
        } catch (Exception e) {
            return 1;
        }
    }

    public Card findByCode(String code) {
        try {
            return cardDAO.findByCode(code);
        } catch (Exception e) {
            return null;
        }
    }

    public CardDAOImpl getCardDAO() {
        return cardDAO;
    }
}