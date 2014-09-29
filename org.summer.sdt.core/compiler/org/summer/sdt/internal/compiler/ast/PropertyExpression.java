package org.summer.sdt.internal.compiler.ast;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public abstract class PropertyExpression extends ASTNode {
	public FieldReference field;
	public Expression value;
	protected PropertyExpression(int s, int e) {
		this.sourceStart = s;
		this.sourceEnd = e;
	}

	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

}
