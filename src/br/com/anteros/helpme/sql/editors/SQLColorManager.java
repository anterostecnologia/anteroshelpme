package br.com.anteros.helpme.sql.editors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class SQLColorManager {

	public static final RGB BACKGROUND = new RGB(255, 255, 255);
	public static final RGB MULTI_LINE_COMMENT = new RGB(0, 128, 0); 
	public static final RGB SINGLE_LINE_COMMENT = new RGB(0, 128, 0);
	public static final RGB KEYWORD = new RGB(128, 0, 64); 
	public static final RGB TYPE = new RGB(128, 0, 0); 
	public static final RGB STRING = new RGB(0, 0, 255); 
	public static final RGB DEFAULT = new RGB(0, 0, 0);
	public static final RGB FUNCTION = new RGB(209, 16, 1);
	public static final RGB OPERATOR = new RGB(255, 0, 0); 
	public static final RGB TABLE = new RGB(0, 100, 0);
	public static final RGB SYMBOL = new RGB(127, 0, 55);

	protected Map fColorTable = new HashMap();

	public void dispose() {
		Iterator e = fColorTable.values().iterator();
		while (e.hasNext())
			 ((Color) e.next()).dispose();
	}

	public Color getColor(RGB rgb) {
		Color color = (Color) fColorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
}
