package ir.university.toosi.wtms.web.action.event;

import ir.university.toosi.tms.model.entity.EventLog;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
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
 * Created by o_javaheri on 10/10/2015.
 */
public class EventLazyDataModel  extends LazyDataModel<EventLog> {

    private EventLogServiceImpl eventLogService;

    public EventLazyDataModel(EventLogServiceImpl eventLogService) {
        this.eventLogService = eventLogService;
    }

    @Override
    public List<EventLog> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        EntityManager em = eventLogService.getEventLogDAO().getEm();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        Predicate predicate = builder.conjunction();
        CriteriaQuery<EventLog> criteria = builder.createQuery(EventLog.class);
        Root<EventLog> root = criteria.from(EventLog.class);

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
        TypedQuery<EventLog> typedQuery = em.createQuery(criteria);

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(first + pageSize);
        return typedQuery.getResultList();
    }



    @Override
    public Object getRowKey(EventLog object) {
        return object.getId();
    }

    @Override
    public EventLog getRowData(String rowKey) {
        return eventLogService.findById(rowKey);
    }

}
