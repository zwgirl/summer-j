package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.CharOperation;
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
public class Attribute extends XAMLNode implements InvocationSite{
	public PropertyReference property;
	public Expression value;
	
	public TypeBinding type;   //type of property 
	public MethodBinding method;
	public ReferenceBinding template;
	public FieldBinding field;
	
	public final static char[] NAME = "name".toCharArray();
	
	public static final int NamedFlag = 1;
	
	public Attribute(PropertyReference property) {
		super();
		this.property = property;
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
	
	protected void printPropertyName(int indent, StringBuffer output) {
		output.append(property.token);
	}

	public void resolve(BlockScope scope) {
		this.constant = Constant.NotAConstant;
		property.resolveType(scope);
	}
	
	public void resolve(ElementScope scope){
		this.type = property.resolveType(scope);
		ClassScope classScope = scope.enclosingClassScope();

		if((this.bits & ASTNode.IsNamedAttribute) != 0){
			classScope.referenceContext.element.bits |= ASTNode.HasDynamicContent;
		}
		
		if(this.value instanceof MarkupExtension){
			MarkupExtension markExt = (MarkupExtension) this.value;
			markExt.resolve(scope);
			classScope.referenceContext.element.bits |= ASTNode.HasDynamicContent;
		} else {
			if(this.type == null){
				return;
			}

			if(this.type.isPrimitiveType()){
				return;
			} 
			
			ReferenceBinding refType = (ReferenceBinding) this.type;
			if((refType.modifiers & ClassFileConstants.AccFunction) != 0 ){
				this.bits |= ASTNode.IsEventCallback;
				StringLiteral stringLiteral = (StringLiteral) this.value;
//				this.method = scope.findMethod(scope.classScope().referenceContext.binding, stringLiteral.source, new TypeBinding[0], this, false);
//				if(this.method == null){
					this.field = scope.findField(scope.classScope().referenceContext.binding, stringLiteral.source, this, true);
					if(field == null){
						scope.problemReporter().errorNoMethodFor(stringLiteral, this.type, new TypeBinding[0]); 
					}
//				}
			} else if(refType.isEnum()){
				this.field = scope.getField(refType, ((StringLiteral) this.value).source, this);
				if((field.modifiers & ClassFileConstants.AccEnum) == 0) {
					scope.problemReporter().invalidProperty(property, property.fieldBinding.type);   //TODO  cym 2015-02-27
				}
				if(field == null){
					scope.problemReporter().invalidProperty(property, property.fieldBinding.type);    //TODO  cym 2015-02-27
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
						scope.problemReporter().invalidProperty(property, property.fieldBinding.type);  //TODO cym 2015-02-27
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
				if(this.type.isSubtypeOf(temBinding)) {
					this.bits |= ASTNode.IsTemplate;
					StringLiteral stringLiteral = (StringLiteral) this.value;
					this.template = (ReferenceBinding) scope.getType(stringLiteral.source);
					if(this.template == null){
						scope.problemReporter().errorNoMethodFor(stringLiteral, property.fieldBinding.type, new TypeBinding[0]); 
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
	
//	@Override
//	protected StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
//		output.append(Html2JsAttributeMapping.getHtmlAttributeName(new String(property.token))).append("=");
//		value.generateExpression(scope, indent, output);
//		
//		return output;
//	}
	
	@Override
	protected StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
//		if((this.bits & ASTNode.IsTemplate) != 0 ){
//			if(CharOperation.equals(this.property.token, TEMPLATE)){
//				attrHasTem = attr;
//			}
//
//			if(CharOperation.equals(this.property.token, ITEM_TEMPLATE)){
//				itemTemplate = attr;
//			}
//			
//			if(attrHasTem != null){
//				output.append("\n");
//				printIndent(indent + 1, output);
//				if(attrHasTem.template != null){
//					output.append("var _t = new (");
//					attrHasTem.template.generate(output);
//					output.append(")();");
//					output.append("\n");
//					printIndent(indent + 1, output);
//					output.append("_t.create(_n);");
//					output.append("\n");
//					printIndent(indent + 1, output);
//					output.append("_c.template = _t;");
//				}
//			}
//			
//			if(itemTemplate != null){
//				for(int i = 0; i<10; i++){
//					output.append("\n");
//					printIndent(indent + 1, output);
//					if(itemTemplate.template != null){
//						output.append("var _t = new (");
//						itemTemplate.template.generate(output);
//						output.append(")();");
//						output.append("\n");
//						printIndent(indent + 1, output);
//						output.append("_t.create(_n);");
//						output.append("\n");
//						printIndent(indent + 1, output);
//						output.append("_c.template = _t;");
//					}
//				}
//			}
//			
//			continue;
//		}
//		if(this.value instanceof MarkupExtension){
//			output.append("\n");
//			printIndent(indent + 1, output);
//			output.append("_n.").append(this.property.token).append(" = ");
//			ReferenceBinding rb = (ReferenceBinding) this.value.resolvedType;
//			output.append("new (");
//			rb.generate(output);
//			output.append(")(");
//			
//			output.append("{");
//			MarkupExtension markup = (MarkupExtension) this.value;
//			boolean comma = false;
//			for(Attribute mAttr : markup.attributes){
//				if(comma){
//					output.append(',');
//				}
//				
//				output.append("\"").append(mAttr.property.token).append("\" : ");
//				if(mAttr.property.fieldBinding.type.isEnum()){
//					((ReferenceBinding)mAttr.property.fieldBinding.type).generate(output);
//					output.append('.').append(((StringLiteral) mAttr.value).source);
//				} else {
//					if(mAttr.value instanceof StringLiteral){
//						StringLiteral s = (StringLiteral) mAttr.value;
//						output.append("\"").append(s.source).append("\"");
//					}
//				}
//				comma = true;
//			}
//			output.append("}");
//			
//			output.append(")");
//			output.append(".provideValue(");
//			output.append("_n, \"").append(this.property.token).append("\")");
//			output.append(";");
//			continue;
//		}
//		
//		if(this.property.fieldBinding != null && (((ReferenceBinding)this.property.fieldBinding.type).modifiers & ClassFileConstants.AccFunction) != 0){
//			output.append("\n");
//			printIndent(indent + 1, output);
//			output.append("_n.addEventListener('").append(CharOperation.subarray(this.property.token, 2, -1)).append("', ");
//			if(this.method != null){
//				if(this.method.isStatic()){
//					output.append(this.method.declaringClass.sourceName).append('.');
//					if(this.value instanceof StringLiteral){
//						output.append(((StringLiteral)this.value).source);
//					}
//				} else {
//					output.append("__this.");
//					if(this.value instanceof StringLiteral){
//						output.append(((StringLiteral)this.value).source);
//					}
//				}
//			} else if(this.field != null){
//				if(this.field.isStatic()){
//					output.append(this.field.declaringClass.sourceName).append('.');
//					if(this.value instanceof StringLiteral){
//						output.append(((StringLiteral)this.value).source);
//					}
//				} else {
//					output.append("__this.");
//					if(this.value instanceof StringLiteral){
//						output.append(((StringLiteral)this.value).source);
//					}
//				}
//			}
//			output.append(", false);");
//			
//			continue;
//		}
//		
//		output.append("\n");
//		printIndent(indent + 1, output);
//		output.append("_n.").append(this.property.token).append(" = ");
//		this.value.generateExpression(scope, indent, output).append(";");
		
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

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		return output;
	}
}
