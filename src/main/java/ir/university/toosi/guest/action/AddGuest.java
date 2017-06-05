package ir.university.toosi.guest.action;

import com.google.common.collect.Lists;
import ir.university.toosi.guest.entity.Guest;
import ir.university.toosi.guest.entity.Log;
import ir.university.toosi.tms.model.entity.WorkGroup;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.util.GuestCameraUtil;
import ir.university.toosi.tms.util.JasperUtil;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.user.HandleUserAction;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.wtms.web.util.CalendarUtil;
import ir.university.toosi.wtms.web.util.LangUtil;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Created by M_Danapour on 05/26/2015.
 */
@Named(value = "addGuest")
@SessionScoped
public class AddGuest implements Serializable {
    @Inject
    protected GeneralHelper generalHelper;
    @Inject
    UserManagementAction me;
    @Inject
    HandleUserAction handleUserAction;
    private Guest guest;
    private Guest person;
    private DataModel<Guest> notAssignGuestList = new ListDataModel<>();
    private int GuestPage = 1;

    private SortOrder GuestnameOrder = SortOrder.UNSORTED;
    private SortOrder GuestFamilyOrder = SortOrder.UNSORTED;
    private SortOrder timeOrder = SortOrder.UNSORTED;

    private String GuestnameFilter;
    private String GuestFamilyFilter;
    private String GuestnelNoFilter;
    private String second;
    private String minute;
    private String hour;
    private DataModel<Card> cardList;
    private String cardnameOrder;
    private String cardnameFilter;
    private String cardCodeOrder;
    private String cardCodeFilter;
    Card selectedCard;
    private String cardPage;
    private String page;
    private String simpleValue;
    private String authenticateType;
    private String fromDate;
    private String toDate;
    private DataModel<Guest> guestList;
    private DataModel<Guest> followList;

    public void beginSearch() {
        init();
        fromDate = CalendarUtil.getPersianDateWithoutSlash(new Locale("fa"));
        toDate = CalendarUtil.getPersianDateWithoutSlash(new Locale("fa"));
        me.redirect("/guest/guest-log.xhtml");
    }

