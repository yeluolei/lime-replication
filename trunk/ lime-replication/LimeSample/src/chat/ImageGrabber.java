package chat;


import javax.swing.*;
import java.net.*;
import java.awt.*;

/**
 * ImageGrabber.java
 *
 * @author Chien-Liang Fok
 */

public class ImageGrabber
{
	
	/**
	 * Returns an image icon of the specified image file.
	 */
    public static ImageIcon getImageIcon(String imgName, String caption) {
		
		URL newURL = ImageGrabber.class.getResource(imgName);
		Image newImage = Toolkit.getDefaultToolkit().getImage(newURL);
		return new ImageIcon(newImage, caption);
    }
	
	/**
	 * Returns an image icon of the specified image file.
	 */
    public static ImageIcon getImageIcon(String imgName) {
		URL newURL = ImageGrabber.class.getResource(imgName);
		Image newImage = Toolkit.getDefaultToolkit().getImage(newURL);
		return new ImageIcon(newImage);
    }
}


