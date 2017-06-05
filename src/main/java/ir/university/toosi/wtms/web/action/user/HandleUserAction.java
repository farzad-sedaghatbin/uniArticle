package ir.university.toosi.wtms.web.action.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.entity.*;
import ir.university.toosi.tms.model.service.PCServiceImpl;
import ir.university.toosi.tms.model.service.UserServiceImpl;
import ir.university.toosi.tms.model.service.WorkGroupServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.util.LangUtil;
import ir.university.toosi.wtms.web.action.AccessControlAction;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.person.HandlePersonAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import ir.university.toosi.wtms.web.action.workgroup.HandleWorkGroupAction;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.wtms.web.util.CalendarUtil;
import ir.university.toosi.wtms.web.util.ImageUtils;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
//import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;


import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
//import javax.validation.constraints.AssertTrue;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleUserAction")
@SessionScoped
public class HandleUserAction implements Serializable {
    @Inject
    private UserManagementAction me;


    @Inject
    private HandleWorkGroupAction handleWorkGroupAction;
    @Inject
    private HandleRoleAction handleRoleAction;
    @Inject
    private GeneralHelper generalHelper;
    @Inject
    private AccessControlAction accessControlAction;
    @Inject
    private HandlePersonAction handlePersonAction;

    @EJB
    private UserServiceImpl userService;
    @EJB
    private PersonServiceImpl personService;
    @EJB
    private PCServiceImpl pcService;
    @EJB
    private WorkGroupServiceImpl workGroupService;

    private LazyDataModel<User> userList;
    private List<Role> roleList = null;
    private List<Role> workGroupRoleList = null;
    private User currentUser = null;
    private User newUser = null;
    private String nationalCode;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String mobile;
    private String address;
    private String status;
    private String extraField1;
    private String extraField2;
    private String extraField3;
    private String extraField4;
    private String password = "";
    private String newPassword;
    private String rePassword;
    private String chatFirstName = "";
    private String chatLastName = "";
    private List<Person> personList;
    private PC currentPC;
    private Person currentPerson;
    private String searchOrganizationName;
    private String editable = "false";
    private boolean view = false;
    private String selectedParticipant = "0";
    private String selectedWorkGroupId = "0";
    private String workgroup;
    private boolean enabled = true;
    private boolean selectAll = false;
    private String lock = "false";
    private boolean roleEnabled;
    private int page = 1;
    private int pcPage = 1;
    private int personPage = 1;
    private SelectItem[] personItem;
    private Set<WorkGroup> selectedWorkGroup;
    private Person selectedPerson;
    private boolean selectRow = false;
    private String oldPassword;

    private String usernameFilter;
    private String workgroupFilter;
    private String personNameFilter;
    private String personLastNameFilter;

    private SortOrder usernameOrder = SortOrder.UNSORTED;
    private SortOrder workgroupOrder = SortOrder.UNSORTED;
    private SortOrder pcIpOrder = SortOrder.UNSORTED;
    private SortOrder roleDescriptionOrder = SortOrder.UNSORTED;
    private SortOrder personNameOrder = SortOrder.UNSORTED;
    private SortOrder personLastNameOrder = SortOrder.UNSORTED;
    private DualListModel<PC> pcList;
    private String oldPass;
    private String newPass;
    private String retypedNewPass;

    private boolean disableFields;
    private byte[] picture;

    public void begin() {
        me.setActiveMenu(MenuType.USER);
        refresh();
        me.redirect("/user/users.xhtml");
    }

    public void
    init() {
        picture = null;
        firstname = "";
        lastname = "";
        nationalCode = "";
        username = "";
        password = "";
        password = "";
        enabled = true;
        password = "";
        newPass = "";
        retypedNewPass = "";
        roleEnabled = true;
        selectedWorkGroup = null;
        page = 1;
        handleWorkGroupAction.refresh();
        handleWorkGroupAction.setSelectWorkGroups(new HashSet<WorkGroup>());
        currentUser = null;
        setSelectRow(false);
        usernameFilter = "";
        workgroupFilter = "";
        personNameFilter = "";
        personLastNameFilter = "";
        setDisableFields(false);

    }

    public void refresh() {
        init();
        userList = new UserLazyDataModel(userService);

        handleRoleAction.setRoleList(new ArrayList<Role>());
    }

