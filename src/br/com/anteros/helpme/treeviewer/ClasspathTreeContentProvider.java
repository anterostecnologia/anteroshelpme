package br.com.anteros.helpme.treeviewer;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ClasspathTreeContentProvider implements ITreeContentProvider {

	private ClasspathTreeViewer treeViewer;
	
	public ClasspathTreeContentProvider(ClasspathTreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return ((IObjectNode)parentElement).getChildren();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object getParent(Object element) {
		return ((IObjectNode)element).getParent();
	}

	@Override
	public boolean hasChildren(Object arg0) {
		return true;
	}

	public ClasspathTreeViewer getTreeViewer() {
		return treeViewer;
	}

	public void setTreeViewer(ClasspathTreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

}
