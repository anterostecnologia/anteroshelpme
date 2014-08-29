package br.com.anteros.helpme.model.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.model.ProjectManager;

import com.thoughtworks.xstream.XStream;

public class SQLProjectManager {

	public static final String CONFIG_PROJECTS = "projects.xml";

	private ProjectManager projectManager;

	public SQLProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}

	public void load() {
		XStream xStream = new XStream();
		File file = new File(AnterosHelpmePlugin.getDefault()
				.getStateLocation().append(CONFIG_PROJECTS).toOSString());
		if (file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				projectManager.loadProjectsPersistent((List) xStream
						.fromXML(fis));
				fis.close();
			} catch (Exception e) {
				AnterosHelpmePlugin.error(
						"Não foi possível carregar as configurações do arquivo "
								+ file.getName(), e, true);
			}
		}
	}

	public void save() {
		FileOutputStream stream;
		try {
			List<SQLProjectPersistent> list = projectManager
					.getProjectsPersistent();
			if (list != null && list.size() > 0) {
				stream = new FileOutputStream(AnterosHelpmePlugin.getDefault()
						.getStateLocation().append(CONFIG_PROJECTS)
						.toOSString());
				OutputStreamWriter writer = new OutputStreamWriter(stream,
						"utf-8");
				XStream xStream = new XStream();

				xStream.toXML(list, writer);
				writer.flush();
				writer.close();
			}
		} catch (Exception e) {
			AnterosHelpmePlugin.error(
					"Não foi possível salvar as configurações.", e, true);
		}
	}

	public ProjectManager getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}
}
