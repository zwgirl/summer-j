package org.summer.sdt.internal.compiler.ast;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public class MarkupExtension extends Expression {
	public TypeReference type;
	public PropertyExpression[] values;

	public MarkupExtension() {
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

}
