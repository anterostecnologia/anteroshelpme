package br.com.anteros.helpme.widgets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class EditText extends CustomText {

	public EditText(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void setValue(Object value) {
		if (value == null)
			((Text) control).setText("");
		else
			((Text) control).setText(value + "");
	}

	@Override
	public Object getValue() {
		return ((Text)control).getText();
	}

	@Override
	public void createControl() {
		control = new Text(parent, style);
	}

}
