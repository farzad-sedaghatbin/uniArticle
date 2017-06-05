package ir.university.toosi.tms.model.dao;

import ir.university.toosi.tms.model.entity.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


public class BaseDAOImpl<T extends BaseEntity> {

    @PersistenceContext(unitName = "kernelPU")
    protected EntityManager em;

    public T update(T entity) {
        try {
            entity.setDeleted("0");
            entity.setStatus("C");
            return em.merge(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(T entity) {
        try {
            entity.setDeleted("1");
            em.merge(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public T create(T entity) {
        try {
            entity.setDeleted("0");
            entity.setStatus("C");
            em.persist(entity);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public T createOld(T entity) {
        try {
            entity.setDeleted("1");
            em.persist(entity);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public T find(T entity, Object primaryKey) {
        try {
            return (T) em.find(entity.getClass(), primaryKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public long maximumId(String query, boolean isNamedQuery) {
        try {
            return ((long) (isNamedQuery ? em.createNamedQuery(query).getSingleResult()
                    : em.createQuery(query).getSingleResult())) + 1;
        } catch (Exception e) {
            return 1;
        }
    }

    public List<T> findAll(String query, boolean isNamedQuery) {
        try {
            return isNamedQuery ? em.createNamedQuery(query).getResultList()
                    : em.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public EntityManager getEm() {
        return em;
    }
}