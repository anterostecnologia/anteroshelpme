package br.com.anteros.helpme.widgets;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import br.com.anteros.helpme.AnterosHelpmePlugin;

public class NumericText extends CustomText implements FocusListener {

	private boolean doit = true;
	protected BigDecimal value;
	protected BigDecimal oldValue;
	protected String pattern;
	protected Integer caretPos;
	protected String textBefore;
	protected DecimalFormat formatter = new DecimalFormat();
	protected DecimalFormat editFormatter = new DecimalFormat();
	protected DecimalFormat currentFormatter = new DecimalFormat();
	private int maximumIntegerDigits = -1;
	private int maximumFractionDigits = 0;

	private boolean nullOk = true;

	public NumericText(Composite parent, int style) {
		super(parent, style);
		init(true);
	}

	private void init(boolean addStandardListeners) {
		formatter.setParseBigDecimal(true);
		if (addStandardListeners)
			addStandardListeners();
	}

	public void addStandardListeners() {
		control.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				setEditFormatter();
				currentFormatter = editFormatter;
				textBefore = ((Text) control).getText();
				if (null != ((Text) control).getText() && 0 < ((Text) control).getText().length()) {
					try {
						doit = false;
						value = (BigDecimal) formatter.parse(((Text) control).getText());
						((Text) control).setText(currentFormatter.format(value));
					} catch (ParseException e1) {
					} finally {
						doit = true;
					}
				}
				((Text) control).selectAll();
			}

