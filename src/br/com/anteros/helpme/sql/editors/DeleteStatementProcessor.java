package br.com.anteros.helpme.sql.editors;

import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

import br.com.anteros.helpme.model.Project;
import br.com.anteros.persistence.sql.format.SqlFormatRule;
import br.com.anteros.persistence.sql.parser.node.StatementNode;

public class DeleteStatementProcessor extends StatementProcessor {

	public DeleteStatementProcessor(Project project, List<ICompletionProposal> proposals, int documentOffset, String word,
			String wordGroup, boolean afterPoint, int scope, SqlFormatRule rule) {
		super(project, proposals, documentOffset, word, wordGroup, afterPoint, scope, rule);
	}

	@Override
	public void createProposals(StatementNode statement) {
		
	}

}