package ir.university.toosi.tms.util;


import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author a_hadadi
 */


public class ImageUtils {

    public static BufferedImage convertByteArrayToBufferedImage(byte[] imageBytes) {
        if (imageBytes == null) {
            return null;
        }
        try {
            return ImageIO.read(new ByteArrayInputStream(imageBytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] imageToByteArray(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return baos.toByteArray();
    }


    public static float calculateScaleRatio(int originalWidth, int targetWidth) {
        //calculate  ratio for scaling
        return (float) targetWidth / originalWidth;
    }

    public static BufferedImage scaleImage(BufferedImage bufferedImage, float scaleRatio) throws IOException {
        //apply scale
        return Thumbnails.of(bufferedImage).scale(scaleRatio).asBufferedImage();
    }

    public static ImageIcon convertToImageIcon(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            return null;
        }
        return new ImageIcon(bufferedImage);
    }

    public static BufferedImage getBufferedImage(File tempFile) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(tempFile);
        } catch (IOException e) {
            System.out.println("cant convert file to Buffered image" + tempFile.getPath());
            e.printStackTrace();
        }
        return bufferedImage;
    }


}
