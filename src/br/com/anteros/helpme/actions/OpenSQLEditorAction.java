package br.com.anteros.helpme.actions;

import org.eclipse.ui.part.ViewPart;

import br.com.anteros.helpme.sql.editors.SQLEditorInput;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.SQLEditorView;

public class OpenSQLEditorAction extends AbstractViewAction {

	public OpenSQLEditorAction(ViewPart view) {
		super(view);
		this.setText("&Open SQL Editor"); 
		this.setToolTipText("Open SQL Editor");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_SQL_EDITOR));
	}

	@Override
	public void run() {
		try {
			AnterosHelpmePlugin.getDefault().openEditor(new SQLEditorInput(), SQLEditorView.ID, false);
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().error("N�o foi poss�vel Abrir o Editor SQL.", e, true);
		}
	}

	@Override
	public boolean isAvailable() {
		return true;
	}
}
