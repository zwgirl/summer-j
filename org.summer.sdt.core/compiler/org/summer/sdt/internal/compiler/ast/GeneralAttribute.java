package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.javascript.Dependency;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

/**
 * 
 * @authorcym
 * 
 * XAML
 */
public class GeneralAttribute extends Attribute{

	public GeneralAttribute(SingleNameReference namespace) {
		super(namespace);
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
	public StringBuffer doGenerateExpression(Scope scope, Dependency depsManager, int indent,
			StringBuffer output) {
		printIndent(indent, output);
		output.append("this.").append(field.token).append(" = ");
		value.doGenerateExpression(scope, depsManager, indent, output);
		return output;
	}
}
