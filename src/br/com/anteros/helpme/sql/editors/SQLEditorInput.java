package br.com.anteros.helpme.sql.editors;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

public class SQLEditorInput implements IStorageEditorInput {
	String name = "EasySQL Editor";
	IStorage is;

	public SQLEditorInput() {
		is = new IStorage() {

			public InputStream getContents() throws CoreException {
				return new ByteArrayInputStream("".getBytes());
			}

			public IPath getFullPath() {
				return null;
			}

			public String getName() {
				return name;
			}

			public boolean isReadOnly() {
				return false;
			}

			public Object getAdapter(Class adapter) {
				return null;
			}
		};
	}

	public SQLEditorInput(IStorage storage) {
		is = storage;
	}

	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return "Execute SQL";
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

	public IStorage getStorage() throws CoreException {
		return is;
	}
}
