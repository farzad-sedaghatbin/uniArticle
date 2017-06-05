package ir.university.toosi.wtms.web.action.person;

import ir.university.toosi.tms.model.entity.User;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.service.UserServiceImpl;
import ir.university.toosi.tms.model.service.personnel.CardServiceImpl;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

/**
 * Created by o_javaheri on 10/11/2015.
 */
public class CardLazyDataModel extends LazyDataModel<Card> {

    private CardServiceImpl cardService;

    public CardLazyDataModel(CardServiceImpl cardService) {
        this.cardService = cardService;
    }

    @Override
    public List<Card> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        EntityManager em = cardService.getCardDAO().getEm();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        Predicate predicate = builder.conjunction();
        CriteriaQuery<Card> criteria = builder.createQuery(Card.class);
        Root<Card> root = criteria.from(Card.class);

        predicate = builder.and(predicate, builder.notEqual(root.<String>get("deleted"), "1"));

        for (Map.Entry<String,Object> e : filters.entrySet()) {
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
        TypedQuery<Card> typedQuery = em.createQuery(criteria);

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(first + pageSize);
        return typedQuery.getResultList();
    }



    @Override
    public Object getRowKey(Card object) {
        return object.getId();
    }

    @Override
    public Card getRowData(String rowKey) {
        return cardService.findById(Long.parseLong(rowKey));
    }
}
