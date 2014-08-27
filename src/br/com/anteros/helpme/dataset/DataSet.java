package br.com.anteros.helpme.dataset;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataSet {

	public static final int DATE = 3;

	public static final int DATE_TIME = 4;

	public static final int FLOAT = 1;

	public static final int INTEGER = 2;

	public static final int LONG = 6;

	public static final int STRING = 0;

	public static final int TIME = 5;

	private DataSetColumn[] columns;

	private DataSetRow[] rows;

	private DataSetTableSorter sorter;

	private DataSet() {

	}

	public DataSet(DataSetColumn[] columns, ResultSet resultSet) throws Exception {

		initialize(columns, resultSet);
	}

	public DataSet(ResultSet resultSet) throws Exception {

		initialize(null, resultSet);
	}

	public DataSet(DataSetColumn[] columns, String sql, Connection connection) throws Exception {

		Statement statement = connection.createStatement();

		statement.execute(sql);
		ResultSet resultSet = statement.getResultSet();

		initialize(columns, resultSet);

		statement.close();

	}

	public DataSet(DataSetColumn[] columns, String[][] data) throws Exception {

		this.columns = columns;

		rows = new DataSetRow[data.length];

		for (int i = 0; i < data.length; i++) {
			rows[i] = new DataSetRow(data[i]);
		}
	}

	public int getColumnIndex(String name) {
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(name)) {
				return i;
			}
		}
		return 0;
	}

	public DataSetRow[] getRows() {
		return rows;
	}

	@SuppressWarnings("unchecked")
	private void initialize(DataSetColumn[] columns, ResultSet resultSet) throws Exception {

		ResultSetMetaData metadata = resultSet.getMetaData();

		if (columns == null || columns.length == 0) {
			columns = new DataSetColumn[metadata.getColumnCount()];
			for (int i = 1; i <= metadata.getColumnCount(); i++) {
				int columnType = 0;

				switch (metadata.getColumnType(i)) {
				case Types.CHAR:
				case Types.VARCHAR:
				case Types.LONGVARCHAR:
				case -9:
					columnType = STRING;
					break;

				case Types.INTEGER:
				case Types.SMALLINT:
				case Types.TINYINT:
					columnType = INTEGER;
					break;

				case Types.DECIMAL:
				case Types.NUMERIC:
				case Types.DOUBLE:
				case Types.FLOAT:
				case Types.REAL:
					columnType = FLOAT;
					break;

				case Types.DATE:
				case Types.TIMESTAMP:
					columnType = DATE_TIME;
					break;

				case Types.TIME:
					columnType = TIME;
					break;

				case Types.BIGINT:
					columnType = LONG;
					break;

				default:
					columnType = STRING;
				}
				columns[i - 1] = new DataSetColumn(metadata.getColumnName(i), i, columnType);
			}
		}
		ArrayList rows = new ArrayList(30);
		try {

			if (resultSet.first()) {
				do {
					DataSetRow row = new DataSetRow(columns.length);
					for (int i = 0; i < columns.length; i++) {
						switch (columns[i].getColumnType()) {
						case STRING:
							row.setValue(i, resultSet.getString(columns[i].getColumnIndex()));
							break;
						case INTEGER:
							row.setValue(i, new Long(resultSet.getInt(columns[i].getColumnIndex())));
							break;
						case FLOAT:
							row.setValue(i, new Double(resultSet.getDouble(columns[i].getColumnIndex())));
							break;
						case DATE:
							row.setValue(i, resultSet.getDate(columns[i].getColumnIndex()));
							break;
						case DATE_TIME:
							try {
								row.setValue(i, resultSet.getTimestamp(columns[i].getColumnIndex()));
							} catch (SQLException e) {
								row.setValue(i, resultSet.getString(columns[i].getColumnIndex()));
							}
							break;
						case TIME:
							row.setValue(i, resultSet.getTime(columns[i].getColumnIndex()));
							break;
						case LONG:
							row.setValue(i, new Long(resultSet.getLong(columns[i].getColumnIndex())));
							break;
						default:
							row.setValue(i, resultSet.getString(columns[i].getColumnIndex()));
							break;
						}
						if (resultSet.wasNull()) {
							row.setValue(i, null);
						}
					}
					rows.add(row);
				} while (resultSet.next());
			}
		} finally {
			resultSet.close();
		}
		this.columns = columns;
		this.rows = (DataSetRow[]) rows.toArray(new DataSetRow[] {});

	}

	@SuppressWarnings("unchecked")
	public void sort(int columnIndex, int sortDirection) {

		if (sorter == null) {
			sorter = new DataSetTableSorter(this);
		}
		sorter.setTopPriority(columnIndex, sortDirection);

		Arrays.sort(rows, sorter);
	}

	public DataSetColumn[] getColumns() {
		return columns;
	}

	public void setColumns(DataSetColumn[] columns) {
		this.columns = columns;
	}

	public String[] getColumnLabels() {
		List<String> result = new ArrayList<String>();
		for (DataSetColumn column : columns) {
			result.add(column.getColumnName());
		}
		return result.toArray(new String[] {});
	}

	public int[] getColumnTypes() {
		int[] result = new int[columns.length];
		for (int i = 0; i < columns.length; i++) {
			result[i] = columns[i].getColumnType();
		}
		return result;
	}

}
