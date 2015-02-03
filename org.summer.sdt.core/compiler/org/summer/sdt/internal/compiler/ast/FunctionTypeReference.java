package org.summer.sdt.internal.compiler.ast;

/**
 * 
 * @author cym
 *
 */
public class FunctionTypeReference extends SingleTypeReference {
	public TypeReference returnType;
	public TypeReference[] parameters;
	public TypeReference[] throwTypes;

	public FunctionTypeReference(char[] source, long pos) {
		super(source, pos);
		// TODO Auto-generated constructor stub
	}

}
