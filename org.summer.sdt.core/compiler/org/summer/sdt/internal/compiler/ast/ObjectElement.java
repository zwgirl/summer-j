package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.lookup.BlockScope;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public class ObjectElement extends Element {
	public Attribute name;
	public AllocationExpression allocation;
	
	@Override
	protected void printTagName(int indent, StringBuffer output) {
		output.append(type.getLastToken());
	}

	@Override
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resolve(BlockScope scope) {
		if(allocation != null){
			allocation.resolve(scope);
		}
	}

}
