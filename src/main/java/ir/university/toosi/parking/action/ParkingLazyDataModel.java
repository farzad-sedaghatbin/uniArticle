package ir.university.toosi.parking.action;

import ir.university.toosi.parking.entity.ParkingLog;
import ir.university.toosi.parking.service.ParkingLogServiceImpl;
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
public class ParkingLazyDataModel extends LazyDataModel<ParkingLog> {

    private ParkingLogServiceImpl ParkingLogService;

    public ParkingLazyDataModel(ParkingLogServiceImpl ParkingLogService) {
        this.ParkingLogService = ParkingLogService;
    }

    @Override
    public List<ParkingLog> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        EntityManager em = ParkingLogService.getParkingLogDAO().getEm();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        Predicate predicate = builder.conjunction();
        CriteriaQuery<ParkingLog> criteria = builder.createQuery(ParkingLog.class);
        Root<ParkingLog> root = criteria.from(ParkingLog.class);

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
        TypedQuery<ParkingLog> typedQuery = em.createQuery(criteria);

        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(first + pageSize);
        return typedQuery.getResultList();
    }



    @Override
    public Object getRowKey(ParkingLog object) {
        return object.getId();
    }

    @Override
    public ParkingLog getRowData(String rowKey) {
        return ParkingLogService.findById(Long.parseLong(rowKey));
    }
}
