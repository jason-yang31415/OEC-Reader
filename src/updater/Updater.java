package updater;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Updater {

	String oecurl = "https://dl.dropboxusercontent.com/s/f7yc7jxlijldutx/oec.jar";
	String versionurl = "https://dl.dropboxusercontent.com/s/fjmebk8c6x76fpk/version.txt";
	String dir;
	
	String latestVersion = "";
	String currentVersion = "";
	
	public static void main(String[] args){
		new Updater().run();
	}
	
	public void run(){
		dir = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
		if (dir.endsWith("/"))
			dir = dir.substring(0, dir.length() - 1);
		dir = dir.substring(0, dir.lastIndexOf("/"));
		
		
		try {
			System.out.println("Downloading from " + versionurl + "...");
			URL website = new URL(versionurl);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(Channels.newInputStream(rbc)));
			String line;
			while ((line = reader.readLine()) != null){
				if (line.startsWith("v"))
					latestVersion = line;
			}
			
			readSettings();

			System.out.println("Latest version: " + latestVersion);
			System.out.println("Current version: " + currentVersion);
			
			compareVersions();
			
			System.out.println("Press enter to exit...");
			System.in.read();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readSettings(){
		try {
			String path = dir + "/version.txt";
			InputStream i = new FileInputStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(i));
			String line;
			String s = "";
			while ((line = reader.readLine()) != null){
				if (line.startsWith("v"))
					currentVersion = line;
			}
		} catch (IOException e){
			
		}
		
	}
	
	public void compareVersions(){
		String[] lv = latestVersion.substring(1, latestVersion.length()).split("\\.");
		String[] cv = currentVersion.substring(1, currentVersion.length()).split("\\.");
		
		int[] lvs = new int[3];
		int[] cvs = new int[3];
		for (int i = 0; i < lv.length; i++){
			String lvi = lv[i];
			lvs[i] = Integer.parseInt(lvi);
			String cvi = cv[i];
			cvs[i] = Integer.parseInt(cvi);
		}
		
		if (lvs[0] > cvs[0])
			download();
		else if (lvs[0] == cvs[0]){
			if (lvs[1] > cvs[1])
				download();
			else if (lvs[1] == cvs[1]){
				if (lvs[2] > cvs[2])
					download();
				else if (lvs[2] == cvs[2])
					System.out.println("Up to date");
			}
		}
	}
	
	public void download(){
		System.out.println("Updating...");
		try {
			System.out.println("Downloading from " + oecurl + "...");
			URL website = new URL(oecurl);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(dir + "/oec.jar");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			
			updateVersionInfo();
			System.out.println("Done.");
		} catch (IOException e){
			
		}
	}
	
	public void updateVersionInfo(){
		System.out.println("Updating version info...");
		try {
			System.out.println("Downloading from " + versionurl + "...");
			URL website = new URL(versionurl);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(dir + "/version.txt");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (IOException e){
			
		}
	}
	
}
