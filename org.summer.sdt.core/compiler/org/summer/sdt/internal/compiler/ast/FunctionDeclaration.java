package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.CompilationResult;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.flow.FlowContext;
import org.summer.sdt.internal.compiler.flow.FlowInfo;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.MethodBinding;
import org.summer.sdt.internal.compiler.lookup.MethodScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

/**
 * 
 * @author cym
 *
 */
public class FunctionDeclaration extends Statement{

	public MethodScope scope;
	//it is not relevent for constructor but it helps to have the name of the constructor here
	//which is always the name of the class.....parsing do extra work to fill it up while it do not have to....
	public char[] selector;
	public int declarationSourceStart;
	public int declarationSourceEnd;
	public int modifiers;
	public int modifiersSourceStart;
	public Annotation[] annotations;
	// jsr 308
	public Receiver receiver;
	public Argument[] arguments;
	public TypeReference[] thrownExceptions;
	public Statement[] statements;
	public int explicitDeclarations;
	public MethodBinding binding;
	public boolean ignoreFurtherInvestigation = false;

	public Javadoc javadoc;

	public int bodyStart;
	public int bodyEnd = -1;
	public CompilationResult compilationResult;
	
	public TypeReference returnType;
	public TypeParameter[] typeParameters;

	public FunctionDeclaration(CompilationResult compilationResult){
		this.compilationResult = compilationResult;
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

	@Override
	public void generateJavascript(Scope scope, int indent, StringBuffer buffer) {
		// TODO Auto-generated method stub
		
	}

}
