package br.com.anteros.helpme.model;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.schema.definition.ColumnSchema;
import br.com.anteros.persistence.schema.definition.ConstraintSchema;

public class Constraints extends TreeNode {

	public static final String[] CONSTRAINTS = new String[] { "Constraints" };

	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_CONSTRAINTS);
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Constraint))
			throw new ModelException("O objeto Constraints aceita apenas objetos do tipo Contraint. Objeto recebido "
					+ node.getClass().getName() + " -> " + Arrays.toString(node.getName()));
		super.addNode(node);
	}

	@Override
	public String[] getName() {
		return CONSTRAINTS;
	}

	@Override
	public void initialized() {
		try {
			AnterosHelpmePlugin.getDefault().setWaitCursor();
			List<ConstraintSchema> constraintsSchema = ((Table) this.getParent()).getTableSchema()
					.getConstraints();
			this.removeAll();
			for (ConstraintSchema c : constraintsSchema) {
				Constraint constraint = new Constraint(c);
				constraint.setName(new String[] { c.getName() });
				constraint.setInitialized(true);
				int i = 0;
				for (ColumnSchema cc : c.getColumns()) {
					Column column = new Column(cc);
					ColumnSchema cm = null;
					//VER AQUI if ((c.getReferenceColumns().size() > i) && c.isFkConstraint())
					//	cm = c.getReferenceColumns().get(i);
					StringBuffer sb = new StringBuffer();
					sb.append(c.getName()).append(" ").append(cc.getTypeSql());
					if (cc.getSize() > 0) {
						sb.append("(").append(cc.getSize()); 
						if (cc.getSubSize() > 0) {
							sb.append(",").append(cc.getSubSize());
						}
						sb.append(")");
					} 
					column.setName(new String[] { sb.toString() });
					column.setInitialized(true);
					constraint.addNode(column);
					i++;
				}
				this.addNode(constraint);
			}
			this.setInitialized(true);
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error("Não foi possível inicializar as Constraints da Tabela "
					+ ((Table) getParent()).getTableSchema().getName() + ".", e, true);
		} finally {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
		}
	}

}