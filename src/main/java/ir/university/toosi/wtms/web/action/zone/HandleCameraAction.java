package ir.university.toosi.wtms.web.action.zone;

import ir.university.toosi.tms.model.entity.zone.Camera;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.PDP;
import ir.university.toosi.tms.model.service.zone.CameraServiceImpl;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
import ir.university.toosi.tms.model.service.zone.PDPServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import org.primefaces.model.DualListModel;
import org.primefaces.model.SortOrder;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: behzad
 * Date: 10/6/13
 * Time: 12:52 AM
 * To change this template use File | Settings | File Templates.
 */
@Named(value = "handleCameraAction")
@SessionScoped
public class HandleCameraAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleGatewayAction handleGatewayAction;
    @EJB
    private CameraServiceImpl cameraService;

    @EJB
    private PDPServiceImpl pdpService;

    @EJB
    private GatewayServiceImpl gatewayService;

    private String editable = "false";

    private List<Camera> cameraList = null;
    private List<Gateway> gatewayGrid = null;
    private String cameraName;
    private String password;
    private String userName;
    private String rePassword;
    private Gateway gateway;
    private Camera newCamera = null;
    private int page = 1;
    private Set<Camera> selectedCameras = new HashSet<>();
    private Camera selectedCamera;
    private boolean selectAll;
    private boolean cameraEnabled;
    private Camera currentCamera = null;
    private boolean selectRow = false;
    private String description;
    private String descText;
    private String ip;
    private String frames;
    private String cameraNameFilter;
    private String cameraDescFilter;
    private String cameraIPFilter;
    private SortOrder cameraNameOrder = SortOrder.UNSORTED;
    private SortOrder ipOrder = SortOrder.UNSORTED;
    private SortOrder descriptionOrder = SortOrder.UNSORTED;
    private boolean disableFields;


    public void begin() {
//        me.setActiveMenu(MenuType.HARDWARE);
        refresh();
        me.redirect("/zone/list-camera.xhtml");
    }

    public void selectCameras(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentCamera.setSelected(true);
            selectedCameras.add(currentCamera);
        } else {
            currentCamera.setSelected(false);
            selectedCameras.remove(currentCamera);
        }
    }

    public void changeCameras(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            cameraEnabled = true;
        } else
            cameraEnabled = false;
    }


    public void selectAllCamera(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (Camera camera : cameraList) {
                camera.setSelected(true);
                selectedCameras.add(camera);
            }
        } else {
            for (Camera camera : cameraList) {
                camera.setSelected(false);
            }
        }
        selectedCameras.clear();
    }

    public List<Camera> getSelectionGrid() {
        List<Camera> cameras = new ArrayList<>();
        refresh();
        return cameraList;
    }

    private void refresh() {
        init();

        handleGatewayAction.setSelectedGateways(new HashSet<Gateway>());
        List<Camera> cameras = cameraService.getAllCamera();
        cameraList = cameras;
    }

    public void add() {
        init();
        gatewayGrid = handleGatewayAction.getSelectionGrid();
        handleGatewayAction.setGatewayDualList(new DualListModel<Gateway>(gatewayGrid, new ArrayList<Gateway>()));
        setEditable("false");
    }

    public void doDelete() {
        currentCamera.setEffectorUser(me.getUsername());

        List<PDP> pdpList = null;
        pdpList = pdpService.findByCameraId(currentCamera.getId());
        if (pdpList != null && pdpList.size() > 0) {
            me.addInfoMessage("camera.pdp");
            me.redirect("/zone/list-camera.xhtml");
        }

        List<Gateway> gatewayList = null;
        gatewayList = gatewayService.getAllGateway();
        boolean flag = false;
        Camera removableCamera = null;
        for (Gateway gateway1 : gatewayList) {
            for (Camera camera : gateway1.getCameras()) {
                if (camera.getId() == currentCamera.getId()) {
                    flag = true;
                    removableCamera = camera;
                }
            }
            if (flag) {
                flag = false;
                gateway1.getCameras().remove(removableCamera);
            }
            boolean condition = gatewayService.editGateway(gateway1);
        }
        String condition = cameraService.deleteCamera(currentCamera);
        refresh();
        me.addInfoMessage(condition);
        me.redirect("/zone/list-camera.xhtml");
    }


    public void init() {
        cameraName = "";
        descText = "";
        ip = "";
        selectAll = false;
        cameraEnabled = false;
        page = 1;
        frames = "";
        handleGatewayAction.init();
        currentCamera = null;
        cameraNameFilter = "";
        cameraDescFilter = "";
        userName="";
        password="";
        rePassword="";
        setSelectRow(false);
    }

    public void view() {

        setEditable("false");
        setDisableFields(true);
        cameraEnabled = currentCamera.isEnabled();
        ip = currentCamera.getIp();
        descText = currentCamera.getDescription();
        cameraName = currentCamera.getName();
        frames = String.valueOf(currentCamera.getFrames());
        currentCamera = cameraService.findById(currentCamera.getId());
        List<Gateway> gatewayList = null;
        gatewayList = gatewayService.getAllGateway();
        handleGatewayAction.setSelectedGateways(new HashSet<Gateway>());
        for (Gateway gateway : gatewayList) {
            gateway.setDescription(me.getValue(gateway.getDescription()));
        }

        List<Gateway> sourceGateways = new ArrayList<>();
        List<Gateway> targetGateways = new ArrayList<>();

        for (Gateway gateway : gatewayList) {
            for (Camera camera : gateway.getCameras()) {

                if ((camera.getId() == currentCamera.getId())) {
                    gateway.setSelected(true);
                    handleGatewayAction.getSelectedGateways().add(gateway);
                    targetGateways.add(gateway);
                } else {
                    sourceGateways.add(gateway);
                }
            }
        }
        handleGatewayAction.setGatewayList(gatewayList);
        handleGatewayAction.setGatewayDualList(new DualListModel<Gateway>(sourceGateways, targetGateways));
    }


    public void edit() {

        setEditable("true");
        setDisableFields(false);
        cameraEnabled = currentCamera.isEnabled();
        ip = currentCamera.getIp();
        descText = currentCamera.getDescription();
        cameraName = currentCamera.getName();
        password="";
        frames = String.valueOf(currentCamera.getFrames());
        userName=currentCamera.getUserName();
        currentCamera = cameraService.findById(currentCamera.getId());
        List<Gateway> gatewayList = null;
        gatewayList = gatewayService.getAllGateway();
        handleGatewayAction.setSelectedGateways(new HashSet<Gateway>());
        for (Gateway gateway : gatewayList) {
            gateway.setDescription(me.getValue(gateway.getDescription()));
        }

        List<Gateway> sourceGateways = new ArrayList<>();
        List<Gateway> targetGateways = new ArrayList<>();

        for (Gateway gateway : gatewayList) {
            for (Camera camera : gateway.getCameras()) {

                if ((camera.getId() == currentCamera.getId())) {
                    gateway.setSelected(true);
                    handleGatewayAction.getSelectedGateways().add(gateway);
                    targetGateways.add(gateway);
                } else {
                    sourceGateways.add(gateway);
                }
            }
        }
        handleGatewayAction.setGatewayList(gatewayList);
        handleGatewayAction.setGatewayDualList(new DualListModel<Gateway>(sourceGateways, targetGateways));
    }


    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();

        }

    }

    public void doEdit() {
        if(!password.equals(rePassword)){
            me.addErrorMessage("password.not.match");
            return;
        }
        currentCamera.setDescription(descText);
        currentCamera.setName(cameraName);
        currentCamera.setIp(ip);
        currentCamera.setEnabled(cameraEnabled);
        currentCamera.setFrames(Long.valueOf(frames));
        currentCamera.setUserName(userName);
        currentCamera.setPassword(password);
        boolean condition = cameraService.exist(currentCamera.getIp(), currentCamera.getId());
        if (condition) {

            me.addInfoMessage("camera.exist");
            return;
        }
        List<Gateway> gatewayList = null;
        gatewayList = gatewayService.getAllGateway();
        boolean flag = false;
        Camera removableCamera = null;
        for (Gateway gateway1 : gatewayList) {
            for (Camera camera : gateway1.getCameras()) {
                if (camera.getId() == currentCamera.getId()) {
                    flag = true;
                    removableCamera = camera;
                }
            }
            if (flag) {
                flag = false;
                gateway1.getCameras().remove(removableCamera);
            }
            condition = gatewayService.editGateway(gateway1);
        }
        for (Gateway gateway1 : handleGatewayAction.getSelectedGateways()) {
            for (Gateway gateway2 : gatewayList) {
                if (gateway1.getId() == gateway2.getId()) {
                    gateway2.getCameras().add(currentCamera);
                    condition = gatewayService.editGateway(gateway2);
                }
            }
        }

        condition = cameraService.editCamera(currentCamera);
        if (condition) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/zone/list-camera.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }

    }


    public void doAdd() {
        if(!password.equals(rePassword)){
            me.addErrorMessage("password.not.match");
            return;
        }
        newCamera = new Camera();
        newCamera.setDescription(descText);
        newCamera.setName(cameraName);
        newCamera.setDeleted("0");
        newCamera.setEnabled(cameraEnabled);
        newCamera.setStatus("c");
        newCamera.setEffectorUser(me.getUsername());
        newCamera.setIp(ip);
        newCamera.setPassword(password);
        newCamera.setUserName(userName);

        if (frames != null || !"".equals(frames))
            newCamera.setFrames(Long.valueOf(frames));

        boolean condition = cameraService.existNotId(newCamera.getIp());
        if (condition) {

            me.addInfoMessage("camera.exist");
            return;
        }

        Set<Gateway> selecteGateway = new HashSet<>();
        for (Gateway gateway : handleGatewayAction.getSelectedGateways()) {

            if (gateway.isSelected()) {

                selecteGateway.add(gateway);
            }
        }
//        if (selecteGateway.size() == 0) {
//            me.addErrorMessage("no_operation_selected");
//            return;
//        }

        Camera insertedCamera = null;
        insertedCamera = cameraService.createCamera(newCamera);

        if (insertedCamera != null) {
            for (Gateway gateway1 : selecteGateway) {
                gateway1.getCameras().add(insertedCamera);
                gatewayService.editGateway(gateway1);
            }
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/zone/list-camera.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }
//
//    public Filter<?> getCameraNameFilterImpl() {
//        return new Filter<Camera>() {
//            public boolean accept(Camera camera) {
//                return cameraNameFilter == null || cameraNameFilter.length() == 0 || camera.getName().toLowerCase().contains(cameraNameFilter.toLowerCase());
//            }
//        };
//    }    public Filter<?> getCameraDescFilterImpl() {
//        return new Filter<Camera>() {
//            public boolean accept(Camera camera) {
//                return cameraDescFilter == null || cameraDescFilter.length() == 0 || camera.getDescription().toLowerCase().contains(cameraDescFilter.toLowerCase());
//            }
//        };
//    }
//    public Filter<?> getCameraIPFilterImpl() {
//        return new Filter<Camera>() {
//            public boolean accept(Camera camera) {
//                return cameraIPFilter == null || cameraIPFilter.length() == 0 || camera.getIp().contains(cameraIPFilter.toLowerCase());
//            }
//        };
//    }
//
//


    public void sortByCameraName() {
        cameraNameOrder = newSortOrder(cameraNameOrder);
    }

    public void sortByIp() {
        ipOrder = newSortOrder(ipOrder);
    }

    public void sortByDescription() {
        descriptionOrder = newSortOrder(descriptionOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        ipOrder = SortOrder.UNSORTED;
        cameraNameOrder = SortOrder.UNSORTED;
        descriptionOrder = SortOrder.UNSORTED;

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

    public List<Gateway> getGatewayGrid() {
        return gatewayGrid;
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

    public List<Camera> getCameraList() {
        return cameraList;
    }

    public void setCameraList(List<Camera> cameraList) {
        this.cameraList = cameraList;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public Camera getCurrentCamera() {
        return currentCamera;
    }

    public void setCurrentCamera(Camera currentCamera) {
        this.currentCamera = currentCamera;
    }

    public Camera getNewCamera() {
        return newCamera;
    }

    public void setNewCamera(Camera newCamera) {
        this.newCamera = newCamera;
    }

    public int getPage() {
        currentCamera = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Set<Camera> getSelectedCameras() {
        return selectedCameras;
    }

    public void setSelectedCameras(Set<Camera> selectedCameras) {
        this.selectedCameras = selectedCameras;
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

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public Camera getSelectedCamera() {
        return selectedCamera;
    }

    public void setSelectedCamera(Camera selectedCamera) {
        this.selectedCamera = selectedCamera;
    }

    public boolean isCameraEnabled() {
        return cameraEnabled;
    }

    public void setCameraEnabled(boolean cameraEnabled) {
        this.cameraEnabled = cameraEnabled;
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

    public String getFrames() {
        return frames;
    }

    public void setFrames(String frames) {
        this.frames = frames;
    }

    public SortOrder getCameraNameOrder() {
        return cameraNameOrder;
    }

    public void setCameraNameOrder(SortOrder cameraNameOrder) {
        this.cameraNameOrder = cameraNameOrder;
    }

    public String getCameraNameFilter() {
        return cameraNameFilter;
    }

    public void setCameraNameFilter(String cameraNameFilter) {
        this.cameraNameFilter = cameraNameFilter;
    }

    public SortOrder getIpOrder() {
        return ipOrder;
    }

    public void setIpOrder(SortOrder ipOrder) {
        this.ipOrder = ipOrder;
    }

    public SortOrder getDescriptionOrder() {
        return descriptionOrder;
    }

    public void setDescriptionOrder(SortOrder descriptionOrder) {
        this.descriptionOrder = descriptionOrder;
    }

    public String getCameraIPFilter() {
        return cameraIPFilter;
    }

    public void setCameraIPFilter(String cameraIPFilter) {
        this.cameraIPFilter = cameraIPFilter;
    }

    public String getCameraDescFilter() {
        return cameraDescFilter;
    }

    public void setCameraDescFilter(String cameraDescFilter) {
        this.cameraDescFilter = cameraDescFilter;
    }

    public boolean isDisableFields() {
        return disableFields;
    }

    public void setDisableFields(boolean disableFields) {
        this.disableFields = disableFields;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
