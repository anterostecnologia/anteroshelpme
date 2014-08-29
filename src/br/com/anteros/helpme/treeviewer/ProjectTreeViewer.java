package br.com.anteros.helpme.treeviewer;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.util.StringUtils;

public class ProjectTreeViewer extends TreeViewer implements Listener {

	public ProjectTreeViewer(Tree tree) {
		super(tree);

		setContentProvider(new ProjectTreeContentProvider(this));
		setLabelProvider(new ProjectTreeLabelProvider());

		tree.addListener(SWT.Expand, this);
		tree.addListener(SWT.MeasureItem, new ListenerMeasure());
		tree.addListener(SWT.PaintItem, new ListenerPaint());
	}

	@Override
	public void handleEvent(Event event) {
		refreshNode((TreeItem) event.item); 
	}

	public void refreshNode(final TreeItem item) {
		List<Object> expandedElements = Arrays.asList(this.getExpandedElements());
		final IObjectNode node = (IObjectNode) item.getData();
		if (!node.isInitialized()) {
			TreeItem[] items = item.getItems();
			for (int i = 0; i < items.length; i++) {
				items[i].dispose();
			}
			TreeItem newItem = null;
			node.initialized();	
			try {
				for (IObjectNode n : node.getChildren()) {
					newItem = new TreeItem(item, 0);
					newItem.setData(n);
					newItem.setText(n.getName());
					newItem.setImage(n.getLeftImage());
					if (n.hasChildren())
						newItem.setItemCount(1);
				}
				item.setImage(node.getLeftImage());
			} catch (Exception e) {
				AnterosHelpmePlugin.error("Não foi possível desenhar o item " + item.getText()
						+ " na árvore do ProjectViewer.", e, true);
			}
		}
	}

	class ListenerPaint implements Listener {

		@Override
		public void handleEvent(Event event) {
			TreeItem item = (TreeItem) event.item;
			Image relationShipImage;
			if (item.getData() != null) {
				try {
					IObjectNode node = (IObjectNode) item.getData();
					relationShipImage = node.getRightImage();
					if (relationShipImage != null) {
						int x = event.x + event.width + 2;
						int itemHeight = getTree().getItemHeight();
						int imageHeight = relationShipImage.getBounds().height;
						int yText = event.y + 3;
						int yImage = event.y + (itemHeight - imageHeight) / 2;
						event.gc.drawImage(relationShipImage, x, yImage);
						event.gc.setForeground(new Color(AnterosHelpmePlugin.getDefault().getShell().getDisplay(), 255,
								181, 106));
						if (StringUtils.isNotEmpty(node.getColumnName()))
							event.gc.drawString(node.getColumnName(), x + relationShipImage.getBounds().width + 2,
									yText);
					} else {
						if (StringUtils.isNotEmpty(node.getColumnName())) {
							int x = event.x + event.width + 2;
							event.gc.setForeground(new Color(AnterosHelpmePlugin.getDefault().getShell().getDisplay(),
									255, 181, 106));
							event.gc.drawString(node.getColumnName(), x, event.y + 2);
						}
					}
				} catch (Exception e) {
					AnterosHelpmePlugin.error("Não foi possível desenhar o item " + item.getText()
							+ " na árvore do ProjectViewer.", e, true);
				}
			}
		}

	}

	class ListenerMeasure implements Listener {
		@Override
		public void handleEvent(Event event) {
			TreeItem item = (TreeItem) event.item;
			Image relationShipImage;
			if (item.getData() != null) {
				try {
					relationShipImage = ((IObjectNode) item.getData()).getRightImage();
					if (relationShipImage != null) {
						event.width += relationShipImage.getBounds().width + 2;
					} else if (StringUtils.isNotEmpty(((IObjectNode) item.getData()).getColumnName())) {
						event.width += (event.gc.getCharWidth('H') * ((IObjectNode) item.getData()).getColumnName()
								.length());
					}
				} catch (Exception e) {
					AnterosHelpmePlugin.error("Não foi possível desenhar o item " + item.getText()
							+ " na árvore do ProjectViewer.", e, true);
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
