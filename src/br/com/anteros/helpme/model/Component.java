package br.com.anteros.helpme.model;

import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.persistence.metadata.descriptor.DescriptionColumn;
import br.com.anteros.persistence.metadata.descriptor.DescriptionField;

public class Component extends TreeNode {

	private DescriptionField descriptionField;

	public Component(String[] name, DescriptionField descriptionField) {
		this.name = name;
		this.descriptionField = descriptionField;
	}

	public Image getLeftImage() throws Exception {
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
	public String getColumnName() {
		if (!descriptionField.isCollection() && (!descriptionField.isJoinTable())) {
			StringBuffer columnName = new StringBuffer();
			boolean append = false;
			for (DescriptionColumn column : descriptionField.getDescriptionColumns()) {
				if (append)
					columnName.append(", ");
				columnName.append(column.getColumnName());
				append = true;
			}
			return columnName.toString();
		}
		return "";
	}

	public Image getRightImage() throws Exception {
		if (descriptionField.isRelationShip()) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_ONE_TO_ONE);
		} else if (descriptionField.isCollectionEntity()) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_ONE_TO_MANY);
		} else if (descriptionField.isJoinTable()) {
			return AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_MANY_TO_MANY);
		}
		return null;
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Validator))
			throw new ModelException("O objeto Component aceita apenas objetos do tipo Validator. Objeto recebido "
					+ node.getClass().getName() + " -> " + node.getName());
		super.addNode(node);
	}

	@Override
	public boolean hasChildren() {
		if (descriptionField.isCollection() || descriptionField.isJoinTable() || descriptionField.isRelationShip())
			return true;
		return false;
	}

	public DescriptionField getDescriptionField() {
		return descriptionField;
	}


}