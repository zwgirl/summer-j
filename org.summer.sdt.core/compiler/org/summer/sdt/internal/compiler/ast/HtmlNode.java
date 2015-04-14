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
	public static final int EMPTY_ELEMENT = 1;   
	public static final int SIMPLE_ELEMENT = 2;	
	public static final int COMPLEX_ELEMENT = 3;
	public static final int ATTRIBUTE_ELEMENT = 4;
	public static final int PCDATA = 5;	
	public static final int MARKUP_EXTENSION = 6;
	public static final int GENERAL_ATTRIBUTE = 7;	
	public static final int ATTACH_ATTRIBUTE = 8;	
	
	public int kind;
	public int tagBits;
	
	protected HtmlNode(){
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
	
	public StringBuffer generateStaticHTML(BlockScope scope, int indent, StringBuffer output) {
		return output;
	}
	
	public StringBuffer generateDynamicHTML(BlockScope scope, int indent, StringBuffer output) {
		return output;
	}
	
	@Override
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {
	}
	
	protected StringBuffer buildDOMScript(Scope scope, int indent, StringBuffer output, String parent, String context){
		return output;}
	
	public StringBuffer html(BlockScope scope, int indent, StringBuffer output) {
		return output;
	}
	
	public ASTNode parserElement(int position){
		return null;
	}
}
