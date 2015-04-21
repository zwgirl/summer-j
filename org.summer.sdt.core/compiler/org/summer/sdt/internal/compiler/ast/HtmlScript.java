package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

public class HtmlScript extends HtmlNode{
	public MethodDeclaration method;
	public int bodyStart, bodyEnd;
	
	
	public HtmlScript(MethodDeclaration method) {
		this.method = method;

		this.sourceStart = method.sourceStart;
		this.sourceEnd = method.sourceEnd;
		this.bodyStart = method.bodyStart;
		this.bodyEnd = method.bodyEnd;
	}
	
	@Override
	public void resolve(BlockScope scope) {
		this.constant = Constant.NotAConstant;
		scope.element.tagBits |= HtmlBits.ScriptNode;
	}
	
	@Override
	public StringBuffer html(BlockScope scope, int indent, StringBuffer output, char[] _this) {
		output.append("<script type = 'text/javascript'>");
		Statement[] statements = method.statements;
		if(statements != null){
			for(Statement statement : statements){
				output.append('\n');
				statement.generateStatement(scope, indent + 1, output);
			}
		}
		output.append('\n');
		printIndent(indent, output);
		output.append("</script>");
		return output;
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		output.append("<script type = 'text/javascript'>");
		Statement[] statements = method.statements;
		if(statements != null){
			for(Statement statement : statements){
				output.append('\n');
				statement.printStatement(indent, output);
			}
		}
		output.append('\n');
		printIndent(indent, output);
		output.append("</script>");
		return output;
	}

	@Override
	protected StringBuffer doGenerateExpression(Scope scope, int indent,
			StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

}
