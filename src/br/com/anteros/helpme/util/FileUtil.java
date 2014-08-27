package br.com.anteros.helpme.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {

	public static final String NEW_LINE = System.getProperty("line.separator");

	public static String[] getChild(String path) {
		File file = new File(path);
		return file.list();
	}

	public static String getFileString(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));

		StringBuffer sb = new StringBuffer();

		String str = null;
		while ((str = reader.readLine()) != null) {
			sb.append(str);
			sb.append(NEW_LINE);
		}

		int len = sb.length();
		if (len > 0) {
			sb.delete(len - NEW_LINE.length(), len);
		}

		return sb.toString();
	}

}
