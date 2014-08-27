package br.com.anteros.helpme.model;

import java.util.List;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.schema.SchemaManager;
import br.com.anteros.persistence.schema.definition.StoredFunctionSchema;
import br.com.anteros.persistence.schema.definition.StoredProcedureSchema;

public class Functions extends TreeNode {

	public static final String[] FUNCTIONS = new String[]{"Functions"};
	
	
	
	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_FUNCTION);
	}
	
	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Function))
			throw new ModelException("O objeto Functions aceita apenas objetos do tipo Function. Objeto recebido "
					+ node.getClass().getName() + " -> " + node.getName());
		super.addNode(node);
	}
	
	@Override
	public String[] getName() {
		return FUNCTIONS;
	}
	
	@Override
	public void initialized() {
		try {
			/*AnterosHelpmePlugin.getDefault().setWaitCursor();
			SchemaManager schemaManager = ((Project) getParent().getParent()).getSchemaManager();
			List<StoredFunctionSchema> functionSchema = schemaManager.getProcedures();
			for (ProcedureMetaData p : functionSchema) {
				if (p.isFunction()) {
					Function function = new Function(p);
					function.setName(new String[] { p.getName() });
					this.addNode(function);
				}
			}*/
			this.setInitialized(true);
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error(
					"Não foi possível inicializar as Function's do banco de dados.", e, true);
		} finally {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
		}
	}

}