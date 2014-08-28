package br.com.anteros.helpme;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PluginVersionIdentifier;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import br.com.anteros.helpme.model.ProjectRoot;
import br.com.anteros.helpme.model.persistence.SQLHistoryManager;
import br.com.anteros.helpme.model.persistence.SQLProjectManager;
import br.com.anteros.helpme.util.ResourceManager;
import br.com.anteros.helpme.views.ConsoleSQLView;

public class AnterosHelpmePlugin extends AbstractUIPlugin {

	private static Logger logger = LoggerProvider.getInstance().getLogger(
			AnterosHelpmePlugin.class.getName());

	public static final String PLUGIN_ID = "br.com.anteros.helpme.AnterosHelpme";
	public static final String TITLE = "Anteros Help-me";
	public static final String CONSOLE_NAME = "Anteros Console - Error";

	public static final String IMG_ANTEROS_16_16 = "anteros-16x16.png";
	public static final String IMG_REVERSE_ENGINEER = "reverseEngineer.png";
	public static final String IMG_GENERATE_SCRIPT = "generateScript.png";
	public static final String IMG_PREFERENCES = "preferences.png";
	public static final String IMG_EXPAND_ALL = "expandAll.gif";
	public static final String IMG_COLLAPSE_ALL = "collapseAll.gif";
	public static final String IMG_SQL_EDITOR = "editorSql.png";
	public static final String IMG_OPEN_SQL = "open.png";
	public static final String IMG_OPEN_CLASS_DIAGRAM = "diagram_class.png";
	public static final String IMG_SAVE_SQL = "save.png";
	public static final String IMG_EXECUTE_SQL = "execute.gif";
	public static final String IMG_EXECUTE_SCRIPT_SQL = "script.gif";
	public static final String IMG_CLEAR_SQL = "clear.png";
	public static final String IMG_PREVIOUS_SQL = "previous.png";
	public static final String IMG_NEXT_SQL = "next.png";
	public static final String IMG_REFRESH = "update.gif";
	public static final String IMG_PROJECTS = "projects.png";
	public static final String IMG_PROJECT = "project.png";
	public static final String IMG_PROJECT_ADD = "projectAdd.png";
	public static final String IMG_PROJECT_REMOVE = "projectRemove.png";
	public static final String IMG_PROJECT_OPEN = "projectOpen.png";
	public static final String IMG_ENTITIES = "entities.png";
	public static final String IMG_CLASS = "class.gif";
	public static final String IMG_DATE = "date.png";
	public static final String IMG_TIME = "timestamp.png";
	public static final String IMG_TABLES = "tables.gif";
	public static final String IMG_TABLE = "table.gif";
	public static final String IMG_CURSOR_SQL = "cursorSQL.png";
	public static final String IMG_CURSOR_HOUR_GLASS = "cursorHourGlass.png";
	public static final String IMG_COLLECTION = "collection.gif";
	public static final String IMG_COLLECTION_MAP = "collectionMap.gif";
	public static final String IMG_ENTITY_LIST = "collectionEntityList.gif";
	public static final String IMG_ENTITY_SET = "collectionEntitySet.gif";
	public static final String IMG_COLLECTION_LIST = "collectionList.gif";
	public static final String IMG_COMPONENT_ID = "componentId.gif";
	public static final String IMG_COMPONENT = "component.gif";
	public static final String IMG_ENUM = "enum.gif";
	public static final String IMG_VERSION = "version.gif";
	public static final String IMG_NUMBER = "number.png";
	public static final String IMG_STRING = "string.gif";
	public static final String IMG_PK_COLUMN = "primaryKeyColumn.gif";
	public static final String IMG_FK_COLUMN = "foreignKeyColumn.gif";
	public static final String IMG_PK_FK_COLUMN = "primaryForeignKeyColumn.gif";
	public static final String IMG_COLUMN = "column.gif";
	public static final String IMG_COLUMNS = "columns.png";
	public static final String IMG_CONSTRAINTS = "constraint.png";
	public static final String IMG_INDEXES = "index.png";
	public static final String IMG_INDEX = "index.png";
	public static final String IMG_CONSTRAINT = "constraint.png";
	public static final String IMG_PROCEDURE = "procedure.png";
	public static final String IMG_FUNCTION = "function.png";
	public static final String IMG_FUNCTION_NATIVE = "functionNative.png";
	public static final String IMG_PARAMETER = "parameter.gif";
	public static final String IMG_GENERATOR = "generator.png";
	public static final String IMG_VALIDATOR = "validator.gif";
	public static final String IMG_ONE_TO_ONE = "oneToOne.gif";
	public static final String IMG_ONE_TO_MANY = "oneToMany.gif";
	public static final String IMG_MANY_TO_MANY = "ManyToMany.gif";
	public static final String IMG_VIEW = "view.png";
	public static final String IMG_TEMPLATE = "template.png";
	public static final String IMG_CODE_UNKNOWN = "unknown.gif";
	public static final String IMG_LEFT = "left.png";
	public static final String IMG_RIGHT = "right.png";
	public static final String IMG_OVAL_BLUE = "oval_blue.gif";
	public static final String IMG_OVAL_RED = "oval_red.gif";
	public static final String IMG_CLEAR = "clear.gif";
	public static final String IMG_FORMAT_SQL = "formatSql.png";
	public static final String IMG_SHOW_SQL = "showSql.png";
	public static final String IMG_ORACLE = "oracle.gif";
	public static final String IMG_DB2 = "db2.gif";
	public static final String IMG_SQLITE = "sqlite.gif";
	public static final String IMG_MSSQL = "mssql.gif";
	public static final String IMG_FIREBIRD = "firebird.gif";
	public static final String IMG_MYSQL = "mysql.gif";
	public static final String IMG_POSTGRESQL = "postgresql.gif";
	public static final String IMG_PACKAGE = "package_obj.gif";
	public static final String IMG_INTERFACE = "interface.gif";
	public static final String IMG_REFERENCE = "reference.gif";
	public static final String IMG_INHERITANCE = "inheritance.gif";
	public static final String IMG_ATTRIBUTE = "attribute.gif";
	public static final String IMG_OPERATION = "operation.gif";
	public static final String IMG_FIELD_PUBLIC = "field_public_obj.gif";
	public static final String IMG_METHOD_PUBLIC = "methpub_obj.gif";
	public static final String IMG_STICKY_NOTE = "sticky_note.gif";
	public static final String IMG_CONNECTION = "connection16.gif";
	public static final String IMG_TWO_WAY_REFERENCE = "twoReferences.gif";
	public static final String IMG_DATATYPE = "dataType.gif";
	public static final String IMG_ENUM_LITERAL = "enumLiteral.gif";
	public static final String IMG_SNAP = "geometry.gif";
	public static final String IMG_SNAP_DISABLED = "geom_dis.gif";
	public static final String IMG_NEW_DIAGRAM_WIZARD = "class_diagram_wiz.gif";

