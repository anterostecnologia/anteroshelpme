package br.com.anteros.helpme.treeviewer;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import br.com.anteros.helpme.treeviewer.ProjectTreeViewer.ListenerMeasure;
import br.com.anteros.helpme.treeviewer.ProjectTreeViewer.ListenerPaint;
import br.com.anteros.helpme.AnterosHelpmePlugin;

public class ObjectTreeViewer extends TreeViewer implements Listener {

	public ObjectTreeViewer(Tree tree) {
		super(tree);
		setContentProvider(new ObjectTreeContentProvider(this));
		setLabelProvider(new ObjectTreeLabelProvider());

		tree.addListener(SWT.Expand, this);
		tree.addListener(SWT.MeasureItem, new ListenerMeasure());
		tree.addListener(SWT.PaintItem, new ListenerPaint());

		tree.setHeaderVisible(true);
		TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
		column1.setText("Object");
		column1.setWidth(300);
		TreeColumn column2 = new TreeColumn(tree, SWT.LEFT);
		column2.setText("Value");
		column2.setWidth(1500);
	}

	@Override
	public void handleEvent(Event event) {
		final TreeItem root = (TreeItem) event.item;
		final IObjectNode node = (IObjectNode) root.getData();
		if (!node.isInitialized()) {
			node.initialized();
			TreeItem[] items = root.getItems();
			for (int i = 0; i < items.length; i++) {
				items[i].dispose();
			}
			TreeItem item=null;
			try {
				for (IObjectNode n : node.getChildren()) {
					item = new TreeItem(root, 0);
					item.setData(n);
					item.setText(n.getName());
					item.setImage(n.getLeftImage());
					if (n.hasChildren())
						item.setItemCount(1);
				}
				root.setImage(node.getLeftImage());
			} catch (Exception e) {
				AnterosHelpmePlugin.error("N�o foi poss�vel desenhar o item "+item.getText()+" na �rvore do ObjectViewer.", e, true);
			}
		}
	}

	class ListenerPaint implements Listener {

		@Override
		public void handleEvent(Event event) {
			TreeItem item = (TreeItem) event.item;
			Image trailingImage;
			if (item.getData() != null) {
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
					AnterosHelpmePlugin.error("N�o foi poss�vel desenhar o item "+item.getText()+" na �rvore do ObjectViewer.", e, true);
				}
			}
		}

	}

	class ListenerMeasure implements Listener {
		@Override
		public void handleEvent(Event event) {
			TreeItem item = (TreeItem) event.item;
			Image trailingImage;
			if (item.getData() != null) {
				try {
					trailingImage = ((IObjectNode) item.getData()).getLeftImage();
					if (trailingImage != null) {
						event.width += trailingImage.getBounds().width + 2;
					}
				} catch (Exception e) {
					AnterosHelpmePlugin.error("N�o foi poss�vel desenhar o item "+item.getText()+" na �rvore do ObjectViewer.", e, true);
				}
			}
		}
	}

	public void select(IObjectNode node) {
		setSelection(new StructuredSelection(node));
	}

	public void setFocus() {
		this.getTree().setFocus();
	}

}
