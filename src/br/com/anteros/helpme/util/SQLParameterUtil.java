package br.com.anteros.helpme.util;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import br.com.anteros.helpme.model.Project;
import br.com.anteros.helpme.sql.editors.SQLParameter;
import br.com.anteros.persistence.schema.definition.ColumnSchema;
import br.com.anteros.persistence.schema.definition.StoredFunctionSchema;
import br.com.anteros.persistence.schema.definition.StoredParameterSchema;
import br.com.anteros.persistence.schema.definition.TableSchema;
import br.com.anteros.persistence.sql.parser.INode;
import br.com.anteros.persistence.sql.parser.ParserUtil;
import br.com.anteros.persistence.sql.parser.node.ColumnNode;
import br.com.anteros.persistence.sql.parser.node.ExpressionNode;
import br.com.anteros.persistence.sql.parser.node.FunctionNode;
import br.com.anteros.persistence.sql.parser.node.KeywordNode;
import br.com.anteros.persistence.sql.parser.node.TableNode;
import br.com.anteros.persistence.sql.parser.node.WhereNode;

public class SQLParameterUtil {

	public static SQLParameter getSQLParameter(Project project, INode bind, INode nextParent, INode[] tables,
			List<INode> processedNodes) throws Exception {
		String parameterName = StringUtils.replace(bind.getName(), ":", "");
		if (nextParent == null)
			return new SQLParameter(parameterName, Types.OTHER);
		if (nextParent instanceof FunctionNode) {
			String functionName = nextParent.getName();
			StoredFunctionSchema procedure = project.getSchemaManager().getFunction(functionName);
			if (procedure == null)
				return new SQLParameter(parameterName, Types.OTHER);

			INode[] parameters = ParserUtil.findChildren(nextParent, "BindNode");
			StoredParameterSchema parameter = null;
			if (parameters != null) {
				int index = Arrays.asList(parameters).indexOf(bind);
				if (procedure.getParameters().size() > index) {
					int i = 0;
					for (StoredParameterSchema p : procedure.getParameters()) {
						if (i == index) {
							parameter = p;
							break;
						}
						i++;
					}
				}
			}
			if (parameter == null)
				return new SQLParameter(parameterName, Types.OTHER);
			return new SQLParameter(parameterName, parameter.getDataTypeSql());
		} else if (nextParent instanceof WhereNode) {
			INode column = null;
			boolean found = false;
			int i = 0;
			for (INode node : nextParent.getChildren()) {
				if (processedNodes.contains(node)) {
					i++;
					continue;
				}
				boolean addNode = true;
				if (nextParent.getChildrenSize() > i + 1) {
					INode nextNode = nextParent.getChild(i + 1);
					if (nextNode instanceof KeywordNode) {
						if (nextNode.getName().equalsIgnoreCase("BETWEEN"))
							addNode = false;
					}
				}
				if (addNode)
					processedNodes.add(node);

				if (node.getClass().getName().equals(ColumnNode.class.getName()))
					column = node;
				if (node.equals(bind))
					found = true;

				if ((found) && (column != null))
					break;
				i++;
			}
			if (column != null) {
				return getSQLParameter(project, tables, parameterName, column);
			}
			return new SQLParameter(parameterName, Types.OTHER);

		} else if (nextParent instanceof ExpressionNode) {
			INode column = ParserUtil.findFirstChild(nextParent, "ColumnNode");
			if (column != null) {
				return getSQLParameter(project, tables, parameterName, column);
			}
			return new SQLParameter(parameterName, Types.OTHER);
		} else {
			return new SQLParameter(parameterName, Types.OTHER);
		}
	}

	private static SQLParameter getSQLParameter(Project project, INode[] tables, String parameterName, INode column) {
		if (StringUtils.isNotEmpty(((ColumnNode) column).getTableName())) {
			String tableName = "";
			String alias = ((ColumnNode) column).getTableName();
			for (INode table : tables) {
				if (alias.equalsIgnoreCase(((TableNode) table).getAliasName())) {
					tableName = table.getName();
					break;
				}
			}
			if (StringUtils.isNotEmpty(tableName)) {
				TableSchema table = project.getSchemaManager().getTable(tableName);
				if (table != null) {
					ColumnSchema c = table.getColumn(((ColumnNode) column).getColumnName());
					if (c != null) {
						return new SQLParameter(parameterName, c.getDataTypeSql());
					}
				}
			}
		} else {
			for (INode table : tables) {
				TableSchema tmd = project.getSchemaManager().getTable(table.getName());
				if (tmd != null) {
					ColumnSchema c = tmd.getColumn(((ColumnNode) column).getColumnName());
					if (c != null) {
						return new SQLParameter(parameterName, c.getDataTypeSql());
					}
				}
			}
		}
		return null;
	}
}
