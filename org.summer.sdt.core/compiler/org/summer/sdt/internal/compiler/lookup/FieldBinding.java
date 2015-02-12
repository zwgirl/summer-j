/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Stephan Herrmann <stephan@cs.tu-berlin.de> - Contributions for
 *								bug 185682 - Increment/decrement operators mark local variables as read
 *								bug 331649 - [compiler][null] consider null annotations for fields
 *								Bug 417295 - [1.8[[null] Massage type annotated null analysis to gel well with deep encoded type bindings.
 *								Bug 447088 - [null] @Nullable on fully qualified field type is ignored
 *								Bug 435805 - [1.8][compiler][null] Java 8 compiler does not recognize declaration style null annotations
 *******************************************************************************/
package org.summer.sdt.internal.compiler.lookup;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.ClassFile;
import org.summer.sdt.internal.compiler.ast.ASTNode;
import org.summer.sdt.internal.compiler.ast.FieldDeclaration;
import org.summer.sdt.internal.compiler.ast.TypeDeclaration;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.util.Util;

public class FieldBinding extends VariableBinding {
	public ReferenceBinding declaringClass;
	public int compoundUseFlag = 0; // number or accesses via postIncrement or compoundAssignment
	
	//cym 2015-02-12
	public TypeBinding returnType;
	public TypeBinding[] parameters;
	public TypeBinding receiver;  // JSR308 - explicit this parameter
	public ReferenceBinding[] thrownExceptions = Binding.NO_EXCEPTIONS;
	public TypeVariableBinding[] typeVariables = Binding.NO_TYPE_VARIABLES;
	char[] signature;

	/** Store nullness information from annotation (incl. applicable default). */
	public Boolean[] parameterNonNullness;  // TRUE means @NonNull declared, FALSE means @Nullable declared, null means nothing declared
	public int defaultNullness; // for null *type* annotations

	/** Store parameter names from MethodParameters attribute (incl. applicable default). */
	public char[][] parameterNames = Binding.NO_PARAMETER_NAMES;
	//cym 2015-02-12 end
	
	protected FieldBinding() {
		super(null, null, 0, null);
		// for creating problem field
	}
	public FieldBinding(char[] name, TypeBinding type, int modifiers, ReferenceBinding declaringClass, Constant constant) {
		super(name, type, modifiers, constant);
		this.declaringClass = declaringClass;
		
		//cym 2015-02-12
		this.parameters = NO_PARAMETERS;
	}
	// special API used to change field declaring class for runtime visibility check
	public FieldBinding(FieldBinding initialFieldBinding, ReferenceBinding declaringClass) {
		super(initialFieldBinding.name, initialFieldBinding.type, initialFieldBinding.modifiers, initialFieldBinding.constant());
		this.declaringClass = declaringClass;
		this.id = initialFieldBinding.id;
		setAnnotations(initialFieldBinding.getAnnotations());
	}
	/* API
	* Answer the receiver's binding type from Binding.BindingID.
	*/
	public FieldBinding(FieldDeclaration field, TypeBinding type, int modifiers, ReferenceBinding declaringClass) {
		this(field.name, type, modifiers, declaringClass, null);
		field.binding = this; // record binding in declaration
	}
	
	public final boolean canBeSeenBy(PackageBinding invocationPackage) {
		if (isPublic()) return true;
		if (isPrivate()) return false;
	
		// isProtected() or isDefault()
		return invocationPackage == this.declaringClass.getPackage();
	}
	/* Answer true if the receiver is visible to the type provided by the scope.
	* InvocationSite implements isSuperAccess() to provide additional information
	* if the receiver is protected.
	*
	* NOTE: Cannot invoke this method with a compilation unit scope.
	*/
	
	public final boolean canBeSeenBy(TypeBinding receiverType, InvocationSite invocationSite, Scope scope) {
		if (isPublic()) return true;
	
		SourceTypeBinding invocationType = scope.enclosingSourceType();
		if (TypeBinding.equalsEquals(invocationType, this.declaringClass) && TypeBinding.equalsEquals(invocationType, receiverType)) return true;
	
		if (invocationType == null) // static import call
			return !isPrivate() && scope.getCurrentPackage() == this.declaringClass.fPackage;
	
		if (isProtected()) {
			// answer true if the invocationType is the declaringClass or they are in the same package
			// OR the invocationType is a subclass of the declaringClass
			//    AND the receiverType is the invocationType or its subclass
			//    OR the method is a static method accessed directly through a type
			//    OR previous assertions are true for one of the enclosing type
			if (TypeBinding.equalsEquals(invocationType, this.declaringClass)) return true;
			if (invocationType.fPackage == this.declaringClass.fPackage) return true;
	
			ReferenceBinding currentType = invocationType;
			int depth = 0;
			ReferenceBinding receiverErasure = (ReferenceBinding)receiverType.erasure();
			ReferenceBinding declaringErasure = (ReferenceBinding) this.declaringClass.erasure();
			do {
				if (currentType.findSuperTypeOriginatingFrom(declaringErasure) != null) {
					if (invocationSite.isSuperAccess())
						return true;
					// receiverType can be an array binding in one case... see if you can change it
					if (receiverType instanceof ArrayBinding)
						return false;
					if (isStatic()) {
						if (depth > 0) invocationSite.setDepth(depth);
						return true; // see 1FMEPDL - return invocationSite.isTypeAccess();
					}
					if (TypeBinding.equalsEquals(currentType, receiverErasure) || receiverErasure.findSuperTypeOriginatingFrom(currentType) != null) {
						if (depth > 0) invocationSite.setDepth(depth);
						return true;
					}
				}
				depth++;
				currentType = currentType.enclosingType();
			} while (currentType != null);
			return false;
		}
	
		if (isPrivate()) {
			// answer true if the receiverType is the declaringClass
			// AND the invocationType and the declaringClass have a common enclosingType
			receiverCheck: {
				if (TypeBinding.notEquals(receiverType, this.declaringClass)) {
					// special tolerance for type variable direct bounds, but only if compliance <= 1.6, see: https://bugs.eclipse.org/bugs/show_bug.cgi?id=334622
					if (scope.compilerOptions().complianceLevel <= ClassFileConstants.JDK1_6 && receiverType.isTypeVariable() && ((TypeVariableBinding) receiverType).isErasureBoundTo(this.declaringClass.erasure()))
						break receiverCheck;
					return false;
				}
			}
	
			if (TypeBinding.notEquals(invocationType, this.declaringClass)) {
				ReferenceBinding outerInvocationType = invocationType;
				ReferenceBinding temp = outerInvocationType.enclosingType();
				while (temp != null) {
					outerInvocationType = temp;
					temp = temp.enclosingType();
				}
	
				ReferenceBinding outerDeclaringClass = (ReferenceBinding) this.declaringClass.erasure();
				temp = outerDeclaringClass.enclosingType();
				while (temp != null) {
					outerDeclaringClass = temp;
					temp = temp.enclosingType();
				}
				if (TypeBinding.notEquals(outerInvocationType, outerDeclaringClass)) return false;
			}
			return true;
		}
	
		// isDefault()
		PackageBinding declaringPackage = this.declaringClass.fPackage;
		if (invocationType.fPackage != declaringPackage) return false;
	
		// receiverType can be an array binding in one case... see if you can change it
		if (receiverType instanceof ArrayBinding)
			return false;
		TypeBinding originalDeclaringClass = this.declaringClass.original();
		ReferenceBinding currentType = (ReferenceBinding) receiverType;
		do {
			if (currentType.isCapture()) { // https://bugs.eclipse.org/bugs/show_bug.cgi?id=285002
				if (TypeBinding.equalsEquals(originalDeclaringClass, currentType.erasure().original())) return true;
			} else {
				if (TypeBinding.equalsEquals(originalDeclaringClass, currentType.original())) return true;
			}
			PackageBinding currentPackage = currentType.fPackage;
			// package could be null for wildcards/intersection types, ignore and recurse in superclass
			if (currentPackage != null && currentPackage != declaringPackage) return false;
		} while ((currentType = currentType.superclass()) != null);
		return false;
	}
	
	/*
	 * declaringUniqueKey dot fieldName ) returnTypeUniqueKey
	 * p.X { X<T> x} --> Lp/X;.x)p/X<TT;>;
	 */
	public char[] computeUniqueKey(boolean isLeaf) {
		// declaring key
		char[] declaringKey =
			this.declaringClass == null /*case of length field for an array*/
				? CharOperation.NO_CHAR
				: this.declaringClass.computeUniqueKey(false/*not a leaf*/);
		int declaringLength = declaringKey.length;
	
		// name
		int nameLength = this.name.length;
	
		// return type
		char[] returnTypeKey = this.type == null ? new char[] {'V'} : this.type.computeUniqueKey(false/*not a leaf*/);
		int returnTypeLength = returnTypeKey.length;
	
		char[] uniqueKey = new char[declaringLength + 1 + nameLength + 1 + returnTypeLength];
		int index = 0;
		System.arraycopy(declaringKey, 0, uniqueKey, index, declaringLength);
		index += declaringLength;
		uniqueKey[index++] = '.';
		System.arraycopy(this.name, 0, uniqueKey, index, nameLength);
		index += nameLength;
		uniqueKey[index++] = ')';
		System.arraycopy(returnTypeKey, 0, uniqueKey, index, returnTypeLength);
		return uniqueKey;
	}
	public Constant constant() {
		Constant fieldConstant = this.constant;
		if (fieldConstant == null) {
			if (isFinal()) {
				//The field has not been yet type checked.
				//It also means that the field is not coming from a class that
				//has already been compiled. It can only be from a class within
				//compilation units to process. Thus the field is NOT from a BinaryTypeBinbing
				FieldBinding originalField = original();
				if (originalField.declaringClass instanceof SourceTypeBinding) {
					SourceTypeBinding sourceType = (SourceTypeBinding) originalField.declaringClass;
					if (sourceType.scope != null) {
						TypeDeclaration typeDecl = sourceType.scope.referenceContext;
						FieldDeclaration fieldDecl = typeDecl.declarationOf(originalField);
						MethodScope initScope = originalField.isStatic() ? typeDecl.staticInitializerScope : typeDecl.initializerScope;
						boolean old = initScope.insideTypeAnnotation;
						try {
							initScope.insideTypeAnnotation = false;
							fieldDecl.resolve(initScope); //side effect on binding
						} finally {
							initScope.insideTypeAnnotation = old;
						}
						fieldConstant = originalField.constant == null ? Constant.NotAConstant : originalField.constant;
					} else {
						fieldConstant = Constant.NotAConstant; // shouldn't occur per construction (paranoid null check)
					}
				} else {
					fieldConstant = Constant.NotAConstant; // shouldn't occur per construction (paranoid null check)
				}
			} else {
				fieldConstant = Constant.NotAConstant;
			}
			this.constant = fieldConstant;
		}
		return fieldConstant;
	}
	
	public void fillInDefaultNonNullness(FieldDeclaration sourceField, Scope scope) {
		LookupEnvironment environment = scope.environment();
		if (   this.type != null
			&& !this.type.isBaseType()
			&& (this.tagBits & TagBits.AnnotationNullMASK) == 0 		// declaration annotation?
			&& (this.type.tagBits & TagBits.AnnotationNullMASK) == 0)	// type annotation? (java.lang.@Nullable String)
		{
			if (environment.usesNullTypeAnnotations())
				this.type = environment.createAnnotatedType(this.type, new AnnotationBinding[]{environment.getNonNullAnnotation()});
			else
				this.tagBits |= TagBits.AnnotationNonNull;
		} else if ((this.tagBits & TagBits.AnnotationNonNull) != 0) {
			scope.problemReporter().nullAnnotationIsRedundant(sourceField);
		}
	}
	
//	/**
//	 * X<T> t   -->  LX<TT;>;
//	 */
//	public char[] genericSignature() {
//	    if ((this.modifiers & ExtraCompilerModifiers.AccGenericSignature) == 0) return null;
//	    return this.type.genericTypeSignature();
//	}
	
	/**
	 * <pre>
	 *<typeParam1 ... typeParamM>(param1 ... paramN)returnType thrownException1 ... thrownExceptionP
	 * T foo(T t) throws X<T>   --->   (TT;)TT;LX<TT;>;
	 * void bar(X<T> t)   -->   (LX<TT;>;)V
	 * <T> void bar(X<T> t)   -->  <T:Ljava.lang.Object;>(LX<TT;>;)V
	 * </pre>
	 */
	public char[] genericSignature() {
		if ((this.modifiers & ExtraCompilerModifiers.AccGenericSignature) == 0) return null;
		StringBuffer sig = new StringBuffer(10);
		if (this.typeVariables != Binding.NO_TYPE_VARIABLES) {
			sig.append('<');
			for (int i = 0, length = this.typeVariables.length; i < length; i++) {
				sig.append(this.typeVariables[i].genericSignature());
			}
			sig.append('>');
		}
		sig.append('(');
		for (int i = 0, length = this.parameters.length; i < length; i++) {
			sig.append(this.parameters[i].genericTypeSignature());
		}
		sig.append(')');
		if (this.returnType != null)
			sig.append(this.returnType.genericTypeSignature());
	
		// only append thrown exceptions if any is generic/parameterized
		boolean needExceptionSignatures = false;
		int length = this.thrownExceptions.length;
		for (int i = 0; i < length; i++) {
			if((this.thrownExceptions[i].modifiers & ExtraCompilerModifiers.AccGenericSignature) != 0) {
				needExceptionSignatures = true;
				break;
			}
		}
		if (needExceptionSignatures) {
			for (int i = 0; i < length; i++) {
				sig.append('^');
				sig.append(this.thrownExceptions[i].genericTypeSignature());
			}
		}
		int sigLength = sig.length();
		char[] genericSignature = new char[sigLength];
		sig.getChars(0, sigLength, genericSignature, 0);
		return genericSignature;
	}
	
	
	/* Answer the receiver's signature.
	*
	* NOTE: This method should only be used during/after code gen.
	* The signature is cached so if the signature of the return type or any parameter
	* type changes, the cached state is invalid.
	*/
	public final char[] signature() /* (ILjava/lang/Thread;)Ljava/lang/Object; */ {
		if (this.signature != null)
			return this.signature;
	
		StringBuffer buffer = new StringBuffer(this.parameters.length + 1 * 20);
		buffer.append('(');
	
		TypeBinding[] targetParameters = this.parameters;
//		boolean isConstructor = isConstructor();
//		if (isConstructor && this.declaringClass.isEnum()) { // insert String name,int ordinal
//			buffer.append(ConstantPool.JavaLangStringSignature);
//			buffer.append(TypeBinding.INT.signature());
//		}
//		boolean needSynthetics = isConstructor && this.declaringClass.isNestedType();
//		if (needSynthetics) {
//			// take into account the synthetic argument type signatures as well
//			ReferenceBinding[] syntheticArgumentTypes = this.declaringClass.syntheticEnclosingInstanceTypes();
//			if (syntheticArgumentTypes != null) {
//				for (int i = 0, count = syntheticArgumentTypes.length; i < count; i++) {
//					buffer.append(syntheticArgumentTypes[i].signature());
//				}
//			}
//	
//			if (this instanceof SyntheticMethodBinding) {
//				targetParameters = ((SyntheticMethodBinding)this).targetMethod.parameters;
//			}
//		}
	
		if (targetParameters != Binding.NO_PARAMETERS) {
			for (int i = 0; i < targetParameters.length; i++) {
				buffer.append(targetParameters[i].signature());
			}
		}
//		if (needSynthetics) {
//			SyntheticArgumentBinding[] syntheticOuterArguments = this.declaringClass.syntheticOuterLocalVariables();
//			int count = syntheticOuterArguments == null ? 0 : syntheticOuterArguments.length;
//			for (int i = 0; i < count; i++) {
//				buffer.append(syntheticOuterArguments[i].type.signature());
//			}
//			// move the extra padding arguments of the synthetic constructor invocation to the end
//			for (int i = targetParameters.length, extraLength = this.parameters.length; i < extraLength; i++) {
//				buffer.append(this.parameters[i].signature());
//			}
//		}
		buffer.append(')');
		if (this.returnType != null)
			buffer.append(this.returnType.signature());
		int nameLength = buffer.length();
		this.signature = new char[nameLength];
		buffer.getChars(0, nameLength, this.signature, 0);
	
		return this.signature;
	}
	/*
	 * This method is used to record references to nested types inside the method signature.
	 * This is the one that must be used during code generation.
	 *
	 * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=171184
	 */
	public final char[] signature(ClassFile classFile) {
		if (this.signature != null) {
			if ((this.tagBits & TagBits.ContainsNestedTypeReferences) != 0) {
				// we need to record inner classes references
//				boolean isConstructor = isConstructor();
				TypeBinding[] targetParameters = this.parameters;
//				boolean needSynthetics = isConstructor && this.declaringClass.isNestedType();
//				if (needSynthetics) {
//					// take into account the synthetic argument type signatures as well
//					ReferenceBinding[] syntheticArgumentTypes = this.declaringClass.syntheticEnclosingInstanceTypes();
//					if (syntheticArgumentTypes != null) {
//						for (int i = 0, count = syntheticArgumentTypes.length; i < count; i++) {
//							ReferenceBinding syntheticArgumentType = syntheticArgumentTypes[i];
//							if ((syntheticArgumentType.tagBits & TagBits.ContainsNestedTypeReferences) != 0) {
//								Util.recordNestedType(classFile, syntheticArgumentType);
//							}
//						}
//					}
//					if (this instanceof SyntheticMethodBinding) {
//						targetParameters = ((SyntheticMethodBinding)this).targetMethod.parameters;
//					}
//				}
	
				if (targetParameters != Binding.NO_PARAMETERS) {
					for (int i = 0, max = targetParameters.length; i < max; i++) {
						TypeBinding targetParameter = targetParameters[i];
						TypeBinding leafTargetParameterType = targetParameter.leafComponentType();
						if ((leafTargetParameterType.tagBits & TagBits.ContainsNestedTypeReferences) != 0) {
							Util.recordNestedType(classFile, leafTargetParameterType);
						}
					}
				}
//				if (needSynthetics) {
//					// move the extra padding arguments of the synthetic constructor invocation to the end
//					for (int i = targetParameters.length, extraLength = this.parameters.length; i < extraLength; i++) {
//						TypeBinding parameter = this.parameters[i];
//						TypeBinding leafParameterType = parameter.leafComponentType();
//						if ((leafParameterType.tagBits & TagBits.ContainsNestedTypeReferences) != 0) {
//							Util.recordNestedType(classFile, leafParameterType);
//						}
//					}
//				}
				if (this.returnType != null) {
					TypeBinding ret = this.returnType.leafComponentType();
					if ((ret.tagBits & TagBits.ContainsNestedTypeReferences) != 0) {
						Util.recordNestedType(classFile, ret);
					}
				}
			}
			return this.signature;
		}
	
		StringBuffer buffer = new StringBuffer((this.parameters.length + 1) * 20);
		buffer.append('(');
	
		TypeBinding[] targetParameters = this.parameters;
//		boolean isConstructor = isConstructor();
//		if (isConstructor && this.declaringClass.isEnum()) { // insert String name,int ordinal
//			buffer.append(ConstantPool.JavaLangStringSignature);
//			buffer.append(TypeBinding.INT.signature());
//		}
//		boolean needSynthetics = isConstructor && this.declaringClass.isNestedType();
//		if (needSynthetics) {
//			// take into account the synthetic argument type signatures as well
//			ReferenceBinding[] syntheticArgumentTypes = this.declaringClass.syntheticEnclosingInstanceTypes();
//			if (syntheticArgumentTypes != null) {
//				for (int i = 0, count = syntheticArgumentTypes.length; i < count; i++) {
//					ReferenceBinding syntheticArgumentType = syntheticArgumentTypes[i];
//					if ((syntheticArgumentType.tagBits & TagBits.ContainsNestedTypeReferences) != 0) {
//						this.tagBits |= TagBits.ContainsNestedTypeReferences;
//						Util.recordNestedType(classFile, syntheticArgumentType);
//					}
//					buffer.append(syntheticArgumentType.signature());
//				}
//			}
//	
//			if (this instanceof SyntheticMethodBinding) {
//				targetParameters = ((SyntheticMethodBinding)this).targetMethod.parameters;
//			}
//		}
	
		if (targetParameters != Binding.NO_PARAMETERS) {
			for (int i = 0, max = targetParameters.length; i < max; i++) {
				TypeBinding targetParameter = targetParameters[i];
				TypeBinding leafTargetParameterType = targetParameter.leafComponentType();
				if ((leafTargetParameterType.tagBits & TagBits.ContainsNestedTypeReferences) != 0) {
					this.tagBits |= TagBits.ContainsNestedTypeReferences;
					Util.recordNestedType(classFile, leafTargetParameterType);
				}
				buffer.append(targetParameter.signature());
			}
		}
//		if (needSynthetics) {
//			SyntheticArgumentBinding[] syntheticOuterArguments = this.declaringClass.syntheticOuterLocalVariables();
//			int count = syntheticOuterArguments == null ? 0 : syntheticOuterArguments.length;
//			for (int i = 0; i < count; i++) {
//				buffer.append(syntheticOuterArguments[i].type.signature());
//			}
//			// move the extra padding arguments of the synthetic constructor invocation to the end
//			for (int i = targetParameters.length, extraLength = this.parameters.length; i < extraLength; i++) {
//				TypeBinding parameter = this.parameters[i];
//				TypeBinding leafParameterType = parameter.leafComponentType();
//				if ((leafParameterType.tagBits & TagBits.ContainsNestedTypeReferences) != 0) {
//					this.tagBits |= TagBits.ContainsNestedTypeReferences;
//					Util.recordNestedType(classFile, leafParameterType);
//				}
//				buffer.append(parameter.signature());
//			}
//		}
		buffer.append(')');
		if (this.returnType != null) {
			TypeBinding ret = this.returnType.leafComponentType();
			if ((ret.tagBits & TagBits.ContainsNestedTypeReferences) != 0) {
				this.tagBits |= TagBits.ContainsNestedTypeReferences;
				Util.recordNestedType(classFile, ret);
			}
			buffer.append(this.returnType.signature());
		}
		int nameLength = buffer.length();
		this.signature = new char[nameLength];
		buffer.getChars(0, nameLength, this.signature, 0);
	
		return this.signature;
	}
	
	
	public final int getAccessFlags() {
		return this.modifiers & ExtraCompilerModifiers.AccJustFlag;
	}
	
	public AnnotationBinding[] getAnnotations() {
		FieldBinding originalField = original();
		ReferenceBinding declaringClassBinding = originalField.declaringClass;
		if (declaringClassBinding == null) {
			return Binding.NO_ANNOTATIONS;
		}
		return declaringClassBinding.retrieveAnnotations(originalField);
	}
	
	/**
	 * Compute the tagbits for standard annotations. For source types, these could require
	 * lazily resolving corresponding annotation nodes, in case of forward references.
	 * @see org.summer.sdt.internal.compiler.lookup.Binding#getAnnotationTagBits()
	 */
	public long getAnnotationTagBits() {
		FieldBinding originalField = original();
		if ((originalField.tagBits & TagBits.AnnotationResolved) == 0 && originalField.declaringClass instanceof SourceTypeBinding) {
			ClassScope scope = ((SourceTypeBinding) originalField.declaringClass).scope;
			if (scope == null) { // synthetic fields do not have a scope nor any annotations
				this.tagBits |= (TagBits.AnnotationResolved | TagBits.DeprecatedAnnotationResolved);
				return 0;
			}
			TypeDeclaration typeDecl = scope.referenceContext;
			FieldDeclaration fieldDecl = typeDecl.declarationOf(originalField);
			if (fieldDecl != null) {
				MethodScope initializationScope = isStatic() ? typeDecl.staticInitializerScope : typeDecl.initializerScope;
				FieldBinding previousField = initializationScope.initializedField;
				int previousFieldID = initializationScope.lastVisibleFieldID;
				try {
					initializationScope.initializedField = originalField;
					initializationScope.lastVisibleFieldID = originalField.id;
					ASTNode.resolveAnnotations(initializationScope, fieldDecl.annotations, originalField);
				} finally {
					initializationScope.initializedField = previousField;
					initializationScope.lastVisibleFieldID = previousFieldID;
				}
			}
		}
		return originalField.tagBits;
	}
	
	public final boolean isDefault() {
		return !isPublic() && !isProtected() && !isPrivate();
	}
	/* Answer true if the receiver is a deprecated field
	*/
	
	/* Answer true if the receiver has default visibility
	*/
	
	public final boolean isDeprecated() {
		return (this.modifiers & ClassFileConstants.AccDeprecated) != 0;
	}
	/* Answer true if the receiver has private visibility
	*/
	
	public final boolean isPrivate() {
		return (this.modifiers & ClassFileConstants.AccPrivate) != 0;
	}
	/* Answer true if the receiver has private visibility or is enclosed by a class that does.
	*/
	
	public final boolean isOrEnclosedByPrivateType() {
		if ((this.modifiers & ClassFileConstants.AccPrivate) != 0)
			return true;
		return this.declaringClass != null && this.declaringClass.isOrEnclosedByPrivateType();
	}
	/* Answer true if the receiver has private visibility and is used locally
	*/
	
	public final boolean isProtected() {
		return (this.modifiers & ClassFileConstants.AccProtected) != 0;
	}
	/* Answer true if the receiver has public visibility
	*/
	
	public final boolean isPublic() {
		return (this.modifiers & ClassFileConstants.AccPublic) != 0;
	}
	/* Answer true if the receiver is a static field
	*/
	
	public final boolean isStatic() {
		return (this.modifiers & ClassFileConstants.AccStatic) != 0;
	}
	/* Answer true if the receiver is not defined in the source of the declaringClass
	*/
	
	public final boolean isSynthetic() {
		return (this.modifiers & ClassFileConstants.AccSynthetic) != 0;
	}
	/* Answer true if the receiver is a transient field
	*/
	
	public final boolean isTransient() {
		return (this.modifiers & ClassFileConstants.AccTransient) != 0;
	}
	/* Answer true if the receiver's declaring type is deprecated (or any of its enclosing types)
	*/
	
	public final boolean isUsed() {
		return (this.modifiers & ExtraCompilerModifiers.AccLocallyUsed) != 0 || this.compoundUseFlag > 0;
	}
	/* Answer true if the only use of this field is in compound assignment or post increment
	 */
	
	public final boolean isUsedOnlyInCompound() {
		return (this.modifiers & ExtraCompilerModifiers.AccLocallyUsed) == 0 && this.compoundUseFlag > 0;
	}
	/* Answer true if the receiver has protected visibility
	*/
	
	public final boolean isViewedAsDeprecated() {
		return (this.modifiers & (ClassFileConstants.AccDeprecated | ExtraCompilerModifiers.AccDeprecatedImplicitly)) != 0;
	}
	/* Answer true if the receiver is a volatile field
	*/
	
	public final boolean isVolatile() {
		return (this.modifiers & ClassFileConstants.AccVolatile) != 0;
	}
	
	public final int kind() {
		return FIELD;
	}
	/* Answer true if the receiver is visible to the invocationPackage.
	*/
	/**
	 * Returns the original field (as opposed to parameterized instances)
	 */
	public FieldBinding original() {
		return this;
	}
	public void setAnnotations(AnnotationBinding[] annotations) {
		this.declaringClass.storeAnnotations(this, annotations);
	}
	public FieldDeclaration sourceField() {
		SourceTypeBinding sourceType;
		try {
			sourceType = (SourceTypeBinding) this.declaringClass;
		} catch (ClassCastException e) {
			return null;
		}
	
		FieldDeclaration[] fields = sourceType.scope.referenceContext.fields;
		if (fields != null) {
			for (int i = fields.length; --i >= 0;)
				if (this == fields[i].binding)
					return fields[i];
		}
		return null;
	}
}
