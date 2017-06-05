package ir.university.toosi.wtms.web.action.operation;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.IterableSerializer;
import ir.university.toosi.tms.model.entity.Operation;
import ir.university.toosi.tms.model.service.OperationServiceImpl;
import ir.university.toosi.tms.util.Configuration;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.converter.OperationConverter;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.SortOrder;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleOperationAction")
@SessionScoped
public class HandleOperationAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @EJB
    private OperationServiceImpl operationService;
    private DataModel<Operation> operationList = null;
    private String editable = "false";
    private String operationName;
    private boolean operationEnabled;
    private String description;
    private Operation currentOperation = null;
    private String currentPage;
    private int page = 1;
    private SortOrder operationDescriptionOrder = SortOrder.UNSORTED;
    private String operationDescriptionFilter;
    private boolean selected;
    private boolean selectAll;
    private Set<Operation> selectedOperations = new HashSet<>();
    private DualListModel<Operation> operations;

    public String begin() {
        refresh();
        return "list-operation";
    }


    public void onTransfer(TransferEvent event) {
        if (event.isAdd()) {
            for (Object item : event.getItems()) {
                ((Operation) item).setSelected(true);
                selectedOperations.add((Operation) item);
            }
        } else {
            for (Object item : event.getItems()) {
                ((Operation) item).setSelected(false);
                Iterator<Operation> iterator = selectedOperations.iterator();
                while (iterator.hasNext()){
                    if (iterator.next().getName().equals(((Operation) item).getName())){
                        iterator.remove();
                    }
                }
            }
        }
    }


    public DataModel<Operation> getSelectionGrid() {
        List<Operation> operations = new ArrayList<>();
        refresh();
        return operationList;
    }

    public void refresh() {
        page = 1;
        List<Operation> operations = operationService.getAllOperation();
        for (Operation operation : operations) {
            operation.setDescription(operation.getDescription());
        }
        this.operations = new DualListModel(operations, new ArrayList());
    }

    public void add() {
        init();
        setEditable("false");

    }

    public void doDelete() {
        currentOperation = operationList.getRowData();
        String condition = operationService.deleteOperation(currentOperation);
        if (condition.equalsIgnoreCase("true")) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/operation/list-operation.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }

    public void init() {
        operationName = "";
        operationEnabled = true;
        description = "";
    }

    public void edit() {
        setEditable("true");
        currentOperation = operationList.getRowData();
        operationEnabled = currentOperation.isEnabled();
        description = currentOperation.getDescription();
    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();
        }
    }

    private void doEdit() {
        currentOperation.setEnabled(operationEnabled);
        currentOperation.setDescription(description);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editOperation");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentOperation)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/operation/list-operation.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void backUp() {

        try {
            operationService.backupDerby();
        } catch (SQLException e) {
            me.addErrorMessage(e.getMessage());
        }
        me.addInfoMessage("backup_done");
        me.redirect("/dashboard.xhtml");
    }

    private void doAdd() {
        Operation newOperation = new Operation();
        newOperation.setDescription(description);
        newOperation.setEnabled(operationEnabled);
        newOperation.setDeleted("0");
        newOperation.setStatus("c");
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/existOperation");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newOperation)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createOperation");
        Operation insertedOperation = null;
        try {
            insertedOperation = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newOperation)), Operation.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (insertedOperation != null) {
//            me.getGeneralHelper().getWebServiceInfo().setServiceName("/createLanguageManagement");
//            LanguageManagement languageManagement = new LanguageManagement();
//            languageManagement.setTitle(newOperation.getDescription());
//            languageManagement.setType(me.getLanguages());
//            try {
//                languageManagement = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(languageManagement)), LanguageManagement.class);
//
//                me.getGeneralHelper().getWebServiceInfo().setServiceName("/createLanguageKeyManagement");
//                LanguageKeyManagement languageKeyManagement = new LanguageKeyManagement();
//                languageKeyManagement.setDescriptionKey(newOperation.getName());
//                Set list = new HashSet();
//                list.add(languageManagement);
//                languageKeyManagement.setLanguageManagements(list);
//
//                languageKeyManagement = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(languageKeyManagement)), LanguageKeyManagement.class);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/operation/list-operation.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }

    }


    public void sortByOperationDescription() {
        if (operationDescriptionOrder.equals(SortOrder.ASCENDING)) {
            setOperationDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setOperationDescriptionOrder(SortOrder.ASCENDING);
        }
    }

    public Operation findForConverter(String value) {
        return operationService.findById(value);
    }

    public OperationConverter getConverter() {
        return new OperationConverter();
    }

/*    public Filter<?> getOperationDescriptionFilterImpl() {
        return new Filter<Operation>() {
            public boolean accept(Operation operation) {
                return operationDescriptionFilter == null || operationDescriptionFilter.length() == 0 || operation.getDescription().toLowerCase().contains(operationDescriptionFilter.toLowerCase());
            }
        };
    }*/

    public DataModel<Operation> getOperationList() {
        return operationList;
    }

    public void setOperationList(DataModel<Operation> operationList) {
        this.operationList = operationList;
    }

    public String getOperationDescriptionFilter() {
        return operationDescriptionFilter;
    }

    public void setOperationDescriptionFilter(String operationDescriptionFilter) {
        this.operationDescriptionFilter = operationDescriptionFilter;
    }

    public SortOrder getOperationDescriptionOrder() {
        return operationDescriptionOrder;
    }

    public void setOperationDescriptionOrder(SortOrder operationDescriptionOrder) {
        this.operationDescriptionOrder = operationDescriptionOrder;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public boolean isOperationEnabled() {
        return operationEnabled;
    }

    public void setOperationEnabled(boolean operationEnabled) {
        this.operationEnabled = operationEnabled;
    }

    public Set<Operation> getSelectedOperations() {
        return selectedOperations;
    }

    public void setSelectedOperations(Set<Operation> selectedOperations) {
        this.selectedOperations = selectedOperations;
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

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Operation getCurrentOperation() {
        return currentOperation;
    }

    public void setCurrentOperation(Operation currentOperation) {
        this.currentOperation = currentOperation;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public DualListModel<Operation> getOperations() {
        if (operations == null) {
            operations = new DualListModel<>();
        }
        return operations;
    }

    public void setOperations(DualListModel<Operation> operations) {
        this.operations = operations;
    }
}