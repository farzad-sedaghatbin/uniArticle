package ir.university.toosi.wtms.web.action.zone;

import ir.university.toosi.tms.model.entity.*;
import ir.university.toosi.tms.model.entity.calendar.Calendar;
import ir.university.toosi.tms.model.entity.calendar.DayType;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.rule.Rule;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.tms.model.entity.zone.Camera;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.PreRequestGateway;
import ir.university.toosi.tms.model.service.GatewayPersonServiceImpl;
import ir.university.toosi.tms.model.service.GatewaySpecialStateScheduler;
import ir.university.toosi.tms.model.service.GatewaySpecialStateServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.rule.RulePackageServiceImpl;
import ir.university.toosi.tms.model.service.rule.RuleServiceImpl;
import ir.university.toosi.tms.model.service.zone.CameraServiceImpl;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
import ir.university.toosi.tms.model.service.zone.PreRequestGatewayServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.person.HandlePersonAction;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.SortOrder;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;


@Named(value = "handleGatewayAction")
@SessionScoped
public class HandleGatewayAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleCameraAction handleCameraAction;
    @Inject
    private HandlePersonAction handlePersonAction;

    @EJB
    private GatewayServiceImpl gatewayService;
    @EJB
    private GatewaySpecialStateServiceImpl specialStateService;
    @EJB
    private GatewayPersonServiceImpl gatewayPersonService;
    @EJB
    private PreRequestGatewayServiceImpl preRequestGatewayService;
    @EJB
    private PersonServiceImpl personService;
    @EJB
    private CameraServiceImpl cameraService;
    @EJB
    private RulePackageServiceImpl rulePackageService;
    @EJB
    private GatewaySpecialStateScheduler gatewaySpecialStateScheduler;
    @EJB
    private RuleServiceImpl ruleService;

    private List<Gateway> gateways;
    private List<Gateway> preRequestGateways;
    private List<Long> preRequestGatewayIds;
    private List<Camera> selectedCamera = new ArrayList<>();
    private List<Person> persons = new ArrayList<>();
    private List<Person> selectedPersons = new ArrayList<>();
    private List<Person> unSelectedPersons = new ArrayList<>();
    private List<Person> newSelectedPersons = new ArrayList<>();

    private List<Gateway> gatewayList = null;
    private List<Gateway> preRequier = null;
    private List<Camera> cameras = null;
    private String editable = "false";
    private String gatewayName;
    private boolean passBackControl;
    private boolean gatewayEnabled;
    private String description;
    private Gateway currentGetway = null;
    private Gateway currentPreGateway = null;
    private Gateway selectGetway = null;
    private String currentPage;
    private int page = 1;
    private int personPage = 1;
    private int pageInPopup = 1;
    private boolean selected;
    private boolean selectAll;
    private boolean selectAllPerson;
    private boolean selectAllPre;
    private boolean selectAllCamera;
    private Set<Gateway> selectedGateways = new HashSet<>();
    private SortOrder operationDescriptionOrder = SortOrder.UNSORTED;
    private String operationDescriptionFilter;
    private RulePackage selectedRulePackage;
    private String rulePackageName;
    private String calendarName;
    private boolean antiPassBack, allowExit, allowExitGadget;
    private List<RulePackage> rulePackageList = null;
    private List<GatewaySpecialState> gatewaySpecialStates = new ArrayList<>();
    private List<GatewaySpecialState> gatewaySpecialStateList = null;
    private GatewaySpecialState currentGatewaySpecialStatus = null;
    private String statusHour;
    private String statusMinute;
    private String statusSecond;
    private String statusDate;
    private String until;
    private String statusStateName;
    private List<GatewayStatusObject> gatewayStatusItem;
    private Hashtable<String, GatewayStatus> statusHashtable = new Hashtable<>();
    private List<Person> notAssignPersonList = new ArrayList<>();
    private String gatewayNameFilter;
    private String gatewayDescriptionFilter;
    private SortOrder gatewayNameOrder = SortOrder.UNSORTED;
    private SortOrder gatewayDescriptionOrder = SortOrder.UNSORTED;
    private DualListModel<Gateway> gatewayDualList;
    private Gateway thisGateway;
    private DualListModel<Gateway> gatewayDuals;
    private boolean disableFields;
    private DualListModel<Gateway> gatewayDualsForZone;


    private String name;
    private ArrayList<Rule> ruleArrayList = new ArrayList<>();

    private boolean ruleAniPassBack = false;
    private boolean ruleAllowExit = false;
    private boolean ruleAllowExitGadget = false;
    private boolean selectRow = false;
    private List<Rule> ruleListTemp = null;
    private ir.university.toosi.tms.model.entity.calendar.Calendar selectedCalendar;
    private DayType ruleDayType;
    private String selectedCalendarIdTemp;
    private String dayTypeIdTemp;
    private String ruleStartTime;
    private String ruleEndTime;
    private String startHour;
    private String startMinute;
    private String startSecond;
    private String endHour;
    private String endMinute;
    private String endSecond;
    private String ruleEntranceCount;
    private String ruleExitCount;
    private Boolean ruleDeny;
    private String editableRule = "false";
    private boolean addNewRuleFlag = false;
    private Rule currentRule;
    private SelectItem[] dayTypeItems;
    private SelectItem[] calendarItems = me.calendarItem;
    private Hashtable<String, DayType> dayTypeHashtable = new Hashtable<>();


    public void begin() {
        refresh();
        me.redirect("/gateway/gateways.xhtml");
    }

    public void selectGateWays(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentGetway.setSelected(true);
            selectedGateways.add(currentGetway);
        } else {
            currentGetway.setSelected(false);
            selectedGateways.remove(currentGetway);
        }
    }

    public void changedList(ValueChangeEvent event) {
        selectedPersons = (List<Person>) event.getNewValue();

    }

    public void changeGateways(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            gatewayEnabled = true;
        } else
            gatewayEnabled = false;
    }


    public void editRule() {
        currentGetway = gatewayService.findById(currentGetway.getId());
        if (currentGetway.getRulePackage() == null) {
            refresh();
            me.addErrorMessage("has.not.rulePackage");
            me.redirect("/gateway/gateways.xhtml");
            return;
        }

        editOrganRule(currentGetway.getRulePackage());
    }

    public void editOrganRule(RulePackage rulePackage) {
        ruleArrayList = new ArrayList<>();
        ruleListTemp = ruleService.getByRulePackageId(rulePackage.getId());
        ruleListTemp = ruleArrayList;
        name = rulePackage.getName();
        ruleAllowExitGadget = rulePackage.isAllowExitGadget();
        ruleAniPassBack = rulePackage.isAniPassBack();
        ruleAllowExit = rulePackage.isAllowExit();
        selectedCalendar = rulePackage.getCalendar();
        selectedCalendarIdTemp = String.valueOf(selectedCalendar.getId());
        editable = "true";
    }

    public void remove() {
//        currentRule = ruleListTemp.getRowData();
        ruleArrayList.remove(currentRule);
        ruleListTemp = ruleArrayList;
    }

    public void addNewRule() {
        ruleDayType = null;
        ruleStartTime = "";
        ruleEndTime = "";
        ruleEntranceCount = "";
        ruleExitCount = "";
        ruleDeny = false;
        addNewRuleFlag = true;
    }

    public void doEditGatewayRule() {
        RulePackage newRulePackage = new RulePackage();
        newRulePackage.setStatus("c");
        newRulePackage.setDeleted("0");
        newRulePackage.setEffectorUser(me.getUsername());
        newRulePackage.setName(name);
        newRulePackage.setAllowExit(ruleAllowExit);
        newRulePackage.setAniPassBack(ruleAniPassBack);
        newRulePackage.setAllowExitGadget(ruleAllowExitGadget);
        newRulePackage.setCalendar(me.calendarHashtable.get(selectedCalendarIdTemp));

        RulePackage addedRulePackage = null;
        addedRulePackage = rulePackageService.createRulePackage(newRulePackage);
        if (addedRulePackage != null) {
            for (Rule rule : ruleArrayList) {
                rule.setRulePackage(addedRulePackage);
                ruleService.createRule(rule);
            }

            currentGetway.setRulePackage(addedRulePackage);
            boolean condition = gatewayService.editGateway(currentGetway);
            if (condition) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/gateway/gateways.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }
    }

    public void doAddNewRule() {
        ruleStartTime = startHour + ":" + startMinute + ":" + startSecond;
        ruleEndTime = endHour + ":" + endMinute + ":" + endSecond;
        Rule rule = new Rule();
        rule.setDayType(dayTypeHashtable.get(dayTypeIdTemp));
        rule.setStartTime(ruleStartTime);
        rule.setEndTime(ruleEndTime);
        rule.setEntranceCount(ruleEntranceCount);
        rule.setExitCount(ruleExitCount);
        rule.setDeny(ruleDeny);
        if (feasible(rule)) {
            ruleArrayList.add(rule);
            ruleListTemp = ruleArrayList;
            addNewRuleFlag = false;
        } else me.addInfoMessage("conflict");
    }

    public boolean feasible(Rule rule) {

        if (rule.isDeny())
            return true;
        long startTime = time2long(rule.getStartTime());
        long endTime = time2long(rule.getEndTime());
        boolean flag = true;

        if (endTime < startTime)
            return false;

        for (Rule rule1 : ruleArrayList) {
            if (rule1.getDayType().getId() != rule.getDayType().getId())
                continue;
            if (startTime >= time2long(rule1.getStartTime()) && endTime <= time2long(rule1.getStartTime()))
                flag = true;
            else if (startTime > time2long(rule1.getEndTime()) && endTime > time2long(rule1.getEndTime()))
                flag = true;
            else {
                flag = false;
                break;
            }
        }

        return flag;
    }

    public List<Gateway> getSelectionGrid() {
        gateways = new ArrayList<>();
        refresh();
        return gatewayList;
    }

    public void refresh() {

        gateways = gatewayService.getAllGateway();
        gatewayList = gateways;
        persons = personService.getAllPersonModel();
    }

    public void add() {
        init();
        preRequier = gateways;
        gatewayDuals = new DualListModel<>(gatewayService.getAllGateway(), new ArrayList<Gateway>());
        setEditable("false");
        setDisableFields(false);
    }

    public void doDelete() {
        if (currentGetway.getZone() != null) {
            refresh();
            me.addInfoMessage("gateway.zone");
            me.redirect("/gateway/gateways.xhtml");
        }
        String condition = gatewayService.deleteGateway(currentGetway);
        refresh();
        me.addInfoMessage(condition);
        me.redirect("/gateway/gateways.xhtml");
    }

    public void init() {
        gatewayName = "";
        description = "";
        page = 1;
        selectAll = false;
        selectAllPre = false;
        selectAllPerson = false;
        gatewayEnabled = false;
        currentGetway = null;
        setSelectRow(false);
        statusHour = "0";
        statusMinute = "0";
        statusSecond = "0";
        statusDate = "0";
        until = "0";
        preRequestGateways = new ArrayList<>();
        preRequestGatewayIds = new ArrayList<>();
        gatewayNameFilter = "";
        gatewayDescriptionFilter = "";

    }

    public void view() {
        edit();
        setEditable("true");
        setDisableFields(true);

    }

    public void edit() {
        setEditable("true");
        setDisableFields(false);
        gatewayEnabled = currentGetway.isEnabled();
        description = currentGetway.getDescription();
        gatewayName = currentGetway.getName();
        preRequestGatewayIds = currentGetway.getPreRequestGateways();

        preRequestGateways = new ArrayList<>();
        for (Long preRequestGatewayId : preRequestGatewayIds) {
            PreRequestGateway preGateway = new PreRequestGateway();
            preGateway = preRequestGatewayService.findById(preRequestGatewayId);
            preRequestGateways.add(preGateway.getPreGateway());
        }

        List<Gateway> gateways;
        if (currentGetway.getPreRequestGateways() == null || currentGetway.getPreRequestGateways().isEmpty()) {
            gateways = gatewayService.getAllGateway();
        } else {
            gateways = gatewayService.getAllGatewayExceptThese(preRequestGateways);
        }

        currentGetway = gatewayService.findById(currentGetway.getId());
        gatewayDuals = new DualListModel<>(gateways, preRequestGateways);

    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();
        }
    }

    private void doEdit() {
        currentGetway.setName(gatewayName);
        currentGetway.setDescription(description);
        currentGetway.setEnabled(gatewayEnabled);
        currentGetway.setEffectorUser(me.getUsername());
        currentGetway.setPreRequestGateways(preRequestGatewayIds);
        boolean condition = gatewayService.editGateway(currentGetway);
        if (condition) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/gateway/gateways.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }

    }

    private void doAdd() {
        Gateway newGateway = new Gateway();
        newGateway.setDescription(description);
        newGateway.setName(gatewayName);
        newGateway.setEnabled(gatewayEnabled);
        newGateway.setDeleted("0");
        newGateway.setStatus("c");
        newGateway.setEffectorUser(me.getUsername());
        newGateway.setPreRequestGateways(preRequestGatewayIds);
        Gateway insertedGateway = null;
        insertedGateway = gatewayService.createGateway(newGateway);

        if (insertedGateway != null) {
//            LanguageManagement languageManagement = new LanguageManagement();
//
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/gateway/gateways.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }


//        public void sortByOperationDescription() {
//            if (operationDescriptionOrder.equals(SortOrder.ASCENDING)) {
//                setOperationDescriptionOrder(SortOrder.DESCENDING);
//            } else {
//                setOperationDescriptionOrder(SortOrder.ASCENDING);
//            }
//        }
//
//        public Filter<?> getOperationDescriptionFilterImpl() {
//            return new Filter<Operation>() {
//                public boolean accept(Operation operation) {
//                    return operationDescriptionFilter == null || operationDescriptionFilter.length() == 0 || operation.getDescription().toLowerCase().contains(operationDescriptionFilter.toLowerCase());
//                }
//            };
//        }


    public void assignRule() {
        currentGetway = gatewayService.findById(currentGetway.getId());  //To change body of catch statement use File | Settings | File Templates.
        selectedRulePackage = currentGetway.getRulePackage();
        if (selectedRulePackage != null) {
            rulePackageName = selectedRulePackage.getName();
            if (selectedRulePackage.getCalendar() != null)
                calendarName = selectedRulePackage.getCalendar().getName();
            else
                calendarName = "";
            antiPassBack = selectedRulePackage.isAniPassBack();
            allowExit = selectedRulePackage.isAllowExit();
            allowExitGadget = selectedRulePackage.isAllowExitGadget();
        } else {
            rulePackageName = "";
            calendarName = "";
            antiPassBack = false;
            allowExit = false;
            allowExitGadget = false;
        }

        rulePackageList = rulePackageService.getAllRulePackage();
    }

    public void selectNewRuleForZone() {
        rulePackageName = selectedRulePackage.getName();
        if (selectedRulePackage.getCalendar() != null)
            calendarName = selectedRulePackage.getCalendar().getName();
        else
            calendarName = "";
        antiPassBack = selectedRulePackage.isAniPassBack();
        allowExit = selectedRulePackage.isAllowExit();
        allowExitGadget = selectedRulePackage.isAllowExitGadget();
        doAssignRule();
    }

    public void doAssignRule() {
        currentGetway.setEffectorUser(me.getUsername());
        currentGetway.setDeleted("0");
        currentGetway.setRulePackage(selectedRulePackage);
        boolean condition = gatewayService.editGateway(currentGetway);
        if (condition) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/gateway/gateways.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }

    }

    public void selectNewRuleForGateway() {
//        selectedRulePackage = rulePackageList.getRowData();
        rulePackageName = selectedRulePackage.getName();
        if (selectedRulePackage.getCalendar() != null)
            calendarName = selectedRulePackage.getCalendar().getName();
        else
            calendarName = "";
        antiPassBack = selectedRulePackage.isAniPassBack();
        allowExit = selectedRulePackage.isAllowExit();
        allowExitGadget = selectedRulePackage.isAllowExitGadget();
    }


    public void assignPerson() {
        handlePersonAction.init();
        personPage = 1;
        List<GatewayPerson> gatewayPersons = null;
        gatewayPersons = gatewayPersonService.findByGatewayId(currentGetway.getId());
        selectedPersons.clear();
        notAssignPersonList = persons;
        for (GatewayPerson gatewayPerson : gatewayPersons) {
            for (Person person : notAssignPersonList) {
                if (gatewayPerson.getPerson().getId() == person.getId()) {
                    selectedPersons.add(person);
                    person.setSelected(true);
                    break;
                }
            }

        }
        selectGetway = currentGetway;
    }

    public void onTransfer(TransferEvent event) {
        if (event.isAdd()) {
            for (Object item : event.getItems()) {
                ((Gateway) item).setSelected(true);
                selectedGateways.add((Gateway) item);
            }
        } else {
            for (Object item : event.getItems()) {
                ((Gateway) item).setSelected(false);
                selectedGateways.remove(item);
            }
        }
    }

    public void onTransferForPreRequire(TransferEvent event) {
        if (event.isAdd()) {
            for (Object item : event.getItems()) {
                ((Gateway) item).setSelected(true);
                preRequestGatewayIds.add(((Gateway) item).getId());
                gatewayDuals.getSource().remove(item);
                gatewayDuals.getTarget().add((Gateway) item);
            }
        } else {
            for (Object item : event.getItems()) {
                ((Gateway) item).setSelected(false);
                preRequestGatewayIds.remove(((Gateway) item).getId());
                gatewayDuals.getSource().add((Gateway) item);
                gatewayDuals.getTarget().remove(item);
            }
        }
    }

    public void doAssignPerson() throws IOException {

        GatewayPerson gatewayPerson = null;
        for (Person selectedPerson : newSelectedPersons) {
            gatewayPerson = new GatewayPerson();
            gatewayPerson.setPerson(selectedPerson);
            gatewayPerson.setGateway(selectGetway);
            gatewayPersonService.createGatewayPerson(gatewayPerson);
        }
        rulePackageService.fillRulePackageHashTable();
        refresh();
        me.addInfoMessage("operation.occurred");
        me.redirect("/gateway/gateways.xhtml");
    }


    public void assignSpecialStatus() {
        statusSecond = "0";
        statusMinute = "0";
        statusHour = "0";
        statusDate = "";
        until = "0";
        statusStateName = null;
        gatewayStatusItem = new ArrayList<>();
        int i = 0;
        for (GatewayStatus gatewayStatus : GatewayStatus.values()) {
            gatewayStatusItem.add(new GatewayStatusObject(me.getBundleMessage(gatewayStatus.getDescription()),gatewayStatus.getValue()));
        }
        gatewaySpecialStateList = specialStateService.findByGatewayId(currentGetway.getId());
        thisGateway = currentGetway;
    }

    public void doAssignSpecialStatus() throws IOException {
        String time = statusHour + ":" + statusMinute + ":" + statusSecond;
        GatewayStatus gatewayStatus = GatewayStatus.findByValue(statusStateName);
        GatewaySpecialState newGatewaySpecialState = new GatewaySpecialState();
        newGatewaySpecialState.setDate(statusDate);
        newGatewaySpecialState.setTime(time);
        newGatewaySpecialState.setGateway(thisGateway);
        newGatewaySpecialState.setGateWayStatus(gatewayStatus);
        newGatewaySpecialState.setDeleted("0");
        newGatewaySpecialState.setStatus("c");
        newGatewaySpecialState.setUntil(until);
        newGatewaySpecialState.setEffectorUser(me.getUsername());
        if (!feasible(newGatewaySpecialState)) {
            me.addInfoMessage("conflict");
            return;
        }
        GatewaySpecialState insertedGatewaySpecialState = null;
        insertedGatewaySpecialState = specialStateService.createGatewaySpecialState(newGatewaySpecialState);
        if (insertedGatewaySpecialState != null) {
            statusSecond = "0";
            statusMinute = "0";
            statusHour = "0";
            statusDate = "";
            gatewaySpecialStates.add(insertedGatewaySpecialState);
            gatewaySpecialStateList = gatewaySpecialStates;
        }
        me.addInfoMessage("operation.occurred");
    }

    public String readStatusFromBundle(String key){
        return me.getBundleMessage(key);
    }

    public boolean feasible(GatewaySpecialState gatewaySpecialState) {

        long startTime = time2long(gatewaySpecialState.getTime());
        long endTime = until2long(gatewaySpecialState.getTime(), gatewaySpecialState.getUntil());
        boolean flag = true;

        if (endTime < startTime)
            return false;
        if (endTime / 10000 > 23)
            return false;

        for (GatewaySpecialState gatewaySpecialState1 : gatewaySpecialStates) {
            if (!gatewaySpecialState1.getDate().equalsIgnoreCase(gatewaySpecialState.getDate()))
                continue;

            if (startTime < time2long(gatewaySpecialState1.getTime()) && endTime < until2long(gatewaySpecialState1.getTime(), gatewaySpecialState1.getUntil()))
                flag = true;
            else if (startTime > time2long(gatewaySpecialState1.getTime()) && endTime > until2long(gatewaySpecialState1.getTime(), gatewaySpecialState1.getUntil()))
                flag = true;
            else {
                return false;
            }
        }

        return flag;
    }

    public long time2long(String time) {
        String[] d = time.split(":");
        String s = (d[0].length() == 2 ? d[0] : '0' + d[0]) + (d[1].length() == 2 ? d[1] : '0' + d[1]) + (d[2].length() == 2 ? d[2] : '0' + d[2]);
        return Long.valueOf(s);
    }

    public long until2long(String time, String until) {
        String[] d = time.split(":");
        String s = (d[0].length() == 2 ? d[0] : '0' + d[0]);
        s = String.valueOf((Long.valueOf(s) + Long.valueOf(until)));
        s += (d[1].length() == 2 ? d[1] : '0' + d[1]) + (d[2].length() == 2 ? d[2] : '0' + d[2]);
        return Long.valueOf(s);
    }


    public void removeSpecialStatus(String id) {
        currentGatewaySpecialStatus = specialStateService.findById(Long.parseLong(id));
//        currentGatewaySpecialStatus = gatewaySpecialStateList.getRowData();
        String condition = specialStateService.deleteGatewaySpecialState(currentGatewaySpecialStatus);
        gatewaySpecialStateList = specialStateService.findByGatewayId(currentGetway.getId());
    }

    public void specialStatusInitialize() {
        gatewaySpecialStateScheduler.stopService();
        gatewaySpecialStateScheduler.startService();
    }

