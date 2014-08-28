package br.com.anteros.helpme.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.ui.part.ViewPart;

import br.com.anteros.helpme.preferences.Preferences;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.ProjectNavigatorView;

public class PreferencesAction extends AbstractViewAction {

	public PreferencesAction(ViewPart view) {
		super(view);
		this.setText("&Preferences");
		this.setToolTipText("Preferences");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_PREFERENCES));
	}

	@Override
	public void run() {
		IPreferencePage page = new Preferences();
		PreferenceManager mgr = new PreferenceManager();
		IPreferenceNode node = new PreferenceNode("Anteros Helpme", page);
		mgr.addToRoot(node);
		PreferenceDialog dialog = new PreferenceDialog(AnterosHelpmePlugin.getDefault().getShell(), mgr);
		dialog.create();
		dialog.setMessage(page.getTitle());
		dialog.open();
	}

	@Override
	public boolean isAvailable() {
		return true;
	}
}
