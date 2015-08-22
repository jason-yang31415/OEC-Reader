package oecreader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Parser {

	ArrayList<StarSystem> starSystem = new ArrayList<StarSystem>();
	
	public void read(String s){
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(new InputSource(new ByteArrayInputStream(s.getBytes("utf-8"))));
			
			if (doc.hasChildNodes())
				readDoc(doc.getChildNodes());
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readDoc(NodeList nl){
		for (int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			
			if (n.hasChildNodes())
				readSystems(n.getChildNodes());
		}
	}
	
	public void readSystems(NodeList nl){
		for (int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			
			if (n.getNodeType() == Node.ELEMENT_NODE){
				StarSystem s = new StarSystem();
				starSystem.add(s);
				
				if (n.hasChildNodes())
					readSystemContent(n.getChildNodes(), s);
			}
		}
	}
	
	public void readSystemContent(NodeList nl, StarSystem s){
		for (int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			
			if (n.getNodeType() == Node.ELEMENT_NODE){
				String text = readNodeText(n);
				if (n.getNodeName().equals("name"))
					s.addName(text);
				else if (n.getNodeName().equals("rightascension"))
					s.setRA(text);
				else if (n.getNodeName().equals("declination"))
					s.setDec(text);
				else if (n.getNodeName().equals("distance"))
					s.setDistance(text);
			}
		}
		
		readSystemRecurse(nl, s);
	}
	
	public void readSystemRecurse(NodeList nl, StarSystem s){
		for (int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			
			if (n.getNodeType() == Node.ELEMENT_NODE){
				if (n.getNodeName().equals("star"))
					readStar(n, s);
				else if (n.getNodeName().equals("planet"))
					readPlanet(n, s);
				
				if (n.hasChildNodes())
					readSystemRecurse(n.getChildNodes(), s);
			}
		}
	}
	
	public void readStar(Node node, StarSystem s){
		Star star = new Star();
		star.setParent(s);
		s.addStar(star);
		
		if (node.hasChildNodes())
			readStarContent(node.getChildNodes(), star);
	}
	
	public void readStarContent(NodeList nl, Star star){
		for (int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			
			if (n.getNodeType() == Node.ELEMENT_NODE){
				String text = readNodeText(n);
				if (n.getNodeName().equals("name"))
					star.addName(text);
				else if (n.getNodeName().equals("mass")){
					if (!text.equals(""))
						star.setMass(text);
					else
						star.setMass(range(n));
				}
				else if (n.getNodeName().equals("radius")){
					if (!text.equals(""))
						star.setRadius(text);
					else
						star.setRadius(range(n));
				}
				else if (n.getNodeName().equals("temperature")){
					if (!text.equals(""))
						star.setTemp(text);
					else
						star.setTemp(range(n));
				}
				else if (n.getNodeName().equals("magV")){
					if (!text.equals(""))
						star.setMagV(text);
					else
						star.setMagV(range(n));
				}
				else if (n.getNodeName().equals("age")){
					if (!text.equals(""))
						star.setAge(text);
					else
						star.setAge(range(n));
				}
				else if (n.getNodeName().equals("spectraltype"))
					star.setSpectralType(readNodeText(n));
			}
		}
	}
	
	public void readPlanet(Node node, StarSystem s){
		Planet p = new Planet();
		p.setParent(s);
		s.addPlanet(p);
		
		if (node.hasChildNodes())
			readPlanetContent(node.getChildNodes(), p);
	}
	
	public void readPlanetContent(NodeList nl, Planet p){
		for (int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			
			if (n.getNodeType() == Node.ELEMENT_NODE){
				String text = readNodeText(n);
				if (n.getNodeName().equals("name"))
					p.addName(text);
				else if (n.getNodeName().equals("mass")){
					if (!text.equals(""))
						p.setMass(text);
					else {
						p.setMass(range(n));
					}
				}
				else if (n.getNodeName().equals("radius")){
					if (!text.equals(""))
						p.setRadius(text);
					else {
						p.setRadius(range(n));
					}
				}
				else if (n.getNodeName().equals("period")){
					if (!text.equals(""))
						p.setPeriod(text);
					else {
						p.setPeriod(range(n));
					}
				}
				else if (n.getNodeName().equals("semimajoraxis")){
					if (!text.equals(""))
						p.setSMAxis(text);
					else {
						p.setSMAxis(range(n));
					}
				}
				else if (n.getNodeName().equals("discoverymethod")){
					if (!text.equals(""))
						p.setDiscMethod(text);
					else {
						p.setDiscMethod(range(n));
					}
				}
				else if (n.getNodeName().equals("discoveryyear")){
					if (!text.equals(""))
						p.setDiscYear(text);
					else {
						p.setDiscYear(range(n));
					}
				}
				else if (n.getNodeName().equals("temperature")){
					if (!text.equals(""))
						p.setTemp(text);
					else {
						p.setTemp(range(n));
					}
				}
				else if (n.getNodeName().equals("age")){
					if (!text.equals(""))
						p.setAge(text);
					else
						p.setAge(range(n));
				}
				else if (n.getNodeName().equals("image")){
					p.setImage(text + ".jpg");
				}
				else if (n.getNodeName().equals("imagedescription")){
					p.setImageDescription(text);
				}
				else if (n.getNodeName().equals("description")){
					p.setDescription(text);
				}
			}
		}
	}
	
	public String readNodeText(Node node){
		String s = "";
		
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if (n.getNodeType() == Node.TEXT_NODE)
				s += n.getNodeValue();
		}
		
		return s;
	}
	
	public String range(Node n){
		Node lowernode = n.getAttributes().getNamedItem("lowerlimit");
		Node uppernode = n.getAttributes().getNamedItem("upperlimit");
		String lower = "";
		String upper = "";
		String s;
		
		if (lowernode != null)
			lower = lowernode.getNodeValue();
		if (uppernode != null)
			upper = uppernode.getNodeValue();
		
		if (lowernode != null && uppernode != null)
			s = lower + " - " + upper;
		else if (lowernode != null)
			s = "> " + lower;
		else if (uppernode != null)
			s = "< " + upper;
		else
			s = "no data";
		
		return s;
	}
	
	public ArrayList<StarSystem> getSystems(){
		return starSystem;
	}
	
}
