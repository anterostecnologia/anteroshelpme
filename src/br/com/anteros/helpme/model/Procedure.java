package br.com.anteros.helpme.model;

import java.util.Arrays;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.helpme.util.StringUtils;
import br.com.anteros.persistence.schema.definition.StoredParameterSchema;
import br.com.anteros.persistence.schema.definition.StoredProcedureSchema;

public class Procedure extends TreeNode {

	private StoredProcedureSchema storedProcedureSchema;

	public Procedure(StoredProcedureSchema storedProcedureSchema) {
		this.storedProcedureSchema = storedProcedureSchema;
	}

	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_PROCEDURE);
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Parameter))
			throw new ModelException("O objeto Procedure aceita apenas objetos do tipo Parameter. Objeto recebido "
					+ node.getClass().getName() + " -> " + Arrays.toString(node.getName()));
		super.addNode(node);
	}

	public StoredProcedureSchema getStoredProcedureSchema() {
		return storedProcedureSchema;
	}

	@Override
	public void initialized() {
		try {
			AnterosHelpmePlugin.getDefault().setWaitCursor();
			for (StoredParameterSchema p : storedProcedureSchema.getParameters()) {
				Parameter parameter = new Parameter(p);
				parameter.setName(new String[] { (StringUtils.isEmpty(p.getName()) ? "" : p.getName() + " ")
						+ p.getType() + " " + p.getTypeSql() });
				parameter.setInitialized(true);
				this.addNode(parameter);
			}
			this.setInitialized(true);
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error("Não foi possível inicializar os Parâmetros da Stored Procedure "
					+ storedProcedureSchema.getName(), e, true);
		} finally {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
		}
	}

}