    public void doDelete() {
        if (currentUser.getWorkGroups() != null && currentUser.getWorkGroups().size() > 0) {
            me.addInfoMessage("user.has.role");
            me.redirect("/user/users.xhtml");
        }
        if (currentUser.getUsername().equalsIgnoreCase(me.getUsername())) {
            me.addInfoMessage("delete.self");
            me.redirect("/user/users.xhtml");
        }
        String currentDate = LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd"));
        String currentTime = CalendarUtil.getTime(new Date(), new Locale("fa"));
        currentUser.setEffectorUser(me.getUsername());
        String condition = userService.deleteUser(currentUser);

        refresh();
        me.addInfoMessage(condition);
        me.redirect("/user/users.xhtml");
    }

    public void add() {
        init();
        setEditable("false");
    }

    public void assignPC() {
        setDisableFields(false);
        List<PC> sourcePCs = new ArrayList<>();
        List<PC> targetPCs = new ArrayList<>();

        List<PC> pcs = pcService.getAllPCs();
        if (currentUser.getPcs() != null && currentUser.getPcs().size() != 0) {
            for (PC pc : pcs) {

                for (PC pc1 : currentUser.getPcs()) {
                    if (pc1.getId() == pc.getId()) {
                        targetPCs.add(pc);
                    } else {
                        sourcePCs.add(pc);
                    }
                }
            }
        } else {
            sourcePCs.addAll(pcs);
        }
        pcList = new DualListModel<>(sourcePCs, targetPCs);
    }

    public void onTransfer(TransferEvent event) {
        if (event.isAdd()) {
            for (Object item : event.getItems()) {
                pcList.getSource().remove((PC) item);
                pcList.getTarget().add((PC) item);
            }
        } else {
            for (Object item : event.getItems()) {
                pcList.getSource().add((PC) item);
                pcList.getTarget().remove(item);
            }
        }
    }


    public void assignPerson() {
        handlePersonAction.setPersonnameOrder(SortOrder.UNSORTED);
        handlePersonAction.setPersonFamilyOrder(SortOrder.ASCENDING);
        handlePersonAction.setPersonnameFilter("");
        handlePersonAction.setPersonFamilyFilter("");
        personPage = 1;

        personList = personService.getAllPersonModel();
    }

    public void associatePerson() {
        currentUser.setPerson(selectedPerson);
        currentUser.setFirstname(selectedPerson.getName());
        currentUser.setLastname(selectedPerson.getLastName());

        boolean condition = userService.editUser(currentUser);
        if (condition) {
            refresh();
            me.addInfoMessage("operation.occurred");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }


    }

    public void doAdd() {
        newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setFirstname(firstname);
        newUser.setLastname(lastname);
        newUser.setDeleted("0");
        newUser.setUserSign(picture);
        newUser.setEnable(enabled == true ? "true" : "false");
        newUser.setStatus("c");
        newUser.setEffectorUser(me.getUsername());
        boolean condition = userService.exist(username);
        if (condition) {
            me.addErrorMessage("user.exists");
            return;
        }


        Set<WorkGroup> selectedWorkGroup = new HashSet<>();
        for (WorkGroup workGroup : handleWorkGroupAction.getSelectWorkGroups()) {

            if (workGroup.isSelected()) {
                selectedWorkGroup.add(workGroup);
            }
        }
        if (selectedWorkGroup.size() == 0) {
            me.addErrorMessage("no_workgroup_selected");
            return;
        }
        newUser.setWorkGroups(selectedWorkGroup);
        User user = null;
        user = userService.createUser(newUser);

        if (user != null) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/user/users.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }

