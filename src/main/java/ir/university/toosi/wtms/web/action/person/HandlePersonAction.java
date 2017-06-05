package ir.university.toosi.wtms.web.action.person;

import ir.university.toosi.parking.action.HandleCarAction;
import ir.university.toosi.parking.entity.Car;
import ir.university.toosi.parking.service.CarServiceImpl;
import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.Lookup;
import ir.university.toosi.tms.model.entity.PersonSearch;
import ir.university.toosi.tms.model.entity.calendar.Calendar;
import ir.university.toosi.tms.model.entity.calendar.DayType;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.personnel.Job;
import ir.university.toosi.tms.model.entity.personnel.Organ;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.rule.Rule;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.tms.model.service.BLookupServiceImpl;
import ir.university.toosi.tms.model.service.calendar.CalendarServiceImpl;
import ir.university.toosi.tms.model.service.calendar.DayTypeServiceImpl;
import ir.university.toosi.tms.model.service.personnel.CardServiceImpl;
import ir.university.toosi.tms.model.service.personnel.JobServiceImpl;
import ir.university.toosi.tms.model.service.personnel.OrganServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.rule.RulePackageServiceImpl;
import ir.university.toosi.tms.model.service.rule.RuleServiceImpl;
import ir.university.toosi.tms.util.Configuration;
import ir.university.toosi.tms.util.LangUtil;
import ir.university.toosi.wtms.web.action.AccessControlAction;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.organ.HandleOrganAction;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.wtms.web.util.CalendarUtil;
import ir.university.toosi.wtms.web.util.ImageUtils;
import ir.university.toosi.wtms.web.util.ReportUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handlePersonAction")
@SessionScoped
public class HandlePersonAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private GeneralHelper generalHelper;
    @Inject
    private AccessControlAction accessControlAction;
    @Inject
    private HandleOrganAction handleOrganAction;
    @Inject
    private CalendarServiceImpl calendarService;
    @Inject
    private HandleCarAction handleCarAction;
    @EJB
    private PersonServiceImpl personService;

    @EJB
    private OrganServiceImpl organService;
    @EJB
    private RulePackageServiceImpl rulePackageService;
    @EJB
    private RuleServiceImpl ruleService;
    @EJB
    private JobServiceImpl jobService;
    @EJB
    private DayTypeServiceImpl dayTypeService;
    @EJB
    private BLookupServiceImpl bLookupService;
    @EJB
    private CardServiceImpl cardService;
    @EJB
    private CarServiceImpl carService;


    private List<Long> personListID = new ArrayList<>();
    private Person currentPerson = null;
    private Job currentJob = null;
    private Person newPerson = null;
    private String nationalCode, lastname, personname, personNo, email, mobile, address, extraField1, extraField2, extraField3, extraField4, simpleValue;
    private String oldPassword, password, rePassword, cardData, personnelNo, workStation;
    private boolean extraField1Enable, extraField2Enable, extraField3Enable, extraField4Enable, roleEnabled;
    private String editable = "false";
    private boolean citySelected = false;
    private boolean enabled = true;
    private String lock = "false";
    private int page = 1;
    private int page2 = 1;
    private int pageInPopup = 1;
    private SelectItem[] organItem;
    private Person selectedPerson;
    private Person dataModelPerson;
    private Organ selectedOrgan;
    private String selectedOrganName, rulePackageName, organId, calendarName;
    private boolean antiPassBack, allowExit, allowExitGadget;
    private List<RulePackage> rulePackageList = null;
    private RulePackage selectedRulePackage;
    private byte[] picture;
    private String name;
    private List<String> pageCount = new ArrayList<>();
    private int pageFrom = 1;
    private int pageTo = 5;
    private List<Rule> ruleArrayList = new ArrayList<>();
    private boolean ruleAniPassBack = false;
    private boolean ruleAllowExit = false;
    private boolean ruleAllowExitGadget = false;
    private boolean selectRow = false;
    private boolean havePicture = false;
    private boolean firstPage = false;
    private boolean lastPage = false;
    private DataModel<Rule> ruleListTemp = null;
    private Calendar selectedCalendar;
    private DayType ruleDayType;
    private String selectedCalendarIdTemp, dayTypeIdTemp, ruleStartTime, ruleEndTime, startHour, startMinute, startSecond;
    private String endHour, endMinute, endSecond, ruleEntranceCount, ruleExitCount, finger;
    private Boolean ruleDeny;
    private List<Card> cards = new ArrayList<>();
    private Job job;
    private String editableRule = "false";
    private boolean addNewRuleFlag = false;
    private Rule currentRule;
    private SelectItem[] dayTypeItems;
    private List<Calendar> calendarItems = new ArrayList();
    private Hashtable<String, DayType> dayTypeHashtable = new Hashtable<>();
    private String employNo;
    private BLookup employeeType;
    private List<BLookup> employeeTypes = new ArrayList<>();
    private BLookup assistType;
    private List<BLookup> assistTypes = new ArrayList<>();
    private BLookup postType;
    private List<BLookup> postTypes = new ArrayList<>();
    private List<Long> innerPersonList = new ArrayList<>();
    private String folderNo, internalTel, preCondition, attributeName, attributeValue, postCondition, description;
    private SelectItem[] preConditions, postConditions, attributeNames;
    private List<PersonSearch> personSearchList = new ArrayList<>();
    private int rowIndex = 0;
    private int pageIndex = 1;
    private int totalPages = 1;
    private List<Person> persons = new ArrayList<>();
    private String lastPageIndex = "1";
    private List<PersonSearch> personSearches = null;

    private SortOrder personnameOrder = SortOrder.UNSORTED;
    private SortOrder personFamilyOrder = SortOrder.UNSORTED;
    private SortOrder personnelNoOrder = SortOrder.UNSORTED;

    private String personnameFilter;
    private String personFamilyFilter;
    private String personnelNoFilter;
    private StreamedContent graphicText;

    public void begin() {
//        me.setActiveMenu(MenuType.PERSONEL);
        refresh();
        me.redirect("/person/persons.xhtml");
    }


    public void init() {
        rowIndex = 0;
        firstPage = true;
        lastPage = false;
        pageIndex = 1;
        totalPages = 1;
        lastname = "";
        nationalCode = "";
        personname = "";
        password = "";
        personNo = "";
        simpleValue = "";
        email = "";
        pageFrom = 1;
        pageTo = 6;
        mobile = "";
        address = "";
        rePassword = "";
        oldPassword = "";
        workStation = "";
        personnelNo = "";
        enabled = false;
        roleEnabled = true;
        page = 1;
        picture = new byte[0];
        calendarItems = new ArrayList();
        fillDayTypeCombo();
        setCurrentPerson(null);
        setSelectRow(false);
        employNo = "";
        employeeType = new BLookup();
        employeeTypes = new ArrayList<>();
        folderNo = "";
        internalTel = "";
        assistType = new BLookup();
        assistTypes = new ArrayList<>();
        postType = new BLookup();
        postTypes = new ArrayList<>();
        description = "";
        selectedOrganName = "";
        preCondition = "";
        postCondition = "";
        attributeName = "";
        attributeValue = "";
        personnameFilter = "";
        personFamilyFilter = "";
        personnelNoFilter = "";
        personSearchList = new ArrayList<>();
        fillSearchCombos();
        personSearches = personSearchList;
        lastPageIndex = "1";
        persons = new ArrayList<>();
        pageCount = new ArrayList<>();
//        extraField1Enable = me.getSystemParameter().get(SystemParameterType.PERSON_EXTRA_FIELD_1).equalsIgnoreCase("true") ? true : false;
//        extraField2Enable = me.getSystemParameter().get(SystemParameterType.PERSON_EXTRA_FIELD_2).equalsIgnoreCase("true") ? true : false;
//        extraField3Enable = me.getSystemParameter().get(SystemParameterType.PERSON_EXTRA_FIELD_3).equalsIgnoreCase("true") ? true : false;
//        extraField4Enable = me.getSystemParameter().get(SystemParameterType.PERSON_EXTRA_FIELD_4).equalsIgnoreCase("true") ? true : false;
        extraField1 = "";
        extraField2 = "";
        extraField3 = "";
        extraField4 = "";
        havePicture = false;
    }

    public void pageCordinator(String pageIndex, boolean ten, boolean increase, boolean lastFirst) {
        if (lastFirst) {
            if (increase) {
                lastPage = true;
                firstPage = false;
                int position = totalPages - Integer.valueOf(lastPageIndex);
                for (int i = 1; i <= Math.abs(position); i++) {
                    next();
                }
            } else {
                lastPage = false;
                firstPage = true;
                int position = Integer.valueOf(lastPageIndex);
                for (int i = 1; i <= Math.abs(position); i++) {
                    previous();
                }
            }
            return;
        }
        int request = 0;
        if (!ten) {
            request = Integer.valueOf(pageIndex);
        } else if (increase) {
            request = Integer.valueOf(this.pageIndex) + 6;
            if (request > totalPages) {
                request = totalPages;
                lastPage = true;
            }
        } else {
            request = Integer.valueOf(this.pageIndex) - 6;
            if (request < 1) {
                request = 1;
                firstPage = true;
            }
        }
        int position = request - Integer.valueOf(lastPageIndex);

        if (position > 0) {
            for (int i = 1; i <= Math.abs(position); i++) {
                next();
            }
        } else {
            for (int i = 1; i <= Math.abs(position); i++) {
                previous();
            }
        }
        lastPageIndex = String.valueOf(request);
    }

    public void selectForEdit() {
        String personId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("personId");

        System.out.println(">>>>>>>>>>>>>>>>");
        currentPerson = personService.findById(Long.parseLong(personId));
        selectRow = true;
    }

    public void back() {
        refresh();
        me.redirect("/person/persons.xhtml");
    }

    private void fillSearchCombos() {

        preConditions = new SelectItem[3];
        preConditions[0] = new SelectItem(" ", "انتخاب یک گزینه");
        preConditions[1] = new SelectItem("(", "(");
        preConditions[2] = new SelectItem("not", "not");

        postConditions = new SelectItem[4];
        postConditions[0] = new SelectItem(" ", "انتخاب یک گزینه");
        postConditions[1] = new SelectItem(")", ")");
        postConditions[2] = new SelectItem("and", "and");
        postConditions[3] = new SelectItem("or", "or");

        attributeNames = new SelectItem[4];
        attributeNames[0] = new SelectItem("name", "نام");
        attributeNames[1] = new SelectItem("lastname", "نام خانوادگی");
        attributeNames[2] = new SelectItem("personnelNo", "کد پرسنلی");
        attributeNames[3] = new SelectItem("nationalCode", "کد ملی");
    }

    public void listener(FileUploadEvent event) throws Exception {
        UploadedFile item = event.getFile();
        BufferedImage sourceBufferedImage = ImageUtils.convertByteArrayToBufferedImage(item.getContents());
        float scaleRatio = ImageUtils.calculateScaleRatio(sourceBufferedImage.getWidth(), 200);//todo from properties
        if (scaleRatio > 0 && scaleRatio < 1) {
            try {
                sourceBufferedImage = ImageUtils.scaleImage(sourceBufferedImage, scaleRatio);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setPicture(ImageUtils.imageToByteArray(sourceBufferedImage));
        havePicture = true;
    }


    private void fillDayTypeCombo() {

        List<DayType> dayTypes = null;
        dayTypes = dayTypeService.getAllDayType();

        dayTypeItems = new SelectItem[dayTypes.size()];
        int i = 0;
        for (DayType dayType : dayTypes) {
            dayTypeHashtable.put(String.valueOf(dayType.getId()), dayType);
            dayTypeItems[i] = new SelectItem(dayType.getId(), dayType.getTitle());
            i++;
        }
    }

    private void refresh() {
        init();
        innerPersonList = personService.getAllPersonID();

        totalPages = (int) Math.abs(Math.ceil(((double) innerPersonList.size()) / 6));
        if (totalPages <= 6)
            pageTo = totalPages;
        for (int i = pageFrom; i <= pageTo; i++) {
            pageCount.add(String.valueOf(i));
        }
        if (innerPersonList.size() > 5) {
            personListID = innerPersonList.subList(rowIndex, rowIndex + 6);
        } else {
            personListID = innerPersonList;
        }
        for (Long personId : personListID) {
            Person person = personService.findById(personId);
            persons.add(person);
        }
        if (innerPersonList.size() == 0) {
            persons.clear();
        }

    }

    public void next() {
        persons.clear();
        pageCount.clear();
        pageIndex++;
        if (pageTo + 1 <= totalPages && pageIndex > 6)
            pageTo++;
        if (totalPages > 6 && pageTo > 6 && pageIndex > 6)
            pageFrom++;
        firstPage = false;
        lastPageIndex = String.valueOf(pageIndex);
        if (pageIndex < totalPages) {
            rowIndex = rowIndex + 6;
            personListID = innerPersonList.subList(rowIndex, rowIndex + 6);
            for (Long personId : personListID) {
                Person person = personService.findById(personId);
                persons.add(person);
            }
            for (int i = pageFrom; i <= pageTo; i++) {
                pageCount.add(String.valueOf(i));
            }
        } else if (pageIndex == totalPages) {
            lastPage = true;
            rowIndex = rowIndex + 6;
            personListID = innerPersonList.subList(rowIndex, rowIndex + (innerPersonList.size() - rowIndex - 1));
            for (Long personId : personListID) {
                Person person = personService.findById(personId);
                persons.add(person);
            }
            for (int i = pageFrom; i <= pageTo; i++) {
                pageCount.add(String.valueOf(i));
            }
        }
    }

    public void previous() {
        persons.clear();
        pageCount.clear();
        if (pageFrom - 1 >= 1)
            pageFrom--;
        if (totalPages > 6 && pageTo > 6 && pageIndex > 6)
            pageTo--;
        if (pageIndex != 1) {
            pageIndex--;
            lastPage = false;
            rowIndex = rowIndex - 6;
            personListID = innerPersonList.subList(rowIndex, rowIndex + 6);
            for (Long personId : personListID) {
                Person person = personService.findById(personId);
                persons.add(person);
            }
            for (int i = pageFrom; i <= pageTo; i++) {
                pageCount.add(String.valueOf(i));
            }
        }
        if (pageIndex == 1) {
            firstPage = true;
            lastPage = false;
        }
        lastPageIndex = String.valueOf(pageIndex);
    }

    public String exportExcel() {
        List<String> titles = new ArrayList<>();
        titles.add("name");
        titles.add("lastname");
        titles.add("personnelCode");
        titles.add("nationalCode");
        Workbook currentWorkbook = new HSSFWorkbook();
        Sheet sheet = currentWorkbook.createSheet("persons");
        sheet.autoSizeColumn(0);

        int rowCounter = 0;
        Row row = sheet.createRow(rowCounter);
        int cellCounter = 0;
        int rowSize = titles.size();
        Cell cell = row.createCell(cellCounter);

        for (String title : titles) {
            cell = row.createCell(cellCounter);
            cellCounter++;
            cell.setCellValue(title);
        }

        for (Long report : innerPersonList) {

            Person reportEntity = returnPerson(report);
            rowCounter++;
            row = sheet.createRow(rowCounter);
            cellCounter = 0;
            cell = row.createCell(cellCounter);
            String s1 = reportEntity.getName();
            cell.setCellValue(s1);
            cellCounter++;
            cell = row.createCell(cellCounter);
            s1 = reportEntity.getLastName();
            cell.setCellValue(s1);
            cellCounter++;
            cell = row.createCell(cellCounter);
            s1 = reportEntity.getPersonnelNo();
            cell.setCellValue(s1);
            cellCounter++;
            cell = row.createCell(cellCounter);
            s1 = reportEntity.getNationalCode();
            cell.setCellValue(s1);
        }
        for (int i = 0; i < titles.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        for (int i = titles.size(); i < (rowSize + titles.size()); i++) {
            sheet.autoSizeColumn(i);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            currentWorkbook.write(byteArrayOutputStream);

            new ReportUtils<>().readyForDownload(byteArrayOutputStream.toByteArray(), "vnd.ms-excel", LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd")) + ".xls");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void addCondition() {

        PersonSearch personSearch = new PersonSearch(preCondition, attributeName, attributeValue, postCondition);
        personSearchList.add(personSearch);
        personSearches = personSearchList;
        preCondition = " ";
        attributeName = "name";
        attributeValue = "";
        postCondition = " ";
    }

    public void removeCondition() {
        List<PersonSearch> searches = new ArrayList<>();
//        PersonSearch personSearchData = personSearches.getRowData();
        for (PersonSearch personSearch : personSearchList) {
//            if (personSearch.equals(personSearchData)) {
//                searches.add(personSearch);
//            }
        }
        personSearchList.removeAll(searches);
        personSearches = personSearchList;
    }

    public void simpleSearch() {
        persons.clear();
        lastPage = false;
        rowIndex = 0;
        pageFrom = 1;
        pageTo = 6;

        String query = "select p.id from Person p where (";
        query += " p.name like \'%" + simpleValue + "%\' or " + " p.lastName like \'%" + simpleValue + "%\' or " + " p.personnelNo like \'%" + simpleValue + "%\') and p.deleted='0' ";

        personSearchList = new ArrayList<>();
        innerPersonList = personService.query(query);
        totalPages = (int) Math.abs(Math.ceil(((double) innerPersonList.size()) / 6));

        if (innerPersonList.size() > 5) {
            personListID = innerPersonList.subList(rowIndex, rowIndex + 6);
        } else {
            personListID = innerPersonList;
        }

        for (Long personId : personListID) {
            Person person = personService.findById(personId);
            persons.add(person);
        }
        if (innerPersonList.size() == 0) {
            persons.clear();
        }
        lastPageIndex = "1";
        pageCount.clear();
        if (totalPages <= 6)
            pageTo = totalPages;
        for (int i = pageFrom; i <= pageTo; i++) {
            pageCount.add(String.valueOf(i));
        }
        if (totalPages == 1)
            lastPage = true;

    }

    public void search() {
        persons.clear();
        lastPage = false;
        rowIndex = 0;
        pageFrom = 1;
        pageTo = 6;

        String query = "select p.id from Person p where ";
        for (PersonSearch personSearch : personSearchList) {
            query += personSearch.getPreCondition() + " p." + personSearch.getAttributeName() + " like \'" + personSearch.getAttributeValue() + "\' " + personSearch.getPostCondition() + " ";
        }
        if (personSearchList.isEmpty())
            query += "p.deleted=\'0\'";
        else
            query += "and p.deleted=\'0\'";

        personSearchList = new ArrayList<>();
        innerPersonList = personService.query(query);

        totalPages = (int) Math.abs(Math.ceil(((double) innerPersonList.size()) / 6));

        if (innerPersonList.size() > 5) {
            personListID = innerPersonList.subList(rowIndex, rowIndex + 6);
        } else {
            personListID = innerPersonList;
        }

        for (Long personId : personListID) {
            Person person = personService.findById(personId);
            persons.add(person);
        }
        if (innerPersonList.size() == 0) {
            persons.clear();
        }
        lastPageIndex = "1";
        pageCount.clear();
        if (totalPages <= 6)
            pageTo = totalPages;
        for (int i = pageFrom; i <= pageTo; i++) {
            pageCount.add(String.valueOf(i));
        }
        if (totalPages == 1)
            lastPage = true;

    }

    public void personChange(ValueChangeEvent event) {
        String id = (String) event.getNewValue();
        if (!id.equalsIgnoreCase("0")) {
            selectedPerson = null;//me.getGeneralHelper().getWorkGroupService().findById(id);
        } else {
            selectedPerson = null;
        }
    }

    public void assignCard() {

    }

    public void selectEmployType(ValueChangeEvent event) {
        Long selectedId = (Long) event.getNewValue();
        for (BLookup bLookup : getEmployeeTypes()) {
            if (selectedId.equals(bLookup.getId())) {
                employeeType = bLookup;
            }
        }
    }

    public void selectPostType(ValueChangeEvent event) {
        Long selectedId = (Long) event.getNewValue();
        for (BLookup bLookup : getPostTypes()) {
            if (selectedId.equals(bLookup.getId())) {
                postType = bLookup;
            }
        }
    }

    public void selectAssistType(ValueChangeEvent event) {
        Long selectedId = (Long) event.getNewValue();
        for (BLookup bLookup : getAssistTypes()) {
            if (selectedId.equals(bLookup.getId())) {
                assistType = bLookup;
            }
        }
    }

    public void doDelete(String personId) {
        currentPerson = personService.findById(Long.parseLong(personId));
        String currentDate = LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd"));
        String currentTime = CalendarUtil.getTime(new Date(), new Locale("fa"));
        currentPerson.setCreateDate(currentDate);
        currentPerson.setCreateTime(currentTime);
        currentPerson.setStatus("o," + me.getUsername());
        currentPerson.setEffectorUser(me.getUsername());
        String condition = personService.deletePerson(currentPerson);

        currentJob = jobService.findByPersonId(currentPerson.getId());

        if (currentJob != null) {
            currentJob.setStatus("o," + me.getUsername());
            currentJob.setEffectorUser(me.getUsername());
            condition = jobService.deleteJob(currentJob);
        }
        refresh();
        me.addInfoMessage(condition);
        me.redirect("/person/persons.xhtml");
    }

    public void add() {
        init();
        rowIndex = 0;
        firstPage = true;
        lastPage = false;
        pageIndex = 1;
        totalPages = 1;
        lastname = "";
        nationalCode = "";
        personname = "";
        password = "";
        personNo = "";
        email = "";
        mobile = "";
        address = "";
        rePassword = "";
        oldPassword = "";
        workStation = "";
        personnelNo = "";
        enabled = false;
        roleEnabled = true;
        page = 1;
        picture = new byte[0];
        calendarItems = new ArrayList();
        fillDayTypeCombo();
        setCurrentPerson(null);
        setSelectRow(false);
        employNo = "";
        employeeType = new BLookup();
        employeeTypes = new ArrayList<>();
        folderNo = "";
        internalTel = "";
        assistType = new BLookup();
        assistTypes = new ArrayList<>();
        postType = new BLookup();
        postTypes = new ArrayList<>();
        description = "";
        selectedOrganName = "";
        preCondition = "";
        postCondition = "";
        attributeName = "";
        attributeValue = "";
        personnameFilter = "";
        personFamilyFilter = "";
        personnelNoFilter = "";
        fillSearchCombos();
//        extraField1Enable = me.getSystemParameter().get(SystemParameterType.PERSON_EXTRA_FIELD_1).equalsIgnoreCase("true") ? true : false;
//        extraField2Enable = me.getSystemParameter().get(SystemParameterType.PERSON_EXTRA_FIELD_2).equalsIgnoreCase("true") ? true : false;
//        extraField3Enable = me.getSystemParameter().get(SystemParameterType.PERSON_EXTRA_FIELD_3).equalsIgnoreCase("true") ? true : false;
//        extraField4Enable = me.getSystemParameter().get(SystemParameterType.PERSON_EXTRA_FIELD_4).equalsIgnoreCase("true") ? true : false;
        extraField1 = "";
        extraField2 = "";
        extraField3 = "";
        extraField4 = "";
        havePicture = false;
        setEditable("false");

    }

    public void resetPassword() {
        if (password.equals(rePassword)) {
            currentPerson.setPassword(password);
            boolean condition = personService.editPerson(currentPerson);
            if (condition) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/person/persons.xhtml");
                return;
            }
        }
        me.addInfoMessage("password.not.match");

    }

    public void showTree() {
        newPerson = new Person();
        init();
        setEditable("false");
    }

    public void doAdd() {
        if (!password.equals(rePassword)) {
            me.addInfoMessage("password.not match");
        }
        String currentDate = LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd"));
        String currentTime = CalendarUtil.getTime(new Date(), new Locale("fa"));
        newPerson = new Person();
        newPerson.setEmail(getEmail());
        newPerson.setAddress(getAddress());
        newPerson.setName(getPersonname());
        newPerson.setLastName(getLastname());
        newPerson.setMobile(getMobile());
        newPerson.setPersonnelNo(getPersonnelNo());
        newPerson.setNationalCode(getNationalCode());
        newPerson.setDeleted("0");
        newPerson.setEffectorUser(me.getUsername());
        newPerson.setWorkStation(getWorkStation());
        if (getPicture().length != 0) {
            newPerson.setPicture(getPicture());
        }
//        newPerson.setEnable(enabled == true ? "true" : "false");
        newPerson.setStatus("c");
        newPerson.setCreateDate(currentDate);
        newPerson.setCreateTime(currentTime);
        newPerson.setCreateBy(me.getUsername());
        newPerson.setPassword(password);
        newPerson.setOrganRef(selectedOrgan);

        boolean condition = personService.editPerson(newPerson);
        if (condition) {
            me.addInfoMessage("person.exist");
            return;
        }
        Person person = null;
        person = personService.createPerson(newPerson);
        Job newJob = new Job();
        newJob.setDeleted("0");
        newJob.setEffectorUser(me.getUsername());
        newJob.setStatus("c");
        newJob.setEffectorUser(me.getUsername());
        newJob.setAssistType(assistType);
        newJob.setDescription(description);
        newJob.setEmployNo(employNo);
        newJob.setInternalTel(internalTel);
        newJob.setPostType(postType);
        newJob.setPerson(person);
        newJob.setFolderNo(folderNo);
        newJob.setEmployType(employeeType);

        Job job = jobService.createJob(newJob);


        if (person != null || job != null) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/person/persons.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }

    public void edit(String personId) {
        currentPerson = personService.findById(Long.parseLong(personId));
        detail();
        setEditable("true");
        havePicture = true;
        personname = currentPerson.getName();
        lastname = currentPerson.getLastName();
        email = currentPerson.getEmail();
        mobile = currentPerson.getMobile();
        address = currentPerson.getAddress();
        if (currentPerson.getOrganRef() != null)
            selectedOrganName = currentPerson.getOrganRef().getName();
        setNationalCode(currentPerson.getNationalCode());
        setPersonnelNo(currentPerson.getPersonnelNo());
        if (currentPerson.getOrganRef() != null)
            selectedOrgan = currentPerson.getOrganRef();
        setWorkStation(currentPerson.getWorkStation());
        picture = currentPerson.getPicture();
        password = currentPerson.getPassword();
        rePassword = currentPerson.getPassword();
        if (currentPerson.getFinger() == null) {
            finger = "false";
        } else {
            finger = "true";
        }
        currentJob = jobService.findByPersonId(currentPerson.getId());
        if (currentJob == null) {
            currentJob = new Job();
        }
        description = currentJob.getDescription();
        internalTel = currentJob.getInternalTel();
        folderNo = currentJob.getFolderNo();
        employNo = currentJob.getEmployNo();
        employeeType = currentJob.getEmployType();
        postType = currentJob.getPostType();
        assistType = currentJob.getAssistType();
        List<Organ> organs = organService.getAllOrgan();
        handleOrganAction.setOrgans(organs);
    }

    public void detail() {
        setEditable("true");
        cards = cardService.findByPersonId(currentPerson.getId());

        if (currentPerson.getFinger() == null) {
            finger = "false";
        } else {
            finger = "true";
        }

    }

    public void doEdit() {
//        Person person = new Person(personname, currentPerson.getPassword(), enabled == true ? "true" : "false");
        String currentDate = LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd"));
        String currentTime = CalendarUtil.getTime(new Date(), new Locale("fa"));
        currentPerson.setCreateDate(currentDate);
        currentPerson.setCreateTime(currentTime);
        currentPerson.setStatus("c");
        currentPerson.setCreateBy(me.getUsername());
        currentPerson.setDeleted("0");
        currentPerson.setEffectorUser(me.getUsername());
        currentPerson.setEmail(getEmail());
        currentPerson.setAddress(getAddress());
        currentPerson.setName(getPersonname());
        currentPerson.setLastName(getLastname());
        currentPerson.setMobile(getMobile());
        currentPerson.setPersonnelNo(getPersonnelNo());
        currentPerson.setNationalCode(getNationalCode());
        currentPerson.setDeleted("0");
        currentPerson.setWorkStation(getWorkStation());
        if (getPicture() != null && getPicture().length != 0) {
            currentPerson.setPicture(getPicture());
        } else {
            currentPerson.setPicture(null);
        }
//        newPerson.setEnable(enabled == true ? "true" : "false");
        currentPerson.setStatus("c");
        currentPerson.setCreateBy(me.getUsername());
        currentPerson.setOrganRef(selectedOrgan);

        boolean condition = personService.editPerson(currentPerson);
        currentJob.setEffectorUser(me.getUsername());
        currentJob.setAssistType(assistType);
        currentJob.setDescription(description);
        currentJob.setEmployNo(employNo);
        currentJob.setInternalTel(internalTel);
        currentJob.setPostType(postType);
        currentJob.setPerson(getCurrentPerson());
        currentJob.setFolderNo(folderNo);
        currentJob.setEmployType(employeeType);
        boolean tempFirst = firstPage;
        boolean tempLast = lastPage;
        condition = jobService.editJob(currentJob);
        if (condition) {
            int p = pageIndex;
//                    refresh();
            page = p;
            lastPage = tempLast;
            firstPage = tempFirst;
            pageCordinator(String.valueOf(p), false, false, false);
            me.addInfoMessage("operation.occurred");
            me.redirect("/person/persons.xhtml");
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

    public void assignRule(String personId) {
        currentPerson = personService.findById(Long.parseLong(personId));
        selectedRulePackage = currentPerson.getRulePackage();
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

        List rulePackages = rulePackageService.getAllRulePackage();
        rulePackageList = rulePackages;
    }

    public void doAssignCar() {
        Car car = handleCarAction.getCurrentCar();
        car.setPerson(currentPerson);
        carService.editCar(car);
    }

    public void editRule(String personId) {
        currentPerson = personService.findById(Long.parseLong(personId));
        if (currentPerson.getRulePackage() == null) {
            refresh();
            me.addErrorMessage("has.not.rulePackage");
            me.redirect("/person/persons.xhtml");
            return;
        }

        editPersonRule(currentPerson.getRulePackage());
    }


    public void editPersonRule(RulePackage rulePackage) {
        ruleArrayList = new ArrayList<>();
        ruleArrayList = ruleService.getByRulePackageId(rulePackage.getId());
        ruleListTemp = new ListDataModel<>(ruleArrayList);
        name = rulePackage.getName();
        ruleAllowExitGadget = rulePackage.isAllowExitGadget();
        ruleAniPassBack = rulePackage.isAniPassBack();
        ruleAllowExit = rulePackage.isAllowExit();
        calendarItems = calendarService.getAllCalendar();
        selectedCalendar = rulePackage.getCalendar();
        if (selectedCalendar != null)
            selectedCalendarIdTemp = String.valueOf(selectedCalendar.getId());
        editable = "true";
    }

    public void remove() {
        currentRule = ruleListTemp.getRowData();
        ruleArrayList.remove(currentRule);
        ruleListTemp = new ListDataModel<>(ruleArrayList);
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
            ruleListTemp = new ListDataModel<>(ruleArrayList);
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

    public void doEditPersonRule() {
        RulePackage newRulePackage = new RulePackage();
        newRulePackage.setStatus("c");
        newRulePackage.setDeleted("0");
        newRulePackage.setEffectorUser(me.getUsername());
        newRulePackage.setName(name + "_" + currentPerson.getPersonnelNo());
        newRulePackage.setAllowExit(ruleAllowExit);
        newRulePackage.setAniPassBack(ruleAniPassBack);
        newRulePackage.setAllowExitGadget(ruleAllowExitGadget);
        newRulePackage.setCalendar(calendarService.findById(selectedCalendarIdTemp));

        RulePackage addedRulePackage = null;
        addedRulePackage = rulePackageService.createRulePackage(newRulePackage);
        if (addedRulePackage != null) {
            for (Rule rule : ruleArrayList) {
                rule.setRulePackage(addedRulePackage);
                ruleService.createRule(rule);
            }

            currentPerson.setRulePackage(addedRulePackage);
            boolean condition = personService.editPerson(currentPerson);
            if (condition) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/person/persons.xhtml");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }
    }


    public void doAssignRule() {
        String currentDate = LangUtil.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd"));
        String currentTime = CalendarUtil.getTime(new Date(), new Locale("fa"));
        currentPerson.setCreateDate(currentDate);
        currentPerson.setCreateTime(currentTime);
        currentPerson.setStatus("c");
        currentPerson.setCreateBy(me.getUsername());
        currentPerson.setDeleted("0");
        currentPerson.setEffectorUser(me.getUsername());
        currentPerson.setRulePackage(selectedRulePackage);
        boolean condition = personService.editPerson(currentPerson);
//        if (condition) {
//            refresh();
//            me.addInfoMessage("operation.occurred");
//            me.redirect("/person/persons.xhtml");
//        } else {
//            me.addInfoMessage("operation.not.occurred");
//            return;
//        }
    }

    public void personDetail() {
//        workGroupRoleList = new DataModel<>(new ArrayList<>(currentPerson.getWorkGroups().getRoles()));
//        enabled = currentPerson.getEnable() == "true" ? true : false;
        personname = currentPerson.getName();
        List<Person> persons = null;//me.getGeneralHelper().getPersonService().getAllPending(currentPerson);


    }

    public void selectNewRuleForPerson(String id) {
        selectedRulePackage = rulePackageService.findById(id);
        rulePackageName = selectedRulePackage.getName();
        if (selectedRulePackage.getCalendar() != null)
            calendarName = selectedRulePackage.getCalendar().getName();
        else
            calendarName = "";
        antiPassBack = selectedRulePackage.isAniPassBack();
        allowExit = selectedRulePackage.isAllowExit();
        allowExitGadget = selectedRulePackage.isAllowExitGadget();
    }

    public void selectOrgan() {
        selectedOrganName = selectedOrgan.getName();
    }

    public void sortByPersonname() {
        personnameOrder = newSortOrder(personnameOrder);
    }

    public void sortByPersonFamily() {
        personFamilyOrder = newSortOrder(personFamilyOrder);
    }

    public void sortByPersonnelNo() {
        personnelNoOrder = newSortOrder(personnelNoOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        personnameOrder = SortOrder.UNSORTED;
        personFamilyOrder = SortOrder.UNSORTED;
        personnelNoOrder = SortOrder.UNSORTED;

        if (currentSortOrder.equals(SortOrder.DESCENDING)) {
            return SortOrder.ASCENDING;
        } else {
            return SortOrder.DESCENDING;
        }

    }

    private OutputStream stream;
    private FacesContext fc;
    private int i = 0;

    public void paint(OutputStream stream, Object object) throws IOException, URISyntaxException {

        Long personId = (Long) object;
        Person person = null;

        person = personService.findById(personId);

        if (null != person.getPicture()) {
            stream.write(person.getPicture());
            stream.flush();
        } else {
            stream.write(generalHelper.getAnonymous());
            stream.flush();
        }
        stream.close();
    }

    public void painter(OutputStream stream, Object object) throws IOException {
        if (picture != null && picture.length != 0) {
            stream.write(picture);
        } else {
            stream.write(generalHelper.getAnonymous());
        }
        stream.flush();
        stream.close();
    }

    public void unsetPicture() {
        picture = new byte[0];
        currentPerson.setPicture(new byte[0]);
    }
/*

    public Filter<?> getPersonnameFilterImpl() {
        return new Filter<Person>() {
            public boolean accept(Person person) {
                return personnameFilter == null || personnameFilter.length() == 0 || person.getName().toLowerCase().contains(personnameFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getPersonFamilyFilterImpl() {
        return new Filter<Person>() {
            public boolean accept(Person person) {
                return personFamilyFilter == null || personFamilyFilter.length() == 0 || person.getLastName().toLowerCase().contains(personFamilyFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getPersonnelNoFilterImpl() {
        return new Filter<Person>() {
            public boolean accept(Person person) {
                return StringUtils.isEmpty(personnelNoFilter) || person.getPersonnelNo().contains(personnelNoFilter);
            }
        };
    }

*/

    public SortOrder getPersonFamilyOrder() {
        return personFamilyOrder;
    }

    public void setPersonFamilyOrder(SortOrder personFamilyOrder) {
        this.personFamilyOrder = personFamilyOrder;
    }

    public String getPersonnelNo() {
        return personnelNo;
    }

    public void setPersonnelNo(String personnelNo) {
        this.personnelNo = personnelNo;
    }

    public String getWorkStation() {
        return workStation;
    }

    public void setWorkStation(String workStation) {
        this.workStation = workStation;
    }

    public List<Long> getPersonList() {
        return personListID;
    }

    public void setPersonList(List<Long> personListID) {
        this.personListID = personListID;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public Person getNewPerson() {
        return newPerson;
    }

    public void setNewPerson(Person newPerson) {
        this.newPerson = newPerson;
    }

    public SortOrder getPersonnameOrder() {
        return personnameOrder;
    }

    public void setPersonnameOrder(SortOrder personnameOrder) {
        this.personnameOrder = personnameOrder;
    }

    public String getPersonnameFilter() {
        return personnameFilter;
    }

    public void setPersonnameFilter(String personnameFilter) {
        this.personnameFilter = personnameFilter;
    }

    public String getPersonFamilyFilter() {
        return personFamilyFilter;
    }

    public void setPersonFamilyFilter(String personFamilyFilter) {
        this.personFamilyFilter = personFamilyFilter;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCardData() {
        return cardData;
    }

    public void setCardData(String cardData) {
        this.cardData = cardData;
    }

    public boolean isCitySelected() {
        return citySelected;
    }

    public void setCitySelected(boolean citySelected) {
        this.citySelected = citySelected;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Long> getInnerPersonList() {
        return innerPersonList;
    }

    public void setInnerPersonList(List<Long> innerPersonList) {
        this.innerPersonList = innerPersonList;
    }
    /*public void personEnable(ValueChangeEvent event) {
        boolean status = (Boolean) event.getNewValue();
        if (!status) {
            currentPerson.setEnable("false");
        } else
            currentPerson.setEnable("true");

    }*/

    public void personEnableChange(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            enabled = true;
        } else
            enabled = false;

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
        setCurrentPerson(null);
        setSelectRow(false);
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

    public String getPersonNo() {
        return personNo;
    }

    public void setPersonNo(String personNo) {
        this.personNo = personNo;
    }

    public SelectItem[] getOrganItem() {
        List<Organ> organs = organService.getAllOrgan();
        organItem = new SelectItem[organs.size()];
        for (int i = 0; i < organs.size(); i++) {
            organItem[i] = new SelectItem(organs.get(i).getId(), organs.get(i).getName());
        }
        return organItem;
    }

    public void setOrganItem(SelectItem[] organItem) {
        this.organItem = organItem;
    }

    public RulePackage getSelectedRulePackage() {
        return selectedRulePackage;
    }

    public void setSelectedRulePackage(RulePackage selectedRulePackage) {
        this.selectedRulePackage = selectedRulePackage;
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

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

/*    public void listener(FileUploadEvent event) throws Exception {
        UploadedFile item =null*//*= event.getUploadedFile()*//*;
//        BufferedImage sourceBufferedImage = ImageUtils.convertByteArrayToBufferedImage(item.getData());
//        float scaleRatio = ImageUtils.calculateScaleRatio(sourceBufferedImage.getWidth(), 200);//todo from properties
//        if (scaleRatio > 0 && scaleRatio < 1) {
//            try {
//                sourceBufferedImage = ImageUtils.scaleImage(sourceBufferedImage, scaleRatio);
//            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
        setPicture(ImageUtils.imageToByteArray(sourceBufferedImage));
        havePicture = true;
    }*/

    public long getTime() {
        return System.currentTimeMillis();
    }

    public void downloadJnlp(String personId) throws IOException {
        currentPerson = personService.findById(Long.parseLong(personId));
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        String base = "http://" + Configuration.getProperty("server.ip") + ":" + Configuration.getProperty("server.port") + ((HttpServletRequest) ec.getRequest()).getServletContext().getContextPath() + "/jnlp";
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<jnlp spec=\"1.0+\" codebase=\"" + base + "\" href=\"testAction\">\n" +
                "    <information>\n" +
                "        <title>Jnlp Testing</title>\n" +
                "        <vendor>Aria</vendor>\n" +
                "        <homepage href=\"" + base + "/\"/>\n" +
                "        <description>Testing Testing</description>\n" +
                "    </information>\n" +
                "    <security>\n" +
                "        <all-permissions/>\n" +
                "    </security>\n" +
                "    <resources>\n" +
                "        <j2se version=\"1.6+\"/>\n" +
                "        <jar href=\"TestJnlp.jar\"/>\n" +
                "    </resources>\n" +
                "    <application-desc main-class=\"ir.university.toosi.wtms.jnlp.CardReader\">\n" +
                "        <argument>" + ((HttpServletRequest) ec.getRequest()).getLocalAddr() + "</argument>\n" +
                "        <argument>" + ((HttpServletRequest) ec.getRequest()).getLocalPort() + "</argument>\n" +
                "        <argument>" + ((HttpServletRequest) ec.getRequest()).getServletContext().getContextPath() + "</argument>\n" +
                "        <argument>" + currentPerson.getId() + "</argument>\n" +
                "        <argument>" + Configuration.getProperty("com.port") + "</argument>\n" +
                "    </application-desc>\n" +
                "</jnlp>";
        byte[] xmlBytes = xml.getBytes("UTF-8");

        ec.responseReset();
        ec.setResponseContentType("application/x-java-jnlp-file");
        ec.setResponseContentLength(xmlBytes.length);
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"test.jnlp\"");

        OutputStream output = ec.getResponseOutputStream();
        output.write(xmlBytes);
        output.flush();
        output.close();
        fc.responseComplete();
    }

    public void deletePicture() {
        picture = new byte[0];
        havePicture = false;
    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public Organ getSelectedOrgan() {
        return selectedOrgan;
    }

    public void setSelectedOrgan(Organ selectedOrgan) {
        this.selectedOrgan = selectedOrgan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public DataModel<Rule> getRuleListTemp() {
        return ruleListTemp;
    }

    public void setRuleListTemp(DataModel<Rule> ruleListTemp) {
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

    public List<Calendar> getCalendarItems() {
        return calendarItems;
    }

    public void setCalendarItems(List<Calendar> calendarItems) {
        this.calendarItems = calendarItems;
    }

    public Hashtable<String, DayType> getDayTypeHashtable() {
        return dayTypeHashtable;
    }

    public void setDayTypeHashtable(Hashtable<String, DayType> dayTypeHashtable) {
        this.dayTypeHashtable = dayTypeHashtable;
    }

    public int getPageInPopup() {
        return pageInPopup;
    }

    public void setPageInPopup(int pageInPopup) {
        this.pageInPopup = pageInPopup;
    }

    public String getFinger() {
        return finger;
    }

    public void setFinger(String finger) {
        this.finger = finger;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getEmployNo() {
        return employNo;
    }

    public void setEmployNo(String employNo) {
        this.employNo = employNo;
    }

    public BLookup getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(BLookup employeeType) {
        this.employeeType = employeeType;
    }

    public List<BLookup> getEmployeeTypes() {
        if (employeeTypes.size() == 0) {
            employeeTypes = bLookupService.getByLookupId(Lookup.EMPLOYEE_TYPE_ID);
            for (BLookup bLookup : employeeTypes) {
                bLookup.setTitleText(bLookup.getCode());
            }
        }
        return employeeTypes;
    }

    public void setEmployeeTypes(List<BLookup> employeeTypes) {
        this.employeeTypes = employeeTypes;
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

    public BLookup getAssistType() {
        return assistType;
    }

    public void setAssistType(BLookup assistType) {
        this.assistType = assistType;
    }

    public List<BLookup> getAssistTypes() {
        if (assistTypes.size() == 0) {
            assistTypes = bLookupService.getByLookupId(Lookup.ASSIST_TYPE_ID);
            for (BLookup bLookup : assistTypes) {
                bLookup.setTitleText(bLookup.getCode());
            }
        }
        return assistTypes;
    }

    public void setAssistTypes(List<BLookup> assistTypes) {
        this.assistTypes = assistTypes;
    }

    public BLookup getPostType() {

        return postType;
    }

    public void setPostType(BLookup postType) {
        this.postType = postType;
    }

    public List<BLookup> getPostTypes() {
        if (postTypes.size() == 0) {
            postTypes = bLookupService.getByLookupId(Lookup.POST_TYPE_ID);
            for (BLookup bLookup : postTypes) {
                bLookup.setTitleText(bLookup.getCode());
            }
        }
        return postTypes;
    }

    public void setPostTypes(List<BLookup> postTypes) {
        this.postTypes = postTypes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHavePicture() {
        return havePicture;
    }

    public void setHavePicture(boolean havePicture) {
        this.havePicture = havePicture;
    }

    public int getPage2() {
        return page2;
    }

    public void setPage2(int page2) {
        this.page2 = page2;
    }

    public Job getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(Job currentJob) {
        this.currentJob = currentJob;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getSelectedOrganName() {
        return selectedOrganName;
    }

    public void setSelectedOrganName(String selectedOrganName) {
        this.selectedOrganName = selectedOrganName;
    }

    public String getPreCondition() {
        return preCondition;
    }

    public void setPreCondition(String preCondition) {
        this.preCondition = preCondition;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getPostCondition() {
        return postCondition;
    }

    public void setPostCondition(String postCondition) {
        this.postCondition = postCondition;
    }

    public SelectItem[] getPreConditions() {
        return preConditions;
    }

    public void setPreConditions(SelectItem[] preConditions) {
        this.preConditions = preConditions;
    }

    public SelectItem[] getPostConditions() {
        return postConditions;
    }

    public void setPostConditions(SelectItem[] postConditions) {
        this.postConditions = postConditions;
    }

    public SelectItem[] getAttributeNames() {
        return attributeNames;
    }

    public void setAttributeNames(SelectItem[] attributeNames) {
        this.attributeNames = attributeNames;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public boolean isPersonAvailable() {
        return persons.size() != 0;
    }

    public boolean isExtraField1Enable() {
        return extraField1Enable;
    }

    public void setExtraField1Enable(boolean extraField1Enable) {
        this.extraField1Enable = extraField1Enable;
    }

    public boolean isExtraField2Enable() {
        return extraField2Enable;
    }

    public void setExtraField2Enable(boolean extraField2Enable) {
        this.extraField2Enable = extraField2Enable;
    }

    public boolean isExtraField3Enable() {
        return extraField3Enable;
    }

    public void setExtraField3Enable(boolean extraField3Enable) {
        this.extraField3Enable = extraField3Enable;
    }

    public boolean isExtraField4Enable() {
        return extraField4Enable;
    }

    public void setExtraField4Enable(boolean extraField4Enable) {
        this.extraField4Enable = extraField4Enable;
    }

    public List<PersonSearch> getPersonSearchList() {
        return personSearchList;
    }

    public void setPersonSearchList(List<PersonSearch> personSearchList) {
        this.personSearchList = personSearchList;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public SortOrder getPersonnelNoOrder() {
        return personnelNoOrder;
    }

    public void setPersonnelNoOrder(SortOrder personnelNoOrder) {
        this.personnelNoOrder = personnelNoOrder;
    }

    public String getPersonnelNoFilter() {
        return personnelNoFilter;
    }

    public void setPersonnelNoFilter(String personnelNoFilter) {
        this.personnelNoFilter = personnelNoFilter;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public String renderedRow(int value) {
        if (value >= 6)
            value = value % 6;
        return value < persons.size() ? "true" : "false";
    }

    public List<String> getPageCount() {
        return pageCount;
    }

    public void setPageCount(List<String> pageCount) {
        this.pageCount = pageCount;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public int getPageFrom() {
        return pageFrom;
    }

    public void setPageFrom(int pageFrom) {
        this.pageFrom = pageFrom;
    }

    public int getPageTo() {
        return pageTo;
    }

    public void setPageTo(int pageTo) {
        this.pageTo = pageTo;
    }

    public String getLastPageIndex() {
        return lastPageIndex;
    }

    public void setLastPageIndex(String lastPageIndex) {
        this.lastPageIndex = lastPageIndex;
    }

    public List<Long> getPersonListID() {
        return personListID;
    }

    public void setPersonListID(List<Long> personListID) {
        this.personListID = personListID;
    }

    public Person getDataModelPerson() {
        return dataModelPerson;
    }

    public void setDataModelPerson(Person dataModelPerson) {
        this.dataModelPerson = dataModelPerson;
    }

    public Person returnPerson(long id) {
        dataModelPerson = personService.findById(id);
        return dataModelPerson;

    }

    public String getSimpleValue() {
        return simpleValue;
    }

    public void setSimpleValue(String simpleValue) {
        this.simpleValue = simpleValue;
    }

    public StreamedContent getGraphicText() {
        return graphicText;
    }

    public void setGraphicText(StreamedContent graphicText) {
        this.graphicText = graphicText;
    }

    public List<PersonSearch> getPersonSearches() {
        return personSearches;
    }

    public void setPersonSearches(List<PersonSearch> personSearches) {
        this.personSearches = personSearches;
    }

    public List<Rule> getRuleArrayList() {
        return ruleArrayList;
    }

    public void setRuleArrayList(List<Rule> ruleArrayList) {
        this.ruleArrayList = ruleArrayList;
    }


}

