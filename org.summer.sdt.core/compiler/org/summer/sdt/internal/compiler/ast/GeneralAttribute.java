package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.html.Html2JsAttributeMapping;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

/**
 * 
 * @authorcym
 * 
 * XAML
 */
public class GeneralAttribute extends Attribute{

	public GeneralAttribute() {
		super();
	}

	@Override
	protected void printPropertyName(int indent, StringBuffer output) {
		output.append(property.token);
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		return output;
	}
	
	@Override
	public void resolve(BlockScope scope) {
		this.property.resolve(scope);
		this.value.resolve(scope);
	}

	@Override
	public StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		output.append(Html2JsAttributeMapping.getHtmlAttributeName(new String(property.token))).append(" = ");
		
		if((this.bits & ASTNode.IsEventCallback) != 0){
			if(this.method.isStatic()){
				output.append(this.method.declaringClass.sourceName).append('.');
				if(value instanceof StringLiteral){
					output.append(((StringLiteral)value).source).append("(event); \"");
				}
			} else {
				output.append("\"__this.");
				if(value instanceof StringLiteral){
					output.append(((StringLiteral)value).source).append("(event); \"");
				}
			}
		} else {
			value.doGenerateExpression(scope, indent, output);
		}
		return output;
	}
	
	public StringBuffer generateHTML(Scope initializerScope, int indent, StringBuffer output) {
		return output;
	}
}
