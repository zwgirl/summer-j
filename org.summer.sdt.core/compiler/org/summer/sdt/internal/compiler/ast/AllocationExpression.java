/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Stephan Herrmann - Contributions for
 *     						bug 236385 - [compiler] Warn for potential programming problem if an object is created but not used
 *     						bug 319201 - [null] no warning when unboxing SingleNameReference causes NPE
 *     						bug 349326 - [1.7] new warning for missing try-with-resources
 * 							bug 186342 - [compiler][null] Using annotations for null checking
 *							bug 358903 - Filter practically unimportant resource leak warnings
 *							bug 368546 - [compiler][resource] Avoid remaining false positives found when compiling the Eclipse SDK
 *							bug 370639 - [compiler][resource] restore the default for resource leak warnings
 *							bug 345305 - [compiler][null] Compiler misidentifies a case of "variable can only be null"
 *							bug 388996 - [compiler][resource] Incorrect 'potential resource leak'
 *							bug 403147 - [compiler][null] FUP of bug 400761: consolidate interaction between unboxing, NPE, and deferred checking
 *							Bug 392238 - [1.8][compiler][null] Detect semantically invalid null type annotations
 *							Bug 417295 - [1.8[[null] Massage type annotated null analysis to gel well with deep encoded type bindings.
 *							Bug 400874 - [1.8][compiler] Inference infrastructure should evolve to meet JLS8 18.x (Part G of JSR335 spec)
 *							Bug 424727 - [compiler][null] NullPointerException in nullAnnotationUnsupportedLocation(ProblemReporter.java:5708)
 *							Bug 424710 - [1.8][compiler] CCE in SingleNameReference.localVariableBinding
 *							Bug 425152 - [1.8] [compiler] Lambda Expression not resolved but flow analyzed leading to NPE.
 *							Bug 424205 - [1.8] Cannot infer type for diamond type with lambda on method invocation
 *							Bug 424415 - [1.8][compiler] Eventual resolution of ReferenceExpression is not seen to be happening.
 *							Bug 426366 - [1.8][compiler] Type inference doesn't handle multiple candidate target types in outer overload context
 *							Bug 426290 - [1.8][compiler] Inference + overloading => wrong method resolution ?
 *							Bug 426764 - [1.8] Presence of conditional expression as method argument confuses compiler
 *							Bug 424930 - [1.8][compiler] Regression: "Cannot infer type arguments" error from compiler.
 *							Bug 427483 - [Java 8] Variables in lambdas sometimes can't be resolved
 *							Bug 427438 - [1.8][compiler] NPE at org.summer.sdt.internal.compiler.ast.ConditionalExpression.generateCode(ConditionalExpression.java:280)
 *							Bug 426996 - [1.8][inference] try to avoid method Expression.unresolve()? 
 *							Bug 428352 - [1.8][compiler] Resolution errors don't always surface
 *							Bug 429203 - [1.8][compiler] NPE in AllocationExpression.binding
 *							Bug 429430 - [1.8] Lambdas and method reference infer wrong exception type with generics (RuntimeException instead of IOException)
 *							Bug 434297 - [1.8] NPE in LamdaExpression.analyseCode with lamda expression nested in a conditional expression
 *     Jesper S Moller <jesper@selskabet.org> - Contributions for
 *							bug 378674 - "The method can be declared as static" is wrong
 *     Andy Clement (GoPivotal, Inc) aclement@gopivotal.com - Contributions for
 *                          Bug 383624 - [1.8][compiler] Revive code generation support for type annotations (from Olivier's work)
 *                          Bug 409245 - [1.8][compiler] Type annotations dropped when call is routed through a synthetic bridge method
 *     Till Brychcy - Contributions for
 *     						bug 413460 - NonNullByDefault is not inherited to Constructors when accessed via Class File
 *******************************************************************************/
package org.summer.sdt.internal.compiler.ast;

import static org.summer.sdt.internal.compiler.ast.ExpressionContext.ASSIGNMENT_CONTEXT;
import static org.summer.sdt.internal.compiler.ast.ExpressionContext.INVOCATION_CONTEXT;
import static org.summer.sdt.internal.compiler.ast.ExpressionContext.VANILLA_CONTEXT;

import java.util.HashMap;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.core.compiler.IProblem;
import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.codegen.Opcodes;
import org.summer.sdt.internal.compiler.flow.FlowContext;
import org.summer.sdt.internal.compiler.flow.FlowInfo;
import org.summer.sdt.internal.compiler.impl.CompilerOptions;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.javascript.Dependency;
import org.summer.sdt.internal.compiler.javascript.JsConstant;
import org.summer.sdt.internal.compiler.lookup.Binding;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.summer.sdt.internal.compiler.lookup.ImplicitNullAnnotationVerifier;
import org.summer.sdt.internal.compiler.lookup.InferenceContext18;
import org.summer.sdt.internal.compiler.lookup.LocalTypeBinding;
import org.summer.sdt.internal.compiler.lookup.LocalVariableBinding;
import org.summer.sdt.internal.compiler.lookup.MethodBinding;
import org.summer.sdt.internal.compiler.lookup.NestedTypeBinding;
import org.summer.sdt.internal.compiler.lookup.ParameterizedGenericMethodBinding;
import org.summer.sdt.internal.compiler.lookup.ParameterizedMethodBinding;
import org.summer.sdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.summer.sdt.internal.compiler.lookup.PolyTypeBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemMethodBinding;
import org.summer.sdt.internal.compiler.lookup.RawTypeBinding;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.SourceTypeBinding;
import org.summer.sdt.internal.compiler.lookup.SyntheticArgumentBinding;
import org.summer.sdt.internal.compiler.lookup.SyntheticFactoryMethodBinding;
import org.summer.sdt.internal.compiler.lookup.TagBits;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeConstants;
import org.summer.sdt.internal.compiler.lookup.TypeIds;
import org.summer.sdt.internal.compiler.lookup.TypeVariableBinding;
import org.summer.sdt.internal.compiler.problem.ProblemSeverities;
import org.summer.sdt.internal.compiler.util.SimpleLookupTable;

public class AllocationExpression extends Expression implements Invocation {

	public TypeReference type;
	public Expression[] arguments;
	public MethodBinding binding;							// exact binding resulting from lookup
	MethodBinding syntheticAccessor;						// synthetic accessor for inner-emulation
	public TypeReference[] typeArguments;
	public TypeBinding[] genericTypeArguments;
	public FieldDeclaration enumConstant; // for enum constant initializations
	protected TypeBinding typeExpected;	  // for <> inference
	public boolean inferredReturnType;

	public FakedTrackingVariable closeTracker;	// when allocation a Closeable store a pre-liminary tracking variable here
	public ExpressionContext expressionContext = VANILLA_CONTEXT;

	 // hold on to this context from invocation applicability inference until invocation type inference (per method candidate):
	private SimpleLookupTable/*<PMB,IC18>*/ inferenceContexts;
	public HashMap<TypeBinding, MethodBinding> solutionsPerTargetType;
	private InferenceContext18 outerInferenceContext; // resolving within the context of an outer (lambda) inference?
	public boolean argsContainCast;
	public TypeBinding[] argumentTypes = Binding.NO_PARAMETERS;
	public boolean argumentsHaveErrors = false;
	
	public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
		// check captured variables are initialized in current context (26134)
		checkCapturedLocalInitializationIfNecessary((ReferenceBinding)this.binding.declaringClass.erasure(), currentScope, flowInfo);
	
		// process arguments
		if (this.arguments != null) {
			boolean analyseResources = currentScope.compilerOptions().analyseResourceLeaks;
			boolean hasResourceWrapperType = analyseResources 
					&& this.resolvedType instanceof ReferenceBinding 
					&& ((ReferenceBinding)this.resolvedType).hasTypeBit(TypeIds.BitWrapperCloseable);
			for (int i = 0, count = this.arguments.length; i < count; i++) {
				flowInfo =
					this.arguments[i]
						.analyseCode(currentScope, flowContext, flowInfo)
						.unconditionalInits();
				// if argument is an AutoCloseable insert info that it *may* be closed (by the target method, i.e.)
				if (analyseResources && !hasResourceWrapperType) { // allocation of wrapped closeables is analyzed specially
					flowInfo = FakedTrackingVariable.markPassedToOutside(currentScope, this.arguments[i], flowInfo, flowContext, false);
				}
				this.arguments[i].checkNPEbyUnboxing(currentScope, flowContext, flowInfo);
			}
			analyseArguments(currentScope, flowContext, flowInfo, this.binding, this.arguments);
		}
	
		// record some dependency information for exception types
		ReferenceBinding[] thrownExceptions;
		if (((thrownExceptions = this.binding.thrownExceptions).length) != 0) {
			if ((this.bits & ASTNode.Unchecked) != 0 && this.genericTypeArguments == null) {
				// https://bugs.eclipse.org/bugs/show_bug.cgi?id=277643, align with javac on JLS 15.12.2.6
				thrownExceptions = currentScope.environment().convertToRawTypes(this.binding.thrownExceptions, true, true);
			}		
			// check exception handling
			flowContext.checkExceptionHandlers(
				thrownExceptions,
				this,
				flowInfo.unconditionalCopy(),
				currentScope);
		}
	
		// after having analysed exceptions above start tracking newly allocated resource:
		if (currentScope.compilerOptions().analyseResourceLeaks && FakedTrackingVariable.isAnyCloseable(this.resolvedType))
			FakedTrackingVariable.analyseCloseableAllocation(currentScope, flowInfo, this);
	
		if (this.binding.declaringClass.isMemberType() && !this.binding.declaringClass.isStatic()) {
			// allocating a non-static member type without an enclosing instance of parent type
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=335845
			currentScope.tagAsAccessingEnclosingInstanceStateOf(this.binding.declaringClass.enclosingType(), false /* type variable access */);
			// Reviewed for https://bugs.eclipse.org/bugs/show_bug.cgi?id=378674 :
			// The corresponding problem (when called from static) is not produced until during code generation
		}
		manageEnclosingInstanceAccessIfNecessary(currentScope, flowInfo);
		manageSyntheticAccessIfNecessary(currentScope, flowInfo);
	
		// account for possible exceptions thrown by the constructor
		flowContext.recordAbruptExit(); // TODO whitelist of ctors that cannot throw any exc.??
	
		return flowInfo;
	}
	
	public void checkCapturedLocalInitializationIfNecessary(ReferenceBinding checkedType, BlockScope currentScope, FlowInfo flowInfo) {
		if (((checkedType.tagBits & ( TagBits.AnonymousTypeMask|TagBits.LocalTypeMask)) == TagBits.LocalTypeMask)
				&& !currentScope.isDefinedInType(checkedType)) { // only check external allocations
			NestedTypeBinding nestedType = (NestedTypeBinding) checkedType;
			SyntheticArgumentBinding[] syntheticArguments = nestedType.syntheticOuterLocalVariables();
			if (syntheticArguments != null)
				for (int i = 0, count = syntheticArguments.length; i < count; i++){
					SyntheticArgumentBinding syntheticArgument = syntheticArguments[i];
					LocalVariableBinding targetLocal;
					if ((targetLocal = syntheticArgument.actualOuterLocalVariable) == null) continue;
					if (targetLocal.declaration != null && !flowInfo.isDefinitelyAssigned(targetLocal)){
						currentScope.problemReporter().uninitializedLocalVariable(targetLocal, this);
					}
				}
		}
	}
	
	public Expression enclosingInstance() {
		return null;
	}
	
	public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
		if (!valueRequired)
			currentScope.problemReporter().unusedObjectAllocation(this);
	
		int pc = codeStream.position;
		MethodBinding codegenBinding = this.binding.original();
		ReferenceBinding allocatedType = codegenBinding.declaringClass;
	
		codeStream.new_(this.type, allocatedType);
		boolean isUnboxing = (this.implicitConversion & TypeIds.UNBOXING) != 0;
		if (valueRequired || isUnboxing) {
			codeStream.dup();
		}
		// better highlight for allocation: display the type individually
		if (this.type != null) { // null for enum constant body
			codeStream.recordPositionsFrom(pc, this.type.sourceStart);
		} else {
			// push enum constant name and ordinal
			codeStream.ldc(String.valueOf(this.enumConstant.name));
			codeStream.generateInlinedValue(this.enumConstant.binding.id);
		}
	
		// handling innerclass instance allocation - enclosing instance arguments
		if (allocatedType.isNestedType()) {
			codeStream.generateSyntheticEnclosingInstanceValues(
				currentScope,
				allocatedType,
				enclosingInstance(),
				this);
		}
		// generate the arguments for constructor
		generateArguments(this.binding, this.arguments, currentScope, codeStream);
		// handling innerclass instance allocation - outer local arguments
		if (allocatedType.isNestedType()) {
			codeStream.generateSyntheticOuterArgumentValues(
				currentScope,
				allocatedType,
				this);
		}
		// invoke constructor
		if (this.syntheticAccessor == null) {
			codeStream.invoke(Opcodes.OPC_invokespecial, codegenBinding, null /* default declaringClass */, this.typeArguments);
		} else {
			// synthetic accessor got some extra arguments appended to its signature, which need values
			for (int i = 0,
				max = this.syntheticAccessor.parameters.length - codegenBinding.parameters.length;
				i < max;
				i++) {
				codeStream.aconst_null();
			}
			codeStream.invoke(Opcodes.OPC_invokespecial, this.syntheticAccessor, null /* default declaringClass */, this.typeArguments);
		}
		if (valueRequired) {
			codeStream.generateImplicitConversion(this.implicitConversion);
		} else if (isUnboxing) {
			// conversion only generated if unboxing
			codeStream.generateImplicitConversion(this.implicitConversion);
			switch (postConversionType(currentScope).id) {
				case T_long :
				case T_double :
					codeStream.pop2();
					break;
				default :
					codeStream.pop();
			}
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}
	
	/**
	 * @see org.summer.sdt.internal.compiler.lookup.InvocationSite#genericTypeArguments()
	 */
	public TypeBinding[] genericTypeArguments() {
		return this.genericTypeArguments;
	}
	
	public boolean isSuperAccess() {
		return false;
	}
	
	public boolean isTypeAccess() {
		return true;
	}
	
	/* Inner emulation consists in either recording a dependency
	 * link only, or performing one level of propagation.
	 *
	 * Dependency mechanism is used whenever dealing with source target
	 * types, since by the time we reach them, we might not yet know their
	 * exact need.
	 */
	public void manageEnclosingInstanceAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo) {
		if ((flowInfo.tagBits & FlowInfo.UNREACHABLE_OR_DEAD) != 0) return;
		ReferenceBinding allocatedTypeErasure = (ReferenceBinding) this.binding.declaringClass.erasure();
	
		// perform some emulation work in case there is some and we are inside a local type only
		if (allocatedTypeErasure.isNestedType()
			&& (currentScope.enclosingSourceType().isLocalType() || currentScope.isLambdaScope())) {
	
			if (allocatedTypeErasure.isLocalType()) {
				((LocalTypeBinding) allocatedTypeErasure).addInnerEmulationDependent(currentScope, false);
				// request cascade of accesses
			} else {
				// locally propagate, since we already now the desired shape for sure
				currentScope.propagateInnerEmulation(allocatedTypeErasure, false);
				// request cascade of accesses
			}
		}
	}
	
	public void manageSyntheticAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo) {
		if ((flowInfo.tagBits & FlowInfo.UNREACHABLE_OR_DEAD) != 0) return;
		// if constructor from parameterized type got found, use the original constructor at codegen time
		MethodBinding codegenBinding = this.binding.original();
	
		ReferenceBinding declaringClass;
		if (codegenBinding.isPrivate() && TypeBinding.notEquals(currentScope.enclosingSourceType(), (declaringClass = codegenBinding.declaringClass))) {
	
			// from 1.4 on, local type constructor can lose their private flag to ease emulation
			if ((declaringClass.tagBits & TagBits.IsLocalType) != 0 && currentScope.compilerOptions().complianceLevel >= ClassFileConstants.JDK1_4) {
				// constructor will not be dumped as private, no emulation required thus
				codegenBinding.tagBits |= TagBits.ClearPrivateModifier;
			} else {
				this.syntheticAccessor = ((SourceTypeBinding) declaringClass).addSyntheticMethod(codegenBinding, isSuperAccess());
				currentScope.problemReporter().needToEmulateMethodAccess(codegenBinding, this);
			}
		}
	}
	
	public StringBuffer printExpression(int indent, StringBuffer output) {
		if (this.type != null) { // type null for enum constant initializations
			output.append("new "); //$NON-NLS-1$
		}
		if (this.typeArguments != null) {
			output.append('<');
			int max = this.typeArguments.length - 1;
			for (int j = 0; j < max; j++) {
				this.typeArguments[j].print(0, output);
				output.append(", ");//$NON-NLS-1$
			}
			this.typeArguments[max].print(0, output);
			output.append('>');
		}
		if (this.type != null) { // type null for enum constant initializations
			this.type.printExpression(0, output);
		}
		output.append('(');
		if (this.arguments != null) {
			for (int i = 0; i < this.arguments.length; i++) {
				if (i > 0) output.append(", "); //$NON-NLS-1$
				this.arguments[i].printExpression(0, output);
			}
		}
		return output.append(')');
	}
	
	public TypeBinding resolveType(BlockScope scope) {
		// Propagate the type checking to the arguments, and check if the constructor is defined.
		final boolean isDiamond = this.type != null && (this.type.bits & ASTNode.IsDiamond) != 0;
		final CompilerOptions compilerOptions = scope.compilerOptions();
		long sourceLevel = compilerOptions.sourceLevel;
		if (this.constant != Constant.NotAConstant) {
			this.constant = Constant.NotAConstant;
			if (this.type == null) {
				// initialization of an enum constant
				this.resolvedType = scope.enclosingReceiverType();
			} else {
				this.resolvedType = this.type.resolveType(scope, true /* check bounds*/);
			}
			if (this.type != null) {
				checkIllegalNullAnnotation(scope, this.resolvedType);
				checkParameterizedAllocation: {
					if (this.type instanceof ParameterizedQualifiedTypeReference) { // disallow new X<String>.Y<Integer>()
						ReferenceBinding currentType = (ReferenceBinding)this.resolvedType;
						if (currentType == null) return currentType;
						do {
							// isStatic() is answering true for toplevel types
							if ((currentType.modifiers & ClassFileConstants.AccStatic) != 0) break checkParameterizedAllocation;
							if (currentType.isRawType()) break checkParameterizedAllocation;
						} while ((currentType = currentType.enclosingType())!= null);
						ParameterizedQualifiedTypeReference qRef = (ParameterizedQualifiedTypeReference) this.type;
						for (int i = qRef.typeArguments.length - 2; i >= 0; i--) {
							if (qRef.typeArguments[i] != null) {
								scope.problemReporter().illegalQualifiedParameterizedTypeAllocation(this.type, this.resolvedType);
								break;
							}
						}
					}
				}
			}
			// will check for null after args are resolved
	
			// resolve type arguments (for generic constructor call)
			if (this.typeArguments != null) {
				int length = this.typeArguments.length;
				this.argumentsHaveErrors = sourceLevel < ClassFileConstants.JDK1_5;
				this.genericTypeArguments = new TypeBinding[length];
				for (int i = 0; i < length; i++) {
					TypeReference typeReference = this.typeArguments[i];
					if ((this.genericTypeArguments[i] = typeReference.resolveType(scope, true /* check bounds*/)) == null) {
						this.argumentsHaveErrors = true;
					}
					if (this.argumentsHaveErrors && typeReference instanceof Wildcard) {
						scope.problemReporter().illegalUsageOfWildcard(typeReference);
					}
				}
				if (isDiamond) {
					scope.problemReporter().diamondNotWithExplicitTypeArguments(this.typeArguments);
					return null;
				}
				if (this.argumentsHaveErrors) {
					if (this.arguments != null) { // still attempt to resolve arguments
						for (int i = 0, max = this.arguments.length; i < max; i++) {
							this.arguments[i].resolveType(scope);
						}
					}
					return null;
				}
			}
	
			// buffering the arguments' types
			if (this.arguments != null) {
				this.argumentsHaveErrors = false;
				int length = this.arguments.length;
				this.argumentTypes = new TypeBinding[length];
				for (int i = 0; i < length; i++) {
					Expression argument = this.arguments[i];
					if (argument instanceof CastExpression) {
						argument.bits |= DisableUnnecessaryCastCheck; // will check later on
						this.argsContainCast = true;
					}
					argument.setExpressionContext(INVOCATION_CONTEXT);
					if (this.arguments[i].resolvedType != null) 
						scope.problemReporter().genericInferenceError("Argument was unexpectedly found resolved", this); //$NON-NLS-1$
					if ((this.argumentTypes[i] = argument.resolveType(scope)) == null) {
						this.argumentsHaveErrors = true;
					}
				}
				if (this.argumentsHaveErrors) {
					/* https://bugs.eclipse.org/bugs/show_bug.cgi?id=345359, if arguments have errors, completely bail out in the <> case.
				   No meaningful type resolution is possible since inference of the elided types is fully tied to argument types. Do
				   not return the partially resolved type.
					 */
					if (isDiamond) {
						return null; // not the partially cooked this.resolvedType
					}
					if (this.resolvedType instanceof ReferenceBinding) {
						// record a best guess, for clients who need hint about possible constructor match
						TypeBinding[] pseudoArgs = new TypeBinding[length];
						for (int i = length; --i >= 0;) {
							pseudoArgs[i] = this.argumentTypes[i] == null ? TypeBinding.NULL : this.argumentTypes[i]; // replace args with errors with null type
						}
						this.binding = scope.findMethod((ReferenceBinding) this.resolvedType, TypeConstants.INIT, pseudoArgs, this, false);
						if (this.binding != null && !this.binding.isValidBinding()) {
							MethodBinding closestMatch = ((ProblemMethodBinding)this.binding).closestMatch;
							// record the closest match, for clients who may still need hint about possible method match
							if (closestMatch != null) {
								if (closestMatch.original().typeVariables != Binding.NO_TYPE_VARIABLES) { // generic method
									// shouldn't return generic method outside its context, rather convert it to raw method (175409)
									closestMatch = scope.environment().createParameterizedGenericMethod(closestMatch.original(), (RawTypeBinding)null);
								}
								this.binding = closestMatch;
								MethodBinding closestMatchOriginal = closestMatch.original();
								if (closestMatchOriginal.isOrEnclosedByPrivateType() && !scope.isDefinedInMethod(closestMatchOriginal)) {
									// ignore cases where method is used from within inside itself (e.g. direct recursions)
									closestMatchOriginal.modifiers |= ExtraCompilerModifiers.AccLocallyUsed;
								}
							}
						}
					}
					return this.resolvedType;
				}
			}
			if (this.resolvedType == null || !this.resolvedType.isValidBinding()) {
				return null;
			}
	
			// null type denotes fake allocation for enum constant inits
			if (this.type != null && !this.resolvedType.canBeInstantiated()) {
				scope.problemReporter().cannotInstantiate(this.type, this.resolvedType);
				return this.resolvedType;
			}
		} 
		if (isDiamond) {
			this.binding = inferConstructorOfElidedParameterizedType(scope);
			if (this.binding == null || !this.binding.isValidBinding()) {
				scope.problemReporter().cannotInferElidedTypes(this);
				return this.resolvedType = null;
			}
			if (this.typeExpected == null && compilerOptions.sourceLevel >= ClassFileConstants.JDK1_8 && this.expressionContext.definesTargetType()) {
				return new PolyTypeBinding(this);
			}
			this.resolvedType = this.type.resolvedType = this.binding.declaringClass;
			resolvePolyExpressionArguments(this, this.binding, this.argumentTypes, scope);
		} else {
			this.binding = findConstructorBinding(scope, this, (ReferenceBinding) this.resolvedType, this.argumentTypes);
		}
		if (!this.binding.isValidBinding()) {
			if (this.binding.declaringClass == null) {
				this.binding.declaringClass = (ReferenceBinding) this.resolvedType;
			}
			if (this.type != null && !this.type.resolvedType.isValidBinding()) {
				return null;
			}
			scope.problemReporter().invalidConstructor(this, this.binding);
			return this.resolvedType;
		}
		if ((this.binding.tagBits & TagBits.HasMissingType) != 0) {
			scope.problemReporter().missingTypeInConstructor(this, this.binding);
		}
		if (isMethodUseDeprecated(this.binding, scope, true)) {
			scope.problemReporter().deprecatedMethod(this.binding, this);
		}
		if (checkInvocationArguments(scope, null, this.resolvedType, this.binding, this.arguments, this.argumentTypes, this.argsContainCast, this)) {
			this.bits |= ASTNode.Unchecked;
		}
		if (this.typeArguments != null && this.binding.original().typeVariables == Binding.NO_TYPE_VARIABLES) {
			scope.problemReporter().unnecessaryTypeArgumentsForMethodInvocation(this.binding, this.genericTypeArguments, this.typeArguments);
		}
		if (!isDiamond && this.resolvedType.isParameterizedTypeWithActualArguments()) {
	 		checkTypeArgumentRedundancy((ParameterizedTypeBinding) this.resolvedType, scope);
	 	}
		if (compilerOptions.isAnnotationBasedNullAnalysisEnabled && (this.binding.tagBits & TagBits.IsNullnessKnown) == 0) {
			new ImplicitNullAnnotationVerifier(scope.environment(), compilerOptions.inheritNullAnnotations)
					.checkImplicitNullAnnotations(this.binding, null/*srcMethod*/, false, scope);
		}
		return this.resolvedType;
	}
	
	/**
	 * Check if 'allocationType' illegally has a top-level null annotation.
	 */
	void checkIllegalNullAnnotation(BlockScope scope, TypeBinding allocationType) {
		if (allocationType != null) {
			// only check top-level null annotation (annots on details are OK):
			long nullTagBits = allocationType.tagBits & TagBits.AnnotationNullMASK;
			if (nullTagBits != 0) {
				Annotation annotation = this.type.findAnnotation(nullTagBits);
				if (annotation != null)
					scope.problemReporter().nullAnnotationUnsupportedLocation(annotation);
			}
		}
	}
	
	// For allocation expressions, boxing compatibility is same as vanilla compatibility, since java.lang's wrapper types are not generic.
	public boolean isBoxingCompatibleWith(TypeBinding targetType, Scope scope) {
		return isPolyExpression() ? false : isCompatibleWith(scope.boxing(targetType), scope);
	}
	
	public boolean isCompatibleWith(TypeBinding targetType, final Scope scope) {
		if (this.argumentsHaveErrors || this.binding == null || !this.binding.isValidBinding() || targetType == null || scope == null)
			return false;
		TypeBinding allocationType = this.resolvedType;
		if (isPolyExpression()) {
			TypeBinding originalExpectedType = this.typeExpected;
			try {
				MethodBinding method = this.solutionsPerTargetType != null ? this.solutionsPerTargetType.get(targetType) : null;
				if (method == null) {
					this.typeExpected = targetType;
					method = inferConstructorOfElidedParameterizedType(scope); // caches result already.
					if (method == null || !method.isValidBinding())
						return false;
				}
				allocationType = method.declaringClass;
			} finally {
				this.typeExpected = originalExpectedType;
			}
		}
		return allocationType != null && allocationType.isCompatibleWith(targetType, scope);
	}
	
	public MethodBinding inferConstructorOfElidedParameterizedType(final Scope scope) {
		if (this.typeExpected != null && this.binding != null) {
			MethodBinding cached = this.solutionsPerTargetType != null ? this.solutionsPerTargetType.get(this.typeExpected) : null;
			if (cached != null)
				return cached;
		}
		ReferenceBinding genericType = ((ParameterizedTypeBinding) this.resolvedType).genericType();
		ReferenceBinding enclosingType = this.resolvedType.enclosingType();
		ParameterizedTypeBinding allocationType = scope.environment().createParameterizedType(genericType, genericType.typeVariables(), enclosingType);
		
		// Given the allocation type and the arguments to the constructor, see if we can infer the constructor of the elided parameterized type.
		MethodBinding factory = scope.getStaticFactory(allocationType, enclosingType, this.argumentTypes, this);
		if (factory instanceof ParameterizedGenericMethodBinding && factory.isValidBinding()) {
			ParameterizedGenericMethodBinding genericFactory = (ParameterizedGenericMethodBinding) factory;
			this.inferredReturnType = genericFactory.inferredReturnType;
			SyntheticFactoryMethodBinding sfmb = (SyntheticFactoryMethodBinding) factory.original();
			TypeVariableBinding[] constructorTypeVariables = sfmb.getConstructor().typeVariables();
			TypeBinding [] constructorTypeArguments = constructorTypeVariables != null ? new TypeBinding[constructorTypeVariables.length] : Binding.NO_TYPES;
			if (constructorTypeArguments.length > 0)
				System.arraycopy(((ParameterizedGenericMethodBinding)factory).typeArguments, sfmb.typeVariables().length - constructorTypeArguments.length , 
													constructorTypeArguments, 0, constructorTypeArguments.length);
			MethodBinding constructor = sfmb.applyTypeArgumentsOnConstructor(((ParameterizedTypeBinding)factory.returnType).arguments, constructorTypeArguments);
			if (constructor instanceof ParameterizedGenericMethodBinding && scope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_8) {
				// force an inference context to be established for nested poly allocations (to be able to transfer b2), but avoid tunneling through overload resolution. We know this is the MSMB.
				if (this.expressionContext == INVOCATION_CONTEXT && this.typeExpected == null)
					constructor = ParameterizedGenericMethodBinding.computeCompatibleMethod18(constructor.shallowOriginal(), this.argumentTypes, scope, this);
			}
			if (this.typeExpected != null)
				registerResult(this.typeExpected, constructor);
			return constructor;
		}
		return null;
	}
	
	public TypeBinding[] inferElidedTypes(final Scope scope) {
		
		ReferenceBinding genericType = ((ParameterizedTypeBinding) this.resolvedType).genericType();
		ReferenceBinding enclosingType = this.resolvedType.enclosingType();
		ParameterizedTypeBinding allocationType = scope.environment().createParameterizedType(genericType, genericType.typeVariables(), enclosingType);
		
		/* Given the allocation type and the arguments to the constructor, see if we can synthesize a generic static factory
		   method that would, given the argument types and the invocation site, manufacture a parameterized object of type allocationType.
		   If we are successful then by design and construction, the parameterization of the return type of the factory method is identical
		   to the types elided in the <>.
		*/
		MethodBinding factory = scope.getStaticFactory(allocationType, enclosingType, this.argumentTypes, this);
		if (factory instanceof ParameterizedGenericMethodBinding && factory.isValidBinding()) {
			ParameterizedGenericMethodBinding genericFactory = (ParameterizedGenericMethodBinding) factory;
			this.inferredReturnType = genericFactory.inferredReturnType;
			return ((ParameterizedTypeBinding)factory.returnType).arguments;
		}
		return null;
	}
	
	public void checkTypeArgumentRedundancy(ParameterizedTypeBinding allocationType, final BlockScope scope) {
		if ((scope.problemReporter().computeSeverity(IProblem.RedundantSpecificationOfTypeArguments) == ProblemSeverities.Ignore) || scope.compilerOptions().sourceLevel < ClassFileConstants.JDK1_7) return;
		if (allocationType.arguments == null) return;  // raw binding
		if (this.genericTypeArguments != null) return; // diamond can't occur with explicit type args for constructor
		if (this.type == null) return;
		if (this.argumentTypes == Binding.NO_PARAMETERS && this.typeExpected instanceof ParameterizedTypeBinding) {
			ParameterizedTypeBinding expected = (ParameterizedTypeBinding) this.typeExpected;
			if (expected.arguments != null && allocationType.arguments.length == expected.arguments.length) {
				// check the case when no ctor takes no params and inference uses the expected type directly
				// eg. X<String> x = new X<String>()
				int i;
				for (i = 0; i < allocationType.arguments.length; i++) {
					if (TypeBinding.notEquals(allocationType.arguments[i], expected.arguments[i]))
						break;
				}
				if (i == allocationType.arguments.length) {
					scope.problemReporter().redundantSpecificationOfTypeArguments(this.type, allocationType.arguments);
					return;
				}	
			}
		}
		TypeBinding [] inferredTypes;
		int previousBits = this.type.bits;
		try {
			// checking for redundant type parameters must fake a diamond, 
			// so we infer the same results as we would get with a diamond in source code:
			this.type.bits |= IsDiamond;
			inferredTypes = inferElidedTypes(scope);
		} finally {
			// reset effects of inference
			this.type.bits = previousBits;
		}
		if (inferredTypes == null) {
			return;
		}
		for (int i = 0; i < inferredTypes.length; i++) {
			if (TypeBinding.notEquals(inferredTypes[i], allocationType.arguments[i]))
				return;
		}
		scope.problemReporter().redundantSpecificationOfTypeArguments(this.type, allocationType.arguments);
	}
	
	public void setActualReceiverType(ReferenceBinding receiverType) {
		// ignored
	}
	
	public void setDepth(int i) {
		// ignored
	}
	
	public void setFieldIndex(int i) {
		// ignored
	}
	
	public void traverse(ASTVisitor visitor, BlockScope scope) {
		if (visitor.visit(this, scope)) {
			if (this.typeArguments != null) {
				for (int i = 0, typeArgumentsLength = this.typeArguments.length; i < typeArgumentsLength; i++) {
					this.typeArguments[i].traverse(visitor, scope);
				}
			}
			if (this.type != null) { // enum constant scenario
				this.type.traverse(visitor, scope);
			}
			if (this.arguments != null) {
				for (int i = 0, argumentsLength = this.arguments.length; i < argumentsLength; i++)
					this.arguments[i].traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, scope);
	}
	/**
	 * @see org.summer.sdt.internal.compiler.ast.Expression#setExpectedType(org.summer.sdt.internal.compiler.lookup.TypeBinding)
	 */
	public void setExpectedType(TypeBinding expectedType) {
		this.typeExpected = expectedType;
	}
	
	public void setExpressionContext(ExpressionContext context) {
		this.expressionContext = context;
	}
	
	public boolean isPolyExpression() {
		return isPolyExpression(this.binding);
	}
	public boolean isPolyExpression(MethodBinding method) {
		return (this.expressionContext == ASSIGNMENT_CONTEXT || this.expressionContext == INVOCATION_CONTEXT) &&
				this.type != null && (this.type.bits & ASTNode.IsDiamond) != 0;
	}
	
	/**
	 * @see org.summer.sdt.internal.compiler.lookup.InvocationSite#invocationTargetType()
	 */
	public TypeBinding invocationTargetType() {
		return this.typeExpected;
	}
	
	public boolean statementExpression() {
		return true;
	}
	
	//-- interface Invocation: --
	public MethodBinding binding() {
		return this.binding;
	}
	public Expression[] arguments() {
		return this.arguments;
	}
	public void registerInferenceContext(ParameterizedGenericMethodBinding method, InferenceContext18 infCtx18) {
		if (this.inferenceContexts == null)
			this.inferenceContexts = new SimpleLookupTable();
		this.inferenceContexts.put(method, infCtx18);
	}
	
	@Override
	public void registerResult(TypeBinding targetType, MethodBinding method) {
		if (method != null && method.isConstructor()) { // ignore the factory.
			if (this.solutionsPerTargetType == null)
				this.solutionsPerTargetType = new HashMap<TypeBinding, MethodBinding>();
			this.solutionsPerTargetType.put(targetType, method);
		}
	}

	public InferenceContext18 getInferenceContext(ParameterizedMethodBinding method) {
		if (this.inferenceContexts == null)
			return null;
		return (InferenceContext18) this.inferenceContexts.get(method);
	}
	//-- interface InvocationSite: --
	public ExpressionContext getExpressionContext() {
		return this.expressionContext;
	}
	public InferenceContext18 freshInferenceContext(Scope scope) {
		return new InferenceContext18(scope, this.arguments, this, this.outerInferenceContext);
	}
	
	protected StringBuffer doGenerateExpression(Scope scope, Dependency dependency, int indent, StringBuffer output) {
		if(this.binding == null){
			return output;
		}
		if(this.binding.declaringClass.isMemberType()){
			output.append("(function(){\n");
			printIndent(indent + 1, output).append("var r = {__enclosing : this, __proto__: ");
			output.append(CharOperation.concatWith(this.binding.declaringClass.getQualifiedName(), '.')).append(".prototype");
			output.append("};\n");
			printIndent(indent + 1, output).append(this.binding.declaringClass.sourceName).append(".apply(r, arguments");
			output.append(");\n");
			printIndent(indent + 1, output).append("return r;\n"); 
			printIndent(indent, output).append("}).call(this");
			outputArguments(scope, dependency, indent, output, true);
			
			if(this.binding != null && (this.binding.tagBits & TagBits.AnnotationOverload) != 0){
				if(this.arguments != null && this.arguments.length > 0){
					output.append(JsConstant.COMMA).append(JsConstant.WHITESPACE);
				}
				output.append("\"").append(Annotation.getOverloadPostfix(this.binding.sourceMethod().annotations)).append("\"");
			}
			output.append(JsConstant.RPAREN);
			return output;
		} else {
			output.append(JsConstant.NEW).append(JsConstant.WHITESPACE); 
	
			if((this.binding.declaringClass.modifiers & ClassFileConstants.AccNative) !=0){
				if (this.type != null) { 
					int id = this.binding.declaringClass.id;
					if(id != TypeIds.NoId){
						switch(id){
						case TypeIds.T_char:
						case TypeIds.T_JavaLangCharacter:
						case TypeIds.T_byte:
						case TypeIds.T_JavaLangByte:
						case TypeIds.T_short:
						case TypeIds.T_JavaLangShort:
						case TypeIds.T_int:
						case TypeIds.T_JavaLangInteger:
						case TypeIds.T_long:
						case TypeIds.T_JavaLangLong:
						case TypeIds.T_float:
						case TypeIds.T_JavaLangFloat:
						case TypeIds.T_double:
						case TypeIds.T_JavaLangDouble:
							output.append("Number");
						}
					} else {
						this.type.doGenerateExpression(scope, dependency, 0, output);
					}
				} else { // type null for enum constant initializations
					output.append(this.typeExpected.sourceName()); 
				}
			} else {
				output.append("__cache[\"").append(CharOperation.concatWith(this.binding.declaringClass.compoundName, '.')).append("\"]");
			}
			
			output.append('(');
			outputArguments(scope, dependency, indent, output, false);
			
			if(this.binding != null && (this.binding.tagBits & TagBits.AnnotationOverload) != 0){
				if(this.arguments != null && this.arguments.length > 0){
					output.append(", ");
				}
				AbstractMethodDeclaration method = this.binding.sourceMethod();
				if(method!=null){
					output.append("\"").append(Annotation.getOverloadPostfix(method.annotations)).append("\"");
				}
				
			}
			output.append(')');
		}
		
		return output;
	}
	
	private void outputArguments(Scope scope, Dependency dependency, int indent, StringBuffer output, boolean comma){
		if (this.arguments != null) {
			if(comma) output.append(", "); //$NON-NLS-1$
			
			if((this.binding.modifiers & ClassFileConstants.AccNative) != 0 || (this.binding.modifiers & ClassFileConstants.AccVarargs) == 0){
				for (int i = 0; i < this.arguments.length ; i ++) {
					if (i > 0) output.append(", "); //$NON-NLS-1$
					this.arguments[i].doGenerateExpression(scope, dependency, 0, output);
				}
			} else{
				if((this.binding.modifiers & ClassFileConstants.AccVarargs) != 0){
					int argIndex = this.binding.parameters.length - 1;
					for (int i = 0; i < argIndex ; i ++) {
						if (i > 0) output.append(", "); //$NON-NLS-1$
						this.arguments[i].doGenerateExpression(scope, dependency, 0, output);
					}
					
					if (argIndex > 0) output.append(",");
					output.append("["); //$NON-NLS-1$
					for(int i = argIndex; i < this.arguments.length; i++){
						if (i > argIndex) output.append(", "); //$NON-NLS-1$
						this.arguments[i].doGenerateExpression(scope, dependency, 0, output);
					}
					output.append("]");
				}
			}
		}
	}
}
