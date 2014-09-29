package org.summer.sdt.internal.compiler.ast;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public abstract class Attribute extends ASTNode{
	public FieldReference field;
	public Expression expression;
	
	protected Attribute() {
	}

	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}
}
