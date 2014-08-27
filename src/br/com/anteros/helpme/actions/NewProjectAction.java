package br.com.anteros.helpme.actions;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.part.ViewPart;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.ProjectNavigatorView;
import br.com.anteros.helpme.wizards.ConfigurationWizard;

public class NewProjectAction extends AbstractViewAction {

	public NewProjectAction(ViewPart view) {
		super(view);
		this.setText("&Add new project");
		this.setToolTipText("Add new project");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_PROJECT_ADD));
	}

	@Override
	public void run() {
		ConfigurationWizard wizard = new ConfigurationWizard((ProjectNavigatorView) view);
		WizardDialog dialog = new WizardDialog(AnterosHelpmePlugin.getDefault().getShell(), wizard);
		dialog.create();
		dialog.open();
	}

	@Override
	public boolean isAvailable() {
		return true;
	}
}
