package org.summer.sdt.internal.compiler.lookup;

import org.summer.sdt.internal.compiler.impl.Constant;

public class FunctionFieldBinding extends FieldBinding {

	public FunctionFieldBinding(char[] name, int modifiers,
			ReferenceBinding declaringClass, Constant constant) {
		super(name, null, modifiers, declaringClass, constant);
	}

}
