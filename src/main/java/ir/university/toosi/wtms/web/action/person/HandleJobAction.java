package ir.university.toosi.wtms.web.action.person;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.util.LangUtil;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.Lookup;
import ir.university.toosi.tms.model.entity.WebServiceInfo;
import ir.university.toosi.tms.model.entity.personnel.Job;
import ir.university.toosi.wtms.web.util.CalendarUtil;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleJobAction")
@SessionScoped
public class HandleJobAction implements Serializable {
    @Inject
    private UserManagementAction me;
    @Inject
    private GeneralHelper generalHelper;
    @Inject
    private HandlePersonAction handlePersonAction;
    private DataModel<Job> personList = null;
    private Job currentJob = null;
    private Job newJob = null;
    private String employNo;
    private BLookup employeeType;
    private List<BLookup> employeeTypes;
    private String folderNo;
    private String internalTel;
    private BLookup assistType;
    private List<BLookup> assistTypes;
    private BLookup postType;
    private List<BLookup> postTypes;
    private String description;
    private String editable = "false";
    private boolean enabled = true;
    private String lock = "false";
    private int page = 1;
    private Job selectedJob;


    public void init() {
        enabled = false;
        page = 1;
        employNo = "";
        folderNo = "";
        internalTel = "";
        description = "";

    }

