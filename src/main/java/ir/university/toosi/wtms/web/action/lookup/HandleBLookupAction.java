package ir.university.toosi.wtms.web.action.lookup;


import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.Lookup;
import ir.university.toosi.tms.model.service.BLookupServiceImpl;
import ir.university.toosi.tms.model.service.LookupServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import org.primefaces.model.SortOrder;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleBLookupAction")
@SessionScoped
public class HandleBLookupAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleLookupAction handleLookupAction;
    @EJB
    private LookupServiceImpl lookupService;
    @EJB
    private BLookupServiceImpl bLookupService;
    private List<BLookup> bLookupList = null;
    private String editable = "false";
    private String bLookupTitle;
    private BLookup currentBLookup = null;
    private Lookup currentLookup = null;
    private String currentPage;
    private int page = 1;
    private SortOrder bLookupDescriptionOrder = SortOrder.UNSORTED;
    private String bLookupDescriptionFilter;
    private boolean selected;
    private Set<BLookup> selectedBLookups = new HashSet<>();

    public void begin() {

        refresh();
        me.redirect("/lookup/list-blookup.xhtml");
    }


    public List<BLookup> getSelectionGrid() {
        List<BLookup> bLookups = new ArrayList<>();
        refresh();
        return bLookupList;
    }

    private void refresh() {
        init();
       bLookupList = bLookupService.getByLookup(currentLookup.getTitle());
            for (BLookup bLookup : bLookupList) {
                bLookup.setTitleText(me.getValue(bLookup.getCode()));
            }
    }

    public void add() {
        init();
        setEditable("false");

    }

    public void doDelete() {
//        currentBLookup = bLookupList.getRowData();
        currentBLookup.setEffectorUser(me.getUsername());
            String condition = bLookupService.deleteBLookup(currentBLookup);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/bLookup/list-blookup.xhtml");
    }

    public void init() {
        bLookupTitle = "";
        page = 1;
        bLookupDescriptionFilter="";
    }

    public void edit() {
        setEditable("true");
//        currentBLookup = bLookupList.getRowData();
//        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getByLookupId");
//        try {
//            currentBLookup = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentBLookup.getId())), BLookup.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        bLookupTitle=me.getValue(currentBLookup.getCode());
    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();
        }
    }

    private void doEdit() {
        currentBLookup.setTitleText(bLookupTitle);
        currentBLookup.setEffectorUser(me.getUsername());
            boolean condition =bLookupService.editBLookup(currentBLookup);
            if (condition) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/lookup/list-blookup.xhtml");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }

    }

    private void doAdd() {
        BLookup newBLookup = new BLookup();
        newBLookup.setTitleText(bLookupTitle);
        newBLookup.setDeleted("0");
        newBLookup.setStatus("c");
        newBLookup.setEffectorUser(me.getUsername());
        newBLookup.setLookup(handleLookupAction.getCurrentLookup());


        BLookup insertedBLookup = null;
            insertedBLookup =bLookupService.createBLookup(newBLookup);

        if (insertedBLookup != null) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/lookup/list-blookup.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }

    }


    public void sortByBLookupDescription() {
        if (bLookupDescriptionOrder.equals(SortOrder.ASCENDING)) {
            setBLookupDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setBLookupDescriptionOrder(SortOrder.ASCENDING);
        }
    }

//    public Filter<?> getBLookupDescriptionFilterImpl() {
//        return new Filter<BLookup>() {
//            public boolean accept(BLookup bLookup) {
//                return bLookupDescriptionFilter == null || bLookupDescriptionFilter.length() == 0 || bLookup.getTitleText().toLowerCase().contains(bLookupDescriptionFilter.toLowerCase());
//            }
//        };
//    }


    public String getBLookupDescriptionFilter() {
        return bLookupDescriptionFilter;
    }

    public void setBLookupDescriptionFilter(String bLookupDescriptionFilter) {
        this.bLookupDescriptionFilter = bLookupDescriptionFilter;
    }

    public SortOrder getBLookupDescriptionOrder() {
        return bLookupDescriptionOrder;
    }

    public void setBLookupDescriptionOrder(SortOrder bLookupDescriptionOrder) {
        this.bLookupDescriptionOrder = bLookupDescriptionOrder;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getBLookupName() {
        return bLookupTitle;
    }

    public void setBLookupName(String bLookupName) {
        this.bLookupTitle = bLookupName;
    }


    public Set<BLookup> getSelectedBLookups() {
        return selectedBLookups;
    }

    public void setSelectedBLookups(Set<BLookup> selectedBLookups) {
        this.selectedBLookups = selectedBLookups;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public String getPcName() {
        return bLookupTitle;
    }

    public void setPcName(String bLookupName) {
        this.bLookupTitle = bLookupName;
    }


    public BLookup getCurrentBLookup() {
        return currentBLookup;
    }

    public void setCurrentBLookup(BLookup currentBLookup) {
        this.currentBLookup = currentBLookup;
    }

    public SortOrder getPcDescriptionOrder() {
        return bLookupDescriptionOrder;
    }

    public void setPcDescriptionOrder(SortOrder bLookupDescriptionOrder) {
        this.bLookupDescriptionOrder = bLookupDescriptionOrder;
    }

    public String getPcDescriptionFilter() {
        return bLookupDescriptionFilter;
    }

    public void setPcDescriptionFilter(String bLookupDescriptionFilter) {
        this.bLookupDescriptionFilter = bLookupDescriptionFilter;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }


    public String getbLookupTitle() {
        return bLookupTitle;
    }

    public void setbLookupTitle(String bLookupTitle) {
        this.bLookupTitle = bLookupTitle;
    }

    public Lookup getCurrentLookup() {
        return currentLookup;
    }

    public void setCurrentLookup(Lookup currentLookup) {
        this.currentLookup = currentLookup;
        refresh();
    }

    public SortOrder getbLookupDescriptionOrder() {
        return bLookupDescriptionOrder;
    }

    public void setbLookupDescriptionOrder(SortOrder bLookupDescriptionOrder) {
        this.bLookupDescriptionOrder = bLookupDescriptionOrder;
    }

    public String getbLookupDescriptionFilter() {
        return bLookupDescriptionFilter;
    }

    public void setbLookupDescriptionFilter(String bLookupDescriptionFilter) {
        this.bLookupDescriptionFilter = bLookupDescriptionFilter;
    }

    public List<BLookup> getbLookupList() {
        return bLookupList;
    }

    public void setbLookupList(List<BLookup> bLookupList) {
        this.bLookupList = bLookupList;
    }
}