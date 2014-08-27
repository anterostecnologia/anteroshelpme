package br.com.anteros.helpme.actions;

import org.eclipse.ui.part.ViewPart;

import br.com.anteros.helpme.AnterosHelpmePlugin;

public class ReverseEngineerAction extends AbstractViewAction {

	public ReverseEngineerAction(ViewPart view) {
		super(view);
		this.setText("&Reverse Engineer");
		this.setToolTipText("Reverse Engineer");
		this.setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor(AnterosHelpmePlugin.IMG_REVERSE_ENGINEER));
	}

	@Override
	public boolean isAvailable() {
		return false;
	}
}
