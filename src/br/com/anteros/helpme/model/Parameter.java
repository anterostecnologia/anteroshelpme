package br.com.anteros.helpme.model;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.schema.definition.StoredParameterSchema;

public class Parameter extends TreeNode {

	private StoredParameterSchema storedParameterSchema;

	public Parameter(StoredParameterSchema storedParameterSchema) {
		this.storedParameterSchema = storedParameterSchema;
	}
	
	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_PARAMETER);
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		throw new ModelException("O objeto Parameter não aceita outros objetos. Objeto recebido "
				+ node.getClass().getName() + " -> " + node.getName());
	}

	public StoredParameterSchema getStoredParameterSchema() {
		return storedParameterSchema;
	}


}