package ir.university.toosi.wtms.web.action.organ;


import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.Lookup;
import ir.university.toosi.tms.model.entity.personnel.Organ;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.service.BLookupServiceImpl;
import ir.university.toosi.tms.model.service.personnel.OrganServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.SortOrder;
import org.primefaces.model.TreeNode;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

/**
 * @author :  FarzadMostafa Rastgar
 * @version : 0.8
 */

@Named(value = "handleOrganAction")
@SessionScoped
public class HandleOrganAction implements Serializable {
    @Inject
    private UserManagementAction me;
    @EJB
    private OrganServiceImpl organService;
    @EJB
    private PersonServiceImpl personService;
    @EJB
    private BLookupServiceImpl bLookupService;
    @Inject
    private HandleRoleAction handleRoleAction;
    private List<Organ> organList = null;
    private List<Person> personList = null;
    private Organ parentOrgan = null;
    private boolean addChild = false;
    private String editable = "false";
    private String organName;
    private String organTitle;
    private String organCode;
    private BLookup organType;
    private List<BLookup> organTypes;
    private Organ currentOrgan = null;
    private DefaultTreeNode selectedNode = null;
    private String currentPage;
    private int page = 1;
    private int pageInPopup = 1;
    private boolean selected;
    private Set<Organ> selectedOrgans = new HashSet<>();
    private TreeNode rootOrgans;
    private boolean inheritance;
    private String name;
    private boolean selectRow = false;
    private String organNameFilter;
    private String organTypeFilter;
    private String organDescriptionFilter;
    private SortOrder organNameOrder = SortOrder.UNSORTED;
    private SortOrder organTypeOrder = SortOrder.UNSORTED;
    private SortOrder organDescriptionOrder = SortOrder.UNSORTED;
    private int personPage = 1;
    private String listPerson;
    private boolean disableFields;
    private List<Organ> organs;


    public void begin() {
//        me.setActiveMenu(MenuType.MANAGEMENT);
        refresh();
        me.redirect("/organ/organs.xhtml");
    }

    public List<Organ> getSelectionGrid() {
        refresh();
        return organList;
    }

    private void refresh() {
        init();
        List<Organ> organs;
        if (parentOrgan != null) {
            organList = organService.getAllActiveOrganByParent(parentOrgan.getId());
        } else {
            organList = organService.getAllActiveOrgan();
        }
        page = 1;
    }


    public void add() {
        init();
        setEditable("false");
        setDisableFields(false);
    }

    public void addChild() {
        init();
        setEditable("false");
        setDisableFields(false);
        addChild = true;
        currentOrgan = (Organ) selectedNode.getData();
        parentOrgan = currentOrgan;
    }

    public void doDelete() {
        currentOrgan=((Organ)selectedNode.getData());
        currentOrgan.setEffectorUser(me.getUsername());
        String condition = organService.deleteOrgan(currentOrgan);
        refresh();
        me.addInfoMessage(condition);
        me.redirect("/organ/organs.xhtml");
    }

    public void init() {
        organName = "";
        addChild = false;
        organTitle = "";
        organCode = "";
        organType = null;
        page = 1;
        organTypeFilter = "";
        personPage = 1;
        rootOrgans = null;
        currentOrgan = null;
        organNameFilter = "";
        organDescriptionFilter = "";
        parentOrgan = null;
        setSelectRow(false);
    }

    public void changeParentOrgan(Long id) {
        if (id.equals(-1l)) {
            parentOrgan = null;
        } else {
            parentOrgan = organService.findById(id);
        }
        refresh();
    }

    public void changeParentOrgan() {
//        parentOrgan = organList.getRowData();
        refresh();
    }

    public void listPerson() {
        currentOrgan=((Organ)selectedNode.getData());
        personList = personService.findByOrgan(currentOrgan.getId());
    }

    public void edit() {
        setEditable("true");
        setDisableFields(false);
        currentOrgan=((Organ)selectedNode.getData());
        currentOrgan = organService.findById(currentOrgan.getId());
        organName = currentOrgan.getName();
        organTitle = currentOrgan.getTitle();
        organCode = currentOrgan.getCode();
        organType = currentOrgan.getOrganType();
    }

