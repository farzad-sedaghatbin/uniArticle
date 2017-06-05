package ir.university.toosi.wtms.web.action.map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.service.MapServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.zone.HandleGatewayAction;
import ir.university.toosi.tms.model.entity.Map;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.zone.DeviceDataModel;
import ir.university.toosi.tms.model.entity.zone.Gateway;
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
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Named(value = "handleMapAction")
@SessionScoped
public class HandleMapAction implements Serializable {
    @Inject
    private UserManagementAction me;
    @Inject
    private HandleGatewayAction handleGatewayAction;

    @EJB
    private MapServiceImpl mapService;

    private String editable = "false";

    private List<Map> mapList = null;
    private DataModel<DeviceDataModel> mapListModel = null;
    private List<DeviceDataModel> listModel = null;
    private DataModel<Gateway> gatewayGrid = null;
    private String mapCode;
    private Gateway gateway;
    private Map currentMap = null;
    private Map newMap = null;
    private int page = 1;
    private Set<Map> selectedMaps = new HashSet<>();
    private Map selectedMap;
    private boolean selectAll;
    private boolean fingers;
    private boolean schedule;
    private boolean db;
    private boolean pictures;
    private boolean mapEnabled;
    private String description;
    private String descText;
    private String ip;
    private String content;
    private String gatewayId;
    private String cameraId;
    private SelectItem[] cameraItems;
    private SelectItem[] gatewayItems;
    private boolean selectRow = false;
    private boolean finger = false;
    private boolean online = false;
    private boolean entrance = false;
    private SortOrder mapCodeOrder = SortOrder.UNSORTED;
    private String mapCodeFilter;
    private SortOrder mapDescriptionOrder = SortOrder.UNSORTED;
    private String mapDescriptionFilter;


    public void begin() {
        refresh();
        me.redirect("/map/list-map.xhtml");
    }


    public void changeMaps(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            mapEnabled = true;
        } else
            mapEnabled = false;
    }


    public List<Map> getSelectionGrid() {
        List<Map> maps = new ArrayList<>();
        refresh();
        return mapList;
    }

    private void refresh() {
        init();

        handleGatewayAction.setSelectedGateways(new HashSet<Gateway>());
        List<Map> maps = mapService.getAllMap();
        mapList = maps;
    }


    public void add() {
        init();
        setEditable("false");
        me.redirect("/map/Editor.xhtml");
    }


    public void doDelete() {


        refresh();
        me.addInfoMessage(mapService.deleteMap(currentMap));
        me.redirect("/map/list-map.xhtml");
    }


    public void init() {
        mapCode = "";
        descText = "";
        ip = "";
        page = 1;
        cameraId = null;
        selectAll = false;
        mapEnabled = false;
        currentMap = null;
        setSelectRow(false);
    }

    public void edit() {
        setEditable("true");
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findMapById");
        try {
            currentMap = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentMap.getId())), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void view() {

        me.redirect("/map/Viewer.xhtml");

    }


    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();

        }

    }

    public void doEdit() {

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editMap");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(selectedMap)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/map/list-map.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void doAdd() {
        newMap = new Map();
        newMap.setDeleted("0");
        newMap.setStatus("c");
        newMap.setEffectorUser(me.getUsername());
        newMap.setContent(content.getBytes());
        System.out.println(content);
        Map insertedMap = null;
        insertedMap = mapService.createMap(newMap);
        if (insertedMap != null) {
//            for (Gateway gateway1 : selecteGateway) {
//                gateway1.getCameras().add(insertedMap);
//                me.getGeneralHelper().getWebServiceInfo().setServiceName("/editGateway");
//                try {
//                    gateway1 = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(gateway1)), Gateway.class);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/map/list-map.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }

/*
    public Filter<?> getMapCodeFilterImpl() {
        return new Filter<Map>() {
            public boolean accept(Map map) {
                return mapCodeFilter == null || mapCodeFilter.length() == 0 || map.getCode().toLowerCase().contains(mapCodeFilter.toLowerCase());
            }
        };
    }*/

    public void sortByMapCode() {
        if (mapCodeOrder.equals(SortOrder.ASCENDING)) {
            setMapCodeOrder(SortOrder.DESCENDING);
        } else {
            setMapCodeOrder(SortOrder.ASCENDING);
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

    public DataModel<Gateway> getGatewayGrid() {
        return gatewayGrid;
    }

    public void setGatewayGrid(DataModel<Gateway> gatewayGrid) {
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
        currentMap = null;
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

    public MapServiceImpl getMapService() {
        return mapService;
    }

    public void setMapService(MapServiceImpl mapService) {
        this.mapService = mapService;
    }

    public List<Map> getMapList() {
        return mapList;
    }

    public void setMapList(List<Map> mapList) {
        this.mapList = mapList;
    }

    public String getMapCode() {
        return mapCode;
    }

    public void setMapCode(String mapCode) {
        this.mapCode = mapCode;
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(Map currentMap) {
        this.currentMap = currentMap;
    }

    public Map getNewMap() {
        return newMap;
    }

    public void setNewMap(Map newMap) {
        this.newMap = newMap;
    }

    public Set<Map> getSelectedMaps() {
        return selectedMaps;
    }

    public void setSelectedMaps(Set<Map> selectedMaps) {
        this.selectedMaps = selectedMaps;
    }

    public Map getSelectedMap() {
        return selectedMap;
    }

    public void setSelectedMap(Map selectedMap) {
        this.selectedMap = selectedMap;
    }

    public boolean isMapEnabled() {
        return mapEnabled;
    }

    public void setMapEnabled(boolean mapEnabled) {
        this.mapEnabled = mapEnabled;
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

    public SortOrder getMapCodeOrder() {
        return mapCodeOrder;
    }

    public void setMapCodeOrder(SortOrder mapCodeOrder) {
        this.mapCodeOrder = mapCodeOrder;
    }

    public String getMapCodeFilter() {
        return mapCodeFilter;
    }

    public void setMapCodeFilter(String mapCodeFilter) {
        this.mapCodeFilter = mapCodeFilter;
    }

    public SortOrder getMapDescriptionOrder() {
        return mapDescriptionOrder;
    }

    public void setMapDescriptionOrder(SortOrder mapDescriptionOrder) {
        this.mapDescriptionOrder = mapDescriptionOrder;
    }

    public String getMapDescriptionFilter() {
        return mapDescriptionFilter;
    }

    public void setMapDescriptionFilter(String mapDescriptionFilter) {
        this.mapDescriptionFilter = mapDescriptionFilter;
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

    public DataModel<DeviceDataModel> getMapListModel() {
        return mapListModel;
    }

    public void setMapListModel(DataModel<DeviceDataModel> mapListModel) {
        this.mapListModel = mapListModel;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

