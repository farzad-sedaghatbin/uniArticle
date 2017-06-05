package ir.university.toosi.wtms.web.action.zone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.IReaderWrapperService;
import ir.ReaderWrapperService;
import ir.university.toosi.tms.model.service.rule.RulePackageServiceImpl;
import ir.university.toosi.tms.model.service.zone.CameraServiceImpl;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
import ir.university.toosi.tms.model.service.zone.PDPServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.person.HandlePersonAction;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.zone.*;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.SortOrder;


import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Named(value = "handlePDPAction")
@SessionScoped
public class HandlePDPAction implements Serializable {
    @Inject
    private UserManagementAction me;
    @Inject
    private HandleGatewayAction handleGatewayAction;
    @Inject
    private HandlePersonAction handlePersonAction;

    @EJB
    private PDPServiceImpl pdpService;
    @EJB
    private CameraServiceImpl cameraService;
    @EJB
    private GatewayServiceImpl gatewayService;
    @EJB
    private RulePackageServiceImpl rulePackageService;


    private String editable = "false";

    private List<PDP> pdpList = null;
    private DataModel<DeviceDataModel> pdpListModel = null;
    private List<DeviceDataModel> listModel = null;
    private List<Gateway> gatewayGrid = null;
    private String pdpName;
    private Gateway gateway;
    private PDP currentPdp = null;
    private PDP newPdp = null;
    private int page = 1;
    private List<PDP> selectedPdps = new ArrayList<>();
    private PDP selectedPdp;
    private boolean selectAll;
    private boolean fingers;
    private boolean schedule;
    private boolean db;
    private boolean pictures;
    private boolean pdpEnabled;
    private String description;
    private String descText;
    private String ip;
    private String gatewayId;
    private String cameraId;
    private SelectItem[] cameraItems;
    private SelectItem[] gatewayItems;
    private boolean selectRow = false;
    private boolean finger = false;
    private boolean online = false;
    private boolean entrance = false;
    private SortOrder pdpNameOrder = SortOrder.UNSORTED;
    private SortOrder pdpDescriptionOrder = SortOrder.UNSORTED;
    private SortOrder ipOrder = SortOrder.UNSORTED;
    private String pdpNameFilter;
    private String pdpDescriptionFilter;
    private String pdpIPFilter;
    private boolean disableFields;


    public void begin() {
//        me.setActiveMenu(MenuType.HARDWARE);
        refresh();
        me.redirect("/pdp/pdps.xhtml");
    }

    public String beginDevice() {
        refreshDevice();
        return "pdp-monitor";
    }

