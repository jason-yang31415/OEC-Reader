package oecreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author Jason Yang
 *
 */

public class Boot {
	
	public static Boot boot;
	
	GUI gui;
	
	Map<String, String> units = new HashMap<String, String>();
	
	String dir;
	String url = "https://github.com/OpenExoplanetCatalogue/oec_gzip/raw/master/systems.xml.gz";
	String img_url = "https://raw.githubusercontent.com/hannorein/oec_outreach/master/images";
	ArrayList<StarSystem> systems = new ArrayList<StarSystem>();
	
	boolean serialize = false;
	
	public static void main (String[] args){
		boot = new Boot();
		boot.run();
	}
	
	public void run(){
		dir = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
		if (dir.endsWith("/"))
			dir = dir.substring(0, dir.length() - 1);
		dir = dir.substring(0, dir.lastIndexOf("/"));
		
		initUnits();
		
		deserialize();
		
		createGUI();
	}
	
	public void displayData(Data d){
		DataDisplay display = new DataDisplay(d, gui);
		display.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		display.setSize(720, 405);
		display.setResizable(false);
		display.setVisible(true);
	}
	
	public ArrayList<Data> search(String search, boolean searchSys, boolean searchStar, boolean searchPlanet){
		ArrayList<Data> matches = new ArrayList<Data>();
		
		for (StarSystem sys : systems){
			boolean sysMatch = false;
			
			for (String n : sys.names){
				if (n.toLowerCase().contains(search.toLowerCase())){
					sysMatch = true;
				}
			}
			
			if (sysMatch && searchSys)
				matches.add(sys);
			
			for (Star star : sys.stars){
				boolean starMatch = false;
				
				for (String n : star.names){
					if (n.toLowerCase().contains(search.toLowerCase()))
						starMatch = true;
				}
				
				if (starMatch && searchStar)
					matches.add(star);
			}
			
			for (Planet p : sys.planets){
				boolean planetMatch = false;
				
				for (String n : p.names){
					if (n.toLowerCase().contains(search.toLowerCase()))
						planetMatch = true;
				}
				
				if (planetMatch && searchPlanet)
					matches.add(p);
			}
		}
		
		return matches;
	}
	
	public void parse(){
		System.out.println("parsing data...");
		String path = dir + "/systems.xml";
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))){
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null){
				sb.append(line);
				sb.append(System.lineSeparator());
			}
			
			Parser parser = new Parser();
			parser.read(sb.toString());
			
			systems = parser.getSystems();
			
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void update(){
		try {
			
			System.out.println("Downloading from " + url + "...");
			URL website = new URL(url);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(dir + "/systems.xml.gz");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			
			System.out.println("Extracting...");
			byte[] buffer = new byte[1024];
			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(dir + "/systems.xml.gz"));
			
			FileOutputStream out = new FileOutputStream(dir + "/systems.xml");
			
			int len;
			while ((len = gzis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		 
			gzis.close();
			out.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateImages(){
		File img_folder = new File(dir + "/images");
		if (!img_folder.exists())
			img_folder.mkdir();
		
		for (StarSystem sys : systems){
			for (Planet p : sys.planets){
				if (p.image != null){
					File img_dir = new File(dir + "/images/" + p.image);
					if (!img_dir.exists()){
						try {
							System.out.println("Downloading from " + img_url + "/" + p.image + "...");
							URL website = new URL(img_url + "/" + p.image);
							ReadableByteChannel rbc = Channels.newChannel(website.openStream());
							FileOutputStream fos = new FileOutputStream(img_dir);
							fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
							fos.close();
							rbc.close();
						} catch (IOException ioe){}
					}
				}
			}
		}
		System.out.println("done");
	}
	
	public void serialize(){
		try {
			System.out.println("serializing...");
			
			FileOutputStream fileOut = new FileOutputStream(dir + "/data.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(systems);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in " + dir + "/data.ser");
		} catch (IOException e){
			System.out.println("Something screwed up :(");
		}
	}
	
	public void deserialize(){
		try {
			System.out.println("deserializing...");
			FileInputStream fileIn = new FileInputStream(dir + "/data.ser");
	        ObjectInputStream in;
			in = new ObjectInputStream(fileIn);
			systems = (ArrayList<StarSystem>) in.readObject();
	        in.close();
	        fileIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (e instanceof InvalidClassException){
				System.out.println("data.ser is invalid for this version of OEC Reader.");
				System.out.println("Re-parsing and serializing...");
				parse();
				serialize();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void help(){
		Help help = new Help(gui);
		help.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		help.setSize(640, 360);
		help.setResizable(false);
		
		help.setVisible(true);
	}
	
	public String getUnits(String key){
		return " " + units.get(key);
	}
	
	public void initUnits(){
		units.put("mass_star", "Msun");
		units.put("mass_planet", "Mj");
		units.put("radius_star", "Rsun");
		units.put("radius_planet", "Rj");
		units.put("distance", "pcs");
		units.put("smaxis", "AU");
		units.put("period", "days");
		units.put("temp", "K");
		units.put("age", "Gyr");
	}
	
	public void createGUI(){
		try {
            // Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			//handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}
		
		
		gui = new GUI();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(960, 540);
		gui.setResizable(false);
		
		gui.setVisible(true);
	}
	
}