    public void associatePCs() {
        currentUser.setPcs(new HashSet<>(pcList.getTarget()));
        boolean condition = userService.editUser(currentUser);
        if (condition) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/user/users.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }

    }

    public void edit() {
        setEditable("true");
        setDisableFields(false);
        currentUser = userService.findById(currentUser.getId());
        firstname = currentUser.getFirstname();
        lastname = currentUser.getLastname();
        username = currentUser.getUsername();
        enabled = currentUser.getEnable().equalsIgnoreCase("true") ? true : false;
        picture = currentUser.getUserSign();
        List<WorkGroup> sourceWorkgroups = new ArrayList<>();
        List<WorkGroup> targetWorkgroups = new ArrayList<>();

        List<WorkGroup> workGroups = null;
        workGroups = workGroupService.getAllWorkGroup();
        handleWorkGroupAction.setSelectWorkGroups(new HashSet<WorkGroup>());
        for (WorkGroup workGroup : workGroups) {
            workGroup.setDescText(me.getValue(workGroup.getDescription()));
        }
        for (WorkGroup currentWorkGroup : currentUser.getWorkGroups()) {
            for (WorkGroup workGroup : workGroups) {
                if ((currentWorkGroup.getId() == workGroup.getId())) {
                    workGroup.setSelected(true);
                    workGroup.setDescription(me.getValue(currentWorkGroup.getDescription()));
                    handleWorkGroupAction.getSelectWorkGroups().add(workGroup);
                    targetWorkgroups.add(workGroup);
                } else {
                    sourceWorkgroups.add(workGroup);
                }
            }
        }
        if (currentUser.getWorkGroups().isEmpty()){
            sourceWorkgroups = workGroups;
        }
        handleWorkGroupAction.setWorkGroupList(workGroups);
        handleWorkGroupAction.setWorkgroups(new DualListModel<WorkGroup>(sourceWorkgroups, targetWorkgroups));

    }

    public void viewMode() {
        view = true;
        setDisableFields(true);
        currentUser = userService.findById(currentUser.getId());
        firstname = currentUser.getFirstname();
        lastname = currentUser.getLastname();
        username = currentUser.getUsername();
        enabled = currentUser.getEnable().equalsIgnoreCase("true") ? true : false;

        List<WorkGroup> sourceWorkgroups = new ArrayList<>();
        List<WorkGroup> targetWorkgroups = new ArrayList<>();

        List<WorkGroup> workGroups = null;
        workGroups = workGroupService.getAllWorkGroup();
        handleWorkGroupAction.setSelectWorkGroups(new HashSet<WorkGroup>());
        for (WorkGroup workGroup : workGroups) {
            workGroup.setDescText(me.getValue(workGroup.getDescription()));
        }
        for (WorkGroup currentWorkGroup : currentUser.getWorkGroups()) {
            for (WorkGroup workGroup : workGroups) {
                if ((currentWorkGroup.getId() == workGroup.getId())) {
                    workGroup.setSelected(true);
                    workGroup.setDescription(me.getValue(currentWorkGroup.getDescription()));
                    handleWorkGroupAction.getSelectWorkGroups().add(workGroup);
                    targetWorkgroups.add(workGroup);
                } else {
                    sourceWorkgroups.add(workGroup);
                }
            }
        }

        if (currentUser.getWorkGroups().isEmpty()){
            sourceWorkgroups = workGroups;
        }
        handleWorkGroupAction.setWorkGroupList(workGroups);
        handleWorkGroupAction.setWorkgroups(new DualListModel<WorkGroup>(sourceWorkgroups, targetWorkgroups));

    }


    public void doEdit() {
        if (handleWorkGroupAction.getSelectWorkGroups() == null) {
            me.addErrorMessage("workGroup_not_selected");
            return;
        }
        currentUser.setEnable(status);
        currentUser.setFirstname(firstname);
        currentUser.setPassword(password);
        currentUser.setEnable(enabled == true ? "true" : "false");
        currentUser.setLastname(lastname);
        currentUser.setUserSign(picture);
        currentUser.setWorkGroups(handleWorkGroupAction.getSelectWorkGroups());
        currentUser.setEffectorUser(me.getUsername());
        if (firstname.trim().length() > 0 || lastname.trim().length() > 0)
            currentUser.setPerson(null);
        boolean condition = userService.editUser(currentUser);
        if (condition) {
            handleWorkGroupAction.setSelectWorkGroups(new HashSet<WorkGroup>());
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/user/users.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }
    }

    public void changePassword() {
        if (oldPass == null || oldPass.equals("")) {
            init();
            me.addErrorMessage("enter.old.pass");
            me.redirect("/dashboard.xhtml");
        }
        if (!newPass.equals(me.getUser().getPassword())) {
            init();
            me.addErrorMessage("oldPass.is.wrong");
            me.redirect("/dashboard.xhtml");
        }
        if (newPass == null || newPass.equals("") || retypedNewPass == null || retypedNewPass.equals("")) {
            init();
            me.addErrorMessage("enter.newPass.or.reNewPass");
            me.redirect("/dashboard.xhtml");
        }
        if (!newPass.equals(retypedNewPass)) {
            init();
            me.addErrorMessage("newPass.oldPass.not.match");
            me.redirect("/dashboard.xhtml");
        }

        currentUser.setPassword(newPass);
        userService.editUser(currentUser);
        me.addInfoMessage("pass.successfully.changed");
        me.redirect("/dashboard.xhtml");
    }

    public void resetPass() {
        currentUser.setPassword("password");
        me.addInfoMessage("pass.reset");
        me.redirect("/user/users.xhtml");
    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();

        }
    }

    public void userDetail() {
//        workGroupRoleList = new DataModel<>(new ArrayList<>(currentUser.getWorkGroups().getRoles()));
        enabled = currentUser.getEnable() == "true" ? true : false;
        username = currentUser.getUsername();
        List<User> users = null;//me.getGeneralHelper().getUserService().getAllPending(currentUser);


    }