    public void search() {
        List<Log> innerTrafficLogList = null;

        List<Guest> logDataModels = null;
        try {
            logDataModels = me.getGeneralHelper().getGuestService().duration(fromDate, toDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (logDataModels == null)
            logDataModels = new ArrayList<>();
        guestList = new ListDataModel<>(Lists.reverse(logDataModels));
    }

    public void selectCard(ValueChangeEvent event) {
        selectedCard = cardList.getRowData();

    }

    public void begin() {
        init();
        me.redirect("/guest/addGuest.xhtml");
    }

    public void init() {
        page = "1";
        person = new Guest();
        setGuest(new Guest());
        for (WorkGroup workGroup : me.getUser().getWorkGroups()) {
            if (workGroup.getName().equalsIgnoreCase("EMPLOYEE")) {
                getGuest().setvName(me.getUser().getFirstname());
                getGuest().setvFamily(me.getUser().getLastname());
            }
        }
    }

    public void beginToday() {
        init();
        try {
            notAssignGuestList = new ListDataModel<>(generalHelper.getGuestService().todayGuest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        me.redirect("/guest/today-list.xhtml");

    }


    public void saveGuest() throws Exception {
        guest.setTime(hour + ":" + minute);
        generalHelper.getGuestService().create(guest);
        setGuest(new Guest());
        init();
        me.addInfoMessage("operation.occurred");
        me.redirect("/guest/addGuest.xhtml");
    }

    public void selectPersonWith() throws Exception {
        guest = notAssignGuestList.getRowData();
        person = new Guest();
    }

    public void savePersonWith() throws Exception {
        guest = generalHelper.getGuestService().getGuestDao().findById(guest.getId());
        person.setDate(guest.getDate());
        person.setTime(guest.getTime());
        person.setvFamily(guest.getvFamily());
        person.setvName(guest.getvName());
        person.setGuestSize(0);
        person = generalHelper.getGuestService().create(person);
        guest.getGuestSet().add(person);
        generalHelper.getGuestService().getGuestDao().update(guest);
        me.addInfoMessage("operation.occurred");
        beginToday();

    }

    public void assignParticipant() {
        person = new Guest();

    }

    public void showParticipant() {
        followList = new ListDataModel<>(new ArrayList(guest.getGuestSet()));
    }

    public void edit() {
        guest = getGeneralHelper().getGuestService().getGuestDao().findById(guest.getId());
        if(guest.getTime()!=null&& guest.getTime().contains(":"))
        hour = guest.getTime().split(":")[0];
        minute = guest.getTime().split(":")[1];
    }

    public void doEdit() throws Exception {
        guest.setTime(hour + ":" + minute);
        generalHelper.getGuestService().getGuestDao().update(guest);
        init();
    }

    public Guest getGuest() {
        if (guest == null)
            return new Guest();
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public GeneralHelper getGeneralHelper() {
        return generalHelper;
    }

    public void setGeneralHelper(GeneralHelper generalHelper) {
        this.generalHelper = generalHelper;
    }

    public DataModel<Guest> getNotAssignGuestList() {
        return notAssignGuestList;
    }

    public void setNotAssignGuestList(DataModel<Guest> notAssignGuestList) {
        this.notAssignGuestList = notAssignGuestList;
    }

    public int getGuestPage() {
        return GuestPage;
    }

    public void setGuestPage(int GuestPage) {
        this.GuestPage = GuestPage;
    }

    public SortOrder getGuestnameOrder() {
        return GuestnameOrder;
    }

    public void setGuestnameOrder(SortOrder GuestnameOrder) {
        this.GuestnameOrder = GuestnameOrder;
    }

    public SortOrder getGuestFamilyOrder() {
        return GuestFamilyOrder;
    }

    public void setGuestFamilyOrder(SortOrder GuestFamilyOrder) {
        this.GuestFamilyOrder = GuestFamilyOrder;
    }


    public String getGuestnameFilter() {
        return GuestnameFilter;
    }

    public void setGuestnameFilter(String GuestnameFilter) {
        this.GuestnameFilter = GuestnameFilter;
    }

    public String getGuestFamilyFilter() {
        return GuestFamilyFilter;
    }


    public void setGuestFamilyFilter(String GuestFamilyFilter) {
        this.GuestFamilyFilter = GuestFamilyFilter;
    }

    public String getGuestnelNoFilter() {
        return GuestnelNoFilter;
    }

    public void setGuestnelNoFilter(String guestnelNoFilter) {
        GuestnelNoFilter = guestnelNoFilter;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public DataModel<Card> getCardList() {
        return cardList;
    }

    public String getCardnameOrder() {
        return cardnameOrder;
    }

    public String getCardnameFilterImpl() {
        return null;
    }

    public String getSortBycardname() {
        return null;
    }

    public String getCardnameFilter() {
        return cardnameFilter;
    }

    public String getcardCodeOrder() {
        return cardCodeOrder;
    }

    public String getcardCodeFilterImpl() {
        return null;
    }

    public String getSortBycardCode() {
        return null;
    }

    public String getcardCodeFilter() {
        return cardCodeFilter;
    }

    public void setCardPage(String cardPage) {
        this.cardPage = cardPage;
    }

    public String getCardPage() {
        return cardPage;
    }

    public void doAssigncard() {
        guest = getGeneralHelper().getGuestService().getGuestDao().findById(guest.getId());
        guest.setHasCard(true);
        getGeneralHelper().getGuestService().getGuestDao().update(guest);
        selectedCard=me.getGeneralHelper().getCardService().findById(selectedCard.getId());
        selectedCard.setGuest(guest);
        selectedCard.setName(guest.getFirstname() + " " + guest.getLastname());
        long id=generalHelper.getCardService().editCard(selectedCard);
        Log log = new Log();
        log.setGuest(guest);
        log.setCard(selectedCard);
        log.setDate(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
        log.setTime(CalendarUtil.getTime(new Date(), new Locale("fa")));
        log.setType("ورودی");
        try {
            generalHelper.getLogService().create(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
        beginToday();


    }

    public void unAssigncard() {
//        guest=notAssignGuestList.getRowData();
        List<Card> cards = generalHelper.getCardService().findByGuestId(guest.getId());
        if (cards == null || cards.size() == 0)
            return;
        selectedCard = cards.get(0);
        selectedCard.setGuest(null);
        selectedCard.setName("");

        long id=generalHelper.getCardService().editCard(selectedCard);
        Log log = new Log();
        log.setGuest(guest);
        log.setCard(selectedCard);
        log.setDate(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
        log.setTime(CalendarUtil.getTime(new Date(), new Locale("fa")));
        log.setType("خروجی");
        guest = generalHelper.getGuestService().getGuestDao().findById(guest.getId());
        guest.setExitTime(CalendarUtil.getTimeWithoutDot(new Date(), new Locale("fa")));
        guest.setHasCard(false);
        getGeneralHelper().getGuestService().update(guest);

        me.addInfoMessage("کارت تخصیص داده شده حذف شد");
        try {
            generalHelper.getLogService().create(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
        beginToday();
    }

    public void takePicture() {
        guest.setPicture(GuestCameraUtil.capture());
        generalHelper.getGuestService().update(guest);

    }

    public void print() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", guest.getFirstname().replace("ی", "ي") + " " + guest.getLastname().replace("ی", "ي"));
        map.put("user", me.getUser().getFirstname().replace("ی", "ي") + " " + me.getUser().getLastname().replace("ی", "ي"));
        map.put("vname", guest.getvName().replace("ی", "ي") + " " + guest.getvFamily().replace("ی", "ي"));
        map.put("date", CalendarUtil.getPersianDateWithSlash(new Locale("fa")));
        map.put("vorgan", guest.getvOrgan().replace("ی", "ي"));
        map.put("time", LangUtil.getFarsiNumber(guest.getTime()));
        if(guest.getPicture()==null){
            map.put("picture", new ByteArrayInputStream(me.getGeneralHelper().getAnonymous()));

        }else {
            map.put("picture", new ByteArrayInputStream(guest.getPicture()));
        }
        map.put("sign", new ByteArrayInputStream(me.getUser().getUserSign()));
        JasperUtil.generatePDFWithoutDataSource("guest.jrxml", CalendarUtil.getPersianDateWithoutSlash(new Locale("fa"))+CalendarUtil.getTimeWithoutDot(new Date(),new Locale("fa"))+".pdf", map);
    }

    public StreamedContent getPic() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        String id = myRequest.getParameter("pic");
        if (id == null)
            return new DefaultStreamedContent(new ByteArrayInputStream(me.getGeneralHelper().getAnonymous()),"image/png");
        Guest guest = generalHelper.getGuestService().getGuestDao().findById(Long.parseLong(id));
        if (guest == null || guest.getPicture() == null)
            return new DefaultStreamedContent();
        else
            return new DefaultStreamedContent(new ByteArrayInputStream(guest.getPicture()), "image/jpeg");
    }

    public void assignCard() {
        if (guest.isHasCard()) {
            me.addInfoMessage("کارت قبلا تخصیص داده شده است");
            beginToday();
        }
        cardList = new ListDataModel<>(generalHelper.getCardService().getAllGuestActiveCard());
    }


    public void setSimpleValue(String simpleValue) {
        this.simpleValue = simpleValue;
    }

    public String getSimpleValue() {
        return simpleValue;
    }

    public void simpleSearch() {

        String query = "select p from Guest p where (";
        query += " p.firstname like \'%" + simpleValue + "%\' or " + " p.lastname like \'%" + simpleValue + "%\' )";
        init();
        notAssignGuestList = new ListDataModel<>(generalHelper.getGuestService().query(query));
    }

    public SortOrder getTimeOrder() {
        return timeOrder;
    }

    public void setTimeOrder(SortOrder timeOrder) {
        this.timeOrder = timeOrder;
    }

    public Guest getPerson() {
        return person;
    }

    public void setPerson(Guest person) {
        this.person = person;
    }

    public void setAuthenticateType(String authenticateType) {
        this.authenticateType = authenticateType;
    }

    public String getAuthenticateType() {
        return authenticateType;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public DataModel<Guest> getGuestList() {
        return guestList;
    }

    public void setGuestList(DataModel<Guest> guestList) {
        this.guestList = guestList;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public DataModel<Guest> getFollowList() {
        return followList;
    }

    public void setFollowList(DataModel<Guest> followList) {
        this.followList = followList;
    }
}
