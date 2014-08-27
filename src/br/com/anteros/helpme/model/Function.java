package br.com.anteros.helpme.model;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.helpme.util.StringUtils;
import br.com.anteros.persistence.schema.definition.StoredFunctionSchema;
import br.com.anteros.persistence.schema.definition.StoredParameterSchema;

public class Function extends TreeNode {

	private StoredFunctionSchema functionSchema;

	public Function(StoredFunctionSchema functionSchema) {
		this.functionSchema = functionSchema;
	}

	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_FUNCTION);
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Parameter))
			throw new ModelException("O objeto Function aceita apenas objetos do tipo Parameter. Objeto recebido "
					+ node.getClass().getName() + " -> " + node.getName());
		super.addNode(node);
	}

	public StoredFunctionSchema getFunctionSchema() {
		return functionSchema;
	}

	@Override
	public void initialized() {
		try {
			for (StoredParameterSchema p : functionSchema.getParameters()) {
				Parameter parameter = new Parameter(p);
				parameter.setName(new String[] { (StringUtils.isEmpty(p.getName()) ? "" : p.getName() + " ")
						+ p.getType() + " " + p.getTypeSql() }); 
				parameter.setInitialized(true);
				this.addNode(parameter);
			}
			this.setInitialized(true);
		} catch (Exception e) {
			AnterosHelpmePlugin.error("Não foi possível inicializar os parâmetros da Procedure/Function "+functionSchema.getName(), e, true);
		}
	}

}