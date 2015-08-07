package oecreader;

import javax.swing.tree.DefaultMutableTreeNode;

public class TreeNode extends DefaultMutableTreeNode {

	private Data d;
	
	public TreeNode(Data d){
		super(d.names.get(0));
		
		this.d = d;
	}
	
	public Data getData(){
		return d;
	}
	
}
