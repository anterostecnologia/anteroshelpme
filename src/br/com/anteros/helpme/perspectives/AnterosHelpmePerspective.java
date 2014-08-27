package br.com.anteros.helpme.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import br.com.anteros.helpme.sql.editors.SQLEditorInput;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.ConsoleSQLView;
import br.com.anteros.helpme.views.ProjectNavigatorView;
import br.com.anteros.helpme.views.SQLEditorView;

public class AnterosHelpmePerspective implements IPerspectiveFactory {

	private IPageLayout factory;

	public AnterosHelpmePerspective() {
		super();
	}

	public void createInitialLayout(IPageLayout factory) {
		this.factory = factory;
		try {
			addViews();
		} catch (Exception e) {
			AnterosHelpmePlugin.error(
					"Ocorreu um problema criando layout da Perspectiva.", e, true);
		}
		addActionSets();
		addNewWizardShortcuts();
		addPerspectiveShortcuts();
		addViewShortcuts();
	}

	private void addViews() throws Exception {
		IFolderLayout bottom = factory.createFolder("bottom", IPageLayout.BOTTOM, 0.82f, factory.getEditorArea());
		bottom.addView(ConsoleSQLView.ID);
		IFolderLayout topLeft = factory.createFolder("left", IPageLayout.LEFT, 0.20f, factory.getEditorArea());
		topLeft.addView(ProjectNavigatorView.ID);		
		AnterosHelpmePlugin.getDefault().openEditor(new SQLEditorInput(), SQLEditorView.ID, false);
	}

	private void addActionSets() {
		factory.addActionSet("org.eclipse.debug.ui.launchActionSet");
	}

	private void addPerspectiveShortcuts() {
		factory.addPerspectiveShortcut("org.eclipse.team.ui.TeamSynchronizingPerspective");
	}

	private void addNewWizardShortcuts() {
		factory.addNewWizardShortcut("org.eclipse.team.cvs.ui.newProjectCheckout");
	}

	private void addViewShortcuts() {
		factory.addShowViewShortcut("org.eclipse.ant.ui.views.AntView");
	}

}
