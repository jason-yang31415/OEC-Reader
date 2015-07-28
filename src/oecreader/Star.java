package oecreader;

import java.io.Serializable;
import java.util.ArrayList;

public class Star extends Data  implements Serializable {
	
	//ArrayList<String> names = new ArrayList<String>();
	String mass, radius, temp, magV;
	String spectraltype;
	StarSystem parent;
	
	public void setParent(StarSystem parent){
		this.parent = parent;
	}
	
	public void setMass(String mass){
		this.mass = mass;
	}
	
	public void setRadius(String radius){
		this.radius = radius;
	}
	
	public void setTemp(String temp){
		this.temp = temp;
	}
	
	public void setMagV(String magV){
		this.magV = magV;
	}
	
	public void setSpectralType(String spectraltype){
		this.spectraltype = spectraltype;
	}
	
}
