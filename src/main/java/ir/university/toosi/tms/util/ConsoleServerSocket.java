package ir.university.toosi.tms.util;

import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.service.CrossingServiceImpl;
import ir.university.toosi.tms.model.service.TrafficLogServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 1.0
 */

public class ConsoleServerSocket implements Runnable {

    private CrossingServiceImpl crossingService;
    private TrafficLogServiceImpl trafficLogService;
    private PersonServiceImpl personService;

    public ConsoleServerSocket(CrossingServiceImpl crossingService, TrafficLogServiceImpl trafficLogService, PersonServiceImpl personService) {
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
                start = System.currentTimeMillis();
                trafficLog = null;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String receivedMSG = bufferedReader.readLine();
                System.out.println("2222>>>>>>>>>>" + receivedMSG);
                receivedMSG = bufferedReader.readLine();
                System.out.println("2222>>>>>>>>>>" + receivedMSG);

//                String d = socket.getInetAddress().toString().replace("/", "");

//                Person person = null;
//                if (messageType[0].equalsIgnoreCase("rq")) {
//                    String[] s = messageType[1].split(",");
////                    System.out.println("1111111111111");
//                    trafficLog = crossingService.authorize(d, s[0], s[3], start);
////                    System.out.println("33333333333333333");
//                    if (trafficLog != null) {
//                        if (trafficLog.isValid()) {
//                            socket.getOutputStream().write(("rsc 0,1").getBytes());
//                            System.out.println("send true");
//                            socket.getOutputStream().flush();
//                            socket.close();
//                            createPicture(trafficLog);
//                            System.out.println(System.currentTimeMillis() - start);
//                            person = personService.getPersonByPersonnelNo(s[0]);
//                            trafficLog.setPerson(person);
//                            trafficLog.setOrgan(person.getOrganRef());
//                            TrafficLog lastTrafficLog = trafficLogService.findLastByPerson(person.getId(), CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
//                            if (lastTrafficLog != null) {
//                                lastTrafficLog.setLast(false);
//                                trafficLogService.editTrafficLog(lastTrafficLog);
//                            }
//                            trafficLog.setLast(true);
//                        } else {
//                            trafficLog.setLast(false);
//                            socket.getOutputStream().write(("rsc 1,1").getBytes());
//                            System.out.println("send false");
//                            socket.getOutputStream().flush();
//                            socket.close();
//                            createPicture(trafficLog);
//                            System.out.println(System.currentTimeMillis() - start);
//                            person = personService.getPersonByPersonnelNo(s[0]);
//                            trafficLog.setPerson(person);
//                            trafficLog.setOrgan(person.getOrganRef());
//                        }
////                        if (Initializer.connections.get(trafficLog.getPdp().getId()) == null) {
////                            Initializer.connections.put(trafficLog.getPdp().getId(), socket);
////                        } else {
////                            socket.close();
////                        }
//                        trafficLogService.createTrafficLog(trafficLog);
//
//                    }
//                } else if (messageType[0].equalsIgnoreCase("off")) {
                new Thread(new OfflineManager(socket, trafficLogService, receivedMSG, personService, crossingService,bufferedReader)).start();
            }

//            }
        } catch (IOException e) {
//            new Thread(new ConsoleServerSocket()).start();
            e.printStackTrace();
            try {
                server.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            new Thread(new ConsoleServerSocket(crossingService, trafficLogService, personService)).start();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        }
    }

    private void createPicture(TrafficLog trafficLog) throws IOException {
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
}
