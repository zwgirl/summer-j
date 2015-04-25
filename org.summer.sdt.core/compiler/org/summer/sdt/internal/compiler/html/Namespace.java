package org.summer.sdt.internal.compiler.html;

import org.summer.sdt.internal.compiler.util.HashtableOfTagMapping;

public final class Namespace {
	public static final char[] SVG = "http://www.w3.org/2000/svg".toCharArray();
	public static final char[] LARK = "http://www.lark.org/2012".toCharArray();
	public static final char[] XLINK = "http://www.w3.org/1999/xlink".toCharArray();
	public static final char[] HTML5 = "http://www.w3.org/2014/html5".toCharArray();
	public static final char[] UNKNOWN = "http://www.w3.org/2014/html5".toCharArray();
	
	public static final char[] XMLNS = "xmlns".toCharArray();
	public static final char[] XML = "xml".toCharArray();
	
	public static final char[] HTML_TAG = "html".toCharArray();
	public static final char[] HEAD_TAG = "head".toCharArray();
	public static final char[] BODY_TAG = "body".toCharArray();
	
	public static final char[] SVG_TAG = "svg".toCharArray();
	
	private static final HashtableOfTagMapping namespaces = new HashtableOfTagMapping();

	static{
		namespaces.put(SVG, new SvgTags());
		namespaces.put(HTML5, new HtmlTags());
		namespaces.put(LARK, new LarkTags());
		namespaces.put(XLINK, new XLinkTags());
	}
	
	public static char[][] getMappingClass(char[] ns, char[] name){
		TagMapping mapping = namespaces.get(ns);
		if(mapping == null){
			return null;
		}
		
		char[][] result = mapping.getMappingClass(name);
		return result;
	}
}
