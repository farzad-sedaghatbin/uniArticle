package ir.university.toosi.wtms.web.action.pc;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.service.PCServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import ir.university.toosi.tms.model.entity.*;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.SortOrder;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@ManagedBean(name = "handlePCAction")
@SessionScoped
public class HandlePCAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleRoleAction handleRoleAction;
    @EJB
    private PCServiceImpl pcService;
    private List<PC> pcList = null;
    private boolean editable;
    private boolean disableFields = true;
    private String pcName;
    private boolean pcEnabled;
    private String pcIP;
    private BLookup pcLocation;
    private PC currentPC = null;
    private String currentPage;
    private int page = 1;
    private String pcDescriptionFilter;
    private String pcIPFilter;
    private boolean selected;
    private Set<PC> selectedPCs = new HashSet<>();
    private List<BLookup> locations;
    private boolean selectRow = false;
    private String pcNameFilter;
    private String pcLocationFilter;
    private SortOrder pcIpOrder = SortOrder.UNSORTED;
    private SortOrder pcNameOrder = SortOrder.UNSORTED;
    private SortOrder pcLocationOrder = SortOrder.UNSORTED;
    private SortOrder pcDescriptionOrder = SortOrder.UNSORTED;


    public void begin() {
//        todo:solve this comment
//        me.setActiveMenu(MenuType.USER);
        System.out.println(me.getDirection());
        refresh();
        me.redirect("/pc/pc.xhtml");
    }

