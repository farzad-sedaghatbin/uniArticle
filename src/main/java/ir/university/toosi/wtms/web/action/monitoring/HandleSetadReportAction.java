package ir.university.toosi.wtms.web.action.monitoring;


import ir.ReaderWrapperService;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.SentryDataModel;
import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.entity.personnel.Organ;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.zone.Virdi;
import ir.university.toosi.tms.model.service.CommentServiceImpl;
import ir.university.toosi.tms.model.service.TrafficLogServiceImpl;
import ir.university.toosi.tms.model.service.personnel.OrganServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
import ir.university.toosi.tms.model.service.zone.PDPServiceImpl;
import ir.university.toosi.tms.model.service.zone.VirdiServiceImpl;
import ir.university.toosi.wtms.web.action.HandleCommentAction;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.person.HandlePersonAction;
import ir.university.toosi.wtms.web.helper.GeneralHelper;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

//import org.richfaces.application.push.TopicKey;
//import org.richfaces.application.push.TopicsContext;
//import org.richfaces.cdi.push.Push;

@Named(value = "handleSetadReportAction")
@SessionScoped
public class HandleSetadReportAction implements Serializable {

    @Inject
    private UserManagementAction me;

    @Inject
    private GeneralHelper generalHelper;
    @Inject
    private HandleCommentAction handleCommentAction;
    @Inject
    private HandlePersonAction handlePersonAction;

    @EJB
    private ReaderWrapperService readerWrapperService;
    @EJB
    private PersonServiceImpl personService;
    @EJB
    private GatewayServiceImpl gatewayService;
    @EJB
    private OrganServiceImpl organService;
    @EJB
    private PDPServiceImpl pdpService;
    @EJB
    private VirdiServiceImpl virdiService;
    @EJB
    private CommentServiceImpl commentService;
    @EJB
    private TrafficLogServiceImpl logService;

    private String message;
    private String fromDate;
    private String toDate;
    private String width;
    private boolean person;
    private boolean personAndGate;
    private boolean personAndTime;
    private int personPage;
    private int page;
    private List<TrafficLog> eventLogList = null;
    private List<Integer> loops = new ArrayList<>();
    private List<List<DataModel<SentryDataModel>>> trafficLogs = null;
    private SentryDataModel currentSentryDataModel;
    private TrafficLog currentTrafficLog;
    private boolean validForComment;


    private List<Person> personList = null;
    private SelectItem[] gatewayItems;
    private SelectItem[] oranItems;
    private String gatewayId;
    private String organId;
    private String startTime;
    private String endTime;
    private String startHour;
    private String startMinute;
    private String startSecond;
    private String endHour;
    private String endMinute;
    private String endSecond;
    private static Hashtable<Long, LinkedList<SentryDataModel>> sentries = new Hashtable<>();
    private static volatile List<List<SentryDataModel>> trafficLogsbygate = new ArrayList<>();
    private volatile List<List<SentryDataModel>> cachedTrafficLogsbygate = new ArrayList<>();
    private long sentryCount;
    private Person currentPerson;


    public void trackByPerson() {
        handlePersonAction.init();
        me.setActiveMenu(MenuType.REPORT);
        personPage = 1;
        person = true;
        personAndGate = false;
        personAndTime = false;
        personList = personService.getAllPersonModel();
        me.redirect("/setad/setad-report.xhtml");

    }


    public void dotrackByPerson() {

        eventLogList = logService.findByPersonInDuration(currentPerson.getId(), fromDate, toDate);
        me.redirect("/setad/report.xhtml");
    }

    public void dotrackByGate() {

        eventLogList = logService.findByVirdiInDuration(Long.valueOf(gatewayId), fromDate, toDate);
        me.redirect("/setad/report.xhtml");
    }

    public void dotrackByOrgan() {

        eventLogList = logService.findByOrganInDuration(Long.valueOf(organId), fromDate, toDate);
        me.redirect("/setad/report.xhtml");
    }


    public void trackByGate() {
        handlePersonAction.init();
        me.setActiveMenu(MenuType.REPORT);
        person = false;
        personAndGate = true;
        personAndTime = false;
        personPage = 1;


        List<Virdi> virdis = virdiService.getAllVirdis();
        gatewayItems = new SelectItem[virdis.size()];
        int i = 0;
        for (Virdi gateway1 : virdis) {
            gatewayItems[i++] = new SelectItem(gateway1.getId(), gateway1.getName());
        }
        if (gatewayItems != null)
            gatewayId = gatewayItems[0].getValue().toString();
        me.redirect("/setad/setad-report.xhtml");
    }

