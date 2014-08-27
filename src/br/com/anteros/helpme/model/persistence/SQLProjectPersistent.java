package br.com.anteros.helpme.model.persistence;

import java.util.List;

public class SQLProjectPersistent {

	public String name;
	public String projectName;
	public String fileConfiguration;
	public List<String> classPath;

	public SQLProjectPersistent(String name, String projectName, String fileConfiguration, List<String> classPath) {
		this.name = name;
		this.projectName = projectName;
		this.fileConfiguration = fileConfiguration;
		this.classPath = classPath;
	}

}
