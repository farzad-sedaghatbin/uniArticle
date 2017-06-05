package ir.university.toosi.guest.service;


import ir.university.toosi.guest.dao.GuestDaoImpl;
import ir.university.toosi.guest.entity.Guest;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by M_Danapour on 04/05/2015.
 */
@Stateless
@LocalBean
public class GuestServiceImpl<T extends Guest> {
    @EJB
    private GuestDaoImpl guestDao;

    public T create(T entity) throws Exception {
        return (T) guestDao.create(entity);

    }    public T update(T entity)  {
        return (T) guestDao.update(entity);
    }
    public List<T> todayGuest() throws Exception {
        return (List<T>) guestDao.todayList();
    }

    public GuestDaoImpl getGuestDao() {
        return guestDao;
    }

    public void setGuestDao(GuestDaoImpl guestDao) {
        this.guestDao = guestDao;
    }
    public List<T> query(String s){
        return (List<T>) guestDao.queryGuest(s);
    }
    public List<T> duration(String from,String to) throws Exception {
        return (List<T>) guestDao.durationList(from, to);
    }
}
