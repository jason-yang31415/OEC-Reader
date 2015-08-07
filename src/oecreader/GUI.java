package oecreader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class GUI extends JFrame {

	Font font;
	Font h1, h2, h3;
	Color background;
	Color h1color, h2color, h3color;
	
	public JButton sync;
	
	public JCheckBox sysCheck, starCheck, pCheck;
	public boolean sysChecked = true;
	public boolean starChecked = true;
	public boolean pChecked = true;
	
	public JPanel search;
	public JTextField searchBar;
	public JPanel searchBarPanel;
	public JButton searchButton;
	
	public JPanel results;
	public JScrollPane scrollFrame;
	
	public MouseListener ml = new MouseListener();
	
	public GUI(){
		super("Open Exoplanet Catalogue");
		
		try {
			Image i = ImageIO.read(this.getClass().getResourceAsStream("res/icon.png"));
			this.setIconImage(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		
		setLayout(new BorderLayout());
		getContentPane().setBackground(background);
		
		initComponents();
	}
	
	public void initComponents(){
		try {
			JPanel options = new JPanel();
			options.setLayout(new BorderLayout());
			options.setBackground(background);
			add(options, BorderLayout.WEST);
			
			JPanel top_buttons = new JPanel();
			top_buttons.setLayout(new BoxLayout(top_buttons, BoxLayout.PAGE_AXIS));
			top_buttons.setBackground(background);
			options.add(top_buttons, BorderLayout.CENTER);
			
			sync = new JButton();
			sync.setFocusable(false);
			Image sync_img = ImageIO.read(this.getClass().getResourceAsStream("res/sync.png"));
			ImageIcon sync_icon = new ImageIcon(sync_img);
			Image sync_hover_img = ImageIO.read(this.getClass().getResourceAsStream("res/sync_hover.png"));
			ImageIcon sync_hover_icon = new ImageIcon(sync_hover_img);
			sync.setIcon(sync_icon);
			sync.setPreferredSize(new Dimension(30, 30));
			sync.setContentAreaFilled(false);
			sync.addMouseListener(new MouseAdapter(){
				
				public void mouseEntered(java.awt.event.MouseEvent evt){
					sync.setIcon(sync_hover_icon);
				}
				
				public void mouseExited(java.awt.event.MouseEvent evt){
					sync.setIcon(sync_icon);
				}
				
			});
			sync.addActionListener(new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Boot.boot.update();
					Boot.boot.parse();
					Boot.boot.serialize();
				}
				
			});
			top_buttons.add(sync);
			
			top_buttons.add(Box.createRigidArea(new Dimension(0, 25)));
			
			sysCheck = new JCheckBox("system", true);
			sysCheck.setBackground(background);
			sysCheck.setFocusable(false);
			
			starCheck = new JCheckBox("star", true);
			starCheck.setBackground(background);
			starCheck.setFocusable(false);
			
			pCheck = new JCheckBox("planet", true);
			pCheck.setBackground(background);
			pCheck.setFocusable(false);
			
			top_buttons.add(sysCheck);
			top_buttons.add(starCheck);
			top_buttons.add(pCheck);
			
			CheckListener cl = new CheckListener();
			sysCheck.addActionListener(cl);
			starCheck.addActionListener(cl);
			pCheck.addActionListener(cl);
			
			JPanel bottom_buttons = new JPanel();
			bottom_buttons.setLayout(new BoxLayout(bottom_buttons, BoxLayout.PAGE_AXIS));
			bottom_buttons.setBackground(background);
			options.add(bottom_buttons, BorderLayout.SOUTH);
			
			JButton help = new JButton();
			help.setFocusable(false);
			Image help_img = ImageIO.read(this.getClass().getResourceAsStream("res/help.png"));
			ImageIcon help_icon = new ImageIcon(help_img);
			Image help_hover_img = ImageIO.read(this.getClass().getResourceAsStream("res/help_hover.png"));
			ImageIcon help_hover_icon = new ImageIcon(help_hover_img);
			help.setIcon(help_icon);
			help.setPreferredSize(new Dimension(30, 30));
			help.setContentAreaFilled(false);
			help.addMouseListener(new MouseAdapter(){
				
				public void mouseEntered(java.awt.event.MouseEvent evt){
					help.setIcon(help_hover_icon);
				}
				
				public void mouseExited(java.awt.event.MouseEvent evt){
					help.setIcon(help_icon);
				}
				
			});
			help.addActionListener(new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Boot.boot.help();
				}
				
			});
			bottom_buttons.add(help);
			
			bottom_buttons.add(Box.createRigidArea(new Dimension(0, 10)));
			
			
			
			JPanel content = new JPanel();
			content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
			content.setBackground(background);
			add(content, BorderLayout.CENTER);
			
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
			content.add(search);
			
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
			
			content.add(resultsPane);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void displayResults(ArrayList<Data> r, boolean clip){
		results.removeAll();
		
		for (Data d : r){
			Result panel = new Result(d);
			panel.setLayout(new GridLayout(0, 1));
			panel.setBackground(background);
			panel.setBorder(BorderFactory.createTitledBorder(null, d.getType(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, font, Color.BLACK));
			panel.setMaximumSize(new Dimension(700, 540));
			panel.addMouseListener(ml);
			
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
			ArrayList<Data> matches = Boot.boot.search(searchBar.getText(), sysChecked, starChecked, pChecked);
			
			if (matches.size() == 1)
				Boot.boot.displayData(matches.get(0));
			
			if (matches.size() > 15){
				ArrayList<Data> sub_matches = new ArrayList<Data>(matches.subList(0, 15));
				displayResults(sub_matches, true);
			}
			else
				displayResults(matches, false);
		}
	};
	
	public class MouseListener extends MouseAdapter {
		
		public void mousePressed(MouseEvent me){
			Result r = (Result) me.getSource();
			Boot.boot.displayData(r.getData());
		}
		
	}
	
	public class CheckListener extends AbstractAction {
	 
	    @Override
	    public void actionPerformed(ActionEvent event){
	        JCheckBox box = (JCheckBox) event.getSource();
	        if (box == sysCheck)
	        	sysChecked = box.isSelected();
	        else if (box == starCheck)
	        	starChecked = box.isSelected();
	        else if (box == pCheck)
	        	pChecked = box.isSelected();
	    }
	    
	}

	
}
