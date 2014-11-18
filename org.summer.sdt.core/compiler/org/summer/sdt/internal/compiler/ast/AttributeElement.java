package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

public class AttributeElement extends Element{
	public TypeReference type;
	public FieldReference field;

	@Override
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resolve(BlockScope scope) {
		type.resolve(scope);
		field.resolve(scope);
		for(Element child : children){
			child.resolve(scope);
		}
	}


	@Override
	protected void printTagName(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StringBuffer generateExpression(Scope scope, int indent,
			StringBuffer output) {
		printIndent(indent, output);
		output.append("this.").append(field.token).append(" = ");
		
//		if(type.){
//			
//		}
		return output;
	}

}
