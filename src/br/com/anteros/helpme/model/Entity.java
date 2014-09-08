package br.com.anteros.helpme.model;

import java.util.Arrays;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.metadata.EntityCache;

public class Entity extends TreeNode {

	private EntityCache entityCache;
	
	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_CLASS);
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Component))
			throw new ModelException("O objeto Entity aceita apenas objetos do tipo Component. Objeto recebido "
					+ node.getClass().getName() + " -> " + Arrays.toString(node.getName()));
		super.addNode(node);
	}

	public EntityCache getEntityCache() {
		return entityCache;
	}

	public void setAnnotationCache(EntityCache entityCache) {
		this.entityCache = entityCache;
	}


}