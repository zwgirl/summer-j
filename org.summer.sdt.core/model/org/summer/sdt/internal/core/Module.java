package org.summer.sdt.internal.core;

import org.summer.sdt.core.IModule;
import org.summer.sdt.core.ISourceRange;
import org.summer.sdt.core.JavaModelException;
import org.summer.sdt.core.SourceRange;

public class Module extends SourceRefElement implements IModule{

	/*
	 * This element's name, or an empty <code>String</code> if this
	 * element does not have a name.
	 */
	protected String name;
	public int declarationSourceStart, declarationSourceEnd;
	public int nameStart, nameEnd;
	
	protected Module(JavaElement parent) {
		super(parent);
	}
	
	public String getElementName() {
		return this.name;
	}

	@Override
	public int getElementType() {
		return MODULE;
	}

	@Override
	public ISourceRange getNameRange() {
		return new SourceRange(this.nameStart, this.nameEnd-this.nameStart+1);
	}
	
	public ISourceRange getSourceRange() throws JavaModelException {
		return new SourceRange(this.declarationSourceStart, this.declarationSourceEnd-this.declarationSourceStart+1);
	}

	protected char getHandleMementoDelimiter() {
		return JavaElement.JEM_COMPILATIONUNIT;
	}

}
