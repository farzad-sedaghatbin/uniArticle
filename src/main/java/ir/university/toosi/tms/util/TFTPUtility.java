package ir.university.toosi.tms.util;

import org.apache.commons.net.tftp.TFTP;
import org.apache.commons.net.tftp.TFTPClient;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;

public class TFTPUtility {

    public static void put(String host, String file, String remoteFileName) throws Exception {
        int transferMode = TFTP.BINARY_MODE;
        TFTPClient tftp;


        // Create our TFTP instance to handle the file transfer.
        tftp = new TFTPClient();
        // We want to timeout if a response takes longer than 60 seconds
        tftp.setDefaultTimeout(1000);
        // Open local socket
        tftp.open();

        // We haven't closed the local file yet.

        // If we're receiving a file, receive, otherwise send.

        // We're sending a file
        FileInputStream input = null;

        // Try to open local file for reading
        input = new FileInputStream(file);

        // Try to send local file via TFTP
        tftp.sendFile(remoteFileName, transferMode, input, host, 1234);
        tftp.close();

    }

    public static byte[] get(String hostname, String remoteFilename) {
        boolean closed;
        int transferMode = TFTP.BINARY_MODE;
        TFTPClient tftp;

        // Create our TFTP instance to handle the file transfer.
        tftp = new TFTPClient();

        // We want to timeout if a response takes longer than 60 seconds
        tftp.setDefaultTimeout(2000);

        // Open local socket
        try {
            tftp.open();
        } catch (SocketException e) {
            System.err.println("Error: could not open local UDP socket.");
            System.err.println(e.getMessage());
            System.exit(1);
        }

        // We haven't closed the local file yet.
        closed = false;

        // If we're receiving a file, receive, otherwise send.
        ByteArrayOutputStream output = new ByteArrayOutputStream();


        // Try to receive remote file via TFTP
        try {
            tftp.receiveFile(remoteFilename, transferMode, output, hostname, 1234);
        } catch (UnknownHostException e) {
            System.err.println("Error: could not resolve hostname.");
            System.err.println(e.getMessage());
        } catch (IOException e) {
        } finally {
            // Close local socket and output file
            tftp.close();
        }
        return output.toByteArray();
    }

    public static void main(String[] args) {
        Date dt =Calendar.getInstance().getTime();
        byte[] data = new byte[8];
        data[1] = (byte) (dt.getYear() - 100 >> 8);
        data[0] = (byte) (dt.getYear() - 100 & 0xff);
        data[2] = (byte) (dt.getMonth()+1);
        data[3] = (byte) dt.getDay();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        data[4] = (byte) (dayOfWeek + 1);
        data[5] = (byte) dt.getHours();
        data[6] = (byte) dt.getMinutes();
        data[7] = (byte) dt.getSeconds();
    }

}
