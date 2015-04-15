package org.summer.sdt.internal.compiler.ast;

public interface HtmlBits {
	int NamedField = ASTNode.Bit1;   //���������ֶ�
	int HasMarkupExtension = ASTNode.Bit2;		//ʹ���˱����չ
	int HasTemplate = ASTNode.Bit3;
	int AttachedAttribute = ASTNode.Bit4;	//��������
	int QualifiedNameAttribute = ASTNode.Bit5;	//�޶�����
	int HasDynamicContent = NamedField 
			| HasMarkupExtension 
			| HasTemplate
			| AttachedAttribute
			| QualifiedNameAttribute;
	
	int HtmlPage = ASTNode.Bit17;
		
	int SimpleElement = ASTNode.Bit18;		//��Ԫ��
	int ComplexElement = ASTNode.Bit19;		//����Ԫ��
	
	int ScriptElement = ASTNode.Bit20;	//�ű�Ԫ��
	int FunctionExpression = ASTNode.Bit21;	//�������ʽ
	int EventCallback = ASTNode.Bit22;	//�¼��ص�
}
