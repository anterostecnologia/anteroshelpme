package br.com.anteros.helpme.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.EditorPart;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.SQLEditorView;

public class SaveSQLAction extends AbstractAction {

	public SaveSQLAction(EditorPart editor) {
		super(editor, "&Save SQL", SWT.NONE);
		this.setToolTipText("save SQL");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_SAVE_SQL));
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public void run() {
		try {
			Shell shell = AnterosHelpmePlugin.getDefault().getShell();
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);

			if (getView().getSqlFileName() != null) {
				dialog.setFileName(getView().getSqlFileName());
			}

			dialog.setFilterExtensions(new String[] { "*.sql", "*.*" });
			dialog.setFilterNames(new String[] { "Arquivo SQL (*.sql)", "Todos os arquivos (*.*)" });
			String fileName = dialog.open();

			if (fileName == null)
				return;

			File file = new File(fileName);
			if (file.exists()) {
				MessageBox msg = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
				msg.setMessage(fileName + "existe.  Sobrescrever ?"); 
				msg.setText("Confirma��o"); 
				int res2 = msg.open();
				if (res2 == SWT.NO)
					return;
			}

			write(fileName, getView().getDocument().get());

			getView().setSqlFileName(file.getName());

		} catch (Exception e) {
			AnterosHelpmePlugin.error("N�o foi poss�vel Salvar o arquivo SQL.", e, true);
		}
	}
	

	public SQLEditorView getView() {
		return ((SQLEditorView) targetObject);
	}
	
	public void write(String fileName, String saveData) throws Exception {
		PrintStream pout = null;
		try {
			pout = new PrintStream(new FileOutputStream(fileName), true);
			pout.print(saveData);
		} finally {
			if (pout != null)
				pout.close();
		}
	}
}