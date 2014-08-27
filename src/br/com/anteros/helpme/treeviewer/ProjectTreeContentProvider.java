package br.com.anteros.helpme.treeviewer;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ProjectTreeContentProvider implements ITreeContentProvider {

	private ProjectTreeViewer treeViewer;
	
	public ProjectTreeContentProvider(ProjectTreeViewer treeViewer) {
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

	public ProjectTreeViewer getTreeViewer() {
		return treeViewer;
	}

	public void setTreeViewer(ProjectTreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

}
