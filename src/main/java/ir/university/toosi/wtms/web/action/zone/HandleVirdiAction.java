package ir.university.toosi.wtms.web.action.zone;

import ir.IReaderWrapperService;
import ir.ReaderWrapperService;
import ir.university.toosi.tms.model.entity.zone.*;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.rule.RulePackageServiceImpl;
import ir.university.toosi.tms.model.service.zone.CameraServiceImpl;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
import ir.university.toosi.tms.model.service.zone.VirdiServiceImpl;
import ir.university.toosi.tms.readerwrapper.Person;
import ir.university.toosi.tms.readerwrapper.PersonHolder;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.person.HandlePersonAction;
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


@Named(value = "handleVirdiAction")
@SessionScoped
public class HandleVirdiAction implements Serializable {
    @Inject
    private UserManagementAction me;
    @Inject
    private HandleGatewayAction handleGatewayAction;
    @Inject
    private HandlePersonAction handlePersonAction;

    @EJB
    private VirdiServiceImpl virdiService;
    @EJB
    private CameraServiceImpl cameraService;
    @EJB
    private GatewayServiceImpl gatewayService;
    @EJB
    private RulePackageServiceImpl rulePackageService;
    @EJB
    private ReaderWrapperService readerWrapperService;
    @EJB
    private PersonServiceImpl personService;

    private String editable = "false";

    private List<Virdi> virdiList = null;
    private DataModel<DeviceDataModel> virdiListModel = null;
    private List<DeviceDataModel> listModel = null;
    private List<Gateway> gatewayGrid = null;
    private String virdiName;
    private Gateway gateway;
    private Virdi currentVirdi = null;
    private Virdi newVirdi = null;
    private int page = 1;
    private Set<Virdi> selectedVirdis = new HashSet<>();
    private Virdi selectedVirdi;
    private boolean selectAll;
    private boolean fingers;
    private boolean schedule;
    private boolean db;
    private boolean pictures;
    private boolean virdiEnabled;
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
    private SortOrder virdiNameOrder = SortOrder.UNSORTED;
    private SortOrder virdiDescriptionOrder = SortOrder.UNSORTED;
    private SortOrder ipOrder = SortOrder.UNSORTED;
    private String virdiNameFilter;
    private String virdiDescriptionFilter;
    private String virdiIPFilter;
    private boolean disableFields;
    private int terminalId;


    public void begin() {
//        me.setActiveMenu(MenuType.HARDWARE);
        refresh();
        me.redirect("/virdi/virdi.xhtml");
    }

