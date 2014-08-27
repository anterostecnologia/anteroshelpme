package br.com.anteros.helpme.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import br.com.anteros.helpme.model.Column;
import br.com.anteros.helpme.model.Constraint;
import br.com.anteros.helpme.model.Index;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.ProjectNavigatorView;

public class RefreshProjectAction extends AbstractViewAction {

	public RefreshProjectAction(ViewPart view) {
		super(view);
		this.setText("&Refresh");
		this.setToolTipText("Refresh");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_REFRESH));
	}

	@Override
	public boolean isAvailable() {
		IStructuredSelection selection = (IStructuredSelection) ((ProjectNavigatorView) view).getViewer()
				.getSelection();
		if (selection == null)
			return false;
		if ((selection.getFirstElement() instanceof IObjectNode)) {
			IObjectNode node = (IObjectNode) selection.getFirstElement();
			if ((!(node instanceof Constraint)) && (!(node instanceof Index)) && (!(node instanceof Column))) {
				return node.isInitialized();
			}
		}
		return false;
	}

	@Override
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) getView().getViewer().getSelection();
		if (selection != null) {
			if ((selection.getFirstElement() instanceof IObjectNode)) {
				IObjectNode node = (IObjectNode) selection.getFirstElement();
				if (node.isInitialized()) {
					node.uninitialized();
					getView().getViewer().refreshNode(getView().getViewer().getTree().getSelection()[0]);
					getView().refreshActions();
				}
			}
		}

	}

	public ProjectNavigatorView getView() {
		return ((ProjectNavigatorView) view);
	}

}