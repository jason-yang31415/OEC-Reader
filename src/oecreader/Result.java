package oecreader;

import javax.swing.*;

public class Result extends JPanel {

	private Data d;
	
	public Result(Data d){
		this.d = d;
	}
	
	public Data getData(){
		return d;
	}
	
}
