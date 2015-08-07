package oecreader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class GUI extends JFrame {

	Font font;
	Font h1, h2, h3;
	Color background;
	Color h1color, h2color, h3color;
	
	public JPanel search;
	public JTextField searchBar;
	public JPanel searchBarPanel;
	public JButton searchButton;
	
	public JPanel results;
	public JScrollPane scrollFrame;
	
	public GUI(){
		super("Open Exoplanet Catalogue");
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - 960 / 2, dim.height / 2 - 540 / 2);
		
		font = new Font("Century Gothic", Font.ROMAN_BASELINE, 14);
		h1 = new Font("Century Gothic", Font.BOLD, 30);
		h2 = new Font("Century Gothic", Font.BOLD, 24);
		h3 = new Font("Century Gothic", Font.ITALIC, 18);
		background = Color.decode("#ffffff"); //#f0f0ff
		h1color = Color.decode("#0038ba");
		h2color = Color.decode("#0038ba");
		h3color = Color.decode("#000000");
		
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
			results.setLayout(new GridLayout(0, 1, 0, 15));
			results.setBackground(background);
			results.setMaximumSize(new Dimension(750, 540));
			
			scrollFrame = new JScrollPane(results);
			scrollFrame.setPreferredSize(new Dimension(740, 400));
			scrollFrame.setBorder(BorderFactory.createEmptyBorder());
			scrollFrame.getVerticalScrollBar().setUnitIncrement(5);
			results.setAutoscrolls(true);
			
			JPanel resultsPane = new JPanel();
			resultsPane.setBackground(background);
			resultsPane.setBorder(BorderFactory.createTitledBorder(null, "Results", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, font, Color.BLACK));
			resultsPane.setMaximumSize(new Dimension(750, 540));
			resultsPane.add(scrollFrame);
			
			add(resultsPane);
			
			//add(results);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void displayResults(ArrayList<Data> r, boolean clip){
		results.removeAll();
		
		for (Data d : r){
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(0, 1));
			panel.setBackground(background);
			panel.setBorder(BorderFactory.createTitledBorder(null, d.getType(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, font, Color.BLACK));
			panel.setMaximumSize(new Dimension(700, 540));
			
			JLabel name = new JLabel(d.names.get(0));
			name.setFont(h2);
			name.setForeground(h2color);
			panel.add(name);
			
			String nameLong;
			if (d.name.length() > 80)
				nameLong = d.name.substring(0, 80) + " ...";
			else
				nameLong = d.name;
			JLabel name_long = new JLabel(nameLong);
			name_long.setFont(font);
			panel.add(name_long);
			
			results.add(panel);
		}
		
		if (clip){
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(0, 1));
			panel.setBackground(background);
			panel.setBorder(BorderFactory.createEmptyBorder());
			panel.setMaximumSize(new Dimension(700, 540));
			
			JLabel label = new JLabel("More results... (not shown)", SwingConstants.CENTER);
			label.setFont(h3);
			label.setForeground(h3color);
			panel.add(label);
			
			results.add(panel);
		}
		
		revalidate();
		repaint();
	}
	
	Action enter = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<Data> matches = Boot.boot.search(searchBar.getText(), true, true, true);
			if (matches.size() > 15){
				ArrayList<Data> sub_matches = new ArrayList<Data>(matches.subList(0, 15));
				displayResults(sub_matches, true);
			}
			else
				displayResults(matches, false);
		}
	};
	
}
