package br.com.anteros.helpme.widgets;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.nebula.widgets.datechooser.DateChooserCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

public class CustomDate extends Composite implements KeyListener, FocusListener {

	private DateChooserCombo dateControl;

	public CustomDate(Composite parent, int style) {
		super(parent, style);

		this.setLayout(new RowLayout(SWT.HORIZONTAL));
		dateControl = new DateChooserCombo(this, SWT.BORDER | SWT.FLAT);
		
		this.setTabList(new Control[]{dateControl});
		
		dateControl.addKeyListener(this);
		this.addKeyListener(this);
		this.addFocusListener(this);
	}
	public DateChooserCombo getDateControl() {
		return dateControl;
	}

	public void setDateControl(DateChooserCombo dateControl) {
		this.dateControl = dateControl;
	}

	public void setData(Object data) {
		dateControl.setData(data);
	}

	public Object getData() {
		return dateControl.getData();
	}

	@Override
	public boolean setFocus() {
		return dateControl.setFocus();
	}

	public Date getDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(dateControl.getValue().getYear(), dateControl.getValue().getMonth(), 0, 0, 0);
		return calendar.getTime();
	}

	public void setDate(Date date) {
		if (date == null) {
			dateControl.setValue(null);
		} else {
			dateControl.setValue(date);
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if ((event.character == SWT.CR) || (event.keyCode == SWT.ARROW_DOWN)) {
			((Control)event.getSource()).traverse(SWT.TRAVERSE_TAB_NEXT);
		} else if (event.keyCode == SWT.ARROW_UP) {
			((Control)event.getSource()).traverse(SWT.TRAVERSE_TAB_PREVIOUS);
		}		
	}

	@Override
	public void keyReleased(KeyEvent event) {
	}

	@Override
	public void focusGained(FocusEvent e) {
		dateControl.setFocus();
	}

	@Override
	public void focusLost(FocusEvent e) {
	}


}
