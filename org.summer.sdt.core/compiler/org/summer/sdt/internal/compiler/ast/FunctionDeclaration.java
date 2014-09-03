package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.flow.FlowContext;
import org.summer.sdt.internal.compiler.flow.FlowInfo;
import org.summer.sdt.internal.compiler.lookup.BlockScope;

/**
 * 
 * @author cym
 *
 */
public class FunctionDeclaration extends Statement{

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
