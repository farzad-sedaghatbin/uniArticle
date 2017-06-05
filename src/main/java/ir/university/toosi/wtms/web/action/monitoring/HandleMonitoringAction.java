package ir.university.toosi.wtms.web.action.monitoring;


import ir.ReaderWrapperService;
import ir.university.toosi.parking.entity.ParkingLog;
import ir.university.toosi.parking.service.CarServiceImpl;
import ir.university.toosi.parking.service.ParkingLogServiceImpl;
import ir.university.toosi.tms.model.entity.*;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.Virdi;
import ir.university.toosi.tms.model.service.CommentServiceImpl;
import ir.university.toosi.tms.model.service.TrafficLogServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
import ir.university.toosi.tms.model.service.zone.PDPServiceImpl;
import ir.university.toosi.tms.model.service.zone.VirdiServiceImpl;
import ir.university.toosi.wtms.web.action.HandleCommentAction;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.person.HandlePersonAction;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.wtms.web.util.CalendarUtil;
import ir.university.toosi.wtms.web.util.LangUtil;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;

//import org.richfaces.application.push.TopicKey;
//import org.richfaces.application.push.TopicsContext;
//import org.richfaces.cdi.push.Push;

@Named(value = "handleMonitoringAction")
@SessionScoped
public class HandleMonitoringAction implements Serializable {

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
    private PDPServiceImpl pdpService;
    @EJB
    private VirdiServiceImpl virdiService;
    @EJB
    private CommentServiceImpl commentService;
    @EJB
    private TrafficLogServiceImpl logService;
    @EJB
    private ParkingLogServiceImpl parkingLogService;
    @EJB
    private CarServiceImpl carService;

    private String message;
    private String width;
    private boolean person;
    private boolean personAndGate;
    private boolean personAndTime;
    private int personPage;
    private int page;
    private List<TrafficLog> eventLogList = null;
    private List<ParkingLog> parkingLogList = null;
    private List<Integer> loops = new ArrayList<>();
    private List<List<DataModel<SentryDataModel>>> trafficLogs = null;
    private SentryDataModel currentSentryDataModel;
    private TrafficLog currentTrafficLog;
    private boolean validForComment;


    private List<Person> personList = null;
    private SelectItem[] gatewayItems;
    private String gatewayId;
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

    @PostConstruct
    public void
    init() {
        try {
            sentryCount = Long.valueOf(me.SENTRY_COUNT);

        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize topics", e);
        }
    }


    public void sendMessage(TrafficLog log) {
        if (log == null || log.getGateway() == null || log.getPerson() == null)
            return;

        LinkedList<SentryDataModel> sentryDataModels = sentries.get(log.getVirdi().getId());
        if (sentryDataModels != null) {
            SentryDataModel dataModel = new SentryDataModel();
            dataModel.setVideo(log.getVideo());
            dataModel.setId(log.getId());
            dataModel.setTime(log.getTime());
            dataModel.setDate(log.getDate());
            dataModel.setExit(log.isExit());
            dataModel.setValid(log.isValid());
            dataModel.setGate(log.getGateway().getName());
            dataModel.setPdpName(log.getVirdi().getName());
            dataModel.setPersonId(log.getPerson().getId());
            if(log.getPerson().getName()==null || log.getPerson().getName().length()<=1){
                if(log.getCard()!=null&&log.getGuest()!=null) {
                    dataModel.setName(log.getGuest().getFirstname() + "  " + log.getGuest().getLastname());
                }else{
                    dataModel.setName("مهمان ثبت نشده");
                }
            }else{
                dataModel.setName(log.getPerson().getName() + "  " + log.getPerson().getLastName());
            }

            sentryDataModels.addFirst(dataModel);
            sentries.put(log.getVirdi().getId(), sentryDataModels);
            trafficLogsbygate = new ArrayList<>();
            loops = new ArrayList<>();
            int i = 0;
            for (Queue<SentryDataModel> dataModels : sentries.values()) {
                trafficLogsbygate.add(new ArrayList<>(dataModels));
                loops.add(i++);
            }

        }
        cachedTrafficLogsbygate = new ArrayList<>(trafficLogsbygate);

//        RequestContext.getCurrentInstance().update("trafficLogList:test");
//            me.redirect("/monitoring/sentry-monitor.xhtml");
//
        EventBusFactory eventBusFactory = EventBusFactory.getDefault();
        if (eventBusFactory != null) {
            EventBus eventBus = eventBusFactory.eventBus();
            eventBus.publish("/notify", new Boolean(true));
        }
    }

