package br.com.anteros.helpme.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.debug.ui.actions.ActionMessages;
import org.eclipse.jdt.internal.debug.ui.actions.ProjectSelectionDialog;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.model.Classpath;
import br.com.anteros.helpme.model.ClasspathExternalJar;
import br.com.anteros.helpme.model.ClasspathJar;
import br.com.anteros.helpme.model.ClasspathProject;
import br.com.anteros.helpme.model.ClasspathRoot;
import br.com.anteros.helpme.treeviewer.ClasspathTreeViewer;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.util.PluginUtils;

public class ConfigurationPageWizard extends WizardPage {
	private Text fldJavaProject;
	private Text fldConfigurationFile;
	private Text fldProjectTitle;
	private CTabFolder tabFolder;
	private ClasspathTreeViewer classpathViewer;
	private Button btnAddProjects;
	private Button btnRemove;
	private Button btnMoveDown;
	private Button btnMoveUp;
	private Button btnAddJars;
	private Button btnAddExternalJars;

	public ConfigurationPageWizard() {
		super("wizardPage");
		setTitle("Configuration Project");
		setDescription("");
		setImageDescriptor(AnterosHelpmePlugin.getDefault().getImageDescriptor("projectWizard.png"));
	}

	public void createControl(Composite parent) {
		createMainTab(parent);
		createClasspathTab(parent);
	}

