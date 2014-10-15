package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.lookup.Scope;

public class EventDeclaration extends ASTNode {
	public TypeReference type;
	public char[] name;
	public MethodDeclaration[] accessors;
	public int modifiers;
	public Annotation[] annotations;
	public EventDeclaration(char[] name,int s, int e){
		this.name = name;
		this.sourceStart = s;
		this.sourceEnd = e;
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
