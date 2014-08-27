package br.com.anteros.helpme.model;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;

public class ClasspathProject extends Classpath {
	
	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage("projects.gif");
	}

	@Override
	public String[] getName() {
		return new String[]{classpath};
	}

}