    public void sendMessage(ParkingLog log, Boolean t) {
        if (log == null || log.getNumber() == null)
            return;
        if (parkingLogList == null) {
            parkingLogList = new ArrayList<>();
        }
        parkingLogList.add(log);

        List carList = carService.findByNumber(log.getNumber());
        if (carList != null && !carList.isEmpty()) log.setHasCar(true);

        EventBusFactory eventBusFactory = EventBusFactory.getDefault();
        if (eventBusFactory != null) {
            EventBus eventBus = eventBusFactory.eventBus();
            eventBus.publish("/notifyParking", new Boolean(true));
        }

    }

    public void forceOpen(List<SentryDataModel> gate) {
        if (gate == null || gate.size() == 0)
            return;
        String logId = String.valueOf(gate.iterator().next().getId());
        try {
            virdiService.forceOpen(logService.findById(Long.parseLong(logId)).getVirdi().getTerminalId());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    public void unLockDoor(List<SentryDataModel> gate) {
        if (gate == null || gate.size() == 0)
            return;
        String logId = String.valueOf(gate.iterator().next().getId());
        try {
            virdiService.unLockDoor(logService.findById(Long.parseLong(logId)).getVirdi().getTerminalId());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void lockDoor(List<SentryDataModel> gate) {
        if (gate == null || gate.size() == 0)
            return;
        String logId = String.valueOf(gate.iterator().next().getId());
        try {
            virdiService.lockDoor(logService.findById(Long.parseLong(logId)).getVirdi().getTerminalId());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    private void initialize() {
        readerWrapperService.setMonitoringAction(this);

        page = 1;
        trafficLogsbygate = new ArrayList<>();
        LinkedList<SentryDataModel> trafficLogList;
        List<Gateway> notAccess = new ArrayList<>();
        List<Long> pdpID = new ArrayList<>();
//        for (WorkGroup workGroup : me.getUser().getWorkGroups()) {
//            for (Role role : workGroup.getRoles()) {
//                for (Permission permission : role.getPermissions()) {
//                    if (permission.getPermissionType().equals(PermissionType.PDP)) {
//                        pdpID.add(Long.valueOf(permission.getObjectId()));
//                    }
//                }
//            }
//        }

        List<Virdi> virdis = virdiService.getAllVirdis();
        for (Virdi virdi : virdis) {
            List<TrafficLog> traffic = logService.findByVirdi(virdi.getId(), ir.university.toosi.tms.util.LangUtil.getEnglishNumber(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa"))));
            SentryDataModel dataModel;
            trafficLogList = new LinkedList<SentryDataModel>() {
                @Override
                public boolean add(SentryDataModel sentryDataModel) {
                    if (size() == sentryCount)
                        remove();
                    return super.add(sentryDataModel);
                }
            };
            sentries.put(virdi.getId(), trafficLogList);
            if (traffic == null) {
                traffic = new ArrayList<>();
            }
            for (TrafficLog log : traffic) {
                dataModel = new SentryDataModel();
                dataModel.setVideo(log.getVideo());
                dataModel.setTime(log.getTime());
                dataModel.setDate(log.getDate());
                dataModel.setExit(log.isExit());
                dataModel.setValid(log.isValid());
                dataModel.setGate(log.getGateway().getName());
                dataModel.setPersonId(log.getPerson().getId());
                dataModel.setId(log.getId());
                dataModel.setPdpName(log.getVirdi().getName());
                if(log.getPerson().getName()==null || log.getPerson().getName().length()<=1){
                    if(log.getCard()!=null && log.getGuest()!=null ) {
                        dataModel.setName(log.getGuest().getFirstname() + "  " + log.getGuest().getLastname());
                    }else{
                        dataModel.setName("مهمان ثبت نشده");
                    }
                }else{
                    dataModel.setName(log.getPerson().getName() + "  " + log.getPerson().getLastName());
                }
                trafficLogList.add(dataModel);
            }
            trafficLogsbygate.add((new ArrayList<>(trafficLogList)));
        }
//                else{
//                    notAccess.add(gateway);
//                }


//            }
//            gateways.remove(notAccess);
//            trafficLogs = new ListDataModel<List<DataModel<SentryDataModel>>>(trafficLogsbygate);

        parkingLogList = parkingLogService.findParkingInDuration(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")), CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));

    }

    public String begin() {
        me.setActiveMenu(MenuType.SENTRY);
        refresh();
        return "monitoring";
    }

    public void beginSentry() {
        me.setActiveMenu(MenuType.SENTRY);
        initialize();
        me.redirect("/monitoring/sentry-monitor.xhtml");
    }

    public void beginSentryParking() {
        me.setActiveMenu(MenuType.SENTRY);
        initialize();
        me.redirect("/monitoring/parking-monitor.xhtml");
    }

    public void trackByPerson() {
        handlePersonAction.init();
        me.setActiveMenu(MenuType.REPORT);
        personPage = 1;
        person = true;
        personAndGate = false;
        personAndTime = false;
        personList = personService.getAllPersonModel();
        me.redirect("/monitoring/track-person.xhtml");

    }

    public void selectForComment(SentryDataModel sentryDataModel) {
        handleCommentAction.setMessage("");
        currentSentryDataModel = sentryDataModel;
        currentTrafficLog = logService.findById(currentSentryDataModel.getId());

        validForComment = true;
        long logMin = time2Minute(currentTrafficLog.getTime());
        long commentMin = time2Minute(LangUtil.getEnglishNumber(CalendarUtil.getTime(new Date(), new Locale("fa"))));
        if ((commentMin - logMin) > Long.valueOf(me.getSystemParameter().get(SystemParameterType.TRAFFIC_LOG_COMMENT_VALID_TIME)))
            validForComment = false;


        Comment comment = commentService.findByTrafficLog(currentSentryDataModel.getId());
        handleCommentAction.setMessage(comment.getMessage());
    }

    public void paint(OutputStream stream, Object object) throws IOException, URISyntaxException {
        Long personId = (Long) object;
        Person person = null;

        person = personService.findById(personId);
        if (person.getPicture() != null)
            stream.write(person.getPicture());
        else

            stream.write(generalHelper.getAnonymous());


        stream.flush();
        stream.close();
    }

    public void dotrackByPerson() {
        eventLogList = logService.findByPerson(currentPerson.getId(), ir.university.toosi.tms.util.LangUtil.getEnglishNumber(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa"))));
        init();
        me.redirect("/monitoring/monitor-log.xhtml");
    }


    public void trackByPersonAndGate() {
        handlePersonAction.init();
        me.setActiveMenu(MenuType.REPORT);
        person = false;
        personAndGate = true;
        personAndTime = false;
        personPage = 1;
        personList = personService.getAllPersonModel();


        List<Gateway> gateways = gatewayService.getAllGateway();
        gatewayItems = new SelectItem[gateways.size()];
        int i = 0;
        for (Gateway gateway1 : gateways) {
            gatewayItems[i++] = new SelectItem(gateway1.getId(), gateway1.getName());
        }
        if (gatewayItems != null)
            gatewayId = gatewayItems[0].getValue().toString();
        me.redirect("/monitoring/track-person.xhtml");
    }

    public void dotrackByPersonAndGate() {
        List<TrafficLog> innerTrafficLogList = null;
        handlePersonAction.init();
        eventLogList = logService.findByPersonAndGate(currentPerson.getId(), Long.valueOf(gatewayId), ir.university.toosi.tms.util.LangUtil.getEnglishNumber(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa"))));
        init();
        me.redirect("/monitoring/monitor-log.xhtml");
    }

    public void trackByPersonAndTime() {
        handlePersonAction.init();
        me.setActiveMenu(MenuType.REPORT);
        person = false;
        personAndGate = false;
        personAndTime = true;
        personPage = 1;
        startTime = "0";
        endTime = "0";
        startHour = "0";
        startMinute = "0";
        startSecond = "0";
        endHour = "0";
        endMinute = "0";
        endSecond = "0";
        personList = personService.getAllPersonModel();
        me.redirect("/monitoring/track-person.xhtml");
    }

    public void dotrackByPersonAndTime() {
        startTime = (startHour.length() == 2 ? startHour : '0' + startHour) + ":" + (startMinute.length() == 2 ? startMinute : '0' + startMinute) + ":" + (startSecond.length() == 2 ? startSecond : '0' + startSecond);
        endTime = (endHour.length() == 2 ? endHour : '0' + endHour) + ":" + (endMinute.length() == 2 ? endMinute : '0' + endMinute) + ":" + (endSecond.length() == 2 ? endSecond : '0' + endSecond);
        long startTime1 = time2long(startTime);
        long endTime1 = time2long(endTime);

        if (endTime1 < startTime1) {
            me.addInfoMessage("wrong.time");
            me.redirect("/monitoring/track-person.xhtml");
            return;
        }

        List<TrafficLog> innerTrafficLogList = null;
        eventLogList = logService.findByPersonLocationInDuration(currentPerson.getId(), startTime, endTime, ir.university.toosi.tms.util.LangUtil.getEnglishNumber(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa"))));

        me.redirect("/monitoring/monitor-log.xhtml");
    }


    private void refresh() {
        initialize();
    }

    public void mapEdition() {

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

    public List<ParkingLog> getParkingLogList() {
        return parkingLogList;
    }

    public void setParkingLogList(List<ParkingLog> parkingLogList) {
        this.parkingLogList = parkingLogList;
    }
}