//    public Filter<?> getGatewayNameFilterImpl() {
//        return new Filter<Gateway>() {
//            public boolean accept(Gateway gateway) {
//                return gatewayNameFilter == null || gatewayNameFilter.length() == 0 || gateway.getName().toLowerCase().contains(gatewayNameFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getGatewayDescriptionFilterImpl() {
//        return new Filter<Gateway>() {
//            public boolean accept(Gateway gateway) {
//                return gatewayDescriptionFilter == null || gatewayDescriptionFilter.length() == 0 || gateway.getDescription().toLowerCase().contains(gatewayDescriptionFilter.toLowerCase());
//            }
//        };
//    }

    public void sortByGatewayName() {
        gatewayDescriptionOrder = SortOrder.UNSORTED;

        if (gatewayNameOrder.equals(SortOrder.ASCENDING)) {
            setGatewayNameOrder(SortOrder.DESCENDING);
        } else {
            setGatewayNameOrder(SortOrder.ASCENDING);
        }
    }

    public void sortByGatewayDescription() {
        gatewayNameOrder = SortOrder.UNSORTED;

        if (gatewayDescriptionOrder.equals(SortOrder.ASCENDING)) {
            setGatewayDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setGatewayDescriptionOrder(SortOrder.ASCENDING);
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

    public HandleCameraAction getHandleCameraAction() {
        return handleCameraAction;
    }

    public void setHandleCameraAction(HandleCameraAction handleCameraAction) {
        this.handleCameraAction = handleCameraAction;
    }

    public List<Gateway> getGatewayList() {
        return gatewayList;
    }

    public void setGatewayList(List<Gateway> gatewayList) {
        this.gatewayList = gatewayList;
    }

    public String getEditable() {
        return editable;
    }

    public List<Gateway> getGateways() {
        return gateways;
    }

    public void setGateways(List<Gateway> gateways) {
        this.gateways = gateways;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPassBackControl() {
        return passBackControl;
    }

    public void setPassBackControl(boolean passBackControl) {
        this.passBackControl = passBackControl;
    }

    public boolean isGatewayEnabled() {
        return gatewayEnabled;
    }

    public void setGatewayEnabled(boolean gateWayEnabled) {
        this.gatewayEnabled = gateWayEnabled;
    }


    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public SortOrder getOperationDescriptionOrder() {
        return operationDescriptionOrder;
    }

    public void setOperationDescriptionOrder(SortOrder operationDescriptionOrder) {
        this.operationDescriptionOrder = operationDescriptionOrder;
    }

    public String getOperationDescriptionFilter() {
        return operationDescriptionFilter;
    }

    public void setOperationDescriptionFilter(String operationDescriptionFilter) {
        this.operationDescriptionFilter = operationDescriptionFilter;
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

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public Gateway getCurrentGetway() {
        return currentGetway;
    }

    public void setCurrentGetway(Gateway currentGetway) {
        this.currentGetway = currentGetway;
    }

    public Set<Gateway> getSelectedGateways() {
        return selectedGateways;
    }

    public void setSelectedGateways(Set<Gateway> selectedGateways) {
        this.selectedGateways = selectedGateways;
    }

    public RulePackage getSelectedRulePackage() {
        return selectedRulePackage;
    }

    public void setSelectedRulePackage(RulePackage selectedRulePackage) {
        this.selectedRulePackage = selectedRulePackage;
    }

    public String getRulePackageName() {
        return rulePackageName;
    }

    public void setRulePackageName(String rulePackageName) {
        this.rulePackageName = rulePackageName;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public boolean isAntiPassBack() {
        return antiPassBack;
    }

    public void setAntiPassBack(boolean antiPassBack) {
        this.antiPassBack = antiPassBack;
    }

    public boolean isAllowExit() {
        return allowExit;
    }

    public void setAllowExit(boolean allowExit) {
        this.allowExit = allowExit;
    }

    public boolean isAllowExitGadget() {
        return allowExitGadget;
    }

    public void setAllowExitGadget(boolean allowExitGadget) {
        this.allowExitGadget = allowExitGadget;
    }


    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Person> getSelectedPersons() {
        return selectedPersons;
    }

    public void setSelectedPersons(List<Person> selectedPersons) {
        this.selectedPersons = selectedPersons;
    }


    public List<GatewaySpecialState> getGatewaySpecialStates() {
        return gatewaySpecialStates;
    }

    public void setGatewaySpecialStates(List<GatewaySpecialState> gatewaySpecialStates) {
        this.gatewaySpecialStates = gatewaySpecialStates;
    }


    public GatewaySpecialState getCurrentGatewaySpecialStatus() {
        return currentGatewaySpecialStatus;
    }

    public void setCurrentGatewaySpecialStatus(GatewaySpecialState currentGatewaySpecialStatus) {
        this.currentGatewaySpecialStatus = currentGatewaySpecialStatus;
    }

    public String getStatusHour() {
        return statusHour;
    }

    public void setStatusHour(String statusHour) {
        this.statusHour = statusHour;
    }

    public String getStatusMinute() {
        return statusMinute;
    }

    public void setStatusMinute(String statusMinute) {
        this.statusMinute = statusMinute;
    }

    public String getStatusSecond() {
        return statusSecond;
    }

    public void setStatusSecond(String statusSecond) {
        this.statusSecond = statusSecond;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getStatusStateName() {
        return statusStateName;
    }

    public void setStatusStateName(String statusStateName) {
        this.statusStateName = statusStateName;
    }

    public List<GatewayStatusObject> getGatewayStatusItem() {
        return gatewayStatusItem;
    }

    public void setGatewayStatusItem(List<GatewayStatusObject> gatewayStatusItem) {
        this.gatewayStatusItem = gatewayStatusItem;
    }

    public Hashtable<String, GatewayStatus> getStatusHashtable() {
        return statusHashtable;
    }

    public void setStatusHashtable(Hashtable<String, GatewayStatus> statusHashtable) {
        this.statusHashtable = statusHashtable;
    }

    public Gateway getThisGateway() {
        return thisGateway;
    }

    public void setThisGateway(Gateway thisGateway) {
        this.thisGateway = thisGateway;
    }

    public int getPageInPopup() {
        return pageInPopup;
    }

    public void setPageInPopup(int pageInPopup) {
        this.pageInPopup = pageInPopup;
    }

    public Gateway getSelectGetway() {
        return selectGetway;
    }

    public void setSelectGetway(Gateway selectGetway) {
        this.selectGetway = selectGetway;
    }

    public Gateway getCurrentPreGateway() {
        return currentPreGateway;
    }

    public void setCurrentPreGateway(Gateway currentPreGateway) {
        this.currentPreGateway = currentPreGateway;
    }

    public boolean isSelectAllPerson() {
        return selectAllPerson;
    }

    public void setSelectAllPerson(boolean selectAllPerson) {
        this.selectAllPerson = selectAllPerson;
    }

    public SortOrder getGatewayNameOrder() {
        return gatewayNameOrder;
    }

    public void setGatewayNameOrder(SortOrder gatewayNameOrder) {
        this.gatewayNameOrder = gatewayNameOrder;
    }

    public String getGatewayNameFilter() {
        return gatewayNameFilter;
    }

    public void setGatewayNameFilter(String gatewayNameFilter) {
        this.gatewayNameFilter = gatewayNameFilter;
    }

    public SortOrder getGatewayDescriptionOrder() {
        return gatewayDescriptionOrder;
    }

    public void setGatewayDescriptionOrder(SortOrder gatewayDescriptionOrder) {
        this.gatewayDescriptionOrder = gatewayDescriptionOrder;
    }

    public String getGatewayDescriptionFilter() {
        return gatewayDescriptionFilter;
    }

    public void setGatewayDescriptionFilter(String gatewayDescriptionFilter) {
        this.gatewayDescriptionFilter = gatewayDescriptionFilter;
    }

    public List<Gateway> getPreRequestGateways() {
        return preRequestGateways;
    }

    public void setPreRequestGateways(List<Gateway> preRequestGateways) {
        this.preRequestGateways = preRequestGateways;
    }

    public boolean isSelectAllPre() {
        return selectAllPre;
    }

    public void setSelectAllPre(boolean selectAllPre) {
        this.selectAllPre = selectAllPre;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public boolean isSelectAllCamera() {
        return selectAllCamera;
    }

    public void setSelectAllCamera(boolean selectAllCamera) {
        this.selectAllCamera = selectAllCamera;
    }

    public List<Camera> getSelectedCamera() {
        return selectedCamera;
    }

    public void setSelectedCamera(List<Camera> selectedCamera) {
        this.selectedCamera = selectedCamera;
    }


    public List<Long> getPreRequestGatewayIds() {
        return preRequestGatewayIds;
    }

    public void setPreRequestGatewayIds(List<Long> preRequestGatewayIds) {
        this.preRequestGatewayIds = preRequestGatewayIds;
    }

    public HandlePersonAction getHandlePersonAction() {
        return handlePersonAction;
    }

    public void setHandlePersonAction(HandlePersonAction handlePersonAction) {
        this.handlePersonAction = handlePersonAction;
    }

    public int getPersonPage() {
        return personPage;
    }

    public void setPersonPage(int personPage) {
        this.personPage = personPage;
    }

    public List<Person> getUnSelectedPersons() {
        return unSelectedPersons;
    }

    public void setUnSelectedPersons(List<Person> unSelectedPersons) {
        this.unSelectedPersons = unSelectedPersons;
    }

    public List<Person> getNewSelectedPersons() {
        return newSelectedPersons;
    }

    public void setNewSelectedPersons(List<Person> newSelectedPersons) {
        this.newSelectedPersons = newSelectedPersons;
    }

    public List<Gateway> getPreRequier() {
        return preRequier;
    }

    public void setPreRequier(List<Gateway> preRequier) {
        this.preRequier = preRequier;
    }

    public List<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }

    public List<RulePackage> getRulePackageList() {
        return rulePackageList;
    }

    public void setRulePackageList(List<RulePackage> rulePackageList) {
        this.rulePackageList = rulePackageList;
    }

    public List<GatewaySpecialState> getGatewaySpecialStateList() {
        return gatewaySpecialStateList;
    }

    public void setGatewaySpecialStateList(List<GatewaySpecialState> gatewaySpecialStateList) {
        this.gatewaySpecialStateList = gatewaySpecialStateList;
    }

    public List<Person> getNotAssignPersonList() {
        return notAssignPersonList;
    }

    public void setNotAssignPersonList(List<Person> notAssignPersonList) {
        this.notAssignPersonList = notAssignPersonList;
    }

    public DualListModel<Gateway> getGatewayDualList() {
        if (gatewayDualList == null)
            gatewayDualList = new DualListModel<>();
        return gatewayDualList;
    }

    public void setGatewayDualList(DualListModel<Gateway> gatewayDualList) {
        this.gatewayDualList = gatewayDualList;
    }

    public DualListModel<Gateway> getGatewayDuals() {
        if (gatewayDuals == null)
            gatewayDuals = new DualListModel<>();
        return gatewayDuals;
    }

    public void setGatewayDuals(DualListModel<Gateway> gatewayDuals) {
        this.gatewayDuals = gatewayDuals;
    }

    public boolean isDisableFields() {
        return disableFields;
    }

    public void setDisableFields(boolean disableFields) {
        this.disableFields = disableFields;
    }

    public DualListModel<Gateway> getGatewayDualsForZone() {
        if (gatewayDualsForZone == null)
            gatewayDualsForZone = new DualListModel<>();
        return gatewayDualsForZone;
    }

    public void setGatewayDualsForZone(DualListModel<Gateway> gatewayDualsForZone) {
        this.gatewayDualsForZone = gatewayDualsForZone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Rule> getRuleArrayList() {
        return ruleArrayList;
    }

    public void setRuleArrayList(ArrayList<Rule> ruleArrayList) {
        this.ruleArrayList = ruleArrayList;
    }

    public boolean isRuleAniPassBack() {
        return ruleAniPassBack;
    }

    public void setRuleAniPassBack(boolean ruleAniPassBack) {
        this.ruleAniPassBack = ruleAniPassBack;
    }

    public boolean isRuleAllowExit() {
        return ruleAllowExit;
    }

    public void setRuleAllowExit(boolean ruleAllowExit) {
        this.ruleAllowExit = ruleAllowExit;
    }

    public boolean isRuleAllowExitGadget() {
        return ruleAllowExitGadget;
    }

    public void setRuleAllowExitGadget(boolean ruleAllowExitGadget) {
        this.ruleAllowExitGadget = ruleAllowExitGadget;
    }

    public List<Rule> getRuleListTemp() {
        return ruleListTemp;
    }

    public void setRuleListTemp(List<Rule> ruleListTemp) {
        this.ruleListTemp = ruleListTemp;
    }

    public Calendar getSelectedCalendar() {
        return selectedCalendar;
    }

    public void setSelectedCalendar(Calendar selectedCalendar) {
        this.selectedCalendar = selectedCalendar;
    }

    public DayType getRuleDayType() {
        return ruleDayType;
    }

    public void setRuleDayType(DayType ruleDayType) {
        this.ruleDayType = ruleDayType;
    }

    public String getSelectedCalendarIdTemp() {
        return selectedCalendarIdTemp;
    }

    public void setSelectedCalendarIdTemp(String selectedCalendarIdTemp) {
        this.selectedCalendarIdTemp = selectedCalendarIdTemp;
    }

    public String getDayTypeIdTemp() {
        return dayTypeIdTemp;
    }

    public void setDayTypeIdTemp(String dayTypeIdTemp) {
        this.dayTypeIdTemp = dayTypeIdTemp;
    }

    public String getRuleStartTime() {
        return ruleStartTime;
    }

    public void setRuleStartTime(String ruleStartTime) {
        this.ruleStartTime = ruleStartTime;
    }

    public String getRuleEndTime() {
        return ruleEndTime;
    }

    public void setRuleEndTime(String ruleEndTime) {
        this.ruleEndTime = ruleEndTime;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(String startMinute) {
        this.startMinute = startMinute;
    }

    public String getStartSecond() {
        return startSecond;
    }

    public void setStartSecond(String startSecond) {
        this.startSecond = startSecond;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(String endMinute) {
        this.endMinute = endMinute;
    }

    public String getEndSecond() {
        return endSecond;
    }

    public void setEndSecond(String endSecond) {
        this.endSecond = endSecond;
    }

    public String getRuleEntranceCount() {
        return ruleEntranceCount;
    }

    public void setRuleEntranceCount(String ruleEntranceCount) {
        this.ruleEntranceCount = ruleEntranceCount;
    }

    public String getRuleExitCount() {
        return ruleExitCount;
    }

    public void setRuleExitCount(String ruleExitCount) {
        this.ruleExitCount = ruleExitCount;
    }

    public Boolean getRuleDeny() {
        return ruleDeny;
    }

    public void setRuleDeny(Boolean ruleDeny) {
        this.ruleDeny = ruleDeny;
    }

    public String getEditableRule() {
        return editableRule;
    }

    public void setEditableRule(String editableRule) {
        this.editableRule = editableRule;
    }

    public boolean isAddNewRuleFlag() {
        return addNewRuleFlag;
    }

    public void setAddNewRuleFlag(boolean addNewRuleFlag) {
        this.addNewRuleFlag = addNewRuleFlag;
    }

    public Rule getCurrentRule() {
        return currentRule;
    }

    public void setCurrentRule(Rule currentRule) {
        this.currentRule = currentRule;
    }

    public SelectItem[] getDayTypeItems() {
        return dayTypeItems;
    }

    public void setDayTypeItems(SelectItem[] dayTypeItems) {
        this.dayTypeItems = dayTypeItems;
    }

    public SelectItem[] getCalendarItems() {
        return calendarItems;
    }

    public void setCalendarItems(SelectItem[] calendarItems) {
        this.calendarItems = calendarItems;
    }

    public Hashtable<String, DayType> getDayTypeHashtable() {
        return dayTypeHashtable;
    }

    public void setDayTypeHashtable(Hashtable<String, DayType> dayTypeHashtable) {
        this.dayTypeHashtable = dayTypeHashtable;
    }

    public Gateway findForConverter(String s) {
        return gatewayService.findById(Long.parseLong(s));
    }
}
