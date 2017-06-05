package ir.university.toosi.tms.model.service;

import ir.university.toosi.tms.model.entity.zone.Camera;
import ir.university.toosi.tms.util.Configuration;
import org.apache.commons.io.IOUtils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
public class PhotoServiceImpl {


    public boolean takePhoto(Camera camera, int delay, String address) {
        try {
            long frames = camera.getFrames();
            while (frames > 0) {
                frames--;
                connect(camera, address);
                disconnect(camera);
            }

        } catch (Exception e) {
            ;
        }


        return true;
    }


    private void connect(Camera camera, String address) {
        String jpgURL = "http://" + camera.getIp() + "/cgi-bin/video.jpg";
        HttpURLConnection huc = null;
        try {
//            URL u = new URL(useMJPGStream ? mjpgURL : jpgURL);
            URL u = new URL(jpgURL);
            Authenticator a = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("portal", "portal".toCharArray());
                }
            };
            Authenticator.setDefault(a);
            huc = (HttpURLConnection) u.openConnection();
            // System.out.println(huc.getContentType());
            InputStream is = huc.getInputStream();
            camera.setConnected(true);
            System.out.println("axid");
            File folder = new File(Configuration.getProperty("pic.person") + address);
            if (!folder.exists()) {
                folder.mkdir();
            }
            int i = 1;
            File file = new File(Configuration.getProperty("pic.person") + address + "/" + i + ".jpg");
            while (file.exists()) {
                i++;
                file = new File(Configuration.getProperty("pic.person") + address + "/" + i + ".jpg");
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            fileOutputStream.write(IOUtils.toByteArray(is));
            fileOutputStream.flush();
            fileOutputStream.close();

//            dis.readFull
//            FileOutputStream stream=new FileOutputStream();

            huc.disconnect();
        } catch (IOException e) { // incase no connection exists wait and try
            // again, instead of printing the error
            e.printStackTrace();

            huc.disconnect();
        }
    }

    public byte[] connect(String ip) {
        String jpgURL = "http://" + ip + "/cgi-bin/video.jpg";
        HttpURLConnection huc = null;
        try {
//            URL u = new URL(useMJPGStream ? mjpgURL : jpgURL);
            URL u = new URL(jpgURL);
            Authenticator a = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("portal", "portal".toCharArray());
                }
            };
            Authenticator.setDefault(a);
            huc = (HttpURLConnection) u.openConnection();
            // System.out.println(huc.getContentType());
            InputStream is = huc.getInputStream();


//            dis.readFull
//            FileOutputStream stream=new FileOutputStream();
            byte[] result = IOUtils.toByteArray(is);
            huc.disconnect();
            return result;
        } catch (IOException e) { // incase no connection exists wait and try
            // again, instead of printing the error
            huc.disconnect();
            return new byte[0];
        }
    }


    public void disconnect(Camera camera) {
        try {
            if (camera.isConnected()) {

                camera.setConnected(false);
            }
        } catch (Exception e) {
            ;
        }
    }


}