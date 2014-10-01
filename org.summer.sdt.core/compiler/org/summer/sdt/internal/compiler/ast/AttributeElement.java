package org.summer.sdt.internal.compiler.ast;

public class AttributeElement extends Element{
	public TypeReference type;
	public FieldReference field;
	
	@Override
	protected void printTagName(int indent, StringBuffer output) {
		output.append(type.getLastToken());
		output.append(".");
		output.append(field.token);
	}
}
