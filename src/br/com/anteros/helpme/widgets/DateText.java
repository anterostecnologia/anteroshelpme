package br.com.anteros.helpme.widgets;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class DateText extends CustomText {

	public DateText(Composite parent, int style) {
		super(parent, style);
		this.style = style|SWT.FLAT;
	}
	
	@Override
	public void setValue(Object value) {
		((CustomDate)control).setDate((Date) value);
	}

	@Override
	public Object getValue() {
		return ((CustomDate)control).getDate();
	}

	@Override
	public void createControl() {
		control = new CustomDate(parent, style);
	}

}