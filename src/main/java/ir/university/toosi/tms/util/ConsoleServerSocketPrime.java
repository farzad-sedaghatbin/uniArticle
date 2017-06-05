package ir.university.toosi.tms.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.service.CrossingServiceImpl;
import ir.university.toosi.tms.model.service.TrafficLogServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 1.0
 */

public class ConsoleServerSocketPrime implements Runnable {

    private CrossingServiceImpl crossingService;
    private TrafficLogServiceImpl trafficLogService;
    private PersonServiceImpl personService;

    public ConsoleServerSocketPrime(CrossingServiceImpl crossingService, TrafficLogServiceImpl trafficLogService, PersonServiceImpl personService) {
        this.crossingService = crossingService;
        this.trafficLogService = trafficLogService;
        this.personService = personService;
    }

    @Override
    public void run() {
        ServerSocket server = null;
        try {
            TrafficLog trafficLog = null;
            server = new ServerSocket(Integer.valueOf(Configuration.getProperty("server.port")), 0, InetAddress.getByName(Configuration.getProperty("server.ip")));
            while (true) {
                long start = 0;
//                long ens=0;
                Socket socket = server.accept();
//                socket.getOutputStream().write(("rsc 0").getBytes());
//                System.out.println("send");

                start = System.currentTimeMillis();
                trafficLog = null;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String receivedMSG = bufferedReader.readLine();
                receivedMSG = bufferedReader.readLine();
                System.out.println("2222>>>>>>>>>>" + receivedMSG);
//                socket.getOutputStream().write(("rsc 0").getBytes());
//                System.out.println("send");
                String[] messageType = receivedMSG.split(" ");
                Person person = null;
                if (messageType[0].equalsIgnoreCase("rqi")) {
                    String[] s = messageType[1].split(",");
//                    System.out.println("1111111111111");
                    trafficLog = crossingService.authorize(socket.getInetAddress().toString().replace("/", ""), s[0], s[1], start);
//                    System.out.println("33333333333333333");
                    if (trafficLog != null) {
                        if (trafficLog.isValid()) {
                            socket.getOutputStream().write(("rsc 0").getBytes());
                            System.out.println("send true");
                            socket.getOutputStream().flush();
                            System.out.println(System.currentTimeMillis() - start);
                            person = personService.getPersonByPersonOtherId(s[0]);
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
                            socket.getOutputStream().write(("rsc 1").getBytes());
                            System.out.println("send false");
                            socket.getOutputStream().flush();
                            System.out.println(System.currentTimeMillis() - start);
                            person = personService.getPersonByPersonOtherId(s[0]);
                            trafficLog.setPerson(person);
                            trafficLog.setOrgan(person.getOrganRef());
                        }
                        if (Initializer.connections.get(trafficLog.getPdp().getId()) == null) {
                            Initializer.connections.put(trafficLog.getPdp().getId(), socket);
                        } else {
                            socket.close();
                        }
                        trafficLogService.createTrafficLog(trafficLog);

                    }
                } else if (messageType[0].equalsIgnoreCase("off")) {
                    String[] s = messageType[1].split(",");

                    trafficLog = trafficLogService.offline(socket.getInetAddress().toString().replace("/", ""), s[0], s[1], s[2]);
                    if (trafficLog != null && trafficLog.isValid()) {
                        socket.getOutputStream().write(("off tok," + s[0] + "\r\n").getBytes());
                        socket.getOutputStream().flush();
                        Thread.sleep(1000);

                    } else {

                        socket.getOutputStream().write(("off notok," + s[0] + "\r\n").getBytes());
                        socket.getOutputStream().flush();
                        Thread.sleep(1000);

                    }
                    if (trafficLog != null) {

                        TrafficLog lastTrafficLog = trafficLogService.findLastByPerson(trafficLog.getPerson().getId(), CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
                        if (lastTrafficLog != null) {
                            lastTrafficLog.setLast(false);
                            trafficLogService.editTrafficLog(lastTrafficLog);
                        }
                        trafficLog.setLast(true);
                        trafficLogService.createTrafficLog(trafficLog);
                    }
                }
                try {
                    new RESTfulClientUtil().restFullService("http://" + Configuration.getProperty("server.ip") + ":" + Configuration.getProperty("kernel.port") + "/WTMS/restful/TMSService", "/Cross", new ObjectMapper().writeValueAsString(trafficLog));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
//            new Thread(new ConsoleServerSocket()).start();
            e.printStackTrace();
            try {
                server.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            new Thread(new ConsoleServerSocketPrime(crossingService, trafficLogService, personService)).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
