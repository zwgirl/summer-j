package org.summer.sdt.internal.compiler.ast;

/**
 * 
 * @author cym
 *
 */
public abstract class XAMLNode extends Expression {
	public static final int EMPTY_ELEMENT = 1;   
	public static final int SIMPLE_ELEMENT = 2;	
	public static final int COMPLEX_ELEMENT = 3;
	public static final int ATTRIBUTE_ELEMENT = 4;
	public static final int PCDATA = 5;	
	public static final int MARKUP_EXTENSION = 6;
	public static final int GENERAL_ATTRIBUTE = 7;	
	public static final int ATTACH_ATTRIBUTE = 8;	
	
	public int kind;
	public final SingleNameReference namespace;
	
	protected XAMLNode(SingleNameReference namespace){
		this.namespace = namespace;
	}
	
}
