package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.ElementScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

public class PCDATA  extends XAMLElement {
	public char[] source;

	public PCDATA(char[] source, long pos) {
		super();
		this.source = source;
		//by default the position are the one of the field (not true for super access)
		this.sourceStart = (int) (pos >>> 32);
		this.sourceEnd = (int) (pos & 0x00000000FFFFFFFFL);
		
	}

	@Override
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {
		
	}
	
	public void resolve(ClassScope scope) {

	}

	@Override
	public void resolve(BlockScope scope) {
		
	}
	
	public void resolve(ElementScope scope) {

	}

	@Override
	public StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		output.append(source);
		return output;
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		return output;
	}

	@Override
	protected void printTagName(int indent, StringBuffer output) {
		
	}

}
