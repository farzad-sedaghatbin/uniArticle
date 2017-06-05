package ir.university.toosi.guest.service;


import ir.university.toosi.guest.dao.GuestDaoImpl;
import ir.university.toosi.guest.dao.LogDaoImpl;
import ir.university.toosi.guest.entity.Log;
import ir.university.toosi.tms.model.service.personnel.CardServiceImpl;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by M_Danapour on 04/05/2015.
 */
@Stateless
@LocalBean
public class LogServiceImpl<T extends Log> {
    @EJB
    private LogDaoImpl guestDao;
    @EJB
    CardServiceImpl cardService;
    @EJB
    GuestDaoImpl guestService;

    public T create(T entity) throws Exception {
        entity.setGuest(guestService.findById(entity.getGuest().getId()));
        entity.setCard(cardService.findById(entity.getCard().getId()));
        return (T) guestDao.create(entity);
    }

    public List<T> todayLog() throws Exception {
        return (List<T>) guestDao.todayList();
    }

    public List<T> duration(String from, String to, String fromH, String toH) throws Exception {
        return (List<T>) guestDao.durationList(from, to, fromH, toH);
    }

}
