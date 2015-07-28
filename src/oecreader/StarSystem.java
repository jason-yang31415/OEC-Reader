package oecreader;

import java.io.Serializable;
import java.util.ArrayList;

public class StarSystem extends Data implements Serializable {
	
	//ArrayList<String> names = new ArrayList<String>();
	String ra, dec, distance;
	ArrayList<Star> stars = new ArrayList<Star>();
	ArrayList<Planet> planets = new ArrayList<Planet>();
	
	public StarSystem(){
		
	}
	
	public void setRA(String ra){
		this.ra = ra;
	}
	
	public void setDec(String dec){
		this.dec = dec;
	}
	
	public void setDistance(String distance){
		this.distance = distance;
	}
	
	public void addStar(Star star){
		stars.add(star);
	}
	
	public void addPlanet(Planet planet){
		planets.add(planet);
	}
	
}
