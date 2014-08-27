package br.com.anteros.helpme.actions;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.part.EditorPart;

import br.com.anteros.helpme.AnterosHelpmePlugin;

public class SelectResultClassAction  extends AbstractAction {

	public SelectResultClassAction(EditorPart editor) {
		super(editor,"&Select Result Class",SWT.NONE);
		this.setToolTipText("Select Result Class");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_SAVE_SQL));
	}

	@Override
	public boolean isAvailable() {
		return true;
	}
	
	@Override
	public void run() {
		//StringButtonDialogField selectResultClass = new StringButtonDialogField(new TypeFieldsAdapter());
		//selectResultClass.doFillIntoGrid(parent, nColumns)
	}
	
	private static class InterfaceWrapper {
		public String interfaceName;

		public InterfaceWrapper(String interfaceName) {
			this.interfaceName= interfaceName;
		}

		@Override
		public int hashCode() {
			return interfaceName.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return obj != null && getClass().equals(obj.getClass()) && ((InterfaceWrapper) obj).interfaceName.equals(interfaceName);
		}
	}
	
	
	private class TypeFieldsAdapter implements IStringButtonAdapter, IDialogFieldListener, IListAdapter, SelectionListener {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
			System.out.println("changeControlPressed "+field);
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
			System.out.println("customButtonPressed "+field+" "+index);
		}

		public void selectionChanged(ListDialogField field) {}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			System.out.println("dialogFieldChanged "+field);
		}

		public void doubleClicked(ListDialogField field) {
			System.out.println("doubleClicked "+field);
		}


		public void widgetSelected(SelectionEvent e) {
			System.out.println("widgetSelected");
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			System.out.println("widgetDefaultSelected");
		}
	}
}