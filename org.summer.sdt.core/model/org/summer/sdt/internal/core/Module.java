package org.summer.sdt.internal.core;

import org.summer.sdt.core.IModule;

public class Module extends Member implements IModule{

	/*
	 * This element's name, or an empty <code>String</code> if this
	 * element does not have a name.
	 */
	protected String name;
	
	protected Module(JavaElement parent, String name) {
		super(parent);
		
		this.name = name;
	}
	
	public String getElementName() {
		return this.name;
	}

	@Override
	public int getElementType() {
		return MODULE;
	}

}
