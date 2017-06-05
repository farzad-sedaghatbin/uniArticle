package ir.university.toosi.tms.model.service;

import ir.university.toosi.tms.model.dao.LookupDAOImpl;
import ir.university.toosi.tms.model.entity.Lookup;

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

public class LookupServiceImpl<T extends Lookup> {

    @EJB
    private LookupDAOImpl lookupDAO;


    public T findById(String id) {
        try {
            return (T) lookupDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public T findByCode(String code) {
        try {
            return (T) lookupDAO.findByTitle(code);
        } catch (Exception e) {
            return null;
        }
    }


    public List<T> getAllLookup() {
        try {
            return (List<T>) lookupDAO.findAll("Lookup.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllDefinableLookup() {
        try {
            return (List<T>) lookupDAO.findDefinable();
        } catch (Exception e) {
            return null;
        }
    }

}