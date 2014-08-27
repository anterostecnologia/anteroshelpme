package br.com.anteros.helpme.sql.editors;

import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

import br.com.anteros.helpme.model.Project;
import br.com.anteros.persistence.sql.format.SqlFormatRule;
import br.com.anteros.persistence.sql.parser.node.StatementNode;

public class UpdateStatementProcessor extends StatementProcessor {

	public UpdateStatementProcessor(Project project, List<ICompletionProposal> proposals, int documentOffset, String word,
			String wordGroup, boolean isAfterPoint, int scope, SqlFormatRule rule) {
		super(project, proposals, documentOffset, word, wordGroup, isAfterPoint, scope,rule);
	}

	@Override
	public void createProposals(StatementNode statement) {
		
	}

}