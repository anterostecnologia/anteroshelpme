package br.com.anteros.helpme.sql.editors;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.model.Project;
import br.com.anteros.helpme.util.PluginUtils;
import br.com.anteros.persistence.metadata.EntityCache;
import br.com.anteros.persistence.metadata.descriptor.DescriptionField;
import br.com.anteros.persistence.schema.definition.TableSchema;
import br.com.anteros.persistence.schema.definition.ViewSchema;
import br.com.anteros.persistence.sql.format.SqlFormatRule;
import br.com.anteros.persistence.sql.parser.INode;
import br.com.anteros.persistence.sql.parser.SqlParser;
import br.com.anteros.persistence.sql.parser.node.AliasNode;
import br.com.anteros.persistence.sql.parser.node.ColumnNode;
import br.com.anteros.persistence.sql.parser.node.FromNode;
import br.com.anteros.persistence.sql.parser.node.ParenthesesNode;
import br.com.anteros.persistence.sql.parser.node.SelectNode;
import br.com.anteros.persistence.sql.parser.node.SelectStatementNode;
import br.com.anteros.persistence.sql.parser.node.StatementNode;
import br.com.anteros.persistence.sql.parser.node.TableNode;

public class SelectStatementProcessor extends StatementProcessor {

	public SelectStatementProcessor(Project project, List<ICompletionProposal> proposals, int documentOffset,
			String word, String wordGroup, boolean afterPoint, int currentSQL, SqlFormatRule rule) {
		super(project, proposals, documentOffset, word, wordGroup, afterPoint, currentSQL, rule);
	}

	public void createProposals(StatementNode statement) {
		try {
			FromNode fromList = (FromNode) PluginUtils.findFirstChild(statement, "FromNode");
			int fromItemCount = fromList != null ? super.getSizeRemoveComma(fromList) : 0;

			switch (scope) {
			case SqlParser.SCOPE_SELECT:
			case SqlParser.SCOPE_WHERE:
			case SqlParser.SCOPE_BY:
				if (fromItemCount == 0) {
					break;
				} else if (fromItemCount == 1) {
					if (this.isAfterPoint()) {
						createColumnProposal(PluginUtils.findFromNode(fromList, word));
					} else {
						int _offset = wordGroup.indexOf('.');
						int _offsetExpression = wordGroup.indexOf('{');
						if (_offset > 0) {
							String table = wordGroup.substring(0, _offset);
							createColumnProposal(PluginUtils.findFromNode(fromList, table));
						} else if (_offsetExpression == 0) {
							createTableProposal(PluginUtils.getFromNodes(fromList));
						} else {
							createColumnProposal(fromList.getChild(0));
							createTableProposal(PluginUtils.getFromNodes(fromList));
						}
					}
					break;
				} else {
					if (this.isAfterPoint()) {
						if (wordGroup.indexOf("#") > 0) {
							createFieldProposal(wordGroup);
						} else
							createColumnProposal(PluginUtils.findFromNode(fromList, word));
					} else {
						int _offset = wordGroup.lastIndexOf('.');
						int _offsetExpression = wordGroup.lastIndexOf('#');
						if ((_offsetExpression > 0) && (_offset > 0)) {
							String w_str = wordGroup.substring(0, _offset);
							createFieldProposal(w_str);
						} else if (_offsetExpression > 0) {
							createClassProposal(PluginUtils.getFromNodes(fromList));
						} else if (_offset > 0) {
							String w_str = wordGroup.substring(0, _offset);
							int _offset2 = w_str.lastIndexOf('.');
							if (_offset2 > 0) {
								createColumnProposal(PluginUtils.findFromNode(fromList, w_str));
							} else {
								createColumnProposal(PluginUtils.findFromNode(fromList, w_str));
							}
						} else {
							createTableProposal(PluginUtils.getFromNodes(fromList));
						}
					}
					break;
				}
			case SqlParser.SCOPE_FROM:
				if (!this.isAfterPoint()) {
					int _offset = wordGroup.indexOf('.');
					if (_offset <= 0) {
						SQLCreatorProposal.addTablesProposal(proposals, new ArrayList<TableSchema>(project.getSchemaManager().getTables()), word,
								documentOffset, isAfterPoint(), false);
						SQLCreatorProposal.addViewsProposal(proposals, new ArrayList<ViewSchema>(project.getSchemaManager().getViews()), word,
								documentOffset, isAfterPoint(), false);
					}
				}
				break;
			}
		} catch (Exception e) {
			AnterosHelpmePlugin.error("Não foi criar as sugestões para o auto completar.", e, true);
		} finally {
		}
	}

