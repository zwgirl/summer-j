package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.IProblem;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.Binding;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.summer.sdt.internal.compiler.lookup.FieldBinding;
import org.summer.sdt.internal.compiler.lookup.MethodScope;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.SourceTypeBinding;
import org.summer.sdt.internal.compiler.lookup.TagBits;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.problem.ProblemReporter;
import org.summer.sdt.internal.compiler.problem.ProblemSeverities;
import org.summer.sdt.internal.compiler.util.Util;

/**
 * 
 * @author cym
 *
 */
public class EventDeclaration extends FieldDeclaration {
	public int bodyStart;
	public int bodyEnd;
	
	public MethodDeclaration add;
	public MethodDeclaration remove;
	
	public EventDeclaration(char[] name, int s, int e) {
		super(name, s, e);
		
		this.modifiers |= ClassFileConstants.AccProperty;
	}
	
	public void resolve(MethodScope initializationScope) {
		// the two <constant = Constant.NotAConstant> could be regrouped into
		// a single line but it is clearer to have two lines while the reason of their
		// existence is not at all the same. See comment for the second one.
	
		//--------------------------------------------------------
		if ((this.bits & ASTNode.HasBeenResolved) != 0) return;
		if (this.binding == null || !this.binding.isValidBinding()) return;
	
		this.bits |= ASTNode.HasBeenResolved;
	
		// check if field is hiding some variable - issue is that field binding already got inserted in scope
		// thus must lookup separately in super type and outer context
		ClassScope classScope = initializationScope.enclosingClassScope();
	
		if (classScope != null) {
			checkHiding: {
				SourceTypeBinding declaringType = classScope.enclosingSourceType();
				checkHidingSuperField: {
					if (declaringType.superclass == null) break checkHidingSuperField;
					// https://bugs.eclipse.org/bugs/show_bug.cgi?id=318171, find field skipping visibility checks
					// we do the checks below ourselves, using the appropriate conditions for access check of
					// protected members from superclasses.
					FieldBinding existingVariable = classScope.findField(declaringType.superclass, this.name, this,  false /*do not resolve hidden field*/, true /* no visibility checks please */);
					if (existingVariable == null) break checkHidingSuperField; // keep checking outer scenario
					if (!existingVariable.isValidBinding())  break checkHidingSuperField; // keep checking outer scenario
					if (existingVariable.original() == this.binding) break checkHidingSuperField; // keep checking outer scenario
					if (!existingVariable.canBeSeenBy(declaringType, this, initializationScope)) break checkHidingSuperField; // keep checking outer scenario
					// collision with supertype field
					initializationScope.problemReporter().fieldHiding(this, existingVariable);
					break checkHiding; // already found a matching field
				}
				// only corner case is: lookup of outer field through static declaringType, which isn't detected by #getBinding as lookup starts
				// from outer scope. Subsequent static contexts are detected for free.
				Scope outerScope = classScope.parent;
				if (outerScope.kind == Scope.COMPILATION_UNIT_SCOPE) break checkHiding;
				Binding existingVariable = outerScope.getBinding(this.name, Binding.VARIABLE, this, false /*do not resolve hidden field*/);
				if (existingVariable == null) break checkHiding;
				if (!existingVariable.isValidBinding()) break checkHiding;
				if (existingVariable == this.binding) break checkHiding;
				if (existingVariable instanceof FieldBinding) {
					FieldBinding existingField = (FieldBinding) existingVariable;
					if (existingField.original() == this.binding) break checkHiding;
					if (!existingField.isStatic() && declaringType.isStatic()) break checkHiding;
				}
				// collision with outer field or local variable
				initializationScope.problemReporter().fieldHiding(this, existingVariable);
			}
		}
	
		if (this.type != null ) { // enum constants have no declared type
			this.type.resolvedType = this.binding.type; // update binding for type reference
		}
	
		FieldBinding previousField = initializationScope.initializedField;
		int previousFieldID = initializationScope.lastVisibleFieldID;
		try {
			initializationScope.initializedField = this.binding;
			initializationScope.lastVisibleFieldID = this.binding.id;
	
			resolveAnnotations(initializationScope, this.annotations, this.binding);
			// Check if this declaration should now have the type annotations bit set
			if (this.annotations != null) {
				for (int i = 0, max = this.annotations.length; i < max; i++) {
					TypeBinding resolvedAnnotationType = this.annotations[i].resolvedType;
					if (resolvedAnnotationType != null && (resolvedAnnotationType.getAnnotationTagBits() & TagBits.AnnotationForTypeUse) != 0) {
						this.bits |= ASTNode.HasTypeAnnotations;
						break;
					}
				}
			}
			
			// check @Deprecated annotation presence
			if ((this.binding.getAnnotationTagBits() & TagBits.AnnotationDeprecated) == 0
					&& (this.binding.modifiers & ClassFileConstants.AccDeprecated) != 0
					&& initializationScope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5) {
				initializationScope.problemReporter().missingDeprecatedAnnotationForField(this);
			}
			//cym 2014-12-18
//			// the resolution of the initialization hasn't been done
//			if (this.initialization == null) {
//				this.binding.setConstant(Constant.NotAConstant);
//			} else {
//				// break dead-lock cycles by forcing constant to NotAConstant
//				this.binding.setConstant(Constant.NotAConstant);
//	
//				TypeBinding fieldType = this.binding.type;
//				TypeBinding initializationType;
//				this.initialization.setExpressionContext(ASSIGNMENT_CONTEXT);
//				this.initialization.setExpectedType(fieldType); // needed in case of generic method invocation
//				if (this.initialization instanceof ArrayInitializer) {
//	
//					if ((initializationType = this.initialization.resolveTypeExpecting(initializationScope, fieldType)) != null) {
//						((ArrayInitializer) this.initialization).binding = (ArrayBinding) initializationType;
//						this.initialization.computeConversion(initializationScope, fieldType, initializationType);
//					}
//				} else if ((initializationType = this.initialization.resolveType(initializationScope)) != null) {
//	
//					if (TypeBinding.notEquals(fieldType, initializationType)) // must call before computeConversion() and typeMismatchError()
//						initializationScope.compilationUnitScope().recordTypeConversion(fieldType, initializationType);
//					if (this.initialization.isConstantValueOfTypeAssignableToType(initializationType, fieldType)
//							|| initializationType.isCompatibleWith(fieldType, classScope)) {
//						this.initialization.computeConversion(initializationScope, fieldType, initializationType);
//						if (initializationType.needsUncheckedConversion(fieldType)) {
//							    initializationScope.problemReporter().unsafeTypeConversion(this.initialization, initializationType, fieldType);
//						}
//						if (this.initialization instanceof CastExpression
//								&& (this.initialization.bits & ASTNode.UnnecessaryCast) == 0) {
//							CastExpression.checkNeedForAssignedCast(initializationScope, fieldType, (CastExpression) this.initialization);
//						}
//					} else if (isBoxingCompatible(initializationType, fieldType, this.initialization, initializationScope)) {
//						this.initialization.computeConversion(initializationScope, fieldType, initializationType);
//						if (this.initialization instanceof CastExpression
//								&& (this.initialization.bits & ASTNode.UnnecessaryCast) == 0) {
//							CastExpression.checkNeedForAssignedCast(initializationScope, fieldType, (CastExpression) this.initialization);
//						}
//					} else {
//						if ((fieldType.tagBits & TagBits.HasMissingType) == 0) {
//							// if problem already got signaled on type, do not report secondary problem
//							initializationScope.problemReporter().typeMismatchError(initializationType, fieldType, this.initialization, null);
//						}
//					}
//					if (this.binding.isFinal()){ // cast from constant actual type to variable type
//						this.binding.setConstant(this.initialization.constant.castTo((this.binding.type.id << 4) + this.initialization.constant.typeID()));
//					}
//				} else {
//					this.binding.setConstant(Constant.NotAConstant);
//				}
//				// check for assignment with no effect
//				if (this.binding == Expression.getDirectBinding(this.initialization)) {
//					initializationScope.problemReporter().assignmentHasNoEffect(this, this.name);
//				}
//			}
			// Resolve Javadoc comment if one is present
			if (this.javadoc != null) {
				this.javadoc.resolve(initializationScope);
			} else if (this.binding != null && this.binding.declaringClass != null && !this.binding.declaringClass.isLocalType()) {
				// Set javadoc visibility
				int javadocVisibility = this.binding.modifiers & ExtraCompilerModifiers.AccVisibilityMASK;
				ProblemReporter reporter = initializationScope.problemReporter();
				int severity = reporter.computeSeverity(IProblem.JavadocMissing);
				if (severity != ProblemSeverities.Ignore) {
					if (classScope != null) {
						javadocVisibility = Util.computeOuterMostVisibility(classScope.referenceType(), javadocVisibility);
					}
					int javadocModifiers = (this.binding.modifiers & ~ExtraCompilerModifiers.AccVisibilityMASK) | javadocVisibility;
					reporter.javadocMissing(this.sourceStart, this.sourceEnd, severity, javadocModifiers);
				}
			}
		} finally {
			initializationScope.initializedField = previousField;
			initializationScope.lastVisibleFieldID = previousFieldID;
			if (this.binding.constant() == null)
				this.binding.setConstant(Constant.NotAConstant);
		}
	}

	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		return output.append("//TODO event compile implements!");
	}
}
