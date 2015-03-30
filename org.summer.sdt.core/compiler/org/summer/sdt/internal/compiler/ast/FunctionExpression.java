package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.lookup.Scope;

public class FunctionExpression extends XAMLNode{

	public FunctionExpression(MethodDeclaration md) {
		this.sourceEnd = md.sourceEnd;
		this.sourceStart = md.sourceStart;
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected StringBuffer doGenerateExpression(Scope scope, int indent,
			StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

}
