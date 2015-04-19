package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

public class HtmlAttributeValue extends Expression{
	public Expression value;
	
	public HtmlAttributeValue(Expression value){
		this.value = value;
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
	
	@Override
	public void resolve(BlockScope scope) {
		if(this.value == null){
			return;
		}
		
		this.value.resolve(scope);
	}

}
