package org.summer.sdt.internal.compiler.html;

import static org.summer.sdt.internal.compiler.lookup.TypeConstants.*;

import org.summer.sdt.internal.compiler.util.HashtableOfCompoundName;

public class HtmlTags extends TagMapping{
	private final static  HashtableOfCompoundName maps = new HashtableOfCompoundName();
	static {
//		根元素
		maps.put("html".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLHtmlElement".toCharArray()});
		
//		文件数据元素
//		<head> (HTMLHeadElement)
		maps.put("head".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLHeadElement".toCharArray()});
//		<title> (HTMLTitleElement)
		maps.put("title".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTitleElement".toCharArray()});
//		<base> (HTMLBaseElement)
		maps.put("base".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLBaseElement".toCharArray()});
//		<link> (HTMLLinkElement)
		maps.put("link".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLLinkElement".toCharArray()});
//		<meta> (HTMLMetaElement)
		maps.put("meta".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLMetaElement".toCharArray()});
		
//		<style> (HTMLStyleElement)
		maps.put("style".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLStyleElement".toCharArray()});
//		<script> (HTMLScriptElement)
		maps.put("script".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLScriptElement".toCharArray()});
		
//		<noscript> (HTMLElement)

//		文件区域元素
//		<body> (HTMLBodyElement)
		maps.put("body".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLBodyElement".toCharArray()});
		
//		<section> (HTMLElement)
		maps.put("section".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<nav> (HTMLElement)
		maps.put("nav".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<article> (HTMLElement)
		maps.put("article".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<aside> (HTMLElement)
		maps.put("aside".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<h1> <h2> <h3> <h4> <h5> <h6> (HTMLHeadingElement)
		maps.put("h1".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLHeadingElement".toCharArray()});
		maps.put("h2".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLHeadingElement".toCharArray()});
		maps.put("h3".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLHeadingElement".toCharArray()});
		maps.put("h4".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLHeadingElement".toCharArray()});
		maps.put("h5".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLHeadingElement".toCharArray()});
		maps.put("h6".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLHeadingElement".toCharArray()});
		
//		<hgroup> (HTMLElement)
		maps.put("hgroup".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});

		//		<header> (HTMLElement)
		maps.put("header".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<footer> (HTMLElement)
		maps.put("footer".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<address> (HTMLElement)
		maps.put("address".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
		
//		群组元素
//		<p> (HTMLParagraphElement)
		maps.put("p".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLParagraphElement".toCharArray()});
		
//		<hr> (HTMLHRElement)
		maps.put("hr".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLHRElement".toCharArray()});
		
//		<pre> (HTMLPreElement)
		maps.put("pre".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLPreElement".toCharArray()});
		
//		<blockquote> (HTMLQuoteElement)
		maps.put("blockquote".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLQuoteElement".toCharArray()});
		
//		<ol> (HTMLOListElement)
		maps.put("ol".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLOListElement".toCharArray()});
		
//		<ul> (HTMLUListElement)
		maps.put("ul".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLOListElement".toCharArray()});
		
//		<li> (HTMLLIElement)
		maps.put("li".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLLIElement".toCharArray()});
		
//		<dl> (HTMLDListElement)
		maps.put("dl".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLDListElement".toCharArray()});

//		<dt> (HTMLElement)
		maps.put("dt".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<dd> (HTMLElement)
		maps.put("dd".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<figure> (HTMLElement)
		maps.put("figure".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<figcaption> (HTMLElement)
		maps.put("figcaption".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<div> (HTMLDivElement)
		maps.put("div".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLDivElement".toCharArray()});

		
//		文字层级元素
//		<a> (HTMLAnchorElement)
		maps.put("a".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLAnchorElement".toCharArray()});
		
//		<em> (HTMLElement)
		maps.put("em".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});

//		<strong> (HTMLElement)
		maps.put("strong".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<small> (HTMLElement)
		maps.put("small".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
//		<s> (HTMLElement)
		maps.put("s".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});

//		<cite> (HTMLElement)
		maps.put("cite".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});

//		<q> (HTMLQuoteElement)
		maps.put("q".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});

//		<dfn> (HTMLElement)
		maps.put("dfn".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});

//		<abbr> (HTMLElement)
		maps.put("abbr".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<data> (HTMLDataElement)
		
//		<time> (HTMLTimeElement)
		maps.put("time".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTimeElement".toCharArray()});
		
//		<code> (HTMLElement)
		maps.put("code".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<var> (HTMLElement)
		maps.put("var".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<samp> (HTMLElement)
		maps.put("samp".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<kbd> (HTMLElement)
		maps.put("kbd".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<sub> (HTMLElement)
		maps.put("sub".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<sup> (HTMLElement)
		maps.put("sup".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<i> (HTMLElement)
		maps.put("i".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<b> (HTMLElement)
		maps.put("b".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<u> (HTMLElement)
		maps.put("u".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<mark> (HTMLElement)
		maps.put("mark".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<ruby> (HTMLElement)
		maps.put("ruby".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<rt> (HTMLElement)
		maps.put("rt".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<rp> (HTMLElement)
		maps.put("rp".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<bdi> (HTMLElement)
		maps.put("bdi".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<bdo> (HTMLElement)
		maps.put("bdo".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<span> (HTMLSpanElement)
		maps.put("span".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLSpanElement".toCharArray()});
		
//		<br / > (HTMLBRElement)
		maps.put("br".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLBRElement".toCharArray()});
		
//		<wbr> (HTMLElement)
		maps.put("wbr".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLBRElement".toCharArray()});



//		编修记录元素
//		<ins> (HTMLModElement)
		maps.put("ins".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLModElement".toCharArray()});
		
//		<del> (HTMLModElement)
		maps.put("del".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLModElement".toCharArray()});


//		内嵌媒体元素
//		<img> (HTMLImageElement)
		maps.put("img".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLImageElement".toCharArray()});
		
//		<iframe> (HTMLIFrameElement)
		maps.put("iframe".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLIFrameElement".toCharArray()});
		
//		<embed> (HTMLEmbedElement)
		maps.put("embed".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLEmbedElement".toCharArray()});
		
//		<object> (HTMLObjectElement)
		maps.put("object".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLObjectElement".toCharArray()});
		
//		<param> (HTMLParamElement)
		maps.put("param".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLParamElement".toCharArray()});
		
//		<video> (HTMLVideoElement)
		maps.put("video".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLVideoElement".toCharArray()});
		
//		<audio> (HTMLAudioElement)
		maps.put("audio".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLAudioElement".toCharArray()});
		
//		<source> (HTMLSourceElement)
		maps.put("source".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLSourceElement".toCharArray()});
		
//		<track> (HTMLTrackElement)
		maps.put("track".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTrackElement".toCharArray()});
		
//		<canvas> (HTMLCanvasElement)
		maps.put("canvas".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLCanvasElement".toCharArray()});
		
//		<map> (HTMLMapElement)
		maps.put("map".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLMapElement".toCharArray()});
		
//		<area> (HTMLAreaElement)
		maps.put("area".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLAreaElement".toCharArray()});
		
//		<applet> (HTMLAppletElement)
		maps.put("applet".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLAppletElement".toCharArray()});   ////////////

//		表格元素
//		<table> (HTMLTableElement)
		maps.put("table".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTableElement".toCharArray()});

//		<caption> (HTMLTableCaptionElement)
		maps.put("caption".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTableCaptionElement".toCharArray()});
		
//		<colgroup> (HTMLTableColElement)
		maps.put("colgroup".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTableColElement".toCharArray()});
	
//		<col> (HTMLTableColElement)
		maps.put("col".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTableColElement".toCharArray()});
		
//		<tbody> (HTMLTableSectionElement)
		maps.put("tbody".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTableSectionElement".toCharArray()});
	
//		<thead> (HTMLTableSectionElement)
		maps.put("thead".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTableSectionElement".toCharArray()});

//		<tfoot> (HTMLTableSectionElement)
		maps.put("tfoot".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTableSectionElement".toCharArray()});
	
//		<tr> (HTMLTableRowElement)
		maps.put("tr".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTableRowElement".toCharArray()});
	
//		<td> (HTMLTableDataCellElement)
		maps.put("td".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTableDataCellElement".toCharArray()});
		
//		<th> (HTMLTableHeaderCellElement)
		maps.put("th".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTableHeaderCellElement".toCharArray()});


//		窗体元素
//		<form> (HTMLFormElement)
		maps.put("form".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLFormElement".toCharArray()});
		
//		<fieldset> (HTMLFieldSetElement)
		maps.put("fieldset".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLFieldSetElement".toCharArray()});
		
//		<legend> (HTMLLegendElement)
		maps.put("legend".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLLegendElement".toCharArray()});
		
//		<label> (HTMLLabelElement)
		maps.put("label".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLLabelElement".toCharArray()});
		
//		<input> (HTMLInputElement)
		maps.put("input".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLInputElement".toCharArray()});
		
//		<button> (HTMLButtonElement)
		maps.put("button".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLButtonElement".toCharArray()});
		
//		<select> (HTMLSelectElement)
		maps.put("select".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLSelectElement".toCharArray()});
		
//		<datalist> (HTMLDataListElement)
		maps.put("datalist".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLDataListElement".toCharArray()});
		
//		<optgroup> (HTMLOptGroupElement)
		maps.put("optgroup".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLOptGroupElement".toCharArray()});
		
//		<option> (HTMLOptionElement)
		maps.put("option".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLOptionElement".toCharArray()});
		
//		<textarea> (HTMLTextAreaElement)
		maps.put("textarea".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTextAreaElement".toCharArray()});
		
//		<keygen> (HTMLKeygenElement)
		maps.put("keygen".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLKeygenElement".toCharArray()});
		
//		<output> (HTMLOutputElement)
		maps.put("output".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLOutputElement".toCharArray()});
		
//		<progress> (HTMLProgressElement)
		maps.put("progress".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLProgressElement".toCharArray()});

//		<meter> (HTMLMeterElement)
		maps.put("mete".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLMeterElement".toCharArray()});


//		交互式元素
//		<details> (HTMLDetailsElement)
		maps.put("details".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLDetailsElement".toCharArray()});
		
//		<summary> (HTMLElement)
		maps.put("summary".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
		
//		<command> (HTMLCommandElement)
		maps.put("command".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLCommandElement".toCharArray()});
		
//		<menu> (HTMLMenuElement)
		maps.put("menu".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLMenuElement".toCharArray()});


//		maps.put("main".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
//		maps.put("rb".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
//		maps.put("rtc".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLElement".toCharArray()});
//		maps.put("media".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLMediaElement".toCharArray()});
//		maps.put("template".toCharArray(), new char[][]{ORG, W3C, HTMLPKG, "HTMLTemplateElement".toCharArray()});
		
		maps.put("text".toCharArray(), new char[][]{ORG, W3C, DOM, "Text".toCharArray()});
	}
	
	public char[][] getMappingClass(char[] name){
		char[][] result = maps.get(name);
		return result;
	}
}

