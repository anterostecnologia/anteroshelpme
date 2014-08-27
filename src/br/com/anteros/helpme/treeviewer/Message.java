/*
 * Copyright (C) 2007 SQL Explorer Development Team
 * http://sourceforge.net/projects/eclipsesql
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package br.com.anteros.helpme.treeviewer;

import br.com.anteros.helpme.util.StringUtils;


public class Message {
	
	private static final int MAX_SQL_DISPLAY_LENGTH = 70;
	
	public static enum Status {
		FAILURE {
			protected String getText() {
				return "SQLEditor.Results.Messages.Failure";
			}
		}, SUCCESS {
			protected String getText() {
				return "SQLEditor.Results.Messages.Success";
			}
		}, STATUS {
			protected String getText() {
				return "SQLEditor.Results.Messages.Status";
			}
		};
		
		protected abstract String getText();
	}
	
	private Status status;
	
	private int lineNo;
	
	private int charNo;
	
	private String sql;
	
	private String message;

	public Message(Status status, int lineNo, int charNo, CharSequence sql, String message) {
		super();
		this.status = status;
		this.lineNo = lineNo;
		this.charNo = charNo;
		if (sql != null)
			setSql(sql);
		this.message = message;
	}
	
	public Message(Status status, int lineNo, int charNo, String message) {
		this(status, lineNo, charNo, null, message);
	}
	
	public Message(Status status, String sql, String message) {
		this(status, 0, 0, sql, message);
	}
	
	public Message(Status status, String message) {
		this(status, 0, 0, message);
	}
	
String[] getTableText() {
		String location = "";
		if (lineNo > 0) {
			location = "line " + lineNo;
			if (charNo > 0)
				location += ", col " + charNo;
		}
		
		String[] result = new String[] {
			status.getText(),
			location,
			(sql == null) ? "" : sql,
			StringUtils.getWrappedText(message)
		};
		return result; 
	}

	public Status getStatus() {
		return status;
	}

	public int getLineNo() {
		return lineNo;
	}

	public int getCharNo() {
		return charNo;
	}

	public String getMessage() {
		return message;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(CharSequence sql) {
		this.sql = StringUtils.compressWhitespace(sql, MAX_SQL_DISPLAY_LENGTH);
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public void setCharNo(int charNo) {
		this.charNo = charNo;
	}
	
	public String toString() {
		return "[" + lineNo + "," + charNo + "] " + message;
	}
	
	
}