package org.summer.sdt.internal.compiler.html;

import java.util.HashMap;
import java.util.Map;

public class CopyOfHtmlTags {
	private static final Map<String, String> maps = new HashMap<String, String>();
	static {
//		根元素
		maps.put("html", "org.w3c.html.HTMLHtmlElement");
		
//		文件数据元素
//		<head> (HTMLHeadElement)
		maps.put("head", "org.w3c.html.HTMLHeadElement");
//		<title> (HTMLTitleElement)
		maps.put("title", "org.w3c.html.HTMLTitleElement");
//		<base> (HTMLBaseElement)
		maps.put("base", "org.w3c.html.HTMLBaseElement");
//		<link> (HTMLLinkElement)
		maps.put("link", "org.w3c.html.HTMLLinkElement");
//		<meta> (HTMLMetaElement)
		maps.put("meta", "org.w3c.html.HTMLMetaElement");
		
//		<style> (HTMLStyleElement)
		maps.put("style", "org.w3c.html.HTMLStyleElement");
//		<script> (HTMLScriptElement)
		maps.put("script", "org.w3c.html.HTMLScriptElement");
		
//		<noscript> (HTMLElement)

//		文件区域元素
//		<body> (HTMLBodyElement)
		maps.put("body", "org.w3c.html.HTMLBodyElement");
		
//		<section> (HTMLElement)
		maps.put("section", "org.w3c.html.HTMLElement");
		
//		<nav> (HTMLElement)
		maps.put("nav", "org.w3c.html.HTMLElement");
		
//		<article> (HTMLElement)
		maps.put("article", "org.w3c.html.HTMLElement");
		
//		<aside> (HTMLElement)
		maps.put("aside", "org.w3c.html.HTMLElement");
		
//		<h1> <h2> <h3> <h4> <h5> <h6> (HTMLHeadingElement)
		maps.put("h1", "org.w3c.html.HTMLHeadingElement");
		maps.put("h2", "org.w3c.html.HTMLHeadingElement");
		maps.put("h3", "org.w3c.html.HTMLHeadingElement");
		maps.put("h4", "org.w3c.html.HTMLHeadingElement");
		maps.put("h5", "org.w3c.html.HTMLHeadingElement");
		maps.put("h6", "org.w3c.html.HTMLHeadingElement");
		
//		<hgroup> (HTMLElement)
		maps.put("hgroup", "org.w3c.html.HTMLElement");

		//		<header> (HTMLElement)
		maps.put("header", "org.w3c.html.HTMLElement");
		
//		<footer> (HTMLElement)
		maps.put("footer", "org.w3c.html.HTMLElement");
		
//		<address> (HTMLElement)
		maps.put("address", "org.w3c.html.HTMLElement");
		
		
//		群组元素
//		<p> (HTMLParagraphElement)
		maps.put("p", "org.w3c.html.HTMLParagraphElement");
		
//		<hr> (HTMLHRElement)
		maps.put("hr", "org.w3c.html.HTMLHRElement");
		
//		<pre> (HTMLPreElement)
		maps.put("pre", "org.w3c.html.HTMLPreElement");
		
//		<blockquote> (HTMLQuoteElement)
		maps.put("blockquote", "org.w3c.html.HTMLQuoteElement");
		
//		<ol> (HTMLOListElement)
		maps.put("ol", "org.w3c.html.HTMLOListElement");
		
//		<ul> (HTMLUListElement)
		maps.put("ul", "org.w3c.html.HTMLOListElement");
		
//		<li> (HTMLLIElement)
		maps.put("li", "org.w3c.html.HTMLLIElement");
		
//		<dl> (HTMLDListElement)
		maps.put("dl", "org.w3c.html.HTMLDListElement");

//		<dt> (HTMLElement)
		maps.put("dt", "org.w3c.html.HTMLElement");
		
//		<dd> (HTMLElement)
		maps.put("dd", "org.w3c.html.HTMLElement");
		
//		<figure> (HTMLElement)
		maps.put("figure", "org.w3c.html.HTMLElement");
		
//		<figcaption> (HTMLElement)
		maps.put("figcaption", "org.w3c.html.HTMLElement");
		
//		<div> (HTMLDivElement)
		maps.put("div", "org.w3c.html.HTMLDivElement");

		
//		文字层级元素
//		<a> (HTMLAnchorElement)
		maps.put("a", "org.w3c.html.HTMLAnchorElement");
		
//		<em> (HTMLElement)
		maps.put("em", "org.w3c.html.HTMLElement");

//		<strong> (HTMLElement)
		maps.put("strong", "org.w3c.html.HTMLElement");
		
//		<small> (HTMLElement)
		maps.put("small", "org.w3c.html.HTMLElement");
//		<s> (HTMLElement)
		maps.put("s", "org.w3c.html.HTMLElement");

//		<cite> (HTMLElement)
		maps.put("cite", "org.w3c.html.HTMLElement");

//		<q> (HTMLQuoteElement)
		maps.put("q", "org.w3c.html.HTMLElement");

//		<dfn> (HTMLElement)
		maps.put("dfn", "org.w3c.html.HTMLElement");

//		<abbr> (HTMLElement)
		maps.put("abbr", "org.w3c.html.HTMLElement");
		
//		<data> (HTMLDataElement)
		
//		<time> (HTMLTimeElement)
		maps.put("time", "org.w3c.html.HTMLTimeElement");
		
//		<code> (HTMLElement)
		maps.put("code", "org.w3c.html.HTMLElement");
		
//		<var> (HTMLElement)
		maps.put("var", "org.w3c.html.HTMLElement");
		
//		<samp> (HTMLElement)
		maps.put("samp", "org.w3c.html.HTMLElement");
		
//		<kbd> (HTMLElement)
		maps.put("kbd", "org.w3c.html.HTMLElement");
		
//		<sub> (HTMLElement)
		maps.put("sub", "org.w3c.html.HTMLElement");
		
//		<sup> (HTMLElement)
		maps.put("sup", "org.w3c.html.HTMLElement");
		
//		<i> (HTMLElement)
		maps.put("i", "org.w3c.html.HTMLElement");
		
//		<b> (HTMLElement)
		maps.put("b", "org.w3c.html.HTMLElement");
		
//		<u> (HTMLElement)
		maps.put("u", "org.w3c.html.HTMLElement");
		
//		<mark> (HTMLElement)
		maps.put("mark", "org.w3c.html.HTMLElement");
		
//		<ruby> (HTMLElement)
		maps.put("ruby", "org.w3c.html.HTMLElement");
		
//		<rt> (HTMLElement)
		maps.put("rt", "org.w3c.html.HTMLElement");
		
//		<rp> (HTMLElement)
		maps.put("rp", "org.w3c.html.HTMLElement");
		
//		<bdi> (HTMLElement)
		maps.put("bdi", "org.w3c.html.HTMLElement");
		
//		<bdo> (HTMLElement)
		maps.put("bdo", "org.w3c.html.HTMLElement");
		
//		<span> (HTMLSpanElement)
		maps.put("span", "org.w3c.html.HTMLSpanElement");
		
//		<br / > (HTMLBRElement)
		maps.put("br", "org.w3c.html.HTMLBRElement");
		
//		<wbr> (HTMLElement)
		maps.put("wbr", "org.w3c.html.HTMLBRElement");



//		编修记录元素
//		<ins> (HTMLModElement)
		maps.put("ins", "org.w3c.html.HTMLModElement");
		
//		<del> (HTMLModElement)
		maps.put("del", "org.w3c.html.HTMLModElement");


//		内嵌媒体元素
//		<img> (HTMLImageElement)
		maps.put("img", "org.w3c.html.HTMLImageElement");
		
//		<iframe> (HTMLIFrameElement)
		maps.put("iframe", "org.w3c.html.HTMLIFrameElement");
		
//		<embed> (HTMLEmbedElement)
		maps.put("embed", "org.w3c.html.HTMLEmbedElement");
		
//		<object> (HTMLObjectElement)
		maps.put("object", "org.w3c.html.HTMLObjectElement");
		
//		<param> (HTMLParamElement)
		maps.put("param", "org.w3c.html.HTMLParamElement");
		
//		<video> (HTMLVideoElement)
		maps.put("video", "org.w3c.html.HTMLVideoElement");
		
//		<audio> (HTMLAudioElement)
		maps.put("audio", "org.w3c.html.HTMLAudioElement");
		
//		<source> (HTMLSourceElement)
		maps.put("source", "org.w3c.html.HTMLSourceElement");
		
//		<track> (HTMLTrackElement)
		maps.put("track", "org.w3c.html.HTMLTrackElement");
		
//		<canvas> (HTMLCanvasElement)
		maps.put("canvas", "org.w3c.html.HTMLCanvasElement");
		
//		<map> (HTMLMapElement)
		maps.put("map", "org.w3c.html.HTMLMapElement");
		
//		<area> (HTMLAreaElement)
		maps.put("area", "org.w3c.html.HTMLAreaElement");
		
//		<applet> (HTMLAppletElement)
		maps.put("applet", "org.w3c.html.HTMLAppletElement");   ////////////

//		表格元素
//		<table> (HTMLTableElement)
		maps.put("table", "org.w3c.html.HTMLTableElement");

//		<caption> (HTMLTableCaptionElement)
		maps.put("caption", "org.w3c.html.HTMLTableCaptionElement");
		
//		<colgroup> (HTMLTableColElement)
		maps.put("colgroup", "org.w3c.html.HTMLTableColElement");
	
//		<col> (HTMLTableColElement)
		maps.put("col", "org.w3c.html.HTMLTableColElement");
		
//		<tbody> (HTMLTableSectionElement)
		maps.put("tbody", "org.w3c.html.HTMLTableSectionElement");
	
//		<thead> (HTMLTableSectionElement)
		maps.put("thead", "org.w3c.html.HTMLTableSectionElement");

//		<tfoot> (HTMLTableSectionElement)
		maps.put("tfoot", "org.w3c.html.HTMLTableSectionElement");
	
//		<tr> (HTMLTableRowElement)
		maps.put("tr", "org.w3c.html.HTMLTableRowElement");
	
//		<td> (HTMLTableDataCellElement)
		maps.put("td", "org.w3c.html.HTMLTableDataCellElement");
		
//		<th> (HTMLTableHeaderCellElement)
		maps.put("th", "org.w3c.html.HTMLTableHeaderCellElement");


//		窗体元素
//		<form> (HTMLFormElement)
		maps.put("form", "org.w3c.html.HTMLFormElement");
		
//		<fieldset> (HTMLFieldSetElement)
		maps.put("fieldset", "org.w3c.html.HTMLFieldSetElement");
		
//		<legend> (HTMLLegendElement)
		maps.put("legend", "org.w3c.html.HTMLLegendElement");
		
//		<label> (HTMLLabelElement)
		maps.put("label", "org.w3c.html.HTMLLabelElement");
		
//		<input> (HTMLInputElement)
		maps.put("input", "org.w3c.html.HTMLInputElement");
		
//		<button> (HTMLButtonElement)
		maps.put("button", "org.w3c.html.HTMLButtonElement");
		
//		<select> (HTMLSelectElement)
		maps.put("select", "org.w3c.html.HTMLSelectElement");
		
//		<datalist> (HTMLDataListElement)
		maps.put("datalist", "org.w3c.html.HTMLDataListElement");
		
//		<optgroup> (HTMLOptGroupElement)
		maps.put("optgroup", "org.w3c.html.HTMLOptGroupElement");
		
//		<option> (HTMLOptionElement)
		maps.put("option", "org.w3c.html.HTMLOptionElement");
		
//		<textarea> (HTMLTextAreaElement)
		maps.put("textarea", "org.w3c.html.HTMLTextAreaElement");
		
//		<keygen> (HTMLKeygenElement)
		maps.put("keygen", "org.w3c.html.HTMLKeygenElement");
		
//		<output> (HTMLOutputElement)
		maps.put("output", "org.w3c.html.HTMLOutputElement");
		
//		<progress> (HTMLProgressElement)
		maps.put("progress", "org.w3c.html.HTMLProgressElement");

//		<meter> (HTMLMeterElement)
		maps.put("mete", "org.w3c.html.HTMLMeterElement");


//		交互式元素
//		<details> (HTMLDetailsElement)
		maps.put("details", "org.w3c.html.HTMLDetailsElement");
		
//		<summary> (HTMLElement)
		maps.put("summary", "org.w3c.html.HTMLElement");
		
//		<command> (HTMLCommandElement)
		maps.put("command", "org.w3c.html.HTMLCommandElement");
		
//		<menu> (HTMLMenuElement)
		maps.put("menu", "org.w3c.html.HTMLMenuElement");


//		maps.put("main", "org.w3c.html.HTMLElement");
//		maps.put("rb", "org.w3c.html.HTMLElement");
//		maps.put("rtc", "org.w3c.html.HTMLElement");
//		maps.put("media", "org.w3c.html.HTMLMediaElement");
//		maps.put("template", "org.w3c.html.HTMLTemplateElement");
	}
	
	public static char[] getMappingClass(char[] name){
		String result = maps.get(new String(name));
		if(result == null){
			return new char[0];
		}
		
		return result.toCharArray();
	}
}

