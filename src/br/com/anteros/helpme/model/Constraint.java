package br.com.anteros.helpme.model;

import java.util.Arrays;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.schema.definition.ConstraintSchema;

public class Constraint extends TreeNode {

	private ConstraintSchema constraintSchema;

	public Constraint(ConstraintSchema constraintSchema) {
		this.constraintSchema = constraintSchema;
	}

	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_CONSTRAINT);
	}

	@Override
	public Image getRightImage() throws Exception {
		return null;
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Column))
			throw new ModelException("O objeto Constraint aceita apenas objetos do tipo Column. Objeto recebido "
					+ node.getClass().getName() + " -> " + Arrays.toString(node.getName()));
		super.addNode(node);
	}

	public ConstraintSchema getConstraintSchema() {
		return constraintSchema;
	}

	@Override
	public void uninitialized() {
	}

	@Override
	public void initialized() {
		super.initialized();
	}

}