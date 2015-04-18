package org.summer.sdt.internal.compiler.html;

import java.util.HashMap;
import java.util.Map;
import static org.summer.sdt.internal.compiler.lookup.TypeConstants.*;

public class HtmlTags {
	private static final Map<String, char[][]> maps = new HashMap<String, char[][]>();
	static {
//		根元素
		maps.put("html", new char[][]{ORG, W3C, HTMLPKG, "HTMLHtmlElement".toCharArray()});
		
//		文件数据元素
//		<head> (HTMLHeadElement)
		maps.put("head", new char[][]{ORG, W3C, HTMLPKG, "HTMLHeadElement".toCharArray()});
//		<title> (HTMLTitleElement)
		maps.put("title", new char[][]{ORG, W3C, HTMLPKG, "HTMLTitleElement".toCharArray()});
//		<base> (HTMLBaseElement)
		maps.put("base", new char[][]{ORG, W3C, HTMLPKG, "HTMLBaseElement".toCharArray()});
//		<link> (HTMLLinkElement)
		maps.put("link", new char[][]{ORG, W3C, HTMLPKG, "HTMLLinkElement".toCharArray()});
//		<meta> (HTMLMetaElement)
		maps.put("meta", new char[][]{ORG, W3C, HTMLPKG, "HTMLMetaElement".toCharArray()});
		
//		<style> (HTMLStyleElement)
		maps.put("style", new char[][]{ORG, W3C, HTMLPKG, "HTMLStyleElement".toCharArray()});
//		<script> (HTMLScriptElement)
		maps.put("script", new char[][]{ORG, W3C, HTMLPKG, "HTMLScriptElement".toCharArray()});
		
//		<noscript> (HTMLElement)

//		文件区域元素
//		<body> (HTMLBodyElement)
		maps.put("body", new char[][]{ORG, W3C, HTMLPKG, "HTMLBodyElement".toCharArray()});
		
//		<section> (HTMLElement)
		maps.put("section", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<nav> (HTMLElement)
		maps.put("nav", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<article> (HTMLElement)
		maps.put("article", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<aside> (HTMLElement)
		maps.put("aside", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<h1> <h2> <h3> <h4> <h5> <h6> (HTMLHeadingElement)
		maps.put("h1", new char[][]{ORG, W3C, HTMLPKG, "HTMLHeadingElement".toCharArray()});
		maps.put("h2", new char[][]{ORG, W3C, HTMLPKG, "HTMLHeadingElement".toCharArray()});
		maps.put("h3", new char[][]{ORG, W3C, HTMLPKG, "HTMLHeadingElement".toCharArray()});
		maps.put("h4", new char[][]{ORG, W3C, HTMLPKG, "HTMLHeadingElement".toCharArray()});
		maps.put("h5", new char[][]{ORG, W3C, HTMLPKG, "HTMLHeadingElement".toCharArray()});
		maps.put("h6", new char[][]{ORG, W3C, HTMLPKG, "HTMLHeadingElement".toCharArray()});
		
//		<hgroup> (HTMLElement)
		maps.put("hgroup", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});

		//		<header> (HTMLElement)
		maps.put("header", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<footer> (HTMLElement)
		maps.put("footer", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<address> (HTMLElement)
		maps.put("address", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
		
//		群组元素
//		<p> (HTMLParagraphElement)
		maps.put("p", new char[][]{ORG, W3C, HTMLPKG, "HTMLParagraphElement".toCharArray()});
		
//		<hr> (HTMLHRElement)
		maps.put("hr", new char[][]{ORG, W3C, HTMLPKG, "HTMLHRElement".toCharArray()});
		
//		<pre> (HTMLPreElement)
		maps.put("pre", new char[][]{ORG, W3C, HTMLPKG, "HTMLPreElement".toCharArray()});
		
//		<blockquote> (HTMLQuoteElement)
		maps.put("blockquote", new char[][]{ORG, W3C, HTMLPKG, "HTMLQuoteElement".toCharArray()});
		
//		<ol> (HTMLOListElement)
		maps.put("ol", new char[][]{ORG, W3C, HTMLPKG, "HTMLOListElement".toCharArray()});
		
//		<ul> (HTMLUListElement)
		maps.put("ul", new char[][]{ORG, W3C, HTMLPKG, "HTMLOListElement".toCharArray()});
		
//		<li> (HTMLLIElement)
		maps.put("li", new char[][]{ORG, W3C, HTMLPKG, "HTMLLIElement".toCharArray()});
		
//		<dl> (HTMLDListElement)
		maps.put("dl", new char[][]{ORG, W3C, HTMLPKG, "HTMLDListElement".toCharArray()});

//		<dt> (HTMLElement)
		maps.put("dt", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<dd> (HTMLElement)
		maps.put("dd", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<figure> (HTMLElement)
		maps.put("figure", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<figcaption> (HTMLElement)
		maps.put("figcaption", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<div> (HTMLDivElement)
		maps.put("div", new char[][]{ORG, W3C, HTMLPKG, "HTMLDivElement".toCharArray()});

		
//		文字层级元素
//		<a> (HTMLAnchorElement)
		maps.put("a", new char[][]{ORG, W3C, HTMLPKG, "HTMLAnchorElement".toCharArray()});
		
//		<em> (HTMLElement)
		maps.put("em", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});

//		<strong> (HTMLElement)
		maps.put("strong", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<small> (HTMLElement)
		maps.put("small", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
//		<s> (HTMLElement)
		maps.put("s", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});

//		<cite> (HTMLElement)
		maps.put("cite", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});

//		<q> (HTMLQuoteElement)
		maps.put("q", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});

//		<dfn> (HTMLElement)
		maps.put("dfn", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});

//		<abbr> (HTMLElement)
		maps.put("abbr", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<data> (HTMLDataElement)
		
//		<time> (HTMLTimeElement)
		maps.put("time", new char[][]{ORG, W3C, HTMLPKG, "HTMLTimeElement".toCharArray()});
		
//		<code> (HTMLElement)
		maps.put("code", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<var> (HTMLElement)
		maps.put("var", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<samp> (HTMLElement)
		maps.put("samp", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<kbd> (HTMLElement)
		maps.put("kbd", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<sub> (HTMLElement)
		maps.put("sub", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<sup> (HTMLElement)
		maps.put("sup", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<i> (HTMLElement)
		maps.put("i", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<b> (HTMLElement)
		maps.put("b", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<u> (HTMLElement)
		maps.put("u", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<mark> (HTMLElement)
		maps.put("mark", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<ruby> (HTMLElement)
		maps.put("ruby", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<rt> (HTMLElement)
		maps.put("rt", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<rp> (HTMLElement)
		maps.put("rp", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<bdi> (HTMLElement)
		maps.put("bdi", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<bdo> (HTMLElement)
		maps.put("bdo", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<span> (HTMLSpanElement)
		maps.put("span", new char[][]{ORG, W3C, HTMLPKG, "HTMLSpanElement".toCharArray()});
		
//		<br / > (HTMLBRElement)
		maps.put("br", new char[][]{ORG, W3C, HTMLPKG, "HTMLBRElement".toCharArray()});
		
//		<wbr> (HTMLElement)
		maps.put("wbr", new char[][]{ORG, W3C, HTMLPKG, "HTMLBRElement".toCharArray()});



//		编修记录元素
//		<ins> (HTMLModElement)
		maps.put("ins", new char[][]{ORG, W3C, HTMLPKG, "HTMLModElement".toCharArray()});
		
//		<del> (HTMLModElement)
		maps.put("del", new char[][]{ORG, W3C, HTMLPKG, "HTMLModElement".toCharArray()});


//		内嵌媒体元素
//		<img> (HTMLImageElement)
		maps.put("img", new char[][]{ORG, W3C, HTMLPKG, "HTMLImageElement".toCharArray()});
		
//		<iframe> (HTMLIFrameElement)
		maps.put("iframe", new char[][]{ORG, W3C, HTMLPKG, "HTMLIFrameElement".toCharArray()});
		
//		<embed> (HTMLEmbedElement)
		maps.put("embed", new char[][]{ORG, W3C, HTMLPKG, "HTMLEmbedElement".toCharArray()});
		
//		<object> (HTMLObjectElement)
		maps.put("object", new char[][]{ORG, W3C, HTMLPKG, "HTMLObjectElement".toCharArray()});
		
//		<param> (HTMLParamElement)
		maps.put("param", new char[][]{ORG, W3C, HTMLPKG, "HTMLParamElement".toCharArray()});
		
//		<video> (HTMLVideoElement)
		maps.put("video", new char[][]{ORG, W3C, HTMLPKG, "HTMLVideoElement".toCharArray()});
		
//		<audio> (HTMLAudioElement)
		maps.put("audio", new char[][]{ORG, W3C, HTMLPKG, "HTMLAudioElement".toCharArray()});
		
//		<source> (HTMLSourceElement)
		maps.put("source", new char[][]{ORG, W3C, HTMLPKG, "HTMLSourceElement".toCharArray()});
		
//		<track> (HTMLTrackElement)
		maps.put("track", new char[][]{ORG, W3C, HTMLPKG, "HTMLTrackElement".toCharArray()});
		
//		<canvas> (HTMLCanvasElement)
		maps.put("canvas", new char[][]{ORG, W3C, HTMLPKG, "HTMLCanvasElement".toCharArray()});
		
//		<map> (HTMLMapElement)
		maps.put("map", new char[][]{ORG, W3C, HTMLPKG, "HTMLMapElement".toCharArray()});
		
//		<area> (HTMLAreaElement)
		maps.put("area", new char[][]{ORG, W3C, HTMLPKG, "HTMLAreaElement".toCharArray()});
		
//		<applet> (HTMLAppletElement)
		maps.put("applet", new char[][]{ORG, W3C, HTMLPKG, "HTMLAppletElement".toCharArray()});   ////////////

//		表格元素
//		<table> (HTMLTableElement)
		maps.put("table", new char[][]{ORG, W3C, HTMLPKG, "HTMLTableElement".toCharArray()});

//		<caption> (HTMLTableCaptionElement)
		maps.put("caption", new char[][]{ORG, W3C, HTMLPKG, "HTMLTableCaptionElement".toCharArray()});
		
//		<colgroup> (HTMLTableColElement)
		maps.put("colgroup", new char[][]{ORG, W3C, HTMLPKG, "HTMLTableColElement".toCharArray()});
	
//		<col> (HTMLTableColElement)
		maps.put("col", new char[][]{ORG, W3C, HTMLPKG, "HTMLTableColElement".toCharArray()});
		
//		<tbody> (HTMLTableSectionElement)
		maps.put("tbody", new char[][]{ORG, W3C, HTMLPKG, "HTMLTableSectionElement".toCharArray()});
	
//		<thead> (HTMLTableSectionElement)
		maps.put("thead", new char[][]{ORG, W3C, HTMLPKG, "HTMLTableSectionElement".toCharArray()});

//		<tfoot> (HTMLTableSectionElement)
		maps.put("tfoot", new char[][]{ORG, W3C, HTMLPKG, "HTMLTableSectionElement".toCharArray()});
	
//		<tr> (HTMLTableRowElement)
		maps.put("tr", new char[][]{ORG, W3C, HTMLPKG, "HTMLTableRowElement".toCharArray()});
	
//		<td> (HTMLTableDataCellElement)
		maps.put("td", new char[][]{ORG, W3C, HTMLPKG, "HTMLTableDataCellElement".toCharArray()});
		
//		<th> (HTMLTableHeaderCellElement)
		maps.put("th", new char[][]{ORG, W3C, HTMLPKG, "HTMLTableHeaderCellElement".toCharArray()});


//		窗体元素
//		<form> (HTMLFormElement)
		maps.put("form", new char[][]{ORG, W3C, HTMLPKG, "HTMLFormElement".toCharArray()});
		
//		<fieldset> (HTMLFieldSetElement)
		maps.put("fieldset", new char[][]{ORG, W3C, HTMLPKG, "HTMLFieldSetElement".toCharArray()});
		
//		<legend> (HTMLLegendElement)
		maps.put("legend", new char[][]{ORG, W3C, HTMLPKG, "HTMLLegendElement".toCharArray()});
		
//		<label> (HTMLLabelElement)
		maps.put("label", new char[][]{ORG, W3C, HTMLPKG, "HTMLLabelElement".toCharArray()});
		
//		<input> (HTMLInputElement)
		maps.put("input", new char[][]{ORG, W3C, HTMLPKG, "HTMLInputElement".toCharArray()});
		
//		<button> (HTMLButtonElement)
		maps.put("button", new char[][]{ORG, W3C, HTMLPKG, "HTMLButtonElement".toCharArray()});
		
//		<select> (HTMLSelectElement)
		maps.put("select", new char[][]{ORG, W3C, HTMLPKG, "HTMLSelectElement".toCharArray()});
		
//		<datalist> (HTMLDataListElement)
		maps.put("datalist", new char[][]{ORG, W3C, HTMLPKG, "HTMLDataListElement".toCharArray()});
		
//		<optgroup> (HTMLOptGroupElement)
		maps.put("optgroup", new char[][]{ORG, W3C, HTMLPKG, "HTMLOptGroupElement".toCharArray()});
		
//		<option> (HTMLOptionElement)
		maps.put("option", new char[][]{ORG, W3C, HTMLPKG, "HTMLOptionElement".toCharArray()});
		
//		<textarea> (HTMLTextAreaElement)
		maps.put("textarea", new char[][]{ORG, W3C, HTMLPKG, "HTMLTextAreaElement".toCharArray()});
		
//		<keygen> (HTMLKeygenElement)
		maps.put("keygen", new char[][]{ORG, W3C, HTMLPKG, "HTMLKeygenElement".toCharArray()});
		
//		<output> (HTMLOutputElement)
		maps.put("output", new char[][]{ORG, W3C, HTMLPKG, "HTMLOutputElement".toCharArray()});
		
//		<progress> (HTMLProgressElement)
		maps.put("progress", new char[][]{ORG, W3C, HTMLPKG, "HTMLProgressElement".toCharArray()});

//		<meter> (HTMLMeterElement)
		maps.put("mete", new char[][]{ORG, W3C, HTMLPKG, "HTMLMeterElement".toCharArray()});


//		交互式元素
//		<details> (HTMLDetailsElement)
		maps.put("details", new char[][]{ORG, W3C, HTMLPKG, "HTMLDetailsElement".toCharArray()});
		
//		<summary> (HTMLElement)
		maps.put("summary", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<command> (HTMLCommandElement)
		maps.put("command", new char[][]{ORG, W3C, HTMLPKG, "HTMLCommandElement".toCharArray()});
		
//		<menu> (HTMLMenuElement)
		maps.put("menu", new char[][]{ORG, W3C, HTMLPKG, "HTMLMenuElement".toCharArray()});


//		maps.put("main", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
//		maps.put("rb", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
//		maps.put("rtc", new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
//		maps.put("media", new char[][]{ORG, W3C, HTMLPKG, "HTMLMediaElement".toCharArray()});
//		maps.put("template", new char[][]{ORG, W3C, HTMLPKG, "HTMLTemplateElement".toCharArray()});
	}
	
	public static char[][] getMappingClass(char[] name){
		char[][] result = maps.get(new String(name));
		return result;
	}
}

