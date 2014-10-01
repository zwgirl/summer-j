package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.flow.FlowContext;
import org.summer.sdt.internal.compiler.flow.FlowInfo;
import org.summer.sdt.internal.compiler.lookup.BlockScope;

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

	@Override
	public FlowInfo analyseCode(BlockScope currentScope,
			FlowContext flowContext, FlowInfo flowInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StringBuffer printStatement(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resolve(BlockScope scope) {
		// TODO Auto-generated method stub
		
	}
}