    public void selectVirdis(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentVirdi.setSelected(true);
            selectedVirdis.add(currentVirdi);
        } else {
            currentVirdi.setSelected(false);
            for (Virdi virdi : virdiList) {
                if (virdi.getId() == currentVirdi.getId())
                    selectedVirdis.remove(virdi);
            }
        }
    }

    public void changeVirdis(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            virdiEnabled = true;
        } else
            virdiEnabled = false;
    }

    public void changeOnline(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            online = true;
        } else
            online = false;
    }


    public void selectAllVirdi(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (Virdi virdi : virdiList) {
                virdi.setSelected(true);
                selectedVirdis.add(virdi);
            }
        } else {
            for (Virdi virdi : virdiList) {
                virdi.setSelected(false);
            }
            selectedVirdis.clear();
        }
    }

    public List<Virdi> getSelectionGrid() {
        List<Virdi> virdis = new ArrayList<>();
        refresh();
        return virdiList;
    }

    private void refresh() {
        init();

        handleGatewayAction.setSelectedGateways(new HashSet<Gateway>());
        List<Virdi> virdis = virdiService.getAllVirdis();
        for (Virdi virdi : virdis) {
            virdi.setDescText(virdi.getDescription());
            virdi.setNameText(virdi.getName());
        }
        virdiList = virdis;
        for (Virdi virdi : virdiList) {
            virdi.setSelected(false);
        }
        selectedVirdis = new HashSet<>();

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
        List<Virdi> virdis = virdiService.getAllVirdis();
        DeviceDataModel virdiDataModel = new DeviceDataModel();
        listModel = new ArrayList<>();
        for (Virdi virdi : virdis) {
            virdiDataModel = new DeviceDataModel();
            virdiDataModel.setName(virdi.getName());
            virdiDataModel.setEnabled(virdi.isEnabled());
            virdiDataModel.setIp(virdi.getIp());
            virdiDataModel.setDescription(virdi.getDescription());
            listModel.add(virdiDataModel);
        }
        virdiListModel = new ListDataModel<>(listModel);


        ping();
    }

    public void ping() {
        for (DeviceDataModel virdiDataModel : listModel) {

            try {
                boolean status = virdiService.ping(virdiDataModel.getIp());
                if (status)
                    virdiDataModel.setEnabled(true);
                else
                    virdiDataModel.setEnabled(false);

            } catch (IOException e) {
                e.printStackTrace();
            }

            virdiListModel = new ListDataModel<>(listModel);

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

    public void finger() {
//        me.setActiveMenu(MenuType.SEND_RECEIVE);
        finger = true;
        refresh();
    }

    public void synchronize() {
        if (!finger) {
            VirdiSync virdiSync = new VirdiSync();
            virdiSync.setDb(db);
            virdiSync.setFinger(fingers);
            virdiSync.setVirdiList(selectedVirdis);
            virdiSync.setPicture(pictures);
            virdiSync.setSchedule(schedule);
            virdiService.synchronizeVirdi(virdiSync);
        } else {
            virdiService.fingerPrint(selectedVirdis);
        }
        me.redirect("/home.htm");
    }

    public void synchronizeOneByOne() {
        VirdiSync virdiSync = new VirdiSync();
        virdiSync.setFinger(fingers);
        virdiSync.setVirdiList(selectedVirdis);
        virdiSync.setPicture(pictures);
        virdiSync.setPerson(handlePersonAction.getCurrentPerson());
        virdiService.synchronizeOneVirdi(virdiSync);
        me.redirect("/person/list-person.htm");
    }

    public void ruleInitialize() {
        rulePackageService.fillRulePackageHashTable();
    }

    public void doDelete() {

        currentVirdi.setEffectorUser(me.getUsername());
        List<Gateway> gatewayList = null;
        gatewayList = gatewayService.getAllGateway();
        boolean flag = false;
        Virdi removableVirdi = null;
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
        String condition = virdiService.deleteVirdi(currentVirdi);
        refresh();
        me.addInfoMessage(condition);
        me.redirect("/virdi/virdi.xhtml");

    }


    public void init() {
        virdiName = "";
        descText = "";
        ip = "";
        page = 1;
        cameraId = null;
        selectAll = false;
        virdiEnabled = false;
        currentVirdi = null;
        selectAll = false;
        fingers = false;
        schedule = false;
        db = false;
        pictures = false;
        virdiEnabled = false;
        setSelectRow(false);
        virdiDescriptionFilter = "";
        virdiIPFilter = "";
        virdiNameFilter = "";
    }

    public void offline() throws MalformedURLException {
//        readerWrapperService.getUserList(currentVirdi.getTerminalId());
        URL url = new URL("http://127.0.0.1:8081/ws?wsdl");
        QName qname = new QName("http://ir/", "ReaderWrapperServiceService");

        Service service = Service.create(url, qname);

        IReaderWrapperService readerWrapperService = service.getPort(IReaderWrapperService.class);
        readerWrapperService.GetAccessEventData(currentVirdi.getTerminalId());
        me.redirect("/virdi/virdi.xhtml");
    }
   public void fetch() throws MalformedURLException {
//        readerWrapperService.getUserList(currentVirdi.getTerminalId());
        URL url = new URL("http://127.0.0.1:8081/ws?wsdl");
        QName qname = new QName("http://ir/", "ReaderWrapperServiceService");

        Service service = Service.create(url, qname);

        IReaderWrapperService readerWrapperService = service.getPort(IReaderWrapperService.class);
        readerWrapperService.getUserList(currentVirdi.getTerminalId());
        me.addInfoMessage("fetch_from_virdi_completed");
        me.redirect("/virdi/virdi.xhtml");
    }

    public void synchSetUsers() throws MalformedURLException {
//        readerWrapperService.se(currentVirdi.getTerminalId());
        URL url = new URL("http://127.0.0.1:8081/ws?wsdl");
        QName qname = new QName("http://ir/", "ReaderWrapperServiceService");

        Service service = Service.create(url, qname);

        IReaderWrapperService readerWrapperService = service.getPort(IReaderWrapperService.class);
        PersonHolder personHolder= new PersonHolder();
        List<ir.university.toosi.tms.model.entity.personnel.Person> persons = personService.getAllPerson();
        Person[] p= new Person[persons.size()];
        int i=0;
        for (ir.university.toosi.tms.model.entity.personnel.Person person : persons) {
            Person person1= new Person();
            person1.setEmplymentCode(person.getPersonnelNo());
            person1.setUserId(Integer.parseInt(person.getPersonOtherId()));
            person1.setUserName(person.getName());
            p[i++]=person1;
        }
        personHolder.setPersons(p);
        readerWrapperService.addUserInfo(currentVirdi.getTerminalId(), personHolder);
        me.addInfoMessage("sync_to_virdi_completed");
        me.redirect("/virdi/virdi.xhtml");
    }

    public void edit() {
        setEditable("true");
        setDisableFields(false);
        virdiEnabled = currentVirdi.isEnabled();
        ip = currentVirdi.getIp();
        descText = currentVirdi.getDescText();
        virdiName = currentVirdi.getNameText();
        terminalId = currentVirdi.getTerminalId();
        currentVirdi = virdiService.findById(currentVirdi.getId());
        if (currentVirdi.getCamera() != null)
            cameraId = String.valueOf(currentVirdi.getCamera().getId());
        gatewayId = String.valueOf(currentVirdi.getGateway().getId());
        selectedVirdi = currentVirdi;
        entrance = currentVirdi.isEntrance();
    }

    public void view() {
        setEditable("true");
        setDisableFields(true);
        virdiEnabled = currentVirdi.isEnabled();
        ip = currentVirdi.getIp();
        descText = currentVirdi.getDescText();
        terminalId = currentVirdi.getTerminalId();
        virdiName = currentVirdi.getNameText();
        currentVirdi = virdiService.findById(currentVirdi.getId());
        if (currentVirdi.getCamera() != null)
            cameraId = String.valueOf(currentVirdi.getCamera().getId());
        gatewayId = String.valueOf(currentVirdi.getGateway().getId());
        selectedVirdi = currentVirdi;
        entrance = currentVirdi.isEntrance();
    }


    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();

        }

    }

    public void doEdit() {
        selectedVirdi.setDescription(descText);
        selectedVirdi.setName(virdiName);
        selectedVirdi.setIp(ip);
        selectedVirdi.setTerminalId(terminalId);
        selectedVirdi.setEnabled(virdiEnabled);
        selectedVirdi.setEntrance(entrance);
        selectedVirdi.setEffectorUser(me.getUsername());
        Gateway gateway = null;
        Camera camera = null;
        gateway = gatewayService.findById(Long.parseLong(gatewayId));
        if (cameraId != null && !cameraId.equalsIgnoreCase(""))
            camera = cameraService.findById(Long.parseLong(cameraId));

        boolean flag = false;
        selectedVirdi.setGateway(gateway);
        selectedVirdi.setCamera(camera);
        selectedVirdi.setOnline(online);
        if (ip != null && ip.length() > 0) {
            boolean condition = virdiService.exist(selectedVirdi.getIp(), selectedVirdi.getId());
            if (condition) {

                me.addInfoMessage("virdi.exist");
                me.redirect("/virdi/virdi.xhtml");
            }
        }
        boolean condition = virdiService.editVirdi(selectedVirdi);
        if (condition) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/virdi/virdi.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }

    }


    public void doAdd() {
        newVirdi = new Virdi();
        newVirdi.setDescription(descText);
        newVirdi.setName(virdiName);
        newVirdi.setDeleted("0");
        newVirdi.setEnabled(virdiEnabled);
        newVirdi.setStatus("c");
        newVirdi.setIp(ip);
        newVirdi.setTerminalId(terminalId);
        newVirdi.setOnline(online);
        newVirdi.setEntrance(entrance);
        newVirdi.setEffectorUser(me.getUsername());
        Gateway gateway = null;
        Camera camera = null;
        gateway = gatewayService.findById(Long.parseLong(gatewayId));
        if (cameraId != null && !cameraId.equalsIgnoreCase(""))
            camera = cameraService.findById(Long.parseLong(cameraId));


        boolean flag = false;
        newVirdi.setGateway(gateway);
        newVirdi.setCamera(camera);
        if (ip != null && ip.length() > 0) {
            boolean condition = virdiService.existNotId(newVirdi.getIp());
            if (condition) {

                me.addInfoMessage("virdi.exist");
                me.redirect("/virdi/virdi.xhtml");
            }
        }
        Virdi insertedVirdi = null;
        insertedVirdi = virdiService.createVirdi(newVirdi);
        if (insertedVirdi != null) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/virdi/virdi.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }


