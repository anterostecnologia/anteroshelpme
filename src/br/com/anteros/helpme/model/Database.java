package br.com.anteros.helpme.model;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.helpme.AnterosHelpmePlugin;

public class Database extends TreeNode {

	private Tables tables;
	private Procedures procedures;
	private Functions functions;
	private Generators generators;
	private Views views;

	public Image getLeftImage() throws Exception {
		String name = getProject().getSession().getDialect().name();
		if (name == null)
			return AnterosHelpmePlugin.getDefault().getImage("database.png");

		if (name.toLowerCase().contains("mysql")) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_MYSQL);
		} else if (name.toLowerCase().contains("oracle")) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_ORACLE);
		} else if (name.toLowerCase().contains("db2")) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_DB2);
		} else if (name.toLowerCase().contains("firebird")) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_FIREBIRD);
		} else if (name.toLowerCase().contains("postgresql")) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_POSTGRESQL);
		} else if (name.toLowerCase().contains("mssql")) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_MSSQL);
		} else
			return AnterosHelpmePlugin.getDefault().getImage("database.png");
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Tables) && !(node instanceof Functions) && !(node instanceof Procedures)
				&& !(node instanceof Generators) && !(node instanceof Views))
			throw new ModelException(
					"O objeto Database aceita apenas objetos do tipo Tables,Functions,Procedures,Views e Generators. Objeto recebido "
							+ node.getClass().getName() + " -> " + node.getName());
		super.addNode(node);
	}

	@Override
	public void uninitialized() {
		super.uninitialized();
	}

	@Override
	public void initialized() {
		try {
			AnterosHelpmePlugin.getDefault().setWaitCursor();
			tables = new Tables();
			this.addNode(tables);

			views = new Views();
			this.addNode(views);

			procedures = new Procedures();
			this.addNode(procedures);

			functions = new Functions();
			this.addNode(functions);

			generators = new Generators();
			this.addNode(generators);
			setInitialized(true);
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error("N�o foi poss�vel inicializar o Banco de Dados.", e, true);
		} finally {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
		}
	}

	private Project getProject() {
		return (Project) this.getParent();
	}

}