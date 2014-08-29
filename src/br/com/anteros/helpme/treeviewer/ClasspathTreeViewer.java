package br.com.anteros.helpme.treeviewer;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.model.ClasspathRoot;

public class ClasspathTreeViewer extends TreeViewer /*implements Listener*/ {

	public ClasspathTreeViewer(Tree tree) {
		super(tree);
		
		setContentProvider(new ClasspathTreeContentProvider(this));
		setLabelProvider(new ClassPathTreeLabelProvider());
		setInput(new ClasspathRoot());
		

		GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
		data.heightHint = getTree().getItemHeight();
		getTree().setLayoutData(data);
	}

	/*@Override
	public void handleEvent(Event event) {
		final TreeItem root = (TreeItem) event.item;
		final IObjectNode node = (IObjectNode) root.getData();
		if (!node.isInitialized()) {
			TreeItem[] items = root.getItems();
			for (int i = 0; i < items.length; i++) {
				items[i].dispose();
			}
			try {
				this.addObjects(root, node.getChildren());
			} catch (Exception e) {
				e.printStackTrace();
			}
			node.setInitialized(true);
		}
	}

	public ClasspathTreeViewer addObject(TreeItem root, IObjectNode node) throws Exception {
		if ((node != null) && (root != null)) {
			TreeItem item = new TreeItem(root, 0);
			item.setData(node);
			item.setText(node.getName());
			item.setImage(node.getLeftImage());
			item.setItemCount(1);
		}
		return this;
	}

	public ClasspathTreeViewer addObjects(TreeItem root, IObjectNode[] nodes) throws Exception {
		if (nodes != null) {
			for (IObjectNode node : nodes) {
				addObject(root, node);
			}
		}
		return this;
	}

	public ClasspathTreeViewer addObject(IObjectNode node) throws Exception {
		return addObject(node, 1);
	}

	public ClasspathTreeViewer addObject(IObjectNode node, int itemCount) throws Exception {
		if (node != null) {
			TreeItem item = new TreeItem(this.getTree(), 0);
			item.setData(node);
			item.setText(node.getName());
			item.setImage(node.getLeftImage());
			item.setItemCount(itemCount);
		}
		return this;
	}

	public ClasspathTreeViewer addObjects(IObjectNode[] nodes) throws Exception {
		if (nodes != null) {
			for (IObjectNode node : nodes) {
				addObject(node);
			}
		}
		return this;
	}*/

	class ListenerPaint implements Listener {
		@Override
		public void handleEvent(Event event) {
			TreeItem item = (TreeItem) event.item;
			Image trailingImage;
			try {
				trailingImage = ((IObjectNode) item.getData()).getRightImage();
				if (trailingImage != null) {
					int x = event.x + event.width + 2;
					int itemHeight = getTree().getItemHeight();
					int imageHeight = trailingImage.getBounds().height;
					int y = event.y + (itemHeight - imageHeight) / 2;
					event.gc.drawImage(trailingImage, x, y);
				}
			} catch (Exception e) {
				AnterosHelpmePlugin.error("Não foi possível desenhar o item "+item.getText()+" na árvore do ClassPath.", e, true);
			}
		}

	}

	class ListenerMeasure implements Listener {
		@Override
		public void handleEvent(Event event) {
			TreeItem item = (TreeItem) event.item;
			Image trailingImage;
			try {
				trailingImage = ((IObjectNode) item.getData()).getLeftImage();
				if (trailingImage != null) {
					event.width += trailingImage.getBounds().width + 2;
				}
			} catch (Exception e) {
				AnterosHelpmePlugin.error("Não foi possível desenhar o item "+item.getText()+" na árvore do ClassPath.", e, true);
			}
		}

	}
	
	public void select(IObjectNode node) {
		setSelection(new StructuredSelection(node));
	}

}
