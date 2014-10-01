package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.core.compiler.IProblem;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.flow.FlowContext;
import org.summer.sdt.internal.compiler.flow.FlowInfo;
import org.summer.sdt.internal.compiler.impl.CompilerOptions;
import org.summer.sdt.internal.compiler.lookup.Binding;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.CompilationUnitScope;
import org.summer.sdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.summer.sdt.internal.compiler.lookup.FieldBinding;
import org.summer.sdt.internal.compiler.lookup.LocalTypeBinding;
import org.summer.sdt.internal.compiler.lookup.MethodBinding;
import org.summer.sdt.internal.compiler.lookup.NestedTypeBinding;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.SourceTypeBinding;
import org.summer.sdt.internal.compiler.lookup.TagBits;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeConstants;
import org.summer.sdt.internal.compiler.lookup.TypeIds;
import org.summer.sdt.internal.compiler.lookup.TypeVariableBinding;
import org.summer.sdt.internal.compiler.problem.AbortType;
import org.summer.sdt.internal.compiler.problem.ProblemReporter;
import org.summer.sdt.internal.compiler.problem.ProblemSeverities;
import org.summer.sdt.internal.compiler.util.Util;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public class ObjectElement extends Element {
	
	@Override
	protected void printTagName(int indent, StringBuffer output) {
		output.append(type.getLastToken());
	}

	@Override
	public FlowInfo analyseCode(BlockScope currentScope,
			FlowContext flowContext, FlowInfo flowInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resolve(BlockScope scope) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Resolve a top level type declaration
	 */
	public void resolve(CompilationUnitScope upperScope) {
		// top level : scope are already created
		resolve();
	}
	
	public void resolve(){
		SourceTypeBinding sourceType = this.binding;
		if (sourceType == null) {
			this.ignoreFurtherInvestigation = true;
			return;
		}
		try {
			boolean old = this.staticInitializerScope.insideTypeAnnotation;
			try {
				this.staticInitializerScope.insideTypeAnnotation = true;
				resolveAnnotations(this.staticInitializerScope, this.annotations, sourceType);
			} finally {
				this.staticInitializerScope.insideTypeAnnotation = old;
			}
			// check @Deprecated annotation
			long annotationTagBits = sourceType.getAnnotationTagBits();
			if ((annotationTagBits & TagBits.AnnotationDeprecated) == 0
					&& (sourceType.modifiers & ClassFileConstants.AccDeprecated) != 0
					&& this.scope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5) {
				this.scope.problemReporter().missingDeprecatedAnnotationForType(this);
			}
			if ((annotationTagBits & TagBits.AnnotationFunctionalInterface) != 0) {
				if(!this.binding.isFunctionalInterface(this.scope)) {
					this.scope.problemReporter().notAFunctionalInterface(this);
				}
			}
			if (this.scope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_8) {
				if ((annotationTagBits & TagBits.AnnotationNullMASK) != 0) {
					for (int i = 0; i < this.annotations.length; i++) {
						ReferenceBinding annotationType = this.annotations[i].getCompilerAnnotation().getAnnotationType();
						if (annotationType != null) {
							if (annotationType.id == TypeIds.T_ConfiguredAnnotationNonNull
									|| annotationType.id == TypeIds.T_ConfiguredAnnotationNullable)
							this.scope.problemReporter().nullAnnotationUnsupportedLocation(this.annotations[i]);
							sourceType.tagBits &= ~TagBits.AnnotationNullMASK;
						}
					}
				}
			}
	
			if ((this.bits & ASTNode.UndocumentedEmptyBlock) != 0) {
				this.scope.problemReporter().undocumentedEmptyBlock(this.bodyStart-1, this.bodyEnd);
			}
			boolean needSerialVersion =
							this.scope.compilerOptions().getSeverity(CompilerOptions.MissingSerialVersion) != ProblemSeverities.Ignore
							&& sourceType.isClass()
							&& sourceType.findSuperTypeOriginatingFrom(TypeIds.T_JavaIoExternalizable, false /*Externalizable is not a class*/) == null
							&& sourceType.findSuperTypeOriginatingFrom(TypeIds.T_JavaIoSerializable, false /*Serializable is not a class*/) != null;
	
			if (needSerialVersion) {
				// if Object writeReplace() throws java.io.ObjectStreamException is present, then no serialVersionUID is needed
				// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=101476
				CompilationUnitScope compilationUnitScope = this.scope.compilationUnitScope();
				MethodBinding methodBinding = sourceType.getExactMethod(TypeConstants.WRITEREPLACE, Binding.NO_TYPES, compilationUnitScope);
				ReferenceBinding[] throwsExceptions;
				needSerialVersion =
					methodBinding == null
						|| !methodBinding.isValidBinding()
						|| methodBinding.returnType.id != TypeIds.T_JavaLangObject
						|| (throwsExceptions = methodBinding.thrownExceptions).length != 1
						|| throwsExceptions[0].id != TypeIds.T_JavaIoObjectStreamException;
				if (needSerialVersion) {
					// check the presence of an implementation of the methods
					// private void writeObject(java.io.ObjectOutputStream out) throws IOException
					// private void readObject(java.io.ObjectInputStream out) throws IOException
					boolean hasWriteObjectMethod = false;
					boolean hasReadObjectMethod = false;
					TypeBinding argumentTypeBinding = this.scope.getType(TypeConstants.JAVA_IO_OBJECTOUTPUTSTREAM, 3);
					if (argumentTypeBinding.isValidBinding()) {
						methodBinding = sourceType.getExactMethod(TypeConstants.WRITEOBJECT, new TypeBinding[] { argumentTypeBinding }, compilationUnitScope);
						hasWriteObjectMethod = methodBinding != null
								&& methodBinding.isValidBinding()
								&& methodBinding.modifiers == ClassFileConstants.AccPrivate
								&& methodBinding.returnType == TypeBinding.VOID
								&& (throwsExceptions = methodBinding.thrownExceptions).length == 1
								&& throwsExceptions[0].id == TypeIds.T_JavaIoException;
					}
					argumentTypeBinding = this.scope.getType(TypeConstants.JAVA_IO_OBJECTINPUTSTREAM, 3);
					if (argumentTypeBinding.isValidBinding()) {
						methodBinding = sourceType.getExactMethod(TypeConstants.READOBJECT, new TypeBinding[] { argumentTypeBinding }, compilationUnitScope);
						hasReadObjectMethod = methodBinding != null
								&& methodBinding.isValidBinding()
								&& methodBinding.modifiers == ClassFileConstants.AccPrivate
								&& methodBinding.returnType == TypeBinding.VOID
								&& (throwsExceptions = methodBinding.thrownExceptions).length == 1
								&& throwsExceptions[0].id == TypeIds.T_JavaIoException;
					}
					needSerialVersion = !hasWriteObjectMethod || !hasReadObjectMethod;
				}
			}
			// generics (and non static generic members) cannot extend Throwable
			if (sourceType.findSuperTypeOriginatingFrom(TypeIds.T_JavaLangThrowable, true) != null) {
				ReferenceBinding current = sourceType;
				checkEnclosedInGeneric : do {
					if (current.isGenericType()) {
						this.scope.problemReporter().genericTypeCannotExtendThrowable(this);
						break checkEnclosedInGeneric;
					}
					if (current.isStatic()) break checkEnclosedInGeneric;
					if (current.isLocalType()) {
						NestedTypeBinding nestedType = (NestedTypeBinding) current.erasure();
						if (nestedType.scope.methodScope().isStatic) break checkEnclosedInGeneric;
					}
				} while ((current = current.enclosingType()) != null);
			}
			// this.maxFieldCount might already be set
			int localMaxFieldCount = 0;
			int lastVisibleFieldID = -1;
			boolean hasEnumConstants = false;
			FieldDeclaration[] enumConstantsWithoutBody = null;
	
			if (this.memberTypes != null) {
				for (int i = 0, count = this.memberTypes.length; i < count; i++) {
					this.memberTypes[i].resolve(this.scope);
				}
			}
			if (this.fields != null) {
				for (int i = 0, count = this.fields.length; i < count; i++) {
					FieldDeclaration field = this.fields[i];
					switch(field.getKind()) {
						case AbstractVariableDeclaration.ENUM_CONSTANT:
							hasEnumConstants = true;
							if (!(field.initialization instanceof QualifiedAllocationExpression)) {
								if (enumConstantsWithoutBody == null)
									enumConstantsWithoutBody = new FieldDeclaration[count];
								enumConstantsWithoutBody[i] = field;
							}
							//$FALL-THROUGH$
						case AbstractVariableDeclaration.FIELD:
							FieldBinding fieldBinding = field.binding;
							if (fieldBinding == null) {
								// still discover secondary errors
								if (field.initialization != null) field.initialization.resolve(field.isStatic() ? this.staticInitializerScope : this.initializerScope);
								this.ignoreFurtherInvestigation = true;
								continue;
							}
							if (needSerialVersion
									&& ((fieldBinding.modifiers & (ClassFileConstants.AccStatic | ClassFileConstants.AccFinal)) == (ClassFileConstants.AccStatic | ClassFileConstants.AccFinal))
									&& CharOperation.equals(TypeConstants.SERIALVERSIONUID, fieldBinding.name)
									&& TypeBinding.equalsEquals(TypeBinding.LONG, fieldBinding.type)) {
								needSerialVersion = false;
							}
							localMaxFieldCount++;
							lastVisibleFieldID = field.binding.id;
							break;
	
						case AbstractVariableDeclaration.INITIALIZER:
							 ((Initializer) field).lastVisibleFieldID = lastVisibleFieldID + 1;
							break;
					}
					field.resolve(field.isStatic() ? this.staticInitializerScope : this.initializerScope);
				}
			}
			if (this.maxFieldCount < localMaxFieldCount) {
				this.maxFieldCount = localMaxFieldCount;
			}
			if (needSerialVersion) {
				//check that the current type doesn't extend javax.rmi.CORBA.Stub
				TypeBinding javaxRmiCorbaStub = this.scope.getType(TypeConstants.JAVAX_RMI_CORBA_STUB, 4);
				if (javaxRmiCorbaStub.isValidBinding()) {
					ReferenceBinding superclassBinding = this.binding.superclass;
					loop: while (superclassBinding != null) {
						if (TypeBinding.equalsEquals(superclassBinding, javaxRmiCorbaStub)) {
							needSerialVersion = false;
							break loop;
						}
						superclassBinding = superclassBinding.superclass();
					}
				}
				if (needSerialVersion) {
					this.scope.problemReporter().missingSerialVersion(this);
				}
			}
	
			// check extends/implements for annotation type
			switch(kind(this.modifiers)) {
				case TypeDeclaration.ANNOTATION_TYPE_DECL :
					if (this.superclass != null) {
						this.scope.problemReporter().annotationTypeDeclarationCannotHaveSuperclass(this);
					}
					if (this.superInterfaces != null) {
						this.scope.problemReporter().annotationTypeDeclarationCannotHaveSuperinterfaces(this);
					}
					break;
				case TypeDeclaration.ENUM_DECL :
					// check enum abstract methods
					if (this.binding.isAbstract()) {
						if (!hasEnumConstants) {
							for (int i = 0, count = this.methods.length; i < count; i++) {
								final AbstractMethodDeclaration methodDeclaration = this.methods[i];
								if (methodDeclaration.isAbstract() && methodDeclaration.binding != null)
									this.scope.problemReporter().enumAbstractMethodMustBeImplemented(methodDeclaration);
							}
						} else if (enumConstantsWithoutBody != null) {
							for (int i = 0, count = this.methods.length; i < count; i++) {
								final AbstractMethodDeclaration methodDeclaration = this.methods[i];
								if (methodDeclaration.isAbstract() && methodDeclaration.binding != null) {
									for (int f = 0, l = enumConstantsWithoutBody.length; f < l; f++)
										if (enumConstantsWithoutBody[f] != null)
											this.scope.problemReporter().enumConstantMustImplementAbstractMethod(methodDeclaration, enumConstantsWithoutBody[f]);
								}
							}
						}
					}
					break;
			}
	
			int missingAbstractMethodslength = this.missingAbstractMethods == null ? 0 : this.missingAbstractMethods.length;
			int methodsLength = this.methods == null ? 0 : this.methods.length;
			if ((methodsLength + missingAbstractMethodslength) > 0xFFFF) {
				this.scope.problemReporter().tooManyMethods(this);
			}
			if (this.methods != null) {
				for (int i = 0, count = this.methods.length; i < count; i++) {
					this.methods[i].resolve(this.scope);
				}
			}
			// Resolve javadoc
			if (this.javadoc != null) {
				if (this.scope != null && (this.name != TypeConstants.PACKAGE_INFO_NAME)) {
					// if the type is package-info, the javadoc was resolved as part of the compilation unit javadoc
					this.javadoc.resolve(this.scope);
				}
			} else if (!sourceType.isLocalType()) {
				// Set javadoc visibility
				int visibility = sourceType.modifiers & ExtraCompilerModifiers.AccVisibilityMASK;
				ProblemReporter reporter = this.scope.problemReporter();
				int severity = reporter.computeSeverity(IProblem.JavadocMissing);
				if (severity != ProblemSeverities.Ignore) {
					if (this.enclosingType != null) {
						visibility = Util.computeOuterMostVisibility(this.enclosingType, visibility);
					}
					int javadocModifiers = (this.binding.modifiers & ~ExtraCompilerModifiers.AccVisibilityMASK) | visibility;
					reporter.javadocMissing(this.sourceStart, this.sourceEnd, severity, javadocModifiers);
				}
			}
		} catch (AbortType e) {
			this.ignoreFurtherInvestigation = true;
			return;
		}
		}
	}

}
