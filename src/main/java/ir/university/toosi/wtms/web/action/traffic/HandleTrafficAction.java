package ir.university.toosi.wtms.web.action.traffic;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import ir.university.toosi.tms.model.service.TrafficLogServiceImpl;
import ir.university.toosi.tms.util.Configuration;
import ir.university.toosi.wtms.web.action.AccessControlAction;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.entity.TrafficLogDataModel;
import ir.university.toosi.wtms.web.util.CalendarUtil;
//import ir.university.toosi.tms.util.Configuration;
import ir.university.toosi.wtms.web.util.LangUtil;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;


import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleTrafficAction")
@SessionScoped
public class HandleTrafficAction implements Serializable {
    @Inject
    private UserManagementAction me;
    @Inject
    private GeneralHelper generalHelper;
    @Inject
    private AccessControlAction accessControlAction;

    @EJB
    private TrafficLogServiceImpl trafficLogService;
    TrafficLogDataModel trafficLog;
    private LazyDataModel<TrafficLog> eventLogList = null;
    private SortOrder eventLogOperationOrder = SortOrder.DESCENDING;
    private SortOrder eventLogDateOrder = SortOrder.DESCENDING;
    private SortOrder eventLogUsernameOrder = SortOrder.DESCENDING;
    private String eventLogOperationFilter;
    private String eventLogDateFilter;
    private String eventLogUsernameFilter;
    private String dateFilter;
    private String fromDate;
    private String toDate;
    private int page = 1;
    private int index = 0;
    TrafficLog currentTrraficLog;
    private boolean selectRow = false;

    private SortOrder gatewayNameOrder = SortOrder.UNSORTED;
    private String gatewayNameFilter;

    public void begin() {
        me.setActiveMenu(MenuType.REPORT);
        refresh();
//
        fromDate = CalendarUtil.getPersianDateWithoutSlash(new Locale("fa"));
        toDate = CalendarUtil.getPersianDateWithoutSlash(new Locale("fa"));
        search();
//        refresh();
        me.redirect("/traffic/traffic-log.xhtml");

    }


    public void search() {
        eventLogList = new TrafficLazyDataModel(trafficLogService);
        List<TrafficLogDataModel> logDataModels = new ArrayList<>();

        for (TrafficLog log : new TrafficLazyDataModel(trafficLogService)) {
            TrafficLogDataModel dataModel = new TrafficLogDataModel();
            dataModel.setVideo(log.getVideo());
            dataModel.setTime(LangUtil.getFarsiNumber(log.getTime()));
            dataModel.setDate(log.getDate());
            dataModel.setExit(log.isExit());
            dataModel.setGate(log.getGateway().getName());
            dataModel.setPictures(log.getPictures());
            dataModel.setValid(log.isValid());
            dataModel.setId(log.getId());
            dataModel.setName(log.getPerson().getName() + "  " + log.getPerson().getLastName());
            logDataModels.add(dataModel);

        }
//        eventLogList = Lists.reverse(logDataModels) ;
        dateFilter = "";
    }

    public void increase() {
        index++;
    }

    public void decrease() {
        index--;
    }

    private void refresh() {
        index = 0;
        page = 1;
        selectRow = false;
//
    }


/*
    public Filter<?> getUserNameFilterImpl() {
        return new Filter<TrafficLogDataModel>() {
            public boolean accept(TrafficLogDataModel trafficLog) {
                return eventLogUsernameFilter == null || eventLogUsernameFilter.length() == 0 || trafficLog.getName().toLowerCase().contains(eventLogUsernameFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getDateFilterImpl() {
        return new Filter<TrafficLogDataModel>() {
            public boolean accept(TrafficLogDataModel trafficLog) {
                return dateFilter == null || dateFilter.length() == 0 || trafficLog.getDate().equals(dateFilter);
            }
        };
    }
*/

    public void sortByTrafficLogUsername() {
        if (eventLogUsernameOrder.equals(SortOrder.ASCENDING)) {
            setTrafficLogUsernameOrder(SortOrder.DESCENDING);
        } else {
            setTrafficLogUsernameOrder(SortOrder.ASCENDING);
        }
    }

/*    public Filter<?> getGatewayeNameFilterImpl() {
        return new Filter<TrafficLogDataModel>() {
            public boolean accept(TrafficLogDataModel trafficLog) {
                return gatewayNameFilter == null || gatewayNameFilter.length() == 0 || trafficLog.getGate().toLowerCase().contains(gatewayNameFilter.toLowerCase());
            }
        };
    }*/

    public void sortByGatewayeName() {
        if (gatewayNameOrder.equals(SortOrder.ASCENDING)) {
            setGatewayNameOrder(SortOrder.DESCENDING);
        } else {
            setGatewayNameOrder(SortOrder.ASCENDING);
        }
    }


    public SortOrder getTrafficLogOperationOrder() {
        return eventLogOperationOrder;
    }

    public void setTrafficLogOperationOrder(SortOrder eventLogOperationOrder) {
        this.eventLogOperationOrder = eventLogOperationOrder;
    }

    public String getTrafficLognameFilter() {
        return eventLogOperationFilter;
    }

    public void setTrafficLognameFilter(String eventLognameFilter) {
        this.eventLogOperationFilter = eventLognameFilter;
    }

    public void selectForEdit() {
        index = 0;
//        currentTrraficLog = eventLogList.getRowData();
        setSelectRow(true);
    }

