package ir.university.toosi.tms.model.service.zone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.zone.PDPDAOImpl;
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
import ir.university.toosi.tms.model.entity.zone.PDP;
import ir.university.toosi.tms.model.entity.zone.PDPSync;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.model.service.GatewayPersonServiceImpl;
import ir.university.toosi.tms.model.service.PermissionServiceImpl;
import ir.university.toosi.tms.model.service.calendar.CalendarDateServiceImpl;
import ir.university.toosi.tms.model.service.calendar.CalendarServiceImpl;
import ir.university.toosi.tms.model.service.personnel.CardServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.rule.RulePackageServiceImpl;
import ir.university.toosi.tms.model.service.rule.RuleServiceImpl;
import ir.university.toosi.tms.util.*;
import ir.university.toosi.wtms.web.util.CalendarUtil;
import org.jboss.ejb3.annotation.TransactionTimeout;

import javax.ejb.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
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
public class PDPServiceImpl<T extends PDP> {

    @EJB
    private PDPDAOImpl PDPdao;
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
            return (T) PDPdao.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByGatewayId(String id) {
        try {
            return (List<T>) PDPdao.findByGatewayId(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllPdpbyIDs(List<Long> id) {
        try {
            return (List<T>) PDPdao.getAllPdpbyIDs(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByCameraId(long id) {
        try {
            return (List<T>) PDPdao.findByCameraId(id);
        } catch (Exception e) {
            return null;
        }
    }

    public T findByIp(String ip) {
        try {
//            EventLogManager.eventLog(eventLogService, null, BLookup.class.getSimpleName(), EventLogType.SEARCH, PDP.getEffectorUser());
            return (T) PDPdao.findByIp(ip);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllPDPs() {
        try {
            return (List<T>) PDPdao.findAll("PDP.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public long getMaximumId() {
        try {
            return PDPdao.maximumId("PDP.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public String deletePDP(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), BLookup.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            PDPdao.delete(findById(entity.getId()));
            return "operation.occurred";
        } catch (Exception e) {
            e.printStackTrace();
            return "FALSE";
        }
    }

    public T createPDP(T entity) {
        try {
            T t = (T) PDPdao.create(entity);
            Permission permission = new Permission(String.valueOf(t.getId()), t.getName(), PermissionType.PDP);
            permissionService.createPermission(permission);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), BLookup.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return t;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean editPDP(T entity) {
        try {
            PDP old = findById(entity.getId());
            PDP newPDP = new PDP();

            newPDP.setName(old.getName());
            newPDP.setEnabled(old.isEnabled());
            newPDP.setGateway(old.getGateway());
            newPDP.setCamera(old.getCamera());
            newPDP.setIp(old.getIp());
            newPDP.setOnline(old.isOnline());
            newPDP.setDescription(old.getDescription());
            newPDP.setStatus("o," + entity.getEffectorUser());

            PDPdao.createOld(newPDP);

            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), BLookup.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            PDPdao.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean cleanDirectory(PDPSync pdpSync) {
        for (PDP pdp : pdpSync.getPdpList()) {


            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(Configuration.getProperty("pdp.dir") + pdp.getId()), "{*.dat,*.sch,*.png,*.txt,*.bmp,*.fpt}")) {

                for (Path path : directoryStream) {
                    Files.delete(path);
                }
            } catch (IOException e) {
                return false;
            }
        }
        List<Gateway> gateways = gatewayService.getAllGateway();
        int i = 1;
        if (gateways != null)
            for (Gateway gateway : gateways) {
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(Configuration.getProperty("gate.dir") + gateway.getId()), "{*.dat,*.sch,*.png,*.txt,*.bmp,*.fpt}")) {
                    for (Path path : directoryStream) {
                        Files.delete(path);
                    }
                } catch (IOException e) {
                    return false;
                }
            }

        return true;
    }

    public boolean generateFiles(PDPSync pdpSync, PDP pdp) throws IOException {
        List<Person> personList = personService.getAllPerson();
        List<Calendar> calendarList = calendarService.getAllCalendar();
        List<RulePackage> rulePackages = rulePackageService.getAllRulePackage();
//        Hashtable<Long, Integer> calendarTable = new Hashtable<>();
        Hashtable<Long, Integer> rulePackageTable = new Hashtable<>();
        String content = "";
        int i = 0;
        Path path = null;
        Files.createDirectories(Paths.get(Configuration.getProperty("pdp.dir") + pdp.getId() + File.separator));


        for (RulePackage rulePackage : rulePackages) {
            if (pdpSync.isSchedule()) {

                content = "";
                if (rulePackage.getCalendar() == null)
                    continue;
                List<CalendarDate> calendarDates = calendarDateService.findByCalendarID(rulePackage.getCalendar().getId());
                if (calendarDates != null)
                    for (CalendarDate calendarDate : calendarDates) {
                        List<Rule> rules = ruleService.findByRulePackageIdAndType(rulePackage.getId(), calendarDate.getDayType().getId());
                        if (rules != null)
                            for (Rule rule : rules) {
                                content += calendarDate.getStartDate() + ";" + calendarDate.getEndDate() + ";";
                                String[] startTime = rule.getStartTime().split(":");
                                startTime[0] = startTime[0].length() == 1 ? "0" + startTime[0] : startTime[0];
                                startTime[1] = startTime[1].length() == 1 ? "0" + startTime[1] : startTime[1];
                                startTime[2] = startTime[2].length() == 1 ? "0" + startTime[2] : startTime[2];
                                String[] endTime = rule.getEndTime().split(":");
                                endTime[0] = endTime[0].length() == 1 ? "0" + endTime[0] : endTime[0];
                                endTime[1] = endTime[1].length() == 1 ? "0" + endTime[1] : endTime[1];
                                endTime[2] = endTime[2].length() == 1 ? "0" + endTime[2] : endTime[2];
                                content += startTime[0] + startTime[1] + startTime[2] + ";" + endTime[0] + endTime[1] + endTime[2] + ";2\r\n";
                            }
                    }
                path = Files.createFile(Paths.get(Configuration.getProperty("pdp.dir") + pdp.getId() + File.separator + i + ".sch"));
                Files.write(path, content.getBytes());
            }
//                  }  calendarTable.put(rulePackage.getCalendar().getId(), i);
            rulePackageTable.put(rulePackage.getId(), i);

            i++;


        }

//            for (Calendar calendar : calendarList) {
//                content = "";
//                List<CalendarDate> calendarDates = calendarDateService.findByCalendarID(String.valueOf(calendar.getId()));
//                for (CalendarDate calendarDate : calendarDates) {
//                    content += calendarDate.getStartDate() + ";" + calendarDate.getEndDate() + "\r\n";
//                }
//                calendarTable.put(calendar.getId(), i);
//                path = Files.createFile(Paths.get(Configuration.getProperty("pdp.dir") + i + ".sch"));
//                Files.write(path, content.getBytes());
//                i++;
//            }

        String db = "";
        String cardData = "";
        String rulepackage = "";
        if (personList != null)
            for (Person person : personList) {
                if (person.getRulePackage() == null)
                    continue;
                cardData = "";
                if (pdpSync.isDb()) {

                    rulepackage = person.getRulePackage() != null ? String.valueOf(rulePackageTable.get(person.getRulePackage().getId())) : "";
                    List<Card> cards = cardService.findByPersonId(person.getId());
                    if (cards != null && cards.size() != 0)

                        for (Card card : cards) {

                            cardData = card.getCode();
                            db += person.getName() + ";" + person.getLastName() + ";" + cardData + ";" + person.getPersonOtherId() + ";" + person.getPassword() + ";" + rulepackage + ";1;0;" + person.getPersonOtherId() + ";" + person.getNationalCode() + "\r\n";
                        }
                    else {
                        db += person.getName() + ";" + person.getLastName() + ";" + "" + ";" + person.getPersonOtherId() + ";" + person.getPassword() + ";" + rulepackage + ";1;0;" + person.getPersonOtherId() + ";" + person.getNationalCode() + "\r\n";
                    }
                }
//
            }
        if (db.length() > 0) {
            path = Files.createFile(Paths.get(Configuration.getProperty("pdp.dir") + pdp.getId() + File.separator + "db.txt"));
            Files.write(path, db.getBytes("utf-16BE"));
            byte[] bytes = new byte[db.getBytes("utf-16BE").length - 2];
            byte[] c = Files.readAllBytes(path);
            for (int j = 2; j < c.length; j++) {
                bytes[j - 2] = c[j];
            }
            Files.write(path, bytes);
        }
        if (pdpSync.isFinger()) {
            List<Person> persons = personService.findWithRulePackage();

            for (Person person : persons) {
                if (person.getFinger() == null || person.getFinger().length == 0)
                    continue;
                String zeros = "";
                for (int j = 1; j <= 8 - (person.getPersonOtherId().length()); j++) {
                    zeros += "0";
                }
                path = Files.createFile(Paths.get(Configuration.getProperty("pdp.dir") + pdp.getId() + File.separator + zeros + person.getPersonOtherId() + ".fpt"));
                Files.write(path, person.getFinger());
            }
            path = Files.createFile(Paths.get(Configuration.getProperty("pdp.dir") + pdp.getId() + File.separator + "Sync.fpt"));
            Files.write(path, new byte[0]);
        }


        return true;
    }

    public boolean generateFiles4One(PDPSync pdpSync, PDP pdp) throws IOException {
        List<RulePackage> rulePackages = rulePackageService.getAllRulePackage();
//        Hashtable<Long, Integer> calendarTable = new Hashtable<>();
        Hashtable<Long, Integer> rulePackageTable = new Hashtable<>();
        String content = "";
        int i = 0;
        Path path = null;
        Files.createDirectories(Paths.get(Configuration.getProperty("pdp.dir") + pdp.getId() + File.separator));


        if (pdpSync.isFinger()) {

            if (pdpSync.getPerson().getFinger() == null || pdpSync.getPerson().getFinger().length == 0)
                return true;
            String zeros = "";
            for (int j = 1; j <= 8 - (pdpSync.getPerson().getPersonOtherId().length()); j++) {
                zeros += "0";
            }
            path = Files.createFile(Paths.get(Configuration.getProperty("pdp.dir") + pdp.getId() + File.separator + zeros + pdpSync.getPerson().getPersonOtherId() + ".fpt"));
            Files.write(path, pdpSync.getPerson().getFinger());
            path = Files.createFile(Paths.get(Configuration.getProperty("pdp.dir") + pdp.getId() + File.separator + "Sync.fpt"));
            Files.write(path, new byte[0]);
        }

        return true;
    }

    public boolean generateGate(PDPSync pdpSync) throws IOException {
        List<Gateway> gateways = gatewayService.getAllGateway();
        int i = 1;
        Path path = null;
        if (pdpSync.isSchedule()) {
            if (gateways != null)
                for (Gateway gateway : gateways) {
                    Files.createDirectories(Paths.get(Configuration.getProperty("gate.dir") + gateway.getId() + File.separator));

                    if (pdpSync.isPicture()) {

                        List<Person> persons = gatewayPersonService.findPersonByGatewayId(gateway.getId());
                        for (Person person : persons) {
                            if (person.getPicture() != null) {
                                path = Files.createFile(Paths.get(Configuration.getProperty("gate.dir") + gateway.getId() + File.separator + person.getPersonOtherId() + ".bmp"));
                                final int targetWidth = 224;
                                final int targetHeight = 300;

                                BufferedImage orgBufferedImage = ImageIO.read(new ByteArrayInputStream(person.getPicture()));

                                //float widthScaleRatio = (float) targetWidth / bufferedImage.getWidth();
                                //float heightScaleRatio = (float) targetHeight / bufferedImage.getHeight();

                                //todo bufferedImage = Thumbnails.of(bufferedImage).scale(heightScaleRatio).asBufferedImage();
                                //todo bufferedImage = Thumbnails.of(bufferedImage).scale(widthScaleRatio).asBufferedImage();

                                Image scaledImage = orgBufferedImage.getScaledInstance(targetWidth, targetHeight,
                                        Image.SCALE_SMOOTH);

                                BufferedImage scaledBufferedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_USHORT_565_RGB);
                                scaledBufferedImage.getGraphics().drawImage(scaledImage, 0, 0, null);


                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ImageIO.write(scaledBufferedImage, "bmp", baos);
                                Files.write(path, baos.toByteArray());
                            }
                        }
                    }
                    String content = "";
                    if (gateway.getRulePackage() == null || gateway.getRulePackage().getCalendar() == null)
                        continue;
                    List<CalendarDate> calendarDates = calendarDateService.findByCalendarID(gateway.getRulePackage().getCalendar().getId());
                    if (calendarDates != null)
                        for (CalendarDate calendarDate : calendarDates) {
                            List<Rule> rules = ruleService.findByRulePackageIdAndType(gateway.getRulePackage().getId(), calendarDate.getDayType().getId());
                            if (rules != null)
                                for (Rule rule : rules) {
                                    content += calendarDate.getStartDate() + ";" + calendarDate.getEndDate() + ";";
                                    String[] startTime = rule.getStartTime().split(":");
                                    startTime[0] = startTime[0].length() == 1 ? "0" + startTime[0] : startTime[0];
                                    startTime[1] = startTime[1].length() == 1 ? "0" + startTime[1] : startTime[1];
                                    startTime[2] = startTime[2].length() == 1 ? "0" + startTime[2] : startTime[2];
                                    String[] endTime = rule.getEndTime().split(":");
                                    endTime[0] = endTime[0].length() == 1 ? "0" + endTime[0] : endTime[0];
                                    endTime[1] = endTime[1].length() == 1 ? "0" + endTime[1] : endTime[1];
                                    endTime[2] = endTime[2].length() == 1 ? "0" + endTime[2] : endTime[2];
                                    content += startTime[0] + startTime[1] + startTime[2] + ";" + endTime[0] + endTime[1] + endTime[2] + ";2\r\n";
                                    ;
                                }
                        }

//                List<CalendarDate> calendarDates = calendarDateService.findByCalendarID(String.valueOf(gateway.getRulePackage().getCalendar().getId()));
//                for (CalendarDate calendarDate : calendarDates) {
//                    content += calendarDate.getStartDate() + ";" + calendarDate.getEndDate() + "\r\n";
//                }
                    path = Files.createFile(Paths.get(Configuration.getProperty("gate.dir") + gateway.getId() + File.separator + "gate" + i + ".sch"));
                    Files.write(path, content.getBytes());
                    i++;
                }
        }
        return true;
    }

    public boolean generate4OneGate(PDPSync pdpSync) throws IOException {
        List<Gateway> gateways = gatewayService.getAllGateway();
        int i = 1;
        Path path = null;

        if (gateways != null)
            for (Gateway gateway : gateways) {
                Files.createDirectories(Paths.get(Configuration.getProperty("gate.dir") + gateway.getId() + File.separator));

                if (pdpSync.isPicture()) {

                    path = Files.createFile(Paths.get(Configuration.getProperty("gate.dir") + gateway.getId() + File.separator + pdpSync.getPerson().getPersonOtherId() + ".bmp"));
                    final int targetWidth = 224;
                    final int targetHeight = 300;
                    BufferedImage orgBufferedImage = ImageIO.read(new ByteArrayInputStream(pdpSync.getPerson().getPicture()));

                    //float widthScaleRatio = (float) targetWidth / bufferedImage.getWidth();
                    //float heightScaleRatio = (float) targetHeight / bufferedImage.getHeight();

                    //todo bufferedImage = Thumbnails.of(bufferedImage).scale(heightScaleRatio).asBufferedImage();
                    //todo bufferedImage = Thumbnails.of(bufferedImage).scale(widthScaleRatio).asBufferedImage();

                    Image scaledImage = orgBufferedImage.getScaledInstance(targetWidth, targetHeight,
                            Image.SCALE_SMOOTH);

                    BufferedImage scaledBufferedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_USHORT_565_RGB);
                    scaledBufferedImage.getGraphics().drawImage(scaledImage, 0, 0, null);


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(scaledBufferedImage, "bmp", baos);
                    Files.write(path, baos.toByteArray());
                }

            }

        return true;
    }

    public boolean synchronizePdp(PDPSync pdpSync) {
        Set<PDP> pdps = pdpSync.getPdpList();
        if (pdps == null || pdps.size() == 0)
            return false;
        boolean flag = true;
        cleanDirectory(pdpSync);
        try {
            generateGate(pdpSync);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (PDP pdp : pdps) {
            try {
                if (ping(pdp.getIp())) {
                    if (generateFiles(pdpSync, pdp)) {
                        new Thread(new FileUploaderUtil(pdp, this)).start();
                    } else {
                        flag = false;

                    }
                } else {
                    T t = findByIp(pdp.getIp());
                    t.setUpdateDate(CalendarUtil.getDate(new Date(), new Locale("fa")));
                    t.setSuccess(false);
                    editPDP(t);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return flag;
    }

    public boolean synchronizeOnePdp(PDPSync pdpSync) {
        Set<PDP> pdps = pdpSync.getPdpList();
        if (pdps == null || pdps.size() == 0)
            return false;
        boolean flag = true;
        cleanDirectory(pdpSync);
        try {
            generate4OneGate(pdpSync);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (PDP pdp : pdps) {
            try {
                if (generateFiles4One(pdpSync, pdp)) {
                    new Thread(new FileUploaderUtil(pdp, this)).start();
                } else {
                    flag = false;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return flag;
    }

    public boolean fingerPrint(Set<PDP> pdps) {
        List<Person> persons = personService.findWithRulePackage();
        for (PDP pdp : pdps) {
            try {
                if (ping(pdp.getIp())) {
                    for (Person person : persons) {

                        String zeros = "";
                        for (int i = 1; i <= 8 - (person.getPersonOtherId().length()); i++) {
                            zeros += "0";
                        }
                        byte[] b = TFTPUtility.get(pdp.getIp(), zeros + person.getPersonOtherId() + ".fpt");
                        person.setFinger(b.length == 0 ? null : b);
                        if (person.getFinger() != null) {
                            personService.editPerson(person);
                        }
                    }
                } else {
                    T t = findByIp(pdp.getIp());
                    t.setUpdateDate(CalendarUtil.getDate(new Date(), new Locale("fa")));
                    t.setSuccess(false);
                    editPDP(t);
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
            return PDPdao.exist(ip, id);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean existNotId(String ip) {
        try {
            return PDPdao.existNotId(ip);
        } catch (Exception e) {
            return false;
        }
    }

}