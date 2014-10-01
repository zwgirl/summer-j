package org.summer.sdt.internal.compiler.ast;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public abstract class Attribute extends ASTNode{
	public FieldReference field;
	public Expression expression;
	
	protected Attribute() {
	}

	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		printPropertyName(indent, output);
		output.append(" = ");
		expression.printExpression(indent, output);
		return output;
	}
	
	protected abstract void printPropertyName(int indent, StringBuffer output);
}