    public void selectPdps(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentPdp.setSelected(true);
            selectedPdps.add(currentPdp);
        } else {
            currentPdp.setSelected(false);
            for (PDP pdp : pdpList) {
                if (pdp.getId() == currentPdp.getId())
                    selectedPdps.remove(pdp);
            }
        }
    }

    public void changePdps(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            pdpEnabled = true;
        } else
            pdpEnabled = false;
    }

    public void changeOnline(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            online = true;
        } else
            online = false;
    }


    public void selectAllPdp(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (PDP pdp : pdpList) {
                pdp.setSelected(true);
                selectedPdps.add(pdp);
            }
        } else {
            for (PDP pdp : pdpList) {
                pdp.setSelected(false);
            }
            selectedPdps.clear();
        }
    }

    public List<PDP> getSelectionGrid() {
        List<PDP> pdps = new ArrayList<>();
        refresh();
        return pdpList;
    }

    private void refresh() {
        init();

        handleGatewayAction.setSelectedGateways(new HashSet<Gateway>());
        List<PDP> pdps = pdpService.getAllPDPs();
        for (PDP pdp : pdps) {
            pdp.setDescText(pdp.getDescription());
            pdp.setNameText(pdp.getName());
        }
        pdpList = pdps;
        for (PDP pdp : pdpList) {
            pdp.setSelected(false);
        }
        selectedPdps = new ArrayList<>();

        List<Gateway> gateways = gatewayService.getAllGateway();

        List<Camera> cameras = cameraService.getAllCamera();
        cameraItems = new SelectItem[cameras.size()];
        gatewayItems = new SelectItem[gateways.size()];
        int i = 0;
        for (Camera camera : cameras) {
            cameraItems[i++] = new SelectItem(camera.getId(), camera.getName());
        }
        i = 0;
        for (Gateway gateway1 : gateways) {
            gatewayItems[i++] = new SelectItem(gateway1.getId(), gateway1.getName());
        }

    }

    private void refreshDevice() {
//        init();

        handleGatewayAction.setSelectedGateways(new HashSet<Gateway>());
        List<PDP> pdps = pdpService.getAllPDPs();
        DeviceDataModel pdpDataModel = new DeviceDataModel();
        listModel = new ArrayList<>();
        for (PDP pdp : pdps) {
            pdpDataModel = new DeviceDataModel();
            pdpDataModel.setName(pdp.getName());
            pdpDataModel.setEnabled(pdp.isEnabled());
            pdpDataModel.setIp(pdp.getIp());
            pdpDataModel.setDescription(pdp.getDescription());
            listModel.add(pdpDataModel);
        }
        pdpListModel = new ListDataModel<>(listModel);


        ping();
    }

    public void ping() {
        for (DeviceDataModel pdpDataModel : listModel) {

            try {
                boolean status = pdpService.ping(pdpDataModel.getIp());
                if (status)
                    pdpDataModel.setEnabled(true);
                else
                    pdpDataModel.setEnabled(false);

            } catch (IOException e) {
                e.printStackTrace();
            }

            pdpListModel = new ListDataModel<>(listModel);

        }
    }

    public void add() {
        init();
        gatewayGrid = handleGatewayAction.getSelectionGrid();
        setEditable("false");
        setDisableFields(false);
        refresh();
    }

    public void synch() {
//        me.setActiveMenu(MenuType.SEND_RECEIVE);
        finger = false;
        refresh();
    }

    public void fingerPage() throws MalformedURLException {
//        me.setActiveMenu(MenuType.SEND_RECEIVE);
        finger = true;
        URL url = new URL("http://127.0.0.1:8081/ws?wsdl");

        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://ir/", "ReaderWrapperServiceService");

        Service service = Service.create(url, qname);

        IReaderWrapperService readerWrapperService = service.getPort(IReaderWrapperService.class);

       readerWrapperService.getUserList(1);

//        refresh();
//        me.redirect("person/selectlist-pdp.xhtml");
    }

    public void synchronize() {
        if (!finger) {
            PDPSync pdpSync = new PDPSync();
            pdpSync.setDb(db);
            pdpSync.setFinger(fingers);
            pdpSync.setPdpList(new HashSet<PDP>(selectedPdps));
            pdpSync.setPicture(pictures);
            pdpSync.setSchedule(schedule);
            pdpService.synchronizePdp(pdpSync);
        } else {
            pdpService.fingerPrint(new HashSet<PDP>(selectedPdps));
        }
        me.redirect("/");
    }

    public void synchronizeOneByOne() {
        PDPSync pdpSync = new PDPSync();
        pdpSync.setFinger(fingers);
        pdpSync.setPdpList(new HashSet<PDP>(selectedPdps));
        pdpSync.setPicture(pictures);
        pdpSync.setPerson(handlePersonAction.getCurrentPerson());
        pdpService.synchronizeOnePdp(pdpSync);
        me.redirect("/person/list-person.htm");
    }

    public void ruleInitialize() {
        rulePackageService.fillRulePackageHashTable();
    }

    public void doDelete() {

        currentPdp.setEffectorUser(me.getUsername());
        List<Gateway> gatewayList = null;
        gatewayList = gatewayService.getAllGateway();
        boolean flag = false;
        PDP removablePdp = null;
//        for (Gateway gateway1 : gatewayList) {
//            for (Camera camera : gateway1.getCameras()) {
//                if (camera.getId() == currentCamera.getId()) {
//                    flag = true;
//                    removableCamera = camera;
//                }
//            }
//            if (flag) {
//                flag = false;
//                gateway1.getCameras().remove(removableCamera);
//            }
//
//        }
        String condition = pdpService.deletePDP(currentPdp);
        refresh();
        me.addInfoMessage(condition);
        me.redirect("/pdp/pdps.xhtml");

    }


    public void init() {
        pdpName = "";
        descText = "";
        ip = "";
        page = 1;
        cameraId = null;
        selectAll = false;
        pdpEnabled = false;
        currentPdp = null;
        selectAll = false;
        fingers = false;
        schedule = false;
        db = false;
        pictures = false;
        pdpEnabled = false;
        setSelectRow(false);
        pdpDescriptionFilter = "";
        pdpIPFilter = "";
        pdpNameFilter = "";
    }

    public void edit() {
        refresh();
        setEditable("true");
        setDisableFields(false);
        pdpEnabled = currentPdp.isEnabled();
        ip = currentPdp.getIp();
        descText = currentPdp.getDescText();
        pdpName = currentPdp.getNameText();
        currentPdp = pdpService.findById(currentPdp.getId());
        if (currentPdp.getCamera() != null) {
            cameraId = String.valueOf(currentPdp.getCamera().getId());
        }
        gatewayId = String.valueOf(currentPdp.getGateway().getId());
        selectedPdp = currentPdp;
        entrance = currentPdp.isEntrance();
    }

    public void view() {
        setEditable("true");
        setDisableFields(true);
        pdpEnabled = currentPdp.isEnabled();
        ip = currentPdp.getIp();
        descText = currentPdp.getDescText();
        pdpName = currentPdp.getNameText();
        currentPdp = pdpService.findById(currentPdp.getId());
        if (currentPdp.getCamera() != null)
            cameraId = String.valueOf(currentPdp.getCamera().getId());
        gatewayId = String.valueOf(currentPdp.getGateway().getId());
        selectedPdp = currentPdp;
        entrance = currentPdp.isEntrance();
    }


    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();

        }

    }

    public void doEdit() {
        selectedPdp.setDescription(descText);
        selectedPdp.setName(pdpName);
        selectedPdp.setIp(ip);
        selectedPdp.setEnabled(pdpEnabled);
        selectedPdp.setEntrance(entrance);
        selectedPdp.setEffectorUser(me.getUsername());
        Gateway gateway = null;
        Camera camera = null;
        gateway = gatewayService.findById(Long.parseLong(gatewayId));
        if (cameraId != null && !cameraId.equalsIgnoreCase(""))
            camera = cameraService.findById(Long.parseLong(cameraId));

        boolean flag = false;
        selectedPdp.setGateway(gateway);
        selectedPdp.setCamera(camera);
        selectedPdp.setOnline(online);
        boolean condition = pdpService.exist(selectedPdp.getIp(), selectedPdp.getId());
        if (condition) {

            me.addInfoMessage("pdp.exist");
            return;
        }
        condition = pdpService.editPDP(selectedPdp);
        if (condition) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/pdp/pdps.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }

    }


    public void doAdd() {
        newPdp = new PDP();
        newPdp.setDescription(descText);
        newPdp.setName(pdpName);
        newPdp.setDeleted("0");
        newPdp.setEnabled(pdpEnabled);
        newPdp.setStatus("c");
        newPdp.setIp(ip);
        newPdp.setOnline(online);
        newPdp.setEntrance(entrance);
        newPdp.setEffectorUser(me.getUsername());
        Gateway gateway = null;
        Camera camera = null;
        gateway = gatewayService.findById(Long.parseLong(gatewayId));
        if (cameraId != null && !cameraId.equalsIgnoreCase(""))
            camera = cameraService.findById(Long.parseLong(cameraId));


        boolean flag = false;
        newPdp.setGateway(gateway);
        newPdp.setCamera(camera);
        boolean condition = pdpService.existNotId(newPdp.getIp());
        if (condition) {

            me.addInfoMessage("pdp.exist");
            return;
        }
        PDP insertedPdp = null;
        insertedPdp = pdpService.createPDP(newPdp);
        if (insertedPdp != null) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/pdp/pdps.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }


