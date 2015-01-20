package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.html.Html2JsAttributeMapping;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.ElementScope;
import org.summer.sdt.internal.compiler.lookup.InferenceContext18;
import org.summer.sdt.internal.compiler.lookup.InvocationSite;
import org.summer.sdt.internal.compiler.lookup.MethodBinding;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TagBits;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeConstants;

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
			
			if((property.binding.type.tagBits & TagBits.AnnotationEventCallback) != 0){
				this.bits |= ASTNode.IsEventCallback;
				StringLiteral str = (StringLiteral) this.value;
				this.method = scope.findMethod(scope.classScope().referenceContext.binding, str.source, new TypeBinding[0], this, false);
				if(this.method == null){
					scope.problemReporter().errorNoMethodFor(str, property.binding.type, new TypeBinding[0]); 
				}
			}
			
			ReferenceBinding temBinding = scope.environment().getType(TypeConstants.JAVA_LANG_TEMPLATE);
			if(property.binding.type.isSubtypeOf(temBinding)) {
				this.bits |= ASTNode.IsTemplate;
				StringLiteral str = (StringLiteral) this.value;
				this.template = (ReferenceBinding) scope.getType(str.source);
				if(this.template == null){
					scope.problemReporter().errorNoMethodFor(str, property.binding.type, new TypeBinding[0]); 
				}
				
				classScope.referenceContext.element.bits |= ASTNode.HasDynamicContent;
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
