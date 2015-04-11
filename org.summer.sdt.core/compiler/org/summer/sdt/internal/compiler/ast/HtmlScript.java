package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ElementScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

public class HtmlScript extends HtmlElement{
	public MethodDeclaration method;
	
	public HtmlScript(MethodDeclaration method) {
		this.method = method;

		this.declarationSourceStart = this.sourceStart = method.sourceStart;
		this.bodyStart = method.bodyStart;
		this.bodyEnd = method.bodyEnd;
	}

	@Override
	protected void printTagName(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void resolve(ElementScope scope) {
		this.constant = Constant.NotAConstant;
	}
	
	@Override
	public void resolve(BlockScope scope) {
		
	}

}
