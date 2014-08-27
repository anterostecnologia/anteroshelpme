package br.com.anteros.helpme.dialog;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SourceViewerDialog extends Dialog {

    private SourceViewer fSourceViewer;

    private IDocument document = null;

    private String title;;

    protected final static int VERTICAL_RULER_WIDTH = 12;

    private IInputValidator validator;

    public SourceViewerDialog(Shell parentShell, String title) {
        super(parentShell);
        this.title = title;
        this.setShellStyle(this.getShellStyle() | SWT.RESIZE);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(title);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        int styles = SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION;
        fSourceViewer = new SourceViewer(composite, null, styles);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(fSourceViewer.getControl());
        if (document != null) {
            fSourceViewer.setDocument(document);
        }
        return composite;
    }

    public void setDocument(IDocument doc) {
        document = doc;
    }

    public void setDocument(File source) {
        if (source == null || !source.exists()) {
            return;
        }
        Reader in = null;
        try {
            in = new FileReader(source);
            StringBuilder buffer = new StringBuilder();
            char[] readBuffer = new char[2048];
            int n = in.read(readBuffer);
            while (n > 0) {
                buffer.append(readBuffer, 0, n);
                n = in.read(readBuffer);
            }
            document = new Document(buffer.toString());
        } catch (Exception e) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException x) {
            	
            }
        }
    }

    public String getResult() {
        return document.get();
    }

    /**
     * Getter for document.
     * 
     * @return the document
     */
    public IDocument getDocument() {
        return this.document;
    }

    /**
     * Creates the vertical ruler to be used by this editor. Subclasses may re-implement this method.
     * 
     * @return the vertical ruler
     */
    protected IVerticalRuler createVerticalRuler() {
        return new VerticalRuler(VERTICAL_RULER_WIDTH);
    }

}
