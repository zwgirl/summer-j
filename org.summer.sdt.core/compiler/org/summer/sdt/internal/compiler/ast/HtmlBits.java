package org.summer.sdt.internal.compiler.ast;

public interface HtmlBits {
	int NamedField = ASTNode.Bit1;   //命名属性字段
	int HasMarkupExtension = ASTNode.Bit2;		//使用了标记扩展
//	int HasTemplate = ASTNode.Bit3;
//	int PrefixAttribute = ASTNode.Bit4;	//指定了名称空间的属性
//	int JoinAttribute = ASTNode.Bit5;	//限定属性
//	int HasDynamicContent = NamedField
//			| HasMarkupExtension
//			| HasTemplate
//			| JoinAttribute;
	
	//lark type 
	int HtmlPage = ASTNode.Bit9;
	int HtmlFragment = ASTNode.Bit10;
	int HtmlTemplate = ASTNode.Bit11;
	int Class = ASTNode.Bit12;
	
	//type of element
	int SimpleElement = ASTNode.Bit11;		//简单元素
	int ComplexElement = ASTNode.Bit12;		//复合元素
	int ScriptNode = ASTNode.Bit13;		//脚本元素
	int TextNode = ASTNode.Bit14;		//文本元素
	int CommentNode = ASTNode.Bit15;		//备注元素
	
	int Template = ASTNode.Bit16;			//模板元素
	int CollectionTemplate = ASTNode.Bit17;	//集合模板元素
	int Fragment = ASTNode.Bit18;			//集合模板元素

	int Lark_Element_Mask = Template | CollectionTemplate | Fragment;
	
	int FunctionExpression = ASTNode.Bit21;	//函数表达式
	int EventCallback = ASTNode.Bit22;		//事件回调
	
	//close tag 
	int NS_IF_ELEMENT = ASTNode.Bit23;
	int IF_ELEMENT = ASTNode.Bit31;
	int SWITCH_ELEMENT = ASTNode.Bit24;
	int NS_SWITCH_ELEMENT = ASTNode.Bit25;
	int ATTRIBUTE_ELEMENT = ASTNode.Bit26;
	int COMMON_ELEMENT = ASTNode.Bit27;
	int NS_COMMON_ELEMENT = ASTNode.Bit28;
	int NS_VAR_ELEMENT = ASTNode.Bit29;
	int VAR_ELEMENT = ASTNode.Bit30;
	int ElementTypeMask = SWITCH_ELEMENT | ATTRIBUTE_ELEMENT | COMMON_ELEMENT | VAR_ELEMENT;
	
	//Flag of attribute
	int XMLNS_PREFIX = ASTNode.Bit19;
	int XMLNS_DEFAULT = ASTNode.Bit20;
	int XML_RESERVED = ASTNode.Bit21;
	int DATA_HYPHEN_ASTERISK = ASTNode.Bit22;
	int COMMON_ATTRIBUTE = ASTNode.Bit23;
	int NS_ATTRIBUTE = ASTNode.Bit24;

	//html element
	int HEAD = ASTNode.Bit19;
	int BODY = ASTNode.Bit20;
	int TEXT = ASTNode.Bit21;   //TextNode of DOM
	
}