    private void refresh() {
        init();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllJob");
        List<Job> innerJobList = null;
        try {
            innerJobList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Job>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        personList = new ListDataModel<>(innerJobList);
    }

    public void personChange(ValueChangeEvent event) {
        String id = (String) event.getNewValue();
        if (!id.equalsIgnoreCase("0")) {
            selectedJob = null;//me.getGeneralHelper().getWorkGroupService().findById(id);
        } else {
            selectedJob = null;
        }
    }

    public void doDelete() {
        String currentDate = LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd"));
        String currentTime = CalendarUtil.getTime(new Date(), new Locale("fa"));
        currentJob = personList.getRowData();
//        currentJob.setCreateDate(currentDate);
//        currentJob.setCreateTime(currentTime);
        currentJob.setStatus("o," + me.getUsername());
        currentJob.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteJob");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentJob)), String.class);
            refresh();
            me.addInfoMessage(condition);
                me.redirect("/person/list-person.htm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findJobByPersonId");
        currentJob = null;
        try {
            currentJob = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(handlePersonAction.getCurrentPerson().getId())), Job.class);
        } catch (IOException e) {
        }
        if (currentJob == null) {
            setEditable("false");
            init();
        } else {
            setEditable("true");
            edit();
        }

    }

    public void doAdd() {
        String currentDate = LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd"));
        String currentTime = CalendarUtil.getTime(new Date(), new Locale("fa"));
        newJob = new Job();
        newJob.setDeleted("0");
        newJob.setEffectorUser(me.getUsername());
        newJob.setStatus("c");
        newJob.setEffectorUser(me.getUsername());
        newJob.setAssistType(assistType);
        newJob.setDescription(description);
        newJob.setEmployNo(employNo);
        newJob.setInternalTel(internalTel);
        newJob.setPostType(postType);
        newJob.setPerson(handlePersonAction.getCurrentPerson());
        newJob.setFolderNo(folderNo);
        newJob.setEmployType(employeeType);

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createJob");
        Job job = null;
        try {
            job = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newJob)), Job.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (job != null) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/person/list-person.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }

    public void edit() {
        description = currentJob.getDescription();
        internalTel = currentJob.getInternalTel();
        folderNo = currentJob.getFolderNo();
        employNo = currentJob.getEmployNo();
        employeeType = currentJob.getEmployType();
        postType = currentJob.getPostType();
        assistType = currentJob.getAssistType();

    }

    public void doEdit() {
        String currentDate = LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd"));
        String currentTime = CalendarUtil.getTime(new Date(), new Locale("fa"));
        currentJob.setEffectorUser(me.getUsername());
        currentJob.setAssistType(assistType);
        currentJob.setDescription(description);
        currentJob.setEmployNo(employNo);
        currentJob.setInternalTel(internalTel);
        currentJob.setPostType(postType);
        currentJob.setPerson(handlePersonAction.getCurrentPerson());
        currentJob.setFolderNo(folderNo);
        currentJob.setEmployType(employeeType);

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editJob");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentJob)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                int p = page;
                refresh();
                page = p;
                me.addInfoMessage("operation.occurred");
                me.redirect("/person/list-person.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();

        }
    }

    public void selectEmployType(ValueChangeEvent event) {
        Long selectedId = (Long) event.getNewValue();
        for (BLookup bLookup : getEmployeeTypes()) {
            if (selectedId.equals(bLookup.getId())) {
                employeeType = bLookup;
            }
        }
    }

    public BLookup getEmployeeType() {
        if (employeeType == null) {
            if (getEmployeeTypes().size() > 0) {
                employeeType = getEmployeeTypes().get(0);
            }
        }
        return employeeType;
    }

    public void setEmployeeType(BLookup employeeType) {
        this.employeeType = employeeType;
    }

    public List<BLookup> getEmployeeTypes() {
        if (employeeTypes == null || employeeTypes.size() == 0) {
            WebServiceInfo bLookupService = new WebServiceInfo();
            bLookupService.setServiceName("/getByLookupId");
            try {
                employeeTypes = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(bLookupService.getServerUrl(), bLookupService.getServiceName(), new ObjectMapper().writeValueAsString(Lookup.EMPLOYEE_TYPE_ID)), new TypeReference<List<BLookup>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (BLookup bLookup : employeeTypes) {
                bLookup.setTitleText(me.getValue(bLookup.getCode()));
            }
        }

        return employeeTypes;
    }


    public void selectAssistType(ValueChangeEvent event) {
        Long selectedId = (Long) event.getNewValue();
        for (BLookup bLookup : getAssistTypes()) {
            if (selectedId.equals(bLookup.getId())) {
                assistType = bLookup;
            }
        }
    }

    public BLookup getAssistType() {
        if (assistType == null) {
            if (getAssistTypes().size() > 0) {
                assistType = getAssistTypes().get(0);
            }
        }
        return assistType;
    }

    public void setAssistType(BLookup assistType) {
        this.assistType = assistType;
    }

    public List<BLookup> getAssistTypes() {
        if (assistTypes == null || assistTypes.size() == 0) {
            WebServiceInfo bLookupService = new WebServiceInfo();
            bLookupService.setServiceName("/getByLookupId");
            try {
                assistTypes = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(bLookupService.getServerUrl(), bLookupService.getServiceName(), new ObjectMapper().writeValueAsString(Lookup.ASSIST_TYPE_ID)), new TypeReference<List<BLookup>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (BLookup bLookup : assistTypes) {
                bLookup.setTitleText(me.getValue(bLookup.getCode()));
            }
        }

        return assistTypes;
    }

    /**
     * @return
     */


    public void selectPostType(ValueChangeEvent event) {
        Long selectedId = (Long) event.getNewValue();
        for (BLookup bLookup : getPostTypes()) {
            if (selectedId.equals(bLookup.getId())) {
                postType = bLookup;
            }
        }
    }

    public BLookup getPostType() {
        if (postType == null) {
            if (getPostTypes().size() > 0) {
                postType = getPostTypes().get(0);
            }
        }
        return postType;
    }

    public void setPostType(BLookup postType) {
        this.postType = postType;
    }

    public List<BLookup> getPostTypes() {
        if (postTypes == null || postTypes.size() == 0) {
            WebServiceInfo bLookupService = new WebServiceInfo();
            bLookupService.setServiceName("/getByLookupId");
            try {
                postTypes = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(bLookupService.getServerUrl(), bLookupService.getServiceName(), new ObjectMapper().writeValueAsString(Lookup.POST_TYPE_ID)), new TypeReference<List<BLookup>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (BLookup bLookup : postTypes) {
                bLookup.setTitleText(me.getValue(bLookup.getCode()));
            }
        }

        return postTypes;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public GeneralHelper getGeneralHelper() {
        return generalHelper;
    }

    public void setGeneralHelper(GeneralHelper generalHelper) {
        this.generalHelper = generalHelper;
    }

    public DataModel<Job> getPersonList() {
        return personList;
    }

    public void setPersonList(DataModel<Job> personList) {
        this.personList = personList;
    }

    public Job getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(Job currentJob) {
        this.currentJob = currentJob;
    }

    public Job getNewJob() {
        return newJob;
    }

    public void setNewJob(Job newJob) {
        this.newJob = newJob;
    }


    public String getEmployNo() {
        return employNo;
    }

    public void setEmployNo(String employNo) {
        this.employNo = employNo;
    }

    public String getFolderNo() {
        return folderNo;
    }

    public void setFolderNo(String folderNo) {
        this.folderNo = folderNo;
    }

    public String getInternalTel() {
        return internalTel;
    }

    public void setInternalTel(String internalTel) {
        this.internalTel = internalTel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Job getSelectedJob() {
        return selectedJob;
    }

    public void setSelectedJob(Job selectedJob) {
        this.selectedJob = selectedJob;
    }
}