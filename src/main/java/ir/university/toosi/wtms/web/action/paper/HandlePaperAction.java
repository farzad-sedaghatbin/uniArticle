package ir.university.toosi.wtms.web.action.paper;


import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.Paper;
import ir.university.toosi.tms.model.service.PaperServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import org.primefaces.model.SortOrder;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author :  FarzadFarzad Sedaghatbin
 * @version : 0.8
 */

@ManagedBean(name = "handlePaperAction")
@SessionScoped
public class HandlePaperAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleRoleAction handleRoleAction;
    @EJB
    private PaperServiceImpl paperService;
    private List<Paper> paperList = null;
    private boolean editable;
    private boolean disableFields = true;
    private String paperName;
    private boolean paperEnabled;
    private String paperAuthor;
    private String paperDescription;
    private Paper currentPaper = null;
    private String currentPage;
    private int page = 1;
    private String paperIPFilter;
    private boolean selected;
    private Set<Paper> selectedPapers = new HashSet<>();
    private List<BLookup> locations;
    private boolean selectRow = false;
    private String paperNameFilter;
    private String paperDescriptionFilter;
    private SortOrder paperNameOrder = SortOrder.UNSORTED;
    private SortOrder paperDescriptionOrder = SortOrder.UNSORTED;


    public void begin() {
//        todo:solve this comment
//        me.setActiveMenu(MenuType.USER);
        System.out.println(me.getDirection());
        refresh();
        me.redirect("/paper/paper.xhtml");
    }

