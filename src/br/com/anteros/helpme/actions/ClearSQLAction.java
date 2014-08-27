package br.com.anteros.helpme.actions;

import org.eclipse.swt.SWT;
import org.eclipse.ui.part.EditorPart;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.SQLEditorView;

public class ClearSQLAction extends AbstractAction  {

	
	public ClearSQLAction(EditorPart editor) {
		super(editor,"&Clear SQL",SWT.NONE);
		this.setToolTipText("Clear SQL");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_CLEAR_SQL));
	}

	@Override
	public boolean isAvailable() {
		return true;
	}
	
	
	@Override
	public void run() {
		getView().clear();
	}
	

	public SQLEditorView getView() {
		return ((SQLEditorView) targetObject);
	}

		
}