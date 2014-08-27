package br.com.anteros.helpme.dataset;

public class DataSetColumn {

	private String columnName;
	private int columnIndex;
	private int columnType;

	public DataSetColumn(String columnName, int columnIndex, int columnType) {
		this.columnName = columnName;
		this.columnIndex = columnIndex;
		this.columnType = columnType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public int getColumnType() {
		return columnType;
	}

	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}
	
}
