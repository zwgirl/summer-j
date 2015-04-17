package org.summer.sdt.internal.compiler.ast;

public interface HtmlBits {
	int NamedField = ASTNode.Bit1;   //命名属性字段
	int HasMarkupExtension = ASTNode.Bit2;		//使用了标记扩展
	int HasTemplate = ASTNode.Bit3;
	int AttachedAttribute = ASTNode.Bit4;	//附加属性
	int QualifiedNameAttribute = ASTNode.Bit5;	//限定属性
	int HasDynamicContent = NamedField 
			| HasMarkupExtension 
			| HasTemplate
			| AttachedAttribute
			| QualifiedNameAttribute;
	
	int HtmlPage = ASTNode.Bit17;
		
	int SimpleElement = ASTNode.Bit18;		//简单元素
	int ComplexElement = ASTNode.Bit19;		//符合元素
	
	int ScriptElement = ASTNode.Bit20;	//脚本元素
	int FunctionExpression = ASTNode.Bit21;	//函数表达式
	int EventCallback = ASTNode.Bit22;	//事件回调
	
	int SWITCH_ELEMENT = ASTNode.Bit24;
	int ATTRIBUTE_ELEMENT = ASTNode.Bit25;
	int COMMON_ELEMENT = ASTNode.Bit26;
	int VAR_ELEMENT = ASTNode.Bit27;
	
	int ElementTypeMask = SWITCH_ELEMENT | ATTRIBUTE_ELEMENT | COMMON_ELEMENT | VAR_ELEMENT;
	
}
