package br.com.anteros.helpme.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.swt.graphics.Image;

import br.com.anteros.helpme.AnterosHelpmePlugin;
import br.com.anteros.helpme.model.persistence.SQLProjectPersistent;
import br.com.anteros.helpme.treeviewer.IObjectNode;
import br.com.anteros.helpme.treeviewer.TreeNode;
import br.com.anteros.helpme.util.ClassLoaderUtil;
import br.com.anteros.helpme.util.PluginUtils;
import br.com.anteros.persistence.schema.SchemaManager;
import br.com.anteros.persistence.session.SQLSession;
import br.com.anteros.persistence.session.SQLSessionFactory;
import br.com.anteros.persistence.session.SQLSessionFactoryHelper;

public class Project extends TreeNode {

	private String projectName;
	private String fileConfiguration;
	private List<Classpath> classpath;
	private SQLSessionFactory sessionFactory;
	private SQLSession session;
	private Configuration configuration;
	private Entities entities;
	private Database database;
	private SchemaManager schemaManager;

	public Project(String name, String projectName, String fileConfiguration,
			List<Classpath> classpath) {
		this.projectName = projectName;
		this.fileConfiguration = fileConfiguration;
		this.name = new String[] { name };
		this.classpath = classpath;
	}

	public Image getLeftImage() throws Exception {
		if (isInitialized())
			return AnterosHelpmePlugin.getDefault().getImage(
					AnterosHelpmePlugin.IMG_PROJECT_OPEN);
		else
			return AnterosHelpmePlugin.getDefault().getImage(
					AnterosHelpmePlugin.IMG_PROJECT);
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getFileConfiguration() {
		return fileConfiguration;
	}

	public void setFileConfiguration(String fileConfiguration) {
		this.fileConfiguration = fileConfiguration;
	}

	public List<Classpath> getClasspath() {
		if (classpath == null)
			classpath = new ArrayList<Classpath>();
		return classpath;
	}

	public void setClasspath(List<Classpath> classpath) {
		this.classpath = classpath;
	}

	@Override
	public void addNode(IObjectNode node) throws Exception {
		if (!(node instanceof Entities) && !(node instanceof Configuration)
				&& !(node instanceof Database))
			throw new ModelException(
					"O objeto Project aceita apenas objetos do tipo Entities. Objeto recebido "
							+ node.getClass().getName() + " -> "
							+ node.getName());
		super.addNode(node);
	}

	@Override
	public void initialized() {
		try {
			AnterosHelpmePlugin.getDefault().setWaitCursor();

			configuration = new Configuration();
			this.addNode(configuration);

			entities = new Entities();
			this.addNode(entities);

			IJavaProject javaProject = PluginUtils.findJavaProject(this
					.getProjectName());

			ClassLoader oldLoader = Thread.currentThread()
					.getContextClassLoader();

			URLClassLoader newClassLoader = PluginUtils
					.getProjectClassLoader(javaProject);

			try {
				String fileName = ResourcesPlugin.getWorkspace().getRoot()
						.getRawLocation().toOSString()
						+ this.getFileConfiguration();
				FileInputStream fis = new FileInputStream(fileName);

				Thread.currentThread().setContextClassLoader(newClassLoader);

				sessionFactory = SQLSessionFactoryHelper
						.getNewSessionFactory(fis);
				session = sessionFactory.openSession();

				schemaManager = new SchemaManager(session,
						session.getEntityCacheManager(), true);
				
				schemaManager.buildTablesSchema();

				database = new Database();
				database.setName(new String[] { session.getDialect().name() });
				this.addNode(database);

				setInitialized(true);
			} finally {
				Thread.currentThread().setContextClassLoader(oldLoader);
			}
		} catch (Exception e) {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
			AnterosHelpmePlugin.error("Não foi possível inicializar o Projeto "
					+ this.getSimpleName() + "->" + this.getProjectName(), e,
					true);
		} finally {
			AnterosHelpmePlugin.getDefault().setDefaultCursor();
		}
	}

	private String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	public SQLSessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SQLSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SQLSession getSession() {
		return session;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public Entities getEntities() {
		return entities;
	}

	public Database getDatabase() {
		return database;
	}

	public static Project createFromPersistent(
			SQLProjectPersistent projectPersistent) {
		List<Classpath> cp = new ArrayList<Classpath>();
		for (String s : projectPersistent.classPath) {
			Classpath c = new Classpath();
			c.setClasspath(s);
			cp.add(c);
		}
		return new Project(projectPersistent.name,
				projectPersistent.projectName,
				projectPersistent.fileConfiguration, cp);
	}

	public SchemaManager getSchemaManager() {
		return schemaManager;
	}

}