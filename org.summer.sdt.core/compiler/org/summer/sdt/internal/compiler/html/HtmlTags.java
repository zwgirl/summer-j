package org.summer.sdt.internal.compiler.html;

import java.util.HashMap;
import java.util.Map;

public class HtmlTags {
	private static final Map<String, String> maps = new HashMap<String, String>();
	static {
//		根元素
		maps.put("html", "HTMLHtmlElement");
		
//		文件数据元素
//		<head> (HTMLHeadElement)
		maps.put("head", "HTMLHeadElement");
//		<title> (HTMLTitleElement)
		maps.put("title", "HTMLTitleElement");
//		<base> (HTMLBaseElement)
		maps.put("base", "HTMLBaseElement");
//		<link> (HTMLLinkElement)
		maps.put("link", "HTMLLinkElement");
//		<meta> (HTMLMetaElement)
		maps.put("meta", "HTMLMetaElement");
		
//		<style> (HTMLStyleElement)
		maps.put("style", "HTMLStyleElement");
//		<script> (HTMLScriptElement)
		maps.put("script", "HTMLScriptElement");
		
//		<noscript> (HTMLElement)

//		文件区域元素
//		<body> (HTMLBodyElement)
		maps.put("body", "HTMLBodyElement");
		
//		<section> (HTMLElement)
		maps.put("section", "HTMLElement");
		
//		<nav> (HTMLElement)
		maps.put("nav", "HTMLElement");
		
//		<article> (HTMLElement)
		maps.put("article", "HTMLElement");
		
//		<aside> (HTMLElement)
		maps.put("aside", "HTMLElement");
		
//		<h1> <h2> <h3> <h4> <h5> <h6> (HTMLHeadingElement)
		maps.put("h1", "HTMLHeadingElement");
		maps.put("h2", "HTMLHeadingElement");
		maps.put("h3", "HTMLHeadingElement");
		maps.put("h4", "HTMLHeadingElement");
		maps.put("h5", "HTMLHeadingElement");
		maps.put("h6", "HTMLHeadingElement");
		
//		<hgroup> (HTMLElement)
		maps.put("hgroup", "HTMLElement");

		//		<header> (HTMLElement)
		maps.put("header", "HTMLElement");
		
//		<footer> (HTMLElement)
		maps.put("footer", "HTMLElement");
		
//		<address> (HTMLElement)
		maps.put("address", "HTMLElement");
		
		
//		群组元素
//		<p> (HTMLParagraphElement)
		maps.put("p", "HTMLParagraphElement");
		
//		<hr> (HTMLHRElement)
		maps.put("hr", "HTMLHRElement");
		
//		<pre> (HTMLPreElement)
		maps.put("pre", "HTMLPreElement");
		
//		<blockquote> (HTMLQuoteElement)
		maps.put("blockquote", "HTMLQuoteElement");
		
//		<ol> (HTMLOListElement)
		maps.put("ol", "HTMLOListElement");
		
//		<ul> (HTMLUListElement)
		maps.put("ul", "HTMLOListElement");
		
//		<li> (HTMLLIElement)
		maps.put("li", "HTMLLIElement");
		
//		<dl> (HTMLDListElement)
		maps.put("dl", "HTMLDListElement");

//		<dt> (HTMLElement)
		maps.put("dt", "HTMLElement");
		
//		<dd> (HTMLElement)
		maps.put("dd", "HTMLElement");
		
//		<figure> (HTMLElement)
		maps.put("figure", "HTMLElement");
		
//		<figcaption> (HTMLElement)
		maps.put("figcaption", "HTMLElement");
		
//		<div> (HTMLDivElement)
		maps.put("div", "HTMLDivElement");

		
//		文字层级元素
//		<a> (HTMLAnchorElement)
		maps.put("a", "HTMLAnchorElement");
		
//		<em> (HTMLElement)
		maps.put("em", "HTMLElement");

//		<strong> (HTMLElement)
		maps.put("strong", "HTMLElement");
		
//		<small> (HTMLElement)
		maps.put("small", "HTMLElement");
//		<s> (HTMLElement)
		maps.put("s", "HTMLElement");

//		<cite> (HTMLElement)
		maps.put("cite", "HTMLElement");

//		<q> (HTMLQuoteElement)
		maps.put("q", "HTMLElement");

//		<dfn> (HTMLElement)
		maps.put("dfn", "HTMLElement");

//		<abbr> (HTMLElement)
		maps.put("abbr", "HTMLElement");
		
//		<data> (HTMLDataElement)
		
//		<time> (HTMLTimeElement)
		maps.put("time", "HTMLTimeElement");
		
//		<code> (HTMLElement)
		maps.put("code", "HTMLElement");
		
//		<var> (HTMLElement)
		maps.put("var", "HTMLElement");
		
//		<samp> (HTMLElement)
		maps.put("samp", "HTMLElement");
		
//		<kbd> (HTMLElement)
		maps.put("kbd", "HTMLElement");
		
//		<sub> (HTMLElement)
		maps.put("sub", "HTMLElement");
		
//		<sup> (HTMLElement)
		maps.put("sup", "HTMLElement");
		
//		<i> (HTMLElement)
		maps.put("i", "HTMLElement");
		
//		<b> (HTMLElement)
		maps.put("b", "HTMLElement");
		
//		<u> (HTMLElement)
		maps.put("u", "HTMLElement");
		
//		<mark> (HTMLElement)
		maps.put("mark", "HTMLElement");
		
//		<ruby> (HTMLElement)
		maps.put("ruby", "HTMLElement");
		
//		<rt> (HTMLElement)
		maps.put("rt", "HTMLElement");
		
//		<rp> (HTMLElement)
		maps.put("rp", "HTMLElement");
		
//		<bdi> (HTMLElement)
		maps.put("bdi", "HTMLElement");
		
//		<bdo> (HTMLElement)
		maps.put("bdo", "HTMLElement");
		
//		<span> (HTMLSpanElement)
		maps.put("span", "HTMLSpanElement");
		
//		<br / > (HTMLBRElement)
		maps.put("br", "HTMLBRElement");
		
//		<wbr> (HTMLElement)
		maps.put("wbr", "HTMLBRElement");



//		编修记录元素
//		<ins> (HTMLModElement)
		maps.put("ins", "HTMLModElement");
		
//		<del> (HTMLModElement)
		maps.put("del", "HTMLModElement");


//		内嵌媒体元素
//		<img> (HTMLImageElement)
		maps.put("img", "HTMLImageElement");
		
//		<iframe> (HTMLIFrameElement)
		maps.put("iframe", "HTMLIFrameElement");
		
//		<embed> (HTMLEmbedElement)
		maps.put("embed", "HTMLEmbedElement");
		
//		<object> (HTMLObjectElement)
		maps.put("object", "HTMLObjectElement");
		
//		<param> (HTMLParamElement)
		maps.put("param", "HTMLParamElement");
		
//		<video> (HTMLVideoElement)
		maps.put("video", "HTMLVideoElement");
		
//		<audio> (HTMLAudioElement)
		maps.put("audio", "HTMLAudioElement");
		
//		<source> (HTMLSourceElement)
		maps.put("source", "HTMLSourceElement");
		
//		<track> (HTMLTrackElement)
		maps.put("track", "HTMLTrackElement");
		
//		<canvas> (HTMLCanvasElement)
		maps.put("canvas", "HTMLCanvasElement");
		
//		<map> (HTMLMapElement)
		maps.put("map", "HTMLMapElement");
		
//		<area> (HTMLAreaElement)
		maps.put("area", "HTMLAreaElement");
		
//		<applet> (HTMLAppletElement)
		maps.put("applet", "HTMLAppletElement");   ////////////

//		表格元素
//		<table> (HTMLTableElement)
		maps.put("table", "HTMLTableElement");

//		<caption> (HTMLTableCaptionElement)
		maps.put("caption", "HTMLTableCaptionElement");
		
//		<colgroup> (HTMLTableColElement)
		maps.put("colgroup", "HTMLTableColElement");
	
//		<col> (HTMLTableColElement)
		maps.put("col", "HTMLTableColElement");
		
//		<tbody> (HTMLTableSectionElement)
		maps.put("tbody", "HTMLTableSectionElement");
	
//		<thead> (HTMLTableSectionElement)
		maps.put("thead", "HTMLTableSectionElement");

//		<tfoot> (HTMLTableSectionElement)
		maps.put("tfoot", "HTMLTableSectionElement");
	
//		<tr> (HTMLTableRowElement)
		maps.put("tr", "HTMLTableRowElement");
	
//		<td> (HTMLTableDataCellElement)
		maps.put("td", "HTMLTableDataCellElement");
		
//		<th> (HTMLTableHeaderCellElement)
		maps.put("th", "HTMLTableHeaderCellElement");


//		窗体元素
//		<form> (HTMLFormElement)
		maps.put("form", "HTMLFormElement");
		
//		<fieldset> (HTMLFieldSetElement)
		maps.put("fieldset", "HTMLFieldSetElement");
		
//		<legend> (HTMLLegendElement)
		maps.put("legend", "HTMLLegendElement");
		
//		<label> (HTMLLabelElement)
		maps.put("label", "HTMLLabelElement");
		
//		<input> (HTMLInputElement)
		maps.put("input", "HTMLInputElement");
		
//		<button> (HTMLButtonElement)
		maps.put("button", "HTMLButtonElement");
		
//		<select> (HTMLSelectElement)
		maps.put("select", "HTMLSelectElement");
		
//		<datalist> (HTMLDataListElement)
		maps.put("datalist", "HTMLDataListElement");
		
//		<optgroup> (HTMLOptGroupElement)
		maps.put("optgroup", "HTMLOptGroupElement");
		
//		<option> (HTMLOptionElement)
		maps.put("option", "HTMLOptionElement");
		
//		<textarea> (HTMLTextAreaElement)
		maps.put("textarea", "HTMLTextAreaElement");
		
//		<keygen> (HTMLKeygenElement)
		maps.put("keygen", "HTMLKeygenElement");
		
//		<output> (HTMLOutputElement)
		maps.put("output", "HTMLOutputElement");
		
//		<progress> (HTMLProgressElement)
		maps.put("progress", "HTMLProgressElement");

//		<meter> (HTMLMeterElement)
		maps.put("mete", "HTMLMeterElement");


//		交互式元素
//		<details> (HTMLDetailsElement)
		maps.put("details", "HTMLDetailsElement");
		
//		<summary> (HTMLElement)
		maps.put("summary", "HTMLElement");
		
//		<command> (HTMLCommandElement)
		maps.put("command", "HTMLCommandElement");
		
//		<menu> (HTMLMenuElement)
		maps.put("menu", "HTMLMenuElement");


//		maps.put("main", "HTMLElement");
//		maps.put("rb", "HTMLElement");
//		maps.put("rtc", "HTMLElement");
//		maps.put("media", "HTMLMediaElement");
//		maps.put("template", "HTMLTemplateElement");
	}
	
	public static char[] getMappingClass(char[] name){
		String result = maps.get(new String(name));
		if(result == null){
			return new char[0];
		}
		
		return result.toCharArray();
	}
}