			public void focusLost(FocusEvent e) {
				if (null == ((Text) control).getText() || 0 == ((Text) control).getText().length())
					if (nullOk)
						value = null;
					else {
						doit = false;
						value = new BigDecimal(0).setScale(maximumFractionDigits, BigDecimal.ROUND_HALF_UP);
						((Text) control).setText("0");
						doit = true;
					}
				else
					try {
						doit = false;
						value = (BigDecimal) currentFormatter.parse(((Text) control).getText());
						((Text) control).setText(formatter.format(value));
					} catch (ParseException e1) {
						((Text) control).setText(textBefore);
						((Text) control).getDisplay().beep();
					} finally {
						doit = true;
						currentFormatter = formatter;
					}
			}
		});
		((Text) control).addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				textBefore = ((Text) control).getText();
			}
		});
		((Text) control).addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				NumericText.this.modifyText();
			}
		});

		((Text) control).addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent event) {

			}

			@Override
			public void keyPressed(KeyEvent event) {
				if ((event.character == SWT.CR) || (event.keyCode == SWT.ARROW_DOWN)) {
					((Text) control).traverse(SWT.TRAVERSE_TAB_NEXT);
				} else if (event.keyCode == SWT.ARROW_UP) {
					((Text) control).traverse(SWT.TRAVERSE_TAB_PREVIOUS);
				}
			}
		});
	}

	public void modifyText() {
		if (doit) {
			BigDecimal valueBefore = value;
			oldValue = value;
			doit = false;
			if (null == ((Text) control).getText() || 0 == ((Text) control).getText().length()
					|| ((Text) control).getText().equals("-")) {
				if (nullOk) {
					value = null;
				} else {
					value = new BigDecimal(0).setScale(maximumFractionDigits, BigDecimal.ROUND_HALF_UP);
				}
			} else
				try {
					String textValue = ((Text) control).getText();
					new BigDecimal(((Text) control).getText());
					int integerDigits = textValue.indexOf('.');
					if (integerDigits > -1) {
						if (0 == maximumFractionDigits)
							throw new ParseException("Fraction symbol is not allowed!", 0);
						if (maximumFractionDigits < textValue.length() - integerDigits - 1)
							throw new ParseException("Too many fraction digits", 0);
					} else {
						integerDigits = ((Text) control).getText().length();
					}
					if ('-' == textValue.charAt(0))
						integerDigits--;
					if (integerDigits > maximumIntegerDigits && -1 != maximumIntegerDigits)
						throw new ParseException("Too many integer digits", 0);
					value = (BigDecimal) currentFormatter.parse(((Text) control).getText());
					caretPos = ((Text) control).getCaretPosition();
				} catch (Exception e1) {
					value = valueBefore;
					setText(textBefore);
					((Text) control).setSelection(caretPos);
					((Text) control).getDisplay().beep();
				}
			doit = true;
		}
	}

	public BigDecimal getValue() {
		return value;
	}

	public BigDecimal getOldValue() {
		return oldValue;
	}

	public void setValue(BigDecimal value) throws ParseException {
		doit = false;
		if (null == value) {
			this.value = null;
			setText("");
		} else {
			this.value = value.setScale(maximumFractionDigits, BigDecimal.ROUND_HALF_UP);
			setText(currentFormatter.format(value));
		}
		doit = true;
	}

	public void setValue(Number value) throws ParseException {
		if (null == value) {
			this.value = null;
			setText("");
		} else
			setValue(new BigDecimal(value.toString()).setScale(maximumFractionDigits, BigDecimal.ROUND_HALF_UP));
		oldValue = this.value;
	}

	protected void setText(String textValue) {
		if (null == textValue)
			((Text) control).setText("");
		else
			((Text) control).setText(textValue);
	}

	public void setFormatter() {
		formatter = new DecimalFormat();
		formatter.setParseBigDecimal(true);
		maximumIntegerDigits = -1;
		maximumFractionDigits = 0;
		currentFormatter = formatter;
	}

	public void setFormatter(DecimalFormatSymbols decimalFormatSymbols) {
		formatter = new DecimalFormat();
		formatter.setParseBigDecimal(true);
		formatter.setDecimalFormatSymbols(decimalFormatSymbols);
		maximumIntegerDigits = -1;
		maximumFractionDigits = 0;
		currentFormatter = formatter;
	}

	public void setFormatter(String pattern) {
		setFormatter(pattern, new DecimalFormatSymbols(Locale.getDefault()));
	}

	public void setFormatter(String pattern, DecimalFormatSymbols decimalFormatSymbols) {
		this.pattern = pattern;
		formatter = new DecimalFormat(pattern, decimalFormatSymbols);
		formatter.setParseBigDecimal(true);

		if (null != pattern && pattern.length() > 0) {
			int grouping = 0;
			for (int i = 0; i < pattern.length(); i++)
				if (pattern.charAt(i) == ',')
					grouping++;
			if (pattern.indexOf('.') >= 0) {
				maximumIntegerDigits = pattern.indexOf('.') - grouping;
				maximumFractionDigits = pattern.length() - pattern.indexOf('.') - 1;
				formatter.setMaximumIntegerDigits(maximumIntegerDigits);
				formatter.setMaximumFractionDigits(maximumFractionDigits);
			} else {
				maximumIntegerDigits = pattern.length() - grouping;
				maximumFractionDigits = 0;
				formatter.setParseIntegerOnly(true);
				formatter.setMaximumIntegerDigits(maximumIntegerDigits);
			}
		}
		currentFormatter = formatter;
	}

	public void setNullOk(boolean nullOk) {
		this.nullOk = nullOk;
	}

	public boolean isDoit() {
		return doit;
	}

	private void setEditFormatter() {
		if (null == pattern) {
			editFormatter = new DecimalFormat();
			editFormatter.setGroupingSize(0);
		} else {
			char[] patternElements = pattern.toCharArray();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < patternElements.length; i++) {
				if ('#' == patternElements[i] || '0' == patternElements[i] || '.' == patternElements[i])
					sb.append(patternElements[i]);
			}
			String editPattern = sb.toString();
			editFormatter = new DecimalFormat(editPattern, new DecimalFormatSymbols(Locale.US));
		}
		editFormatter.setParseBigDecimal(true);
	}

	public int getMaximumFractionDigits() {
		return maximumFractionDigits;
	}

	@Override
	public void focusGained(FocusEvent event) {
		((Text) event.widget).selectAll();
	}

	@Override
	public void focusLost(FocusEvent event) {

	}

	@Override
	public void setValue(Object value) {
		try {
			if (value instanceof BigDecimal) {
				setValue((BigDecimal) value);
			} else if (value instanceof Number) {
				setValue((Number) value);
			} else if (value instanceof String) {
                setValue((String)value);
			}
		} catch (ParseException e) {
		}
	}

	@Override
	public void createControl() {
		style |= SWT.RIGHT;
		control = new Text(parent, style);
	}

}
