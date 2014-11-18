package org.summer.sdt.internal.compiler.ast;

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
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void printPropertyName(int indent, StringBuffer output) {
		output.append(field.token);
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		return output;
	}
	
	@Override
	public void resolve(BlockScope scope) {
		this.field.resolve(scope);
		this.value.resolve(scope);
	}

	@Override
	public StringBuffer generateExpression(Scope scope, int indent,
			StringBuffer output) {
		printIndent(indent, output);
		output.append("this.").append(field.token).append(" = ");
		value.generateExpression(scope, indent, output);
		return output;
	}
}
