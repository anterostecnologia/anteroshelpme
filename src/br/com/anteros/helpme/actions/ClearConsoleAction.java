package br.com.anteros.helpme.actions;

import org.eclipse.swt.SWT;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.ConsoleSQLView;

public class ClearConsoleAction extends AbstractAction {

	public ClearConsoleAction(ConsoleSQLView console) {
		super(console,"&Clear",SWT.NONE);
		this.setToolTipText("Clear");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_CLEAR));
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public void run() {
		if (targetObject != null) {
			if (targetObject instanceof ConsoleSQLView) {
				((ConsoleSQLView) targetObject).getSourceViewer().getTextWidget().setText("");
			}
		}
	}

}
