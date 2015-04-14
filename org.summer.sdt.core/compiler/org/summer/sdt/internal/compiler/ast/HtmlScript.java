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
		scope.element.tagBits |= HtmlBits.HasScriptElement;
	}
	
	@Override
	public StringBuffer html(BlockScope scope, int indent, StringBuffer output) {
		output.append("<script type = 'text/javascript'>");
		
		output.append("</script>");
		return output;
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
