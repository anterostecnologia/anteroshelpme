package br.com.anteros.helpme.sql.editors;

import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

import br.com.anteros.helpme.model.Project;
import br.com.anteros.persistence.sql.format.SqlFormatRule;
import br.com.anteros.persistence.sql.parser.INode;
import br.com.anteros.persistence.sql.parser.node.CommaNode;
import br.com.anteros.persistence.sql.parser.node.StatementNode;

public abstract class StatementProcessor {

	protected String wordGroup;
	protected String word;
	protected int documentOffset;
	protected List<ICompletionProposal> proposals;
	protected int scope;
	private boolean afterPoint;
	protected Project project;
	protected SqlFormatRule rule;

	public StatementProcessor(Project project, List<ICompletionProposal> proposals, int documentOffset, String word,
			String wordGroup, boolean afterPoint, int scope, SqlFormatRule rule) {
		this.project = project;
		this.proposals = proposals;
		this.documentOffset = documentOffset;
		this.word = word;
		this.wordGroup = wordGroup;
		this.scope = scope;
		this.afterPoint = afterPoint;
		this.rule = rule;
	}

	public String getWordGroup() {
		return wordGroup;
	}

	public void setWordGroup(String wordGroup) {
		this.wordGroup = wordGroup;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getDocumentOffset() {
		return documentOffset;
	}

	public void setDocumentOffset(int documentOffset) {
		this.documentOffset = documentOffset;
	}

	public List<ICompletionProposal> getProposals() {
		return proposals;
	}

	public void setProposals(List<ICompletionProposal> proposals) {
		this.proposals = proposals;
	}
	
	protected int getSizeRemoveComma(INode target) {
		int cnt = 0;
		for (int i = 0; i < target.getChildrenSize(); i++) {
			INode node = target.getChild(i);
			if (!(node instanceof CommaNode)) {
				cnt++;
			}
		}
		return cnt;
	}
	
	public abstract void createProposals(StatementNode statement);

	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	public boolean isAfterPoint() {
		return afterPoint;
	}

	public void setAfterPoint(boolean afterPoint) {
		this.afterPoint = afterPoint;
	}

	public SqlFormatRule getRule() {
		return rule;
	}

	public void setRule(SqlFormatRule rule) {
		this.rule = rule;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	

}
