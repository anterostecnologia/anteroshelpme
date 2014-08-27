package br.com.anteros.helpme.model;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.helpme.AnterosHelpmePlugin;

public class Validator extends TreeNode {

	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_VALIDATOR);
	}

	
}
