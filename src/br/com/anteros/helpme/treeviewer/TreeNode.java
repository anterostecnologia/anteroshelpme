package br.com.anteros.helpme.treeviewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Image;

public abstract class TreeNode implements IObjectNode {

	protected IObjectNode parent;
	protected List<IObjectNode> children = new ArrayList<IObjectNode>();
	protected String[] name;
	protected String columnName;
	protected boolean initialized;
	protected Set<IObjectNodeListener> listeners = new LinkedHashSet<IObjectNodeListener>();

	@Override
	public IObjectNode[] getChildren() {
		return children.toArray(new IObjectNode[] {});
	}

	@Override
	public void setName(String[] name) {
		this.name = name;
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public void setLeftImage(Image image) {
	}

	@Override
	public Image getLeftImage() throws Exception {
		return null;
	}

	@Override
	public void setRightImage(Image image) {
	}

	@Override
	public Image getRightImage() throws Exception {
		return null;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	@Override
	public IObjectNode getParent() {
		return parent;
	}

	@Override
	public void setParent(IObjectNode parent) {
		this.parent = parent;
	}

	@Override
	public void remove(IObjectNode node) {
		children.remove(node);
		for (IObjectNodeListener ln : listeners) {
			ln.onRemoveNode(node);
		}
	}

	@Override
	public void removeAll() {
		children.clear();
		for (IObjectNodeListener ln : listeners) {
			ln.onRemoveAllNode();
		}
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!isExistsNode(node)) {
			node.setParent(this);
			children.add(node);
			for (IObjectNodeListener ln : listeners) {
				ln.onAddNode(node);
			}
		}
	}

	@Override
	public void addAllNode(IObjectNode[] node) throws Exception {
		for (IObjectNode n : node) {
			addNode(n);
		}
	}

	@Override
	public boolean isExistsNode(IObjectNode node) {
		for (IObjectNode source : children) {
			if (source.getSimpleName().equals(node.getSimpleName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getSimpleName() {
		if ((getName() == null) && (getName().length == 0))
			return "";
		return getName()[0];
	}

	@Override
	public void initialized() {
		initialized=true;
	}

	@Override
	public void addListener(IObjectNodeListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(IObjectNodeListener listener) {
		listeners.remove(listener);
	}

	public Set<IObjectNodeListener> getListeners() {
		return Collections.unmodifiableSet(listeners);
	}

	@Override
	public IObjectNode getFirstNode() {
		if (children.size() == 0)
			return null;
		return children.get(0);
	}

	@Override
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	@Override
	public String getColumnName() {
		return columnName;
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public void uninitialized() {
		initialized = false;
		children.clear();
	}

}
