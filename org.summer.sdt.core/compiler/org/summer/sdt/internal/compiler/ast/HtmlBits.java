package org.summer.sdt.internal.compiler.ast;

public interface HtmlBits {
	int NamedField = ASTNode.Bit1;
	int HasMarkupExtension = ASTNode.Bit2;
	int HasTemplate = ASTNode.Bit5;
	int HasDynamicContent = NamedField | HasMarkupExtension | HasTemplate;
	
	int IsHtmlPage = ASTNode.Bit17;
	int IsSimpleElement = ASTNode.Bit18;
	int IsComplexElement = ASTNode.Bit19;
	
	int HasScriptElement = ASTNode.Bit20;
	int HasFunctionExpression = ASTNode.Bit21;
	int IsEventCallback =  ASTNode.Bit22;
	int HasAttachAttribute = ASTNode.Bit23;
}
