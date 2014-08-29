package br.com.anteros.helpme.treeviewer;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class ObjectTreeLabelProvider implements ITableLabelProvider {
	

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getColumnImage(Object element, int index) {
		if (index == 0) {
			try {
				return ((IObjectNode) element).getLeftImage();
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int index) {
		String[] s = ((IObjectNode) element).getName();
		if ((s != null) && (s.length > index))
			return s[index];
		return null;
	}
}
