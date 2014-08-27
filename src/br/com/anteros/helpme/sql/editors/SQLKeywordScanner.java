package br.com.anteros.helpme.sql.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.model.Project;
import br.com.anteros.persistence.schema.definition.StoredFunctionSchema;
import br.com.anteros.persistence.schema.definition.StoredProcedureSchema;
import br.com.anteros.persistence.schema.definition.TableSchema;
import br.com.anteros.persistence.schema.definition.ViewSchema;
import br.com.anteros.persistence.sql.format.SqlFormatRule;
import br.com.anteros.persistence.sql.format.tokenizer.TokenUtil;
import br.com.anteros.persistence.sql.parser.SqlKeyword;

public class SQLKeywordScanner extends RuleBasedScanner implements ISQLTokenScanner {

	static class WordDetector implements IWordDetector {

		public boolean isWordPart(char c) {
			return Character.isJavaIdentifierPart(c);
		}

		public boolean isWordStart(char c) {
			return Character.isJavaIdentifierStart(c);
		}
	}

	static class WhitespaceDetector implements IWhitespaceDetector {

		public boolean isWhitespace(char character) {
			return Character.isWhitespace(character);
		}
	}

	protected SqlFormatRule rule = new SqlFormatRule();
	private SQLColorManager colorManager;
	private Project project;

	public SQLKeywordScanner(Project project, SQLColorManager colorManager) {
		this.colorManager = colorManager;
		this.project = project;
		initialize(false);
	}

	public void initialize() {
		initialize(true);
	}

