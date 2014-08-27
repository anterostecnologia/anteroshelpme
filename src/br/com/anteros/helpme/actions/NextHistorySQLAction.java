package br.com.anteros.helpme.actions;

import org.eclipse.swt.SWT;
import org.eclipse.ui.part.EditorPart;

import br.com.anteros.helpme.AnterosHelpmePlugin;

public class NextHistorySQLAction extends AbstractAction  {

	public NextHistorySQLAction(EditorPart editor) {
		super(editor,"&Next SQL",SWT.NONE);
		this.setToolTipText("Next SQL");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_NEXT_SQL));
	}

	@Override
	public boolean isAvailable() {
		return true;
	}
}