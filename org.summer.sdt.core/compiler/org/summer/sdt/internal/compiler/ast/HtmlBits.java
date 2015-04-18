package org.summer.sdt.internal.compiler.ast;

public interface HtmlBits {
	int NamedField = ASTNode.Bit1;   //���������ֶ�
	int HasMarkupExtension = ASTNode.Bit2;		//ʹ���˱����չ
	int HasTemplate = ASTNode.Bit3;
	int PrefixAttribute = ASTNode.Bit4;	//ָ�������ƿռ������
	int JoinAttribute = ASTNode.Bit5;	//�޶�����
	int HasDynamicContent = NamedField
			| HasMarkupExtension
			| HasTemplate
			| JoinAttribute;
	
	int HtmlPage = ASTNode.Bit17;
		
	int SimpleElement = ASTNode.Bit18;		//��Ԫ��
	int ComplexElement = ASTNode.Bit19;		//����Ԫ��
	
	int ScriptElement = ASTNode.Bit20;	//�ű�Ԫ��
	int FunctionExpression = ASTNode.Bit21;	//�������ʽ
	int EventCallback = ASTNode.Bit22;	//�¼��ص�
	
	int SWITCH_ELEMENT = ASTNode.Bit24;
	int NS_SWITCH_ELEMENT = ASTNode.Bit25;
	int ATTRIBUTE_ELEMENT = ASTNode.Bit26;
	int COMMON_ELEMENT = ASTNode.Bit27;
	int NS_COMMON_ELEMENT = ASTNode.Bit28;
	int NS_VAR_ELEMENT = ASTNode.Bit29;
	int VAR_ELEMENT = ASTNode.Bit30;
	
	int ElementTypeMask = SWITCH_ELEMENT | ATTRIBUTE_ELEMENT | COMMON_ELEMENT | VAR_ELEMENT;
	
}
