package org.summer.sdt.internal.core;

import java.util.HashMap;

import org.summer.sdt.core.IJavaElement;
import org.summer.sdt.core.IModule;
import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.env.ISourceImport;

/**
 * 
 * @author cym
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ModuleElementInfo extends AnnotatableInfo {
	protected static final ISourceImport[] NO_IMPORTS = new ISourceImport[0];
	protected static final SourceField[] NO_FIELDS = new SourceField[0];
	protected static final SourceMethod[] NO_METHODS = new SourceMethod[0];
	protected static final SourceType[] NO_TYPES = new SourceType[0];

	protected IJavaElement[] children = JavaElement.NO_ELEMENTS;
	
	/**
	 * Backpointer to my type handle - useful for translation
	 * from info to handle.
	 */
	protected IModule handle = null;

	public void setHandle(Module handle) {
		this.handle = handle;
		
	}
	
	protected HashMap categories;

	protected void addCategories(IJavaElement element, char[][] elementCategories) {
		if (elementCategories == null) return;
		if (this.categories == null)
			this.categories = new HashMap();
		this.categories.put(element, CharOperation.toStrings(elementCategories));
	}
	
	/*
	 * Return a map from an IJavaElement (this type or a child of this type) to a String[] (the categories of this element)
	 */
	public HashMap getCategories() {
		return this.categories;
	}
	
	public IJavaElement[] getChildren() {
		return this.children;
	}

}
