package org.summer.sdt.internal.compiler.env;

import org.summer.sdt.internal.compiler.impl.Constant;

public interface IBinaryMember extends IGenericMember{
	/**
	 * Answer the runtime visible and invisible annotations for this field or null if none.
	 */
	IBinaryAnnotation[] getAnnotations();
	
	/**
	 * Answer the runtime visible and invisible type annotations for this field or null if none.
	 */
	IBinaryTypeAnnotation[] getTypeAnnotations();
	
	/**
	 *
	 * @return org.summer.sdt.internal.compiler.Constant
	 */
	Constant getConstant();
	
	/**
	 * Answer the receiver's signature which describes the parameter &
	 * return types as specified in section 4.4.4 of the Java 2 VM spec.
	 */
	char[] getGenericSignature();
	
	/**
	 * Answer the name of the field.
	 */
	char[] getName();
	
	/**
	 * Answer the tagbits set according to the bits for annotations.
	 */
	long getTagBits();
	
	/**
	 * Answer the resolved name of the receiver's type in the
	 * class file format as specified in section 4.3.2 of the Java 2 VM spec.
	 *
	 * For example:
	 *   - java.lang.String is Ljava/lang/String;
	 *   - an int is I
	 *   - a 2 dimensional array of strings is [[Ljava/lang/String;
	 *   - an array of floats is [F
	 */
	char[] getTypeName();
	
	//cym 2014-12-05
	char[][] getParameters();
	
	/**
	 * Return {@link ClassSignature} for a Class {@link java.lang.Class}.
	 * Return {@link org.summer.sdt.internal.compiler.impl.Constant} for compile-time constant of primitive type, as well as String literals.
	 * Return {@link EnumConstantSignature} if value is an enum constant.
	 * Return {@link IBinaryAnnotation} for annotation type.
	 * Return {@link Object}[] for array type.
	 *
	 * @return default value of this annotation method
	 */
	Object getDefaultValue();
	
	/**
	 * Answer the resolved names of the exception types in the
	 * class file format as specified in section 4.2 of the Java 2 VM spec
	 * or null if the array is empty.
	 *
	 * For example, java.lang.String is java/lang/String.
	 */
	char[][] getExceptionTypeNames();
	
	
	/**
	 * Answer the receiver's method descriptor which describes the parameter &
	 * return types as specified in section 4.4.3 of the Java 2 VM spec.
	 *
	 * For example:
	 *   - int foo(String) is (Ljava/lang/String;)I
	 *   - Object[] foo(int) is (I)[Ljava/lang/Object;
	 */
	char[] getMethodDescriptor();
	
	/**
	 * Answer whether the receiver represents a class initializer method.
	 */
	boolean isClinit();
}
