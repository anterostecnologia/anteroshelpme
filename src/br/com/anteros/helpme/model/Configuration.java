package br.com.anteros.helpme.model;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.helpme.AnterosHelpmePlugin;

public class Configuration extends TreeNode {

	public static final String[] CONFIGURATION = new String[]{"Configuration"};
	
	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage("config.png");
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		throw new ModelException("O objeto Configuration n�o aceita outros objetos. Objeto recebido "
				+ node.getClass().getName() + " -> " + node.getName());
	}
	
	@Override
	public String[] getName() {
		return CONFIGURATION;
	}

}