package br.com.anteros.helpme.actions;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.EditorPart;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.SQLEditorView;

public class OpenSQLAction extends AbstractAction {

	public OpenSQLAction(EditorPart editor) {
		super(editor, "&Open SQL", SWT.NONE);
		this.setToolTipText("Open SQL");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_OPEN_SQL));
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public void run() {
		try {
			Shell shell = AnterosHelpmePlugin.getDefault().getShell();
			FileDialog dialog = new FileDialog(shell, SWT.OPEN);
			if (getView().getSqlFileName() != null) {
				dialog.setFileName(getView().getSqlFileName());
			}
			dialog.setFilterExtensions(new String[] { "*.sql", "*.*" });
			dialog.setFilterNames(new String[] { "Arquivo SQL (*.sql)", "Todos os arquivos (*.*)" });
			String fileName = dialog.open();

			if (fileName == null)
				return;

			File file = new File(fileName);

			if (file.exists() && file.canRead()) {
				String sqlData = IOUtils.toString(new FileInputStream(file));
				getView().getDocument().set(sqlData);
				getView().setSqlFileName(file.getName());
			}

		} catch (Exception e) {
			AnterosHelpmePlugin.error("Não foi possível Abrir o arquivo SQL.", e, true);
		}
	}

	public SQLEditorView getView() {
		return ((SQLEditorView) targetObject);
	}

}