    public void view() {
        setEditable("true");
        setDisableFields(true);
        currentOrgan=((Organ)selectedNode.getData());
        currentOrgan = organService.findById(currentOrgan.getId());
        organName = currentOrgan.getName();
        organTitle = currentOrgan.getTitle();
        organCode = currentOrgan.getCode();
        organType = currentOrgan.getOrganType();
    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();
        }
    }

    public void viewPerson() {
        currentOrgan=((Organ)selectedNode.getData());
        personList = personService.findByOrgan(currentOrgan.getId());
    }

    private void doEdit() {

        currentOrgan.setOrganType(getOrganType());
        currentOrgan.setCode(organCode);
        currentOrgan.setTitle(organTitle);
        currentOrgan.setName(organName);
        currentOrgan.setInheritance(inheritance);
        currentOrgan.setEffectorUser(me.getUsername());
        if (parentOrgan != null) {
            currentOrgan.setParentOrgan(parentOrgan);
        } else {
            currentOrgan.setParentOrgan(null);
        }
        boolean condition = organService.editOrgan(currentOrgan);
        if (condition) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/organ/organs.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }

    }

    private void doAdd() {
        Organ newOrgan = new Organ();
        newOrgan.setName(organName);
        newOrgan.setTitle(organTitle);
        newOrgan.setCode(organCode);
        newOrgan.setInheritance(inheritance);
        newOrgan.setOrganType(getOrganType());
        if (parentOrgan != null) {
            newOrgan.setParentOrgan(parentOrgan);
        } else {
            newOrgan.setParentOrgan(null);
        }
        newOrgan.setDeleted("0");
        newOrgan.setStatus("c");
        newOrgan.setEffectorUser(me.getUsername());

        boolean condition = organService.existOrgan(newOrgan);
        if (condition) {

            me.addInfoMessage("organ.exist");
            return;
        }


        Organ insertedOrgan = null;
        insertedOrgan = organService.createOrgan(newOrgan);

        if (insertedOrgan != null) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/organ/organs.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }

    }



