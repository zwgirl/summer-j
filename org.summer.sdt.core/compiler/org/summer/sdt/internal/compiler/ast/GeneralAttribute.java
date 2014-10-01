package org.summer.sdt.internal.compiler.ast;
/**
 * 
 * @authorcym
 * 
 * XAML
 */
public class GeneralAttribute extends Attribute{

	public GeneralAttribute() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void printPropertyName(int indent, StringBuffer output) {
		output.append(field.token);
	}
}
