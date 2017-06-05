package ir.university.toosi.wtms.web.action.user;

import ir.university.toosi.tms.model.entity.User;
import ir.university.toosi.tms.model.service.UserServiceImpl;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by omid on 10/8/2015.
 */
public class UserLazyDataModel extends LazyDataModel<User> {

    private UserServiceImpl userService;

    public UserLazyDataModel(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public List<User> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        EntityManager em = userService.getUserDAO().getEm();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        Predicate predicate = builder.conjunction();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);

        predicate = builder.and(predicate, builder.notEqual(root.<String>get("deleted"), "1"));

        for (Map.Entry<String, Object> e : filters.entrySet()) {
            predicate = builder.and(predicate, builder.like(root.<String>get(e.getKey()), "%" + e.getValue() + "%"));
        }

        if (sortField != null) {
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                criteria.orderBy(builder.asc(root.get(sortField)));
            } else {
                criteria.orderBy(builder.desc(root.get(sortField)));
            }
        }

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        countQuery.getRoots().add(root);
        countQuery.select(builder.count(root)).where(predicate);
        int count = em.createQuery(countQuery).getSingleResult().intValue();
        this.setRowCount(count);

        criteria.select(root).where(predicate);
        TypedQuery<User> typedQuery = em.createQuery(criteria);

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(first + pageSize);
        return typedQuery.getResultList();
    }


    @Override
    public Object getRowKey(User object) {
        return object.getId();
    }

    @Override
    public User getRowData(String rowKey) {
        return userService.findById(Long.parseLong(rowKey));
    }
}
