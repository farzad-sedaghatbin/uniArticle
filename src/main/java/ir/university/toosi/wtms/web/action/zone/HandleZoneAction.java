package ir.university.toosi.wtms.web.action.zone;

import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.calendar.Calendar;
import ir.university.toosi.tms.model.entity.calendar.DayType;
import ir.university.toosi.tms.model.entity.rule.Rule;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.HardwareTree;
import ir.university.toosi.tms.model.entity.zone.Zone;
import ir.university.toosi.tms.model.service.rule.RulePackageServiceImpl;
import ir.university.toosi.tms.model.service.rule.RuleServiceImpl;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
import ir.university.toosi.tms.model.service.zone.ZoneServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.SortOrder;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: behzad
 * Date: 10/27/13
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
@Named(value = "handleZoneAction")
@SessionScoped
public class HandleZoneAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleGatewayAction handleGatewayAction;

    @EJB
    private ZoneServiceImpl zoneService;
    @EJB
    private GatewayServiceImpl gatewayService;
    @EJB
    private RulePackageServiceImpl rulePackageService;
    @EJB
    private RuleServiceImpl ruleService;


    private String editable = "false";
    private DataModel<Zone> zoneList = null;
    private String zoneName;
    private Zone currentZone;
    private Zone newZone = null;
    private int page = 1;
    private Set<Zone> selectedZones = new HashSet<>();
    private Zone selectedZone;
    private boolean selectAll;
    private boolean zoneEnabled;
    private boolean truePassControl;
    private String description;
    private String descText;
    private RulePackage selectedRulePackage;
    private String rulePackageName;
    private String calendarName;
    private boolean antiPassBack, allowExit, allowExitGadget;
    private boolean selectRow = false;
    private List<RulePackage> rulePackageList = null;
    private String zoneNameFilter;
    private String zoneDescriptionFilter;
    private List<HardwareTree> rootZones;
    private SortOrder zoneNameOrder = SortOrder.UNSORTED;
    private SortOrder zoneDescriptionOrder = SortOrder.UNSORTED;

    private ArrayList<Rule> ruleArrayList = new ArrayList<>();

    private boolean ruleAniPassBack = false;
    private boolean ruleAllowExit = false;
    private boolean ruleAllowExitGadget = false;
    private List<Rule> ruleListTemp = null;
    private Calendar selectedCalendar;
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
    private String name;
    private Rule currentRule;
    private SelectItem[] dayTypeItems;
    private SelectItem[] calendarItems = me.calendarItem;
    private Hashtable<String, DayType> dayTypeHashtable = new Hashtable<>();
    private String organNameFilter;
    private String organTypeFilter;
    private String organDescriptionFilter;
    private SortOrder organNameOrder = SortOrder.UNSORTED;
    private SortOrder organTypeOrder = SortOrder.UNSORTED;
    private SortOrder organDescriptionOrder = SortOrder.UNSORTED;
    private int personPage = 1;
    private String listPerson;
    private boolean disableFields;
    private int pageInPopup = 1;


    public void begin() {
        me.setActiveMenu(MenuType.ZONE);
        refresh();
        me.redirect("/zone/list-zone.xhtml");
    }

    public String beginTree() {
        return "zone-tree";
    }

    public void selectZones(ValueChangeEvent event) {
        currentZone = zoneList.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentZone.setSelected(true);
            selectedZones.add(currentZone);
        } else {
            currentZone.setSelected(false);
            selectedZones.remove(currentZone);
        }
    }

    public void changeZones(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            zoneEnabled = true;
        } else
            zoneEnabled = false;
    }


    public void selectAllZone(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (Zone zone : zoneList) {
                zone.setSelected(true);
                selectedZones.add(zone);
            }
        } else {
            for (Zone zone : zoneList) {
                zone.setSelected(false);
            }
        }
        selectedZones.clear();
    }

    public DataModel<Zone> getSelectionGrid() {
        List<Zone> zones = new ArrayList<>();
        refresh();
        return zoneList;
    }

    private void refresh() {
        init();
        List<Zone> zones = zoneService.getAllZone();
        for (Zone zone : zones) {
            zone.setDescText(zone.getDescription());
            zone.setEnabled(zone.isEnabled());
        }
        zoneList = new ListDataModel<>(zones);
    }

    public void onTransfer(TransferEvent event) {
        if (event.isAdd()) {
            for (Object item : event.getItems()) {
                ((Gateway) item).setSelected(true);
                handleGatewayAction.getSelectedGateways().add((Gateway) item);
                if (handleGatewayAction.getGatewayDualsForZone().getTarget() != null)
                    handleGatewayAction.getGatewayDualsForZone().getTarget().add((Gateway) item);
                if (handleGatewayAction.getGatewayDualsForZone().getSource() != null)
                    handleGatewayAction.getGatewayDualsForZone().getSource().remove(item);
            }
        } else {
            for (Object item : event.getItems()) {
                ((Gateway) item).setSelected(false);
                handleGatewayAction.getSelectedGateways().remove(item);
                if (handleGatewayAction.getGatewayDualsForZone().getTarget() != null)
                    handleGatewayAction.getGatewayDualsForZone().getTarget().remove(item);
                if (handleGatewayAction.getGatewayDualsForZone().getSource() != null)
                    handleGatewayAction.getGatewayDualsForZone().getSource().add((Gateway) item);
            }
        }
    }

    public void editRule() {
        currentZone = zoneService.findById(currentZone.getId());
        if (currentZone.getRulePackage() == null) {
            refresh();
            me.addErrorMessage("has.not.rulePackage");
            me.redirect("/zone/list-zone.xhtml");
            return;
        }

        editOrganRule(currentZone.getRulePackage());
    }

    public void remove(String id) {
        currentRule = ruleService.findById(id);
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

    public void doEditZoneRule() {
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

            currentZone.setRulePackage(addedRulePackage);
            boolean condition = zoneService.editZone(currentZone);
            if (condition) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/zone/list-zone.xhtml");
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

    public long time2long(String time) {
        String[] d = time.split(":");
        String s = (d[0].length() == 2 ? d[0] : '0' + d[0]) + (d[1].length() == 2 ? d[1] : '0' + d[1]) + (d[2].length() == 2 ? d[2] : '0' + d[2]);
        return Long.valueOf(s);
    }

    public void editOrganRule(RulePackage rulePackage) {
        rulePackageList = new ArrayList<>();
        ruleListTemp = ruleService.getByRulePackageId(rulePackage.getId());
        name = rulePackage.getName();
        ruleAllowExitGadget = rulePackage.isAllowExitGadget();
        ruleAniPassBack = rulePackage.isAniPassBack();
        ruleAllowExit = rulePackage.isAllowExit();
        selectedCalendar = rulePackage.getCalendar();
        selectedCalendarIdTemp = String.valueOf(selectedCalendar.getId());
        editable = "true";
    }


    public void add() {
        init();
        handleGatewayAction.refresh();
        handleGatewayAction.setGatewayDualsForZone(new DualListModel<>(handleGatewayAction.getGateways(), new ArrayList<Gateway>()));
        setEditable("false");
        setDisableFields(false);
    }

    public void doDelete() {
        currentZone.setEffectorUser(me.getUsername());

        String condition = zoneService.deleteZone(currentZone);
        refresh();
        me.addInfoMessage(condition);
        me.redirect("/zone/list-zone.xhtml");
    }

    public void init() {
        zoneName = "";
        descText = "";
        page = 1;
        selectAll = false;
        zoneEnabled = false;
        currentZone = null;
        zoneDescriptionFilter = "";
        zoneNameFilter = "";
        setSelectRow(false);
    }

    public void view() {
        edit();
        setDisableFields(true);
    }


    public void edit() {
        handleGatewayAction.refresh();
        setEditable("true");
        setDisableFields(false);
        zoneEnabled = currentZone.isEnabled();
        descText = currentZone.getDescText();
        zoneName = currentZone.getName();
        truePassControl = currentZone.isTruePass();
        List<Gateway> gatewayList = null;
        currentZone = zoneService.findById(currentZone.getId());

        gatewayList = gatewayService.findByZone(currentZone);

        List<Gateway> gateways;
        if (gatewayList == null || gatewayList.isEmpty()) {
            gateways = gatewayService.getAllGateway();
        } else {
            gateways = gatewayService.getAllGatewayForZone(gatewayList);
        }
        handleGatewayAction.setGatewayList(handleGatewayAction.getGateways());
        handleGatewayAction.setSelectedGateways(new HashSet<>(gatewayList));
        handleGatewayAction.setGatewayDualsForZone(new DualListModel<>(gateways, gatewayList));
    }


    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();

        }

    }

    public void doEdit() {
        currentZone.setDescription(descText);
        currentZone.setName(zoneName);
        currentZone.setEnabled(zoneEnabled);
        currentZone.setEffectorUser(me.getUsername());
        List<Gateway> gatewayList = null;
        zoneService.editZone(currentZone);
        currentZone = zoneService.findById(currentZone.getId());

        gatewayList = gatewayService.findByZone(currentZone);

        for (Gateway gateway : gatewayList) {
            gateway.setZone(null);
            gatewayService.editGateway(gateway);
        }
        boolean condition = false;
        for (Gateway gateway : handleGatewayAction.getSelectedGateways()) {
            gateway.setZone(currentZone);
            condition = gatewayService.editGateway(gateway);
        }

        if (condition) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/zone/list-zone.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }
    }


    public void doAdd() {
        newZone = new Zone();
        newZone.setDescription(descText);
        newZone.setName(zoneName);
        newZone.setDeleted("0");
        newZone.setEnabled(zoneEnabled);
        newZone.setTruePass(truePassControl);
        newZone.setEffectorUser(me.getUsername());
        newZone.setStatus("c");
        ////???????????????
        Zone insertedZone = null;
        insertedZone = zoneService.createZone(newZone);
        Set<Gateway> list = handleGatewayAction.getSelectedGateways();
        for (Gateway gateway : list) {
            if (gateway.getZone() == null) {
                gateway.setZone(insertedZone);
                boolean condition = gatewayService.editGateway(gateway);
            }
        }


        if (insertedZone != null) {
//
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/zone/list-zone.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }

    public void assignRule() {
        currentZone = zoneService.findById(currentZone.getId());
        selectedRulePackage = currentZone.getRulePackage();
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


    private void doAssignRule() {
        currentZone.setEffectorUser(me.getUsername());
        currentZone.setRulePackage(selectedRulePackage);
        boolean condition = zoneService.editZone(currentZone);
        if (condition) {
            refresh();
            me.addInfoMessage("operation.occurred");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }
    }
//
//    public Filter<?> getZoneNameFilterImpl() {
//        return new Filter<Zone>() {
//            public boolean accept(Zone zone) {
//                return zoneNameFilter == null || zoneNameFilter.length() == 0 || zone.getName().toLowerCase().contains(zoneNameFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getZoneDescriptionFilterImpl() {
//        return new Filter<Zone>() {
//            public boolean accept(Zone zone) {
//                return zoneDescriptionFilter == null || zoneDescriptionFilter.length() == 0 || zone.getDescription().toLowerCase().contains(zoneDescriptionFilter.toLowerCase());
//            }
//        };
//    }

    public void sortByZoneName() {
        zoneDescriptionOrder = SortOrder.UNSORTED;

        if (zoneNameOrder.equals(SortOrder.ASCENDING)) {
            setZoneNameOrder(SortOrder.DESCENDING);
        } else {
            setZoneNameOrder(SortOrder.ASCENDING);
        }
    }

    public void sortByZoneDescription() {
        zoneNameOrder = SortOrder.UNSORTED;

        if (zoneDescriptionOrder.equals(SortOrder.ASCENDING)) {
            setZoneDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setZoneDescriptionOrder(SortOrder.ASCENDING);
        }
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

    public void selectForEdit() {
        currentZone = zoneList.getRowData();
        setSelectRow(true);

    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }


    public int getPage() {
//        currentZone = null;
        setSelectRow(false);
        return page;
    }

    public List<HardwareTree> getRootZones() {
        List<Zone> zones = zoneService.getAllZone();
//                rootZones = Zone.prepareHierarchy(zones, me);
        return rootZones;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getEditable() {
        return editable;
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

    public DataModel<Zone> getZoneList() {
        return zoneList;
    }

    public void setZoneList(DataModel<Zone> zoneList) {
        this.zoneList = zoneList;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Zone getCurrentZone() {
        return currentZone;
    }

    public void setCurrentZone(Zone currentZone) {
        this.currentZone = currentZone;
    }

    public Zone getNewZone() {
        return newZone;
    }

    public void setNewZone(Zone newZone) {
        this.newZone = newZone;
    }

    public Set<Zone> getSelectedZones() {
        return selectedZones;
    }

    public void setSelectedZones(Set<Zone> selectedZones) {
        this.selectedZones = selectedZones;
    }

    public Zone getSelectedZone() {
        return selectedZone;
    }

    public void setSelectedZone(Zone selectedZone) {
        this.selectedZone = selectedZone;
    }

    public boolean isZoneEnabled() {
        return zoneEnabled;
    }

    public void setZoneEnabled(boolean zoneEnabled) {
        this.zoneEnabled = zoneEnabled;
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

    public List<RulePackage> getRulePackageList() {
        return rulePackageList;
    }

    public void setRulePackageList(List<RulePackage> rulePackageList) {
        this.rulePackageList = rulePackageList;
    }

    public void setRootZones(List<HardwareTree> rootZones) {
        this.rootZones = rootZones;
    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public SortOrder getZoneNameOrder() {
        return zoneNameOrder;
    }

    public void setZoneNameOrder(SortOrder zoneNameOrder) {
        this.zoneNameOrder = zoneNameOrder;
    }

    public String getZoneNameFilter() {
        return zoneNameFilter;
    }

    public void setZoneNameFilter(String zoneNameFilter) {
        this.zoneNameFilter = zoneNameFilter;
    }

    public SortOrder getZoneDescriptionOrder() {
        return zoneDescriptionOrder;
    }

    public void setZoneDescriptionOrder(SortOrder zoneDescriptionOrder) {
        this.zoneDescriptionOrder = zoneDescriptionOrder;
    }

    public String getZoneDescriptionFilter() {
        return zoneDescriptionFilter;
    }

    public void setZoneDescriptionFilter(String zoneDescriptionFilter) {
        this.zoneDescriptionFilter = zoneDescriptionFilter;
    }

    public HandleGatewayAction getHandleGatewayAction() {
        return handleGatewayAction;
    }

    public void setHandleGatewayAction(HandleGatewayAction handleGatewayAction) {
        this.handleGatewayAction = handleGatewayAction;
    }

    public boolean isTruePassControl() {
        return truePassControl;
    }

    public void setTruePassControl(boolean truePassControl) {
        this.truePassControl = truePassControl;
    }

    public boolean isDisableFields() {
        return disableFields;
    }

    public void setDisableFields(boolean disableFields) {
        this.disableFields = disableFields;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getOrganDescriptionFilter() {
        return organDescriptionFilter;
    }

    public void setOrganDescriptionFilter(String organDescriptionFilter) {
        this.organDescriptionFilter = organDescriptionFilter;
    }

    public SortOrder getOrganNameOrder() {
        return organNameOrder;
    }

    public void setOrganNameOrder(SortOrder organNameOrder) {
        this.organNameOrder = organNameOrder;
    }

    public SortOrder getOrganTypeOrder() {
        return organTypeOrder;
    }

    public void setOrganTypeOrder(SortOrder organTypeOrder) {
        this.organTypeOrder = organTypeOrder;
    }

    public SortOrder getOrganDescriptionOrder() {
        return organDescriptionOrder;
    }

    public void setOrganDescriptionOrder(SortOrder organDescriptionOrder) {
        this.organDescriptionOrder = organDescriptionOrder;
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

    public int getPageInPopup() {
        return pageInPopup;
    }

    public void setPageInPopup(int pageInPopup) {
        this.pageInPopup = pageInPopup;
    }
}