	private void createMainTab(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		tabFolder = new CTabFolder(container, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem tabMain = new CTabItem(tabFolder, SWT.NONE);
		tabMain.setText("Main");
		tabMain.setImage(AnterosHelpmePlugin.getDefault().getImage("sql.png"));

		Composite compositeMain = new Composite(tabFolder, SWT.NONE);
		tabMain.setControl(compositeMain);

		Group grpProject = new Group(compositeMain, SWT.NONE);
		grpProject.setText("Project:");
		grpProject.setBounds(10, 77, 556, 64);

		fldJavaProject = new Text(grpProject, SWT.BORDER);
		fldJavaProject.setBounds(10, 24, 462, 19);
		fldJavaProject.setEditable(false);

		Button btnProject = new Button(grpProject, SWT.NONE);
		btnProject.setBounds(478, 24, 68, 23);
		btnProject.setText("Browse...");
		btnProject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browseProject();
			}
		});

		Group grpConfigurationFile = new Group(compositeMain, SWT.NONE);
		grpConfigurationFile.setText("Configuration file:");
		grpConfigurationFile.setBounds(10, 147, 556, 64);

		fldConfigurationFile = new Text(grpConfigurationFile, SWT.BORDER);
		fldConfigurationFile.setBounds(10, 24, 462, 19);
		fldConfigurationFile.setEditable(false);

		Button btnConfigurationFile = new Button(grpConfigurationFile, SWT.NONE);
		btnConfigurationFile.setText("Browse...");
		btnConfigurationFile.setBounds(478, 24, 68, 23);
		btnConfigurationFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browseConfigurationFile();
			}
		});

		Group grpTitle = new Group(compositeMain, SWT.NONE);
		grpTitle.setText("Title:");
		grpTitle.setBounds(10, 10, 556, 64);

		fldProjectTitle = new Text(grpTitle, SWT.BORDER);
		fldProjectTitle.setBounds(10, 24, 462, 19);
	}

	private void createClasspathTab(Composite parent) {
		CTabItem tabClassPath = new CTabItem(tabFolder, SWT.NONE);
		tabClassPath.setText("Classpath");
		tabClassPath.setImage(AnterosHelpmePlugin.getDefault().getImage("order.gif"));

		Composite compositeClassPath = new Composite(tabFolder, SWT.NONE);
		tabClassPath.setControl(compositeClassPath);

		setControl(compositeClassPath);
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 2;
		compositeClassPath.setLayout(topLayout);
		GridData gd;

		Label label = new Label(compositeClassPath, SWT.NONE);
		label.setText("Classpath");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);

		final Tree tree = new Tree(compositeClassPath, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

		classpathViewer = new ClasspathTreeViewer(tree);

		Composite pathButtonComp = new Composite(compositeClassPath, SWT.NONE);
		GridLayout pathButtonLayout = new GridLayout();
		pathButtonLayout.marginHeight = 0;
		pathButtonLayout.marginWidth = 0;
		pathButtonComp.setLayout(pathButtonLayout);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.HORIZONTAL_ALIGN_FILL);
		pathButtonComp.setLayoutData(gd);
		createClassPathButtons(pathButtonComp);
	}

	protected void createClassPathButtons(Composite pathButtonComp) {
		btnMoveUp = SWTFactory.createPushButton(pathButtonComp, "Move U&p", null);
		btnMoveUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					moveUpClasspath();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnMoveDown = SWTFactory.createPushButton(pathButtonComp, "Move &Down", null);
		btnMoveDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					moveDownClasspath();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRemove = SWTFactory.createPushButton(pathButtonComp, "Re&move", null);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeClasspath();
			}
		});
		btnAddProjects = SWTFactory.createPushButton(pathButtonComp, "Add Project&s...", null);
		btnAddProjects.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addProjectsClassPath();
			}
		});

		btnAddJars = SWTFactory.createPushButton(pathButtonComp, "Add &JARs...", null);
		btnAddJars.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addJarsClasspath();
			}
		});
		btnAddExternalJars = SWTFactory.createPushButton(pathButtonComp, "Add E&xternal JARs...", null);
		btnAddExternalJars.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addExternalJarsClassPath();
			}
		});

		enableActions();

	}

	private void enableActions() {
		btnMoveUp.setEnabled(classpathViewer.getTree().getItems().length > 1);
		btnMoveDown.setEnabled(classpathViewer.getTree().getItems().length > 1);
		btnRemove.setEnabled(classpathViewer.getTree().getItems().length > 0);
	}

	protected void browseProject() {
		try {
			IJavaProject[] projects;
			try {
				projects = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()).getJavaProjects();
			} catch (JavaModelException ex) {
				projects = new IJavaProject[0];
			}

			ILabelProvider labelProvider = new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
			ElementListSelectionDialog dialog = new ElementListSelectionDialog(AnterosHelpmePlugin.getDefault().getShell(),
					labelProvider);
			dialog.setTitle("Selecione um projeto Java");
			dialog.setMessage("Projeto Java");
			dialog.setElements(projects);

			IJavaProject javaProject = null;
			if (javaProject != null) {
				dialog.setInitialSelections(new Object[] { javaProject });
			}
			if (dialog.open() == Window.OK) {
				javaProject = (IJavaProject) dialog.getFirstResult();
			}
			if (javaProject != null) {
				fldJavaProject.setText(javaProject.getProject().getName());
			} else {
				fldJavaProject.setText("");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	protected void browseConfigurationFile() {
		try {
			IPath initialPath = new Path(fldJavaProject.getText());
			IPath[] paths = PluginUtils.chooseFileEntries(AnterosHelpmePlugin.getDefault().getShell(), initialPath,
					new IPath[0], "Selecione um Arquivo", "Arquivo de configuração XML", new String[] { "xml" }, false,
					false, true);
			if (paths != null && paths.length == 1) {
				fldConfigurationFile.setText(paths[0].toOSString());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	protected void moveUpClasspath() throws Exception {
		List<IObjectNode> targets = ((IStructuredSelection) classpathViewer.getSelection()).toList();
		if (targets.isEmpty()) {
			return;
		}
		List<IObjectNode> list = Arrays.asList(((ClasspathRoot) classpathViewer.getInput()).getChildren());
		int top = 0;
		int index = 0;
		Iterator<IObjectNode> entries = targets.iterator();
		while (entries.hasNext()) {
			IObjectNode target = entries.next();
			index = list.indexOf(target);
			if (index > top) {
				top = index - 1;
				IObjectNode temp = list.get(top);
				list.set(top, target);
				list.set(index, temp);
			}
			if (index == top) {
				top++;
			} else {
				top = index;
			}
		}
		((ClasspathRoot) classpathViewer.getInput()).removeAll();
		((ClasspathRoot) classpathViewer.getInput()).addAllNode(list.toArray(new IObjectNode[] {}));
		classpathViewer.refresh();
		classpathViewer.getTree().setFocus();
		enableActions();
	}

	protected void moveDownClasspath() throws Exception {
		List<IObjectNode> targets = ((IStructuredSelection) classpathViewer.getSelection()).toList();
		if (targets.isEmpty()) {
			return;
		}
		List<IObjectNode> list = Arrays.asList(((ClasspathRoot) classpathViewer.getInput()).getChildren());
		int bottom = list.size() - 1;
		int index = 0;
		for (int i = targets.size() - 1; i >= 0; i--) {
			IObjectNode target = targets.get(i);
			index = list.indexOf(target);
			if (index < bottom) {
				bottom = index + 1;
				IObjectNode temp = list.get(bottom);
				list.set(bottom, target);
				list.set(index, temp);
			}
			if (bottom == index) {
				bottom--;
			} else {
				bottom = index;
			}
		}
		((ClasspathRoot) classpathViewer.getInput()).removeAll();
		((ClasspathRoot) classpathViewer.getInput()).addAllNode(list.toArray(new IObjectNode[] {}));
		classpathViewer.refresh();
		classpathViewer.getTree().setFocus();
		enableActions();
	}

	protected void removeClasspath() {
		IStructuredSelection selection = (IStructuredSelection) classpathViewer.getSelection();
		Object[] list = selection.toArray();
		for (Object obj : list) {
			((IObjectNode) obj).getParent().remove((IObjectNode) obj);
		}
		classpathViewer.refresh();
		enableActions();
	}

	protected void addProjectsClassPath() {
		List<IJavaProject> projects;
		try {
			IObjectNode[] children = ((ClasspathRoot) classpathViewer.getInput()).getChildren();
			IJavaProject[] prj = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()).getJavaProjects();
			List<IJavaProject> lstProjects = new ArrayList<IJavaProject>();
			boolean found;
			for (IJavaProject p : prj) {
				String name = p.getProject().getName();
				found = false;
				for (IObjectNode node : children) {
					String n = node.getSimpleName();
					if (n.equals(name)) {
						found = true;
						break;
					}
				}
				if (!found)
					lstProjects.add(p);
			}
			projects = lstProjects;

		} catch (JavaModelException ex) {
			projects = new ArrayList<IJavaProject>();
		}
		ProjectSelectionDialog dialog = new ProjectSelectionDialog(getShell(), projects);
		dialog.setTitle(ActionMessages.AddProjectAction_Project_Selection_2);
		if (dialog.open() == Window.OK) {
			Object[] result = dialog.getResult();
			try {
				for (Object obj : result) {
					ClasspathProject node = new ClasspathProject();
					node.setClasspath(((IJavaProject) obj).getProject().getName());
					((ClasspathRoot) classpathViewer.getInput()).addNode(node);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			classpathViewer.refresh();
			enableActions();
		}
		enableActions();
	}

	protected void addJarsClasspath() {
		IObjectNode[] children = ((ClasspathRoot) classpathViewer.getInput()).getChildren();
		List<IPath> selectedPaths = new ArrayList<IPath>();
		for (IObjectNode node : children) {
			if (node instanceof Classpath)
				selectedPaths.add(Path.fromOSString(((Classpath) node).getClasspath()));
		}

		IPath[] paths = PluginUtils.chooseFileEntries(AnterosHelpmePlugin.getDefault().getShell(), null,
				selectedPaths.toArray(new IPath[] {}), "JAR Selection",
				"Selecione os arquivos para serem adicionados no ClassPath", new String[] { "jar" }, true, false, true);
		if (paths != null) {
			try {
				ClasspathJar node;
				for (int i = 0; i < paths.length; i++) {
					node = new ClasspathJar();
					node.setClasspath(paths[i].toOSString());
					((ClasspathRoot) classpathViewer.getInput()).addNode(node);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			classpathViewer.refresh();
			enableActions();
		}
	}

	protected void addExternalJarsClassPath() {
		FileDialog dialog = new FileDialog(getShell(), SWT.MULTI);
		dialog.setText(ActionMessages.AddExternalJar_Jar_Selection_3);
		dialog.setFilterExtensions(new String[] { "*.jar;*.zip", "*.*" });
		String res = dialog.open();
		if (res == null) {
			return;
		}
		String[] fileNames = dialog.getFileNames();
		int nChosen = fileNames.length;

		IPath filterPath = new Path(dialog.getFilterPath());
		IPath path = null;
		try {
			ClasspathExternalJar node;
			for (int i = 0; i < nChosen; i++) {
				path = filterPath.append(fileNames[i]).makeAbsolute();
				node = new ClasspathExternalJar();
				node.setClasspath(path.toOSString());
				((ClasspathRoot) classpathViewer.getInput()).addNode(node);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		classpathViewer.refresh();
		enableActions();
	}

	public boolean isValid() {
		if (fldProjectTitle.getText() == "") {
			setErrorMessage("Informe o t�tulo do projeto.");
			return false;
		}
		if (fldJavaProject.getText() == "") {
			setErrorMessage("Selecione um projeto java.");
			return false;
		}
		if (fldConfigurationFile.getText() == "") {
			setErrorMessage("Selecione o arquivo de configura��o no projeto java.");
			return false;
		}
		return true;
	}

	public Text getFldJavaProject() {
		return fldJavaProject;
	}

	public Text getFldConfigurationFile() {
		return fldConfigurationFile;
	}

	public Text getFldProjectTitle() {
		return fldProjectTitle;
	}

	public List<Classpath> getClasspath() {
		List<Classpath> result = new ArrayList<Classpath>();

		for (IObjectNode node : ((ClasspathRoot) classpathViewer.getInput()).getChildren()) {
			result.add((Classpath) node);
		}
		return result;
	}

}
