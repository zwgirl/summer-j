package org.summer.sdt.internal.compiler.ast;

import static org.summer.sdt.internal.compiler.ast.ExpressionContext.ASSIGNMENT_CONTEXT;

import org.summer.sdt.core.compiler.IProblem;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.flow.FlowContext;
import org.summer.sdt.internal.compiler.flow.FlowInfo;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.ArrayBinding;
import org.summer.sdt.internal.compiler.lookup.Binding;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.summer.sdt.internal.compiler.lookup.FieldBinding;
import org.summer.sdt.internal.compiler.lookup.InferenceContext18;
import org.summer.sdt.internal.compiler.lookup.MethodScope;
import org.summer.sdt.internal.compiler.lookup.PropertyBinding;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
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
public class PropertyDeclaration extends FieldDeclaration {
	
//	public PropertyBinding binding;
//	public Javadoc javadoc;
	
	public int bodyStart;
	public int bodyEnd;

	//allows to retrieve both the "type" part of the declaration (part1)
	//and also the part that decribe the name and the init and optionally
	//some other dimension ! ....
	//public int[] a, b[] = X, c ;
	//for b that would give for
	// - part1 : public int[]
	// - part2 : b[] = X,

//	public int endPart1Position;
//	public int endPart2Position;
//	
//	public int declarationEnd;
//	/**
//	 * For local declarations (outside of for statement initialization) and field declarations,
//	 * the declarationSourceEnd covers multiple locals if any.
//	 * For local declarations inside for statement initialization, this is not the case.
//	 */
//	public int declarationSourceEnd;
//	public int declarationSourceStart;
//	public int hiddenVariableDepth; // used to diagnose hiding scenarii
//	public Expression initialization;
//	public int modifiers;
//	public int modifiersSourceStart;
//	public Annotation[] annotations;
//
//	public char[] name;
//
//	public TypeReference type;
	
	public MethodDeclaration setter;
	public MethodDeclaration getter;

	public PropertyDeclaration(char[] name, int s, int e) {
		super(name, s, e);
	}
	
