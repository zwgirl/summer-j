package org.summer.sdt.internal.compiler.lookup;

public class ModuleBinding extends ReferenceBinding {
	public char[] sourceName;
	private FieldBinding[] fields;                         // MUST NOT be modified directly, use setter !
	private MethodBinding[] methods;                       // MUST NOT be modified directly, use setter !
	public ReferenceBinding[] memberTypes;                 // MUST NOT be modified directly, use setter !
	public ModuleScope scope;
	
	@Override
	public int kind() {
		return Binding.MODULE_TYPE;
	}

	@Override
	public char[] readableName() {
		return sourceName;
	}
	
	public FieldBinding[] fields() {
		return fields;
	}
	
	@Override
	public MethodBinding[] methods() {
		return methods;
	}
	
	@Override
	public ReferenceBinding[] memberTypes() {
		return memberTypes;
	}

}
