package br.com.anteros.helpme.actions;

import org.eclipse.ui.part.ViewPart;

import br.com.anteros.helpme.forms.SimpleFormEditor;
import br.com.anteros.helpme.forms.SimpleFormEditorInput;
import br.com.anteros.helpme.AnterosHelpmePlugin;

public class EditConfigurationXMLAction extends AbstractViewAction {

	public EditConfigurationXMLAction(ViewPart view) {
		super(view);
		this.setText("&Edit configuration"); 
		this.setToolTipText("Edit configuration");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_SQL_EDITOR));
	}

	@Override
	public void run() {
		try {
			AnterosHelpmePlugin.getDefault().openEditor(new SimpleFormEditorInput("Configuration"), SimpleFormEditor.ID, false);
			throw new Exception("TEste");
		} catch (Exception e) {
			AnterosHelpmePlugin.error("N�o foi poss�vel abrir a Configura��o.", e,true);
		}
	}

	@Override
	public boolean isAvailable() {
		return true;
	}
}
