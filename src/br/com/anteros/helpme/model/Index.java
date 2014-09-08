package br.com.anteros.helpme.model;

import java.util.Arrays;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.schema.definition.ColumnSchema;
import br.com.anteros.persistence.schema.definition.IndexSchema;

public class Index extends TreeNode {

	private IndexSchema indexSchema;

	public Index(IndexSchema indexSchema) {
		this.indexSchema = indexSchema;
	}

	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_INDEX);
	}

	@Override
	public Image getRightImage() throws Exception {
		return null;
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Column))
			throw new ModelException("O objeto Index aceita apenas objetos do tipo Column. Objeto recebido "
					+ node.getClass().getName() + " -> " + Arrays.toString(node.getName()));
		super.addNode(node);
	}

	public IndexSchema getIndexSchema() {
		return indexSchema;
	}

	@Override
	public void initialized() {
		try {
			AnterosHelpmePlugin.getDefault().setWaitCursor();
			this.removeAll();
			for (ColumnSchema c : indexSchema.getColumns()) {
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
			this.setInitialized(true);
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error("Não foi possível inicializar as Colunas do índice " + indexSchema.getName(), e,
					true);
		} finally {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
		}
	}

}