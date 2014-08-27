package br.com.anteros.helpme.sql.editors;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import br.com.anteros.helpme.util.ResourceManager;
import br.com.anteros.helpme.widgets.CustomText;
import br.com.anteros.helpme.widgets.DateText;
import br.com.anteros.helpme.widgets.DateTimeText;
import br.com.anteros.helpme.widgets.EditText;
import br.com.anteros.helpme.widgets.NumericText;
import br.com.anteros.helpme.widgets.TimeText;
import br.com.anteros.persistence.parameter.NamedParameter;
import br.com.anteros.persistence.parameter.SubstitutedParameter;

public class SQLParameterEditor extends Dialog {

	protected List<NamedParameter> parameters;

	private ArrayList<Object> widgetFields;

	private Set<SQLParameter> parametersType;

	public SQLParameterEditor(Shell parentShell, List<NamedParameter> parameters, Set<SQLParameter> parametersType) {
		super(parentShell);
		this.parameters = parameters;
		this.parametersType = parametersType;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Parameter Editor");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setShowFocusedControl(true);
		scrolledComposite.setAlwaysShowScrollBars(true);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));

		widgetFields = new ArrayList<Object>();
		List<Control> tabList = new ArrayList<Control>();
		for (NamedParameter parameter : parameters) {
			CustomText field = createFieldParameter(parameter, composite);
			widgetFields.add(field);
			tabList.add(field.getControl());
		}
		composite.setTabList(tabList.toArray(new Control[] {}));

		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		return container;
	}

	private CustomText createFieldParameter(NamedParameter parameter, Composite parent) {
		Label lbField = new Label(parent, SWT.NONE | SWT.NO_FOCUS);
		lbField.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbField.setText(parameter.getName());
		lbField.setFont(ResourceManager.getFont("Tahoma", 8, SWT.BOLD));

		final Button btnCheckButton = new Button(parent, SWT.CHECK | SWT.NO_FOCUS);
		btnCheckButton.setText("null");

		CustomText field = null;
		SQLParameter type = getParameterType(parameter.getName());
		if (type != null) {
			GridData gridDataField = null;
			GridData gdNumeric = null;
			switch (type.parameterType) {
			case Types.DATE:
				field = new DateText(parent, SWT.NONE);
				gridDataField = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
				gridDataField.minimumWidth = 100;
				((DateText) field).setLayoutData(gridDataField);
				break;
			case Types.TIMESTAMP:
				field = new DateTimeText(parent, SWT.NONE);
				gridDataField = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
				gridDataField.minimumWidth = 200;
				((DateTimeText) field).setLayoutData(gridDataField);
				((DateTimeText) field).setValue(null);
				break;
			case Types.TIME:
				field = new TimeText(parent, SWT.BORDER);
				gridDataField = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
				gridDataField.minimumWidth = 200;
				((TimeText) field).setLayoutData(gridDataField);
				((TimeText) field).setValue(null);
				break;
			case Types.DECIMAL:
			case Types.FLOAT:
			case Types.DOUBLE:
			case Types.NUMERIC:
			case Types.REAL:
				field = new NumericText(parent, SWT.BORDER);
				((NumericText) field).setFormatter("###,###,##0.00##########");
				gdNumeric = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
				gdNumeric.minimumWidth = 200;
				((NumericText) field).setLayoutData(gdNumeric);
				break;
			case Types.INTEGER:
			case Types.BIGINT:
			case Types.SMALLINT:
				field = new NumericText(parent, SWT.BORDER);
				((NumericText) field).setFormatter("#############0");
				gdNumeric = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
				gdNumeric.minimumWidth = 200;
				((NumericText) field).setLayoutData(gdNumeric);
				break;
			default:
				field = new EditText(parent, SWT.BORDER);
				((EditText) field).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				break;
			}
		} else {
			field = new EditText(parent, SWT.BORDER);
			((EditText) field).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}

		Button btnSubstituition = new Button(parent, SWT.CHECK | SWT.NO_FOCUS);
		btnSubstituition.setText("Substituition?");

		((CustomText) field).setData(new Object[] { parameter, btnCheckButton, btnSubstituition });

		SelectionListener listener = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				CustomText field = (CustomText) ((Button) event.getSource()).getData();
				if (((Button) event.getSource()).getSelection()) {
					if (event.getSource() == btnCheckButton)
						field.setValue(null);
					field.setFocus();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent event) {

			}
		};

		btnCheckButton.setData(field);
		btnCheckButton.addSelectionListener(listener);

		btnSubstituition.setData(field);
		btnSubstituition.addSelectionListener(listener);

		return field;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			for (Object control : widgetFields) {
				Object[] objects;
				NamedParameter parameter = null;
				Button check = null;
				Button substituition = null;
				if (control instanceof CustomText) {
					objects = (Object[]) ((CustomText) control).getData();
					parameter = (NamedParameter) objects[0];
					check = (Button) objects[1];
					substituition = (Button) objects[2];
				}
				if (!check.getSelection()) {
					if (control instanceof CustomText) {
						if (substituition.getSelection()) {
							int index = parameters.indexOf(parameter);
							parameters.remove(index);
							parameter = new SubstitutedParameter(parameter.getName(), null);
							parameters.add(index, parameter);
						}
						parameter.setValue(((CustomText) control).getValue());
					}
				}
			}
			super.buttonPressed(buttonId);
		} else
			super.buttonPressed(buttonId);
	}

	private SQLParameter getParameterType(String name) {
		if (parametersType != null) {
			for (SQLParameter p : parametersType) {
				if (p.parameter.equalsIgnoreCase(name))
					return p;
			}
		}
		return null;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 300);
	}

}
