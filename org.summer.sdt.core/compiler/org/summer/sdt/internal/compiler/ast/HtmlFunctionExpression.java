package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeConstants;

public class HtmlFunctionExpression extends Expression{
	private final MethodDeclaration md;
	
	public int bodyStart, bodyEnd;
	public HtmlFunctionExpression(MethodDeclaration md) {
		this.sourceEnd = md.sourceEnd;
		this.sourceStart = md.sourceStart;
		
		this.bodyStart = md.bodyStart;
		this.bodyEnd = md.bodyEnd;
		this.md = md;
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		output.append("'");
		for(Statement statement: md.statements){
			statement.generateExpression(scope, 0, output);
			output.append(";");
		}
		output.append("'");
		return output;
	}
	
	@Override
	public TypeBinding resolveTypeExpecting(BlockScope scope,
			TypeBinding expectedType) {
		if(expectedType.isBaseType()){
			scope.problemReporter().typeMismatchError((TypeBinding)scope.getTypeOrPackage(TypeConstants.JAVA_LANG_FUNCTION), expectedType, this, null);
		}
		
		if((((ReferenceBinding)expectedType).modifiers & ClassFileConstants.AccFunction) == 0 ){
			scope.problemReporter().typeMismatchError((TypeBinding)scope.getTypeOrPackage(TypeConstants.JAVA_LANG_FUNCTION), expectedType, this, null);
		}
		return super.resolveTypeExpecting(scope, expectedType);
	}

}
