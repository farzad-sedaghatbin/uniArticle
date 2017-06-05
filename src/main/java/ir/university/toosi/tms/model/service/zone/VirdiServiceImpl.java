package ir.university.toosi.tms.model.service.zone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.IReaderWrapperService;
import ir.university.toosi.tms.model.dao.zone.VirdiDAOImpl;
import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.Permission;
import ir.university.toosi.tms.model.entity.PermissionType;
import ir.university.toosi.tms.model.entity.calendar.Calendar;
import ir.university.toosi.tms.model.entity.calendar.CalendarDate;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.rule.Rule;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.PDPSync;
import ir.university.toosi.tms.model.entity.zone.Virdi;
import ir.university.toosi.tms.model.entity.zone.VirdiSync;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.model.service.GatewayPersonServiceImpl;
import ir.university.toosi.tms.model.service.PermissionServiceImpl;
import ir.university.toosi.tms.model.service.calendar.CalendarDateServiceImpl;
import ir.university.toosi.tms.model.service.calendar.CalendarServiceImpl;
import ir.university.toosi.tms.model.service.personnel.CardServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.rule.RulePackageServiceImpl;
import ir.university.toosi.tms.model.service.rule.RuleServiceImpl;
import ir.university.toosi.tms.readerwrapper.AddCompletedCallBackDelegate;
import ir.university.toosi.tms.readerwrapper.GetUserInfoCompletedCallBackDelegate;
import ir.university.toosi.tms.readerwrapper.ReaderWrapper;
import ir.university.toosi.tms.util.*;
import ir.university.toosi.wtms.web.util.CalendarUtil;
import org.jboss.ejb3.annotation.TransactionTimeout;

