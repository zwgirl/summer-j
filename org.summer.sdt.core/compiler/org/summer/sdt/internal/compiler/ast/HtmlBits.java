package org.summer.sdt.internal.compiler.ast;

public interface HtmlBits {
	int IsSimpleElement = ASTNode.Bit1;
	int IsComplexElement = ASTNode.Bit2;
	int HasNameAttribute = ASTNode.Bit3;
	int HasMarkupExtension = ASTNode.Bit4;
	int HasFunctionExpression = ASTNode.Bit5;
	int HasScriptElement = ASTNode.Bit6;
}
