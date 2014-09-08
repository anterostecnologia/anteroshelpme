package br.com.anteros.helpme.model;

import java.util.Arrays;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.schema.definition.ColumnSchema;

public class Column extends TreeNode {

	private ColumnSchema columnSchema;

	public Column(ColumnSchema columnSchema) {
		this.columnSchema = columnSchema;
	}

	public Image getLeftImage() throws Exception {
		if (columnSchema.isPrimaryKey() && columnSchema.isForeignKey())
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_PK_FK_COLUMN);
		else if (columnSchema.isPrimaryKey())
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_PK_COLUMN);
		else if (columnSchema.isForeignKey())
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_FK_COLUMN);
		else
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_COLUMN);
	}

	@Override
	public Image getRightImage() throws Exception {
		return null;
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		throw new ModelException("O objeto Column não aceita outros objetos. Objeto recebido "
				+ node.getClass().getName() + " -> " + Arrays.toString(node.getName()));
	}

	public ColumnSchema getColumnSchema() {
		return columnSchema;
	}

}