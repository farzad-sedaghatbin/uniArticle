package ir.university.toosi.tms;

import ir.university.toosi.tms.util.Configuration;

import java.net.Socket;

public class ConsoleClientSocket {

    public static void sendMessage(byte[] message, String ip) {
        try {
            Socket s= new Socket(ip, Integer.valueOf(Configuration.getProperty("server.port")));
            s.getOutputStream().write(message);
            s.getOutputStream().flush();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}