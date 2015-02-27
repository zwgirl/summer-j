package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.html.Html2JsAttributeMapping;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.ElementScope;
import org.summer.sdt.internal.compiler.lookup.FieldBinding;
import org.summer.sdt.internal.compiler.lookup.InferenceContext18;
import org.summer.sdt.internal.compiler.lookup.InvocationSite;
import org.summer.sdt.internal.compiler.lookup.MethodBinding;
import org.summer.sdt.internal.compiler.lookup.MissingTypeBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemFieldBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemReasons;
import org.summer.sdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
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
	public ReferenceBinding template;
	public FieldBinding field;
	
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
	}
	
	public void resolve(ElementScope scope){
		property.resolveType(scope);
		ClassScope classScope = scope.enclosingClassScope();

		if((this.bits & ASTNode.IsNamedAttribute) != 0){
			classScope.referenceContext.element.bits |= ASTNode.HasDynamicContent;
		}
		
		if(this.value instanceof MarkupExtension){
			MarkupExtension markExt = (MarkupExtension) this.value;
			markExt.resolve(scope);
			classScope.referenceContext.element.bits |= ASTNode.HasDynamicContent;
		} else {
			if(property.binding == null || property.binding.type == null){
				return;
			}
			
//			if((property.binding.type.tagBits & TagBits.AnnotationEventCallback) != 0){
//				this.bits |= ASTNode.IsEventCallback;
//				StringLiteral stringLiteral = (StringLiteral) this.value;
//				this.method = scope.findMethod(scope.classScope().referenceContext.binding, stringLiteral.source, new TypeBinding[0], this, false);
//				if(this.method == null){
//					scope.problemReporter().errorNoMethodFor(stringLiteral, property.binding.type, new TypeBinding[0]); 
//				}
//			}
			if(!(property.binding.type instanceof ReferenceBinding)){
				return;
			}
			
			ReferenceBinding refType = (ReferenceBinding) property.binding.type;
			if((refType.modifiers & ClassFileConstants.AccFunction) != 0 ){
				this.bits |= ASTNode.IsEventCallback;
				StringLiteral stringLiteral = (StringLiteral) this.value;
				this.method = scope.findMethod(scope.classScope().referenceContext.binding, stringLiteral.source, new TypeBinding[0], this, false);
				if(this.method == null){
					this.field = scope.findField(scope.classScope().referenceContext.binding, stringLiteral.source, this, true);
					if(field == null){
						scope.problemReporter().errorNoMethodFor(stringLiteral, property.binding.type, new TypeBinding[0]); 
					}
				}
			} else if(refType.isEnum()){
				this.field = scope.getField(refType, ((StringLiteral) this.value).source, this);
				if((field.modifiers & ClassFileConstants.AccEnum) == 0) {
					scope.problemReporter().invalidProperty(property, property.binding.type);   //TODO  cym 2015-02-27
				}
				if(field == null){
					scope.problemReporter().invalidProperty(property, property.binding.type);    //TODO  cym 2015-02-27
				}
				
				
				if (!field.isValidBinding()) {
					this.constant = Constant.NotAConstant;
					if (refType instanceof ProblemReferenceBinding) {
						// problem already got signaled on receiver, do not report secondary problem
						return;
					}
					// https://bugs.eclipse.org/bugs/show_bug.cgi?id=245007 avoid secondary errors in case of
					// missing super type for anonymous classes ... 
					ReferenceBinding declaringClass = field.declaringClass;
					boolean avoidSecondary = declaringClass != null &&
											 declaringClass.isAnonymousType() &&
											 declaringClass.superclass() instanceof MissingTypeBinding;
					if (!avoidSecondary) {
						scope.problemReporter().invalidProperty(property, property.binding.type);  //TODO cym 2015-02-27
					}
					if (field instanceof ProblemFieldBinding) {
						ProblemFieldBinding problemFieldBinding = (ProblemFieldBinding) field;
						FieldBinding closestMatch = problemFieldBinding.closestMatch;
						switch(problemFieldBinding.problemId()) {
							case ProblemReasons.InheritedNameHidesEnclosingName :
							case ProblemReasons.NotVisible :
							case ProblemReasons.NonStaticReferenceInConstructorInvocation :
							case ProblemReasons.NonStaticReferenceInStaticContext :
								if (closestMatch != null) {
									field = closestMatch;
								}
						}
					}
					if (!field.isValidBinding()) {
						return;
					}
				}
				
			} else {
				ReferenceBinding temBinding = scope.getJavaLangTemplate();
				if(property.binding.type.isSubtypeOf(temBinding)) {
					this.bits |= ASTNode.IsTemplate;
					StringLiteral stringLiteral = (StringLiteral) this.value;
					this.template = (ReferenceBinding) scope.getType(stringLiteral.source);
					if(this.template == null){
						scope.problemReporter().errorNoMethodFor(stringLiteral, property.binding.type, new TypeBinding[0]); 
					}
					
					classScope.referenceContext.element.bits |= ASTNode.HasDynamicContent;
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

	}
	
	@Override
	protected StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		output.append(Html2JsAttributeMapping.getHtmlAttributeName(new String(property.token))).append("=");
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