	public void initialize(boolean marge) {
		setDefaultReturnToken(new Token(new TextAttribute(colorManager.getColor(SQLColorManager.DEFAULT))));

		List<IRule> rules = new ArrayList<IRule>();
		IToken other = new Token(new TextAttribute(colorManager.getColor(SQLColorManager.DEFAULT)));

		IToken keyword = new Token(new TextAttribute(colorManager.getColor(SQLColorManager.KEYWORD), null, SWT.BOLD));
		IToken function = new Token(new TextAttribute(colorManager.getColor(SQLColorManager.FUNCTION), null, SWT.BOLD));
		IToken comment = new Token(new TextAttribute(colorManager.getColor(SQLColorManager.SINGLE_LINE_COMMENT)));
		IToken string = new Token(new TextAttribute(colorManager.getColor(SQLColorManager.STRING), null, SWT.BOLD));
		IToken operator = new Token(new TextAttribute(colorManager.getColor(SQLColorManager.OPERATOR), null, SWT.BOLD));
		IToken table = new Token(new TextAttribute(colorManager.getColor(SQLColorManager.TABLE), null, SWT.BOLD));
		IToken symbol = new Token(new TextAttribute(colorManager.getColor(SQLColorManager.SYMBOL), null, SWT.BOLD));

		WordRule wordRule = new WordRule(new WordDetector(), other);

		String[] keywords = TokenUtil.MULTI_KEYWORD;
		for (int i = 0; i < keywords.length; i++) {
			String key = keywords[i];
			wordRule.addWord(key.toUpperCase(), keyword);
			wordRule.addWord(key.toLowerCase(), keyword);
		}

		keywords = TokenUtil.KEYWORD;
		for (int i = 0; i < keywords.length; i++) {
			String key = keywords[i];
			wordRule.addWord(key.toUpperCase(), keyword);
			wordRule.addWord(key.toLowerCase(), keyword);
		}
		
		keywords = TokenUtil.BEGIN_SQL_KEYWORD;
		for (int i = 0; i < keywords.length; i++) {
			String key = keywords[i];
			wordRule.addWord(key.toUpperCase(), keyword);
			wordRule.addWord(key.toLowerCase(), keyword);
		}

		keywords = TokenUtil.KEYWORD_DATATYPE;
		for (int i = 0; i < keywords.length; i++) {
			String key = keywords[i];
			wordRule.addWord(key.toUpperCase(), keyword);
			wordRule.addWord(key.toLowerCase(), keyword);
		}

		String[] functs = TokenUtil.KEYWORD_FUNCTION;
		for (int i = 0; i < functs.length; i++) {
			String name = functs[i];
			wordRule.addWord(name.toUpperCase(), function);
			wordRule.addWord(name.toLowerCase(), function);

		}
		
		String[] operators = TokenUtil.OPERATOR;
		for (int i = 0; i < operators.length; i++) {
			String key = operators[i];
			wordRule.addWord(key.toUpperCase(), operator);
			wordRule.addWord(key.toLowerCase(), operator);
		}
		
		char[] binds = TokenUtil.BIND_VARIABLE;
		for (int i = 0; i < binds.length; i++) {
			char key = binds[i];
			wordRule.addWord(key+"", operator);
			wordRule.addWord(key+"", operator);
		}
		
		String[] coments = TokenUtil.COMMENT;
		for (int i = 0; i < coments.length; i++) {
			String key = coments[i];
			wordRule.addWord(key.toUpperCase(), comment);
			wordRule.addWord(key.toLowerCase(), comment);
		}
		
		String[] symbols = TokenUtil.SYMBOL;
		for (int i = 0; i < symbols.length; i++) {
			String key = symbols[i];
			wordRule.addWord(key.toUpperCase(), symbol);
			wordRule.addWord(key.toLowerCase(), symbol);
		}
		
		if (project != null) {
			try {
				Set<StoredProcedureSchema> procedures = project.getSchemaManager().getProcedures();
				for (StoredProcedureSchema procedure : procedures) {
					String name = procedure.getName();
					wordRule.addWord(name.toUpperCase(), function);
					wordRule.addWord(name.toLowerCase(), function);
					wordRule.addWord(StringUtils.capitalize(name), function);
				}

				Set<StoredFunctionSchema> functions = project.getSchemaManager().getFunctions();
				for (StoredFunctionSchema funct : functions) {
					String name = funct.getName();
					wordRule.addWord(name.toUpperCase(), function);
					wordRule.addWord(name.toLowerCase(), function);
					wordRule.addWord(StringUtils.capitalize(name), function);
				}

				Set<TableSchema> tables = project.getSchemaManager().getTables();
				for (TableSchema tb : tables) {
					String name = tb.getName();
					wordRule.addWord(name.toUpperCase(), table);
					wordRule.addWord(name.toLowerCase(), table);
					wordRule.addWord(StringUtils.capitalize(name), table);
				}

				Set<ViewSchema> views = project.getSchemaManager().getViews();
				for (ViewSchema view : views) {
					String name = view.getName();
					wordRule.addWord(name.toUpperCase(), table);
					wordRule.addWord(name.toLowerCase(), table);
					wordRule.addWord(StringUtils.capitalize(name), table);
				}
			} catch (Exception e) {
				AnterosHelpmePlugin.error(
						"Não foi possível criar a lista de palavras que serão destacadas no Editor SQL.", e, true);
			}
		}

		rules.add(wordRule);
		rules.add(new WhitespaceRule(new WhitespaceDetector()));
		rules.add(new EndOfLineRule("//", comment));
		rules.add(new MultiLineRule("/*", "*/", comment, (char) 0, true));
		rules.add(new SingleLineRule("\"", "\"", string, '\\'));
		rules.add(new SingleLineRule("'", "'", string, '\\'));
		rules.add(new OperatorRule(operator));

		setRules(rules.toArray(new IRule[] {}));

	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
		initialize();
	}

	private static final class OperatorRule implements IRule {

		private final char[] SQL_OPERATORS = { ';', '.', '=', '/', '\\', '+', '-', '*', '<', '>', ':', '?', '!', ',',
				'|', '&', '^', '%', '~', '{', '}', '#' };
		private final IToken fToken;

		public OperatorRule(IToken token) {
			fToken = token;
		}

		public boolean isOperator(char character) {
			for (int index = 0; index < SQL_OPERATORS.length; index++) {
				if (SQL_OPERATORS[index] == character)
					return true;
			}
			return false;
		}

		public IToken evaluate(ICharacterScanner scanner) {

			int character = scanner.read();
			if (isOperator((char) character)) {
				do {
					character = scanner.read();
				} while (isOperator((char) character));
				scanner.unread();
				return fToken;
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}
}
