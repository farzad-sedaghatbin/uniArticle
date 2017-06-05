package ir.university.toosi.tms.util;

import java.io.BufferedOutputStream;
import java.io.OutputStream;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 1.0
 */

public class ConsoleClientSocket {

    public static void sendMessage(String message, OutputStream outputStream) {
        try {
            BufferedOutputStream bufferedWriter = new BufferedOutputStream(outputStream);
            bufferedWriter.write(message.getBytes("UTF-8"));
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