	private static AnterosHelpmePlugin plugin;

	private ProjectRoot projectRoot = new ProjectRoot();

	private ResourceBundle resourceBundle;
	private SQLHistoryManager sqlHistoryManager;
	private SQLProjectManager sqlProjectManager;

	public AnterosHelpmePlugin() {
		try {
			resourceBundle = ResourceBundle
					.getBundle("br.com.anteros.helpme.AnterosHelpmePluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		loadSettings();

	}

	private void loadSettings() {
		getSQLProjectManager().load();
		getSQLHistoryManager().load();
	}

	public void stop(BundleContext context) throws Exception {
		saveSettings();
		plugin = null;
		super.stop(context);
	}

	private void saveSettings() {
		getSQLProjectManager().save();
		getSQLHistoryManager().save();

	}

	public static AnterosHelpmePlugin getDefault() {
		return plugin;
	}

	private static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

	public static final String getPluginVersion() {
		String pluginId = getPluginId();
		IPluginRegistry registry = Platform.getPluginRegistry();
		IPluginDescriptor descriptor = registry.getPluginDescriptor(pluginId);
		PluginVersionIdentifier v = descriptor.getVersionIdentifier();
		int major = v.getMajorComponent();
		int minor = v.getMinorComponent();
		int service = v.getServiceComponent();
		String date = v.getQualifierComponent();
		return major + "." + minor + "." + service + " release " + date;
	}

	public IWorkbenchPage getPage() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return window.getActivePage();
	}

	public IWorkbenchWindow getWindow() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		return workbench.getActiveWorkbenchWindow();
	}