//
//    public Filter<?> getUsernameFilterImpl() {
//        return new Filter<User>() {
//            public boolean accept(User user) {
//                return usernameFilter == null || usernameFilter.length() == 0 || user.getUsername().toLowerCase().contains(usernameFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getPersonNameFilterImpl() {
//        return new Filter<User>() {
//            public boolean accept(User user) {
//                return StringUtils.isEmpty(personNameFilter) || user.getPerson().getName().contains(personNameFilter);
//            }
//        };
//    }
//
//    public Filter<?> getPersonLastNameFilterImpl() {
//        return new Filter<User>() {
//            public boolean accept(User user) {
//                return StringUtils.isEmpty(personLastNameFilter) || user.getPerson().getLastName().contains(personLastNameFilter);
//            }
//        };
//    }


    //    @AssertTrue
    public boolean isPasswordsEquals() {
        if (!rePassword.equals(newPassword)) {
            me.addInfoMessage(" Different passwords entered!");
            return false;
        }
        return true;
    }

    public void storeNewPassword() {
        me.addInfoMessage("success");
        currentUser = me.getUser();
        currentUser.setPassword(newPassword);
        boolean condition = userService.editUser(currentUser);
        if (condition) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/user/users.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }
    }

    public void selectForEdit() {
//        currentUser = userList.getRowData();
        setSelectRow(true);

    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }


    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public SortOrder getUsernameOrder() {
        return usernameOrder;
    }

    public void setUsernameOrder(SortOrder usernameOrder) {
        this.usernameOrder = usernameOrder;
    }

    public String getUsernameFilter() {
        return usernameFilter;
    }

    public void setUsernameFilter(String usernameFilter) {
        this.usernameFilter = usernameFilter;
    }

    public SortOrder getWorkgroupOrder() {
        return workgroupOrder;
    }

    public void setWorkgroupOrder(SortOrder workgroupOrder) {
        this.workgroupOrder = workgroupOrder;
    }

    public String getWorkgroupFilter() {
        return workgroupFilter;
    }

    public void setWorkgroupFilter(String workgroupFilter) {
        this.workgroupFilter = workgroupFilter;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public String getSearchOrganizationName() {
        return searchOrganizationName;
    }

    public void setSearchOrganizationName(String searchOrganizationName) {
        this.searchOrganizationName = searchOrganizationName;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSelectedParticipant() {
        return selectedParticipant;
    }

    public void setSelectedParticipant(String selectedParticipant) {
        this.selectedParticipant = selectedParticipant;
    }

    public String getSelectedWorkGroupId() {
        return selectedWorkGroupId;
    }

    public void setSelectedWorkGroupId(String selectedWorkGroupId) {
        this.selectedWorkGroupId = selectedWorkGroupId;
    }

    public String getChatFirstName() {
        return chatFirstName;
    }

    public void setChatFirstName(String chatFirstName) {
        this.chatFirstName = chatFirstName;
    }

    public String getChatLastName() {
        return chatLastName;
    }

    public void setChatLastName(String chatLastName) {
        this.chatLastName = chatLastName;
    }


    public String getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(String workgroup) {
        this.workgroup = workgroup;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void userEnable(ValueChangeEvent event) {
        boolean status = (Boolean) event.getNewValue();
        if (!status) {
            currentUser.setEnable("false");
        } else
            currentUser.setEnable("true");

    }

    public void userEnableChange(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            status = "true";
        } else
            status = "false";

    }
    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void listener(FileUploadEvent event) throws Exception {
        BufferedImage sourceBufferedImage = ImageUtils.convertByteArrayToBufferedImage(event.getFile().getContents());
        float scaleRatio = ImageUtils.calculateScaleRatio(sourceBufferedImage.getWidth(), 200);//todo from properties
        if (scaleRatio > 0 && scaleRatio < 1) {
            try {
                sourceBufferedImage = ImageUtils.scaleImage(sourceBufferedImage, scaleRatio);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setPicture(ImageUtils.imageToByteArray(sourceBufferedImage));
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public boolean isRoleEnabled() {
        return roleEnabled;
    }

    public void setRoleEnabled(boolean roleEnabled) {
        this.roleEnabled = roleEnabled;
    }

    public int getPage() {
        currentUser = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public SelectItem[] getPersonItem() {
        return personItem;
    }

    public void setPersonItem(SelectItem[] personItem) {
        this.personItem = personItem;
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExtraField1() {
        return extraField1;
    }

    public void setExtraField1(String extraField1) {
        this.extraField1 = extraField1;
    }

    public String getExtraField2() {
        return extraField2;
    }

    public void setExtraField2(String extraField2) {
        this.extraField2 = extraField2;
    }

    public String getExtraField3() {
        return extraField3;
    }

    public void setExtraField3(String extraField3) {
        this.extraField3 = extraField3;
    }

    public String getExtraField4() {
        return extraField4;
    }

    public void setExtraField4(String extraField4) {
        this.extraField4 = extraField4;
    }


    public PC getCurrentPC() {
        return currentPC;
    }

    public void setCurrentPC(PC currentPC) {
        this.currentPC = currentPC;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public Set<WorkGroup> getSelectedWorkGroup() {
        return selectedWorkGroup;
    }

    public void setSelectedWorkGroup(Set<WorkGroup> selectedWorkGroup) {
        this.selectedWorkGroup = selectedWorkGroup;
    }

    public int getPcPage() {
        return pcPage;
    }

    public void setPcPage(int pcPage) {
        this.pcPage = pcPage;
    }

    public int getPersonPage() {
        return personPage;
    }

    public void setPersonPage(int personPage) {
        this.personPage = personPage;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public HandleWorkGroupAction getHandleWorkGroupAction() {
        return handleWorkGroupAction;
    }

    public void setHandleWorkGroupAction(HandleWorkGroupAction handleWorkGroupAction) {
        this.handleWorkGroupAction = handleWorkGroupAction;
    }

    public HandleRoleAction getHandleRoleAction() {
        return handleRoleAction;
    }

    public void setHandleRoleAction(HandleRoleAction handleRoleAction) {
        this.handleRoleAction = handleRoleAction;
    }

    public GeneralHelper getGeneralHelper() {
        return generalHelper;
    }

    public void setGeneralHelper(GeneralHelper generalHelper) {
        this.generalHelper = generalHelper;
    }

    public AccessControlAction getAccessControlAction() {
        return accessControlAction;
    }

    public void setAccessControlAction(AccessControlAction accessControlAction) {
        this.accessControlAction = accessControlAction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SortOrder getPcIpOrder() {
        return pcIpOrder;
    }

    public void setPcIpOrder(SortOrder pcIpOrder) {
        this.pcIpOrder = pcIpOrder;
    }

    public SortOrder getRoleDescriptionOrder() {
        return roleDescriptionOrder;
    }

    public void setRoleDescriptionOrder(SortOrder roleDescriptionOrder) {
        this.roleDescriptionOrder = roleDescriptionOrder;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public SortOrder getPersonNameOrder() {
        return personNameOrder;
    }

    public void setPersonNameOrder(SortOrder personNameOrder) {
        this.personNameOrder = personNameOrder;
    }

    public SortOrder getPersonLastNameOrder() {
        return personLastNameOrder;
    }

    public void setPersonLastNameOrder(SortOrder personLastNameOrder) {
        this.personLastNameOrder = personLastNameOrder;
    }

    public String getPersonNameFilter() {
        return personNameFilter;
    }

    public void setPersonNameFilter(String personNameFilter) {
        this.personNameFilter = personNameFilter;
    }

    public String getPersonLastNameFilter() {
        return personLastNameFilter;
    }

    public void setPersonLastNameFilter(String personLastNameFilter) {
        this.personLastNameFilter = personLastNameFilter;
    }

    public boolean isDisableFields() {
        return disableFields;
    }

    public void setDisableFields(boolean disableFields) {
        this.disableFields = disableFields;
    }

    public DualListModel<PC> getPcList() {
        if (pcList == null)
            pcList = new DualListModel<>();
        return pcList;
    }

    public void setPcList(DualListModel<PC> pcList) {
        this.pcList = pcList;
    }

    public LazyDataModel<User> getUserList() {
        return userList;
    }

    public void setUserList(LazyDataModel<User> userList) {
        this.userList = userList;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getRetypedNewPass() {
        return retypedNewPass;
    }

    public void setRetypedNewPass(String retypedNewPass) {
        this.retypedNewPass = retypedNewPass;
    }
}