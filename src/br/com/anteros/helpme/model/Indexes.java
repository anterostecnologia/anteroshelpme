package br.com.anteros.helpme.model;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.schema.definition.IndexSchema;

public class Indexes extends TreeNode {

	public static final String[] INDEXES = new String[] { "Indexes" };

	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_INDEXES);
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Index))
			throw new ModelException("O objeto Indexes aceita apenas objetos do tipo Index. Objeto recebido "
					+ node.getClass().getName() + " -> " + Arrays.toString(node.getName()));
		super.addNode(node);
	}

	@Override
	public String[] getName() {
		return INDEXES;
	}

	@Override
	public void initialized() {
		try {
			AnterosHelpmePlugin.getDefault().setWaitCursor();
			this.removeAll();
			List<IndexSchema> indexesMetadata = ((Table) this.getParent()).getTableSchema().getIndexes();
			for (IndexSchema c : indexesMetadata) {
				Index index = new Index(c);
				index.setName(new String[] { c.getName() });
				this.addNode(index);
			}
			this.setInitialized(true);
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error("Não foi possível inicializar os índices da tabela "
					+ ((Table) this.getParent()).getTableSchema().getName(), e, true);
		} finally {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
		}
	}

}