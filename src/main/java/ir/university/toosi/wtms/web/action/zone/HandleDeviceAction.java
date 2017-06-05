package ir.university.toosi.wtms.web.action.zone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.entity.zone.Virdi;
import ir.university.toosi.tms.model.service.PCServiceImpl;
import ir.university.toosi.tms.model.service.zone.CameraServiceImpl;
import ir.university.toosi.tms.model.service.zone.PDPServiceImpl;
import ir.university.toosi.tms.model.service.zone.VirdiServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.tms.model.entity.BaseEntity;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.zone.Camera;
import ir.university.toosi.tms.model.entity.zone.DeviceDataModel;
import ir.university.toosi.tms.model.entity.zone.PDP;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.SortOrder;


import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Named(value = "handleDeviceAction")
@SessionScoped
public class HandleDeviceAction implements Serializable {
    @Inject
    private UserManagementAction me;
    @EJB
    private PDPServiceImpl pdpService;
    @EJB
    private VirdiServiceImpl virdiService;
    @EJB
    private CameraServiceImpl cameraService;
    @EJB
    private PCServiceImpl pcService;

    private String editable = "false";
    private List<DeviceDataModel> listModel;
    private List<BaseEntity> deviceList = null;
    private int page = 1;
    private SortOrder ipOrder = SortOrder.UNSORTED;
    private SortOrder nameOrder = SortOrder.UNSORTED;
    private SortOrder descriptionOrder = SortOrder.UNSORTED;
    private String deviceNameFilter;
    private String deviceDescriptionFilter;
    private String deviceIPFilter;

    public void beginDevice() {
        refreshDevice();
        me.redirect("/monitoring/device-monitoring.xhtml");
    }


    private void refreshDevice() {
//        init();
        page = 1;

            List<Virdi> virdis = virdiService.getAllVirdis();
            DeviceDataModel deviceDataModel;
            listModel = new ArrayList<>();
            for (Virdi virdi : virdis) {
                deviceDataModel = new DeviceDataModel();
                deviceDataModel.setName(virdi.getName());
                deviceDataModel.setEnabled(virdi.isEnabled());
                deviceDataModel.setIp(virdi.getIp());
                deviceDataModel.setDescription(virdi.getDescription());
                listModel.add(deviceDataModel);
            }


            List<PDP> pdps = pdpService.getAllPDPs();
            for (PDP pdp : pdps) {
                deviceDataModel = new DeviceDataModel();
                deviceDataModel.setName(pdp.getName());
                deviceDataModel.setEnabled(pdp.isEnabled());
                deviceDataModel.setIp(pdp.getIp());
                deviceDataModel.setDescription(pdp.getDescription());
                listModel.add(deviceDataModel);
            }

            List<Camera> cameras =cameraService.getAllCamera();
            for (Camera camera : cameras) {
                deviceDataModel = new DeviceDataModel();
                deviceDataModel.setName(camera.getName());
                deviceDataModel.setEnabled(camera.isEnabled());
                deviceDataModel.setIp(camera.getIp());
                deviceDataModel.setDescription(camera.getDescription());
                listModel.add(deviceDataModel);
            }


        ping();
        deviceDescriptionFilter = "";
        deviceIPFilter = "";
        deviceNameFilter = "";
    }

    public void ping() {
        for (DeviceDataModel deviceDataModel : listModel) {

            try {
                boolean status = pdpService.ping(deviceDataModel.getIp());
                if (status)
                    deviceDataModel.setEnabled(true);
                else
                    deviceDataModel.setEnabled(false);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public void sortByIp() {
        ipOrder = newSortOrder(ipOrder);
    }

    public void sortByName() {
        nameOrder = newSortOrder(nameOrder);
    }

    public void sortByDescription() {
        descriptionOrder = newSortOrder(descriptionOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        ipOrder = SortOrder.UNSORTED;
        nameOrder = SortOrder.UNSORTED;
        descriptionOrder = SortOrder.UNSORTED;

        if (currentSortOrder.equals(SortOrder.DESCENDING)) {
            return SortOrder.ASCENDING;
        } else {
            return SortOrder.DESCENDING;
        }

    }
//    public Filter<?> getDeviceIPFilterImpl() {
//        return new Filter<DeviceDataModel>() {
//            public boolean accept(DeviceDataModel device) {
//                return deviceIPFilter == null || deviceIPFilter.length() == 0 || device.getIp().startsWith(deviceIPFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getDeviceNameFilterImpl() {
//        return new Filter<DeviceDataModel>() {
//            public boolean accept(DeviceDataModel device) {
//                return deviceNameFilter == null || deviceNameFilter.length() == 0 || device.getName().toLowerCase().contains(deviceNameFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getDeviceDescriptionFilterImpl() {
//        return new Filter<DeviceDataModel>() {
//            public boolean accept(DeviceDataModel device) {
//                return deviceDescriptionFilter == null || deviceDescriptionFilter.length() == 0 || device.getDescription().toLowerCase().contains(deviceDescriptionFilter.toLowerCase());
//            }
//        };
//    }
    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public List<DeviceDataModel> getListModel() {
        return listModel;
    }

    public void setListModel(List<DeviceDataModel> listModel) {
        this.listModel = listModel;
    }

    public List<BaseEntity> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<BaseEntity> deviceList) {
        this.deviceList = deviceList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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

    public SortOrder getNameOrder() {
        return nameOrder;
    }

    public void setNameOrder(SortOrder nameOrder) {
        this.nameOrder = nameOrder;
    }

    public String getDeviceNameFilter() {
        return deviceNameFilter;
    }

    public void setDeviceNameFilter(String deviceNameFilter) {
        this.deviceNameFilter = deviceNameFilter;
    }

    public String getDeviceDescriptionFilter() {
        return deviceDescriptionFilter;
    }

    public void setDeviceDescriptionFilter(String deviceDescriptionFilter) {
        this.deviceDescriptionFilter = deviceDescriptionFilter;
    }

    public String getDeviceIPFilter() {
        return deviceIPFilter;
    }

    public void setDeviceIPFilter(String deviceIPFilter) {
        this.deviceIPFilter = deviceIPFilter;
    }
}

