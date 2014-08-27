package br.com.anteros.helpme.model;

import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.helpme.AnterosHelpmePlugin;

public class ProjectRoot extends TreeNode {

	
	public ProjectRoot() {
		try {
			this.addNode(new ProjectManager());
		} catch (Exception e) {
			AnterosHelpmePlugin.error(
					"N�o foi poss�vel inicializar o Gerenciador de Projetos.", e, true);
		}
	}

	public ProjectManager getProjectManager() {
		return (ProjectManager) getFirstNode();
	}

}
