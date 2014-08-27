package br.com.anteros.helpme.treeviewer;

public interface IObjectNodeListener {

	public void onRemoveNode(IObjectNode node);
	
	public void onRemoveAllNode();
	
	public void onAddNode(IObjectNode node);
	
}
