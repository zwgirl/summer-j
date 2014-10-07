package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.lookup.Scope;

public class ParenthesizedExpression extends Expression {
	/**
	 * The expression within the parentheses.
	 */
	public Expression expression;
	public ParenthesizedExpression(Expression expression) {
		this.expression = expression;
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateJavascript(Scope scope, int indent, StringBuffer buffer) {
		// TODO Auto-generated method stub
		
	}
}
