package br.com.anteros.helpme.dialog;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class ProgressDialog {

    private Shell parentShell;

    private long timeBeforeShowDialog;

    public ProgressDialog(Shell parentShell) {
        super();
        this.parentShell = parentShell;
    }

    public ProgressDialog(Shell parentShell, int timeBeforeShowDialog) {
        super();
        this.parentShell = parentShell;
        this.timeBeforeShowDialog = timeBeforeShowDialog;
    }

    public void executeProcess() throws InvocationTargetException, InterruptedException {
        Display display2 = null;
        if (parentShell != null) {
            display2 = parentShell.getDisplay();
        }
        final Display display = display2;
        final InvocationTargetException[] iteHolder = new InvocationTargetException[1];
        try {
            final IRunnableWithProgress op = new IRunnableWithProgress() {

                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    final InvocationTargetException[] iteHolder1 = new InvocationTargetException[1];
                    display.syncExec(new Runnable() {

                        public void run() {
                            try {
                                ProgressDialog.this.run(monitor);
                            } catch (InvocationTargetException e) {
                                iteHolder1[0] = e;
                            } catch (InterruptedException e) {
                                throw new OperationCanceledException(e.getMessage());
                            }
                        }

                    });
                    if (iteHolder1[0] != null) {
                        throw iteHolder1[0];
                    }
                }
            };

            display.syncExec(new Runnable() {

                public void run() {
                    final ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(parentShell);
                    if (timeBeforeShowDialog > 0) {
                        progressMonitorDialog.setOpenOnRun(false);
                        AsynchronousThreading asynchronousThreading = new AsynchronousThreading(timeBeforeShowDialog, true,
                                display, new Runnable() {

                                    public void run() {
                                        progressMonitorDialog.open();
                                    }
                                });
                        asynchronousThreading.start();
                    }

                    try {
                        progressMonitorDialog.run(false, true, op);
                    } catch (InvocationTargetException e) {
                        iteHolder[0] = e;
                    } catch (InterruptedException e) {
                        throw new OperationCanceledException(e.getMessage());
                    }
                }
            });

        } catch (OperationCanceledException e) {
            throw new InterruptedException(e.getMessage());
        }
        if (iteHolder[0] != null) {
            throw iteHolder[0];
        }
    }

    public abstract void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException;

}
