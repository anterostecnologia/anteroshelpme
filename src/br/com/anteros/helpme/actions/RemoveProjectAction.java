package br.com.anteros.helpme.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import br.com.anteros.helpme.model.Project;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.ProjectNavigatorView;

public class RemoveProjectAction extends AbstractViewAction {

	public RemoveProjectAction(ViewPart view) {
		super(view);
		this.setText("&Remove project");
		this.setToolTipText("Remove project");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(
				AnterosHelpmePlugin.IMG_PROJECT_REMOVE));
	}

	@Override
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) ((ProjectNavigatorView) view).getViewer()
				.getSelection();
		if (selection != null) {
			Project project = (Project) selection.getFirstElement();
			if (AnterosHelpmePlugin.confirmDialog("Tem certeza que deseja remover o projeto " + project.getSimpleName()
					+ " ?")) {
				TreeItem treeItem = ((ProjectNavigatorView) view).getViewer().getTree().getSelection()[0];
				treeItem.dispose();
				AnterosHelpmePlugin.getDefault().getSQLProjectManager().getProjectManager().remove(project);
			}
		}
	}

	@Override
	public boolean isAvailable() {
		IStructuredSelection selection = (IStructuredSelection) ((ProjectNavigatorView) view).getViewer()
				.getSelection();
		if (selection == null)
			return false;
		return ((selection.getFirstElement() instanceof Project));
	}

}
