package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

public class HtmlComment extends HtmlNode{
	public char[] source;

	public HtmlComment(char[] source, long pos) {
		super();
		this.source = source;
		//by default the position are the one of the field (not true for super access)
		this.sourceStart = (int) (pos >>> 32);
		this.sourceEnd = (int) (pos & 0x00000000FFFFFFFFL);
	}

	@Override
	protected StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		return output;
	}
	
	public StringBuffer html(BlockScope scope, int indent, StringBuffer output){
		output.append(source);
		return output;
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		return output;
	}

}
