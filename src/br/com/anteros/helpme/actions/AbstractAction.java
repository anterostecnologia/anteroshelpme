package br.com.anteros.helpme.actions;

import org.eclipse.jface.action.Action;

public abstract class AbstractAction extends Action {

	protected Object targetObject;

	public AbstractAction(Object targetObject, String text, int style) {
		super(text,style);
		this.targetObject = targetObject;
	}
	
	public abstract boolean isAvailable();

	public Object getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}
}