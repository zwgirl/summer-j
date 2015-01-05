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
	public StringBuffer doGenerateExpression(Scope scope, Dependency dependency, int indent,
			StringBuffer output) {
		output.append(property.token).append(" = ");
		value.doGenerateExpression(scope, dependency, indent, output);
		return output;
	}
}
