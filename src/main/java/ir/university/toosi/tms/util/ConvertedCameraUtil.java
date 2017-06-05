package ir.university.toosi.tms.util;

import ir.university.toosi.tms.model.entity.zone.Camera;
import net.sf.jipcam.axis.MjpegFrame;
import net.sf.jipcam.axis.MjpegInputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

public class ConvertedCameraUtil {
    private boolean running = true;
    private int x = 10, y = 10;
    private Dimension size;
    private JFrame frame;
    private static URL url;


    public static void capture(Camera camera, String picName) {
        try {

            if(camera == null){
                return;
            }
            url = new URL("http://" + camera.getIp() + "/cgi/mjpg/mjpg.cgi");
            String pos = "50x30";
            int x = 0, y = 0, width = 640, height = 510;

            Authenticator.setDefault(new HTTPAuthenticator(camera.getUserName(), camera.getPassword()));
//            ConvertedCameraUtil viewer = new ConvertedCameraUtil();
//            viewer.setLocation(x, y);
//            viewer.setSize(width, height);
            MjpegInputStream m = new MjpegInputStream(url.openStream());
            MjpegFrame f;
            f = m.readMjpegFrame();
//                    ip.setImage((BufferedImage) f.getImage());

            File file = new File(Configuration.getProperty("pic.person") + picName + "/" + 1 + ".jpg");
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdir();
            }

            ImageIO.write((BufferedImage) f.getImage(), "jpg", file);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setSize(int width, int height) {
        size = new Dimension(width, height);
        if (frame != null)
            frame.setSize(size);
    }

    private void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        if (frame != null)
            frame.setLocation(x, y);
    }


    static class HTTPAuthenticator extends Authenticator {
        private String username, password;

        public HTTPAuthenticator(String user, String pass) {
            username = user;
            password = pass;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password.toCharArray());
        }
    }
}
