package ir.university.toosi.tms.util;

import ir.university.toosi.tms.model.entity.zone.Camera;
import net.sf.jipcam.axis.MjpegFrame;
import net.sf.jipcam.axis.MjpegInputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

public class GuestCameraUtil {
    private boolean running = true;
    private int x = 10, y = 10;
    private Dimension size;
    private JFrame frame;
    private static URL url;


    public static byte[] capture() {
        try {

            url = new URL("http://" + Configuration.getProperty("guestCamera") + "/image.jpg");
            String pos = "50x30";
            int x = 0, y = 0, width = 640, height = 510;

            Authenticator.setDefault(new HTTPAuthenticator(Configuration.getProperty("user"), Configuration.getProperty("pass")));
//            ConvertedCameraUtil viewer = new ConvertedCameraUtil();
//            viewer.setLocation(x, y);
//            viewer.setSize(width, height);
            MjpegInputStream m = new MjpegInputStream(url.openStream());
            MjpegFrame f;
            f = m.readMjpegFrame();
//                    ip.setImage((BufferedImage) f.getImage());
            return f.getJpegBytes();


        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
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
