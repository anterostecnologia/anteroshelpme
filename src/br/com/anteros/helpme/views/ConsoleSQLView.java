package br.com.anteros.helpme.views;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.part.ViewPart;

import br.com.anteros.helpme.model.Project;
import br.com.anteros.helpme.sql.editors.SQLColorManager;
import br.com.anteros.helpme.sql.editors.SQLDocumentSetupParticipant;
import br.com.anteros.helpme.sql.editors.SQLSourceViewerConfiguration;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.actions.ClearConsoleAction;
import br.com.anteros.helpme.actions.ConsoleFormatSQLAction;
import br.com.anteros.helpme.actions.ConsoleShowSQLAction;
import br.com.anteros.helpme.util.ResourceManager;

public class ConsoleSQLView extends ViewPart {

	public static final String ID = ConsoleSQLView.class.getName();
	private IOConsole console;
	private SourceViewer sourceViewer;
	private Project project;
	private SQLColorManager colorManager = new SQLColorManager();
	private SQLDocumentSetupParticipant docSetupParticipant = new SQLDocumentSetupParticipant();
	private ClearConsoleAction clearSQLAction;
	private Composite parent;
	private ConsoleFormatSQLAction consoleFormatSQLAction;
	private ConsoleShowSQLAction consoleShowSQLAction;

	public ConsoleSQLView() {
		super();
	}

	public void createPartControl(Composite parent) {
		this.parent = parent;
		createSourceViewer(parent);
	}

	private void createSourceViewer(Composite parent) {
		parent.setLayout(new GridLayout());
		sourceViewer = new SourceViewer(parent, null, SWT.H_SCROLL | SWT.V_SCROLL);
		sourceViewer.getTextWidget().setFont(ResourceManager.getFont("Courier New", 10, SWT.NORMAL));
		sourceViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sourceViewer.setEditable(false);
		IDocument doc = new Document();
		sourceViewer.setDocument(doc);
		docSetupParticipant.setup(doc);

		makeActions();
		makeContextMenu();
	}

	private void makeContextMenu() {
		MenuManager menuMgr = new MenuManager("#ConsoleNavigatorMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				makeItemsContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(sourceViewer.getControl());
		sourceViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, sourceViewer);
	}

	protected void makeItemsContextMenu(IMenuManager manager) {
		manager.add(clearSQLAction);
	}

	private void makeActions() {
		clearSQLAction = new ClearConsoleAction(this);
		consoleFormatSQLAction = new ConsoleFormatSQLAction(this);
		consoleShowSQLAction = new ConsoleShowSQLAction(this);
		getViewSite().getActionBars().getToolBarManager().add(clearSQLAction);
		getViewSite().getActionBars().getToolBarManager().add(consoleFormatSQLAction);
		getViewSite().getActionBars().getToolBarManager().add(consoleShowSQLAction);

	}

	public void setFocus() {
		sourceViewer.getControl().setFocus();
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		if (!project.equals(this.project)) {
			this.project = project;
			sourceViewer.unconfigure();
			sourceViewer.configure(new SQLSourceViewerConfiguration(project, colorManager));
		}
	}

	public SourceViewer getSourceViewer() {
		return sourceViewer;
	}

	public void setSourceViewer(SourceViewer sourceViewer) {
		this.sourceViewer = sourceViewer;
	}

	public boolean isFormatSql() {
		return consoleFormatSQLAction.isChecked();
	}

	public boolean isShowSql() {
		return consoleShowSQLAction.isChecked();
	}

}
