package org.summer.sdt.internal.compiler.ast;

public class HtmlAttributeElement extends HtmlElement {
	public char[][] selectors;
	public long[] selectorPos;
	public HtmlAttributeElement(char[][] selectors, long[] selectorPos, char[][] tokens, long[] positions) {
		super(tokens, positions);
		this.selectorPos = selectorPos;
		this.selectors = selectors;
	}

}