	private void createFieldProposal(String expression) {
		expression = StringUtils.replace(expression, "{", "");
		expression = StringUtils.replace(expression, "}", "");
		String tableAlias = "";
		String newExpression = expression;
		if (expression.indexOf("#") > 0) {
			tableAlias = expression.substring(0, expression.indexOf("#"));
			newExpression = expression.substring(expression.indexOf("#") + 1);
		}

		String[] split = newExpression.split("\\.");

		EntityCache[] caches = new EntityCache[] { project.getSession().getEntityCacheManager()
				.getEntityCacheByClassName(split[0]) };
		if (caches[0] == null) {
			return;
		}
		String attribute;
		for (int i = 0, size = split.length; i < size; i++) {
			attribute = split[i];
			if ((size - 1) == i) {
				caches = new EntityCache[] { project.getSession().getEntityCacheManager()
						.getEntityCacheByClassName(split[i]) };
				if (caches[0] == null) {
					return;
				}
				Set<DescriptionField> fields = new LinkedHashSet<DescriptionField>();
				for (int j = 0; j < caches.length; j++) {
					fields.addAll(caches[j].getDescriptionFields());
				}
				SQLCreatorProposal.addFieldsProposal(proposals, fields, word, documentOffset, isAfterPoint());
			}
		}
	}

	protected void createColumnProposal(INode target) {
		if (target != null) {
			if (target instanceof TableNode) {
				createColumn((TableNode) target);

			} else if (target instanceof SelectStatementNode) {
				createColumn((SelectStatementNode) target);

			} else if (target instanceof ParenthesesNode) {
				SelectStatementNode select = (SelectStatementNode) PluginUtils.findFirstChild(target,
						"SelectStatementNode");
				if (select != null) {
					createColumnProposal(select);
				} else {
					throw new IllegalStateException("Unknown node");
				}
			}
		}

	}

	void createColumn(TableNode target) {
		if (target != null) {
			if ((project != null) && (project.isInitialized())) {
				String tableName = ((TableNode) target).getTableName();
				TableSchema table = project.getSchemaManager().getTable(tableName);
				if (table != null)
					SQLCreatorProposal.addColumnsProposal(proposals, table.getColumns(), word, documentOffset,
							isAfterPoint());
			}
		}

	}

	void createColumn(SelectStatementNode target) {
		if (target != null) {
			String alias = target.getAliasName();
			SelectNode selectList = (SelectNode) target.getChild("SelectNode");

			int count = getSizeRemoveComma(selectList);
			String[][] colInfo = new String[count][2];

			int index = 0;
			for (int i = 0; i < selectList.getChildrenSize(); i++) {
				INode node = selectList.getChild(i);
				StringBuffer sb = new StringBuffer();
				if (node instanceof ColumnNode) {
					ColumnNode column = (ColumnNode) node;
					String columnName = column.getAliasName();
					sb.append(columnName);

					if (alias != null) {
						sb.append("\\ - SubQuery [");
						sb.append(alias);
						sb.append("]'s");
						sb.append(columnName);
					}

					colInfo[index][0] = columnName;
					colInfo[index][1] = sb.toString();

					index++;
				}
			}
			SQLCreatorProposal.addProposal(proposals, colInfo, word, documentOffset, isAfterPoint());
		}

	}

	private void createTableProposal(INode[] target) throws Exception {
		if ((target != null) && (project != null) && (project.isInitialized())) {
			List<TableSchema> result = new ArrayList<TableSchema>();
			for (int i = 0; i < target.length; i++) {
				INode node = target[i];
				if (node instanceof TableNode) {
					TableNode table = (TableNode) node;
					TableSchema tableSchema = project.getSchemaManager().getTable(table.getName());
					if (tableSchema != null) {
						tableSchema.setAlias(null);
						if (table.hasAlias())
							tableSchema.setAlias(table.getAliasName());
						result.add(tableSchema);
					}
				} else if (node instanceof AliasNode) {
					AliasNode alias = (AliasNode) node;
					if (alias.getAliasName() != null) {
						// list.add(new TableInfo(alias.getAliasName(),
						// Messages.getString("SelectProcessor.1")));
						throw new Exception("chegou no Alias");
					}
				}
			}
			SQLCreatorProposal.addTablesProposal(proposals, result, word, documentOffset, isAfterPoint(), true);

		}
	}

	private void createClassProposal(INode[] target) throws Exception {
		if ((target != null) && (project != null) && (project.isInitialized())) {
			List<EntityCache> result = new ArrayList<EntityCache>();
			for (int i = 0; i < target.length; i++) {
				INode node = target[i];
				if (node instanceof TableNode) {
					TableNode table = (TableNode) node;
					List<EntityCache> caches = project.getSession().getEntityCacheManager()
							.getEntityCachesByTableName(table.getTableName());
					if (caches != null) {
						result.addAll(caches);
					}
				} else if (node instanceof AliasNode) {
					AliasNode alias = (AliasNode) node;
					if (alias.getAliasName() != null) {
						throw new Exception("chegou no Alias");
					}
				}
			}
			SQLCreatorProposal.addClassProposal(proposals, result, word, documentOffset, isAfterPoint(), true);

		}
	}

	/*
	 * protected boolean addTableProposalBySchema(String inputWord) throws
	 * Exception { String correctSchemaName = ci.findCorrectSchema(inputWord);
	 * if (correctSchemaName != null) {
	 * SQLCreatorProposal.addProposal(proposals,
	 * ci.getTableInfo(correctSchemaName), word, documentOffset,
	 * isAfterPoint()); return true; } else { return false; } }
	 */

}
