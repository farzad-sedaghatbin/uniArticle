package ir.university.toosi.wtms.web.action.traffic;

import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.entity.TrafficLogDataModel;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.service.TrafficLogServiceImpl;
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
public class TrafficLazyDataModel extends LazyDataModel<TrafficLog> {

    private TrafficLogServiceImpl trafficLogService;

    public TrafficLazyDataModel(TrafficLogServiceImpl trafficLogService) {
        this.trafficLogService = trafficLogService;
    }

    @Override
    public List<TrafficLog> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        EntityManager em = trafficLogService.getTrafficLogDAO().getEm();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        Predicate predicate = builder.conjunction();
        CriteriaQuery<TrafficLog> criteria = builder.createQuery(TrafficLog.class);
        Root<TrafficLog> root = criteria.from(TrafficLog.class);

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
        TypedQuery<TrafficLog> typedQuery = em.createQuery(criteria);

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(first + pageSize);
        return typedQuery.getResultList();
    }



    @Override
    public Object getRowKey(TrafficLog object) {
        return object.getId();
    }

    @Override
    public TrafficLog getRowData(String rowKey) {
        return trafficLogService.findById(Long.parseLong(rowKey));
    }
}
