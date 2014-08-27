package br.com.anteros.helpme.actions;

import org.eclipse.swt.SWT;
import org.eclipse.ui.part.EditorPart;

import br.com.anteros.helpme.AnterosHelpmePlugin;

public class ExecuteScriptSQLAction extends AbstractAction {

	public ExecuteScriptSQLAction(EditorPart editor) {
		super(editor,"Execute &Script SQL",SWT.NONE);
		this.setEnabled(false);
		this.setToolTipText("Execute Script SQL");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_EXECUTE_SCRIPT_SQL));
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

}