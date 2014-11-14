package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.flow.FlowContext;
import org.summer.sdt.internal.compiler.flow.FlowInfo;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

public class PCDATA  extends Element {
	char[] source;

	public PCDATA(char[] source, int sourceStart, int sourceEnd) {
		this.source = source;
		this.sourceStart = sourceStart;
		this.sourceEnd = sourceEnd;
	}

	@Override
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {
		// TODO Auto-generated method stub
		
	}
	
	public void resolve(ClassScope scope) {
//		this.constant = Constant.NotAConstant;
//		this.resolvedType = type.resolveType(scope);
////		resolveType(scope);
//		resolveAttribute(scope);
//		resolveChild(scope);
	}

	@Override
	public void resolve(BlockScope scope) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StringBuffer generateExpression(Scope scope, int indent,
			StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void printTagName(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		
	}

}
