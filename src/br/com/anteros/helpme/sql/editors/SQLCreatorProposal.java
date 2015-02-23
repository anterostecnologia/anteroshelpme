package br.com.anteros.helpme.sql.editors;

import java.util.List;
import java.util.Set;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.util.StringUtils;
import br.com.anteros.persistence.metadata.EntityCache;
import br.com.anteros.persistence.metadata.descriptor.DescriptionField;
import br.com.anteros.persistence.schema.definition.ColumnSchema;
import br.com.anteros.persistence.schema.definition.TableSchema;
import br.com.anteros.persistence.schema.definition.ViewSchema;

public class SQLCreatorProposal {

	public static void addProposal(List proposals, String[] modifiers, String word, int documentOffset,
			boolean isAfterPoint) {
		if (modifiers != null) {
			Image img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_TEMPLATE);

			if (isAfterPoint)
				word = "";

			int len = word.length();
			for (int i = 0; i < modifiers.length; i++) {
				String modifier = modifiers[i];
				String value = StringUtils.subString(modifiers[i], len);
				if (value != null && value.compareToIgnoreCase(word) == 0) {
					proposals.add(new CompletionProposal(modifier, documentOffset - len, len, modifier.length(), img,
							null, new ContextInformation(null, modifier), null));

				}
			}
		}
	}

	public static void addProposal(List proposals, String[][] modifiers, String word, int documentOffset,
			boolean isAfterPoint) {
		if (modifiers != null) {
			Image img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_CODE_UNKNOWN);

			if (isAfterPoint)
				word = "";

			int len = word.length();
			for (int i = 0; i < modifiers.length; i++) {
				String modifier = modifiers[i][0];
				String display = modifiers[i][1];
				String value = StringUtils.subString(modifier, len);
				if (value != null && value.compareToIgnoreCase(word) == 0) {
					proposals.add(new CompletionProposal(modifier, documentOffset - len, len, modifier.length(), img,
							display, new ContextInformation(null, modifier), null));

				}
			}
		}
	}

	public static void addTablesProposal(List proposals, List<TableSchema> tables, String word, int documentOffset,
			boolean isAfterPoint, boolean withAlias) {
		if (tables != null) {
			Image img = null;
			if (isAfterPoint)
				word = "";
			int len = word.length();
			for (TableSchema table : tables) {
				img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_TABLE);

				String modifier = (StringUtils.isNotEmpty(table.getAlias()) && withAlias ? table.getAlias() : table
						.getName());
				String display = modifier;

				String value = StringUtils.subString(modifier, len);
				if ((value != null && value.compareToIgnoreCase(word) == 0)) {
					proposals.add(new CompletionProposal(modifier, documentOffset - len, len, modifier.length(), img,
							display, new ContextInformation(null, modifier), null));

				} else if ((value != null) && ("{".equals(word))) {
					modifier += "#";
					proposals.add(new CompletionProposal(modifier, documentOffset, 0, modifier.length(), img, display,
							new ContextInformation(null, modifier), null));
				}
			}

		}
	}

	public static void addViewsProposal(List proposals, List<ViewSchema> views, String word, int documentOffset,
			boolean isAfterPoint, boolean withAlias) {
		if (views != null) {
			Image img = null;
			if (isAfterPoint)
				word = "";
			int len = word.length();
			for (ViewSchema view : views) {
				img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_VIEW);

				String modifier = (StringUtils.isNotEmpty(view.getAlias()) && withAlias ? view.getAlias() : view
						.getName());
				String display = modifier;

				String value = StringUtils.subString(modifier, len);
				if ((value != null && value.compareToIgnoreCase(word) == 0)) {
					proposals.add(new CompletionProposal(modifier, documentOffset - len, len, modifier.length(), img,
							display, new ContextInformation(null, modifier), null));

				} else if ((value != null) && ("{".equals(word))) {
					modifier += "#";
					proposals.add(new CompletionProposal(modifier, documentOffset, 0, modifier.length(), img, display,
							new ContextInformation(null, modifier), null));
				}
			}

		}
	}

	public static void addClassProposal(List proposals, List<EntityCache> entities, String word, int documentOffset,
			boolean isAfterPoint, boolean withAlias) {
		if (entities != null) {
			String[] split = word.split("\\#");
			String value = "";
			if (split.length > 1)
				value = split[1];
			Image img = null;
			if (isAfterPoint)
				word = "";
			int len = word.length();
			for (EntityCache cache : entities) {
				img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_CLASS);

				String modifier = cache.getEntityClass().getSimpleName();
				String display = modifier;

				proposals.add(new CompletionProposal(modifier, documentOffset - value.length(), value.length(),
						modifier.length(), img, display, new ContextInformation(null, modifier), null));
			}
		}
	}

	public static void addColumnsProposal(List proposals, List<ColumnSchema> columns, String word, int documentOffset,
			boolean isAfterPoint) {
		if (columns != null) {
			if (isAfterPoint)
				word = "";
			Image img = null;
			int len = word.length();
			for (ColumnSchema column : columns) {
				if (column.isPrimaryKey())
					img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_PK_COLUMN);
				else if (column.isPrimaryKey() && column.isForeignKey())
					img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_PK_FK_COLUMN);
				else if (column.isForeignKey())
					img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_FK_COLUMN);
				else
					img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_COLUMN);
				String modifier = column.getName();
				String display = column.getName();
				String value = StringUtils.subString(modifier, len);
				if (value != null && value.compareToIgnoreCase(word) == 0) {
					proposals.add(new CompletionProposal(modifier, documentOffset - len, len, modifier.length(), img,
							display, new ContextInformation(null, modifier), null));

				}
			}
		}
	}

	public static void addFunctionsProposal(List proposals, String[] modifiers, String word, int documentOffset,
			boolean isAfterPoint) {
		if (modifiers != null) {
			Image img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_FUNCTION);
			if (isAfterPoint)
				word = "";
			int len = word.length();
			for (int i = 0; i < modifiers.length; i++) {
				String modifier = modifiers[i];
				String value = StringUtils.subString(modifiers[i], len);
				if (value != null && value.compareToIgnoreCase(word) == 0) {
					proposals.add(new CompletionProposal(modifier, documentOffset - len, len, modifier.length(), img,
							null, new ContextInformation(null, modifier), null));

				}
			}
		}
	}

	public static void addFieldsProposal(List<ICompletionProposal> proposals, Set<DescriptionField> descriptionFields,
			String word, int documentOffset, boolean isAfterPoint) {
		if (descriptionFields != null) {
			if (isAfterPoint)
				word = "";
			Image img = null;
			int len = word.length();
			for (DescriptionField descriptionField : descriptionFields) {
				if (descriptionField.isPrimaryKey() && (!descriptionField.isRelationShip())) {
					img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_COMPONENT_ID);
				} else if (descriptionField.isCollectionEntity()) {
					if (ReflectionUtils.isImplementsInterface(descriptionField.getField().getType(), Set.class)) {
						img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_ENTITY_SET);
					} else if (ReflectionUtils.isImplementsInterface(descriptionField.getField().getType(), List.class)) {
						img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_ENTITY_LIST);
					}
				} else if (descriptionField.isMapTable()) {
					img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_COLLECTION_MAP);
				} else if (descriptionField.isCollectionTable()) {
					img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_COLLECTION_LIST);
				} else if (descriptionField.isEnumerated()) {
					img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_ENUM);
				} else if (descriptionField.isVersioned()) {
					img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_VERSION);
				} else if (descriptionField.isTemporalDate()) {
					img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_DATE);
				} else if (descriptionField.isTemporalTime()) {
					img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_TIME);
				} else if (descriptionField.isNumber()) {
					img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_NUMBER);
				} else if (descriptionField.isString()) {
					img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_STRING);
				} else

					img = AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_COMPONENT);
				String modifier = descriptionField.getName();
				String display = descriptionField.getName();
				String value = StringUtils.subString(modifier, len);
				if (value != null && value.compareToIgnoreCase(word) == 0) {
					proposals.add(new CompletionProposal(modifier, documentOffset - word.length(), word.length(),
							modifier.length(), img, display, new ContextInformation(null, modifier), null));
				}

			}
		}
	}

}
