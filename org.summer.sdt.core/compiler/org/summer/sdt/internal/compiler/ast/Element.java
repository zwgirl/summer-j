package org.summer.sdt.internal.compiler.ast;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public abstract class Element extends ASTNode {
	public int closeMode;
	public static final int CLOSE_TAG = 1;   // "/>"
	public static final int CLOSE_ELEMENT = 2;		 // "</"
	
	public static final Attribute[] noAttribute = new Attribute[0];
	
	public TypeReference type;
	public Attribute[] attributes = noAttribute;
	public static final Element[] noChild = new Element[0];
	public Element[] children = noChild;
	public int declarationSourceStart;
	public int declarationSourceEnd;
	public int bodyStart;
	public int bodyEnd; // doesn't include the trailing comment if any.

	public StringBuffer print(int indent, StringBuffer output) {
		printIndent(indent, output);
		
		output.append("<");
		printTagName(indent, output);
		printProperties(indent, output);
		
		if(this.closeMode == CLOSE_TAG){
			return output.append("/>");
		} else {
			output.append(">");
		}
		
		printChildElement(indent, output);
		
		output.append("\n");
		printIndent(indent, output).append("</");
		
		printTagName(indent, output);
		
		return output.append(">");
		
	}
	
	protected abstract void printTagName(int indent, StringBuffer output);
	
	protected void printProperties(int indent, StringBuffer output){
		for(Attribute attribute : attributes){
			output.append(" ");
			attribute.print(indent, output);
		}
	}
	
	public StringBuffer printChildElement(int indent, StringBuffer output) {
		for(Element element : children){
			output.append('\n');
			element.print(indent + 1, output);
		}
		return output;
	}
	
}
