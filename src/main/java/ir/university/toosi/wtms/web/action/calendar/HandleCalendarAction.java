package ir.university.toosi.wtms.web.action.calendar;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.service.calendar.CalendarDateServiceImpl;
import ir.university.toosi.tms.model.service.calendar.CalendarServiceImpl;
import ir.university.toosi.tms.model.service.calendar.DayTypeServiceImpl;
import ir.university.toosi.tms.model.service.rule.RulePackageServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.WebServiceInfo;
import ir.university.toosi.tms.model.entity.calendar.Calendar;
import ir.university.toosi.tms.model.entity.calendar.CalendarDate;
import ir.university.toosi.tms.model.entity.calendar.CalendarInfo;
import ir.university.toosi.tms.model.entity.calendar.DayType;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.SortOrder;

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

@Named(value = "handleCalendarAction")
@SessionScoped
public class HandleCalendarAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleRoleAction handleRoleAction;

    @EJB
    private CalendarServiceImpl calendarService;
    @EJB
    private DayTypeServiceImpl dayTypeService;
    @EJB
    private CalendarDateServiceImpl calendarDateService;
    @EJB
    private RulePackageServiceImpl rulePackageService;

    private List<Calendar> calendarList = null;
    private String editable = "false";
    private String name;
    private boolean calendarDefault;
    private String code = "#ffffff";
    private String description;
    private Calendar currentCalendar = null;
    private String currentPage;
    private String address = "/modena-1.0.1/restful/TMSService/getEditCalendar";
    private String content;
    private int page = 1;
    private boolean selected;
    private Set<Calendar> selectedCalendars = new HashSet<>();
    private boolean selectRow = false;
    private String calendarNameFilter;
    private String calendarDescriptionFilter;
    private SortOrder calendarNameOrder = SortOrder.UNSORTED;
    private SortOrder calendarDescriptionOrder = SortOrder.UNSORTED;


    public void begin() {
        me.setActiveMenu(MenuType.CALENDAR);
        refresh();
        me.redirect("/calendar/list-calendar.xhtml");
    }


    public List<Calendar> getSelectionGrid() {
        List<Calendar> calendars = new ArrayList<>();
        refresh();
        return calendarList;
    }

    private void refresh() {
        init();
        calendarList = calendarService.getAllCalendar();
    }

    public void add() {
        init();
        setEditable("false");
        me.redirect("/calendar/handle-calendar.xhtml");

    }

    public void copy() {
        Calendar calendar = new Calendar();
        calendar.setContent(currentCalendar.getContent());
        calendar.setDeleted(currentCalendar.getDeleted());
        calendar.setDescription(currentCalendar.getDescription());
        calendar.setName(currentCalendar.getName() + "_copy");
        calendar.setStatus("c");
        calendar.setEffectorUser(me.getUsername());
        Calendar insertedCalendar = null;
        List<CalendarDate> calendarDates = null;
        calendarDates = calendarDateService.findByCalendarID(currentCalendar.getId());

        insertedCalendar = calendarService.createCalendar(calendar);
        CalendarDate calendarDate = null;
        for (CalendarDate innerCalendarDate : calendarDates) {
            calendarDate = new CalendarDate();
            calendarDate.setEffectorUser(me.getUsername());
            calendarDate.setCalendar(insertedCalendar);
            calendarDate.setDayType(innerCalendarDate.getDayType());
            calendarDate.setEndDate(innerCalendarDate.getEndDate());
            calendarDate.setStartDate(innerCalendarDate.getStartDate());
            calendarDateService.createCalendarDate(calendarDate);
        }

        List<RulePackage> rulePackages = null;
        rulePackages = rulePackageService.findByCalendarID(currentCalendar.getId());

        RulePackage rulePackage = null;
        for (RulePackage innerRulePackage : rulePackages) {
            rulePackage = new RulePackage();
            rulePackage.setCalendar(calendar);
            rulePackage.setEffectorUser(me.getUsername());
            rulePackage.setName(innerRulePackage.getName() + "_copy");
            rulePackage.setAllowExit(innerRulePackage.isAllowExit());
            rulePackage.setAllowExitGadget(innerRulePackage.isAllowExitGadget());
            rulePackage.setAniPassBack(innerRulePackage.isAniPassBack());
            rulePackage.setEffectorUser(me.getUsername());
            rulePackageService.createRulePackage(rulePackage);
        }
        if (insertedCalendar != null) {
            refresh();
            me.addInfoMessage("operation.occurred");
        }
        me.redirect("/calendar/list-calendar.xhtml");


    }

    public void doDelete() {
        currentCalendar.setEffectorUser(me.getUsername());
        String condition = calendarService.deleteCalendar(currentCalendar);
        refresh();
        me.addInfoMessage(condition);
        me.redirect("/calendar/list-calendar.xhtml");
    }

    public void init() {
        name = "";
        calendarDefault = true;
        code = "";
        description = "";
        page = 1;
        calendarDescriptionFilter = "";
        calendarNameFilter = "";
        currentCalendar = null;
        setSelectRow(false);
    }

    public void edit() {
        setEditable("true");
        name = currentCalendar.getName();
        code = currentCalendar.getCode();
        description = currentCalendar.getDescription();
        me.redirect("/calendar/handle-edit-calendar.xhtml");

    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();
        }
    }

    private void doEdit() {


        Calendar newCalendar = new Calendar();
        newCalendar.setName(name);
        newCalendar.setDescription(description);
        newCalendar.setDeleted("0");
        newCalendar.setStatus("c");
        newCalendar.setEffectorUser(me.getUsername());
        newCalendar.setContent(content.getBytes());
        content = content.substring(content.indexOf("Initialize") + 14, content.length());
        content = "[[" + content;
        content = content.substring(0, content.indexOf("}]]"));
        content += "}]]";
        content = content.toLowerCase();
        content = content.substring(3, content.length() - 1);
        content = content.replace("[", " {\"day\":[");
        content = content.replace("]", " ]}");
        content = "{\"year\": [{\"month\": [{\"day\":[{" + content + "]}]}";
        CalendarInfo calendarInfo = null;
        try {
            calendarInfo = new ObjectMapper().readValue(content, CalendarInfo.class);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Calendar insertedCalendar = null;
        insertedCalendar = calendarService.createCalendar(newCalendar);
        CalendarInfo.Month[] month = calendarInfo.getYear()[0].getMonth();
        CalendarDate calendarDate;
        int lastType = -1;
        boolean flag = false;
        List<List<List<CalendarInfo.Day>>> yearMonth = new ArrayList<>();
        List<List<CalendarInfo.Day>> daysMonth = new ArrayList<>();
        List<CalendarInfo.Day> days = new ArrayList<>();
        for (int i = 0; i < month.length; i++) {
            if (daysMonth.size() != 0) {
//                daysMonth.add(days);
                days = new ArrayList<>();
            }
            daysMonth = new ArrayList<>();
            lastType = -1;
            for (int j = 0; j < month[i].getDay().length; j++) {
                if (month[i].getDay()[j].get_id() != null && Integer.valueOf(month[i].getDay()[j].get_id()) != lastType) {
                    lastType = Integer.valueOf(month[i].getDay()[j].get_id());
                    if (days.size() != 0)
                        daysMonth.add(days);
                    days = new ArrayList<>();
                    days.add(month[i].getDay()[j]);

                } else if (month[i].getDay()[j].get_id() != null) {
                    lastType = Integer.valueOf(month[i].getDay()[j].get_id());
                    days.add(month[i].getDay()[j]);
                }
            }
            daysMonth.add(days);
            yearMonth.add(daysMonth);
        }
        DayType dayType = null;
        for (int i = 1; i <= 12; i++) {
            for (int j = 1; j <= yearMonth.get(i - 1).size(); j++) {
                calendarDate = new CalendarDate();
                calendarDate.setCalendar(insertedCalendar);
                dayType = dayTypeService.findById(Integer.valueOf(yearMonth.get(i - 1).get(j - 1).get(0).get_id()) + 1);
                int start = 0;
                int end = 0;
                calendarDate.setDayType(dayType);
                if (i <= 6) {
                    start = Integer.valueOf(yearMonth.get(i - 1).get(j - 1).get(0).getDay()) + (i - 1) * 31;
                    end = (start + yearMonth.get(i - 1).get(j - 1).size()) - 1;
                } else {
                    start = Integer.valueOf(yearMonth.get(i - 1).get(j - 1).get(0).getDay()) + (i - 7) * 30 + 186;
                    end = (start + yearMonth.get(i - 1).get(j - 1).size()) - 1;
                }
                calendarDate.setStartDate(start);
                calendarDate.setEndDate(end);
                calendarDateService.createCalendarDate(calendarDate);
            }
        }

        if (insertedCalendar != null) {
            List<RulePackage> rulePackages = rulePackageService.findByCalendarID(insertedCalendar.getId());
            for (RulePackage rulePackage : rulePackages) {
                rulePackage.setCalendar(insertedCalendar);
                rulePackageService.editRulePackage(rulePackage);

            }

            String condition = calendarService.deleteCalendar(currentCalendar);
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/calendar/list-calendar.xhtml");
        } else

        {
            me.addInfoMessage("operation.not.occurred");
        }


    }

    private void doAdd() {
        try {
            Calendar newCalendar = new Calendar();
            newCalendar.setName(name);
            newCalendar.setDescription(description);
            newCalendar.setDeleted("0");
            newCalendar.setStatus("c");
            newCalendar.setEffectorUser(me.getUsername());
            newCalendar.setContent(content.getBytes());
            content = content.substring(content.indexOf("Initialize") + 14, content.length());
            content = "[[" + content;
            content = content.substring(0, content.indexOf("}]]"));
            content += "}]]";
            content = content.toLowerCase();
            content = content.substring(3, content.length() - 1);
            content = content.replace("[", " {\"day\":[");
            content = content.replace("]", " ]}");
            content = "{\"year\": [{\"month\": [{\"day\":[{" + content + "]}]}";
            CalendarInfo calendarInfo = null;
            try {
                calendarInfo = new ObjectMapper().readValue(content, CalendarInfo.class);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            Calendar insertedCalendar = calendarService.createCalendar(newCalendar);
            CalendarInfo.Month[] month = calendarInfo.getYear()[0].getMonth();
            CalendarDate calendarDate;
            int lastType = -1;
            boolean flag = false;
            List<List<List<CalendarInfo.Day>>> yearMonth = new ArrayList<>();
            List<List<CalendarInfo.Day>> daysMonth = new ArrayList<>();
            List<CalendarInfo.Day> days = new ArrayList<>();
            for (int i = 0; i < month.length; i++) {
                if (daysMonth.size() != 0) {
//                daysMonth.add(days);
                    days = new ArrayList<>();
                }
                daysMonth = new ArrayList<>();
                lastType = -1;
                for (int j = 0; j < month[i].getDay().length; j++) {
                    if (month[i].getDay()[j].get_id() != null && Integer.valueOf(month[i].getDay()[j].get_id()) != lastType) {
                        lastType = Integer.valueOf(month[i].getDay()[j].get_id());
                        if (days.size() != 0)
                            daysMonth.add(days);
                        days = new ArrayList<>();
                        days.add(month[i].getDay()[j]);

                    } else if (month[i].getDay()[j].get_id() != null) {
                        lastType = Integer.valueOf(month[i].getDay()[j].get_id());
                        days.add(month[i].getDay()[j]);
                    }
                }
                daysMonth.add(days);
                yearMonth.add(daysMonth);
            }
            DayType dayType = null;
            for (int i = 1; i <= 12; i++) {
                for (int j = 1; j <= yearMonth.get(i - 1).size(); j++) {
                    calendarDate = new CalendarDate();
                    calendarDate.setCalendar(insertedCalendar);
                    dayType = dayTypeService.findById(Long.valueOf(yearMonth.get(i - 1).get(j - 1).get(0).get_id()));
                    int start = 0;
                    int end = 0;
                    calendarDate.setDayType(dayType);
                    if (i <= 6) {
                        start = Integer.valueOf(yearMonth.get(i - 1).get(j - 1).get(0).getDay()) + (i - 1) * 31;
                        end = (start + yearMonth.get(i - 1).get(j - 1).size()) - 1;
                    } else {
                        start = Integer.valueOf(yearMonth.get(i - 1).get(j - 1).get(0).getDay()) + (i - 7) * 30 + 186;
                        end = (start + yearMonth.get(i - 1).get(j - 1).size()) - 1;
                    }
                    calendarDate.setStartDate(start);
                    calendarDate.setEndDate(end);
                    calendarDateService.createCalendarDate(calendarDate);
                }
            }

            if (insertedCalendar != null) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/calendar/list-calendar.xhtml");
            } else {
                me.addInfoMessage("operation.not.occurred");
            }
        } catch (Exception ex) {
            refresh();
            me.addInfoMessage("operation.not.occurred");
            me.redirect("/calendar/list-calendar.xhtml");
        }

    }

//    public Filter<?> getCalendarNameFilterImpl() {
//        return new Filter<Calendar>() {
//            public boolean accept(Calendar calendar) {
//                return calendarNameFilter == null || calendarNameFilter.length() == 0 || calendar.getName().toLowerCase().contains(calendarNameFilter.toLowerCase());
//            }
//        };
//    }

    public void sortByCalendarName() {
        calendarDescriptionOrder = SortOrder.UNSORTED;

        if (calendarNameOrder.equals(SortOrder.ASCENDING)) {
            setCalendarNameOrder(SortOrder.DESCENDING);
        } else {
            setCalendarNameOrder(SortOrder.ASCENDING);
        }
    }

    public void sortByCalendarDescription() {
        calendarNameOrder = SortOrder.UNSORTED;

        if (calendarDescriptionOrder.equals(SortOrder.ASCENDING)) {
            setCalendarDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setCalendarDescriptionOrder(SortOrder.ASCENDING);
        }
    }
//
//    public Filter<?> getCalendarDescriptionFilterImpl() {
//        return new Filter<Calendar>() {
//            public boolean accept(Calendar calendar) {
//                return calendarDescriptionFilter == null || calendarDescriptionFilter.length() == 0 || calendar.getDescription().toLowerCase().contains(calendarDescriptionFilter.toLowerCase());
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

    public List<Calendar> getCalendarList() {
        return calendarList;
    }

    public void setCalendarList(List<Calendar> calendarList) {
        this.calendarList = calendarList;
    }

    public String getCalendarDescriptionFilter() {
        return calendarDescriptionFilter;
    }

    public void setCalendarDescriptionFilter(String calendarDescriptionFilter) {
        this.calendarDescriptionFilter = calendarDescriptionFilter;
    }

    public SortOrder getCalendarDescriptionOrder() {
        return calendarDescriptionOrder;
    }

    public void setCalendarDescriptionOrder(SortOrder calendarDescriptionOrder) {
        this.calendarDescriptionOrder = calendarDescriptionOrder;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getCalendarName() {
        return name;
    }

    public void setCalendarName(String calendarName) {
        this.name = calendarName;
    }

    public boolean isCalendarEnabled() {
        return calendarDefault;
    }

    public void setCalendarEnabled(boolean calendarDefault) {
        this.calendarDefault = calendarDefault;
    }


    public Set<Calendar> getSelectedCalendars() {
        return selectedCalendars;
    }

    public void setSelectedCalendars(Set<Calendar> selectedCalendars) {
        this.selectedCalendars = selectedCalendars;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        currentCalendar = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getColor() {
        return code;
    }

    public void setColor(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SortOrder getCalendarNameOrder() {
        return calendarNameOrder;
    }

    public void setCalendarNameOrder(SortOrder calendarNameOrder) {
        this.calendarNameOrder = calendarNameOrder;
    }

    public String getCalendarNameFilter() {
        return calendarNameFilter;
    }

    public void setCalendarNameFilter(String calendarNameFilter) {
        this.calendarNameFilter = calendarNameFilter;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Calendar getCurrentCalendar() {
        return currentCalendar;
    }

    public void setCurrentCalendar(Calendar currentCalendar) {
        this.currentCalendar = currentCalendar;
    }
}