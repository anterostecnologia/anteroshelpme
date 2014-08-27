package br.com.anteros.helpme.dataset;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class DataSetTable {

    private Composite container;

	private DataSetTable() {

    }

    public DataSetTable(Composite parent, final DataSet dataSet, String info, boolean sqlExecution) throws Exception {
        container = new Composite(parent, SWT.FILL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginLeft = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 2;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        container.setLayout(layout);
        if (sqlExecution) {
            container.setLayoutData(new GridData(GridData.FILL_BOTH));
        }

        DataSetColumn[] columns = dataSet.getColumns();
        if (columns == null || columns.length == 0) {
            throw new Exception("Invalid columns in DataSet "); 
        }

        final TableViewer tableViewer = new TableViewer(container, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI
                | SWT.VIRTUAL);
        final Table table = tableViewer.getTable();
        tableViewer.setColumnProperties(dataSet.getColumnLabels());
        table.setItemCount(dataSet.getRows().length);

        Listener sortListener = new Listener() {

            public void handleEvent(Event e) {

                TableColumn sortColumn = table.getSortColumn();
                TableColumn currentColumn = (TableColumn) e.widget;
                int dir = table.getSortDirection();
                if (sortColumn == currentColumn) {
                    dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
                } else {
                    table.setSortColumn(currentColumn);
                    dir = SWT.UP;
                }

                dataSet.sort(((DataSetColumn) currentColumn.getData("orignalColumn")).getColumnIndex(), dir); 

                table.setSortDirection(dir);
                table.clearAll();
            }
        };

        GridData tGridData = new GridData();
        tGridData.horizontalSpan = 2;
        tGridData.grabExcessHorizontalSpace = true;
        tGridData.grabExcessVerticalSpace = true;
        tGridData.horizontalAlignment = SWT.FILL;
        tGridData.verticalAlignment = SWT.FILL;
        table.setLayoutData(tGridData);
        GridLayout tlayout = new GridLayout();
        tlayout.numColumns = 2;
        tlayout.marginLeft = 0;
        table.setLayout(tlayout);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        table.setData(dataSet);

        for (DataSetColumn c: columns) {
            TableColumn column = new TableColumn(table, SWT.LEFT);
            column.setText(c.getColumnName());
            column.setMoveable(true);
            column.setResizable(true);
            column.addListener(SWT.Selection, sortListener);
            column.setData("orignalColumn", c); 
            if ((c.getColumnType() == DataSet.FLOAT)||(c.getColumnType() == DataSet.LONG)||(c.getColumnType() == DataSet.INTEGER)){
            	column.setAlignment(SWT.RIGHT);
            }
        }

        tableViewer.setContentProvider(new DataSetTableContentProvider());
        tableViewer.setLabelProvider(new DataSetTableLabelProvider());
        tableViewer.setInput(dataSet);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumn(i).pack();
        }

        Label infoLabel = new Label(container, SWT.NULL);
        infoLabel.setText(info);
        infoLabel.setLayoutData(new GridData(SWT.LEFT, SWT.NULL, true, false));
        final Label positionLabel = new Label(container, SWT.NULL);
        positionLabel.setText(""); 
        positionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NULL, true, false));

        final TableCursor cursor = new TableCursor(table, SWT.NONE);
        cursor.setBackground(table.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
        cursor.setForeground(table.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
        cursor.setLayout(new FillLayout());
        cursor.setVisible(false);
        cursor.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                table.setSelection(new TableItem[] { cursor.getRow() });
                cursor.setVisible(true);

                positionLabel.setText("row: " 
                        + (table.indexOf(cursor.getRow()) + 1) + "col: " 
                        + (cursor.getColumn() + 1));
                positionLabel.getParent().layout();
                positionLabel.redraw();
            }
        });

        cursor.addControlListener(new ControlAdapter() {

            public void controlResized(ControlEvent e) {
                if (cursor != null) {
                    if (cursor.getRow() == null) {
                        cursor.setVisible(false);
                    } else {
                        cursor.layout();
                        cursor.redraw();
                        cursor.setVisible(true);
                    }
                }
            }
        });

        table.addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent e) {
                Table t = (Table) e.widget;
                if (t.getItemCount() != 0) {
                    cursor.setVisible(true);
                }
            }
        });

        KeyListener keyListener = new DataSetTableKeyListener(parent, table, cursor);
        cursor.addKeyListener(keyListener);
        table.addKeyListener(keyListener);

    }
    
    public Control getControl(){
    	return container;
    }

}
