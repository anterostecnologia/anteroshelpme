package br.com.anteros.helpme.actions;

import org.eclipse.swt.SWT;
import org.eclipse.ui.part.EditorPart;

import br.com.anteros.helpme.AnterosHelpmePlugin;

public class PreviousHistorySQLAction extends AbstractAction {

	public PreviousHistorySQLAction(EditorPart editor) {
		super(editor,"&Previous SQL",SWT.NONE);
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_PREVIOUS_SQL));
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

}