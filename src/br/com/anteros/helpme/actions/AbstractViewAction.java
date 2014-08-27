package br.com.anteros.helpme.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.part.ViewPart;

public abstract class AbstractViewAction extends Action {

	protected ViewPart view;

	public AbstractViewAction(ViewPart view) {
		this.view = view;
	}

	public ViewPart getView() {
		return view;
	}

	public void setView(ViewPart view) {
		this.view = view;
	}
	
	public abstract boolean isAvailable();
}
