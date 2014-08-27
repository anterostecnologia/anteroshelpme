package br.com.anteros.helpme.sql.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.TextOperationAction;

import br.com.anteros.helpme.model.Project;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.views.SQLEditorView;

public class SQLTextEditor extends TextEditor {

	private SQLEditorView view;
	private Project selectedProject;
	private SQLSourceViewerConfiguration sourceViewerConfiguration;

	public SQLTextEditor(SQLEditorView view) {
		super();
		this.view = view;
		this.selectedProject = view.getSelectedProject();
		sourceViewerConfiguration = new SQLSourceViewerConfiguration(selectedProject, new SQLColorManager());
		setSourceViewerConfiguration(sourceViewerConfiguration);
		setAction("ContentAssistProposal", new TextOperationAction(AnterosHelpmePlugin.getDefault().getResourceBundle(),
				"ContentAssistProposal.", this, ISourceViewer.CONTENTASSIST_PROPOSALS));
		setAction("ContentAssistTip", new TextOperationAction(AnterosHelpmePlugin.getDefault().getResourceBundle(),
				"ContentAssistTip.", this, ISourceViewer.CONTENTASSIST_CONTEXT_INFORMATION));
	}

	public SQLEditorView getView() {
		return view;
	}

	public void setView(SQLEditorView view) {
		this.view = view;
	}

	public String getText() {
		String text = getSourceViewer().getTextWidget().getText();
		return text;
	}
	
	public void clear() {
		getSourceViewer().getTextWidget().setText("");
	}

	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
		sourceViewerConfiguration.setProject(selectedProject);		
	}
	

	public void doSave(IProgressMonitor monitor)  {
        super.doSave(monitor);
	}

	public void doSaveAs()  {
        super.doSaveAs();
	}

	public void gotoMarker(IMarker marker)  {
		super.getAdapter(IGotoMarker.class);
	}

	public boolean isDirty()  {
		if (getEditorInput() instanceof FileEditorInput) {
			return super.isDirty();
		}
		return false;
	}

	public boolean isSaveAsAllowed()  {
		return true;
	}
	
	
	
}