    public void trackByOrgan() {
        handlePersonAction.init();
        me.setActiveMenu(MenuType.REPORT);
        person = false;
        personAndGate = false;
        personAndTime = true;
        personPage = 1;


        List<Organ> organs = organService.getAllOrgan();
        oranItems = new SelectItem[organs.size()];
        int i = 0;
        for (Organ organ : organs) {
            oranItems[i++] = new SelectItem(organ.getId(), organ.getName());
        }
        if (oranItems != null)
            organId = oranItems[0].getValue().toString();
        me.redirect("/setad/setad-report.xhtml");
    }


    public long time2long(String time) {
        String[] d = time.split(":");
        String s = (d[0].length() == 2 ? d[0] : '0' + d[0]) + (d[1].length() == 2 ? d[1] : '0' + d[1]) + (d[2].length() == 2 ? d[2] : '0' + d[2]);
        return Long.valueOf(s);
    }

    public long time2Minute(String time) {
        String[] d = time.split(":");
        long min = Long.valueOf(d[0]) * 60 + Long.valueOf(d[1]);
        return min;
    }

    public void resetPersonPage() {
        setPersonPage(1);
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Hashtable<Long, LinkedList<SentryDataModel>> getSentries() {
        return sentries;
    }

    public void setSentries(Hashtable<Long, LinkedList<SentryDataModel>> sentries) {
        this.sentries = sentries;
    }

    public boolean isPerson() {
        return person;
    }

    public void setPerson(boolean person) {
        this.person = person;
    }

    public boolean isPersonAndGate() {
        return personAndGate;
    }

    public void setPersonAndGate(boolean personAndGate) {
        this.personAndGate = personAndGate;
    }

    public boolean isPersonAndTime() {
        return personAndTime;
    }

    public void setPersonAndTime(boolean personAndTime) {
        this.personAndTime = personAndTime;
    }


    public int getPersonPage() {
        return personPage;
    }

    public void setPersonPage(int personPage) {
        this.personPage = personPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public SelectItem[] getGatewayItems() {
        return gatewayItems;
    }

    public void setGatewayItems(SelectItem[] gatewayItems) {
        this.gatewayItems = gatewayItems;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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


    public long getSentryCount() {
        return sentryCount;
    }

    public void setSentryCount(long sentryCount) {
        this.sentryCount = sentryCount;
    }

    public SentryDataModel getCurrentSentryDataModel() {
        return currentSentryDataModel;
    }

    public void setCurrentSentryDataModel(SentryDataModel currentSentryDataModel) {
        this.currentSentryDataModel = currentSentryDataModel;
    }

    public TrafficLog getCurrentTrafficLog() {
        return currentTrafficLog;
    }

    public void setCurrentTrafficLog(TrafficLog currentTrafficLog) {
        this.currentTrafficLog = currentTrafficLog;
    }

    public String getWidth() {

        return (100 / trafficLogsbygate.size()) + "%";
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public boolean isValidForComment() {
        return validForComment;
    }

    public void setValidForComment(boolean validForComment) {
        this.validForComment = validForComment;
    }

    public List<TrafficLog> getEventLogList() {
        return eventLogList;
    }

    public void setEventLogList(List<TrafficLog> eventLogList) {
        this.eventLogList = eventLogList;
    }

    public List<List<DataModel<SentryDataModel>>> getTrafficLogs() {
        return trafficLogs;
    }

    public void setTrafficLogs(List<List<DataModel<SentryDataModel>>> trafficLogs) {
        this.trafficLogs = trafficLogs;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public List<List<SentryDataModel>> getTrafficLogsbygate() {
        if (trafficLogsbygate.size() == 0) {
            trafficLogsbygate = cachedTrafficLogsbygate;
        }
        return trafficLogsbygate;
    }

    public void setTrafficLogsbygate(List<List<SentryDataModel>> trafficLogsbygate) {
        this.trafficLogsbygate = trafficLogsbygate;
    }

    public List<Integer> getLoops() {
        return loops;
    }

    public void setLoops(List<Integer> loops) {
        this.loops = loops;
    }

    public List<List<SentryDataModel>> getCachedTrafficLogsbygate() {
        return cachedTrafficLogsbygate;
    }

    public void setCachedTrafficLogsbygate(List<List<SentryDataModel>> cachedTrafficLogsbygate) {
        this.cachedTrafficLogsbygate = cachedTrafficLogsbygate;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public SelectItem[] getOranItems() {
        return oranItems;
    }

    public void setOranItems(SelectItem[] oranItems) {
        this.oranItems = oranItems;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
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
}
