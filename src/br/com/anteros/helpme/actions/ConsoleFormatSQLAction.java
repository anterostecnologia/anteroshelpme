package br.com.anteros.helpme.actions;

import org.eclipse.swt.SWT;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.ConsoleSQLView;

public class ConsoleFormatSQLAction extends AbstractAction {

	public ConsoleFormatSQLAction(ConsoleSQLView console) {
		super(console, "&Format SQL", SWT.TOGGLE);
		this.setToolTipText("Format SQL");
		this.setChecked(true);
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_FORMAT_SQL));
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

}