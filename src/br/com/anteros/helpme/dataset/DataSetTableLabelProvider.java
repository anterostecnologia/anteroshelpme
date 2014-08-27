package br.com.anteros.helpme.dataset;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.nebula.widgets.formattedtext.DateTimeFormatter;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.swt.graphics.Image;

public class DataSetTableLabelProvider implements ITableLabelProvider {

	private DateTimeFormatter df;
	private NumberFormatter nf;

	public DataSetTableLabelProvider() {
		df = new DateTimeFormatter();
		nf = new NumberFormatter("###,###,###,##0.00#########");
	}

	public void addListener(ILabelProviderListener listener) {

	}

	public void dispose() {

	}

	public Image getColumnImage(Object element, int columnIndex) {

		return null;
	}

	public String getColumnText(Object element, int columnIndex) {

		DataSetRow row = (DataSetRow) element;

		Object tmp = row.getObjectValue(columnIndex);

		if (tmp != null) {

			if (tmp.getClass() == Timestamp.class) {
				df.setValue(new java.util.Date(((Timestamp) tmp).getTime()));
				return df.getDisplayString();
			}
			if (tmp.getClass() == Date.class) {
				df.setValue(new java.util.Date(((Date) tmp).getTime()));
				return df.getDisplayString();
			}
			if ((tmp.getClass() == Float.class) || (tmp.getClass() == Double.class)
					|| (tmp.getClass() == BigDecimal.class)) {
                nf.setValue(tmp);
                return nf.getDisplayString();
			}

			return tmp.toString();
		}
		return "<null>";

	}

	public boolean isLabelProperty(Object element, String property) {

		return false;
	}

	public void removeListener(ILabelProviderListener listener) {

	}

}