/*
    public Filter<?> getOrganNameFilterImpl() {
        return new Filter<Organ>() {
            public boolean accept(Organ organ) {
                return organNameFilter == null || organNameFilter.length() == 0 || organ.getName().toLowerCase().contains(organNameFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getOrganTypeFilterImpl() {
        return new Filter<Organ>() {
            public boolean accept(Organ organ) {
                return StringUtils.isEmpty(organTypeFilter) || organ.getOrganType().getTitleText().toLowerCase().contains(organTypeFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getOrganDescriptionFilterImpl() {
        return new Filter<Organ>() {
            public boolean accept(Organ organ) {
                return organDescriptionFilter == null || organDescriptionFilter.length() == 0 || organ.getTitle().toLowerCase().contains(organDescriptionFilter.toLowerCase());
            }
        };
    }*/

    public void sortByOrganName() {
        organNameOrder = newSortOrder(organNameOrder);
    }

    public void sortByOrganType() {
        organTypeOrder = newSortOrder(organTypeOrder);
    }

    public void sortByOrganDescription() {
        organDescriptionOrder = newSortOrder(organDescriptionOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        organNameOrder = SortOrder.UNSORTED;
        organTypeOrder = SortOrder.UNSORTED;
        organDescriptionOrder = SortOrder.UNSORTED;

        if (currentSortOrder.equals(SortOrder.DESCENDING)) {
            return SortOrder.ASCENDING;
        } else {
            return SortOrder.DESCENDING;
        }
    }

    public List<Organ> getAllParents() {
        List<Organ> parents = new ArrayList();
        addParents(parents, parentOrgan);
        return parents;
    }

    private void addParents(List<Organ> parents, Organ parentOrgan) {
        if (parentOrgan != null) {
            addParents(parents, parentOrgan.getParentOrgan());
        }
        parents.add(parentOrgan);
    }

    public void selectOrganType(ValueChangeEvent event) {
        Long selectedId = (Long) event.getNewValue();
        for (BLookup bLookup : getOrganTypes()) {
            if (selectedId.equals(bLookup.getId())) {
                organType = bLookup;
            }
        }
    }

    public void selectForEdit() {
//        currentOrgan = organList.getRowData();
        setSelectRow(true);
    }


    public String getOrganDescriptionFilter() {
        return organDescriptionFilter;
    }

    public void setOrganDescriptionFilter(String organDescriptionFilter) {
        this.organDescriptionFilter = organDescriptionFilter;
    }

    public SortOrder getOrganDescriptionOrder() {
        return organDescriptionOrder;
    }

    public void setOrganDescriptionOrder(SortOrder organDescriptionOrder) {
        this.organDescriptionOrder = organDescriptionOrder;
    }

    public Organ getParentOrgan() {
        return parentOrgan;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getOrganTitle() {
        return organTitle;
    }

    public void setOrganTitle(String organTitle) {
        this.organTitle = organTitle;
    }

    public String getOrganCode() {
        return organCode;
    }

    public void setOrganCode(String organCode) {
        this.organCode = organCode;
    }

    public BLookup getOrganType() {
        if (organType == null) {
            if (getOrganTypes().size() > 0) {
                organType = getOrganTypes().get(0);
            }
        }
        return organType;
    }

    public void selectInheritance(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            inheritance = true;
        } else {
            inheritance = false;
        }
    }


    public void setOrganType(BLookup organType) {
        this.organType = organType;
    }

    public List<BLookup> getOrganTypes() {
        if (organTypes == null) {
            organTypes = bLookupService.getByLookupId(Lookup.ORGAN_TYPE_ID);
            for (BLookup bLookup : organTypes) {
                bLookup.setTitleText(me.getValue(bLookup.getCode()));
            }
        }

        return organTypes;
    }

    public void resetPersonPage() {
        setPersonPage(1);
    }

    public Set<Organ> getSelectedOrgans() {
        return selectedOrgans;
    }

    public void setSelectedOrgans(Set<Organ> selectedOrgans) {
        this.selectedOrgans = selectedOrgans;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        currentOrgan = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public Organ getCurrentOrgan() {
        return currentOrgan;
    }

    public void setCurrentOrgan(Organ currentOrgan) {
        this.currentOrgan = currentOrgan;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public TreeNode getRootOrgans() {
        HashMap<Long, Boolean> hashMap = new HashMap();
        TreeNode root = new DefaultTreeNode(new Organ("*****", null, null, null), null);
        if (rootOrgans == null) {
            List<Organ> organs = organService.getAllOrgan();
            organs = Organ.prepareHierarchy(organs);
            for (Organ organ : organs) {
                if (organ.getParent() == null) {
                    recurciveNodes(new DefaultTreeNode(organ, root));
                }
            }
        }
        return root;
    }

    private TreeNode recurciveNodes(TreeNode organ) {
        if (organ.getChildren().size() != 0) {
            for (TreeNode treeNode : organ.getChildren()) {

            }
        }
        if (((Organ) organ.getData()) == null || ((Organ) organ.getData()).getChildren() == null || ((Organ) organ.getData()).getChildren().size() == 0) {
            return organ;
        } else {
            for (TreeNode treeNode : ((Organ) organ.getData()).getChildren()) {
                recurciveNodes(new DefaultTreeNode(treeNode, organ));
            }

        }
        return organ;

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

    public void setParentOrgan(Organ parentOrgan) {
        this.parentOrgan = parentOrgan;
    }

    public void setOrganTypes(List<BLookup> organTypes) {
        this.organTypes = organTypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public int getPageInPopup() {
        return pageInPopup;
    }

    public void setPageInPopup(int pageInPopup) {
        this.pageInPopup = pageInPopup;
    }

    public SortOrder getOrganNameOrder() {
        return organNameOrder;
    }

    public void setOrganNameOrder(SortOrder organNameOrder) {
        this.organNameOrder = organNameOrder;
    }

    public String getOrganNameFilter() {
        return organNameFilter;
    }

    public void setOrganNameFilter(String organNameFilter) {
        this.organNameFilter = organNameFilter;
    }

    public String getOrganTypeFilter() {
        return organTypeFilter;
    }

    public void setOrganTypeFilter(String organTypeFilter) {
        this.organTypeFilter = organTypeFilter;
    }

    public SortOrder getOrganTypeOrder() {
        return organTypeOrder;
    }

    public void setOrganTypeOrder(SortOrder organTypeOrder) {
        this.organTypeOrder = organTypeOrder;
    }


    public int getPersonPage() {
        return personPage;
    }

    public void setPersonPage(int personPage) {
        this.personPage = personPage;
    }

    public String getListPerson() {
        return listPerson;
    }

    public void setListPerson(String listPerson) {
        this.listPerson = listPerson;
    }

    public List<Organ> getOrganList() {
        return organList;
    }

    public void setOrganList(List<Organ> organList) {
        this.organList = organList;
    }

    public boolean isDisableFields() {
        return disableFields;
    }

    public void setDisableFields(boolean disableFields) {
        this.disableFields = disableFields;
    }


    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public DefaultTreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(DefaultTreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public List<Organ> getOrgans() {
        return organs;
    }

    public void setOrgans(List<Organ> organs) {
        this.organs = organs;
    }
}
