package br.com.anteros.helpme.treeviewer;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.StatusLineLayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public class CursorPositionContrib extends ContributionItem {

    private CLabel cursorPosLabel;

	public CursorPositionContrib() {
		super(CursorPositionContrib.class.getName());
	}

	@Override
	public void fill(Composite parent) {
		super.fill(parent);
        
		String text = "9999, 999";
		GC gc = new GC(parent);
		gc.setFont(parent.getFont());
		FontMetrics fm = gc.getFontMetrics();
		Point extent = gc.textExtent(text);
		
		StatusLineLayoutData statusLineLayoutData = new StatusLineLayoutData();
		statusLineLayoutData.widthHint = extent.x;
		statusLineLayoutData.heightHint = fm.getHeight();
		gc.dispose();
		
        cursorPosLabel = new CLabel(parent, SWT.NONE);
		cursorPosLabel.setLayoutData(statusLineLayoutData);
		cursorPosLabel.setText("");
	}

	public void setPosition(int lineNo, int charNo) {
		if (cursorPosLabel == null || cursorPosLabel.isDisposed())
			return;
		cursorPosLabel.setText("" + lineNo + ", " + charNo);
		cursorPosLabel.pack();
		cursorPosLabel.getParent().layout();
	}

}