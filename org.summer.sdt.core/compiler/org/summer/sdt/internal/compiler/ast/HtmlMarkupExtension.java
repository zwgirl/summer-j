package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TagBits;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;

/**
 * 
 * @author cym
 * 
 */
public class HtmlMarkupExtension extends HtmlElement {
	
	public HtmlMarkupExtension(char[] tokens, long positions, TypeReference type) {
		super(new char[][]{tokens}, new long[]{positions}, type);
	}

	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		return printExpression(indent, output);
	}
	
	protected void printProperties(int indent, StringBuffer output){
		for(HtmlAttribute attribute : this.attributes){
			output.append(" ");
			attribute.print(indent, output);
		}
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		output.append("{").append(type.getLastToken());
		printProperties(indent, output);
		return output.append(" }");
	}
	
	protected TypeBinding internalResolve1(BlockScope parentScope){
		this.scope = new BlockScope(parentScope, this);
		this.constant = Constant.NotAConstant;
		if(this.type != null){
			this.resolvedType = type.resolveType(this.scope);
		}
		
		resolveAttribute(this.scope);
		return resolvedType;
		
	}
	
	public void resolve(BlockScope parentScope) {
		this.internalResolve1(parentScope);
	}
	
	/**
	 * Resolve the type of this expression in the context of a blockScope
	 *
	 * @param scope
	 * @return
	 * 	Return the actual type of this expression after resolution
	 */
	public TypeBinding resolveType(BlockScope scope) {
		return this.internalResolve1(scope);
	}
	
	/**
	 * Resolve the type of this expression in the context of a classScope
	 *
	 * @param scope
	 * @return
	 * 	Return the actual type of this expression after resolution
	 */
	public TypeBinding resolveType(ClassScope scope) {
		// by default... subclasses should implement a better TB if required.
		return null;
	}
	
	public TypeBinding resolveTypeExpecting(BlockScope parentScope, TypeBinding expectedType) {
		setExpectedType(expectedType); // needed in case of generic method invocation
		this.scope = new BlockScope(parentScope, this);
		TypeBinding expressionType = this.resolveType(scope);
		if (expressionType == null) return null;
//		if (TypeBinding.equalsEquals(expressionType, expectedType)) return expressionType;
//	
//		if (!expressionType.isCompatibleWith(expectedType)) {
//			if (scope.isBoxingCompatibleWith(expressionType, expectedType)) {
//				computeConversion(scope, expectedType, expressionType);
//			} else {
//				scope.problemReporter().typeMismatchError(expressionType, expectedType, this, null);
//				return null;
//			}
//		}
		return expressionType;
	}

//	@Override
//	public StringBuffer scriptDom(BlockScope scope, int indent, StringBuffer output, String parent, String _this ) {
//		if(this.resolvedType == null){
//			return output;
//		}
//		ReferenceBinding rb = (ReferenceBinding) this.resolvedType;
//		output.append("new (");
//		rb.generate(output, null);
//		output.append(")(");
//		
//		output.append("{");
//		boolean comma = false;
//		for(HtmlAttribute attribute : this.attributes){
//			if(comma){
//				output.append(", ");
//			}
//			
//			attribute.option(scope, indent, output, _this);
//			comma = true;
//		}
//		output.append("}");
//		output.append(")");
//
//		return output;
//	}
	
	@Override
	public StringBuffer scriptDom(BlockScope parentScope, int indent, StringBuffer output, String parentNode, String logicParent, String context ) {
		if(this.resolvedType == null){
			return output;
		}
		
		ReferenceBinding rb = (ReferenceBinding) this.resolvedType;
		if(rb.isSubtypeOf(scope.getJavalangPage())){
			output.append("var __m = new (__lc(\"java.lang.ProxyPage\"))(");
			rb.generate(output, scope.enclosingSourceType());
			output.append(".prototype.__class");
			output.append(", ").append(parentNode);
			output.append(");");
			
			for(HtmlAttribute attribute : this.attributes){
				attribute.scriptDom(scope, indent, output, "__m", logicParent, context);
			}
			
			output.append("__m");
		} else {
			newMarkupExtension(output, rb);
			
			output.append("{");
			buildOptions(scope, indent, output, context);
			output.append("}");
			output.append(")");
		}

		return output;
	}

	public void newMarkupExtension(StringBuffer output, ReferenceBinding rb) {
		output.append("new (");
		rb.generate(output, null);
		output.append(")(");
	}

	public boolean buildOptions(BlockScope scope, int indent, StringBuffer output, String context) {
		boolean comma = false;
		for(HtmlAttribute attribute : this.attributes){
			if(comma){
				output.append(", ");
			}
			
			attribute.option(scope, indent, output, context);
			comma = true;
		}
		
		return comma;
	}
	
	public StringBuffer optionValue(BlockScope scope, int indent, StringBuffer output, String context){
		if(this.resolvedType == null){
			return output;
		}
		ReferenceBinding rb = (ReferenceBinding) this.resolvedType;
		output.append("new (");
		rb.generate(output, null);
		output.append(")(");
		
		output.append("{");
		boolean comma = false;
		for(HtmlAttribute attribute : this.attributes){
			if(comma){
				output.append(", ");
			}
			
			attribute.option(scope, indent, output, context);
			comma = true;
		}
		output.append("}");
		output.append(")");

		return output;
		
	}

}
