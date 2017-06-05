package ir.university.toosi.wtms.web.action.calendar;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.service.calendar.DayTypeServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.WebServiceInfo;
import ir.university.toosi.tms.model.entity.calendar.DayType;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.SortOrder;
//import org.primefaces.model.SortOrder;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
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

@Named(value = "handleDayTypeAction")
@SessionScoped
public class HandleDayTypeAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleRoleAction handleRoleAction;

    @EJB
    private DayTypeServiceImpl dayTypeService;
    private List<DayType> dayTypeList = null;
    private String editable = "false";
    private String title;
    private boolean dayTypeEnabled;
    private String color = "#ffffff";
    private String description;
    private DayType currentDayType = null;
    private String currentPage;
    private int page = 1;
    private boolean selected;
    private Set<DayType> selectedDayTypes = new HashSet<>();
    private boolean selectRow = false;
    private String dayNameFilter;
    private String dayTypeDescriptionFilter;
    private SortOrder dayNameOrder = SortOrder.UNSORTED;
    private SortOrder dayTypeDescriptionOrder = SortOrder.UNSORTED;
    private boolean disableFields;


    public void begin() {
        me.setActiveMenu(MenuType.CALENDAR);
        refresh();
        me.redirect("/day/days.xhtml");
    }


    public List<DayType> getSelectionGrid() {
        List<DayType> dayTypes = new ArrayList<>();
        refresh();
        return dayTypeList;
    }

    private void refresh() {
        init();
    dayTypeList = dayTypeService.getAllDayType();
    }

    public void add() {
        init();
        setEditable("false");

    }

    public void doDelete() {
        currentDayType.setEffectorUser(me.getUsername());
            String condition =dayTypeService.deleteDayType(currentDayType);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/day/days.xhtml");
    }

    public void init() {
        title = "";
        dayTypeEnabled = true;
        color = "";
        description = "";
        page = 1;
        currentDayType = null;
        dayNameFilter = "";
        dayTypeDescriptionFilter = "";
        setSelectRow(false);
    }

    public void edit() {
        setEditable("true");
        setDisableFields(false);
            currentDayType =dayTypeService.findById(currentDayType.getId());
        title = currentDayType.getTitle();
        color = currentDayType.getColor();
        description = currentDayType.getDescription();
    }

    public void view() {
        setDisableFields(true);
            currentDayType =dayTypeService.findById(currentDayType.getId());
        title = currentDayType.getTitle();
        color = currentDayType.getColor();
        description = currentDayType.getDescription();
    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();
        }
    }

    private void doEdit() {
        currentDayType.setDescription(description);
        currentDayType.setColor(color);
        currentDayType.setTitle(title);
        currentDayType.setEffectorUser(me.getUsername());
            boolean condition =dayTypeService.editDayType(currentDayType);
            if (condition) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/day/days.xhtml");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }

    }

    private void doAdd() {
        DayType newDayType = new DayType();
        newDayType.setTitle(title);
        newDayType.setColor(color);
        newDayType.setDescription(description);
        newDayType.setDeleted("0");
        newDayType.setStatus("c");
        newDayType.setEffectorUser(me.getUsername());


        DayType insertedDayType = null;
            insertedDayType = dayTypeService.createDayType(newDayType);

        if (insertedDayType != null) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/day/days.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }

    }

//    public Filter<?> getDayNameFilterImpl() {
//        return new Filter<DayType>() {
//            public boolean accept(DayType day) {
//                return dayNameFilter == null || dayNameFilter.length() == 0 || day.getTitle().toLowerCase().contains(dayNameFilter.toLowerCase());
//            }
//        };
//    }

    public void sortByDayName() {
        dayTypeDescriptionOrder = SortOrder.UNSORTED;

        if (dayNameOrder.equals(SortOrder.ASCENDING)) {
            setDayNameOrder(SortOrder.DESCENDING);
        } else {
            setDayNameOrder(SortOrder.ASCENDING);
        }
    }


    public void sortByDayTypeDescription() {
        dayNameOrder = SortOrder.UNSORTED;

        if (dayTypeDescriptionOrder.equals(SortOrder.ASCENDING)) {
            setDayTypeDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setDayTypeDescriptionOrder(SortOrder.ASCENDING);
        }
    }

//    public Filter<?> getDayTypeDescriptionFilterImpl() {
//        return new Filter<DayType>() {
//            public boolean accept(DayType dayType) {
//                return dayTypeDescriptionFilter == null || dayTypeDescriptionFilter.length() == 0 || dayType.getDescription().toLowerCase().contains(dayTypeDescriptionFilter.toLowerCase());
//            }
//        };
//    }

    public void selectForEdit() {
        setSelectRow(true);

    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public List<DayType> getDayTypeList() {
        return dayTypeList;
    }

    public void setDayTypeList(List<DayType> dayTypeList) {
        this.dayTypeList = dayTypeList;
    }

    public String getDayTypeDescriptionFilter() {
        return dayTypeDescriptionFilter;
    }

    public void setDayTypeDescriptionFilter(String dayTypeDescriptionFilter) {
        this.dayTypeDescriptionFilter = dayTypeDescriptionFilter;
    }

    public SortOrder getDayTypeDescriptionOrder() {
        return dayTypeDescriptionOrder;
    }

    public void setDayTypeDescriptionOrder(SortOrder dayTypeDescriptionOrder) {
        this.dayTypeDescriptionOrder = dayTypeDescriptionOrder;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getDayTypeName() {
        return title;
    }

    public void setDayTypeName(String dayTypeName) {
        this.title = dayTypeName;
    }

    public boolean isDayTypeEnabled() {
        return dayTypeEnabled;
    }

    public void setDayTypeEnabled(boolean dayTypeEnabled) {
        this.dayTypeEnabled = dayTypeEnabled;
    }

    public String getPcIP() {
        return color;
    }

    public void setPcIP(String color) {
        this.color = color;
    }

    public String getPcLocation() {
        return description;
    }

    public void setPcLocation(String description) {
        this.description = description;
    }

    public Set<DayType> getSelectedDayTypes() {
        return selectedDayTypes;
    }

    public void setSelectedDayTypes(Set<DayType> selectedDayTypes) {
        this.selectedDayTypes = selectedDayTypes;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        currentDayType = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SortOrder getDayNameOrder() {
        return dayNameOrder;
    }

    public void setDayNameOrder(SortOrder dayNameOrder) {
        this.dayNameOrder = dayNameOrder;
    }

    public String getDayNameFilter() {
        return dayNameFilter;
    }

    public void setDayNameFilter(String dayNameFilter) {
        this.dayNameFilter = dayNameFilter;
    }

    public boolean isDisableFields() {
        return disableFields;
    }

    public void setDisableFields(boolean disableFields) {
        this.disableFields = disableFields;
    }

    public DayType getCurrentDayType() {
        return currentDayType;
    }

    public void setCurrentDayType(DayType currentDayType) {
        this.currentDayType = currentDayType;
    }
}