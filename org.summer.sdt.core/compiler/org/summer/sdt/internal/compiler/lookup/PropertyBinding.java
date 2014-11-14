package org.summer.sdt.internal.compiler.lookup;

import org.summer.sdt.internal.compiler.ast.ASTNode;
import org.summer.sdt.internal.compiler.ast.PropertyDeclaration;
import org.summer.sdt.internal.compiler.impl.Constant;

public class PropertyBinding extends FieldBinding {
//	public ReferenceBinding declaringClass;
//	public int compoundUseFlag = 0; // number or accesses via postIncrement or compoundAssignment
//	
//	public int modifiers;
//	public TypeBinding type;
//	public char[] name;
//	protected Constant constant;
//	public int id; // for flow-analysis (position in flowInfo bit vector)
//	public long tagBits;
	
	protected PropertyBinding() {
		this((char[])null, null, 0, null);
		// for creating problem field
	}
	public PropertyBinding(char[] name, TypeBinding type, int modifiers, ReferenceBinding declaringClass, Constant constant) {
		this(name, type, modifiers, constant);
		this.declaringClass = declaringClass;
	}
	// special API used to change field declaring class for runtime visibility check
	public PropertyBinding(PropertyBinding initialFieldBinding, ReferenceBinding declaringClass) {
		this(initialFieldBinding.name, initialFieldBinding.type, initialFieldBinding.modifiers, initialFieldBinding.constant());
		this.declaringClass = declaringClass;
		this.id = initialFieldBinding.id;
		setAnnotations(initialFieldBinding.getAnnotations());
	}
	/* API
	* Answer the receiver's binding type from Binding.BindingID.
	*/
	public PropertyBinding(PropertyDeclaration field, TypeBinding type, int modifiers, ReferenceBinding declaringClass) {
		this(field.name, type, modifiers, declaringClass, null);
		field.binding = this; // record binding in declaration
	}

	protected PropertyBinding(char[] name, TypeBinding type, int modifiers, Constant constant) {
		this.name = name;
		this.type = type;
		this.modifiers = modifiers;
		this.constant = constant;
		if (type != null) {
			this.tagBits |= (type.tagBits & TagBits.HasMissingType);
		}
	}
	
	/**
	 * Returns the original field (as opposed to parameterized instances)
	 */
	public PropertyBinding original() {
		return this;
	}

	public AnnotationBinding[] getAnnotations() {
		PropertyBinding originalField = original();
		ReferenceBinding declaringClassBinding = originalField.declaringClass;
		if (declaringClassBinding == null) {
			return Binding.NO_ANNOTATIONS;
		}
		return declaringClassBinding.retrieveAnnotations(originalField);
	}
	
	public String toString() {
		StringBuffer output = new StringBuffer(10);
		ASTNode.printModifiers(this.modifiers, output);
		if ((this.modifiers & ExtraCompilerModifiers.AccUnresolved) != 0) {
			output.append("[unresolved] "); //$NON-NLS-1$
		}
		output.append(this.type != null ? this.type.debugName() : "<no type>"); //$NON-NLS-1$
		output.append(" "); //$NON-NLS-1$
		output.append((this.name != null) ? new String(this.name) : "<no name>"); //$NON-NLS-1$
		return output.toString();
	}
}