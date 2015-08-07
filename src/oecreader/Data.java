package oecreader;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Data  implements Serializable {

	ArrayList<String> names = new ArrayList<String>();
	String name = "";
	
	public void addName(String name){
		names.add(name);
		if (this.name.equals(""))
			this.name += name;
		else
			this.name += ", " + name;
	}
	
	public String getType(){
		if (this instanceof StarSystem)
			return "System";
		else if (this instanceof Star)
			return "Star";
		else if (this instanceof Planet)
			return "Planet";
		return null;
	}
	
}
