package br.com.anteros.helpme.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;


/**
 * Classe utilitária para facilitar o uso dos ClassLoader's
 * @author Edson Martins
 *
 */
public class ClassLoaderUtil {

	private static final Class[] parameters = new Class[] { URL.class };

	/**
	 * Permite adicionar uma URL no ClassPath do ClassLoader
	 * @param u URL do jar, da classe ou pasta
	 * @param sysLoader ClassLoader
	 * @throws IOException
	 */
	public static void addURLClassPath(URL u, URLClassLoader sysLoader)
			throws IOException {
		addURLClassPath(new URL[] { u }, sysLoader);
	}

	/**
	 * Permite adicionar uma ou mais Url's no ClassPath do ClassLoader
	 * @param u URL's dos jars, das classes ou pastas
	 * @param sysLoader ClassLoader
	 * @throws IOException
	 */
	public static void addURLClassPath(URL[] u, URLClassLoader sysLoader)
			throws IOException {
		URL urls[] = sysLoader.getURLs();
		for (int i = 0; i < urls.length; i++) {
			if (urls[i].toString().equalsIgnoreCase(u.toString())) {
				System.out.println("URL " + u + " já adicionada no CLASSPATH");
				return;
			}
		}
		Class sysclass = URLClassLoader.class;
		try {
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysLoader, u);
		} catch (Throwable t) {
			throw new IOException("Erro adicionando URL no classPath "+t.getMessage());
		}
	}

	/**
	 * Imprime no console o ClassPath de um ClassLoader
	 * @param classLoader
	 * @throws URISyntaxException
	 */
	public static void printClassLoaderPath(URLClassLoader classLoader)
			throws URISyntaxException {
		System.out.println("");
		System.out.println("ClassPath:");
		System.out.println("---------------------------");
		URL urls[] = classLoader.getURLs();
		for (int i = 0; i < urls.length; i++) {
			System.out.println(urls[i].getFile());
		}
	}

	/**
	 * Converte as urls de um ClassLoader para o formato de Url de um ClassPath
	 * jar;jar;jar;. Formato usado para compilação de relatórios pelo JasperReport.
	 * @param classLoader
	 * @return
	 * @throws URISyntaxException
	 */
	public static String convertURLClassLoaderToClassPath(
			URLClassLoader classLoader) throws URISyntaxException {
		URL urls[] = classLoader.getURLs();
		String result = "";
		for (int i = 0; i < urls.length; i++) {
			String u = urls[i].toString();
			u = StringUtils.replace(u, "file:/", "");
			if (result.equals(""))
				result = result + u;
			else
				result = result + File.pathSeparator + u;
		}
		return result;
	}
}