//    public Filter<?> getVirdiIPFilterImpl() {
//        return new Filter<Virdi>() {
//            public boolean accept(Virdi virdi) {
//                return virdiIPFilter == null || virdiIPFilter.length() == 0 || virdi.getIp().startsWith(virdiIPFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getVirdiNameFilterImpl() {
//        return new Filter<Virdi>() {
//            public boolean accept(Virdi virdi) {
//                return virdiNameFilter == null || virdiNameFilter.length() == 0 || virdi.getName().toLowerCase().contains(virdiNameFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getVirdiDescriptionFilterImpl() {
//        return new Filter<Virdi>() {
//            public boolean accept(Virdi virdi) {
//                return virdiDescriptionFilter == null || virdiDescriptionFilter.length() == 0 || virdi.getDescription().toLowerCase().contains(virdiDescriptionFilter.toLowerCase());
//            }
//        };
//    }

    public void sortByVirdiName() {
        virdiNameOrder = newSortOrder(virdiNameOrder);
    }

    public void sortByVirdiDescription() {
        virdiDescriptionOrder = newSortOrder(virdiDescriptionOrder);
    }

    public void sortByIp() {
        ipOrder = newSortOrder(ipOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        virdiNameOrder = SortOrder.UNSORTED;
        virdiDescriptionOrder = SortOrder.UNSORTED;
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
        currentVirdi = null;
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

    public List<Virdi> getVirdiList() {
        return virdiList;
    }

    public void setVirdiList(List<Virdi> virdiList) {
        this.virdiList = virdiList;
    }

    public String getVirdiName() {
        return virdiName;
    }

    public void setVirdiName(String virdiName) {
        this.virdiName = virdiName;
    }

    public Virdi getCurrentVirdi() {
        return currentVirdi;
    }

    public void setCurrentVirdi(Virdi currentVirdi) {
        this.currentVirdi = currentVirdi;
    }

    public Virdi getNewVirdi() {
        return newVirdi;
    }

    public void setNewVirdi(Virdi newVirdi) {
        this.newVirdi = newVirdi;
    }

    public Set<Virdi> getSelectedVirdis() {
        return selectedVirdis;
    }

    public void setSelectedVirdis(Set<Virdi> selectedVirdis) {
        this.selectedVirdis = selectedVirdis;
    }

    public Virdi getSelectedVirdi() {
        return selectedVirdi;
    }

    public void setSelectedVirdi(Virdi selectedVirdi) {
        this.selectedVirdi = selectedVirdi;
    }

    public boolean isVirdiEnabled() {
        return virdiEnabled;
    }

    public void setVirdiEnabled(boolean virdiEnabled) {
        this.virdiEnabled = virdiEnabled;
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

    public SortOrder getVirdiNameOrder() {
        return virdiNameOrder;
    }

    public void setVirdiNameOrder(SortOrder virdiNameOrder) {
        this.virdiNameOrder = virdiNameOrder;
    }

    public String getVirdiNameFilter() {
        return virdiNameFilter;
    }

    public void setVirdiNameFilter(String virdiNameFilter) {
        this.virdiNameFilter = virdiNameFilter;
    }

    public SortOrder getVirdiDescriptionOrder() {
        return virdiDescriptionOrder;
    }

    public void setVirdiDescriptionOrder(SortOrder virdiDescriptionOrder) {
        this.virdiDescriptionOrder = virdiDescriptionOrder;
    }

    public String getVirdiDescriptionFilter() {
        return virdiDescriptionFilter;
    }

    public void setVirdiDescriptionFilter(String virdiDescriptionFilter) {
        this.virdiDescriptionFilter = virdiDescriptionFilter;
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

    public DataModel<DeviceDataModel> getVirdiListModel() {
        return virdiListModel;
    }

    public void setVirdiListModel(DataModel<DeviceDataModel> virdiListModel) {
        this.virdiListModel = virdiListModel;
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

    public String getVirdiIPFilter() {
        return virdiIPFilter;
    }

    public void setVirdiIPFilter(String virdiIPFilter) {
        this.virdiIPFilter = virdiIPFilter;
    }

    public boolean isDisableFields() {
        return disableFields;
    }

    public void setDisableFields(boolean disableFields) {
        this.disableFields = disableFields;
    }

    public int getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }
}

