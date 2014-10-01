package org.summer.sdt.internal.compiler.ast;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public class AttachAttribute extends Attribute {
	public TypeReference type;

	public AttachAttribute() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void printPropertyName(int indent, StringBuffer output) {
		output.append(type.getLastToken());
		output.append(".");
		output.append(field.token);
	}

}
