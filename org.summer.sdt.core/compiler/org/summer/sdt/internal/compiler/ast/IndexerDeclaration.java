package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.lookup.Scope;

/**
 * 
 * @author cym
 *
 */
public class IndexerDeclaration extends FieldDeclaration {
	public Argument[] arguments;
	public MethodDeclaration[] accessors;

	public IndexerDeclaration(char[] name, int s, int e) {
		super(name, s, e);
	}

	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer generateJavascript(Scope scope, int indent,
			StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}
}
