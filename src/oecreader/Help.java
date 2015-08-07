package oecreader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Help extends JFrame {

	GUI gui;
	
	public Help(GUI gui){
		super("Help");
		
		try {
			Image i = ImageIO.read(this.getClass().getResourceAsStream("res/icon.png"));
			this.setIconImage(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - 640 / 2, dim.height / 2 - 360 / 2);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(gui.background);
		
		this.gui = gui;
		
		initComponents();
	}
	
	public void initComponents(){
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		content.setBackground(gui.background);
		add(content, BorderLayout.CENTER);
		
		content.add(Box.createRigidArea(new Dimension(0, 50)));
		
		JLabel title = new JLabel("Help");
		title.setFont(gui.h1);
		title.setForeground(gui.h1color);
		title.setAlignmentX(CENTER_ALIGNMENT);
		content.add(title);
		
		JLabel text = new JLabel("It's really self-explanatory.");
		text.setFont(gui.font);
		text.setAlignmentX(CENTER_ALIGNMENT);
		content.add(text);
		
		JLabel fat = new JLabel("Raymond is a fatty.");
		fat.setFont(gui.font);
		fat.setAlignmentX(CENTER_ALIGNMENT);
		content.add(fat);
		
		content.add(Box.createRigidArea(new Dimension(0, 50)));
		
		JLabel c = new JLabel("Made by Jason Yang.");
		c.setFont(gui.font);
		c.setAlignmentX(CENTER_ALIGNMENT);
		content.add(c);
	}
	
}
