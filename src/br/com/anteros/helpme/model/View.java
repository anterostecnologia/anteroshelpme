package br.com.anteros.helpme.model;

import java.util.List;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.schema.definition.ViewSchema;

public class View extends TreeNode {

	private ViewSchema viewSchema;

	public View(ViewSchema viewSchema) {
		this.viewSchema = viewSchema;
	}

	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_VIEW);
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Column))
			throw new ModelException("O objeto View aceita apenas objetos do tipo Column. Objeto recebido "
					+ node.getClass().getName() + " -> " + node.getName());
		super.addNode(node);
	}

	
	@Override
	public void initialized() {
		try {
			/*AnterosHelpmePlugin.getDefault().setWaitCursor();
			List<ColumnMetaData> columnsMetadata = viewSchema.getColumns();
			this.removeAll();
			for (ColumnMetaData c : columnsMetadata) {
				Column column = new Column(c);
				StringBuffer sb = new StringBuffer();
				sb.append(c.getName()).append(" ").append(c.getStringDataType());
				if (c.getLength() > 0) {
					sb.append("(").append(c.getLength());
					if (c.getPrecision() > 0) {
						sb.append(",").append(c.getPrecision());
					}
					sb.append(")");
				}
				column.setName(new String[] { sb.toString() });
				column.setInitialized(true);
				this.addNode(column);
			}*/
			this.setInitialized(true);
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error(
					"Não foi possível inicializar as Colunas da View "+viewSchema.getName(), e, true);
		} finally {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
		}
	}

	public ViewSchema getViewSchema() {
		return viewSchema;
	}

}