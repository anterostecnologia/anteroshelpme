package br.com.anteros.helpme.model;

import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.metadata.descriptor.DescriptionField;

public class VariableComponent extends TreeNode {

	private DescriptionField descriptionField;
	private Object source;

	public VariableComponent(Object source, DescriptionField descriptionfield) {
		this.source = source;
		this.descriptionField = descriptionfield;
	}

	@Override
	public Image getLeftImage() {
		if (descriptionField.isPrimaryKey() && (!descriptionField.isRelationShip())) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_COMPONENT_ID);
		} else if (descriptionField.isCollectionEntity()) {
			if (ReflectionUtils.isImplementsInterface(descriptionField.getField().getType(), Set.class)) {
				return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_ENTITY_SET);
			} else if (ReflectionUtils.isImplementsInterface(descriptionField.getField().getType(), List.class)) {
				return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_ENTITY_LIST);
			}
		} else if (descriptionField.isCollectionMapTable()) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_COLLECTION_MAP);
		} else if (descriptionField.isCollectionTable()) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_COLLECTION_LIST);
		} else if (descriptionField.isEnumerated()) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_ENUM);
		} else if (descriptionField.isVersioned()) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_VERSION);
		} else if (descriptionField.isTemporalDate()) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_DATE);
		} else if (descriptionField.isTemporalTime()) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_TIME);
		} else if (descriptionField.isNumber()) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_NUMBER);
		} else if (descriptionField.isString()) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_STRING);
		}

		return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_COMPONENT);
	}

	@Override
	public boolean hasChildren() {
		if (descriptionField.isCollection() || descriptionField.isJoinTable() || descriptionField.isRelationShip())
			return true;
		return false;

	}
	
	@Override
	public void uninitialized() {
	}

	
}
