package ir.university.toosi.tms.util;

import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.entity.zone.Virdi;
import ir.university.toosi.tms.model.service.AuthenticateServiceImpl;
import ir.university.toosi.tms.model.service.CrossingServiceImpl;
import ir.university.toosi.tms.model.service.TrafficLogServiceImpl;
import ir.university.toosi.tms.model.service.personnel.CardServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.zone.VirdiServiceImpl;
import ir.university.toosi.tms.readerwrapper.*;
import ir.university.toosi.wtms.web.util.CalendarUtil;
import net.sf.jni4net.Bridge;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import javax.faces.application.FacesMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

/**
 * Created by farzad on 10/4/15.
 */
public class VirdiListener {
    private static VirdiServiceImpl virdiService;
    private static PersonServiceImpl personService;
    private static AuthenticateServiceImpl authenticateService;
    private static CrossingServiceImpl crossingService;
    private static CardServiceImpl cardService;
    private static TrafficLogServiceImpl trafficLogService;
    static ReaderWrapper readerWrapper;

    private final static String CHANNEL = "/notify";

    public static void accessControl() throws IOException, URISyntaxException {
        Bridge.setVerbose(true);
        Bridge.init(new File("D:\\lib"));
        Bridge.setDebug(true);

        Bridge.LoadAndRegisterAssemblyFrom(new File("D:\\lib\\ReaderWrapper.j4n.dll"));
        System.load("d:\\lib\\Interop.UCSAPICOMLib.dll");
        System.load("d:\\lib\\jni4net.n-0.8.8.0.dll");
        System.load("d:\\lib\\log4net.dll");
        System.load("d:\\lib\\ReaderWrapper.dll");
        System.load("d:\\lib\\ReaderWrapper.j4n.dll");
        System.load("d:\\lib\\UNIONCOMM.SDK.UCBioBSP.dll");
        readerWrapper = new ReaderWrapper();
        readerWrapper.addOnVerifyFinger1To1(new VerifyFinger1To1Delegate() {
            @Override
            public void Invoke(int terminalId, int userId, VirdiAuthMode authMode, byte[] fingerData) {

                ir.university.toosi.tms.model.entity.personnel.Person personEntity = personService.findById(userId);

                TrafficLog trafficLog = null;
                Person person = new Person();
//                person.setFingerprints(personEntity.getFinger());
//                person.setUserIdforTerminal(userId);
                if (readerWrapper.Verify(person, fingerData)) {
                    try {
                        trafficLog = crossingService.authorize(virdiService.findById(terminalId), String.valueOf(userId), String.valueOf(authMode.GetHashCode()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (trafficLog != null) {
                        try {
                            finalizeTerrificLog(trafficLog, personService.findById(userId));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        readerWrapper.SendAuthResultToTerminal(terminalId, userId, null, trafficLog.isValid());
                        send(trafficLog);

                    } else {
                        readerWrapper.SendAuthResultToTerminal(terminalId, userId, null, false);
                    }

                }
                readerWrapper.SendAuthResultToTerminal(terminalId, userId, null, false);
            }
        });

        readerWrapper.addOnVerifyCard(new VerifyCardDelegate() {
            TrafficLog trafficLog = null;

            @Override
            public void Invoke(int terminalId, VirdiAuthMode authMode, String card) {

                ir.university.toosi.tms.model.entity.personnel.Person person = cardService.findByCode(card).getPerson();
                if (person != null) {
                    try {
                        trafficLog = crossingService.authorize(virdiService.findById(terminalId), String.valueOf(person.getId()), String.valueOf(authMode.GetHashCode()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (trafficLog != null) {
                        try {
                            finalizeTerrificLog(trafficLog, person);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        readerWrapper.SendAuthResultToTerminal(terminalId, Integer.parseInt(String.valueOf(person.getId())), null, trafficLog.isValid());
                        send(trafficLog);

                    } else {
                        readerWrapper.SendAuthResultToTerminal(terminalId, Integer.parseInt(String.valueOf(person.getId())), null, false);
                    }
                } else {
                    readerWrapper.SendAuthResultToTerminal(terminalId, Integer.parseInt(String.valueOf(person.getId())), null, false);
                }

            }
        });

        readerWrapper.addOnVerifyUserPass(new VerifyUserPassDelegate() {
            @Override
            public void Invoke(int terminalId, int userId, VirdiAuthMode authMode, String password) {
                TrafficLog trafficLog = null;
                try {
                    trafficLog = crossingService.authorize(virdiService.findById(terminalId), String.valueOf(userId), String.valueOf(authMode.GetHashCode()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (trafficLog != null) {
                    try {
                        finalizeTerrificLog(trafficLog, personService.findById(userId));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    readerWrapper.SendAuthResultToTerminal(terminalId, userId, null, trafficLog.isValid());
                    send(trafficLog);

                } else {
                    readerWrapper.SendAuthResultToTerminal(terminalId, userId, null, false);
                }

            }
        });
        readerWrapper.StartService(9870);
    }

    public static void VirdiHealth() {
        readerWrapper.addOnTerminalConnected(new TerminalConnectedDelegate() {
            @Override
            public void Invoke(int terminalId, String terminalIP) {
                Virdi virdi = virdiService.findById(terminalId);
                virdi.setHealthStatus(true);
                virdiService.editVirdi(virdi);
            }
        });
        readerWrapper.addOnTerminalConnected(new TerminalConnectedDelegate() {
            @Override
            public void Invoke(int terminalId, String terminalIP) {
                Virdi virdi = virdiService.findById(terminalId);
                virdi.setHealthStatus(false);
                virdiService.editVirdi(virdi);
            }
        });

    }

    private static void finalizeTerrificLog(TrafficLog trafficLog, ir.university.toosi.tms.model.entity.personnel.Person person) throws IOException {
        if (trafficLog.isValid()) {

            if (trafficLog.getPdp().getCamera() != null) {
                createPicture(trafficLog);
            }
            trafficLog.setPerson(person);
            trafficLog.setOrgan(person.getOrganRef());
            TrafficLog lastTrafficLog = trafficLogService.findLastByPerson(person.getId(), CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
            if (lastTrafficLog != null) {
                lastTrafficLog.setLast(false);
                trafficLogService.editTrafficLog(lastTrafficLog);
            }
            trafficLog.setLast(true);
        } else {
            trafficLog.setLast(false);
            if (trafficLog.getPdp().getCamera() != null) {
                createPicture(trafficLog);
            }
            trafficLog.setPerson(person);
            trafficLog.setOrgan(person.getOrganRef());
        }
        trafficLogService.createTrafficLog(trafficLog);
    }

    private static void createPicture(TrafficLog trafficLog) throws IOException {
        File folder = new File(Configuration.getProperty("pic.person") + trafficLog.getPictures());
        List<byte[]> pics = Initializer.pics.get(trafficLog.getPdp().getCamera().getId());
        if (!folder.exists()) {
            folder.mkdir();
        }
        for (int i = 0; i < pics.size(); i++) {
            File file = new File(Configuration.getProperty("pic.person") + trafficLog.getPictures() + "/" + i + ".jpg");

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(pics.get(i));
            fileOutputStream.flush();
            fileOutputStream.close();

        }


    }

    public static void send(TrafficLog trafficLog) {
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish(CHANNEL, trafficLog);
    }


}
