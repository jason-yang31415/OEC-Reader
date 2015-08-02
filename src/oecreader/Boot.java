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

/**
 * @author Jason Yang
 *
 */

public class Boot {
	
	Map<String, String> units = new HashMap<String, String>();
	
	String dir;
	String url = "https://github.com/OpenExoplanetCatalogue/oec_gzip/raw/master/systems.xml.gz";
	ArrayList<StarSystem> systems = new ArrayList<StarSystem>();
	
	boolean serialize = false;
	Scanner sc;
	
	public enum State {
		HOME("HOME"),
		RESULTS("HOME > SEARCH"),
		SYSTEM("SYSTEM"),
		STAR("STAR"),
		PLANET("PLANET");
		
		final String loc;
		
		State(String loc){
			this.loc = loc;
		}
	}
	
	public static void main (String[] args){
		new Boot().run();
	}
	
	public void run(){
		dir = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
		if (dir.endsWith("/"))
			dir = dir.substring(0, dir.length() - 1);
		dir = dir.substring(0, dir.lastIndexOf("/"));
		
		initUnits();
		
		deserialize();
        
		sc = new Scanner(System.in);
		
		loop();
	}
	
	public void loop(String... cmd){
		String in;
		if (cmd.length <= 0){
			in = input(State.HOME);
		}
		else
			in = parseInput(cmd[0]);
		
		if (in == null)
			loop();
		
		String[] cmds = in.split(",");
		
		for (String cmdlet : cmds){
			cmdlet = parseInput(cmdlet);
			
			String[] commands = cmdlet.split(" ");
			
			if (commands.length > 0){
				
				if (commands[0].equalsIgnoreCase("search")){
					String s = "";
					String params = "";
					
					for (int i = 1; i < commands.length; i++){
						String arg = commands[i];
						if (arg.toLowerCase().equals("-system"))
							params += "type=system;";
						else if (arg.toLowerCase().equals("-star"))
							params += "type=star;";
						else if (arg.toLowerCase().equals("-planet"))
							params += "type=planet;";
						else
							s += arg + " ";
					}
					
					if (s.equals(""))
						loop();
					
					if (!params.contains("type"))
						params += "type=system;type=star;type=planet";
					
					s = s.substring(0, s.length() - 1);
					ArrayList<Data> matches = search(s, params);
					
					if (matches.size() == 1)
						displayData(matches.get(0));
					else {
						if (matches.size() > 0)
							chooseResult(matches);
						else
							System.out.println("No matches found.");
					}
				}
				else if (commands[0].equalsIgnoreCase("update"))
					update();
				else if (commands[0].equalsIgnoreCase("parse"))
					parse();
				else if (commands[0].equalsIgnoreCase("serialize"))
					serialize();
				else if (commands[0].equalsIgnoreCase("deserialize"))
					deserialize();
				else
					System.out.println("Type 'help' for help");
			}
		}
		
		loop();
	}
	
	public void chooseResult(ArrayList<Data> matches){
		System.out.println("\n\n");
		
		for (int i = 0; i < matches.size(); i++){
			Data m = matches.get(i);
			
			if (m instanceof StarSystem)
				System.out.println("\n" + (i + 1) + ".	" + m.name + " (System)");
			else
				System.out.println((i + 1) + ".	" + m.name);
		}
		
		String in = input(State.RESULTS);
		
		try {
			int choice = Integer.parseInt(in);
			
			if (choice > 0)
				displayData(matches.get(choice - 1));
			else
				loop();
		} catch (NumberFormatException e){
			System.err.println("Not a valid option");
			chooseResult(matches);
		}
	}
	
	public void displayData(Data d){
		System.out.println("\n\n");
		
		if (d instanceof StarSystem)
			displaySystemData((StarSystem) d);
		else if (d instanceof Star)
			displayStarData((Star) d);
		else if (d instanceof Planet)
			displayPlanetData((Planet) d);
	}
	
	public void displaySystemData(StarSystem sys){
		System.out.println("Star System");
		
		System.out.println(sys.name);
		hr();
		
		System.out.println("	" + sys.ra + ", " + sys.dec);
		System.out.println("	Distance: " + sys.distance + getUnits("distance"));
		
		hr();
		
		for (int i = 0; i < sys.stars.size(); i++){
			Star star = sys.stars.get(i);
			
			System.out.println((i + 1) + ". " + star.name);
		}
		
		for (int i = 0; i < sys.planets.size(); i++){
			Planet p = sys.planets.get(i);
			
			System.out.println((i + sys.stars.size() + 1) + ". " + p.name);
		}

		String in = input(State.SYSTEM, sys.names.get(0));
		try {
			int choice = Integer.parseInt(in);
			
			if (choice > 0){
				if (choice - 1 < sys.stars.size())
					displayData(sys.stars.get(choice - 1));
				else
					displayData(sys.planets.get(choice - sys.stars.size() - 1));
			}
		} catch (NumberFormatException e){
			System.err.println("Not a valid option");
			displayData(sys);
		}
	}
	
