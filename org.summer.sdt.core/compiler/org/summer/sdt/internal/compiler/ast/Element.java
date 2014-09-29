package org.summer.sdt.internal.compiler.ast;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public abstract class Element extends ASTNode {
	public TypeReference type;
	public Attribute[] attributes;
	public Element[] children;

	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

}