//    public Filter<?> getPDPIPFilterImpl() {
//        return new Filter<PDP>() {
//            public boolean accept(PDP pdp) {
//                return pdpIPFilter == null || pdpIPFilter.length() == 0 || pdp.getIp().startsWith(pdpIPFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getPdpNameFilterImpl() {
//        return new Filter<PDP>() {
//            public boolean accept(PDP pdp) {
//                return pdpNameFilter == null || pdpNameFilter.length() == 0 || pdp.getName().toLowerCase().contains(pdpNameFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getPdpDescriptionFilterImpl() {
//        return new Filter<PDP>() {
//            public boolean accept(PDP pdp) {
//                return pdpDescriptionFilter == null || pdpDescriptionFilter.length() == 0 || pdp.getDescription().toLowerCase().contains(pdpDescriptionFilter.toLowerCase());
//            }
//        };
//    }

    public void sortByPdpName() {
        pdpNameOrder = newSortOrder(pdpNameOrder);
    }

    public void sortByPdpDescription() {
        pdpDescriptionOrder = newSortOrder(pdpDescriptionOrder);
    }

    public void sortByIp() {
        ipOrder = newSortOrder(ipOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        pdpNameOrder = SortOrder.UNSORTED;
        pdpDescriptionOrder = SortOrder.UNSORTED;
        ipOrder = SortOrder.UNSORTED;

        if (currentSortOrder.equals(SortOrder.DESCENDING)) {
            return SortOrder.ASCENDING;
        } else {
            return SortOrder.DESCENDING;
        }

    }


    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public void setGatewayGrid(List<Gateway> gatewayGrid) {
        this.gatewayGrid = gatewayGrid;
    }


    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }


    public int getPage() {
        currentPdp = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public HandleGatewayAction getHandleGatewayAction() {
        return handleGatewayAction;
    }

    public void setHandleGatewayAction(HandleGatewayAction handleGatewayAction) {
        this.handleGatewayAction = handleGatewayAction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public String getDescText() {
        return descText;
    }

    public void setDescText(String descText) {
        this.descText = descText;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<PDP> getPdpList() {
        return pdpList;
    }

    public void setPdpList(List<PDP> pdpList) {
        this.pdpList = pdpList;
    }

    public String getPdpName() {
        return pdpName;
    }

    public void setPdpName(String pdpName) {
        this.pdpName = pdpName;
    }

    public PDP getCurrentPdp() {
        return currentPdp;
    }

    public void setCurrentPdp(PDP currentPdp) {
        this.currentPdp = currentPdp;
    }

    public PDP getNewPdp() {
        return newPdp;
    }

    public void setNewPdp(PDP newPdp) {
        this.newPdp = newPdp;
    }

    public List<PDP> getSelectedPdps() {
        return selectedPdps;
    }

    public void setSelectedPdps(List<PDP> selectedPdps) {
        this.selectedPdps = selectedPdps;
    }

    public PDP getSelectedPdp() {
        return selectedPdp;
    }

    public void setSelectedPdp(PDP selectedPdp) {
        this.selectedPdp = selectedPdp;
    }

    public boolean isPdpEnabled() {
        return pdpEnabled;
    }

    public void setPdpEnabled(boolean pdpEnabled) {
        this.pdpEnabled = pdpEnabled;
    }

    public SelectItem[] getCameraItems() {
        return cameraItems;
    }

    public void setCameraItems(SelectItem[] cameraItems) {
        this.cameraItems = cameraItems;
    }

    public SelectItem[] getGatewayItems() {
        return gatewayItems;
    }

    public void setGatewayItems(SelectItem[] gatewayItems) {
        this.gatewayItems = gatewayItems;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isFinger() {
        return finger;
    }

    public void setFinger(boolean finger) {
        this.finger = finger;
    }

    public SortOrder getPdpNameOrder() {
        return pdpNameOrder;
    }

    public void setPdpNameOrder(SortOrder pdpNameOrder) {
        this.pdpNameOrder = pdpNameOrder;
    }

    public String getPdpNameFilter() {
        return pdpNameFilter;
    }

    public void setPdpNameFilter(String pdpNameFilter) {
        this.pdpNameFilter = pdpNameFilter;
    }

    public SortOrder getPdpDescriptionOrder() {
        return pdpDescriptionOrder;
    }

    public void setPdpDescriptionOrder(SortOrder pdpDescriptionOrder) {
        this.pdpDescriptionOrder = pdpDescriptionOrder;
    }

    public String getPdpDescriptionFilter() {
        return pdpDescriptionFilter;
    }

    public void setPdpDescriptionFilter(String pdpDescriptionFilter) {
        this.pdpDescriptionFilter = pdpDescriptionFilter;
    }

    public boolean isFingers() {
        return fingers;
    }

    public void setFingers(boolean fingers) {
        this.fingers = fingers;
    }

    public boolean isSchedule() {
        return schedule;
    }

    public void setSchedule(boolean schedule) {
        this.schedule = schedule;
    }

    public boolean isDb() {
        return db;
    }

    public void setDb(boolean db) {
        this.db = db;
    }

    public boolean isPictures() {
        return pictures;
    }

    public void setPictures(boolean pictures) {
        this.pictures = pictures;
    }

    public DataModel<DeviceDataModel> getPdpListModel() {
        return pdpListModel;
    }

    public void setPdpListModel(DataModel<DeviceDataModel> pdpListModel) {
        this.pdpListModel = pdpListModel;
    }

    public List<DeviceDataModel> getListModel() {
        return listModel;
    }

    public void setListModel(List<DeviceDataModel> listModel) {
        this.listModel = listModel;
    }

    public boolean isEntrance() {
        return entrance;
    }

    public void setEntrance(boolean entrance) {
        this.entrance = entrance;
    }

    public SortOrder getIpOrder() {
        return ipOrder;
    }

    public void setIpOrder(SortOrder ipOrder) {
        this.ipOrder = ipOrder;
    }

    public HandlePersonAction getHandlePersonAction() {
        return handlePersonAction;
    }

    public void setHandlePersonAction(HandlePersonAction handlePersonAction) {
        this.handlePersonAction = handlePersonAction;
    }

    public String getPdpIPFilter() {
        return pdpIPFilter;
    }

    public void setPdpIPFilter(String pdpIPFilter) {
        this.pdpIPFilter = pdpIPFilter;
    }

    public boolean isDisableFields() {
        return disableFields;
    }

    public void setDisableFields(boolean disableFields) {
        this.disableFields = disableFields;
    }
}

