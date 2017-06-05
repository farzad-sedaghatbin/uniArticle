package ir.university.toosi.parking.action;


import com.google.common.collect.Lists;
import ir.ReaderWrapperService;
import ir.university.toosi.parking.entity.ParkingLog;
import ir.university.toosi.parking.service.ParkingLogServiceImpl;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.util.Configuration;
import ir.university.toosi.wtms.web.action.AccessControlAction;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.monitoring.HandleMonitoringAction;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.wtms.web.util.CalendarUtil;
import ir.university.toosi.wtms.web.util.LangUtil;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//import ir.university.toosi.tms.util.Configuration;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleParkingAction")
@SessionScoped
public class HandleParkingAction implements Serializable {
    @Inject
    private UserManagementAction me;
    @Inject
    private GeneralHelper generalHelper;
    @Inject
    private AccessControlAction accessControlAction;

    @EJB
    private ParkingLogServiceImpl ParkingLogService;
    ParkingLogDataModel parkingLog;
    private LazyDataModel<ParkingLog> eventLogList = null;
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
    ParkingLog currentTrraficLog;
    private boolean selectRow = false;

    private SortOrder gatewayNameOrder = SortOrder.UNSORTED;
    private String gatewayNameFilter;


    @Inject
    private HandleMonitoringAction monitoringAction;

    public void test() {
        ReaderWrapperService service = new ReaderWrapperService();
        service.setParkingLogService(ParkingLogService);
        service.setMonitoringAction(monitoringAction);
        service.sendParking("a123b", new byte[]{102});
    }

    public void begin() {
        me.setActiveMenu(MenuType.REPORT);
        refresh();
//
        fromDate = CalendarUtil.getPersianDateWithoutSlash(new Locale("fa"));
        toDate = CalendarUtil.getPersianDateWithoutSlash(new Locale("fa"));
        search();
//        refresh();
        me.redirect("/parking/parking-log.xhtml");

    }


    public void search() {
        eventLogList = new ParkingLazyDataModel(ParkingLogService);
        List<ParkingLogDataModel> logDataModels = new ArrayList<>();

        for (ParkingLog log : new ParkingLazyDataModel(ParkingLogService)) {
            ParkingLogDataModel dataModel = new ParkingLogDataModel();
            dataModel.setTime(LangUtil.getFarsiNumber(log.getTraffic_time()));
            dataModel.setDate(log.getTraffic_date());
            dataModel.setPictures(log.getPictures());
            dataModel.setId(log.getId());
            dataModel.setNumber(log.getNumber());
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
        return new Filter<ParkingLogDataModel>() {
            public boolean accept(ParkingLogDataModel ParkingLog) {
                return eventLogUsernameFilter == null || eventLogUsernameFilter.length() == 0 || ParkingLog.getName().toLowerCase().contains(eventLogUsernameFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getDateFilterImpl() {
        return new Filter<ParkingLogDataModel>() {
            public boolean accept(ParkingLogDataModel ParkingLog) {
                return dateFilter == null || dateFilter.length() == 0 || ParkingLog.getDate().equals(dateFilter);
            }
        };
    }
*/

    public void sortByParkingLogUsername() {
        if (eventLogUsernameOrder.equals(SortOrder.ASCENDING)) {
            setParkingLogUsernameOrder(SortOrder.DESCENDING);
        } else {
            setParkingLogUsernameOrder(SortOrder.ASCENDING);
        }
    }

/*    public Filter<?> getGatewayeNameFilterImpl() {
        return new Filter<ParkingLogDataModel>() {
            public boolean accept(ParkingLogDataModel ParkingLog) {
                return gatewayNameFilter == null || gatewayNameFilter.length() == 0 || ParkingLog.getGate().toLowerCase().contains(gatewayNameFilter.toLowerCase());
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


    public SortOrder getParkingLogOperationOrder() {
        return eventLogOperationOrder;
    }

    public void setParkingLogOperationOrder(SortOrder eventLogOperationOrder) {
        this.eventLogOperationOrder = eventLogOperationOrder;
    }

    public String getParkingLognameFilter() {
        return eventLogOperationFilter;
    }

    public void setParkingLognameFilter(String eventLognameFilter) {
        this.eventLogOperationFilter = eventLognameFilter;
    }

    public void selectForEdit() {
        index = 0;
//        currentTrraficLog = eventLogList.getRowData();
        setSelectRow(true);
    }

    public void paint(OutputStream stream, Object object) {
        Long Park = (Long) object;

        ParkingLog ParkingLog1 = null;
        ParkingLog1 = ParkingLogService.findById(Park);
        if (ParkingLog1 != null) {
            String address = ParkingLog1.getPictures();
            if (address == null)
                return;
            address = address + "/" + index + ".jpg";
            try {
                stream.write(Files.readAllBytes(Paths.get(Configuration.getProperty("pic.parking") + address)));
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
                stream.write(Files.readAllBytes(Paths.get(Configuration.getProperty("pic.parking") + address)));
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


    public String getParkingLogOperationFilter() {
        return eventLogOperationFilter;
    }

    public void setParkingLogOperationFilter(String eventLogOperationFilter) {
        this.eventLogOperationFilter = eventLogOperationFilter;
    }

    public SortOrder getParkingLogDateOrder() {
        return eventLogDateOrder;
    }

    public void setParkingLogDateOrder(SortOrder eventLogDateOrder) {
        this.eventLogDateOrder = eventLogDateOrder;
    }

    public SortOrder getParkingLogUsernameOrder() {
        return eventLogUsernameOrder;
    }

    public void setParkingLogUsernameOrder(SortOrder eventLogUsernameOrder) {
        this.eventLogUsernameOrder = eventLogUsernameOrder;
    }

    public String getParkingLogUsernameFilter() {
        return eventLogUsernameFilter;
    }

    public void setParkingLogUsernameFilter(String eventLogUsernameFilter) {
        this.eventLogUsernameFilter = eventLogUsernameFilter;
    }

    public String getParkingLogDateFilter() {
        return eventLogDateFilter;
    }

    public void setParkingLogDateFilter(String eventLogDateFilter) {
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

    public ParkingLogDataModel getParkingLog() {
        return parkingLog;
    }

    public void setParkingLog(ParkingLogDataModel parkingLog) {
        this.parkingLog = parkingLog;
    }


    public LazyDataModel<ParkingLog> getEventLogList() {
        return eventLogList;
    }

    public void setEventLogList(LazyDataModel<ParkingLog> eventLogList) {
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

    public ParkingLog getCurrentTrraficLog() {
        return currentTrraficLog;
    }

    public void setCurrentTrraficLog(ParkingLog currentTrraficLog) {
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
            return new DefaultStreamedContent(new FileInputStream(new File(Configuration.getProperty("pic.parking")  + address)), "image/jpeg");
        } catch (IOException e) {
//                e.printStackTrace();
        }
        return null;
    }

}
