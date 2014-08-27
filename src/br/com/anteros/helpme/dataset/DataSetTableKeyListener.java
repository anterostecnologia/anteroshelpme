package br.com.anteros.helpme.dataset;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import br.com.anteros.helpme.AnterosHelpmePlugin;

public class DataSetTableKeyListener implements KeyListener {

    private Composite pparent = null;

    private Table ptable = null;

    private TableCursor pcursor = null;

    private Shell ppopup = null;

    private static final int CTRL_C = 3;

    private static final int CTRL_F = 6;

    private static final int ENTER = 13;

    private String plastNameSearched = null;
    
    private int plastColumnIndex = 0;

   
    public DataSetTableKeyListener(Composite parent, Table table, TableCursor cursor) {

        ptable = table;
        pparent = parent;
        pcursor = cursor;
        
    }


    public void keyPressed(KeyEvent e) {

        switch (e.character) {

        case CTRL_C:
            try {

                Clipboard clipBoard = new Clipboard(Display.getCurrent());
                TextTransfer textTransfer = TextTransfer.getInstance();

                TableItem[] items = ptable.getSelection();

                if (items == null || items.length == 0) {
                    return;
                }

                int columnIndex = pcursor.getColumn();
                clipBoard.setContents(new Object[] { items[0].getText(columnIndex) }, new Transfer[] { textTransfer });

            } catch (Exception ex) {
            	AnterosHelpmePlugin.getDefault().error("N�o foi poss�vel executar o SQL.", ex, false);
            }
            break;

        case CTRL_F:
            createPopup();
            break;
        default:
            return;

        }

    }


    public void keyReleased(KeyEvent e) {

        switch (e.keyCode) {

        case SWT.F5:
            break;

        case SWT.ESC:
            disposePopup();
            break;
        default:
            return;

        }

    }


   private void createPopup() {

        plastNameSearched = null;
        
        if (ppopup != null && !ppopup.isDisposed()) {
            if (!ppopup.isVisible()) {
                ppopup.open();
            }
            return;
        }

        Point popupLocation = ptable.toDisplay(10, 40);
        
        ppopup = new Shell(pparent.getShell(), SWT.BORDER | SWT.ON_TOP);
        ppopup.setBackground(pparent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
        ppopup.setForeground(pparent.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
        ppopup.setSize(250, 50);
        ppopup.setLocation(popupLocation);
        ppopup.setLayout(new GridLayout());

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;

        Label label = new Label(ppopup, SWT.NULL);
        label.setText("Find"); 
        label.setBackground(pparent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
        
        final Text input = new Text(ppopup, SWT.SINGLE | SWT.FILL);
        input.setLayoutData(gridData);
        input.setBackground(pparent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));

        
        input.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {

                Text t = (Text) e.widget;
                String text = t.getText();

                if (jumpToColumn(text)) {
                    input.setForeground(pparent.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
                } else {
                    input.setForeground(pparent.getDisplay().getSystemColor(SWT.COLOR_RED));                    
                }
            }

        });
        

        input.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {       
                if (e.character == ENTER) {
                    if (jumpToColumn(null)) {
                        input.setForeground(pparent.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
                    } else {
                        input.setForeground(pparent.getDisplay().getSystemColor(SWT.COLOR_RED));                    
                    }
                }                
            }            
        });
        
        input.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                disposePopup();
            }
            
        });

        
        ppopup.open();
        ppopup.forceActive();

    }


    private void disposePopup() {
        if (ppopup != null && !ppopup.isDisposed()) {
            ppopup.close();
            ppopup.dispose();
            ppopup = null;
        }
    }
    
    
    private boolean jumpToColumn(String name) {
        
        String text = null;
        if (name != null) {            
            text = name.toLowerCase().trim();
            plastNameSearched = text;
            plastColumnIndex = 0;
        } else {
            text = plastNameSearched;
            plastColumnIndex += 1;
        }
        
        if (text == null) {
            text = ""; 
        }
        
        
        TableColumn[] columns = ptable.getColumns();        
        if (columns == null || plastColumnIndex >= columns.length) {
                       
            plastColumnIndex = 0;
            return false;
        }
        
        boolean columnFound = false;
        
        for (int i = plastColumnIndex; i < columns.length; i++) {
                       
            TableColumn column = columns[i];

            if (column.getText().toLowerCase().startsWith(text)) {

                columnFound = true;

                ptable.showColumn(columns[columns.length - 1]);

                ptable.showColumn(column);
                
                if (ptable.getItemCount() > 0) {
                    pcursor.setSelection(0, i);
                    pcursor.setVisible(true);
                }

                plastColumnIndex = i;
                
                break;

            }
        }
        
        if (!columnFound) {
            plastColumnIndex = 0;
        }
        
        return columnFound;
    }
}
