package br.com.anteros.helpme.model.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.anteros.persistence.parameter.NamedParameter;

public class SQLHistoryPersistent {

	public String project;
	public String resultClass;
	public String sql;
	public Date date;
	public List<NamedParameter> parameters = new ArrayList<NamedParameter>();
	
	
	public SQLHistoryPersistent(String project, String resultClass, String sql, Date date, List<NamedParameter> parameters) {
		this.project = project;
		this.resultClass = resultClass;
		this.sql = sql;
		this.date = date;
		this.parameters = parameters;
	}

}