//    public void selectPapers(ValueChangeEvent event) {
//        currentPaper = paperList.getRowData();
//        boolean temp = (Boolean) event.getNewValue();
//        if (temp) {
//            currentPaper.setSelected(true);
//            selectedPapers.add(currentPaper);
//        } else {
//            currentPaper.setSelected(false);
//            selectedPapers.remove(currentPaper);
//        }
//    }

    public void changePapers(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            paperEnabled = true;
        } else
            paperEnabled = false;
    }

    public List<Paper> getSelectionGrid() {
        List<Paper> papers = new ArrayList<>();
        refresh();
        return paperList;
    }

    private void refresh() {
        init();
        List<Paper> papers = paperService.getAllPapers();
//        todo:must be uncommented
//            for (Paper paper : papers) {
//                paper.getLocation().setTitleText(paper.getName());
////                todo:the commented line is correct
////                paper.getLocation().setTitleText(me.getValue(paper.getLocation().getCode()));
//            }
            paperList = new ArrayList<>(papers);
    }

    public void add() {
        init();
        setEditable(false);
        setDisableFields(false);
    }


    public void doDelete() {

//        currentPaper.setEffectorUser(me.getUsername());
        paperService.deletePaper(currentPaper);
        refresh();
        me.addInfoMessage("delete was successful");
        me.redirect("/paper/paper.xhtml");
    }

    public void init() {
        paperName = "";

        paperEnabled = true;
        paperAuthor = "";
        page = 1;
        currentPaper = null;
        paperDescriptionFilter = "";
        paperIPFilter = "";
        paperNameFilter = "";
        paperDescriptionFilter = "";
        setSelectRow(false);
        setDisableFields(true);
    }

    public void edit() {
        setEditable(true);
        setDisableFields(false);
        paperName = currentPaper.getName();
        paperAuthor = currentPaper.getAuthor();
        paperDescription = currentPaper.getDescription();
    }

    public void view(){
        paperName = currentPaper.getName();
        paperAuthor = currentPaper.getAuthor();
        paperDescription = currentPaper.getDescription();
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
        currentPaper.setAuthor(paperAuthor);
        currentPaper.setName(paperName);

//        currentPaper.setEffectorUser(me.getUsername());
        boolean condition = paperService.editPaper(currentPaper);
            if (condition) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/paper/paper.xhtml");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }

    }

    private void doAdd() {
        System.out.println(me.getDirection());
        Paper newPaper = new Paper();
        newPaper.setName(paperName);
        newPaper.setAuthor(paperAuthor);
        newPaper.setDeleted("0");
        newPaper.setStatus("c");
//        newPaper.setEffectorUser(me.getUsername());
        newPaper.setDescription(paperDescription);
        boolean condition = paperService.existNotId(String.valueOf(paperAuthor));
            if (condition) {
                me.addInfoMessage("paper.exist");
                return;
            }

        Paper insertedPaper = null;
        insertedPaper = paperService.createPaper(newPaper);
        if (insertedPaper != null) {
            refresh();
//            me.addInfoMessage("operation.occurred");
            me.redirect("/paper/paper.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }

    }

//    public Filter<?> getPaperIPFilterImpl() {
//        return new Filter<Paper>() {
//            public boolean accept(Paper paper) {
//                return paperIPFilter == null || paperIPFilter.length() == 0 || paper.getIp().startsWith(paperIPFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getPcNameFilterImpl() {
//        return new Filter<Paper>() {
//            public boolean accept(Paper paper) {
//                return paperNameFilter == null || paperNameFilter.length() == 0 || paper.getName().toLowerCase().contains(paperNameFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getPaperDescriptionFilterImpl() {
//        return new Filter<Paper>() {
//            public boolean accept(Paper paper) {
//                return paperDescriptionFilter == null || paperDescriptionFilter.length() == 0 || paper.getLocation().getTitleText().toLowerCase().contains(paperDescriptionFilter.toLowerCase());
//            }
//        };
//    }


    public void sortByPcName() {
        paperNameOrder = newSortOrder(paperNameOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        paperNameOrder = SortOrder.UNSORTED;
        paperDescriptionOrder = SortOrder.UNSORTED;
        paperDescriptionOrder = SortOrder.UNSORTED;

        if (currentSortOrder.equals(SortOrder.DESCENDING)) {
            return SortOrder.ASCENDING;
        } else {
            return SortOrder.DESCENDING;
        }
    }

//    public void selectForEdit() {
//        currentPaper = paperList.getRowData();
//        setSelectRow(true);
//    }


    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public List<Paper> getPaperList() {
        return paperList;
    }

    public void setPaperList(List<Paper> paperList) {
        this.paperList = paperList;
    }


    public SortOrder getPaperDescriptionOrder() {
        return paperDescriptionOrder;
    }

    public void setPaperDescriptionOrder(SortOrder paperDescriptionOrder) {
        this.paperDescriptionOrder = paperDescriptionOrder;
    }

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public boolean isPaperEnabled() {
        return paperEnabled;
    }

    public void setPaperEnabled(boolean paperEnabled) {
        this.paperEnabled = paperEnabled;
    }

    public String getPcIP() {
        return paperAuthor;
    }

    public void setPcIP(String paperIP) {
        this.paperAuthor = paperIP;
    }

    public Set<Paper> getSelectedPapers() {
        return selectedPapers;
    }

    public void setSelectedPapers(Set<Paper> selectedPapers) {
        this.selectedPapers = selectedPapers;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        currentPaper = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Paper> getPcList() {
        return paperList;
    }

    public void setPcList(List<Paper> paperList) {
        this.paperList = paperList;
    }

    public String getPcName() {
        return paperName;
    }

    public void setPcName(String paperName) {
        this.paperName = paperName;
    }

    public boolean isPcEnabled() {
        return paperEnabled;
    }

    public void setPcEnabled(boolean paperEnabled) {
        this.paperEnabled = paperEnabled;
    }

    public Paper getCurrentPaper() {
        return currentPaper;
    }

    public void setCurrentPaper(Paper currentPaper) {
        this.currentPaper = currentPaper;
    }

    public SortOrder getPcDescriptionOrder() {
        return paperDescriptionOrder;
    }

    public void setPcDescriptionOrder(SortOrder paperDescriptionOrder) {
        this.paperDescriptionOrder = paperDescriptionOrder;
    }

    public String getPcDescriptionFilter() {
        return paperDescriptionFilter;
    }

    public void setPcDescriptionFilter(String paperDescriptionFilter) {
        this.paperDescriptionFilter = paperDescriptionFilter;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public SortOrder getPcNameOrder() {
        return paperNameOrder;
    }

    public void setPcNameOrder(SortOrder paperNameOrder) {
        this.paperNameOrder = paperNameOrder;
    }

    public String getPcNameFilter() {
        return paperNameFilter;
    }

    public void setPcNameFilter(String paperNameFilter) {
        this.paperNameFilter = paperNameFilter;
    }



    public String getPaperDescriptionFilter() {
        return paperDescriptionFilter;
    }

    public void setPaperDescriptionFilter(String paperDescriptionFilter) {
        this.paperDescriptionFilter = paperDescriptionFilter;
    }

    public void setPaperDescription(String paperDescription) {
        this.paperDescription = paperDescription;
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
        return paperIPFilter;
    }

    public void setPcIPFilter(String paperIPFilter) {
        this.paperIPFilter = paperIPFilter;
    }

    public boolean isDisableFields() {
        return disableFields;
    }

    public void setDisableFields(boolean disableFields) {
        this.disableFields = disableFields;
    }

    public Paper findForConverter(long value) {
        return paperService.findById(value);
    }
}