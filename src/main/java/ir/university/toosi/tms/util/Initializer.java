package ir.university.toosi.tms.util;

import ir.university.toosi.tms.model.service.SystemConfigurationServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.net.Socket;
import java.util.Hashtable;
import java.util.List;

@Singleton
@Startup
public class Initializer {

    @EJB
    private SystemConfigurationServiceImpl systemConfigurationService;
    @EJB
    private PersonServiceImpl personService;
//    @Inject
//    private GeneralHelper generalHelper;


    public static Hashtable<Long, Socket> connections = new Hashtable<>();
    public static Hashtable<String, Object[]> persons = new Hashtable<>();
    public static Hashtable<Long, List<byte[]>> pics = new Hashtable<>();




    @PostConstruct
    public void initial() {

//        System.out.println(generalHelper.getWebServiceInfo().getServerUrl());
//        new Thread(new ConsoleServerSocket(crossingService, trafficLogService, personService)).start();
        SystemParameterManager.fillSystemConfiguration(systemConfigurationService);
        fillPersons();

        //TODO : ADD SystemConfiguration
//        lang = languageService.loadLanguage("fa");
//        new Thread(new CameraManager(photoService, cameraService)).start();
    }

    private void fillPersons() {
        List<Object[]> list = personService.getPersonByPersonnelNumber();
        for (Object[] objects : list) {
            persons.put(String.valueOf(objects[2]), objects);
        }
    }
}