	public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
		return flowInfo;
	}
	
	public void resolve(MethodScope scope) {
		// the two <constant = Constant.NotAConstant> could be regrouped into
		// a single line but it is clearer to have two lines while the reason of their
		// existence is not at all the same. See comment for the second one.
	
		//--------------------------------------------------------
		if ((this.bits & ASTNode.HasBeenResolved) != 0) return;
		if (this.binding == null || !this.binding.isValidBinding()) return;
	
		this.bits |= ASTNode.HasBeenResolved;
	
		// check if field is hiding some variable - issue is that field binding already got inserted in scope
		// thus must lookup separately in super type and outer context
		ClassScope classScope = scope.enclosingClassScope();
	
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
					if (!existingVariable.canBeSeenBy(declaringType, this, scope)) break checkHidingSuperField; // keep checking outer scenario
					// collision with supertype field
					scope.problemReporter().fieldHiding(this, existingVariable);
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
				scope.problemReporter().fieldHiding(this, existingVariable);
			}
		}
	
		if (this.type != null ) { // enum constants have no declared type
			this.type.resolvedType = this.binding.type; // update binding for type reference
		}
	
		FieldBinding previousField = scope.initializedField;
		int previousFieldID = scope.lastVisibleFieldID;
		try {
			scope.initializedField = this.binding;
			scope.lastVisibleFieldID = this.binding.id;
	
			resolveAnnotations(scope, this.annotations, this.binding);
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
					&& scope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5) {
				scope.problemReporter().missingDeprecatedAnnotationForField(this);
			}
			// the resolution of the initialization hasn't been done
			if (this.initialization == null) {
				this.binding.setConstant(Constant.NotAConstant);
			} else {
				// break dead-lock cycles by forcing constant to NotAConstant
				this.binding.setConstant(Constant.NotAConstant);
	
				TypeBinding fieldType = this.binding.type;
				TypeBinding initializationType;
				this.initialization.setExpressionContext(ASSIGNMENT_CONTEXT);
				this.initialization.setExpectedType(fieldType); // needed in case of generic method invocation
				if (this.initialization instanceof ArrayInitializer) {
	
					if ((initializationType = this.initialization.resolveTypeExpecting(scope, fieldType)) != null) {
						((ArrayInitializer) this.initialization).binding = (ArrayBinding) initializationType;
						this.initialization.computeConversion(scope, fieldType, initializationType);
					}
				} else if ((initializationType = this.initialization.resolveType(scope)) != null) {
					//cym add 2014-10-26 TODO
					if(this.getKind() == ENUM_CONSTANT){
						return;
					}
					
					if (TypeBinding.notEquals(fieldType, initializationType)) // must call before computeConversion() and typeMismatchError()
						scope.compilationUnitScope().recordTypeConversion(fieldType, initializationType);
					if (this.initialization.isConstantValueOfTypeAssignableToType(initializationType, fieldType)
							|| initializationType.isCompatibleWith(fieldType, classScope)) {
						this.initialization.computeConversion(scope, fieldType, initializationType);
						if (initializationType.needsUncheckedConversion(fieldType)) {
							    scope.problemReporter().unsafeTypeConversion(this.initialization, initializationType, fieldType);
						}
						if (this.initialization instanceof CastExpression
								&& (this.initialization.bits & ASTNode.UnnecessaryCast) == 0) {
							CastExpression.checkNeedForAssignedCast(scope, fieldType, (CastExpression) this.initialization);
						}
					} else if (isBoxingCompatible(initializationType, fieldType, this.initialization, scope)) {
						this.initialization.computeConversion(scope, fieldType, initializationType);
						if (this.initialization instanceof CastExpression
								&& (this.initialization.bits & ASTNode.UnnecessaryCast) == 0) {
							CastExpression.checkNeedForAssignedCast(scope, fieldType, (CastExpression) this.initialization);
						}
					} else {
						if ((fieldType.tagBits & TagBits.HasMissingType) == 0) {
							// if problem already got signaled on type, do not report secondary problem
							scope.problemReporter().typeMismatchError(initializationType, fieldType, this.initialization, null);
						}
					}
					if (this.binding.isFinal()){ // cast from constant actual type to variable type
						this.binding.setConstant(this.initialization.constant.castTo((this.binding.type.id << 4) + this.initialization.constant.typeID()));
					}
				} else {
					this.binding.setConstant(Constant.NotAConstant);
				}
				// check for assignment with no effect
				if (this.binding == Expression.getDirectBinding(this.initialization)) {
					scope.problemReporter().assignmentHasNoEffect(this, this.name);
				}
			}
			// Resolve Javadoc comment if one is present
			if (this.javadoc != null) {
				this.javadoc.resolve(scope);
			} else if (this.binding != null && this.binding.declaringClass != null && !this.binding.declaringClass.isLocalType()) {
				// Set javadoc visibility
				int javadocVisibility = this.binding.modifiers & ExtraCompilerModifiers.AccVisibilityMASK;
				ProblemReporter reporter = scope.problemReporter();
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
			scope.initializedField = previousField;
			scope.lastVisibleFieldID = previousFieldID;
			if (this.binding.constant() == null)
				this.binding.setConstant(Constant.NotAConstant);
		}
	}

	public StringBuffer printAsExpression(int indent, StringBuffer output) {
		printIndent(indent, output);
		printModifiers(this.modifiers, output);
		if (this.annotations != null) {
			printAnnotations(this.annotations, output);
			output.append(' ');
		}

		if (this.type != null) {
			this.type.print(0, output).append(' ');
		}
		output.append(this.name);
//		switch(getKind()) {
//			case ENUM_CONSTANT:
//				if (this.initialization != null) {
//					this.initialization.printExpression(indent, output);
//				}
//				break;
//			default:
//				if (this.initialization != null) {
//					output.append(" = "); //$NON-NLS-1$
//					this.initialization.printExpression(indent, output);
//				}
//		}
		return output;
	}

	public StringBuffer generateExpression(Scope scope, int indent, StringBuffer output) {
		printIndent(indent, output);
//		generateModifiers(this.modifiers, output);
//		if (this.annotations != null) {
//			generateAnnotations(this.annotations, output);
//			output.append(' ');
//		}
//
//		if (this.type != null) {
//			this.type.print(0, output).append(' ');
//		}
		output.append("var ");
		output.append(this.name);
//		switch(getKind()) {
//			case ENUM_CONSTANT:
//				if (this.initialization != null) {
//					this.initialization.generateExpression(scope, indent, output);
//				}
//				break;
//			default:
//				if (this.initialization != null) {
//					output.append(" = "); //$NON-NLS-1$
//					this.initialization.generateExpression(scope, indent, output);
//				}
//		}
		return output;
	}
	
	public StringBuffer generateStatement(Scope scope, int indent, StringBuffer output) {
		output.append("\n");
		if (this.javadoc != null) {
			this.javadoc.generateJavascript(scope, indent, output);
		}
		
		printIndent(indent, output);
//		output.append("Object.defineProperty(");
//		if(this.isStatic()){
//			output.append(binding.declaringClass.sourceName);
//		} else {
//			output.append(binding.declaringClass.sourceName).append(".prototype");
//		}
		
		output.append(", ").append(this.name).append(", ").append('{').append("\n");
		boolean comma = false;
		if(this.getter != null){
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("get : ").append("function( ) {").append("\n");
			if(this.getter.statements != null){
				for(int i = 0, length = this.getter.statements.length; i < length; i++){
					output.append("\n");
					this.getter.statements[i].generateStatement(scope, indent + 2, output);
				}
			}
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("}");
			
			comma = true;
		}
		
		if(this.setter != null){
			if(comma){
				output.append(", ");
			}
			
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("set : ").append("function(value ) {").append("\n");
			if(this.setter.statements != null){
				for(int i = 0, length = this.setter.statements.length; i < length; i++){
					output.append("\n");
					this.setter.statements[i].generateStatement(scope, indent + 2, output);
				}
			}
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("}");
			
			comma = true;
		}
		
		output.append("\n");
		printIndent(indent, output).append("});");
		
		return output;
	}

//	@Override
//	public void generateCode(BlockScope currentScope, CodeStream codeStream) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public StringBuffer printStatement(int indent, StringBuffer output) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	public int getKind(){
//		return 0;
//	}
//
//	public boolean isStatic() {
//		// TODO Auto-generated method stub
//		return false;
//	}
}
