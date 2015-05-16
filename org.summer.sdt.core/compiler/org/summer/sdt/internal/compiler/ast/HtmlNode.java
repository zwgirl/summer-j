package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

/**
 * 
 * @author cym
 *
 */
public abstract class HtmlNode extends Expression {
	public int tagBits;
	public int bodyStart, bodyEnd;
	public char[] prefix;
	public long prefixPos;
	
	protected HtmlNode(){
	}
	
	protected HtmlNode(char[] prefix, long prefixPos){
		this.prefix = prefix;
		this.prefixPos = prefixPos;
	}
	
	@Override
	public StringBuffer generateStatement(Scope scope, int indent, StringBuffer output) {
		printIndent(indent, output);
		generateExpression(scope, indent, output);
		return output;
	}
	
	@Override
	protected StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output){
		return output;
	}
	
	protected abstract StringBuffer scriptDom(BlockScope scope, int indent, StringBuffer output, String parentNode, String logicParent, String context);
	
	public abstract StringBuffer html(BlockScope scope, int indent, StringBuffer output, String _this);
	
	public abstract StringBuffer option(BlockScope scope, int indent, StringBuffer output, String _this);
	
	public ASTNode parserElement(int position){
		return null;
	}
}
