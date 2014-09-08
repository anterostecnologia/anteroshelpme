package br.com.anteros.helpme.model;

import java.util.Arrays;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.schema.definition.SequenceGeneratorSchema;

public class Generator extends TreeNode {
	
	private SequenceGeneratorSchema sequenceGeneratorSchema;

	public Generator(SequenceGeneratorSchema sequenceGeneratorSchema) {
		this.sequenceGeneratorSchema=sequenceGeneratorSchema;
	}
	
	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_GENERATOR);
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		throw new ModelException("O objeto Generator não aceita outros objetos. Objeto recebido "
				+ node.getClass().getName() + " -> " + Arrays.toString(node.getName()));
	}

	public SequenceGeneratorSchema getSequenceGeneratorSchema() {
		return sequenceGeneratorSchema;
	}

	
}