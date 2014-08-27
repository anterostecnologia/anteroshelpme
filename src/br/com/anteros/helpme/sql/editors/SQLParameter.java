package br.com.anteros.helpme.sql.editors;

public class SQLParameter {

	public String parameter;
	public int parameterType;

	public SQLParameter(String parameter, int parameterType) {
		this.parameter = parameter;
		this.parameterType = parameterType;
	}
	
	@Override
	public String toString() {
		return parameter+"="+parameterType;
	}

}
