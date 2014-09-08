package br.com.anteros.helpme.model;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.schema.definition.ColumnSchema;

public class Columns extends TreeNode {

	public static final String[] COLUMNS = new String[] { "Columns" };

	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_COLUMNS);
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Column))
			throw new ModelException("O objeto Columns aceita apenas objetos do tipo Column. Objeto recebido "
					+ node.getClass().getName() + " -> " + Arrays.toString(node.getName()));
		super.addNode(node);
	}

	@Override
	public String[] getName() {
		return COLUMNS;
	}

	@Override
	public void initialized() {
		try {
			AnterosHelpmePlugin.getDefault().setWaitCursor();
			List<ColumnSchema> columnsSchema = ((Table) this.getParent()).getTableSchema().getColumns();
			this.removeAll();
			for (ColumnSchema c : columnsSchema) {
				Column column = new Column(c);
				StringBuffer sb = new StringBuffer();
				sb.append(c.getName()).append(" ").append(c.getTypeSql());
				if (c.getSize() > 0) {
					sb.append("(").append(c.getSize()); 
					if (c.getSubSize() > 0) {
						sb.append(",").append(c.getSubSize());
					}
					sb.append(")");
				} 
				column.setName(new String[] { sb.toString() });
				column.setInitialized(true);
				this.addNode(column);
			}
			setInitialized(true);
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error("Não foi possível inicializar as Colunas da Tabela "
					+ ((Table) getParent()).getTableSchema().getName() + ".", e, true);
		} finally {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
		}

	}

}