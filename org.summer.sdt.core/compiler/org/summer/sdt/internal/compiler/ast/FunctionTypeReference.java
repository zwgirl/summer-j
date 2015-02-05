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
	
	public static final char[] NO_NAME = new char[0];

	public FunctionTypeReference(char[] source, long pos) {
		super(source, pos);
	}

}
