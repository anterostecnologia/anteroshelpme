package br.com.anteros.helpme.model;

import java.io.Serializable;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.helpme.util.PluginUtils;
import br.com.anteros.persistence.metadata.EntityCache;
import br.com.anteros.persistence.metadata.descriptor.DescriptionField;

public class Entities extends TreeNode {

	public static final String[] ENTITIES = new String[] { "Entities" };

	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_ENTITIES);
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Entity))
			throw new ModelException("O objeto Entities aceita apenas objetos do tipo Entity. Objeto recebido "
					+ node.getClass().getName() + " -> " + Arrays.toString(node.getName()));
		super.addNode(node);
	}

	@Override
	public String[] getName() {
		return ENTITIES;
	}

	@Override
	public void initialized() {
		try {
			AnterosHelpmePlugin.getDefault().setWaitCursor();
			this.removeAll();
			IJavaProject javaProject = PluginUtils.findJavaProject(getProject().getProjectName());

			ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
			URLClassLoader newClassLoader = PluginUtils.getProjectClassLoader(javaProject);
			try {
				Thread.currentThread().setContextClassLoader(newClassLoader);

				Iterator<Class<? extends Serializable>> iterator = getProject().getSession()
						.getEntityCacheManager().getEntities().keySet().iterator();
				Class<?> clazz;
				EntityCache entityCache;
				while (iterator.hasNext()) {
					clazz = iterator.next();
					entityCache = getProject().getSession().getEntityCacheManager().getEntityCache(clazz);
					Entity entity = new Entity();
					entity.setName(new String[] { entityCache.getEntityClass().getName() });
					entity.setAnnotationCache(entityCache);
					this.addNode(entity);
					for (DescriptionField field : entityCache.getDescriptionFields()) {
						Component comp = new Component(new String[] { field.getField().getName() }, field);
						entity.addNode(comp);
					}
				}
				setInitialized(true);
			} finally {
				AnterosHelpmePlugin.getDefault().setDefaultCursor();
				Thread.currentThread().setContextClassLoader(oldLoader);
			}
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error("Não foi possível inicializar as Entidades do banco de dados.", e, true);
		}
	}

	public Project getProject() {
		return (Project) this.getParent();
	}

}