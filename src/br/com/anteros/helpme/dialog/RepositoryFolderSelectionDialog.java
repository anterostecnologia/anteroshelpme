package br.com.anteros.helpme.dialog;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

public class RepositoryFolderSelectionDialog extends SelectionDialog {

    private TreeViewer treeViewer;

    private ITreeContentProvider contentProvider;

    private ILabelProvider labelProvider;

    private ITreeViewerListener treeListener;

    public void setTreeListener(ITreeViewerListener treeListener) {
        this.treeListener = treeListener;
    }

    private Object input;

    private int widthInChars = 55;

    private int heightInChars = 15;

    public RepositoryFolderSelectionDialog(Shell parentShell) {
        super(parentShell);
    }

    public void setInput(Object input) {
        this.input = input;
    }

    public void setContentProvider(ITreeContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    public void setLabelProvider(ILabelProvider labelProvider) {
        this.labelProvider = labelProvider;
    }

    @Override
    protected Control createDialogArea(Composite container) {
        Composite parent = (Composite) super.createDialogArea(container);
        createMessageArea(parent);
        treeViewer = new TreeViewer(parent, SWT.BORDER);
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(labelProvider);
        treeViewer.setInput(input);
        treeViewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                okPressed();
            }

        });

        treeViewer.addTreeListener(treeListener);

        GridData gd = new GridData(GridData.FILL_BOTH);
        treeViewer.getTree().setLayoutData(gd);
        gd.heightHint = convertHeightInCharsToPixels(heightInChars);
        gd.widthHint = convertWidthInCharsToPixels(widthInChars);
        return parent;
    }

    @Override
    protected void okPressed() {
        IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        setResult(selection.toList());
        super.okPressed();
    }

    public boolean getExpandedState(Object element) {
        if (element == null) {
            return false;
        }
        return treeViewer.getExpandedState(element);
    }
}