	public IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}

	public Shell getShell() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return window.getShell();
	}

	public Display getDisplay() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return window.getShell().getDisplay();
	}

	public static IViewPart findView(String id) {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView(id);
	}

	public static void showView(String id) throws Exception {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.showView(id);
	}

	public static void getCloseEditors(IEditorReference[] editorRefs) {
		getDefault().getPage().closeEditors(editorRefs, true);
	}

	public static void showInformationMessage(String message) {
		MessageDialog.openInformation(AnterosHelpmePlugin.getDefault()
				.getShell(), TITLE, message);
	}

	public static void showWarningMessage(String message) {
		MessageDialog.openWarning(AnterosHelpmePlugin.getDefault().getShell(),
				TITLE, message);
	}

	public static boolean confirmDialog(String message) {
		return MessageDialog.openConfirm(AnterosHelpmePlugin.getDefault()
				.getShell(), TITLE, message);
	}

	public static MessageDialogWithToggle confirmDialogWithToggle(
			String message, String toggleMessage, boolean toggleStatus) {
		return MessageDialogWithToggle.openYesNoQuestion(AnterosHelpmePlugin
				.getDefault().getShell(), TITLE, message, toggleMessage,
				toggleStatus, null, null);
	}

	public void openEditor(IEditorInput editorInput, String editorID,
			boolean activate) throws Exception {
		IDE.openEditor(getPage(), editorInput, editorID, activate);
	}

	public boolean isActivePerspective(String perspective) {
		boolean isFound = false;

		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IPerspectiveDescriptor perspectiveDescriptor = page.getPerspective();

		if (perspectiveDescriptor != null
				&& perspectiveDescriptor.getId().equals(perspective))
			isFound = true;

		return isFound;

	}

	public Image getImage(String fileName) {
		try {
			IPath path = new Path("icons/" + fileName);
			URL url = find(path);
			if (url != null) {
				return ResourceManager.getImage(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ImageDescriptor getImageDescriptor(String fileName) {
		try {
			IPath path = new Path("icons/" + fileName);
			URL url = find(path);
			if (url != null) {
				return ResourceManager.getImageDescriptor(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public SQLProjectManager getSQLProjectManager() {
		if (sqlProjectManager == null)
			sqlProjectManager = new SQLProjectManager(
					projectRoot.getProjectManager());

		return sqlProjectManager;
	}

	public SQLHistoryManager getSQLHistoryManager() {
		if (sqlHistoryManager == null)
			sqlHistoryManager = new SQLHistoryManager();

		return sqlHistoryManager;
	}

	public ProjectRoot getProjectRoot() {
		return projectRoot;
	}

	public void setSQLCursor() {
		getShell().setCursor(
				new Cursor(getShell().getDisplay(), getImage(IMG_CURSOR_SQL)
						.getImageData(), 0, 0));
	}

	public void setHourGlassCursor() {
		getShell().setCursor(
				new Cursor(getShell().getDisplay(), getImage(
						IMG_CURSOR_HOUR_GLASS).getImageData(), 0, 0));
	}

	public void setDefaultCursor() {
		getShell().setCursor(
				new Cursor(AnterosHelpmePlugin.getDefault().getShell()
						.getDisplay(), SWT.CURSOR_ARROW));
	}

	public void setWaitCursor() {
		getShell().setCursor(
				new Cursor(AnterosHelpmePlugin.getDefault().getShell()
						.getDisplay(), SWT.CURSOR_WAIT));
	}

	public static String getResourceString(String key) {
		ResourceBundle bundle = AnterosHelpmePlugin.getDefault()
				.getResourceBundle();
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public static void error(String message, Throwable t, boolean showError) {
		if (getDefault() != null) {
			getDefault().getLog().log(
					new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, String
							.valueOf(message), t));
			logger.error(message, t);

			try {
				AnterosHelpmePlugin.getDefault().getPage()
						.showView("org.eclipse.pde.runtime.LogView");
			} catch (Exception e) {
			}

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			errorToConsole(t);

			if (showError) {
				showError(message);
			}
		}
	}

	public static void error(String message, boolean showError) {
		getDefault().getLog().log(
				new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, String
						.valueOf(message), null));
		logger.error(message);

		try {
			AnterosHelpmePlugin.getDefault().getPage()
					.showView("org.eclipse.pde.runtime.LogView");
		} catch (Exception e) {
		}

		if (showError)
			MessageDialog.openError(
					AnterosHelpmePlugin.getDefault().getShell(), "Atenção",
					message);
	}

	public static void error(Throwable e, boolean showError) {
		error(e.getMessage(), e, showError);
	}

	public static void showError(String message) {
		MessageDialog.openError(AnterosHelpmePlugin.getDefault().getShell(),
				"Atenção", message);
	}

	public static void errorToConsole(Throwable t) {
		MessageConsole console = AnterosHelpmePlugin.findConsole(CONSOLE_NAME);
		console.activate();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		MessageConsoleStream stream = console.newMessageStream();
		stream.println(sw.toString());
	}

	public static MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		MessageConsole result = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { result });
		return result;
	}

	public ConsoleSQLView getDefaultConsoleView() throws Exception {
		AnterosHelpmePlugin.showView(ConsoleSQLView.ID);
		return (ConsoleSQLView) AnterosHelpmePlugin.findView(ConsoleSQLView.ID);
	}

	public ImageDescriptor getItemImageDescriptor(Object item) {
		return null;
	}

	public static ImageDescriptor getBundledImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public Image getBundledImage(String path) {
		Image image = getImageRegistry().get(path);
		if (image == null) {
			getImageRegistry().put(path, getBundledImageDescriptor(path));
			image = getImageRegistry().get(path);
		}
		return image;
	}

}
