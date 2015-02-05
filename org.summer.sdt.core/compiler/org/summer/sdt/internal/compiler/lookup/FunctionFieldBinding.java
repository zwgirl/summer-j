package org.summer.sdt.internal.compiler.lookup;

import org.summer.sdt.internal.compiler.impl.Constant;

public class FunctionFieldBinding extends FieldBinding {
	public MethodBinding methodBinding;

	public FunctionFieldBinding(char[] name, TypeBinding type, int modifiers,
			ReferenceBinding declaringClass, Constant constant) {
		super(name, type, modifiers, declaringClass, constant);
	}

}
