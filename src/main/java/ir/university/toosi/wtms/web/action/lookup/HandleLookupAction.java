package ir.university.toosi.wtms.web.action.lookup;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.service.LookupServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.tms.model.entity.Lookup;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.WebServiceInfo;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.SortOrder;


import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleLookupAction")
@SessionScoped
public class HandleLookupAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleBLookupAction handleBLookupAction;

    @EJB
    private LookupServiceImpl lookupService;
    private List<Lookup> lookupList = null;
    private String editable = "false";
    private String lookupTitle;
    private Lookup currentLookup = null;
    private String currentPage;
    private int page = 1;
    private SortOrder lookupDescriptionOrder = SortOrder.UNSORTED;
    private String lookupDescriptionFilter;
    private boolean selected;
    private Set<Lookup> selectedLookups = new HashSet<>();
    private boolean selectRow = false;
    private boolean definable = false;

    public void begin() {
//        me.setActiveMenu(MenuType.SETTING);
        refresh();
        me.redirect("/lookup/list-lookup.xhtml");
    }

    public String edit() {
        setEditable("true");
        handleBLookupAction.setCurrentLookup(currentLookup);

        return "list-blookup";
    }
    private void refresh() {
        init();
     lookupList = lookupService.getAllLookup();
            for (Lookup lookup : lookupList) {
//                lookup.setTitleText(me.getValue(lookup.getTitle()));
                lookup.setTitleText(lookup.getTitle());
            }
    }

    public void add() {
        init();
        setEditable("false");

    }


    public void init() {
        lookupTitle = "";
        lookupDescriptionFilter="";
        page = 1;
        currentLookup = null;
        setSelectRow(false);
        setDefinable(false);
    }




    public void sortByLookupDescription() {
        if (lookupDescriptionOrder.equals(SortOrder.ASCENDING)) {
            setLookupDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setLookupDescriptionOrder(SortOrder.ASCENDING);
        }
    }

   /* public Filter<?> getLookupDescriptionFilterImpl() {
        return new Filter<Lookup>() {
            public boolean accept(Lookup lookup) {
                return lookupDescriptionFilter == null || lookupDescriptionFilter.length() == 0 || lookup.getTitle().toLowerCase().contains(lookupDescriptionFilter.toLowerCase());
            }
        };
    }*/

    public void selectForEdit() {
//        currentLookup = lookupList.getRowData();
        setSelectRow(true);
        setDefinable(currentLookup.isDefinable());
    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public List<Lookup> getLookupList() {
        return lookupList;
    }

    public void setLookupList(List<Lookup> lookupList) {
        this.lookupList = lookupList;
    }

    public String getLookupDescriptionFilter() {
        return lookupDescriptionFilter;
    }

    public void setLookupDescriptionFilter(String lookupDescriptionFilter) {
        this.lookupDescriptionFilter = lookupDescriptionFilter;
    }

    public SortOrder getLookupDescriptionOrder() {
        return lookupDescriptionOrder;
    }

    public void setLookupDescriptionOrder(SortOrder lookupDescriptionOrder) {
        this.lookupDescriptionOrder = lookupDescriptionOrder;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getLookupTitle() {
        return lookupTitle;
    }

    public void setLookupTitle(String lookupTitle) {
        this.lookupTitle = lookupTitle;
    }

    public Set<Lookup> getSelectedLookups() {
        return selectedLookups;
    }

    public void setSelectedLookups(Set<Lookup> selectedLookups) {
        this.selectedLookups = selectedLookups;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        currentLookup = null;
        setSelectRow(false);
        setDefinable(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public String getPcName() {
        return lookupTitle;
    }

    public void setPcName(String lookupName) {
        this.lookupTitle = lookupName;
    }

    public Lookup getCurrentLookup() {
        return currentLookup;
    }

    public void setCurrentLookup(Lookup currentLookup) {
        this.currentLookup = currentLookup;
    }

    public SortOrder getPcDescriptionOrder() {
        return lookupDescriptionOrder;
    }

    public void setPcDescriptionOrder(SortOrder lookupDescriptionOrder) {
        this.lookupDescriptionOrder = lookupDescriptionOrder;
    }

    public String getPcDescriptionFilter() {
        return lookupDescriptionFilter;
    }

    public void setPcDescriptionFilter(String lookupDescriptionFilter) {
        this.lookupDescriptionFilter = lookupDescriptionFilter;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isDefinable() {
        return definable;
    }

    public void setDefinable(boolean definable) {
        this.definable = definable;
    }
}