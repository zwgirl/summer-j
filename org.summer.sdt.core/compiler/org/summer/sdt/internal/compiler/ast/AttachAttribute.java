package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public class AttachAttribute extends Attribute {
	public TypeReference type;
	public char[] PROPERTY = "Property".toCharArray();

	public AttachAttribute() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void printPropertyName(int indent, StringBuffer output) {
		output.append(type.getLastToken());
		output.append(".");
		output.append(field.token);
	}

	@Override
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resolve(BlockScope scope) {
//		this.type.resolve(scope);
		this.field.resolve(scope);
		this.value.resolve(scope);
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return output;
	}
	
	public StringBuffer generateExpression(Scope scope, int indent, StringBuffer output) {
		printIndent(indent, output);
		output.append(type.getLastToken()).append(".");
		output.append("set");
		if(CharOperation.endsWith(field.token, PROPERTY)){
			char[] name = CharOperation.subarray(field.token, 0, field.token.length - PROPERTY.length);
			output.append(name);
		} else {
			output.append(field.token);
		}
		output.append("(this, ");
		value.generateExpression(scope, indent, output);
		output.append(")");
		
		return output;
	}

}
