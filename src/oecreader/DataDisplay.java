package oecreader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class DataDisplay extends JFrame {

	Data d;
	GUI gui;
	
	JTree tree;
	Dimension treeDim = new Dimension(160, 405);
	
	MouseListener ml = new MouseListener();

	
	public DataDisplay(Data d, GUI gui){
		super(d.names.get(0));
		
		try {
			Image i = ImageIO.read(this.getClass().getResourceAsStream("res/icon.png"));
			this.setIconImage(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 3 - 720 / 2, dim.height / 3 - 405 / 2);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(gui.background);
		
		this.d = d;
		this.gui = gui;
		
		if (d instanceof StarSystem)
			displaySystemData((StarSystem) d);
		
		else if (d instanceof Star)
			displayStarData((Star) d);
		
		else if (d instanceof Planet)
			displayPlanetData((Planet) d);
	}
	
	public void displaySystemData(StarSystem sys){
		TreeNode sysTree = new TreeNode(sys);
		tree = new JTree(sysTree);
		tree.setShowsRootHandles(true);
		tree.addMouseListener(ml);
		
		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setBackground(gui.background);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setPreferredSize(treeDim);
		add(scrollPane, BorderLayout.WEST);
		
		for (Star star : sys.stars){
			TreeNode starTree = new TreeNode(star);
			sysTree.add(starTree);
		}
		
		for (Planet p : sys.planets){
			TreeNode pTree = new TreeNode(p);
			sysTree.add(pTree);
		}
		
		JPanel content = new JPanel();
		content.setBackground(gui.background);
		content.setLayout(new BorderLayout());
		add(content, BorderLayout.CENTER);
		
		JPanel title = new JPanel();
		title.setBackground(gui.background);
		title.setLayout(new BorderLayout());
		content.add(title, BorderLayout.NORTH);
		
		JLabel type = new JLabel("System");
		type.setFont(gui.font);
		title.add(type, BorderLayout.NORTH);
		
		JLabel name = new JLabel(sys.names.get(0));
		name.setFont(gui.h1);
		name.setForeground(gui.h1color);
		title.add(name, BorderLayout.CENTER);
		
		JLabel name_long = new JLabel(sys.name);
		name_long.setFont(gui.h3);
		type.setForeground(gui.h3color);
		title.add(name_long, BorderLayout.SOUTH);
		
		JPanel data = new JPanel();
		data.setBackground(gui.background);
		data.setLayout(new BoxLayout(data, BoxLayout.PAGE_AXIS));
		content.add(data, BorderLayout.CENTER);
		
		data.add(Box.createRigidArea(new Dimension(0, 30)));
		
		JLabel radec = new JLabel("RA, Dec: " + sys.ra + ", " + sys.dec);
		radec.setFont(gui.font);
		data.add(radec);
		
		JLabel distance = new JLabel("Distance: " + sys.distance + Boot.boot.getUnits("distance"));
		distance.setFont(gui.font);
		data.add(distance);
	}
	
	public void displayStarData(Star star){
		TreeNode sysTree = new TreeNode(star.parent);
		tree = new JTree(sysTree);
		tree.setShowsRootHandles(true);
		tree.addMouseListener(ml);
		
		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setBackground(gui.background);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setPreferredSize(treeDim);
		add(scrollPane, BorderLayout.WEST);
		
		for (Star s : star.parent.stars){
			TreeNode starTree = new TreeNode(s);
			sysTree.add(starTree);
		}
		
		for (Planet p : star.parent.planets){
			TreeNode pTree = new TreeNode(p);
			sysTree.add(pTree);
		}
		
		JPanel content = new JPanel();
		content.setBackground(gui.background);
		content.setLayout(new BorderLayout());
		add(content, BorderLayout.CENTER);
		
		JPanel title = new JPanel();
		title.setBackground(gui.background);
		title.setLayout(new BorderLayout());
		content.add(title, BorderLayout.NORTH);
		
		JLabel type = new JLabel("Star");
		type.setFont(gui.font);
		title.add(type, BorderLayout.NORTH);
		
		JLabel name = new JLabel(star.names.get(0));
		name.setFont(gui.h1);
		name.setForeground(gui.h1color);
		title.add(name, BorderLayout.CENTER);
		
		JLabel name_long = new JLabel(star.name);
		name_long.setFont(gui.h3);
		type.setForeground(gui.h3color);
		title.add(name_long, BorderLayout.SOUTH);
		
		JPanel data = new JPanel();
		data.setBackground(gui.background);
		data.setLayout(new BoxLayout(data, BoxLayout.PAGE_AXIS));
		content.add(data, BorderLayout.CENTER);
		
		data.add(Box.createRigidArea(new Dimension(0, 30)));
		
		if (star.mass != null){
			JLabel mass = new JLabel("Mass: " + star.mass +  Boot.boot.getUnits("mass_star"));
			mass.setFont(gui.font);
			data.add(mass);
		}
		if (star.radius != null){
			JLabel radius = new JLabel("Radius: " + star.radius + Boot.boot.getUnits("radius_star"));
			radius.setFont(gui.font);
			data.add(radius);
		}
		if (star.temp != null){
			JLabel temp = new JLabel("Temp: " + star.temp + Boot.boot.getUnits("temp"));
			temp.setFont(gui.font);
			data.add(temp);
		}
		if (star.magV != null){
			JLabel magV = new JLabel("Visual Mag: " + star.magV);
			magV.setFont(gui.font);
			data.add(magV);
		}
		if (star.age != null){
			JLabel age = new JLabel("Age: " + star.age + Boot.boot.getUnits("age"));
			age.setFont(gui.font);
			data.add(age);
		}
		if (star.spectraltype != null){
			JLabel spectraltype = new JLabel("Spectral Type: " + star.spectraltype);
			spectraltype.setFont(gui.font);
			data.add(spectraltype);
		}
	}
	
	public void displayPlanetData(Planet p){
		TreeNode sysTree = new TreeNode(p.parent);
		tree = new JTree(sysTree);
		tree.setShowsRootHandles(true);
		tree.addMouseListener(ml);
		
		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setBackground(gui.background);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setPreferredSize(treeDim);
		add(scrollPane, BorderLayout.WEST);
		
		for (Star s : p.parent.stars){
			TreeNode starTree = new TreeNode(s);
			sysTree.add(starTree);
		}
		
		for (Planet pl : p.parent.planets){
			TreeNode pTree = new TreeNode(pl);
			sysTree.add(pTree);
		}
		
		JPanel content = new JPanel();
		content.setBackground(gui.background);
		content.setLayout(new BorderLayout());
		add(content, BorderLayout.CENTER);
		
		JPanel title = new JPanel();
		title.setBackground(gui.background);
		title.setLayout(new BorderLayout());
		content.add(title, BorderLayout.NORTH);
		
		JLabel type = new JLabel("Planet");
		type.setFont(gui.font);
		title.add(type, BorderLayout.NORTH);
		
		JLabel name = new JLabel(p.names.get(0));
		name.setFont(gui.h1);
		name.setForeground(gui.h1color);
		title.add(name, BorderLayout.CENTER);
		
		JLabel name_long = new JLabel(p.name);
		name_long.setFont(gui.h3);
		type.setForeground(gui.h3color);
		title.add(name_long, BorderLayout.SOUTH);
		
		JPanel data = new JPanel();
		data.setBackground(gui.background);
		data.setLayout(new BoxLayout(data, BoxLayout.PAGE_AXIS));
		content.add(data, BorderLayout.CENTER);
		
		data.add(Box.createRigidArea(new Dimension(0, 30)));
		
		if (p.mass != null){
			JLabel mass = new JLabel("Mass: " + p.mass + Boot.boot.getUnits("mass_planet"));
			mass.setFont(gui.font);
			data.add(mass);
		}
		if (p.radius != null){
			JLabel radius = new JLabel("Radius: " + p.radius + Boot.boot.getUnits("radius_planet"));
			radius.setFont(gui.font);
			data.add(radius);
		}
		if (p.period != null){
			JLabel period = new JLabel("Period: " + p.period + Boot.boot.getUnits("period"));
			period.setFont(gui.font);
			data.add(period);
		}
		if (p.smaxis != null){
			JLabel smaxis = new JLabel("Semimajor Axis: " + p.smaxis + Boot.boot.getUnits("smaxis"));
			smaxis.setFont(gui.font);
			data.add(smaxis);
		}
		if (p.temp != null){
			JLabel temp = new JLabel("Temp: " + p.temp + Boot.boot.getUnits("temp"));
			temp.setFont(gui.font);
			data.add(temp);
		}
		if (p.age != null){
			JLabel age = new JLabel("Age: " + p.age + Boot.boot.getUnits("age"));
			age.setFont(gui.font);
			data.add(age);
		}
		if (p.discmethod != null && p.discyear != null){
			JLabel disc = new JLabel("Discoverd through " + p.discmethod + " in " + p.discyear);
			disc.setFont(gui.font);
			data.add(disc);
		}
		
		data.add(Box.createRigidArea(new Dimension(0, 15)));
		
		JTextArea desc = new JTextArea(p.description);
		desc.setEditable(false);
		desc.setLineWrap(true);
		desc.setWrapStyleWord(true);
		desc.setFont(gui.font);
		desc.setAlignmentX(LEFT_ALIGNMENT);
		data.add(desc);
	}
	
	public class MouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int selRow = tree.getRowForLocation(e.getX(), e.getY());
			TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
			if(selRow != -1) {
				if(e.getClickCount() == 1) {
					
				}
				else if(e.getClickCount() == 2) {
					TreeNode tn = (TreeNode) 
							selPath.getLastPathComponent();
					if (tn.getData() != d)
						Boot.boot.displayData(tn.getData());
				}
			}
		}
	}
	
}
