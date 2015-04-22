package org.summer.sdt.internal.compiler.ast;

public class HtmlAttributeElement extends HtmlElement {
	public char[][] selectors;
	public long[] selectorPos;
	public HtmlAttributeElement(char[][] selectors, long[] selectorPos) {
		super(selectors, selectorPos, null);
		this.selectorPos = selectorPos;
		this.selectors = selectors;
	}

}
