package br.com.anteros.helpme.widgets;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

public class TimeText extends CustomText {

	public TimeText(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public Object getValue() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(0, 0, 0, ((DateTime) control).getHours(), ((DateTime) control).getMinutes(),
				((DateTime) control).getSeconds());
		return calendar.getTime();
	}
	@Override
	public void setValue(Object value) {
		if (value == null) {
			((DateTime) control).setHours(0);
			((DateTime) control).setMinutes(0);
			((DateTime) control).setSeconds(0);
		} else {
			((DateTime) control).setHours(((Date) value).getHours());
			((DateTime) control).setMinutes(((Date) value).getMinutes());
			((DateTime) control).setSeconds(((Date) value).getSeconds());
		}
	}

	@Override
	public void createControl() {
		style |= SWT.TIME;
		style |= SWT.LONG;
		control = new DateTime(parent, style);
	}

}
