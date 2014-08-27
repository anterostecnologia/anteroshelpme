package br.com.anteros.helpme.actions;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.ui.part.EditorPart;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.model.Project;
import br.com.anteros.helpme.sql.editors.SQLParameter;
import br.com.anteros.helpme.sql.editors.SQLParameterEditor;
import br.com.anteros.helpme.util.PluginUtils;
import br.com.anteros.helpme.util.SQLParameterUtil;
import br.com.anteros.helpme.util.StringUtils;
import br.com.anteros.helpme.views.SQLEditorView;
import br.com.anteros.persistence.parameter.NamedParameter;
import br.com.anteros.persistence.parameter.NamedParameterParserResult;
import br.com.anteros.persistence.session.SQLSessionResult;
import br.com.anteros.persistence.sql.format.SqlFormatRule;
import br.com.anteros.persistence.sql.parser.INode;
import br.com.anteros.persistence.sql.parser.ParserUtil;
import br.com.anteros.persistence.sql.parser.ParserVisitor2;
import br.com.anteros.persistence.sql.parser.SqlParser;
import br.com.anteros.persistence.sql.parser.node.RootNode;
import br.com.anteros.persistence.sql.statement.NamedParameterStatement;

public class ExecuteSQLAction extends AbstractAction {

	public ExecuteSQLAction(EditorPart editor) {
		super(editor, "&Execute SQL", SWT.NONE);
		this.setToolTipText("Execute SQL");
		this.setAccelerator(SWT.F9);
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault()
				.getImageDescriptor(AnterosHelpmePlugin.IMG_EXECUTE_SQL));
	}

	@Override
	public boolean isAvailable() {
		return (getView().getText().length() > 0);
	}

	@Override
	public void run() {
		if (getView().getText().length() == 0) {
			AnterosHelpmePlugin.showInformationMessage("Informe a instrução SQL.");
		} else {
			Project selectedProject = getView().getSelectedProject();
			IJavaProject javaProject = PluginUtils.findJavaProject(selectedProject.getProjectName());

			ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
			try {
				AnterosHelpmePlugin.getDefault().setWaitCursor();
				URLClassLoader newClassLoader = PluginUtils.getProjectClassLoader(javaProject);
				Thread.currentThread().setContextClassLoader(newClassLoader);

				SqlParser parser = new SqlParser(getView().getText(), new SqlFormatRule());
				ParserVisitor2 visitor = new ParserVisitor2();
				INode node = new RootNode();
				parser.parse(node);
				node.accept(visitor, null);

				Set<SQLParameter> parametersType = new LinkedHashSet<SQLParameter>();
				INode[] selects = ParserUtil.findChildren(node, "SelectStatementNode");
				if (selects != null) {
					for (INode select : selects) {
						parametersType.addAll(getSQLParserBind(selectedProject, select));
					}
				}

				NamedParameterParserResult resultParams = NamedParameterStatement.parse(getView().getText(), null);
				List<NamedParameter> namedParameters = resultParams.getNamedParameters();
				int ok = IDialogConstants.OK_ID;
				if (namedParameters.size() > 0) {
					SQLParameterEditor parameterEditor = new SQLParameterEditor(AnterosHelpmePlugin.getDefault()
							.getShell(), namedParameters, parametersType);
					ok = parameterEditor.open();
				}

				if (ok == IDialogConstants.OK_ID) {
					AnterosHelpmePlugin.getDefault().getDefaultConsoleView().setProject(selectedProject);
					selectedProject.getSession().setFormatSql(
							AnterosHelpmePlugin.getDefault().getDefaultConsoleView().isFormatSql());
					selectedProject.getSession().setShowSql(
							AnterosHelpmePlugin.getDefault().getDefaultConsoleView().isShowSql());
					selectedProject.getSession().addListener(getView());
					AnterosHelpmePlugin.getDefault().setWaitCursor();
					SQLSessionResult<?> result = selectedProject
							.getSession()
							.createQuery(getView().getText(), getView().getSelectedResultClass(),
									namedParameters.toArray(new NamedParameter[] {})).getResultListAndResultSet();
					getView().setResultData(result);

					AnterosHelpmePlugin
							.getDefault()
							.getSQLHistoryManager()
							.addHistory(getView().getSelectedProject().getSimpleName(),
									getView().getSelectedResultClass().getName(), getView().getText(), namedParameters);
				}

			} catch (Exception e) {
				AnterosHelpmePlugin.getDefault().setDefaultCursor();
				AnterosHelpmePlugin.error("Não foi possível executar o SQL.", e, true);
			} finally {
				AnterosHelpmePlugin.getDefault().setDefaultCursor();
				Thread.currentThread().setContextClassLoader(oldLoader);
			}
		}
	}

	private Set<SQLParameter> getSQLParserBind(Project project, INode node) throws Exception {
		INode[] tables = ParserUtil.findChildren(node, "TableNode");
		Set<SQLParameter> result = new LinkedHashSet<SQLParameter>();
		INode[] binds = ParserUtil.findChildren(node, "BindNode");
		List<INode> processedNodes = new ArrayList<INode>();
		for (INode bind : binds) {
			SQLParameter sqlParameter = SQLParameterUtil.getSQLParameter(project, bind,
					ParserUtil.findBindParent(bind), tables, processedNodes);
			if (sqlParameter != null)
				result.add(sqlParameter);
		}
		return result;
	}

	private INode findCurrentNode(ParserVisitor2 visitor, String offsetSql) {
		int nodeOffset = StringUtils.endWordPosition(offsetSql);
		INode node = visitor.findNodeByOffset(nodeOffset);
		if (node != null) {
			return node;
		} else {
			return findCurrentNode(visitor, offsetSql.substring(0, nodeOffset));
		}
	}

	public SQLEditorView getView() {
		return ((SQLEditorView) targetObject);
	}

}