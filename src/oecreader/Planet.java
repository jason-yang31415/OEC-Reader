package oecreader;

import java.io.Serializable;
import java.util.ArrayList;

public class Planet extends Data  implements Serializable {
	
	//ArrayList<String> names = new ArrayList<String>();
	String mass, radius, period, smaxis, discmethod, discyear, temp;
	String age;
	String description = "";
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
	
	public void setPeriod(String period){
		this.period = period;
	}
	
	public void setSMAxis(String smaxis){
		this.smaxis = smaxis;
	}
	
	public void setDiscMethod(String discmethod){
		this.discmethod = discmethod;
	}
	
	public void setDiscYear(String discyear){
		this.discyear = discyear;
	}
	
	public void setTemp(String temp){
		this.temp = temp;
	}
	
	public void setAge(String age){
		this.age = age;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
}
