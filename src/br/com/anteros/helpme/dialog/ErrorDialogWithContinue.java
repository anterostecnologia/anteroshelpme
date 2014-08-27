package br.com.anteros.helpme.dialog;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class ErrorDialogWithContinue extends ErrorDialog {

    static int displayMask = IStatus.OK | IStatus.INFO | IStatus.WARNING | IStatus.ERROR;

    public ErrorDialogWithContinue(Shell parentShell, String dialogTitle, String message, IStatus status,
            int displayMask) {
        super(parentShell, dialogTitle, message, status, displayMask);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "Set parameters and continue", true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
        createDetailsButton(parent);
    }

    public static int openErrorWithContinueButton(Shell parentShell, String title, String message, IStatus status) {
        ErrorDialogWithContinue dialog = new ErrorDialogWithContinue(parentShell, title, message, status, displayMask);
        return dialog.open();
    }
}
