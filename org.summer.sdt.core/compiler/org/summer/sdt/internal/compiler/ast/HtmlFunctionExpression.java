package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.lookup.Scope;

public class HtmlFunctionExpression extends HtmlNode{
	private final MethodDeclaration md;
	public HtmlFunctionExpression(MethodDeclaration md) {
		this.sourceEnd = md.sourceEnd;
		this.sourceStart = md.sourceStart;
		
		this.md = md;
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		output.append("'");
		for(Statement statement: md.statements){
			statement.generateStatement(scope, indent, output);
		}
		output.append("'");
		return output;
	}

}
