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
		output.append("(");
		expression.printExpression(indent, output);
		output.append(")");
		return output;
	}

	@Override
	public StringBuffer generateExpression(Scope scope, int indent,
			StringBuffer output) {
		output.append("(");
		expression.generateExpression(scope, indent, output);
		output.append(")");
		return output;
	}
}
