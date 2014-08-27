package br.com.anteros.helpme.views;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.actions.AbstractViewAction;
import br.com.anteros.helpme.actions.CollapseAllAction;
import br.com.anteros.helpme.actions.EditConfigurationXMLAction;
import br.com.anteros.helpme.actions.ExpandAllAction;
import br.com.anteros.helpme.actions.GenerateScriptAction;
import br.com.anteros.helpme.actions.NewProjectAction;
import br.com.anteros.helpme.actions.OpenSQLEditorAction;
import br.com.anteros.helpme.actions.PreferencesAction;
import br.com.anteros.helpme.actions.RefreshProjectAction;
import br.com.anteros.helpme.actions.RemoveProjectAction;
import br.com.anteros.helpme.actions.ReverseEngineerAction;
import br.com.anteros.helpme.model.Component;
import br.com.anteros.helpme.model.Configuration;
import br.com.anteros.helpme.model.ProjectManager;
import br.com.anteros.helpme.treeviewer.ProjectTreeViewer;

public class ProjectNavigatorView extends ViewPart {

	public static final String ID = ProjectNavigatorView.class.getName();

	private ProjectTreeViewer viewer;

	private NewProjectAction newProjectAction;

	private GenerateScriptAction generateScriptAction;

	private ReverseEngineerAction reverseEngineerAction;

	private ExpandAllAction expandAllAction;

	private CollapseAllAction collapseAllAction;

	private OpenSQLEditorAction openEditorAction;

	private PreferencesAction preferencesAction;

	private RefreshProjectAction refreshProjectAction;

	private RemoveProjectAction removeProjectAction;
	
	public ProjectNavigatorView() {
	}

	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.FILL);
		container.setLayout(new FillLayout());
		final Tree tree = new Tree(container, SWT.H_SCROLL | SWT.V_SCROLL);
		viewer = new ProjectTreeViewer(tree);
		viewer.setInput(AnterosHelpmePlugin.getDefault().getProjectRoot());

		final EditConfigurationXMLAction action = new EditConfigurationXMLAction(this);
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Object firstElement = ((TreeSelection) event.getSelection()).getFirstElement();
				if (firstElement instanceof Configuration)
					action.run();
				else if (firstElement instanceof Component) {
					/*
					 * VER DEFEITO COLATERAL DO DOUBLE CLICK if (((Component)
					 * firstElement).getDescriptionField().getTargetEntity() !=
					 * null) { IObjectNode[] entities = ((Component)
					 * firstElement).getParent().getParent().getChildren(); for
					 * (IObjectNode node : entities) { if
					 * (node.getSimpleName().equals( (((Component)
					 * firstElement).getDescriptionField().getTargetEntity()
					 * .getEntityClass().getName()))) viewer.setSelection(new
					 * StructuredSelection(node)); } }
					 */
				}
			}
		});

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				refreshActions();
			}
		});
		
		viewer.getTree().addListener(SWT.Expand, new Listener() {
			@Override
			public void handleEvent(Event event) {
				refreshActions();
			}
		});

		try {
			viewer.refresh();
		} catch (Exception e) {
			AnterosHelpmePlugin.error("Ocorreu um erro criando o Gerenciador de Projetos.", e, true);
		}
		makeActions();
		makeContextMenu();

		viewer.getTree().setFocus();
	}

	private void makeContextMenu() {
		MenuManager menuMgr = new MenuManager("#ProjectNavigatorMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				makeItemsContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);

	}

	protected void makeItemsContextMenu(IMenuManager manager) {
		Object obj = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();

		if (obj instanceof ProjectManager) {
			manager.add(newProjectAction);
			manager.add(removeProjectAction);
			manager.add(openEditorAction);
			manager.add(new Separator());
			manager.add(generateScriptAction);
			manager.add(reverseEngineerAction);
			manager.add(new Separator());
			manager.add(preferencesAction);
			manager.add(expandAllAction);
			manager.add(collapseAllAction);
		} else if (obj instanceof Configuration) {
			manager.add(new EditConfigurationXMLAction(this));
		}

	}

	private void makeActions() {
		newProjectAction = new NewProjectAction(this);
		removeProjectAction = new RemoveProjectAction(this);
		removeProjectAction.setEnabled(false);
		openEditorAction = new OpenSQLEditorAction(this);
		generateScriptAction = new GenerateScriptAction(this);
		generateScriptAction.setEnabled(false);
		reverseEngineerAction = new ReverseEngineerAction(this);
		reverseEngineerAction.setEnabled(false);
		expandAllAction = new ExpandAllAction(this);
		collapseAllAction = new CollapseAllAction(this);
		preferencesAction = new PreferencesAction(this);
		refreshProjectAction = new RefreshProjectAction(this);
		refreshProjectAction.setEnabled(false);
		getViewSite().getActionBars().getToolBarManager().add(newProjectAction);
		getViewSite().getActionBars().getToolBarManager().add(removeProjectAction);
		getViewSite().getActionBars().getToolBarManager().add(refreshProjectAction);
		getViewSite().getActionBars().getToolBarManager().add(openEditorAction);
		getViewSite().getActionBars().getToolBarManager().add(new Separator());
		getViewSite().getActionBars().getToolBarManager().add(generateScriptAction);
		getViewSite().getActionBars().getToolBarManager().add(reverseEngineerAction);
		getViewSite().getActionBars().getToolBarManager().add(new Separator());
		getViewSite().getActionBars().getToolBarManager().add(preferencesAction);
		getViewSite().getActionBars().getToolBarManager().add(expandAllAction);
		getViewSite().getActionBars().getToolBarManager().add(collapseAllAction);
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void refreshActions() {
		IToolBarManager toolbar = getViewSite().getActionBars().getToolBarManager();
		IContributionItem[] items = toolbar.getItems();
		for (IContributionItem item : items) {
			if (item instanceof ActionContributionItem) {
				ActionContributionItem contrib = (ActionContributionItem) item;
				IAction contribAction = contrib.getAction();
				if (contribAction instanceof AbstractViewAction) {
					AbstractViewAction action = (AbstractViewAction) contribAction;
					action.setEnabled(action.isAvailable());
				}
			}
		}
	}

	public ProjectTreeViewer getViewer() {
		return viewer;
	}

	public ProjectManager getProjects() {
		return AnterosHelpmePlugin.getDefault().getSQLProjectManager().getProjectManager();
	}

}
