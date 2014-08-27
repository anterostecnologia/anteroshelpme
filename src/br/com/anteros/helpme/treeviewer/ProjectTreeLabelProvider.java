package br.com.anteros.helpme.treeviewer;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ProjectTreeLabelProvider extends LabelProvider {
	public String getText(Object element) {
		String[] s = ((IObjectNode) element).getName();
		if ((s != null) && (s.length > 0))
			return s[0];
		return null;
	}

	public Image getImage(Object element) {
		try {
			return ((IObjectNode) element).getLeftImage();
		} catch (Exception e) {
			return null;
		}
	}

}
