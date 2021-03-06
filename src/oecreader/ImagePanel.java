package oecreader;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private BufferedImage image;
	int width, height;
	
	public ImagePanel(String dir) {
		try {
			image = ImageIO.read(new File(dir));
			width = 150;
			float factor = width / (float) image.getWidth();
			height = (int) (image.getHeight() * factor);
			setPreferredSize(new Dimension(width, height));
		} catch (IOException ex) {
			System.err.println("Please update / sync with the OEC Database");
			System.err.println("See the wiki for more info");
			System.err.println("     wiki >> How to use OEC Reader >> v0.3.0 and After");
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, width, height, null); // see javadoc for more info on the parameters            
	}
	
	public int getImageWidth(){
		return width;
	}
	
	public int getImageHeight(){
		return height;
	}
	
}
