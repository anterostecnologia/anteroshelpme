package br.com.anteros.helpme.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

public class ObjectResultView extends ViewPart {

	public static final String ID = ObjectResultView.class.getName();

	private TreeViewer viewer;

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.FILL);
		container.setLayout(new FillLayout());
		final int IMAGE_MARGIN = 2;
		final Tree tree = new Tree(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer = new TreeViewer(tree);
		viewer.setContentProvider(new MyContentProvider());
		viewer.setLabelProvider(new LabelProvider());
		viewer.setInput(getInitialModel());
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	
	
	/*
	 * defines initial version of the model
	 */
	private static TreeNode getInitialModel() {
		TreeNode result = new TreeNode(null, "Root");
		new TreeNode(result, "Top level " + 0);
		new TreeNode(result, "Top level " + 1);
		for (int i = 2; i < 5; i++) {
			TreeNode tl = new TreeNode(result, "Top level " + i);
			for (int j = 0; j < 3; j++) {
				new TreeNode(tl, "Leaf " + i + "" + j);
			}
		}
		return result;
	}

	/*
	 * changes the model
	 */
	private static void updateModel(TreeNode model) {
		for (int j = 2; j < 5; j++) {
			for (int i = 2; i > 0; i--) {
				model.children.get(j).children.remove(i);
			}
		}
		model.children.remove(0);
		model.children.remove(0);
	}

	private static final class MyContentProvider implements ILazyTreeContentProvider {

		private TreeViewer treeViewer;

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			treeViewer = (TreeViewer) viewer;
			treeViewer.refresh();
		}

		@Override
		public void updateElement(Object parent, int index) {
			TreeNode node = (TreeNode) parent;
			if (index < node.children.size()) {
				TreeNode element = node.children.get(index);
				treeViewer.replace(parent, index, element);
				updateChildCount(element, -1);
			}
		}

		@Override
		public void updateChildCount(Object element, int currentChildCount) {
			TreeNode node = (TreeNode) element;
			int size = node.children.size();
			treeViewer.setChildCount(element, size);
		}

		@Override
		public Object getParent(Object element) {
			return ((TreeNode) element).parent;
		}
	}

	/**
	 * Model for the viewer.
	 */
	private static class TreeNode {
		private String value;
		private final List<TreeNode> children = new ArrayList<TreeNode>();
		private final TreeNode parent;

		public TreeNode(TreeNode parent, String value) {
			this.parent = parent;
			this.value = value;
			if (parent != null) {
				parent.children.add(this);
			}
		}

		@Override
		public String toString() {
			return value;
		}
	}


}
