package ir.university.toosi.tms.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.service.CrossingServiceImpl;
import ir.university.toosi.tms.model.service.TrafficLogServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import java.io.*;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OfflineManager implements Runnable {
    private Socket socket;
    TrafficLog trafficLog;
    TrafficLogServiceImpl trafficLogService;
    PersonServiceImpl personService;
    CrossingServiceImpl crossingService;
    DataInputStream dataInputStream;
    BufferedReader bufferedReader;
    String message;

    public OfflineManager(Socket socket, TrafficLogServiceImpl trafficLogService, String message, PersonServiceImpl personService, CrossingServiceImpl crossingService, BufferedReader bufferedReader) {
        this.socket = socket;
        this.trafficLogService = trafficLogService;
        this.message = message;
        this.personService = personService;
        this.crossingService = crossingService;
        this.bufferedReader = bufferedReader;
//        try {
//            dataInputStream = new DataInputStream(socket.getInputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void run() {
        Person person;
        boolean done = true;
        try {
            socket.getOutputStream().write(pdpSync());
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // rq 272,1,1393031124112344,1

        while (done) {
            String[] messageType = message.split(" ");
            String[] messageContent = messageType[1].split(",");
            try {
                if (messageType[0].equalsIgnoreCase("rq")) {
//                    System.out.println("1111111111111");
                    String d = socket.getInetAddress().toString().replace("/", "");
                    trafficLog = crossingService.authorize(d, messageContent[0], messageContent[3], 0);
//                    System.out.println("33333333333333333");
                    if (trafficLog != null) {
                        if (trafficLog.isValid()) {
//                            socket.getOutputStream().write(("rsc 0,1").getBytes());
//                            System.out.println("send true");
//                            socket.getOutputStream().flush();

                            if (trafficLog.getPdp().getCamera() != null) {
                                createPicture(trafficLog);
                            }
                            person = personService.getPersonByPersonOtherId(messageContent[0]);
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
//                            socket.getOutputStream().write(("rsc 1,1").getBytes());
//                            System.out.println("send false");
//                            socket.getOutputStream().flush();
//                            socket.getOutputStream().write(pdpSync());
//                            socket.getOutputStream().flush();
                            if (trafficLog.getPdp().getCamera() != null) {
                                createPicture(trafficLog);
                            }
                            person = personService.getPersonByPersonOtherId(messageContent[0]);
                            trafficLog.setPerson(person);
                            trafficLog.setOrgan(person.getOrganRef());
                        }
//                        if (Initializer.connections.get(trafficLog.getPdp().getId()) == null) {
//                            Initializer.connections.put(trafficLog.getPdp().getId(), socket);
//                        } else {
//                            socket.close();
//                        }
                        trafficLogService.createTrafficLog(trafficLog);
                    }
                    new RESTfulClientUtil().restFullService("http://" + Configuration.getProperty("server.ip") + ":" + Configuration.getProperty("kernel.port") + "/WTMS/restful/TMSService", "/Cross", new ObjectMapper().writeValueAsString(trafficLog));

                } else {
                    trafficLog = trafficLogService.offline(socket.getInetAddress().toString().replace("/", ""), messageContent[0], messageContent[1], messageContent[2]);
                    if (trafficLog != null && trafficLog.isValid()) {
                      //  socket.getOutputStream().write(("off ok," + messageContent[0] + "\r\n").getBytes());
//                        socket.getOutputStream().flush();
                        Thread.sleep(1000);

                    } else {

//                        socket.getOutputStream().write(("off notok," + messageContent[0] + "\r\n").getBytes());
//                        socket.getOutputStream().flush();
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
//                if(bufferedReader.)
                message = bufferedReader.readLine();
                if (message != null) {
                    System.out.println(message);
                    if (message.split(" ")[1].equalsIgnoreCase("end")) {
                        done = false;
                    }
                } else {
                    done = false;

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                done = false;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                done = false;
            } catch (IOException e) {
                e.printStackTrace();
                done = false;
            } catch (Exception e) {
                e.printStackTrace();
                done = false;
            }
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

    public byte[] pdpSync() {
        Date dt = new Date();
        byte[] data = new byte[8];
        String date = CalendarUtil.getDateWithoutSlash(new Date(), Locale.US, "YYMMdd");
        data[1] = (byte) ((Integer.valueOf(date.substring(2, 4))) >> 8);
        data[0] = (byte) ((Integer.valueOf(date.substring(2, 4))) - 100 & 0xff);
        data[2] = ((Integer.valueOf(date.substring(4, 6))).byteValue());
        data[3] = ((Integer.valueOf(date.substring(6, 8))).byteValue());
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        data[4] = (byte) (dayOfWeek + 1);
        data[5] = (byte) dt.getHours();
        data[6] = (byte) dt.getMinutes();
        data[7] = (byte) dt.getSeconds();

        byte[] b = new byte[]{0x40, 0x03, 0x00, 0x00, 0x00, 0x00, 0x08, 0x00, data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], 0x11, 0x0A};
        return b;

    }
}
