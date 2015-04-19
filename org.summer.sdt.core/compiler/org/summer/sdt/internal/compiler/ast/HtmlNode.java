package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.codegen.CodeStream;
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
	public StringBuffer generateExpression(Scope scope, int indent, StringBuffer output) {
		return this.doGenerateExpression(scope, indent, output);
	}
	
	@Override
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {
	}
	
	protected StringBuffer buildDOMChildScript(Scope scope, int indent, StringBuffer output, String parent, String context){
		return output;}
	
	public StringBuffer html(BlockScope scope, int indent, StringBuffer output) {
		return output;
	}
	
	public ASTNode parserElement(int position){
		return null;
	}
	
	public StringBuffer option(BlockScope scope, int indent, StringBuffer output) {
		return output;
	}
}
