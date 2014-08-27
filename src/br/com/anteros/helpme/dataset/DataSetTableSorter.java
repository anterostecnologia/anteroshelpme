package br.com.anteros.helpme.dataset;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Comparator;

import org.eclipse.swt.SWT;

public class DataSetTableSorter implements Comparator {

	protected int[] priorities;

	protected int[] directions;

	protected int[] columnTypes;

	public DataSetTableSorter(DataSet dataSet) {

		this.columnTypes = dataSet.getColumnTypes();
		this.priorities = new int[columnTypes.length];
		this.directions = new int[columnTypes.length];

		for (int i = 0; i < columnTypes.length; i++) {
			this.directions[i] = SWT.NONE;
			this.priorities[i] = i;
		}

	}

	public void setTopPriority(int priority, int direction) {

		if (priority < 0 || priority >= priorities.length) {
			return;
		}
		int index = -1;
		for (int i = 0; i < priorities.length; i++) {
			if (priorities[i] == priority) {
				index = i;
				break;
			}
		}

		if (index == -1) {
			return;
		}

		for (int i = index; i > 0; i--) {
			priorities[i] = priorities[i - 1];
		}
		priorities[0] = priority;
		directions[priority] = direction;
	}

	public int compare(Object e1, Object e2) {
		return compareColumnValue((DataSetRow) e1, (DataSetRow) e2, 0);
	}

	private int compareColumnValue(DataSetRow m1, DataSetRow m2, int depth) {
		if (depth >= priorities.length) {
			return 0;
		}
		int columnNumber = priorities[depth];
		int direction = directions[columnNumber];
		int result = 0;

		Object o1 = m1.getObjectValue(columnNumber);
		Object o2 = m2.getObjectValue(columnNumber);

		if (o1 == null || o2 == null) {
			if (o1 == null && o2 != null) {
				result = 1;
			} else if (o1 != null && o2 == null) {
				result = -1;
			} else {
				result = 0;
			}

			if (result == 0) {
				return compareColumnValue(m1, m2, depth + 1);
			}

			if (direction == SWT.DOWN) {
				return result * -1;
			}
			return result;
		}

		switch (columnTypes[columnNumber]) {

		case DataSet.STRING:
			result = ((String) o1).compareTo((String) o2);
			break;

		case DataSet.FLOAT:
			result = ((Double) o1).compareTo((Double) o2);
			break;

		case DataSet.LONG:
		case DataSet.INTEGER:
			result = ((Long) o1).compareTo((Long) o2);
			break;

		case DataSet.DATE:
			result = ((Date) o1).compareTo((Date) o2);
			break;

		case DataSet.DATE_TIME:
			result = ((Timestamp) o1).compareTo((Timestamp) o2);
			break;

		case DataSet.TIME:
			result = ((Time) o1).compareTo((Time) o2);
			break;
		default:
		}

		if (result == 0) {
			return compareColumnValue(m1, m2, depth + 1);
		}

		if (direction == SWT.DOWN) {
			return result * -1;
		}
		return result;
	}

}