//    public void selectPCs(ValueChangeEvent event) {
//        currentPC = pcList.getRowData();
//        boolean temp = (Boolean) event.getNewValue();
//        if (temp) {
//            currentPC.setSelected(true);
//            selectedPCs.add(currentPC);
//        } else {
//            currentPC.setSelected(false);
//            selectedPCs.remove(currentPC);
//        }
//    }

    public void changePCs(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            pcEnabled = true;
        } else
            pcEnabled = false;
    }

    public List<PC> getSelectionGrid() {
        List<PC> pcs = new ArrayList<>();
        refresh();
        return pcList;
    }

    private void refresh() {
        init();
        List<PC> pcs = pcService.getAllPCs();
//        todo:must be uncommented
//            for (PC pc : pcs) {
//                pc.getLocation().setTitleText(pc.getName());
////                todo:the commented line is correct
////                pc.getLocation().setTitleText(me.getValue(pc.getLocation().getCode()));
//            }
            pcList = new ArrayList<>(pcs);
    }

    public void add() {
        init();
        setEditable(false);
        setDisableFields(false);
    }


    public void doDelete() {

//        currentPC.setEffectorUser(me.getUsername());
        pcService.deletePC(currentPC);
        refresh();
        me.addInfoMessage("delete was successful");
        me.redirect("/pc/pc.xhtml");
    }

    public void init() {
        pcName = "";

        pcEnabled = true;
        pcIP = "";
        page = 1;
        currentPC = null;
        pcDescriptionFilter = "";
        pcIPFilter = "";
        pcNameFilter = "";
        pcLocationFilter = "";
        setSelectRow(false);
        setDisableFields(true);
    }

    public void edit() {
        setEditable(true);
        setDisableFields(false);
        pcName = currentPC.getName();
        pcIP = currentPC.getIp();
        pcLocation = currentPC.getLocation();
    }

    public void view(){
        pcName = currentPC.getName();
        pcIP = currentPC.getIp();
        pcLocation = currentPC.getLocation();
    }

    public void saveOrUpdate() {
        if (!editable) {
            doAdd();
        } else {
            doEdit();
        }
    }

    public void resetEditable() {
        setEditable(false);
    }

    private void doEdit() {
        currentPC.setIp(pcIP);
        currentPC.setName(pcName);
        boolean condition = pcService.exist(currentPC.getIp(),currentPC.getId());
        if (condition) {
            me.addInfoMessage("pc.exist");
            return;
        }

//        currentPC.setEffectorUser(me.getUsername());
        condition = pcService.editPC(currentPC);
            if (condition) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/pc/pc.xhtml");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }

    }

    private void doAdd() {
        System.out.println(me.getDirection());
        PC newPC = new PC();
        newPC.setName(pcName);
        newPC.setIp(pcIP);
        newPC.setDeleted("0");
        newPC.setStatus("c");
//        newPC.setEffectorUser(me.getUsername());
        newPC.setLocation(pcLocation);
        boolean condition = pcService.existNotId(String.valueOf(pcIP));
            if (condition) {
                me.addInfoMessage("pc.exist");
                return;
            }

        PC insertedPC = null;
        insertedPC = pcService.createPC(newPC);
        if (insertedPC != null) {
            refresh();
//            me.addInfoMessage("operation.occurred");
            me.redirect("/pc/pc.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }

    }

//    public Filter<?> getPCIPFilterImpl() {
//        return new Filter<PC>() {
//            public boolean accept(PC pc) {
//                return pcIPFilter == null || pcIPFilter.length() == 0 || pc.getIp().startsWith(pcIPFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getPcNameFilterImpl() {
//        return new Filter<PC>() {
//            public boolean accept(PC pc) {
//                return pcNameFilter == null || pcNameFilter.length() == 0 || pc.getName().toLowerCase().contains(pcNameFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getPcLocationFilterImpl() {
//        return new Filter<PC>() {
//            public boolean accept(PC pc) {
//                return pcLocationFilter == null || pcLocationFilter.length() == 0 || pc.getLocation().getTitleText().toLowerCase().contains(pcLocationFilter.toLowerCase());
//            }
//        };
//    }

    public void sortByPcIp() {
        pcIpOrder = newSortOrder(pcIpOrder);
    }

    public void sortByPcName() {
        pcNameOrder = newSortOrder(pcNameOrder);
    }

    public void sortByPcLocation() {
        pcLocationOrder = newSortOrder(pcLocationOrder);
    }

    public void sortByPCDescription() {
        pcDescriptionOrder = newSortOrder(pcDescriptionOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        pcIpOrder = SortOrder.UNSORTED;
        pcNameOrder = SortOrder.UNSORTED;
        pcLocationOrder = SortOrder.UNSORTED;
        pcDescriptionOrder = SortOrder.UNSORTED;

        if (currentSortOrder.equals(SortOrder.DESCENDING)) {
            return SortOrder.ASCENDING;
        } else {
            return SortOrder.DESCENDING;
        }
    }

//    public void selectForEdit() {
//        currentPC = pcList.getRowData();
//        setSelectRow(true);
//    }


    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public List<PC> getPCList() {
        return pcList;
    }

    public void setPCList(List<PC> pcList) {
        this.pcList = pcList;
    }

    public String getPCDescriptionFilter() {
        return pcDescriptionFilter;
    }

    public void setPCDescriptionFilter(String pcDescriptionFilter) {
        this.pcDescriptionFilter = pcDescriptionFilter;
    }

    public SortOrder getPCDescriptionOrder() {
        return pcDescriptionOrder;
    }

    public void setPCDescriptionOrder(SortOrder pcDescriptionOrder) {
        this.pcDescriptionOrder = pcDescriptionOrder;
    }

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getPCName() {
        return pcName;
    }

    public void setPCName(String pcName) {
        this.pcName = pcName;
    }

    public boolean isPCEnabled() {
        return pcEnabled;
    }

    public void setPCEnabled(boolean pcEnabled) {
        this.pcEnabled = pcEnabled;
    }

    public String getPcIP() {
        return pcIP;
    }

    public void setPcIP(String pcIP) {
        this.pcIP = pcIP;
    }

    public Set<PC> getSelectedPCs() {
        return selectedPCs;
    }

    public void setSelectedPCs(Set<PC> selectedPCs) {
        this.selectedPCs = selectedPCs;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        currentPC = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<PC> getPcList() {
        return pcList;
    }

    public void setPcList(List<PC> pcList) {
        this.pcList = pcList;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    public boolean isPcEnabled() {
        return pcEnabled;
    }

    public void setPcEnabled(boolean pcEnabled) {
        this.pcEnabled = pcEnabled;
    }

    public PC getCurrentPC() {
        return currentPC;
    }

    public void setCurrentPC(PC currentPC) {
        this.currentPC = currentPC;
    }

    public SortOrder getPcDescriptionOrder() {
        return pcDescriptionOrder;
    }

    public void setPcDescriptionOrder(SortOrder pcDescriptionOrder) {
        this.pcDescriptionOrder = pcDescriptionOrder;
    }

    public String getPcDescriptionFilter() {
        return pcDescriptionFilter;
    }

    public void setPcDescriptionFilter(String pcDescriptionFilter) {
        this.pcDescriptionFilter = pcDescriptionFilter;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public SortOrder getPcNameOrder() {
        return pcNameOrder;
    }

    public void setPcNameOrder(SortOrder pcNameOrder) {
        this.pcNameOrder = pcNameOrder;
    }

    public String getPcNameFilter() {
        return pcNameFilter;
    }

    public void setPcNameFilter(String pcNameFilter) {
        this.pcNameFilter = pcNameFilter;
    }

    public SortOrder getPcLocationOrder() {
        return pcLocationOrder;
    }

    public void setPcLocationOrder(SortOrder pcLocationOrder) {
        this.pcLocationOrder = pcLocationOrder;
    }

    public String getPcLocationFilter() {
        return pcLocationFilter;
    }

    public void setPcLocationFilter(String pcLocationFilter) {
        this.pcLocationFilter = pcLocationFilter;
    }

    public void setPcLocation(BLookup pcLocation) {
        this.pcLocation = pcLocation;
    }

    public SortOrder getPcIpOrder() {
        return pcIpOrder;
    }

    public void setPcIpOrder(SortOrder pcIpOrder) {
        this.pcIpOrder = pcIpOrder;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public HandleRoleAction getHandleRoleAction() {
        return handleRoleAction;
    }

    public void setHandleRoleAction(HandleRoleAction handleRoleAction) {
        this.handleRoleAction = handleRoleAction;
    }

    public String getPcIPFilter() {
        return pcIPFilter;
    }

    public void setPcIPFilter(String pcIPFilter) {
        this.pcIPFilter = pcIPFilter;
    }

    public boolean isDisableFields() {
        return disableFields;
    }

    public void setDisableFields(boolean disableFields) {
        this.disableFields = disableFields;
    }

    public PC findForConverter(long value) {
        return pcService.findById(value);
    }
}