package org.summer.sdt.internal.compiler.ast;

public class IndexerDeclaration extends ASTNode {
	public TypeReference type;
	public Argument[] arguments;
	public MethodDeclaration[] accessors;
	public int modifiers;
	public Annotation[] annotations;
	
	public IndexerDeclaration(int s, int e){
		this.sourceStart = s;
		this.sourceEnd = e;
	}
	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}
}
