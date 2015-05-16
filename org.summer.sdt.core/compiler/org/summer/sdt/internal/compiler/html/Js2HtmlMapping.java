package org.summer.sdt.internal.compiler.html;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cym
 */
public final class Js2HtmlMapping {
	final static Map<String, String> html2js = new HashMap<String, String>();
	final static Map<String, String> js2html = new HashMap<String, String>();
	static{
		html2js.put("http-equiv", "httpEquiv");
		html2js.put("for", "htmlFor");
		html2js.put("class", "className");
		
		js2html.put("httpEquiv", "http-equiv");
		js2html.put("htmlFor", "for");
		js2html.put("className", "class");
	}
	
	public static String getJsName(String name){
		String result = html2js.get(name);
		return result == null ? name : result;
	}
	
	public static String getHtmlName(String name){
		String result = js2html.get(name);
		return result == null ? name : result;
	}
}
