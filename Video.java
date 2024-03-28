import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

public class Video {

    public static void main(String[] args) {
         // This Java code snippet is capturing an image from the default webcam connected to the
         // system. Here's a breakdown of what each part does:
         Webcam webcam = Webcam.getDefault();
        webcam.open();
        java.awt.image.BufferedImage image = webcam.getImage();

        // Save the image (you can customize the filename)
        File outputFile = new File("webcam_image.jpg");
        try {
            ImageIO.write(image, "JPG", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Close the webcam
        webcam.close();
    }
    
}
