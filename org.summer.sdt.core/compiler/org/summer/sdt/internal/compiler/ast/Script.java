package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.ElementScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

public class Script extends XAMLElement{
	public Block block;
	public Script() {
		super();
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

}
