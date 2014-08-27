package br.com.anteros.helpme.dataset;

public class DataSetRow {

    private Object[] pvalues;

    public DataSetRow(int columnCount) {
        pvalues = new Object[columnCount];
    }

    public DataSetRow(String[] values) {
        pvalues = values;
    }

    public Object getObjectValue(int column) {

        Object tmp = pvalues[column];
        if (tmp != null) {
            return tmp;
        }
        return "<null>";
    }

    public void setValue(int column, Object value) {
        pvalues[column] = value;
    }

    public int length() {
        if (pvalues == null) {
            return 0;
        }
        return pvalues.length;
    }
}
