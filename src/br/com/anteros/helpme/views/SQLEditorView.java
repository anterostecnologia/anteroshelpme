package br.com.anteros.helpme.views;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import br.com.anteros.core.utils.ReflectionUtils;
import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.actions.ClearSQLAction;
import br.com.anteros.helpme.actions.ExecuteSQLAction;
import br.com.anteros.helpme.actions.ExecuteScriptSQLAction;
import br.com.anteros.helpme.actions.NextHistorySQLAction;
import br.com.anteros.helpme.actions.OpenSQLAction;
import br.com.anteros.helpme.actions.PreviousHistorySQLAction;
import br.com.anteros.helpme.actions.SaveSQLAction;
import br.com.anteros.helpme.actions.SelectResultClassAction;
import br.com.anteros.helpme.dataset.DataSet;
import br.com.anteros.helpme.dataset.DataSetTable;
import br.com.anteros.helpme.model.Entity;
import br.com.anteros.helpme.model.Project;
import br.com.anteros.helpme.model.VariableObject;
import br.com.anteros.helpme.model.VariableRoot;
import br.com.anteros.helpme.sql.editors.SQLTextEditor;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.ObjectTreeViewer;
import br.com.anteros.helpme.treeviewer.PartAdapter2;
import br.com.anteros.persistence.metadata.EntityCache;
import br.com.anteros.persistence.metadata.identifier.Identifier;
import br.com.anteros.persistence.parameter.NamedParameter;
import br.com.anteros.persistence.session.SQLSession;
import br.com.anteros.persistence.session.SQLSessionListener;
import br.com.anteros.persistence.session.SQLSessionResult;

public class SQLEditorView extends EditorPart implements SQLSessionListener {

	private static final Color SASH_COLOR = new Color(null, 153, 186, 243);

	public static final String[] SUPPORTED_FILETYPES = new String[] { "*.sql", "*.txt", "*.*" };

	public static String ID = SQLEditorView.class.getName();

	private SQLTextEditor editorSQL;
	private CoolBar coolBar;
	private CoolBarManager coolBarManager;
	private ToolBarManager toolBarManager;
	private ImageCombo selectProject;
	private ImageCombo selectResultClass;
	private Label lblProject;
	private Label lblNewLabel;
	private CTabFolder pageControl;
	private CTabItem tabObjectView;
	private ObjectTreeViewer resultObjectTreeviewer;
	private VariableRoot variableRoot;
	private DataSetTable gridResult;

	private boolean isDirty;
	private boolean isUntitled;
	private boolean isScratchFile;

	private CTabFolder pageControlResultView;

	private CTabItem tabResult;

