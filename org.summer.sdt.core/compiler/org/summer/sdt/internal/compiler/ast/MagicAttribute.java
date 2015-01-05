package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ElementScope;

/**
 * 
 * @author cym
 *
 */
public class MagicAttribute extends Attribute{
	public SingleTypeReference type;
	public MagicAttribute(SingleTypeReference type) {
		this.type = type;
	}

	@Override
	protected void printPropertyName(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void resolve(ElementScope scope) {
		this.constant = Constant.NotAConstant;
		this.resolvedType = type.resolveType(scope);
		ElementScope eleScope = new ElementScope(this, scope);
		this.property.resolve(eleScope);
		this.value.resolve(scope);
	}

}
