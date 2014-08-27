package br.com.anteros.helpme.widgets;

import java.util.Date;

import org.eclipse.swt.widgets.Composite;

public class DateTimeText extends CustomText {

	public DateTimeText(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	public void setValue(Object value) {
		((CustomDateTime)control).setDate((Date) value);
	}

	@Override
	public Object getValue() {
		return ((CustomDateTime)control).getDate();
	}

	@Override
	public void createControl() {
		control = new CustomDateTime(parent, style);
	}

}
