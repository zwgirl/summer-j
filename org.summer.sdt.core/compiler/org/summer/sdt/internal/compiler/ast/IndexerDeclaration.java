package org.summer.sdt.internal.compiler.ast;

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

}
