package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ElementScope;
import org.summer.sdt.internal.compiler.lookup.InferenceContext18;
import org.summer.sdt.internal.compiler.lookup.InvocationSite;
import org.summer.sdt.internal.compiler.lookup.MethodBinding;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TagBits;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public abstract class Attribute extends XAMLNode implements InvocationSite{
	public PropertyReference property;
	public Expression value;
	public MethodBinding method;
	
	public final static char[] NAME = "name".toCharArray();
	
	public static final int NamedFlag = 1;
	
	protected Attribute() {
		super();
	}

	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		return printStatement(indent, output);
	}
	
	@Override
	public StringBuffer printStatement(int indent, StringBuffer output) {
		printPropertyName(indent, output);
		output.append(" = ");
		value.printExpression(indent, output);
		return output;
	}
	
	protected abstract void printPropertyName(int indent, StringBuffer output);

	public void resolve(BlockScope scope) {
		this.constant = Constant.NotAConstant;
		property.resolveType(scope);
//		TypeBinding receiverType = scope.context.resolvedType;
//		if(receiverType == null){
////			return new ProblemFieldBinding(
////					receiverType instanceof ReferenceBinding ? (ReferenceBinding) receiverType : null,
////							this.field.token,
////					ProblemReasons.NotFound);
//			scope.problemReporter().invalidField(field, receiverType);
//			return null;
//		}
//		FieldBinding fieldBinding = field.binding = scope.getField(receiverType, this.field.token, this.field);
//		if(fieldBinding == null){
//			scope.problemReporter().invalidField(field, receiverType);
//			return null;
//		}
//		if (!fieldBinding.isValidBinding()) {
//			field.constant = Constant.NotAConstant;
//			if (scope.context.type.resolvedType instanceof ProblemReferenceBinding) {
//				// problem already got signaled on receiver, do not report secondary problem
//				return null;
//			}
//			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=245007 avoid secondary errors in case of
//			// missing super type for anonymous classes ... 
//			ReferenceBinding declaringClass = fieldBinding.declaringClass;
//			boolean avoidSecondary = declaringClass != null &&
//									 declaringClass.isAnonymousType() &&
//									 declaringClass.superclass() instanceof MissingTypeBinding;
//			if (!avoidSecondary) {
//				scope.problemReporter().invalidField(field, receiverType);
//			}
//			if (fieldBinding instanceof ProblemFieldBinding) {
//				ProblemFieldBinding problemFieldBinding = (ProblemFieldBinding) fieldBinding;
//				FieldBinding closestMatch = problemFieldBinding.closestMatch;
//				switch(problemFieldBinding.problemId()) {
//					case ProblemReasons.InheritedNameHidesEnclosingName :
//					case ProblemReasons.NotVisible :
//					case ProblemReasons.NonStaticReferenceInConstructorInvocation :
//					case ProblemReasons.NonStaticReferenceInStaticContext :
//						if (closestMatch != null) {
//							fieldBinding = closestMatch;
//						}
//				}
//			}
//			if (!fieldBinding.isValidBinding()) {
//				return null;
//			}
//		}
//		return fieldBinding;
	}
	
	public void resolve(ElementScope scope){
		property.resolveType(scope);

		if(this.value instanceof MarkupExtension){
			MarkupExtension markExt = (MarkupExtension) this.value;
			markExt.resolve(scope);
		} else {
			if(property.binding == null || property.binding.type == null){
				return;
			}
			
			if((property.binding.type.tagBits & TagBits.AnnotationEventCallback) != 0){
				StringLiteral str = (StringLiteral) this.value;
				this.method = scope.findMethod(scope.classScope().referenceContext.binding, str.source, new TypeBinding[0], this, false);
				if(this.method == null){
					scope.problemReporter().errorNoMethodFor(str, property.binding.type, new TypeBinding[0]); 
				}
			}
		}
	}
	
	/**
	 * Every expression is responsible for generating its implicit conversion when necessary.
	 *
	 * @param currentScope org.summer.sdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.summer.sdt.internal.compiler.codegen.CodeStream
	 * @param valueRequired boolean
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
//		if (this.constant != Constant.NotAConstant) {
//			// generate a constant expression
//			int pc = codeStream.position;
//			codeStream.generateConstant(this.constant, this.implicitConversion);
//			codeStream.recordPositionsFrom(pc, this.sourceStart);
//		} else {
//			// actual non-constant code generation
//			throw new ShouldNotImplement(Messages.ast_missingCode);
//		}
	}
	
	@Override
	protected StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		output.append(property.token).append("=");
		value.generateExpression(scope, indent, output);
		
		return output;
	}
	
	@Override
	public StringBuffer generateStatement(Scope scope, int indent, StringBuffer output) {
		generateExpression(scope, indent, output);
		return output;
	}
	
	@Override
	public StringBuffer generateExpression(Scope scope, int indent, StringBuffer output) {
		return doGenerateExpression(scope, indent, output);
	}
	
	public TypeBinding[] genericTypeArguments() { return null;}
	public boolean isSuperAccess() {return false;}
	public boolean isTypeAccess() {return false;}
	public void setActualReceiverType(ReferenceBinding receiverType) {/* empty */}
	public void setDepth(int depth) {/* empty */ }
	public void setFieldIndex(int depth) {/* empty */ }
	public int sourceEnd() {return this.sourceEnd; }
	public int sourceStart() {return this.sourceStart; }
	public TypeBinding invocationTargetType() { return null; }
	public boolean receiverIsImplicitThis() { return false; }
	public InferenceContext18 freshInferenceContext(Scope scope) { return null; }
	public ExpressionContext getExpressionContext() { return ExpressionContext.VANILLA_CONTEXT; }
	@Override
	public boolean isQualifiedSuper() { return false; }
	public boolean checkingPotentialCompatibility() { return false; }
	public void acceptPotentiallyCompatibleMethods(MethodBinding[] methods) { /* ignore */ }
}
