package org.summer.sdt.internal.compiler.ast;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public class MarkupExtension extends Expression {
	public TypeReference type;
	public Attribute[] properties;

	public MarkupExtension() {
	}

	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		output.append("{").append(type.getLastToken());
		printProperties(indent, output);
		return output.append(" }");
	}
	
	protected void printProperties(int indent, StringBuffer output){
		for(Attribute attribute : properties){
			output.append(" ");
			attribute.print(indent, output);
		}
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		return print(indent, output);
	}

}
