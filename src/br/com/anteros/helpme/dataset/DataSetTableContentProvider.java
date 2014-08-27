package br.com.anteros.helpme.dataset;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class DataSetTableContentProvider implements IStructuredContentProvider {

	public Object[] getElements(Object inputElement) {

		DataSet dataSet = (DataSet) inputElement;
		return dataSet.getRows();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
