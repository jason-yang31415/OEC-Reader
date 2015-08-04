package oecreader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class GUI extends JFrame {

	Font font;
	Color background;
	
	public JPanel search;
	public JTextField searchBar;
	public JPanel searchBarPanel;
	public JButton searchButton;
	
	public JPanel results;
	
	public GUI(){
		super("Open Exoplanet Catalogue");
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - 960 / 2, dim.height / 2 - 540 / 2);
		
		font = new Font("Century Gothic", Font.ROMAN_BASELINE, 14);
		background = Color.decode("#f0f0ff");
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		getContentPane().setBackground(background);
		
		initComponents();
	}
	
	public void initComponents(){
		try {
			search = new JPanel();
			search.setBorder(BorderFactory.createTitledBorder(null, "Search", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, font, Color.BLACK));
			search.setMaximumSize(new Dimension(750, 100));
			search.setBackground(background);
			
			searchBarPanel = new JPanel();
			searchBarPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#555555"), 2));
			searchBarPanel.setBackground(Color.WHITE);
			
			searchBar = new JTextField(40);
			searchBar.setBorder(BorderFactory.createEmptyBorder());
			searchBar.setFont(font);
			searchBar.addActionListener(enter);
			
			searchBarPanel.add(searchBar);
			
			searchButton = new JButton();
			Image img = ImageIO.read(this.getClass().getResourceAsStream("res/search.png"));
			ImageIcon icon = new ImageIcon(img);
			searchButton.setIcon(icon);
			searchButton.setPreferredSize(new Dimension(60, 35));
			searchButton.addActionListener(enter);
			
			search.add(searchBarPanel);
			search.add(searchButton);
			add(search);
			
			results = new JPanel();
			results.setBackground(background);
			results.setBorder(BorderFactory.createTitledBorder(null, "Results", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, font, Color.BLACK));
			results.setMaximumSize(new Dimension(750, 540));
			add(results);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	Action enter = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent e) {
			Boot.boot.loop("search " + searchBar.getText());
		}
	};
	
}
