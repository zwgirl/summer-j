package org.summer.sdt.core.dom;

public class HtmlMarkupExtension extends HtmlElement{

	HtmlMarkupExtension(AST ast) {
		super(ast);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int getNodeType0() {
		return XAML_MARKUP_EXTENSION;
	}
}
