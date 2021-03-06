package br.com.anteros.helpme.util;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class FileFilter extends ViewerFilter {

	private final String[] fileExtensions;

	private List<IResource> excludedFiles;
	private boolean recursive ;

	private final boolean allowDirectories;
	
	public FileFilter(String[] fileExtensions, IResource[] excludedFiles, boolean recusive) {
		this.fileExtensions = fileExtensions;
		if (excludedFiles != null) {
			this.excludedFiles= Arrays.asList(excludedFiles);
		} else {
			this.excludedFiles= null;
		}
		recursive = recusive;
		allowDirectories = false;
	}
	
	public FileFilter(String[] fileExtensions, List<IResource> usedFiles, boolean recusive, boolean allowDirectories) {
		
		this.fileExtensions = fileExtensions;
		this.excludedFiles= usedFiles;
		recursive = recusive;
		this.allowDirectories = allowDirectories;		
	}
	
	public boolean select(Viewer viewer, Object parent, Object element) {
		if ( (element instanceof IFile) ) {
			if (this.excludedFiles != null && this.excludedFiles.contains(element) ) {
				return false;
			}
			return isFileExtension( ( (IFile)element).getFullPath() );
		} 
		else if (allowDirectories && element instanceof IFolder) {
			return true;
		} else if (element instanceof IContainer) { // IProject, IFolder
			if (!((IContainer)element).isAccessible()) return false;
			if (!recursive ) {
				return true;
			}
			try {
				IResource[] resources= ( (IContainer)element).members();
				for (int i= 0; i < resources.length; i++) {
					if (select(viewer, parent, resources[i]) ) {
						return true;
					}
				}
			} catch (CoreException e) {
			}				
		}
		return false;
	}
	
	public boolean isFileExtension(IPath path) {
		for (int i= 0; i < fileExtensions.length; i++) {
			if (path.lastSegment().endsWith(fileExtensions[i]) ) {
				return true;
			}
		}
		return false;
	}
			
	
			
}
