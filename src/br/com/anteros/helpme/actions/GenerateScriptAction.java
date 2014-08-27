package br.com.anteros.helpme.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.part.ViewPart;

import br.com.anteros.helpme.model.Project;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.ProjectNavigatorView;

public class GenerateScriptAction extends AbstractViewAction  {

	public GenerateScriptAction(ViewPart view) {
		super(view);
		this.setText("&Generate database script"); 
		this.setToolTipText("Generate database script");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_GENERATE_SCRIPT));
	}

	@Override
	public boolean isAvailable() {
		/*IStructuredSelection selection = (IStructuredSelection) ((ProjectNavigatorView) view).getViewer().getSelection();
		if (selection == null)
			return false;
		return ((selection.getFirstElement() instanceof Project));*/
		return false;
	}

}