	public void displayStarData(Star star){
		System.out.println("\nStar");
		
		System.out.println(star.name);
		hr();
		
		if (star.mass != null)
			System.out.println("	Mass: " + star.mass + getUnits("mass_star"));
		if (star.radius != null)
			System.out.println("	Radius: " + star.radius + getUnits("radius_star"));
		if (star.temp != null)
			System.out.println("	Temp: " + star.temp + getUnits("temp"));
		if (star.magV != null)
			System.out.println("	Visual Mag: " + star.magV);
		if (star.age != null)
			System.out.println("	Age: " + star.age + getUnits("age"));
		if (star.spectraltype != null)
			System.out.println("	Spectral Type: " + star.spectraltype);
		
		hr();
		
		System.out.println();
		System.out.println("'up' - view system");

		String in = input(State.SYSTEM, star.parent.names.get(0) + " > " + star.names.get(0));
		if (in.equals("up"))
			displayData(star.parent);
		else {
			System.err.println("Not a valid option");
			displayData(star);
		}
	}
	
	public void displayPlanetData(Planet p){
		System.out.println("\nPlanet");
		
		System.out.println(p.name);
		hr();
		
		if (p.mass != null)
			System.out.println("	Mass: " + p.mass + getUnits("mass_planet"));
		if (p.radius != null)
			System.out.println("	Radius: " + p.radius + getUnits("radius_planet"));
		if (p.period != null)
			System.out.println("	Period: " + p.period + getUnits("period"));
		if (p.smaxis != null)
			System.out.println("	Semimajor Axis: " + p.smaxis + getUnits("smaxis"));
		if (p.temp != null)
			System.out.println("	Temp: " + p.temp + getUnits("temp"));
		if (p.age != null)
			System.out.println("	Age: " + p.age + getUnits("age"));
		if (p.discmethod != null && p.discyear != null)
			System.out.println("	Discoverd through " + p.discmethod + " in " + p.discyear);
		System.out.println("	" + p.description);
		
		hr();
		
		System.out.println();
		System.out.println("'up' - view system");

		String in = input(State.SYSTEM, p.parent.names.get(0) + " > " + p.names.get(0));
		if (in.equals("up"))
			displayData(p.parent);
		else {
			System.err.println("Not a valid option");
			displayData(p);
		}
	}
	
	public String input(State state, String... location){
		System.out.println();
		if (location.length > 0)
			System.out.print(location[0]);
		else
			System.out.print(state.loc);
		System.out.print(" >> ");
		
		String in = sc.nextLine();
		return parseInput(in);
	}
	
	public String parseInput(String in){
		String[] cmds = in.split(",");
		String cmd = cmds[0];
		
		if (cmd.equals("exit"))
			System.exit(0);
		else if (cmd.equals("home")){
			if (cmds.length > 1)
				loop(in.split(",", 2)[1]);
			else
				loop();
		}
		else if (cmd.equals("help")){
			help();
		}
		else
			return in;
		return null;
	}
	
	public ArrayList<Data> search(String search, String params){
		ArrayList<Data> matches = new ArrayList<Data>();
		
		boolean searchSys, searchStar, searchPlanet;
		searchSys = searchStar = searchPlanet = false;
		
		String[] pArray = params.split(";");
		for (String s : pArray){
			if (!s.equals("")){
				String param = s.split("=")[0];
				String value = s.split("=")[1];
				switch (param){
				case "type":
					switch (value){
					case "system":
						searchSys = true;
						break;
					case "star":
						searchStar = true;
						break;
					case "planet":
						searchPlanet = true;
						break;
					default:
						break;
					}
					break;
				default:
					break;
				}
			}
		}
		
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
		System.out.println("\nHelp");
		System.out.println("Type 'search [keywords]' to search for a star system, star, or exoplanet.");
		System.out.println("Searches are not case-sensitive.");
		System.out.println("Enter the number preceding an option to view that option.");
		System.out.println("Enter 'home' at any point to go back to the home view.");
		hr();
		System.out.println("Type 'update' to download and extract the latest data file.");
		System.out.println("Type 'parse' to parse the data file.");
		System.out.println("Type 'serialize' to export data to data.ser after parsing.");
		System.out.println("Type 'deserialize' to import data from data.ser.");
		System.out.println("Type 'exit' to quit.");
		hr();
		System.out.println("Multiple commands can be executed with one command. "
				+ "For example,  update,parse,serialize  will update the data, parse it, "
				+ "then export it to data.ser with one line.");
		hr();
		System.out.println("Try typing 'search earth'");
		hr();
		System.out.println("See https://github.com/jason-yang31415/OEC-Reader/wiki for more details.");
		System.out.println("\n");
	}
	
	public void hr(){
		System.out.println("----------------------------------------");
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
	
}
