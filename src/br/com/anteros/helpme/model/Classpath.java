package br.com.anteros.helpme.model;

import br.com.anteros.helpme.treeviewer.TreeNode;

public class Classpath extends TreeNode implements IClasspath {
	
	protected String classpath;

	@Override
	public void setClasspath(String value) {
		this.classpath = value;		
	}

	@Override
	public String getClasspath() {
		return classpath;
	}

}
