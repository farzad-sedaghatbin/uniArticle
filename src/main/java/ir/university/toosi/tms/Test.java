package ir.university.toosi.tms;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by farzad on 7/1/14.
 */
public class Test {
    public static void main(String[] args) {
        try {
            Socket s= new Socket("192.168.161.69",6337);
            s.getOutputStream().write("\nrq 1,0,1393031124112344,0".getBytes());
            s.getOutputStream().flush();
//            Thread.sleep(10000);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        }

//        String jpgURL = "http://192.168.0.25/cgi-bin/video.jpg";
//        String userPass = "portal:portal";
//        HttpURLConnection huc = null;
//        try {
////            URL u = new URL(useMJPGStream ? mjpgURL : jpgURL);
//            URL u = new URL(jpgURL);
//            Authenticator a = new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication("portal", "portal".toCharArray());
//                }
//            };
//            Authenticator.setDefault(a);
//            huc = (HttpURLConnection) u.openConnection();
//            // System.out.println(huc.getContentType());
//            InputStream is = huc.getInputStream();
//
//            BufferedInputStream bis = new BufferedInputStream(is);
//
//            InputStreamReader inputStreamReader = new InputStreamReader(is);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            System.out.println("axid");
//            File folder = new File("e:\\pdp\\");
//            if (!folder.exists()) {
//                folder.mkdir();
//            }
//            int i = 1;
//            File file = new File("e:\\pdp\\" + i + ".png");
//
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//
//            fileOutputStream.write(IOUtils.toByteArray(is));
//            fileOutputStream.flush();
//            fileOutputStream.close();
//
////            dis.readFull
////            FileOutputStream stream=new FileOutputStream();
//
//            huc.disconnect();
//        } catch (IOException e) { // incase no connection exists wait and try
//            // again, instead of printing the error
//            e.printStackTrace();
//
//            huc.disconnect();
//        }


//        String s="13921006192514";
//        System.out.println(s);
    }

}
