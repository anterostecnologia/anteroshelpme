package br.com.anteros.helpme.sql.editors;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class SQLPartitionScanner extends RuleBasedPartitionScanner {

	public static final String SQL_COMMENT = "__sql_comment"; 

	public static final String SQL_STRING = "__sql_string"; 
	
	
	public final static String[] SQL_PARTITION_TYPES= new String[] {
		SQL_COMMENT,
		SQL_STRING
    };

	public SQLPartitionScanner() {
		IPredicateRule[] rules = new IPredicateRule[4];

		IToken comment = new Token(SQL_COMMENT);
		rules[0] = new MultiLineRule("/*", "*/", comment); 

		IToken string = new Token(SQL_STRING);
		rules[1] = new MultiLineRule("\"", "\"", string); 
		rules[2] = new MultiLineRule("\'", "\'", string); 

		rules[3] = new EndOfLineRule("--", comment); 

		setPredicateRules(rules);
	}

}
