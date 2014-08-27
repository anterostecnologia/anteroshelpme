package br.com.anteros.helpme.dialog;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;

public class ErrorDialogWithDetailAreaAndContinueButton {

    private int codeOfButton;

    public ErrorDialogWithDetailAreaAndContinueButton(Shell shell, String pid, String mainMessage, String detailMessage) {
        MultiStatus info = new MultiStatus(pid, 1, mainMessage, null);
        if (detailMessage != null) {
            String[] lines = detailMessage.split("\n"); 
            for (int i = 0; i < lines.length; i++) {
                info
                        .add(new Status(IStatus.INFO, pid, 1, lines[i].replaceAll("\t", "    ").replaceAll("\r", ""), 
                                null));
            }
        }
        codeOfButton = ErrorDialogWithContinue.openErrorWithContinueButton(shell, "Error Message", null, info); 
    }

    public int getCodeOfButton() {
        return this.codeOfButton;
    }
}
