package br.com.anteros.helpme.util;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.ICoreConstants;
import org.eclipse.core.internal.resources.ResourceInfo;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.ui.viewsupport.IViewPartInputProvider;
import org.eclipse.jdt.internal.ui.wizards.TypedElementSelectionValidator;
import org.eclipse.jdt.internal.ui.wizards.buildpaths.FolderSelectionDialog;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.navigator.ResourceSorter;
import org.eclipse.ui.wizards.IWizardDescriptor;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.persistence.sql.parser.INode;
import br.com.anteros.persistence.sql.parser.node.AliasNode;
import br.com.anteros.persistence.sql.parser.node.FromNode;

@SuppressWarnings("restriction")
public class PluginUtils {

	public static final String META_INF_PERSISTENCE_XML = "META-INF/persistence.xml";
	public static final String META_INF_ORM_XML = "META-INF/orm.xml";

	public static IJavaProject chooseJavaProject(Shell shell, IJavaProject initialSelection, String title,
			String description) throws Exception {
		IJavaProject[] projects;
		projects = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()).getJavaProjects();

		ILabelProvider labelProvider = new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, labelProvider);
		dialog.setTitle(title);
		dialog.setMessage(description);
		dialog.setElements(projects);

		IJavaProject javaProject = initialSelection;
		if (javaProject != null) {
			dialog.setInitialSelections(new Object[] { javaProject });
		}
		if (dialog.open() == Window.OK) {
			return (IJavaProject) dialog.getFirstResult();
		}
		return initialSelection;
	}

	static public String chooseImplementation(String supertype, String initialSelection, String title, Shell shell) {
		SelectionDialog dialog = null;
		try {
			final IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
			dialog = JavaUI.createTypeDialog(shell, PlatformUI.getWorkbench().getProgressService(), scope,
					IJavaElementSearchConstants.CONSIDER_CLASSES, false, supertype);
		} catch (JavaModelException jme) {
			return null;
		}

		dialog.setTitle(title);
		dialog.setMessage(title);

		if (dialog.open() == IDialogConstants.CANCEL_ID)
			return null;

		Object[] types = dialog.getResult();
		if (types != null && types.length > 0) {
			IType type = (IType) types[0];
			return type.getFullyQualifiedName('.');
		}
		return null;
	}

	public static IPath[] chooseFileEntries(Shell shell, IPath initialSelection, IPath[] usedEntries, String title,
			String description, String[] fileExtensions, boolean allowMultiple, boolean allowDirectories,
			boolean allowFiles) {
		if (usedEntries == null) {
			throw new IllegalArgumentException("UsedEntries must be not null");
		}

		List<Class<?>> clazzes = new ArrayList<Class<?>>();
		if (allowDirectories) {
			clazzes.add(IFolder.class);
			clazzes.add(IProject.class);
		}
		if (allowFiles) {
			clazzes.add(IFile.class);
		}
		Class<?>[] acceptedClasses = clazzes.toArray(new Class[clazzes.size()]);

		TypedElementSelectionValidator validator = new TypedElementSelectionValidator(acceptedClasses, true);
		List<IResource> usedFiles = new ArrayList<IResource>(usedEntries.length);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (int i = 0; i < usedEntries.length; i++) {
			IResource resource = root.findMember(usedEntries[i]);
			if (resource instanceof IFile) {
				usedFiles.add(resource);
			}
		}
		IResource focus = initialSelection != null ? root.findMember(initialSelection) : null;

		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(),
				new WorkbenchContentProvider());
		dialog.setValidator(validator);
		dialog.setAllowMultiple(allowMultiple);
		dialog.setTitle(title);
		dialog.setMessage(description);
		dialog.addFilter(new FileFilter(fileExtensions, usedFiles, true, allowDirectories));
		dialog.setInput(root);
		dialog.setSorter(new ResourceSorter(ResourceSorter.NAME));
		dialog.setInitialSelection(focus);

		if (dialog.open() == Window.OK) {
			Object[] elements = dialog.getResult();
			IPath[] res = new IPath[elements.length];
			for (int i = 0; i < res.length; i++) {
				IResource elem = (IResource) elements[i];
				res[i] = elem.getFullPath();
			}
			return res;
		}
		return null;
	}

	public static IPath[] chooseFolderEntries(Shell shell, IPath initialSelection, String title, String description,
			boolean allowMultiple) {
		List<Class<?>> clazzes = new ArrayList<Class<?>>();
		clazzes.add(IFolder.class);
		clazzes.add(IProject.class);

		Class<?>[] acceptedClasses = clazzes.toArray(new Class[clazzes.size()]);

		TypedElementSelectionValidator validator = new TypedElementSelectionValidator(acceptedClasses, true);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource focus = initialSelection != null ? root.findMember(initialSelection) : null;

		ElementTreeSelectionDialog dialog = null;
		dialog = new FolderSelectionDialog(shell, new WorkbenchLabelProvider(), new WorkbenchContentProvider());

		dialog.setValidator(validator);
		dialog.setAllowMultiple(allowMultiple);
		dialog.setTitle(title);
		dialog.setMessage(description);
		dialog.setInput(root);
		dialog.setSorter(new ResourceSorter(ResourceSorter.NAME));
		dialog.setInitialSelection(focus);

		if (dialog.open() == Window.OK) {
			Object[] elements = dialog.getResult();
			IPath[] res = new IPath[elements.length];
			for (int i = 0; i < res.length; i++) {
				IResource elem = (IResource) elements[i];
				res[i] = elem.getFullPath();
			}
			return res;
		}
		return null;
	}

	static public IJavaProject findJavaProject(IEditorPart part) {
		if (part != null)
			return findJavaProject(part.getEditorInput());
		return null;
	}

	static public IJavaProject findJavaProject(IEditorInput input) {
		if (input != null && input instanceof IFileEditorInput) {
			IFile file = null;
			IProject project = null;
			IJavaProject jProject = null;

			IFileEditorInput fileInput = (IFileEditorInput) input;
			file = fileInput.getFile();
			project = file.getProject();
			jProject = JavaCore.create(project);

			return jProject;
		}

		return null;
	}

	public static IProject findProject(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(name);
		return project;
	}

	public static IJavaProject findJavaProject(String name) {
		IProject project = findProject(name);
		if (project != null) {
			return JavaCore.create(project);
		}
		return null;
	}

	public static boolean exists(IFile f) {
		if (!(f instanceof File)) {
			return false;
		}
		File file = (File) f;
		ResourceInfo info = file.getResourceInfo(false, false);
		int flags = file.getFlags(info);
		if (flags != ICoreConstants.NULL_FLAG) {
			int type = ResourceInfo.getType(flags);
			if (type == IResource.FILE || type == IResource.FOLDER || type == IResource.PROJECT) {
				return true;
			}
		}
		return false;
	}

	public static boolean delete(java.io.File path) {
		boolean res = true, tmp = true;
		if (path.exists()) {
			java.io.File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					tmp = delete(files[i]);
				} else {
					tmp = deleteFile(files[i]);
				}
				res = res && tmp;
			}
		}
		tmp = deleteFile(path);
		res = res && tmp;
		return res;
	}

	public static boolean deleteFile(java.io.File file) {
		boolean res = false;
		if (file.exists()) {
			if (file.delete()) {
				res = true;
			}
		}
		return res;
	}

	static public org.eclipse.jdt.core.dom.CompilationUnit getCompilationUnit(ICompilationUnit source, boolean bindings) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(source);
		parser.setResolveBindings(bindings);
		org.eclipse.jdt.core.dom.CompilationUnit result = (org.eclipse.jdt.core.dom.CompilationUnit) parser
				.createAST(null);
		return result;
	}

	static public ICompilationUnit findCompilationUnit(IJavaProject javaProject, String fullyQualifiedName) {
		IType lwType = findType(javaProject, fullyQualifiedName);
		if (lwType != null) {
			return lwType.getCompilationUnit();
		}
		return null;
	}

	static public IType findType(IJavaProject javaProject, String fullyQualifiedName) {
		IType lwType = null;
		try {
			lwType = javaProject.findType(fullyQualifiedName);
		} catch (JavaModelException e) {
		}
		return lwType;
	}

	@SuppressWarnings("unchecked")
	static public String getParentTypename(IJavaProject proj, String fullyQualifiedName) {
		String res = null;
		ICompilationUnit icu = findCompilationUnit(proj, fullyQualifiedName);
		if (icu == null) {
			return res;
		}
		org.eclipse.jdt.core.dom.CompilationUnit cu = getCompilationUnit(icu, true);
		if (cu == null) {
			return res;
		}
		List types = cu.types();
		for (int i = 0; i < types.size() && res == null; i++) {
			Object obj = types.get(i);
			if (!(obj instanceof TypeDeclaration)) {
				continue;
			}
			TypeDeclaration td = (TypeDeclaration) obj;
			Type superType = td.getSuperclassType();
			if (superType != null) {
				ITypeBinding tb = superType.resolveBinding();
				if (tb != null) {
					if (tb.getJavaElement() instanceof SourceType) {
						SourceType sourceT = (SourceType) tb.getJavaElement();
						try {
							res = sourceT.getFullyQualifiedParameterizedName();
						} catch (JavaModelException e) {
						}
					}
				}
			}
		}
		return res;
	}

	public static IJavaModel getJavaModel() {
		return JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
	}

	public static URL[] getRawLocationsURLForResources(IPath[] classpaths) throws Exception {
		URL[] l = new URL[classpaths.length];
		for (int i = 0; i < classpaths.length; i++) {
			l[i] = getRawLocationURL(classpaths[i]);
		}
		return l;
	}

	static public IPath getProjectLocation(IProject project) {
		if (project.getRawLocation() == null) {
			return project.getLocation();
		} else
			return project.getRawLocation();
	}

	private static java.io.File getRawLocationFile(IPath simplePath) throws Exception {
		IResource member = ResourcesPlugin.getWorkspace().getRoot().findMember(simplePath);
		java.io.File file = null;
		if (member != null) {
			IPath rawLocation = member.getRawLocation();
			if (rawLocation == null) {
				rawLocation = member.getLocation();
				if (rawLocation == null) {
					throw new Exception("N�o foi poss�vel determinar a localiza��o " + simplePath);
				}
			}
			file = rawLocation.toFile();
		} else {
			file = simplePath.toFile();
		}
		return file;
	}

	static public URLClassLoader getProjectClassLoader(IJavaProject project) throws Exception {
		Set<URL> pathElements = getProjectClassPathURLs(project);
		URL urlPaths[] = pathElements.toArray(new URL[pathElements.size()]);

		for (URL url : urlPaths) {
			System.out.println(url);
		}

		return new URLClassLoader(urlPaths, Thread.currentThread().getContextClassLoader());
	}

	public static Set<URL> getProjectClassPathURLs(IJavaProject javaProject) {
		Set<URL> urls = new LinkedHashSet<URL>();
		IProject project = javaProject.getProject();
		IWorkspaceRoot workspaceRoot = project.getWorkspace().getRoot();

		IClasspathEntry[] paths = null;
		IPath defaultOutputLocation = null;
		try {
			paths = javaProject.getResolvedClasspath(true);
			defaultOutputLocation = javaProject.getOutputLocation();
		} catch (JavaModelException e) {
		}
		if (paths != null) {
			IPath projectPath = javaProject.getProject().getLocation();
			for (int i = 0; i < paths.length; ++i) {
				IClasspathEntry cpEntry = paths[i];
				IPath p = null;
				if (cpEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					p = cpEntry.getOutputLocation();
					if (p == null) {
						p = defaultOutputLocation;
					}
				} else if (cpEntry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
					String projName = cpEntry.getPath().toPortableString().substring(1);
					IProject proj = workspaceRoot.getProject(projName);
					IJavaProject projj = JavaCore.create(proj);
					urls.addAll(getProjectClassPathURLs(projj));
					continue;
				} else {
					p = cpEntry.getPath();
				}

				if (p == null) {
					continue;
				}
				if (!p.toFile().exists()) {
					p = projectPath.append(p.removeFirstSegments(1));
					if (!p.toFile().exists()) {
						continue;
					}
				}
				try {
					urls.add(p.toFile().toURL());
				} catch (MalformedURLException e) {
				}
			}
		}
		return urls;
	}

	private static URL getRawLocationURL(IPath simplePath) throws Exception {
		java.io.File file = getRawLocationFile(simplePath);
		return file.toURL();
	}

	static public IType getClassFromElement(IJavaElement element) {
		IType classToTest = null;
		if (element != null) {
			IType typeInCompUnit = (IType) element.getAncestor(IJavaElement.TYPE);
			if (typeInCompUnit != null) {
				if (typeInCompUnit.getCompilationUnit() != null) {
					classToTest = typeInCompUnit;
				}
			} else {
				ICompilationUnit cu = (ICompilationUnit) element.getAncestor(IJavaElement.COMPILATION_UNIT);
				if (cu != null)
					classToTest = cu.findPrimaryType();
				else {
					if (element instanceof IClassFile) {
						try {
							IClassFile cf = (IClassFile) element;
							if (cf.isStructureKnown())
								classToTest = cf.getType();
						} catch (JavaModelException e) {
						}
					}
				}
			}
		}
		return classToTest;
	}

	static public IJavaElement getInitialJavaElement(ISelection simpleSelection) {
		IJavaElement jelem = null;
		if (simpleSelection != null && !simpleSelection.isEmpty() && simpleSelection instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) simpleSelection;
			Object selectedElement = selection.getFirstElement();
			if (selectedElement instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) selectedElement;

				jelem = (IJavaElement) adaptable.getAdapter(IJavaElement.class);
				if (jelem == null) {
					IResource resource = (IResource) adaptable.getAdapter(IResource.class);
					if (resource != null && resource.getType() != IResource.ROOT) {
						while (jelem == null && resource.getType() != IResource.PROJECT) {
							resource = resource.getParent();
							jelem = (IJavaElement) resource.getAdapter(IJavaElement.class);
						}
						if (jelem == null) {
							jelem = JavaCore.create(resource);
						}
					}
				}
			}
		}
		if (jelem == null) {
			IWorkbenchPart part = getActivePage().getActivePart();
			if (part instanceof ContentOutline) {
				part = getActivePage().getActiveEditor();
			}

			if (part instanceof IViewPartInputProvider) {
				Object elem = ((IViewPartInputProvider) part).getViewPartInput();
				if (elem instanceof IJavaElement) {
					jelem = (IJavaElement) elem;
				}
			}
		}
		return jelem;
	}

	public static IWorkbenchPage getActivePage() {
		IWorkbenchWindow window = getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return null;
		return getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}

	static public IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}

	public static void openWizard(String id) throws Exception {
		IWizardDescriptor descriptor = PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(id);
		if (descriptor == null) {
			descriptor = PlatformUI.getWorkbench().getImportWizardRegistry().findWizard(id);
		}
		if (descriptor == null) {
			descriptor = PlatformUI.getWorkbench().getExportWizardRegistry().findWizard(id);
		}
		try {
			if (descriptor != null) {
				IWizard wizard = descriptor.createWizard();
				WizardDialog wd = new WizardDialog(AnterosHelpmePlugin.getDefault().getShell(), wizard);
				wd.setTitle(wizard.getWindowTitle());
				wd.open();
			}
		} catch (CoreException e) {
			throw new Exception("N�o foi poss�vel abrir o wizard. "+e.getMessage());
		}
	}

	public static ElementListSelectionDialog createAllClassesDialog(Shell shell, IJavaProject[] originals,
			final boolean includeDefaultPackage) throws JavaModelException {
		final List<IType> types = new ArrayList<IType>();

		if (originals == null) {
			IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
			IJavaModel model = JavaCore.create(wsroot);
			originals = model.getJavaProjects();
		}
		final IJavaProject[] projects = originals;
		for (IJavaProject proj : projects) {
			for (IPackageFragmentRoot pckgRoot : proj.getAllPackageFragmentRoots()) {
				for (IJavaElement pckg : pckgRoot.getChildren()) {
					if (pckg instanceof IPackageFragment) {
						for (ICompilationUnit clazz : ((IPackageFragment) pckg).getCompilationUnits()) {
							for (IType type : clazz.getAllTypes()) {
								String name = type.getFullyQualifiedName();
								types.add(type);
							}
						}
					}
				}
			}
		}

		int flags = JavaElementLabelProvider.SHOW_DEFAULT;
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, new JavaElementLabelProvider(flags));
		dialog.setTitle("Type Selection");
		dialog.setIgnoreCase(false);
		dialog.setElements(types.toArray());
		return dialog;
	}

	public static SelectionDialog createClassDialog(Shell shell, String title, String message)
			throws JavaModelException {
		SelectionDialog ret;
		ret = JavaUI.createTypeDialog(shell, null, SearchEngine.createJavaSearchScope(getClassesInWorkspace()),
				org.eclipse.jdt.ui.IJavaElementSearchConstants.CONSIDER_CLASSES, false);
		ret.setTitle(title);
		ret.setMessage(message);
		return ret;
	}

	private static IJavaElement[] getClassesInWorkspace() throws JavaModelException {
		List<IJavaElement> classes = new ArrayList<IJavaElement>();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IJavaModel model = JavaCore.create(root);
		IJavaProject[] projects = model.getJavaProjects();
		for (IJavaProject proj : projects) {
			for (IPackageFragmentRoot pckgRoot : proj.getAllPackageFragmentRoots()) {
				for (IJavaElement pckg : pckgRoot.getChildren()) {
					if (pckg instanceof IPackageFragment) {
						for (ICompilationUnit clazz : ((IPackageFragment) pckg).getCompilationUnits()) {
							for (IType type : clazz.getAllTypes()) {
								classes.add(type);
							}
						}
					}
				}
			}
		}

		return classes.toArray(new IJavaElement[0]);
	}

	public static ArrayList<IPath> getSourceClasspaths(IJavaProject javaProject) {
		ArrayList<IPath> sourceList = new ArrayList<IPath>();
		IClasspathEntry[] classpaths = javaProject.readRawClasspath();
		if (classpaths != null) {
			for (IClasspathEntry e : classpaths) {
				if (e.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					sourceList.add(e.getPath());
				}
			}
		}
		return sourceList;
	}

	public final static IMarker addMarker(IResource file, String markerId, String message, int lineNumber, int severity) throws Exception {
		try {
			IMarker marker = file.createMarker(markerId);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			if (lineNumber < 1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
			file.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
			return marker;
		} catch (CoreException e) {
			throw new Exception("Ocorreu um erro adicionando Marker."+e.getMessage());
		}
	}

	public final static IMarker addMarker(IResource resource, String markerId, String message, int severity) throws Exception {
		try {
			IMarker marker = resource.createMarker(markerId);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);

			resource.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());

			return marker;
		} catch (CoreException e) {
			throw new Exception("Ocorreu um erro adicionando Marker."+e.getMessage());
		}

	}

	public static IJavaProject[] getAndroidProjects() {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IJavaModel javaModel = JavaCore.create(workspaceRoot);

		return getAndroidProjects(javaModel);
	}

	public static IJavaProject[] getAndroidProjects(IJavaModel javaModel) {
		IJavaProject[] javaProjectList = null;
		try {
			javaProjectList = javaModel.getJavaProjects();
		} catch (JavaModelException jme) {
			return new IJavaProject[0];
		}

		ArrayList<IJavaProject> androidProjectList = new ArrayList<IJavaProject>();

		for (IJavaProject javaProject : javaProjectList) {
			IProject project = javaProject.getProject();
			try {
				if (project.hasNature("com.android.ide.eclipse.adt.AndroidNature")) {
					androidProjectList.add(javaProject);
				}
			} catch (CoreException e) {
			}
		}
		return androidProjectList.toArray(new IJavaProject[androidProjectList.size()]);
	}

	public final static IFolder getOutputFolder(IProject project) {
		try {
			if (project.isOpen() && project.hasNature(JavaCore.NATURE_ID)) {
				IJavaProject javaProject = JavaCore.create(project);
				IPath path = javaProject.getOutputLocation();
				IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
				IResource outputResource = wsRoot.findMember(path);
				if (outputResource != null && outputResource.getType() == IResource.FOLDER) {
					return (IFolder) outputResource;
				}
			}
		} catch (JavaModelException e) {
		} catch (CoreException e) {
		}
		return null;
	}

	public static IJavaElement selectJavaElement(IJavaElement[] elements, Shell shell, String title, String message) {
		int nResults = elements.length;
		if (nResults == 0)
			return null;
		if (nResults == 1)
			return elements[0];
		int flags = JavaElementLabelProvider.SHOW_DEFAULT | JavaElementLabelProvider.SHOW_QUALIFIED
				| JavaElementLabelProvider.SHOW_ROOT;

		ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, new JavaElementLabelProvider(flags));
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setElements(elements);

		if (dialog.open() == Window.OK) {
			Object[] selection = dialog.getResult();
			if (selection != null && selection.length > 0) {
				nResults = selection.length;
				for (int i = 0; i < nResults; i++) {
					Object current = selection[i];
					if (current instanceof IJavaElement)
						return (IJavaElement) current;
				}
			}
		}
		return null;
	}

	private List<IPackageFragment> listPackages(Shell shell, final IJavaProject jproject,
			final boolean includeDefaultPackage) throws JavaModelException {
		final List<IPackageFragment> packageList = new ArrayList<IPackageFragment>();
		final JavaModelException[] exception = new JavaModelException[1];
		final boolean[] monitorCanceled = new boolean[] { false };
		IRunnableWithProgress r = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) {
				try {
					Set<String> packageNameSet = new HashSet<String>();
					monitor.beginTask("Listing packages in project", 1);
					IPackageFragmentRoot[] roots = jproject.getPackageFragmentRoots();
					List<IPackageFragment> pkgs = new ArrayList<IPackageFragment>();
					for (IPackageFragmentRoot root : roots) {
						if (!root.isArchive()) {
							try {
								IJavaElement[] rootFragments = root.getChildren();
								for (int j = 0; j < rootFragments.length; j++) {
									pkgs.add((IPackageFragment) rootFragments[j]);
								}
							} catch (JavaModelException e) {
							}
						}
					}
					for (IPackageFragment pkg : pkgs) {
						if (monitor.isCanceled()) {
							monitorCanceled[0] = true;
							return;
						}
						if (!pkg.hasChildren() && (pkg.getNonJavaResources().length > 0)) {
							continue;
						}
						String pkgName = pkg.getElementName();
						if (!includeDefaultPackage && pkgName.length() == 0) {
							continue;
						}
						if (packageNameSet.add(pkgName)) {
							packageList.add(pkg);
						}
					}
					monitor.worked(1);
					monitor.done();
				} catch (JavaModelException jme) {
					exception[0] = jme;
				}
			}
		};
		try {
			PlatformUI.getWorkbench().getProgressService().busyCursorWhile(r);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (exception[0] != null) {
			throw exception[0];
		}
		if (monitorCanceled[0]) {
			return null;
		}

		return packageList;
	}

	public static String getNameFromFileName(String fileName) {
		Filename f = new Filename(fileName, System.getProperty("file.separator").charAt(0), '.');
		return f.filename();
	}

	public static String getPathFromFileName(String fileName) {
		Filename f = new Filename(fileName, System.getProperty("file.separator").charAt(0), '.');
		return f.path();
	}

	public static boolean isAfterPoint(IDocument document, int documentOffset) {
		try {
			char c = document.getChar(documentOffset - 1);
			if (c == '.') {
				return true;
			} else {
				return false;
			}
		} catch (BadLocationException e) {
			return false;
		}
	}

	public static String getPreviousWord(IDocument document, int documentOffset) {

		if (isAfterPoint(document, documentOffset)) {
			--documentOffset;
		}
		StringBuffer buf = new StringBuffer();
		while (true) {
			try {
				char c = document.getChar(--documentOffset);
				if (isWhitespace(c) || c == '.')
					return buf.reverse().toString();
				buf.append(c);

			} catch (BadLocationException e) {
				return buf.reverse().toString();
			}
		}
	}

	public static String getPreviousWordGroup(IDocument document, int documentOffset) {
		if (isAfterPoint(document, documentOffset)) {
			--documentOffset;
		}

		StringBuffer buf = new StringBuffer();
		while (true) {
			try {
				char c = document.getChar(--documentOffset);
				if (isWhitespace(c))
					return buf.reverse().toString();

				buf.append(c);

			} catch (BadLocationException e) {
				return buf.reverse().toString();
			}
		}
	}

	public static String subString(String modifier, int length) {
		if (modifier == null)
			return null;
		if (modifier.length() <= length) {
			return modifier;
		} else {
			return modifier.substring(0, length);
		}
	}

	public static boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '(' || c == ')' || c == ',');
	}

	public static INode findParent(INode node, String type) {
		if (node == null || type.equals(node.getNodeClassName())) {
			return node;
		} else {
			return findParent((INode) node.getParent(), type);
		}

	}

	public static INode findFirstChild(INode node, String type) {
		INode[] nodes = findChildren(node, type);
		if (nodes != null && nodes.length > 0) {
			return nodes[0];
		} else {
			return null;
		}
	}

	public static INode[] findChildren(INode node, String type) {
		List list = new ArrayList();
		if (node != null) {
			for (int i = 0; i < node.getChildrenSize(); ++i) {
				INode n = (INode) node.getChild(i);
				if (n.getNodeClassName().equals(type)) {
					list.add(n);
				} else {
					INode[] nn = findChildren(n, type);
					if (nn != null) {
						for (int j = 0; j < nn.length; j++) {
							list.add(nn[j]);
						}
					}
				}
			}
		}
		return (INode[]) list.toArray(new INode[0]);
	}

	public static INode findFromNode(FromNode fromlist, String aliasName) {
		if (fromlist != null) {
			for (int i = 0; i < fromlist.getChildrenSize(); i++) {
				INode node = fromlist.getChild(i);
				if (node instanceof AliasNode) {
					if (aliasName.equalsIgnoreCase(((AliasNode) node).getAliasName())) {
						return node;

					} else if (aliasName.equalsIgnoreCase(((AliasNode) node).getName())) {
						return node;
					}
				}
			}
		}
		return null;
	}

	public static INode[] getFromNodes(FromNode fromlist) {
		List list = new ArrayList();
		for (int i = 0; i < fromlist.getChildrenSize(); i++) {
			INode node = fromlist.getChild(i);
			if (node instanceof AliasNode) {
				list.add(node);
			}
		}
		return (INode[]) list.toArray(new INode[0]);
	}

	

}
