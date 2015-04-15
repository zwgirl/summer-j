package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.lookup.Scope;

public class HtmlFunctionExpression extends Expression{
	private final MethodDeclaration md;
	
	public int bodyStart, bodyEnd;
	public HtmlFunctionExpression(MethodDeclaration md) {
		this.sourceEnd = md.sourceEnd;
		this.sourceStart = md.sourceStart;
		
		this.bodyStart = md.bodyStart;
		this.bodyEnd = md.bodyEnd;
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
			statement.generateExpression(scope, 0, output);
			output.append(";");
		}
		output.append("'");
		return output;
	}

}
