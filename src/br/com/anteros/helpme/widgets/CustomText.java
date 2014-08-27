package br.com.anteros.helpme.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class CustomText implements KeyListener {
	protected Control control = null;
	protected Composite parent;
	protected int style;

	public CustomText(Composite parent, int style) {
		this.parent = parent;
		this.style = style;
		createControl();
		if (control != null)
			control.addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if ((event.character == SWT.CR) || (event.keyCode == SWT.ARROW_DOWN)) {
			control.traverse(SWT.TRAVERSE_TAB_NEXT);
		} else if (event.keyCode == SWT.ARROW_UP) {
			control.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {

	}

	public Composite getParent() {
		return parent;
	}

	public void setParent(Composite parent) {
		this.parent = parent;
		control.setParent(parent);
	}

	public int getStyle() {
		return style;
	}

	public Control getControl() {
		return control;
	}

	public void setData(Object data) {
		control.setData(data);
	}

	public Object getData() {
		return control.getData();
	}
	
	public void setLayoutData(GridData layoutData) {
		control.setLayoutData(layoutData);
	}

	public abstract void setValue(Object value);

	public abstract Object getValue();

	public abstract void createControl();

	public void setFocus() {
		control.setFocus();
	}

}