import javax.ejb.*;
import javax.imageio.ImageIO;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
@TransactionTimeout(unit = TimeUnit.HOURS, value = 4)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class VirdiServiceImpl<T extends Virdi> {

    @EJB
    private VirdiDAOImpl virdiDAO;
    @EJB
    private PersonServiceImpl personService;
    @EJB
    private CalendarServiceImpl calendarService;
    @EJB
    private CardServiceImpl cardService;
    @EJB
    private GatewayServiceImpl gatewayService;
    @EJB
    private GatewayPersonServiceImpl gatewayPersonService;
    @EJB
    private CalendarDateServiceImpl calendarDateService;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private RulePackageServiceImpl rulePackageService;
    @EJB
    private PermissionServiceImpl permissionService;
    @EJB
    private RuleServiceImpl ruleService;

    public T findById(long id) {
        try {
            return (T) virdiDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }
    public T findByTerminalId(int id) {
        try {
            return (T) virdiDAO.findByTerminalId(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByGatewayId(String id) {
        try {
            return (List<T>) virdiDAO.findByGatewayId(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllVirdibyIDs(List<Long> id) {
        try {
            return (List<T>) virdiDAO.getAllVirdibyIDs(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByCameraId(long id) {
        try {
            return (List<T>) virdiDAO.findByCameraId(id);
        } catch (Exception e) {
            return null;
        }
    }

    public T findByIp(String ip) {
        try {
//            EventLogManager.eventLog(eventLogService, null, BLookup.class.getSimpleName(), EventLogType.SEARCH, Virdi.getEffectorUser());
            return (T) virdiDAO.findByIp(ip);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllVirdis() {
        try {
            return (List<T>) virdiDAO.findAll("Virdi.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public long getMaximumId() {
        try {
            return virdiDAO.maximumId("Virdi.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public String deleteVirdi(T entity) {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), BLookup.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            virdiDAO.delete(findById(entity.getId()));
            return "operation.occurred";
    }

    public T createVirdi(T entity) {
        try {
            T t = (T) virdiDAO.create(entity);
            Permission permission = new Permission(String.valueOf(t.getId()), t.getName(), PermissionType.VIRDI);
            permissionService.createPermission(permission);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), BLookup.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return t;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean editVirdi(T entity) {
        try {
            Virdi old = findById(entity.getId());
            Virdi newVirdi = new Virdi();

            newVirdi.setName(old.getName());
            newVirdi.setEnabled(old.isEnabled());
            newVirdi.setGateway(old.getGateway());
            newVirdi.setCamera(old.getCamera());
            newVirdi.setIp(old.getIp());
            newVirdi.setOnline(old.isOnline());
            newVirdi.setDescription(old.getDescription());
            newVirdi.setStatus("o," + entity.getEffectorUser());

            virdiDAO.createOld(newVirdi);

            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), BLookup.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            virdiDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }






    public void synchronizeVirdi(VirdiSync virdiSync) {
        List<Virdi> virdis = new ArrayList<>(virdiSync.getVirdiList());
        if (virdis == null || virdis.size() == 0)
            return ;
        ReaderWrapper readerWrapper= new ReaderWrapper();
        int[] terminals= new int[virdis.size()];

        for (int i = 0; i < virdis.size(); i++) {
            terminals[i]=(int)virdis.get(i).getId();

        }
        List<Person> persons=personService.getAllPerson();
        ir.university.toosi.tms.readerwrapper.Person[] personVirdi= new ir.university.toosi.tms.readerwrapper.Person[persons.size()];
        for (int i = 0; i < persons.size(); i++) {

            ir.university.toosi.tms.readerwrapper.Person p= new ir.university.toosi.tms.readerwrapper.Person();
//            p.setUserIdforTerminal((int) persons.get(i).getId());
//            p.setFingerprints(persons.get(i).getFinger());
//            p.setEmploymentCode(persons.get(i).getPersonnelNo());
//            p.setPasswordForTerminal(persons.get(i).getPassword());
//            p.setPicture(persons.get(i).getPicture());
            p.setUserName(persons.get(i).getName()+" "+persons.get(i).getLastName());
            personVirdi[i]=p;

        }
        readerWrapper.AddUserInfo(terminals, personVirdi, true, new AddCompletedCallBackDelegate() {
            @Override
            public void Invoke(int terminalId, boolean isSucceed, String failedMessage) {

                T t = findById(terminalId);
                t.setUpdateDate(CalendarUtil.getDate(new Date(), new Locale("fa")));
                t.setSuccess(isSucceed);
                editVirdi(t);
//
            }
        });


    }

    public void synchronizeOneVirdi(VirdiSync virdiSync) {
        List<Virdi> virdis = new ArrayList<>(virdiSync.getVirdiList());
        if (virdis == null || virdis.size() == 0)
            return ;
        ReaderWrapper readerWrapper= new ReaderWrapper();
        int[] terminals= new int[virdis.size()];

        for (int i = 0; i < virdis.size(); i++) {
            terminals[i]=(int)virdis.get(i).getId();

        }
        Person persons=virdiSync.getPerson();
        ir.university.toosi.tms.readerwrapper.Person[] personVirdi= new ir.university.toosi.tms.readerwrapper.Person[1];

            ir.university.toosi.tms.readerwrapper.Person p= new ir.university.toosi.tms.readerwrapper.Person();
//            p.setUserIdforTerminal((int) persons.getId());
//            p.setFingerprints(persons.getFinger());
//            p.setEmploymentCode(persons.getPersonnelNo());
//            p.setPasswordForTerminal(persons.getPassword());
//            p.setPicture(persons.getPicture());
            p.setUserName(persons.getName()+" "+persons.getLastName());
            personVirdi[0]=p;

        readerWrapper.AddUserInfo(terminals, personVirdi, false, new AddCompletedCallBackDelegate() {
            @Override
            public void Invoke(int terminalId, boolean isSucceed, String failedMessage) {

                T t = findById(terminalId);
                t.setUpdateDate(CalendarUtil.getDate(new Date(), new Locale("fa")));
                t.setSuccess(isSucceed);
                editVirdi(t);
//
            }
        });
    }

    public boolean fingerPrint(Set<Virdi> virdis) {
        List<Person> persons = personService.findWithRulePackage();
        ReaderWrapper readerWrapper= new ReaderWrapper();
        for (Virdi virdi : virdis) {
            try {
                if (ping(virdi.getIp())) {

                    for (final Person outPerson : persons) {
                        readerWrapper.GetUserInfo((int) virdi.getId(), (int) outPerson.getId(), new GetUserInfoCompletedCallBackDelegate() {
                            @Override
                            public void Invoke(int terminalId, ir.university.toosi.tms.readerwrapper.Person person, boolean isSucceed, String failedMessage) {
//                                outPerson.setFinger(person.getFingerprints());
                            }
                        });
                        if (outPerson.getFinger() != null) {
                            personService.editPerson(outPerson);
                        }
                    }
                } else {
                    T t = findByIp(virdi.getIp());
                    t.setUpdateDate(CalendarUtil.getDate(new Date(), new Locale("fa")));
                    t.setSuccess(false);
                    editVirdi(t);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean ping(String ip) throws IOException {
        InetAddress inet;
        inet = InetAddress.getByName(ip);
        return inet.isReachable(2000);
    }

    public boolean exist(String ip, long id) {
        try {
            return virdiDAO.exist(ip, id);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean existNotId(String ip) {
        try {
            return virdiDAO.existNotId(ip);
        } catch (Exception e) {
            return false;
        }
    }

    public void forceOpen(int terminalId) throws MalformedURLException {
        URL url = new URL("http://127.0.0.1:8081/ws?wsdl");

        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://ir/", "ReaderWrapperServiceService");

        Service service = Service.create(url, qname);

        IReaderWrapperService readerWrapperService = service.getPort(IReaderWrapperService.class);

        readerWrapperService.forceOpenDoor(terminalId);
    }
    public void unLockDoor(int terminalId) throws MalformedURLException {
        URL url = new URL("http://127.0.0.1:8081/ws?wsdl");

        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://ir/", "ReaderWrapperServiceService");

        Service service = Service.create(url, qname);

        IReaderWrapperService readerWrapperService = service.getPort(IReaderWrapperService.class);

        readerWrapperService.unLockDoor(terminalId);
    }
    public void lockDoor(int terminalId) throws MalformedURLException {
        URL url = new URL("http://127.0.0.1:8081/ws?wsdl");

        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://ir/", "ReaderWrapperServiceService");

        Service service = Service.create(url, qname);

        IReaderWrapperService readerWrapperService = service.getPort(IReaderWrapperService.class);

        readerWrapperService.unLockDoor(terminalId);
    }
}