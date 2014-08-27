package br.com.anteros.helpme.model;

import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.util.PluginUtils;

public class ClasspathJar extends Classpath {
	
	public Image getLeftImage() throws Exception {
		return AnterosHelpmePlugin.getDefault().getImage("jar.gif");
	}

	@Override
	public String[] getName() {
		return new String[]{PluginUtils.getNameFromFileName(classpath)+" - "+PluginUtils.getPathFromFileName(classpath)};
	}

}