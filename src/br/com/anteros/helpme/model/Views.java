package br.com.anteros.helpme.model;

import java.util.Arrays;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;

public class Views extends TreeNode {

	public static final String[] VIEWS = new String[] { "Views" };

	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_VIEW);
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof View))
			throw new ModelException("O objeto Views aceita apenas objetos do tipo View. Objeto recebido "
					+ node.getClass().getName() + " -> " + Arrays.toString(node.getName()));
		super.addNode(node);
	}

	@Override
	public String[] getName() {
		return VIEWS;
	}

	@Override
	public void initialized() {
		try {
			/*AnterosHelpmePlugin.getDefault().setWaitCursor();
			SchemaManager schemaManager = ((Project) getParent().getParent()).getSchemaManager();
			List<ViewSchema> viewMetadata = schemaManager.getViews();
			for (ViewSchema p : viewMetadata) {
				View view = new View(p);
				view.setName(new String[] { p.getName() });
				this.addNode(view);
			}*/
			this.setInitialized(true);
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error(
					"Não foi possível inicializar as Views do banco de dados.", e, true);
		} finally {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
		}
	}


}