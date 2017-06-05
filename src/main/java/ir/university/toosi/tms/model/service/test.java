package ir.university.toosi.tms.model.service;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class test {

    public test() {
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        InetAddress inet;

        inet = InetAddress.getByName("192.168.1.1");
        System.out.println("Sending Ping Request to " + inet);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");

        inet = InetAddress.getByName("192.168.1.8");
        System.out.println("Sending Ping Request to " + inet);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");
    }
}
