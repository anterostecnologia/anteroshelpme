package br.com.anteros.helpme.treeviewer;

import org.eclipse.swt.graphics.Image;

public interface IObjectNode {

	public IObjectNode[] getChildren();
	
	public boolean hasChildren();

	public void setName(String[] name);
	
	public void setColumnName(String columnName);
	
	public String getColumnName();

	public String[] getName();
	
	public String getSimpleName();

	public void setLeftImage(Image image);

	public Image getLeftImage() throws Exception;

	public void setRightImage(Image image);

	public Image getRightImage() throws Exception;

	public boolean isInitialized();

	public IObjectNode getParent();

	public void setParent(IObjectNode parent);
	
	public void remove(IObjectNode node);
	
	public void removeAll();
	
	public void addNode(IObjectNode node) throws Exception;
	
	public void addAllNode(IObjectNode[] node) throws Exception;
	
	public boolean isExistsNode(IObjectNode node);
	
	public void addListener(IObjectNodeListener listener);
	
	public void removeListener(IObjectNodeListener listener);
	
	public IObjectNode getFirstNode();
	
	public void initialized();
	
	public void uninitialized();
}
