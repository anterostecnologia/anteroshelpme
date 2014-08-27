package br.com.anteros.helpme.model;

import java.util.List;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.schema.SchemaManager;
import br.com.anteros.persistence.schema.definition.StoredProcedureSchema;

public class Procedures extends TreeNode {

	public static final String[] PROCEDURES = new String[] { "Procedures" };

	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_PROCEDURE);
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Procedure))
			throw new ModelException("O objeto Procedures aceita apenas objetos do tipo Procedure. Objeto recebido "
					+ node.getClass().getName() + " -> " + node.getName());
		super.addNode(node);
	}

	@Override
	public String[] getName() {
		return PROCEDURES;
	}

	@Override
	public void initialized() {
		try {
			/*AnterosHelpmePlugin.getDefault().setWaitCursor();
			SchemaManager schemaManager = ((Project) getParent().getParent()).getSchemaManager();
			List<StoredProcedureSchema> storedProcedureSchema = schemaManager.getProcedures();
			for (StoredProcedureSchema p : storedProcedureSchema) {
				if (!p.isFunction()) {
					Procedure procedure = new Procedure(p);
					procedure.setName(new String[] { p.getName() });
					this.addNode(procedure);
				}
			}*/
			this.setInitialized(true);
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error(
					"N!ao foi possível inicializar as Stored Procedures do banco de dados.", e, true);
		} finally {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
		}
	}

	

}