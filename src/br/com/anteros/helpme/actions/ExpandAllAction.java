package br.com.anteros.helpme.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.part.ViewPart;

import br.com.anteros.helpme.model.ProjectManager;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.ProjectNavigatorView;

public class ExpandAllAction extends AbstractViewAction implements Runnable {

	public ExpandAllAction(ViewPart view) {
		super(view);
		this.setText("&Expand All"); 
		this.setToolTipText("Expand All");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_EXPAND_ALL));
	}

	@Override
	public boolean isAvailable() {
		IStructuredSelection selection = (IStructuredSelection) ((ProjectNavigatorView) view).getViewer().getSelection();
		if (selection == null)
			return false;
		return (!(selection.getFirstElement() instanceof ProjectManager));
	}

}
