package br.com.anteros.helpme.sql.editors;

import org.eclipse.jface.text.templates.Template;

public class SQLTemplate extends Template{
	
	protected boolean nativeFunction;

	public boolean isNativeFunction() {
		return nativeFunction;
	}

	public void setNativeFunction(boolean nativeFunction) {
		this.nativeFunction = nativeFunction;
	} 

}
