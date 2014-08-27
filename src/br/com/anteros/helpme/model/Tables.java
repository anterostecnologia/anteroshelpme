package br.com.anteros.helpme.model;

import java.net.URLClassLoader;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.helpme.util.PluginUtils;
import br.com.anteros.persistence.schema.definition.TableSchema;

public class Tables extends TreeNode {

	public static final String[] TABLES = new String[]{"Tables"};
	
	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_TABLES);
	}
	
	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Table))
			throw new ModelException("O objeto Tables aceita apenas objetos do tipo Table. Objeto recebido "
					+ node.getClass().getName() + " -> " + node.getName());
		super.addNode(node);
	}
	
	@Override
	public String[] getName() {
		return TABLES;
	}
	
	@Override
	public void initialized() {
		try {
			AnterosHelpmePlugin.getDefault().setWaitCursor();
			IJavaProject javaProject = PluginUtils.findJavaProject(getProject().getProjectName());

			ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
			URLClassLoader newClassLoader = PluginUtils.getProjectClassLoader(javaProject);
			try {
				Thread.currentThread().setContextClassLoader(newClassLoader);
				for (TableSchema tb : getProject().getSchemaManager().getTables()) {
					Table table = new Table(tb);
					table.setName(new String[] { tb.getName() });
					this.addNode(table);
				}
			} finally {
				Thread.currentThread().setContextClassLoader(oldLoader);
			}
			setInitialized(true);
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error("Não foi possível inicializar as Tabelas mapeadas nas entidades.", e, true);
		} finally {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
		}
	}
	

	public Project getProject() {
		return (Project) this.getParent().getParent();
	}
}