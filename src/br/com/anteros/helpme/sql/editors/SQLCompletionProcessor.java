package br.com.anteros.helpme.sql.editors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.model.Project;
import br.com.anteros.helpme.util.PluginUtils;
import br.com.anteros.helpme.util.StringUtils;
import br.com.anteros.persistence.schema.definition.StoredFunctionSchema;
import br.com.anteros.persistence.schema.definition.StoredParameterSchema;
import br.com.anteros.persistence.schema.definition.StoredProcedureSchema;
import br.com.anteros.persistence.schema.definition.type.StoredParameterType;
import br.com.anteros.persistence.sql.format.SqlFormatRule;
import br.com.anteros.persistence.sql.parser.INode;
import br.com.anteros.persistence.sql.parser.ParserVisitor2;
import br.com.anteros.persistence.sql.parser.SqlParser;
import br.com.anteros.persistence.sql.parser.node.RootNode;
import br.com.anteros.persistence.sql.parser.node.StatementNode;

public class SQLCompletionProcessor extends TemplateCompletionProcessor implements IContentAssistProcessor {

	private static final class ProposalComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			return ((TemplateProposal) o2).getRelevance() - ((TemplateProposal) o1).getRelevance();
		}
	}

	private static final Comparator proposalComparator = new ProposalComparator();

	protected Project project;
	protected SqlFormatRule rule = new SqlFormatRule();

	public SQLCompletionProcessor(Project project) {
		this.project = project;
	}

	private INode currentNode;

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer textViewer, int documentOffset) {
		List<ICompletionProposal> result = new ArrayList<ICompletionProposal>();

		IDocument doc = textViewer.getDocument();

		String word = PluginUtils.getPreviousWord(doc, documentOffset).toLowerCase();
		String wordGroup = PluginUtils.getPreviousWordGroup(doc, documentOffset).toLowerCase();
		boolean isAfterPoint = PluginUtils.isAfterPoint(doc, documentOffset);

		CurrentSql cs = new CurrentSql(doc, documentOffset, ";");
		String allSql = cs.getSql();
		if (allSql != null && !"".equals(allSql.trim())) {
			SqlParser parser = new SqlParser(allSql, rule);
			ParserVisitor2 visitor = new ParserVisitor2();
			INode node = new RootNode();
			parser.parse(node);
			node.accept(visitor, null);
			currentNode = findCurrentNode(visitor, cs.getOffsetSql());

			StatementNode statement = null;
			if ((statement = (StatementNode) PluginUtils.findParent(currentNode, "SelectStatementNode")) != null) {
				SelectStatementProcessor processor = new SelectStatementProcessor(project, result, documentOffset,
						word, wordGroup, isAfterPoint, currentNode.getScope(), rule);
				processor.createProposals(statement);
			} else if ((statement = (StatementNode) PluginUtils
					.findParent(currentNode, "InsertStatementNode")) != null) {
				InsertStatementProcessor processor = new InsertStatementProcessor(project, result, documentOffset,
						word, wordGroup, isAfterPoint, currentNode.getScope(), rule);
				processor.createProposals(statement);
			} else if ((statement = (StatementNode) PluginUtils
					.findParent(currentNode, "UpdateStatementNode")) != null) {
				UpdateStatementProcessor processor = new UpdateStatementProcessor(project, result, documentOffset,
						word, wordGroup, isAfterPoint, currentNode.getScope(), rule);
				processor.createProposals(statement);
			} else if ((statement = (StatementNode) PluginUtils
					.findParent(currentNode, "DeleteStatementNode")) != null) {
				DeleteStatementProcessor processor = new DeleteStatementProcessor(project, result, documentOffset,
						word, wordGroup, isAfterPoint, currentNode.getScope(), rule);
				processor.createProposals(statement);
			} else {
			}
		}

		ICompletionProposal[] templates = super.computeCompletionProposals(textViewer, documentOffset);
		addTemplateProposal(result, templates, word, isAfterPoint);

		ICompletionProposal[] tFunctions = computeCompletionProposalsForFunction(textViewer, documentOffset);
		addTemplateProposal(result, tFunctions, word, isAfterPoint);

		return result.toArray(new ICompletionProposal[] {});
	}

	public ICompletionProposal[] computeCompletionProposalsForFunction(ITextViewer viewer, int offset) {
		ITextSelection selection = (ITextSelection) viewer.getSelectionProvider().getSelection();
		if (selection.getOffset() == offset)
			offset = selection.getOffset() + selection.getLength();
		String prefix = extractPrefix(viewer, offset);
		Region region = new Region(offset - prefix.length(), prefix.length());
		TemplateContext context = createContextForFunction(viewer, region);
		if (context == null)
			return new ICompletionProposal[0];
		context.setVariable("selection", selection.getText());
		Template[] templates = getTemplates(context.getContextType().getId());
		List matches = new ArrayList();
		for (int i = 0; i < templates.length; i++) {
			Template template = templates[i];
			boolean isNativeFunction = false;
			if (template instanceof SQLTemplate)
				isNativeFunction = ((SQLTemplate)template).isNativeFunction();
				
			try {
				context.getContextType().validate(template.getPattern());
			} catch (TemplateException e) {
				continue;
			}
			if (template.getName().toUpperCase().startsWith(prefix))
				matches.add(createProposalForFunction(template, context, (IRegion) region,
						getRelevance(template, prefix),isNativeFunction));
		}

		Collections.sort(matches, proposalComparator);

		return (ICompletionProposal[]) matches.toArray(new ICompletionProposal[matches.size()]);
	}

	protected TemplateContext createContextForFunction(ITextViewer viewer, IRegion region) {
		TemplateContextType contextType = getContextTypeForFunction(viewer, region);
		if (contextType != null) {
			IDocument document = viewer.getDocument();
			return new SQLTemplateContext(contextType, document, region.getOffset(), region.getLength());
		}
		return null;
	}

	protected TemplateContextType getContextTypeForFunction(ITextViewer viewer, IRegion region) {
		return new TemplateContextType("anteros.function");
	}

	protected ICompletionProposal createProposalForFunction(Template template, TemplateContext context, IRegion region,
			int relevance, boolean isNativeFunction) {
		return new SQLTemplateProposal(template, context, region, (isNativeFunction ? AnterosHelpmePlugin.getDefault()
				.getImage(AnterosHelpmePlugin.IMG_FUNCTION_NATIVE) : AnterosHelpmePlugin.getDefault().getImage(
				AnterosHelpmePlugin.IMG_FUNCTION)), relevance);
	}

	private void addTemplateProposal(List proposals, ICompletionProposal[] templates, String word, boolean isAfterPoint) {
		if (templates != null) {
			if (isAfterPoint)
				word = "";
			int len = word.length();
			for (int i = 0; i < templates.length; i++) {
				ICompletionProposal template = templates[i];
				String str = template.getDisplayString();
				String value = StringUtils.subString(str, len);
				if (value != null && value.compareToIgnoreCase(word) == 0) {
					proposals.add(template);
				}
			}
		}
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

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer textViewer, int documentOffset) {
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '{', '.', '#' };
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	protected TemplateContextType getContextType(ITextViewer textViewer, IRegion region) {
		return null;
	}

	@Override
	protected Image getImage(Template template) {
		return null;
	}

	@Override
	protected Template[] getTemplates(String contextTypeId) {
		List<Template> result = new ArrayList<Template>();
		try {
			Set<StoredFunctionSchema> functions = project.getSchemaManager().getFunctions();
			StringBuffer sb;
			for (StoredProcedureSchema function : functions) {
				SQLTemplate template = new SQLTemplate();
				template.setContextTypeId(contextTypeId);
				template.setName(function.getName());
				template.setNativeFunction(false);
				sb = new StringBuffer();
					sb.append(function.getName());
					if (function.getParameters().size() > 0)
						sb.append("(");
					boolean append = false;
					for (StoredParameterSchema parameter : function.getParameters()) {
						if ((parameter.getParameterType() == StoredParameterType.IN) || (parameter.getParameterType() == StoredParameterType.IN_OUT)) {
							if (append)
								sb.append(",");

							sb.append("${").append(parameter.getName()).append("}");
							append = true;
						}
					}
					if (function.getParameters().size() > 0)
						sb.append(")");
					template.setPattern(sb.toString());
				result.add(template);
			}

			Set<StoredProcedureSchema> procedures = project.getSchemaManager().getProcedures();
			sb=null;
			for (StoredProcedureSchema procedure : procedures) {
				SQLTemplate template = new SQLTemplate();
				template.setContextTypeId(contextTypeId);
				template.setName(procedure.getName());
				template.setNativeFunction(false);
				sb = new StringBuffer();
					sb.append(procedure.getName());
					if (procedure.getParameters().size() > 0)
						sb.append("(");
					boolean append = false;
					for (StoredParameterSchema parameter : procedure.getParameters()) {
						if (append)
							sb.append(",");
						sb.append("${").append(parameter.getName()).append("}");
						append = true;
					}
					if (procedure.getParameters().size() > 0)
						sb.append(")");
					template.setPattern(sb.toString());
				result.add(template);
			}
		} catch (Exception e) {
			AnterosHelpmePlugin.error("Não foi criar os templates para Procedures/Functions usadas no auto completar.", e, true);
		}
		return result.toArray(new Template[] {});
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
