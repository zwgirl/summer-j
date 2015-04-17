package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;

/**
 * 
 * @author cym
 * 
 */
public class HtmlMarkupExtension extends HtmlElement {
	
	public HtmlMarkupExtension(char[][] tokens, long[] positions) {
		super(tokens, positions);
		
		type = new SingleTypeReference1(tokens, positions);
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

	@Override
	public StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
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
			
			attribute.buildMarkupExtensionOptionPart(scope, indent, output);
			comma = true;
		}
		output.append("}");
		output.append(")");

		return output;
	}

	@Override
	protected void printTagName(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		
	}

}
