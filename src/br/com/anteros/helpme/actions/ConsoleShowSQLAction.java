package br.com.anteros.helpme.actions;

import org.eclipse.swt.SWT;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.ConsoleSQLView;

public class ConsoleShowSQLAction extends AbstractAction {

	public ConsoleShowSQLAction(ConsoleSQLView console) {
		super(console, "&Show SQL", SWT.TOGGLE);
		this.setToolTipText("Show SQL");
		this.setChecked(true);
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_SHOW_SQL));
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

}