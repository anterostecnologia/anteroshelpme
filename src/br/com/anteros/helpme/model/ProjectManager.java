package br.com.anteros.helpme.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.model.persistence.SQLProjectPersistent;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.helpme.AnterosHelpmePlugin;

public class ProjectManager extends TreeNode {

	public static final String[] PROJECTS = new String[] { "Project Manager" };

	public ProjectManager() throws Exception {
	}

	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_PROJECTS);
	}

	@Override
	public String[] getName() {
		return PROJECTS;
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Project))
			throw new ModelException("O objeto Projects aceita apenas objetos do tipo Project. Objeto recebido "
					+ node.getClass().getName() + " -> " + Arrays.toString(node.getName()));
		super.addNode(node);
	}

	public void loadProjectsPersistent(List fromXML) {
		removeAll();
		for (Object obj : fromXML) {
			children.add(Project.createFromPersistent((SQLProjectPersistent) obj));
		}
	}

	public List<SQLProjectPersistent> getProjectsPersistent() {
		List<SQLProjectPersistent> result = new ArrayList<SQLProjectPersistent>();
		for (IObjectNode node : getChildren()) {
			List<String> cp = new ArrayList<String>();
			for (Classpath c : ((Project) node).getClasspath()) {
				cp.add(c.getClasspath());
			}
			result.add(new SQLProjectPersistent(((Project) node).getSimpleName(), ((Project) node).getProjectName(),
					((Project) node).getFileConfiguration(), cp));
		}
		return result;
	}

	
}