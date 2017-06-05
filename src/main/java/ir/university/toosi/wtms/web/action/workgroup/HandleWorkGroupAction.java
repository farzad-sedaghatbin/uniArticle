package ir.university.toosi.wtms.web.action.workgroup;


import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.Role;
import ir.university.toosi.tms.model.entity.WorkGroup;
import ir.university.toosi.tms.model.service.RoleServiceImpl;
import ir.university.toosi.tms.model.service.WorkGroupServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import ir.university.toosi.wtms.web.action.user.HandleUserAction;
import ir.university.toosi.wtms.web.converter.WorkgroupConverter;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.SortOrder;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleWorkGroupAction")
@SessionScoped
public class HandleWorkGroupAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleRoleAction handleRoleAction;
    @Inject
    private HandleUserAction handleUserAction;

    @EJB
    private WorkGroupServiceImpl workGroupService;

    @EJB
    private RoleServiceImpl roleService;
    private List<WorkGroup> workGroupList = null;
    private WorkGroup currentWorkGroup = null;
    private String editable = "false";
    private String description;
    private String descText;
    private String name;
    private String status;
    private List<WorkGroup> workGroups;
    private String currentPage;
    private String selectedWorkgroup = "0";
    private String workGroupTitleFilter;
    private List<Role> roleSelectionGrid = null;
    private WorkGroup selectedWorkGroup = new WorkGroup();
    private List<Role> lastSelected;
    private Set<WorkGroup> selectWorkGroups;
    private String titleFilter;
    private boolean workGroupEnabled;
    private boolean selectAll;
    private int page = 1;
    private String workGroupDescriptionFilter;
    private boolean selectRow = false;
    private SortOrder titleOrder = SortOrder.UNSORTED;
    private SortOrder workGroupTitleOrder = SortOrder.UNSORTED;
    private SortOrder workGroupDescriptionOrder = SortOrder.UNSORTED;
    private DualListModel<WorkGroup> workgroups;
    private boolean disableFields;

    public void begin() {
        me.setActiveMenu(MenuType.USER);
        refresh();
        me.redirect("/workgroup/workgroups.xhtml");
    }

    public void init() {
        description = "";
        descText = "";
        selectAll = false;
        workGroupEnabled = true;
        status = "true";
        page = 1;
        currentWorkGroup = null;
        setSelectRow(false);
        titleFilter = "";
        workGroupDescriptionFilter = "";
        workGroupTitleFilter = "";
    }

    public void refresh() {
        init();
        workGroupList = workGroupService.getAllWorkGroup();
        for (WorkGroup workGroup : workGroupList) {
            workGroup.setDescText(me.getValue(workGroup.getDescription()));
        }
        workgroups = new DualListModel<>(workGroupList, new ArrayList<WorkGroup>());
    }

    public void fillGrid() {
        for (WorkGroup workGroup : workGroups) {
            for (WorkGroup selectWorkGroup : selectWorkGroups) {
                if (workGroup.getId() == selectWorkGroup.getId())
                    workGroup.setSelected(true);
            }
        }
        workGroupList = workGroups;
    }


    public void doDelete() {
        currentWorkGroup.setEffectorUser(me.getUsername());
        String condition = workGroupService.deleteWorkGroup(currentWorkGroup);
        refresh();
        me.addInfoMessage(condition);
        me.redirect("/workgroup/workgroups.xhtml");


    }

    public void selectAllWorkGroup(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (WorkGroup workGroup : workGroupList) {
                workGroup.setSelected(true);
                selectWorkGroups.add(workGroup);
            }
        } else {
            for (WorkGroup workGroup : workGroupList) {
                workGroup.setSelected(false);

            }
            selectWorkGroups.clear();
        }
    }

    public void add(String currentPage) {
        init();
        setEditable("false");
        roleSelectionGrid = handleRoleAction.getSelectionGrid();
        handleRoleAction.setSelectedRoles(new HashSet<Role>());

        for (Role role : roleSelectionGrid) {
            role.setSelected(false);
        }

        handleRoleAction.setCurrentPage(currentPage);
        handleRoleAction.setRoles(new DualListModel<Role>(roleSelectionGrid, new ArrayList<Role>()));
    }

    public void doAdd() {
      /*  if((descText == null) || (descText.isEmpty())){
             me.addErrorMessage("description_is_empty");
            return;
        }
        if((descText == null) || (descText.isEmpty())){
             me.addErrorMessage("description_text_is_empty");
            return;
        }
        if((status == null) || (status.isEmpty())){
             me.addErrorMessage("status_is_empty");
            return;
        }*/
        WorkGroup newWorkgroup = new WorkGroup();
        newWorkgroup.setDescription(descText);
        newWorkgroup.setDescText(descText);
        newWorkgroup.setName(name);
        newWorkgroup.setEnabled(status);
        newWorkgroup.setDeleted("0");
        newWorkgroup.setStatus("c");
        newWorkgroup.setEffectorUser(me.getUsername());

        Set<Role> selectedRole = new HashSet<>();
        for (Role role : handleRoleAction.getSelectedRoles()) {
            selectedRole.add(role);
        }

        newWorkgroup.setRoles(selectedRole);
        WorkGroup insertedWorkGroup = null;
        insertedWorkGroup = workGroupService.createWorkGroup(newWorkgroup);

        if (insertedWorkGroup != null) {

            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/workgroup/workgroups.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }

    }

    public void onTransfer(TransferEvent event) {
        if (event.isAdd()) {
            for (Object item : event.getItems()) {
                ((WorkGroup) item).setSelected(true);
                selectWorkGroups.add((WorkGroup) item);
            }
        } else {
            for (Object item : event.getItems()) {
                ((WorkGroup) item).setSelected(false);
                Iterator<WorkGroup> iterator = selectWorkGroups.iterator();
                while (iterator.hasNext()){
                    if (iterator.next().getName().equals(((WorkGroup) item).getName())){
                        iterator.remove();
                    }
                }
            }
        }
    }

    public WorkGroup findForConverter(long value) {
        return workGroupService.findById(value);
    }

    public WorkgroupConverter getConverter() {
        return new WorkgroupConverter();
    }

    public void edit() {
        setEditable("true");
        setDisableFields(false);
        currentWorkGroup = workGroupService.findById(currentWorkGroup.getId());  //To change body of catch statement use File | Settings | File Templates.

        workGroupEnabled = Boolean.valueOf(currentWorkGroup.getEnabled());
        descText = currentWorkGroup.getDescription();
        status = currentWorkGroup.getEnabled();
        name = currentWorkGroup.getName();

        List<Role> roles;
        if (currentWorkGroup.getRoles() == null || currentWorkGroup.getRoles().isEmpty()) {
            roles = roleService.getAllRole();
        } else {
            roles = roleService.getAllRoleForWorkgroupEdit(currentWorkGroup.getRoles());
        }

        handleRoleAction.getSelectedRoles().clear();
        handleRoleAction.getSelectedRoles().addAll(currentWorkGroup.getRoles());

        handleRoleAction.setRoles(new DualListModel<>(roles, new ArrayList<>(currentWorkGroup.getRoles())));
    }

    public void doEdit() {
        currentWorkGroup.setDescText(descText);
        currentWorkGroup.setEnabled(status);
        currentWorkGroup.setRoles(new HashSet<>(handleRoleAction.getRoles().getTarget()));
        currentWorkGroup.setEffectorUser(me.getUsername());
        currentWorkGroup.setName(name);
        boolean condition = workGroupService.editWorkGroup(currentWorkGroup);
        if (condition) {
            handleRoleAction.setSelectedRoles(new HashSet<Role>());
            refresh();
            handleRoleAction.setSelectedRoles(new HashSet<Role>());
            me.addInfoMessage("operation.occurred");
            me.redirect("/workgroup/workgroups.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }

    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();

        } else {
            doEdit();
        }
    }

    public void selectWorkGroups(ValueChangeEvent event) {
//        currentWorkGroup = workGroupList.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentWorkGroup.setSelected(true);
            selectWorkGroups.add(currentWorkGroup);
        } else {
            currentWorkGroup.setSelected(false);
            selectWorkGroups.remove(currentWorkGroup);
        }
    }

    public void workGroupChange(ValueChangeEvent event) {
        handleRoleAction.setRoleList(new ArrayList<Role>());
        String id = (String) event.getNewValue();
        if (!id.equalsIgnoreCase("0")) {
            WorkGroup workGroup = null;//me.getGeneralHelper().getWorkGroupService().findById(id);
            if (workGroup != null)
                handleRoleAction.setRoleList(new ArrayList<>(workGroup.getRoles()));
            selectedWorkGroup = workGroup;
        }
    }

    public void workGroupEnableChange(ValueChangeEvent event) {
        workGroupEnabled = (Boolean) event.getNewValue();
        if (workGroupEnabled) {
            status = "true";
        } else
            status = "false";

    }

    public WorkGroup findById(String id) {
        return workGroupService.findById(Long.parseLong(id));
    }