    public void paint(OutputStream stream, Object object) {
        Long traffic = (Long) object;

        TrafficLog trafficLog1 = null;
        trafficLog1 = trafficLogService.findById(traffic);
        if (trafficLog1 != null) {
            String address = trafficLog1.getPictures();
            if (address == null)
                return;
            address = address + "/" + index + ".jpg";
            try {
                stream.write(Files.readAllBytes(Paths.get(Configuration.getProperty("pic.person") + address)));
                stream.flush();
                stream.close();
            } catch (IOException e) {
                return;
            }

        }
    }

    public void painter(OutputStream stream, Object object) throws IOException {
        if (currentTrraficLog != null) {
            String address = currentTrraficLog.getPictures();
            if (address == null)
                return;
            address = address + "/" + index + ".jpg";
            try {
                stream.write(Files.readAllBytes(Paths.get(Configuration.getProperty("pic.person") + address)));
                stream.flush();
                stream.close();
            } catch (IOException e) {
                index = 0;
                painter(stream, object);
                return;
            }

        }
    }

    public void resetPage() {
        setPage(1);
    }

    public int getPage() {

        return page;
    }

    public int getIndex() {

        return index;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public String getTrafficLogOperationFilter() {
        return eventLogOperationFilter;
    }

    public void setTrafficLogOperationFilter(String eventLogOperationFilter) {
        this.eventLogOperationFilter = eventLogOperationFilter;
    }

    public SortOrder getTrafficLogDateOrder() {
        return eventLogDateOrder;
    }

    public void setTrafficLogDateOrder(SortOrder eventLogDateOrder) {
        this.eventLogDateOrder = eventLogDateOrder;
    }

    public SortOrder getTrafficLogUsernameOrder() {
        return eventLogUsernameOrder;
    }

    public void setTrafficLogUsernameOrder(SortOrder eventLogUsernameOrder) {
        this.eventLogUsernameOrder = eventLogUsernameOrder;
    }

    public String getTrafficLogUsernameFilter() {
        return eventLogUsernameFilter;
    }

    public void setTrafficLogUsernameFilter(String eventLogUsernameFilter) {
        this.eventLogUsernameFilter = eventLogUsernameFilter;
    }

    public String getTrafficLogDateFilter() {
        return eventLogDateFilter;
    }

    public void setTrafficLogDateFilter(String eventLogDateFilter) {
        this.eventLogDateFilter = eventLogDateFilter;
    }

    public SortOrder getGatewayNameOrder() {
        return gatewayNameOrder;
    }

    public void setGatewayNameOrder(SortOrder gatewayNameOrder) {
        this.gatewayNameOrder = gatewayNameOrder;
    }

    public String getGatewayNameFilter() {
        return gatewayNameFilter;
    }

    public void setGatewayNameFilter(String gatewayNameFilter) {
        this.gatewayNameFilter = gatewayNameFilter;
    }

    public TrafficLogDataModel getTrafficLog() {
        return trafficLog;
    }

    public void setTrafficLog(TrafficLogDataModel trafficLog) {
        this.trafficLog = trafficLog;
    }


    public LazyDataModel<TrafficLog> getEventLogList() {
        return eventLogList;
    }

    public void setEventLogList(LazyDataModel<TrafficLog> eventLogList) {
        this.eventLogList = eventLogList;
    }

    public SortOrder getEventLogOperationOrder() {
        return eventLogOperationOrder;
    }

    public void setEventLogOperationOrder(SortOrder eventLogOperationOrder) {
        this.eventLogOperationOrder = eventLogOperationOrder;
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

    public String getEventLogOperationFilter() {
        return eventLogOperationFilter;
    }

    public void setEventLogOperationFilter(String eventLogOperationFilter) {
        this.eventLogOperationFilter = eventLogOperationFilter;
    }

    public String getEventLogDateFilter() {
        return eventLogDateFilter;
    }

    public void setEventLogDateFilter(String eventLogDateFilter) {
        this.eventLogDateFilter = eventLogDateFilter;
    }

    public String getEventLogUsernameFilter() {
        return eventLogUsernameFilter;
    }

    public void setEventLogUsernameFilter(String eventLogUsernameFilter) {
        this.eventLogUsernameFilter = eventLogUsernameFilter;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public GeneralHelper getGeneralHelper() {
        return generalHelper;
    }

    public void setGeneralHelper(GeneralHelper generalHelper) {
        this.generalHelper = generalHelper;
    }

    public AccessControlAction getAccessControlAction() {
        return accessControlAction;
    }

    public void setAccessControlAction(AccessControlAction accessControlAction) {
        this.accessControlAction = accessControlAction;
    }

    public TrafficLog getCurrentTrraficLog() {
        return currentTrraficLog;
    }

    public void setCurrentTrraficLog(TrafficLog currentTrraficLog) {
        this.currentTrraficLog = currentTrraficLog;
    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(String dateFilter) {
        this.dateFilter = dateFilter.replace("/", "");
    }

    public StreamedContent getPic() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        String address = myRequest.getParameter("address");
        if (address == null)
            return new DefaultStreamedContent();
        address = address + "/" + 1 + ".jpg";
        try {
            return new DefaultStreamedContent(new FileInputStream(new File(Configuration.getProperty("pic.person") + address)), "image/jpeg");
        } catch (IOException e) {
//                e.printStackTrace();
        }
        return null;
    }
}