	private String sqlFileName;

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		editorSQL = new SQLTextEditor(this);
		editorSQL.init(site, input);
		getSite().getPage().addPartListener(new PartAdapter2() {
			public void partClosed(IWorkbenchPartReference partRef) {
				if (partRef.getPart(false) == SQLEditorView.this) {
					onCloseEditor();
				}
			}
		});
	}

	private void onCloseEditor() {
		editorSQL.getDocumentProvider().disconnect(getEditorInput());
		editorSQL.setInput(null);
		clearResults();
	}

	public void clearResults() {
		if (pageControl.isDisposed())
			return;
		synchronized (this) {
			CTabItem[] tabItems = pageControl.getItems();
			for (int i = 0; i < tabItems.length; i++) {

			}
		}
	}

	@Override
	public boolean isDirty() {
		boolean saveOnClose = true;
		return saveOnClose && !isScratchFile && (isDirty || editorSQL.isDirty());
	}

	@Override
	public boolean isSaveAsAllowed() {
		return editorSQL.isSaveAsAllowed();
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());

		Composite containerTop = new Composite(container, SWT.NONE);
		containerTop.setLayout(null);
		FormData fd_containerTop = new FormData();
		fd_containerTop.bottom = new FormAttachment(0, 25);
		fd_containerTop.top = new FormAttachment(0);
		fd_containerTop.left = new FormAttachment(0);
		fd_containerTop.right = new FormAttachment(100);
		containerTop.setLayoutData(fd_containerTop);

		SashForm sashForm = new SashForm(container, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		FormData fd_sashForm = new FormData();
		fd_sashForm.bottom = new FormAttachment(100);
		fd_sashForm.right = new FormAttachment(100);
		fd_sashForm.top = new FormAttachment(containerTop);
		fd_sashForm.left = new FormAttachment(container, 0, SWT.LEFT);
		sashForm.setLayoutData(fd_sashForm);

		createToolBar(containerTop);
		createEditor(sashForm);
		createPageControl(sashForm);
	}

	private void createToolBar(Composite container) {
		coolBar = new CoolBar(container, SWT.FLAT);
		coolBar.setBounds(0, 0, 200, 25);

		coolBarManager = new CoolBarManager(coolBar);
		toolBarManager = new ToolBarManager(SWT.FLAT);

		createActions();

		coolBarManager.add(new ToolBarContributionItem(toolBarManager, this.getClass().getName()));
		coolBarManager.update(true);

		lblProject = new Label(container, SWT.NONE);
		lblProject.setBounds(240, 2, 34, 13);
		lblProject.setText("Project");

		selectProject = new ImageCombo(container, SWT.BORDER);
		selectProject.setBounds(280, 2, 153, 20);
		selectProject.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				updateSelectResultClass();
				editorSQL.setSelectedProject(getSelectedProject());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		initializeSelectProjects();

		lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setBounds(438, 2, 58, 13);
		lblNewLabel.setText("Result Class");

		selectResultClass = new ImageCombo(container, SWT.BORDER);
		selectResultClass.setBounds(500, 2, 239, 20);
	}

	private void createPageControl(Composite container) {
		pageControl = new CTabFolder(container, SWT.BORDER);
		pageControl.setSelectionBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		tabObjectView = new CTabItem(pageControl, SWT.NONE);
		tabObjectView.setText("Object View");

		final Tree tree = createTabObjectViewContent();
		tabObjectView.setControl(tree);

		createTabResultViewContent();
		pageControl.setSelection(0);
	}

	private void createTabResultViewContent() {
		CTabItem tabResultView = new CTabItem(pageControl, SWT.NONE);
		tabResultView.setText("Result View");

		pageControlResultView = new CTabFolder(pageControl, SWT.BORDER);
		tabResultView.setControl(pageControlResultView);
		pageControlResultView.setSelectionBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		tabResult = new CTabItem(pageControlResultView, SWT.NONE);
		tabResult.setText("Result");

		pageControlResultView.setSelection(tabResult);
	}

	private Tree createTabObjectViewContent() {
		final Tree tree = new Tree(pageControl, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		final SQLEditorView v = this;
		tree.addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {
				event.detail &= ~SWT.HOT;
				if ((event.detail & SWT.SELECTED) == 0) {
					if (event.item.getData() instanceof VariableObject) {
						VariableObject variableObject = (VariableObject) event.item.getData();
						if (variableObject.getEntityCache() != null) {
							if (ReflectionUtils.isExtendsClass(v.getSelectedResultClass(), variableObject
									.getEntityCache().getEntityClass())) {
								GC gc = event.gc;
								Rectangle area = tree.getClientArea();
								int columnCount = tree.getColumnCount();
								if (event.index == columnCount - 1 || columnCount == 0) {
									int width = area.x + area.width - event.x;
									if (width > 0) {
										Region region = new Region();
										gc.getClipping(region);
										region.add(event.x, event.y, width, event.height);
										gc.setClipping(region);
										region.dispose();
									}
								}
								gc.setAdvanced(true);
								if (gc.getAdvanced())
									gc.setAlpha(127);
								Rectangle rect = event.getBounds();
								Color foreground = gc.getForeground();
								Color background = gc.getBackground();
								gc.setForeground(AnterosHelpmePlugin.getDefault().getDisplay()
										.getSystemColor(SWT.COLOR_GRAY));
								gc.fillGradientRectangle(0, rect.y, 5000, rect.height, false);
								gc.setForeground(foreground);
								gc.setBackground(background);
								event.detail &= ~SWT.SELECTED;
							}
						}
					}
				}
			}
		});

		tree.setLinesVisible(true);
		resultObjectTreeviewer = new ObjectTreeViewer(tree);
		variableRoot = new VariableRoot();
		resultObjectTreeviewer.setInput(variableRoot);
		return tree;
	}

	private void createEditor(Composite container) {
		final Composite editorParent = new Composite(container, SWT.NONE);
		editorParent.setLayout(new FillLayout());
		editorSQL.createPartControl(editorParent);
		editorSQL.addPropertyListener(new IPropertyListener() {
			public void propertyChanged(Object source, int propertyId) {
				SQLEditorView.this.firePropertyChange(propertyId);
			}
		});

	}

	private void createActions() {
		toolBarManager.add(new OpenSQLAction(this));
		toolBarManager.add(new SaveSQLAction(this));
		toolBarManager.add(new Separator());
		toolBarManager.add(new ExecuteSQLAction(this));
		toolBarManager.add(new ExecuteScriptSQLAction(this));
		toolBarManager.add(new Separator());
		toolBarManager.add(new ClearSQLAction(this));
		toolBarManager.add(new Separator());
		toolBarManager.add(new PreviousHistorySQLAction(this));
		toolBarManager.add(new NextHistorySQLAction(this));
		toolBarManager.add(new Separator());
		toolBarManager.add(new SelectResultClassAction(this));
	}

	@Override
	public void setFocus() {
		editorSQL.setFocus();
	}

	public Project getSelectedProject() {
		if (selectProject != null) {
			if (selectProject.getSelectionIndex() != -1) {
				return (Project) selectProject.getData(selectProject.getItem(selectProject.getSelectionIndex())
						.getText());
			}
		}
		return null;
	}

	public Class<?> getSelectedResultClass() {
		if (selectResultClass.getSelectionIndex() != -1) {
			Entity entity = (Entity) selectResultClass.getData(selectResultClass.getItem(
					selectResultClass.getSelectionIndex()).getText());
			return entity.getEntityCache().getEntityClass();
		}
		return null;
	}

	public String getText() {
		return editorSQL.getText();
	}

	public void setResultData(SQLSessionResult<?> result) {
		variableRoot.removeAll();
		Project project = getSelectedProject();
		EntityCache cache = project.getSession().getEntityCacheManager()
				.getEntityCache(getSelectedResultClass());
		if ((result != null) && ((result.getResultList() != null))) {
			VariableObject vo;
			try {
				int i = 1;
				for (Object obj : result.getResultList()) {
					Identifier<?> id = project.getSession().getIdentifier(obj);
					vo = new VariableObject(obj, project.getSession().getEntityCacheManager()
							.getEntityCache(obj.getClass()), project.getSession(), true);
					vo.setName(new String[] { obj.getClass().getSimpleName() + " " + i, id + "" });
					variableRoot.addNode(vo);
					i++;
				}
			} catch (Exception e) {
				AnterosHelpmePlugin
						.error("Não foi possível processar os objetos recebidos na execução do SQL.", e, true);
			}
		}
		resultObjectTreeviewer.refresh();

		try {
			DataSet dataSet = new DataSet(result.getResultSet());
			gridResult = new DataSetTable(pageControlResultView, dataSet, "Teste", false);
			tabResult.setControl(gridResult.getControl());
		} catch (Exception e) {
			AnterosHelpmePlugin.error("Não foi possível processar o ResultSet recebido na execução do SQL.", e, true);
		} finally {
			try {
				result.getResultSet().close();
			} catch (Exception e) {
			}
		}
	}

	private void initializeSelectProjects() {
		selectProject.removeAll();
		for (IObjectNode project : AnterosHelpmePlugin.getDefault().getSQLProjectManager().getProjectManager()
				.getChildren()) {
			selectProject.add(project.getSimpleName(),
					AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_PROJECT));
			selectProject.setData(project.getSimpleName(), project);
		}
		updateSelectResultClass();
	}

	private void updateSelectResultClass() {
		if (selectResultClass != null) {
			selectResultClass.removeAll();
			Project project = getSelectedProject();
			if ((project != null) && (project.isInitialized())) {
				for (IObjectNode node : project.getEntities().getChildren()) {
					selectResultClass.add(node.getSimpleName(),
							AnterosHelpmePlugin.getDefault().getImage(AnterosHelpmePlugin.IMG_CLASS));
					selectResultClass.setData(node.getSimpleName(), node);
				}
			}
		}
	}

	@Override
	public void dispose() {
		IObjectNode[] projects = AnterosHelpmePlugin.getDefault().getSQLProjectManager().getProjectManager()
				.getChildren();
		for (IObjectNode project : projects) {
			if (((Project) project).isInitialized())
				((Project) project).getSession().removeListener(this);
		}
		super.dispose();
	}

	@Override
	public void onExecuteSQL(String sql, NamedParameter[] parameters) {
		try {
			StyledText text = AnterosHelpmePlugin.getDefault().getDefaultConsoleView().getSourceViewer().getTextWidget();
			text.append("Sql-> " + sql);
			if ((parameters != null) && (parameters.length > 0)) {
				text.append("\nParameters -> ");
				for (NamedParameter p : parameters) {
					text.append("\n");
					text.append(p.toString());
				}
			}
			text.append("\n\n");
		} catch (Exception e) {
		}
	}

	@Override
	public void onExecuteSQL(String sql, Map<String, Object> parameters) {

	}

	@Override
	public void onExecuteSQL(String sql, Object[] parameters) {

	}

	@Override
	public void onExecuteUpdateSQL(String sql, NamedParameter[] parameters) {

	}

	@Override
	public void onExecuteUpdateSQL(String sql, Map<String, Object> parameters) {

	}

	@Override
	public void onExecuteUpdateSQL(String sql, Object[] parameters) {

	}

	public void clear() {
		editorSQL.clear();
	}

	public Document getDocument() {
		return (Document) editorSQL.getDocumentProvider().getDocument(getEditorInput());
	}

	public String getSqlFileName() {
		return sqlFileName;
	}

	public void setSqlFileName(String sqlFileName) {
		this.sqlFileName = sqlFileName;
	}

	@Override
	public void onClose(SQLSession session) {
		
	}

}
