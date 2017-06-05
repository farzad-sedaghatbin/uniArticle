package ir.university.toosi.tms.util;

import ir.university.toosi.tms.model.entity.LanguageManagement;
import ir.university.toosi.tms.model.entity.rule.Rule;
import ir.university.toosi.tms.model.entity.rule.RuleException;
import ir.university.toosi.tms.model.entity.zone.PDP;
import ir.university.toosi.tms.model.service.*;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.rule.RulePackageServiceImpl;
import ir.university.toosi.tms.model.service.zone.CameraServiceImpl;
import ir.university.toosi.tms.model.service.zone.PDPServiceImpl;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.*;

@Singleton
@Startup
public class Initializer {

    @EJB
    private RulePackageServiceImpl rulePackageService;
    @EJB
    private SystemConfigurationServiceImpl systemConfigurationService;
    @EJB
    private GatewaySpecialStateScheduler gatewaySpecialStateScheduler;
    @EJB
    private CrossingServiceImpl crossingService;
    @EJB
    private TrafficLogServiceImpl trafficLogService;
    @EJB
    private PersonServiceImpl personService;
    @EJB
    private LanguageServiceImpl languageService;
    @EJB
    private CreateSavedReportScheduler createSavedReportScheduler;
    @EJB
    private PhotoServiceImpl photoService;
    @EJB
    private CameraServiceImpl cameraService;
    @EJB
    private PDPServiceImpl pdpService;
//    @Inject
//    private GeneralHelper generalHelper;


    public static Hashtable<Long, Hashtable<String, List<Rule>>> dateRuleHashTables = new Hashtable<>();
    public static Hashtable<Long, Hashtable<String, List<RuleException>>> dateRuleExceptionHashTables = new Hashtable<>();
    public static Hashtable<Long, Socket> connections = new Hashtable<>();
    public static Hashtable<String, Object[]> persons = new Hashtable<>();
    public static Hashtable<String, LanguageManagement> lang = new Hashtable<>();
    public static Hashtable<Long, List<byte[]>> pics = new Hashtable<>();


 //   @Schedule(minute = "*/5",hour = "*")
    public void pdpSync() {
        Date dt = new Date();
        byte[] data = new byte[8];
        String date= CalendarUtil.getDateWithoutSlash(new Date(), Locale.US, "YYMMdd");
        data[1] = (byte) ((Integer.valueOf(date.substring(2,4)))>> 8);
        data[0] = (byte) ((Integer.valueOf(date.substring(2,4))) - 100 & 0xff);
        data[2] =  ((Integer.valueOf(date.substring(4,6))).byteValue());
        data[3] = ((Integer.valueOf(date.substring(6,8))).byteValue());
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        data[4] = (byte) (dayOfWeek + 1);
        data[5] = (byte) dt.getHours();
        data[6] = (byte) dt.getMinutes();
        data[7] = (byte) dt.getSeconds();

        byte[] b = new byte[]{0x40, 0x03, 0x00, 0x00, 0x00, 0x00, 0x08, 0x00, data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], 0x11, 0x0A};
        List<PDP> pdps = pdpService.getAllPDPs();
        for (PDP pdp : pdps) {
            try {
                if(pdpService.ping(pdp.getIp()))
                ir.university.toosi.tms.ConsoleClientSocket.sendMessage(b, pdp.getIp());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @PostConstruct
    public void initial() {

//        System.out.println(generalHelper.getWebServiceInfo().getServerUrl());
//        new Thread(new ConsoleServerSocket(crossingService, trafficLogService, personService)).start();
        rulePackageService.fillRulePackageHashTable();
        SystemParameterManager.fillSystemConfiguration(systemConfigurationService);
        gatewaySpecialStateScheduler.stopService();
        gatewaySpecialStateScheduler.startService();
        fillPersons();

        //TODO : ADD SystemConfiguration
//        lang = languageService.loadLanguage("fa");
        createSavedReportScheduler.startService();
//        new Thread(new CameraManager(photoService, cameraService)).start();
    }

    private void fillPersons() {
        List<Object[]> list = personService.getPersonByPersonnelNumber();
        for (Object[] objects : list) {
            persons.put(String.valueOf(objects[2]), objects);
        }
    }
}
