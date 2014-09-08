package br.com.anteros.helpme.model;

import java.util.Arrays;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.schema.definition.TableSchema;

public class Table extends TreeNode {

	private TableSchema tableSchema;
	private Columns columns;
	private Constraints constraints;
	private Indexes indexes;

	public Table(TableSchema tableSchema) {
		this.tableSchema = tableSchema;
	}

	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_TABLE);
	}

	@Override
	public Image getRightImage() throws Exception {
			return null;
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Columns) && !(node instanceof Constraints) && !(node instanceof Indexes))
			throw new ModelException(
					"O objeto Table aceita apenas objetos do tipo Columns,Indexes e Contraints. Objeto recebido "
							+ node.getClass().getName() + " -> " + Arrays.toString(node.getName()));
		super.addNode(node);
	}

	public TableSchema getTableSchema() {
		return tableSchema;
	}

	@Override
	public void initialized() {
		try {
			AnterosHelpmePlugin.getDefault().setWaitCursor();
			this.removeAll();
			columns = new Columns();
			this.addNode(columns);

			constraints = new Constraints();
			this.addNode(constraints);

			indexes = new Indexes();
			this.addNode(indexes);

		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error("Não foi possível inicializar a Tabela " + tableSchema.getName(), e, true);
		} finally {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
		}
	}

	@Override
	public void uninitialized() {
		if (initialized) {
		}
		super.uninitialized();
	}

}