//    public Filter<?> getWorkGroupDescriptionFilterImpl() {
//        return new Filter<WorkGroup>() {
//            public boolean accept(WorkGroup workGroup) {
//                return workGroupDescriptionFilter == null || workGroupDescriptionFilter.length() == 0 || workGroup.getDescText().toLowerCase().contains(workGroupDescriptionFilter.toLowerCase());
//            }
//        };
//    }

    public void selectForEdit() {
//        currentWorkGroup = workGroupList.getRowData();
        setSelectRow(true);

    }


    public void sortByTitle() {
        titleOrder = newSortOrder(titleOrder);
    }


    public void sortByWorkGroupTitle() {
        workGroupTitleOrder = newSortOrder(workGroupTitleOrder);
    }

    public void sortByWorkGroupDescription() {
        workGroupDescriptionOrder = newSortOrder(workGroupDescriptionOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        titleOrder = SortOrder.UNSORTED;
        workGroupTitleOrder = SortOrder.UNSORTED;
        workGroupDescriptionOrder = SortOrder.UNSORTED;

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

    public HandleUserAction getHandleUserAction() {
        return handleUserAction;
    }

    public void setHandleUserAction(HandleUserAction handleUserAction) {
        this.handleUserAction = handleUserAction;
    }

    public List<WorkGroup> getWorkGroupList() {
        return workGroupList;
    }

    public void setWorkGroupList(List<WorkGroup> workGroupList) {
        this.workGroupList = workGroupList;
    }

    public WorkGroup getCurrentWorkGroup() {
        return currentWorkGroup;
    }

    public void setCurrentWorkGroup(WorkGroup currentWorkGroup) {
        this.currentWorkGroup = currentWorkGroup;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getSelectedWorkgroup() {
        return selectedWorkgroup;
    }

    public void setSelectedWorkgroup(String selectedWorkgroup) {
        this.selectedWorkgroup = selectedWorkgroup;
    }

    public SortOrder getWorkGroupTitleOrder() {
        return workGroupTitleOrder;
    }

    public void setWorkGroupTitleOrder(SortOrder workGroupTitleOrder) {
        this.workGroupTitleOrder = workGroupTitleOrder;
    }

    public List<Role> getRoleSelectionGrid() {
        return roleSelectionGrid;
    }

    public void setRoleSelectionGrid(List<Role> roleSelectionGrid) {
        this.roleSelectionGrid = roleSelectionGrid;
    }

    public WorkGroup getSelectedWorkGroup() {
        return selectedWorkGroup;
    }

    public void setSelectedWorkGroup(WorkGroup selectedWorkGroup) {
        this.selectedWorkGroup = selectedWorkGroup;
    }

    public SortOrder getTitleOrder() {
        return titleOrder;
    }

    public void setTitleOrder(SortOrder titleOrder) {
        this.titleOrder = titleOrder;
    }

    public boolean isWorkGroupEnabled() {
        return workGroupEnabled;
    }

    public void setWorkGroupEnabled(boolean workGroupEnabled) {
        this.workGroupEnabled = workGroupEnabled;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        currentWorkGroup = null;
        setSelectRow(false);
        this.page = page;
    }

    public Set<WorkGroup> getSelectWorkGroups() {
        return selectWorkGroups;
    }

    public void setSelectWorkGroups(Set<WorkGroup> selectWorkGroups) {
        this.selectWorkGroups = selectWorkGroups;
    }

    public SortOrder getWorkGroupDescriptionOrder() {
        return workGroupDescriptionOrder;
    }

    public void setWorkGroupDescriptionOrder(SortOrder workGroupDescriptionOrder) {
        this.workGroupDescriptionOrder = workGroupDescriptionOrder;
    }

    public String getWorkGroupDescriptionFilter() {
        return workGroupDescriptionFilter;
    }

    public void setWorkGroupDescriptionFilter(String workGroupDescriptionFilter) {
        this.workGroupDescriptionFilter = workGroupDescriptionFilter;
    }

    public List<WorkGroup> getWorkGroups() {
        return workGroups;
    }

    public void setWorkGroups(List<WorkGroup> workGroups) {
        this.workGroups = workGroups;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWorkGroupTitleFilter(String workGroupTitleFilter) {
        this.workGroupTitleFilter = workGroupTitleFilter;
    }

    public List<Role> getLastSelected() {
        return lastSelected;
    }

    public void setLastSelected(List<Role> lastSelected) {
        this.lastSelected = lastSelected;
    }

    public void setTitleFilter(String titleFilter) {
        this.titleFilter = titleFilter;
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

    public String getWorkGroupTitleFilter() {
        return workGroupTitleFilter;
    }

    public String getTitleFilter() {
        return titleFilter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DualListModel<WorkGroup> getWorkgroups() {
        if (workgroups == null) {
            workgroups = new DualListModel<>();
        }
        return workgroups;
    }

    public void setWorkgroups(DualListModel<WorkGroup> workgroups) {
        this.workgroups = workgroups;
    }

    public boolean isDisableFields() {
        return disableFields;
    }

    public void setDisableFields(boolean disableFields) {
        this.disableFields = disableFields;
    }
}