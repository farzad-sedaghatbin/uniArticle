package ir.university.toosi.tms.util;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by farzad on 12/18/2015.
 */
public class WebCamutil {


    public static void main(String[] args) {
        Webcam webcam = Webcam.getDefault();
        webcam.open();

        // get image
        BufferedImage image = webcam.getImage();

        // save image to PNG file
        try {
            ImageIO.write(image, "PNG", new File("d:/test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
