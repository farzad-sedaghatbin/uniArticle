package ir.university.toosi.wtms.web.action.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.wtms.web.action.AccessControlAction;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.tms.model.entity.EventLog;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleEventAction")
@SessionScoped
public class HandleEventAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private GeneralHelper generalHelper;
    @Inject
    private AccessControlAction accessControlAction;

    @EJB
    private EventLogServiceImpl logService;
    private LazyDataModel<EventLog> eventLogList = null;
    private SortOrder eventLogOperationOrder = SortOrder.DESCENDING;
    private SortOrder eventLogDateOrder = SortOrder.DESCENDING;
    private SortOrder eventLogUsernameOrder = SortOrder.DESCENDING;
    private SortOrder eventLogObjectOrder = SortOrder.UNSORTED;
    private SortOrder tableNameOrder = SortOrder.UNSORTED;
    private String eventLogOperationFilter;
    private String eventLogDateFilter;
    private String eventLogUsernameFilter;
    private String fromDate;
    private String toDate;
    private int page = 1;

    public void begin() {
        me.setActiveMenu(MenuType.REPORT);
        refresh();
        me.redirect("/event/list-event.xhtml");
    }

    private void refresh() {
        page = 1;
        fromDate = "";
        toDate = "";
        eventLogOperationFilter = "";
        eventLogDateFilter = "";
        eventLogUsernameFilter = "";
        List<EventLog> innerEventLogList = null;
            innerEventLogList = logService.getAllEventLog();

        eventLogList = new EventLazyDataModel(logService);
    }


    public void sortByEventLogOperation() {


        if (eventLogOperationOrder.equals(SortOrder.ASCENDING)) {
            setEventLogOperationOrder(SortOrder.DESCENDING);
        } else {
            setEventLogOperationOrder(SortOrder.ASCENDING);
        }
    }

//    public Filter<?> getEventLognameFilterImpl() {
//        return new Filter<EventLog>() {
//            public boolean accept(EventLog eventLog) {
//                return eventLogOperationFilter == null || eventLogOperationFilter.length() == 0 || eventLog.getOperation().getDescription().toLowerCase().contains(eventLogOperationFilter.toLowerCase());
//            }
//        };
//    }

    public void sortByEventLogDate() {
        if (eventLogDateOrder.equals(SortOrder.ASCENDING)) {
            setEventLogDateOrder(SortOrder.DESCENDING);
        } else {
            setEventLogDateOrder(SortOrder.ASCENDING);
        }
    }

//    public Filter<?> getEventLogDateFilterImpl() {
//        return new Filter<EventLog>() {
//            public boolean accept(EventLog eventLog) {
//                return eventLogDateFilter == null || eventLogDateFilter.length() == 0 || eventLog.getDate().toLowerCase().contains(eventLogDateFilter.toLowerCase().replace("/", ""));
//            }
//        };
//    }

    public void sortByEventLogUsername() {


        if (eventLogUsernameOrder.equals(SortOrder.ASCENDING)) {
            setEventLogUsernameOrder(SortOrder.DESCENDING);
        } else {
            setEventLogUsernameOrder(SortOrder.ASCENDING);
        }
    }

    public void sortByEventObject() {


        if (eventLogObjectOrder.equals(SortOrder.ASCENDING)) {
            setEventLogObjectOrder(SortOrder.DESCENDING);
        } else {
            setEventLogObjectOrder(SortOrder.ASCENDING);
        }
    }

    public void sortByTableName() {


        if (tableNameOrder.equals(SortOrder.ASCENDING)) {
            setTableNameOrder(SortOrder.DESCENDING);
        } else {
            setTableNameOrder(SortOrder.ASCENDING);
        }
    }

    public SortOrder getTableNameOrder() {
        return tableNameOrder;
    }

    public void setTableNameOrder(SortOrder tableNameOrder) {
        this.tableNameOrder = tableNameOrder;
    }

//    public Filter<?> getEventLogUsernameFilterImpl() {
//        return new Filter<EventLog>() {
//            public boolean accept(EventLog eventLog) {
//                return eventLog.getUsername() != null && (eventLogUsernameFilter == null || eventLogUsernameFilter.length() == 0 || eventLog.getUsername().toLowerCase().contains(eventLogUsernameFilter.toLowerCase()));
//            }
//        };
//    }

    public void resetPage() {
        setPage(1);
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public LazyDataModel<EventLog> getEventLogList() {
        return eventLogList;
    }

    public void setEventLogList(LazyDataModel<EventLog> eventLogList) {
        this.eventLogList = eventLogList;
    }

    public SortOrder getEventLogOperationOrder() {
        return eventLogOperationOrder;
    }

    public void setEventLogOperationOrder(SortOrder eventLogOperationOrder) {
        this.eventLogOperationOrder = eventLogOperationOrder;
    }

    public String getEventLognameFilter() {
        return eventLogOperationFilter;
    }

    public void setEventLognameFilter(String eventLognameFilter) {
        this.eventLogOperationFilter = eventLognameFilter;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getEventLogOperationFilter() {
        return eventLogOperationFilter;
    }

    public void setEventLogOperationFilter(String eventLogOperationFilter) {
        this.eventLogOperationFilter = eventLogOperationFilter;
    }

    public SortOrder getEventLogDateOrder() {
        return eventLogDateOrder;
    }

    public void setEventLogDateOrder(SortOrder eventLogDateOrder) {
        this.eventLogDateOrder = eventLogDateOrder;
    }

    public SortOrder getEventLogUsernameOrder() {
        return eventLogUsernameOrder;
    }

    public void setEventLogUsernameOrder(SortOrder eventLogUsernameOrder) {
        this.eventLogUsernameOrder = eventLogUsernameOrder;
    }

    public String getEventLogUsernameFilter() {
        return eventLogUsernameFilter;
    }

    public void setEventLogUsernameFilter(String eventLogUsernameFilter) {
        this.eventLogUsernameFilter = eventLogUsernameFilter;
    }

    public SortOrder getEventLogObjectOrder() {
        return eventLogObjectOrder;
    }

    public void setEventLogObjectOrder(SortOrder eventLogObjectOrder) {
        this.eventLogObjectOrder = eventLogObjectOrder;
    }

    public AccessControlAction getAccessControlAction() {
        return accessControlAction;
    }

    public void setAccessControlAction(AccessControlAction accessControlAction) {
        this.accessControlAction = accessControlAction;
    }

    public GeneralHelper getGeneralHelper() {
        return generalHelper;
    }

    public void setGeneralHelper(GeneralHelper generalHelper) {
        this.generalHelper = generalHelper;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public String getEventLogDateFilter() {
        return eventLogDateFilter;
    }

    public void setEventLogDateFilter(String eventLogDateFilter) {
        this.eventLogDateFilter = eventLogDateFilter;
    }
}