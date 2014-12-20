package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.javascript.Dependency;
import org.summer.sdt.internal.compiler.lookup.Scope;

/**
 * 
 * @author cym
 *
 */
public class IndexerDeclaration extends FieldDeclaration {
	public int bodyStart;
	public int bodyEnd;
	public Argument[] arguments;

	public MethodDeclaration setter;
	public MethodDeclaration getter;

	public IndexerDeclaration(char[] name, int s, int e) {
		super(name, s, e);
	}
	
	@Override
	public StringBuffer doGenerateExpression(Scope scope, Dependency depsManager, int indent,
			StringBuffer output) {
		return output.append("//TODO implements!");
	}

}
