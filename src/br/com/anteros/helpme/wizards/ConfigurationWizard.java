package br.com.anteros.helpme.wizards;

import org.eclipse.jface.wizard.Wizard;

import br.com.anteros.helpme.model.Project;
import br.com.anteros.helpme.views.ProjectNavigatorView;

public class ConfigurationWizard extends Wizard {

	private ConfigurationPageWizard configurationPageWizard;
	private ProjectNavigatorView view;

	public ConfigurationWizard(ProjectNavigatorView view) {
		setWindowTitle("New Project");
		this.view = view;
	}

	@Override
	public void addPages() {
		configurationPageWizard = new ConfigurationPageWizard();
		addPage(configurationPageWizard);
	}

	@Override
	public boolean performFinish() {
		if (configurationPageWizard.isValid()) {
			try {
				Project project = new Project(configurationPageWizard.getFldProjectTitle().getText(),
						configurationPageWizard.getFldJavaProject().getText(), configurationPageWizard
								.getFldConfigurationFile().getText(), configurationPageWizard.getClasspath());
				view.getProjects().addNode(project);
				view.getViewer().refresh();
				view.getViewer().select(project);
				view.getViewer().setFocus();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
