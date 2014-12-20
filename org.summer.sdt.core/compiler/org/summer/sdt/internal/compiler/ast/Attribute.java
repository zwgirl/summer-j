package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public abstract class Attribute extends XAMLNode{
	public FieldReference field;
	public Expression value;
	
	public final static char[] NAME = "name".toCharArray();
	
	public static final int NamedFlag = 1;
	
	protected Attribute(SingleNameReference namespace) {
		super(namespace);
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
		field.resolveType(scope);
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
}
