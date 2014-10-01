package org.summer.sdt.internal.compiler.ast;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public class ObjectElement extends Element {
	
	@Override
	protected void printTagName(int indent, StringBuffer output) {
		output.append(type.getLastToken());
	}

}
