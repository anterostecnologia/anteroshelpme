package br.com.anteros.helpme.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class Preferences extends PreferencePage implements IWorkbenchPreferencePage{

	/**
	 * Create the preference page.
	 */
	public Preferences() {
		setDescription("Color");
		setTitle("Anteros Helpme");
	}

	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		
		Label lblColor = new Label(container, SWT.NONE);
		lblColor.setBounds(30, 18, 29, 13);
		lblColor.setText("Color");
		
		Combo combo = new Combo(container, SWT.NONE);
		combo.setBounds(65, 15, 233, 21);

		return container;
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}
}
