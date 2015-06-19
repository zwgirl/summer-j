package org.summer.sdt.internal.compiler.html;

import static org.summer.sdt.internal.compiler.lookup.TypeConstants.ORG;
import static org.summer.sdt.internal.compiler.lookup.TypeConstants.SVGPKG;
import static org.summer.sdt.internal.compiler.lookup.TypeConstants.W3C;

import org.summer.sdt.internal.compiler.util.HashtableOfCompoundName;

public final class SvgTags extends TagMapping{
	public static final char[] SVG = "svg".toCharArray();
	
	private final static  HashtableOfCompoundName tagMaps = new HashtableOfCompoundName();
	static{

		/**
		* <p>A key interface definition is the <a>SVGSVGElement</a> interface,
		* which is the interface that corresponds to the <a>'svg'</a> element. This
		* interface contains various miscellaneous commonly-used utility
		* methods, such as matrix operations and the ability to control the
		* time of redraw on visual rendering devices.</p>
		*
		* <p><a>SVGSVGElement</a> extends <a>ViewCSS</a> and <a>DocumentCSS</a> to
		* provide access to the computed values of properties and the override style
		* sheet as described in <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113/"><cite>DOM Level 2 Style</cite></a>
		* [<a href="refs.html#ref-DOM2STYLE">DOM2STYLE</a>].</p>
		*/
		tagMaps.put("svg".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGSVGElement".toCharArray()});

		/**
		* The <a>SVGSVGElement</a> interface corresponds to the <a>'g'</a> element.
		*/
		tagMaps.put("g".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGSVGElement".toCharArray()});
		
		/**
		* The <a>SVGDefsElement</a> interface corresponds to the <a>'defs'</a>
		* element.
		*/
		tagMaps.put("defs".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGDefsElement".toCharArray()});

		/**
		* The <a>SVGDescElement</a> interface corresponds to the <a>'desc'</a>
		* element.
		*/
		tagMaps.put("desc".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGDescElement".toCharArray()});

		/**
		* The <a>SVGTitleElement</a> interface corresponds to the <a>'title'</a>
		* element.
		*/
		tagMaps.put("title".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGTitleElement".toCharArray()});

		/**
		* The <a>SVGSymbolElement</a> interface corresponds to the <a>'symbol'</a>
		* element.
		*/
		tagMaps.put("symbol".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGSymbolElement".toCharArray()});

		/**
		* The <a>SVGUseElement</a> interface corresponds to the <a>'use'</a> element.
		*/
		tagMaps.put("use".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGUseElement".toCharArray()});

		/**
		* The <a>SVGImageElement</a> interface corresponds to the <a>'image'</a>
		* element.
		*/
		tagMaps.put("image".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGImageElement".toCharArray()});


		/**
		* The <a>SVGSwitchElement</a> interface corresponds to the <a>'switch'</a>
		* element.
		*/
		tagMaps.put("switch".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGSwitchElement".toCharArray()});


		/**
		* The <a>SVGStyleElement</a> interface corresponds to the <a>'style element'</a>
		* element.
		*/
		tagMaps.put("style".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGStyleElement".toCharArray()});
//
//	
//		/**
//		* <p>Many of the SVG DOM interfaces refer to objects of class
//		* <a>SVGPoint</a>. An <a>SVGPoint</a> is an (x,聽y) coordinate pair. When
//		* used in matrix operations, an <a>SVGPoint</a> is treated as a vector of
//		* the form:</p>
//		*
//		* <pre>
//		[x]
//		[y]
//		[1]</pre>
//		*
//		* <p>If an <a>SVGRect</a> object is designated as <em>read only</em>,
//		* then attempting to assign to one of its attributes will result in
//		* an exception being thrown.</p>
//		*/
//		public native interface SVGPoint { 
//
//		/**
//		* <p>This interface defines a list of SVGPoint objects.</p>
//		*
//		* <p><a>SVGPointList</a> has the same attributes and methods as other
//		* SVGxxxList interfaces. Implementers may consider using a single base class
//		* to implement the various SVGxxxList interfaces.</p>
//		*/
//		public native interface SVGPointList { 
//
//		/**
//		* <p>Many of SVG's graphics operations utilize 2x3 matrices of the form:</p>
//		*
//		* <pre>
//		[a c e]
//		[b d f]</pre>
//		*
//		* <p>which, when expanded into a 3x3 matrix for the purposes of matrix
//		* arithmetic, become:</p>
//		*
//		* <pre>
//		[a c e]
//		[b d f]
//		[0 0 1]</pre>
//		*/
//		public native interface SVGMatrix { 
//
//		/**
//		* <a>SVGTransform</a> is the interface for one of the component
//		* transformations within an <a>SVGTransformList</a>; thus, an
//		* <a>SVGTransform</a> object corresponds to a single component (e.g.,
//		* <span class='attr-value'>'scale(鈥�)'</span> or
//		* <span class='attr-value'>'matrix(鈥�)'</span>) within a <a>'transform'</a>
//		* attribute specification.
//		*/
//		public native interface SVGTransform { 
//
//		/**
//		* <p>This interface defines a list of SVGTransform objects.</p>
//		*
//		* <p>The <a>SVGTransformList</a> and <a>SVGTransform</a> interfaces correspond
//		* to the various attributes which specify a set of transformations, such as
//		* the <a>'transform'</a> attribute which is available for many of SVG's elements.</p>
//		* 
//		* <p><a>SVGTransformList</a> has the same attributes and methods as other
//		* SVGxxxList interfaces. Implementers may consider using a single base class
//		* to implement the various SVGxxxList interfaces.</p>
//		*
//		* <p id="ReadOnlyTransformList">An <a>SVGTransformList</a> object can be designated as <em>read only</em>,
//		* which means that attempts to modify the object will result in an exception
//		* being thrown, as described below.</p>
//		*/
//		public native interface SVGTransformList { 
//
//
//		/**
//		* Used for the various attributes which specify a set of transformations,
//		* such as the <a>'transform'</a> attribute which is available for many of
//		* SVG's elements, and which can be animated.
//		*/
//		public native interface SVGAnimatedTransformList { 
//
//		/**
//		* The <a>SVGPreserveAspectRatio</a> interface corresponds to the
//		* <a>'preserveAspectRatio'</a> attribute, which is available for some of
//		* SVG's elements.
//		*
//		* <p id="ReadOnlyPreserveAspectRatio">An <a>SVGPreserveAspectRatio</a> object can be designated as <em>read only</em>,
//		* which means that attempts to modify the object will result in an exception
//		* being thrown, as described below.</p>
//		*/
//		public native interface SVGPreserveAspectRatio { 
//
//		/**
//		* Used for attributes of type <a>SVGPreserveAspectRatio</a> which can be
//		* animated. 
//		*/
//		public native interface SVGAnimatedPreserveAspectRatio { 
//
//		/**
//		* The <a>SVGPathSeg</a> interface is a base interface that corresponds to a
//		* single command within a path data specification.
//		*/
//		public native interface SVGPathSeg { 
//
//		/**
//		* The <a>SVGPathSegClosePath</a> interface corresponds to a
//		* "closepath" (z) path data command.
//		*/
//		public native interface SVGPathSegClosePath extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegMovetoAbs</a> interface corresponds to an
//		* "absolute moveto" (M) path data command.
//		*/
//		public native interface SVGPathSegMovetoAbs extends SVGPathSeg {
//
//
//		/**
//		* The <a>SVGPathSegMovetoRel</a> interface corresponds to a
//		* "relative moveto" (m) path data command.
//		*/
//		public native interface SVGPathSegMovetoRel extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegLinetoAbs</a> interface corresponds to an
//		* "absolute lineto" (L) path data command.
//		*/
//		public native interface SVGPathSegLinetoAbs extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegLinetoRel</a> interface corresponds to a
//		* "relative lineto" (l) path data command.
//		*/
//		public native interface SVGPathSegLinetoRel extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegCurvetoCubicAbs</a> interface corresponds to an
//		* "absolute cubic B茅zier curveto" (C) path data command.
//		*/
//		public native interface SVGPathSegCurvetoCubicAbs extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegCurvetoCubicRel</a> interface corresponds to a
//		* "relative cubic B茅zier curveto" (c) path data command.
//		*/
//		public native interface SVGPathSegCurvetoCubicRel extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegCurvetoQuadraticAbs</a> interface corresponds to an
//		* "absolute quadratic B茅zier curveto" (Q) path data command.
//		*/
//		public native interface SVGPathSegCurvetoQuadraticAbs extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegCurvetoQuadraticRel</a> interface corresponds to a
//		* "relative quadratic B茅zier curveto" (q) path data command.
//		*/
//		public native interface SVGPathSegCurvetoQuadraticRel extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegArcAbs</a> interface corresponds to an
//		* "absolute arcto" (A) path data command.
//		*/
//		public native interface SVGPathSegArcAbs extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegArcRel</a> interface corresponds to a
//		* "relative arcto" (a) path data command.
//		*/
//		public native interface SVGPathSegArcRel extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegLinetoHorizontalAbs</a> interface corresponds to an
//		* "absolute horizontal lineto" (H) path data command.
//		*/
//		public native interface SVGPathSegLinetoHorizontalAbs extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegLinetoHorizontalRel</a> interface corresponds to a
//		* "relative horizontal lineto" (h) path data command.
//		*/
//		public native interface SVGPathSegLinetoHorizontalRel extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegLinetoVerticalAbs</a> interface corresponds to an
//		* "absolute vertical lineto" (V) path data command.
//		*/
//		interface SVGPathSegLinetoVerticalAbs extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegLinetoVerticalRel</a> interface corresponds to a
//		* "relative vertical lineto" (v) path data command.
//		*/
//		interface SVGPathSegLinetoVerticalRel extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegCurvetoCubicSmoothAbs</a> interface corresponds to an
//		* "absolute smooth cubic curveto" (S) path data command.
//		*/
//		interface SVGPathSegCurvetoCubicSmoothAbs extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegCurvetoCubicSmoothRel</a> interface corresponds to a
//		* "relative smooth cubic curveto" (s) path data command.
//		*/
//		interface SVGPathSegCurvetoCubicSmoothRel extends SVGPathSeg {
//		/**
//		* The <a>SVGPathSegCurvetoQuadraticSmoothAbs</a> interface corresponds to an
//		* "absolute smooth cubic curveto" (T) path data command.
//		*/
//		public native interface SVGPathSegCurvetoQuadraticSmoothAbs extends SVGPathSeg {
//
//		/**
//		* The <a>SVGPathSegCurvetoQuadraticSmoothRel</a> interface corresponds to a
//		* "relative smooth cubic curveto" (t) path data command.
//		*/
//		public native interface SVGPathSegCurvetoQuadraticSmoothRel extends SVGPathSeg {
//		/**
//		* <p>This interface defines a list of SVGPathSeg objects.</p>
//		*
//		* <p><a>SVGPathSegList</a> has the same attributes and methods as other
//		* SVGxxxList interfaces. Implementers may consider using a single base class
//		* to implement the various SVGxxxList interfaces.</p>
//		*/
//		public native interface SVGPathSegList { 
//
//		/**
//		* <p>The SVGAnimatedPathData interface supports elements which have a <span class='attr-name'>'d'</span>
//		* attribute which holds SVG path data, and supports the ability to animate
//		* that attribute.</p>
//		*
//		* <p>The <a>SVGAnimatedPathData</a> interface provides two lists to access and
//		* modify the base (i.e., static) contents of the <span class='attr-name'>'d'</span> attribute:</p>
//		*
//		* <ul>
//		*   <li>DOM attribute <a>pathSegList</a> provides access to the static/base
//		*   contents of the <span class='attr-name'>'d'</span> attribute in a form which matches one-for-one
//		*   with SVG's syntax.</li>
//		*
//		*   <li>DOM attribute <a>normalizedPathSegList</a> provides normalized access
//		*   to the static/base contents of the <span class='attr-name'>'d'</span> attribute where all path
//		*   data commands are expressed in terms of the following subset of
//		*   <a>SVGPathSeg</a> types:
//		*   SVG_PATHSEG_MOVETO_ABS (M),
//		*   SVG_PATHSEG_LINETO_ABS (L),
//		*   SVG_PATHSEG_CURVETO_CUBIC_ABS (C) and
//		*   SVG_PATHSEG_CLOSEPATH (z).</li>
//		* </ul>
//		*
//		* <p>and two lists to access the current animated values of the <span class='attr-name'>'d'</span>
//		* attribute:</p>
//		*
//		* <ul>
//		*   <li>DOM attribute <a>animatedPathSegList</a> provides access to the current
//		*   animated contents of the <span class='attr-name'>'d'</span> attribute in a form which matches
//		*   one-for-one with SVG's syntax.</li>
//		*
//		*   <li>DOM attribute <a>animatedNormalizedPathSegList</a> provides
//		*   normalized access to the current animated contents of the <span class='attr-name'>'d'</span>
//		*   attribute where all path data commands are expressed in terms of the
//		*   following subset of <a>SVGPathSeg</a> types:
//		*   SVG_PATHSEG_MOVETO_ABS (M),
//		*   SVG_PATHSEG_LINETO_ABS (L),
//		*   SVG_PATHSEG_CURVETO_CUBIC_ABS (C) and
//		*   SVG_PATHSEG_CLOSEPATH (z).</li>
//		* </ul>
//		*
//		* <p>Each of the two lists are always kept synchronized. Modifications to one
//		* list will immediately cause the corresponding list to be modified.
//		* Modifications to <a>normalizedPathSegList</a> might cause entries in
//		* <a>pathSegList</a> to be broken into a set of normalized path segments.</p>
//		*
//		* <p>Additionally, the <a>'path/d'</a> attribute on the <a>'path'</a> element
//		* accessed via the XML DOM (e.g., using the <code>getAttribute()</code>
//		* method call) will reflect any changes made to <a>pathSegList</a> or
//		* <a>normalizedPathSegList</a>.</p>
//		*/
//		public native interface SVGAnimatedPathData { 

		/**
		* The <a>SVGPathElement</a> interface corresponds to the <a>'path'</a>
		* element.
		*/
		tagMaps.put("path".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGPathElement".toCharArray()});
	
		/**
		* The <a>SVGRectElement</a> interface corresponds to the <a>'rect'</a>
		* element.
		*/
		tagMaps.put("rect".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGRectElement".toCharArray()});

		/**
		* The <a>SVGCircleElement</a> interface corresponds to the <a>'circle'</a>
		* element.
		*/
		tagMaps.put("circle".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGCircleElement".toCharArray()});

		/**
		* The <a>SVGLineElement</a> interface corresponds to the <a>'line'</a>
		* element.
		*/
		tagMaps.put("line".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGLineElement".toCharArray()});
		
		/**
		* The <a>SVGEllipseElement</a> interface corresponds to the <a>'ellipse'</a>
		* element.
		*/
		tagMaps.put("ellipse".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGEllipseElement".toCharArray()});

//		/**
//		* <p>The <a>SVGAnimatedPoints</a> interface supports elements which have a
//		* <span class='attr-name'>'points'</span> attribute which holds a list of
//		* coordinate values and which support the ability to animate that
//		* attribute.</p>
//		*
//		* <p>Additionally, the <span class='attr-name'>'points'</span> attribute on
//		* the original element accessed via the XML DOM (e.g., using the
//		* <code>getAttribute()</code> method call) will reflect any changes made to
//		* <a>points</a>.</p>
//		*/
//		public native interface SVGAnimatedPoints { 

		/**
		* The <a>SVGPolylineElement</a> interface corresponds to the <a>'polyline'</a>
		* element.
		*/
		tagMaps.put("polyline".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGPolylineElement".toCharArray()});

		/**
		* The <a>SVGPolygonElement</a> interface corresponds to the <a>'polygon'</a>
		* element.
		*/
		tagMaps.put("polygon".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGPolygonElement".toCharArray()});

//		/**
//		* <p>The <a>SVGTextContentElement</a> is inherited by various text-related
//		* interfaces, such as <a>SVGTextElement</a>, <a>SVGTSpanElement</a>,
//		* <a>SVGTRefElement</a>, <a>SVGAltGlyphElement</a> and
//		* <a>SVGTextPathElement</a>.</p>
//		*
//		* <p>For the methods on this interface that refer to an index to a character
//		* or a number of characters, these references are to be interpreted as an
//		* index to a UTF-16 code unit or a number of UTF-16 code units, respectively.
//		* This is for consistency with DOM Level 2 Core, where methods on the
//		* <a>CharacterData</a> interface use UTF-16 code units as indexes and counts
//		* within the character data. Thus for example, if the text content of a
//		* <a>'text'</a> element is a single non-BMP character, such as U+10000, then
//		* invoking <a>SVGTextContentElement::getNumberOfChars</a> on that element
//		* will return 2 since there are two UTF-16 code units (the surrogate pair)
//		* used to represent that one character.</p>
//		*/
//		public native interface SVGTextContentElement extends 
//		             SVGElement,
//		             SVGTests,
//		             SVGLangSpace,
//		             SVGExternalResourcesRequired,
//		             SVGStylable {
//
//		/**
//		* The <a>SVGTextPositioningElement</a> interface is inherited by text-related
//		* interfaces: <a>SVGTextElement</a>, <a>SVGTSpanElement</a>,
//		* <a>SVGTRefElement</a> and <a>SVGAltGlyphElement</a>.
//		*/
//		public native interface SVGTextPositioningElement extends SVGTextContentElement { 

		/**
		* The <a>SVGTextElement</a> interface corresponds to the <a>'text'</a>
		* element.
		*/
		tagMaps.put("text".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGTextElement".toCharArray()});

		/**
		* The <a>SVGTSpanElement</a> interface corresponds to the <a>'tspan'</a>
		* element.
		*/
		tagMaps.put("tspan".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGTSpanElement".toCharArray()});

		/**
		* The <a>SVGTRefElement</a> interface corresponds to the <a>'tref'</a>
		* element.
		*/
		tagMaps.put("tref".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGTRefElement".toCharArray()});

		/**
		* The <a>SVGTextPathElement</a> interface corresponds to the <a>'textPath'</a>
		* element.
		*/
		tagMaps.put("textPath".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGTextPathElement".toCharArray()});

		/**
		* The <a>SVGAltGlyphElement</a> interface corresponds to the
		* <a>'altGlyph'</a> element.
		*/
		tagMaps.put("altGlyph".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGAltGlyphElement".toCharArray()});

		/**
		* The <a>SVGAltGlyphDefElement</a> interface corresponds to the
		* <a>'altGlyphDef'</a> element.
		*/
		tagMaps.put("altGlyphDef".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGAltGlyphDefElement".toCharArray()});

		/**
		* The <a>SVGAltGlyphItemElement</a> interface corresponds to the
		* <a>'altGlyphItem'</a> element.
		*/
		tagMaps.put("altGlyphItem".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGAltGlyphItemElement".toCharArray()});

		/**
		* The <a>SVGGlyphRefElement</a> interface corresponds to the
		* <a>'glyphRef element'</a> element.
		*/
		tagMaps.put("glyphRef".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGGlyphRefElement".toCharArray()});

//		/**
//		* <p>The <a>SVGPaint</a> interface corresponds to basic type
//		* <a href='painting.html#SpecifyingPaint'>&lt;paint&gt;</a> and represents
//		* the values of properties <a>'fill'</a> and <a>'stroke'</a>.</p>
//		*
//		* <p>Note: The <a>SVGPaint</a> interface is deprecated, and may be 
//		* dropped from future versions of the SVG specification.</p>
//		*/
//		public native interface SVGPaint extends SVGColor { 

		/**
		* The <a>SVGMarkerElement</a> interface corresponds to the
		* <a>'marker element'</a> element.
		*/
		tagMaps.put("marker".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGMarkerElement".toCharArray()});

		/**
		* The <a>SVGColorProfileElement</a> interface corresponds to the
		* <a>'color-profile element'</a> element.
		*/
		tagMaps.put("color-profile".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGColorProfileElement".toCharArray()});    

//		/**
//		* <p>The <a>SVGColorProfileRule</a> interface represents an &#64;color-profile
//		* rule in a CSS style sheet. An &#64;color-profile rule identifies a ICC
//		* profile which can be referenced within a given document.</p>
//		*
//		* <p>Support for the <a>SVGColorProfileRule</a> interface is only required
//		* in user agents that support <a href='styling.html#StylingWithCSS'>styling with CSS</a>.</p>
//		*/
//		public native interface SVGColorProfileRule extends 
//		             SVGCSSRule,
//		             SVGRenderingIntent { 
//
//		/**
//		* The <a>SVGGradientElement</a> interface is a base interface used by
//		* <a>SVGLinearGradientElement</a> and <a>SVGRadialGradientElement</a>.
//		*/
//		public native interface SVGGradientElement extends 
//		             SVGElement,
//		             SVGURIReference,
//		             SVGExternalResourcesRequired,
//		             SVGStylable,
//		             SVGUnitTypes { 

		/**
		* The <a>SVGLinearGradientElement</a> interface corresponds to the
		* <a>'linearGradient'</a> element.
		*/
		tagMaps.put("linearGradient".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGLinearGradientElement".toCharArray()});  

		/**
		* The <a>SVGRadialGradientElement</a> interface corresponds to the
		* <a>'radialGradient'</a> element.
		*/
		tagMaps.put("radialGradient".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGRadialGradientElement".toCharArray()});  

		/**
		* The <a>SVGStopElement</a> interface corresponds to the <a>'stop'</a>
		* element.
		*/
		tagMaps.put("stop".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGStopElement".toCharArray()}); 
		
		/**
		* The <a>SVGPatternElement</a> interface corresponds to the <a>'pattern'</a>
		* element.
		*/
		tagMaps.put("pattern".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGPatternElement".toCharArray()}); 

		/**
		* The <a>SVGClipPathElement</a> interface corresponds to the
		* <a>'clipPath'</a> element.
		*/
		tagMaps.put("clipPath".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGClipPathElement".toCharArray()}); 

		/**
		* The <a>SVGMaskElement</a> interface corresponds to the
		* <a>'mask element'</a> element.
		*/
		tagMaps.put("mask".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGMaskElement".toCharArray()}); 

		/**
		* The <a>SVGFilterElement</a> interface corresponds to the <a>'filter element'</a>
		* element.
		*/
		tagMaps.put("filter".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFilterElement".toCharArray()}); 

//		/**
//		* This interface defines the set of DOM attributes that are common across
//		* the filter primitive interfaces.
//		*/
//		public native interface SVGFilterPrimitiveStandardAttributes extends SVGStylable { 

		/**
		* The <a>SVGFEBlendElement</a> interface corresponds to the <a>'feBlend'</a>
		* element.
		*/
		tagMaps.put("feBlend".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEBlendElement".toCharArray()}); 

		/**
		* The <a>SVGFEColorMatrixElement</a> interface corresponds to the
		* <a>'feColorMatrix'</a> element.
		*/
		tagMaps.put("feColorMatrix".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEColorMatrixElement".toCharArray()}); 

		/**
		* The <a>SVGFEComponentTransferElement</a> interface corresponds to the
		* <a>'feComponentTransfer'</a> element.
		*/
		tagMaps.put("feComponentTransfer".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEComponentTransferElement".toCharArray()}); 

		/**
		* The <a>SVGFEFuncRElement</a> interface corresponds to the <a>'feFuncR'</a>
		* element.
		*/
		tagMaps.put("feFuncR".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEFuncRElement".toCharArray()}); 

		/**
		* The <a>SVGFEFuncRElement</a> interface corresponds to the <a>'feFuncG'</a>
		* element.
		*/
		tagMaps.put("feFuncG".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEFuncGElement".toCharArray()}); 
		
		/**
		* The <a>SVGFEFuncBElement</a> interface corresponds to the <a>'feFuncB'</a>
		* element.
		*/
		tagMaps.put("feFuncB".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEFuncBElement".toCharArray()}); 
		
		/**
		* The <a>SVGFEFuncAElement</a> interface corresponds to the <a>'feFuncA'</a>
		* element.
		*/
		tagMaps.put("feFuncA".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEFuncAElement".toCharArray()}); 
		
		/**
		* The <a>SVGFECompositeElement</a> interface corresponds to the
		* <a>'feComposite'</a> element.
		*/
		tagMaps.put("feComposite".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFECompositeElement".toCharArray()}); 

		/**
		* The <a>SVGFEConvolveMatrixElement</a> interface corresponds to the
		* <a>'feConvolveMatrix'</a> element.
		*/
		tagMaps.put("feConvolveMatrix".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEConvolveMatrixElement".toCharArray()}); 

		/**
		* The <a>SVGFEDiffuseLightingElement</a> interface corresponds to the
		* <a>'feDiffuseLighting'</a> element.
		*/
		tagMaps.put("feDiffuseLighting".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEDiffuseLightingElement".toCharArray()}); 

		/**
		* The <a>SVGFEPointLightElement</a> interface corresponds to the
		* <a>'fePointLight'</a> element.
		*/
		tagMaps.put("fePointLight".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEPointLightElement".toCharArray()}); 

		/**
		* The <a>SVGFESpotLightElement</a> interface corresponds to the
		* <a>'feSpotLight'</a> element.
		*/
		tagMaps.put("feSpotLight".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFESpotLightElement".toCharArray()}); 

		/**
		* The <a>SVGFEDisplacementMapElement</a> interface corresponds to the
		* <a>'feDisplacementMap'</a> element.
		*/
		tagMaps.put("feDisplacementMap".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEDisplacementMapElement".toCharArray()}); 

		/**
		* The <a>SVGFEFloodElement</a> interface corresponds to the
		* <a>'feFlood'</a> element.
		*/
		tagMaps.put("feFlood".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEFloodElement".toCharArray()}); 

		/**
		* The <a>SVGFEGaussianBlurElement</a> interface corresponds to the
		* <a>'feGaussianBlur'</a> element.
		*/
		tagMaps.put("feGaussianBlur".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEGaussianBlurElement".toCharArray()}); 

		/**
		* The <a>SVGFEImageElement</a> interface corresponds to the
		* <a>'feImage'</a> element.
		*/
		tagMaps.put("feImage".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEImageElement".toCharArray()}); 

		/**
		* The <a>SVGFEMergeElement</a> interface corresponds to the
		* <a>'feMerge'</a> element.
		*/
		tagMaps.put("feMerge".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEMergeElement".toCharArray()}); 

		/**
		* The <a>SVGFEMergeNodeElement</a> interface corresponds to the
		* <a>'feMergeNode'</a> element.
		*/
		tagMaps.put("feMergeNode".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEMergeNodeElement".toCharArray()}); 

		/**
		* The <a>SVGFEMorphologyElement</a> interface corresponds to the
		* <a>'feMorphology'</a> element.
		*/
		tagMaps.put("feMorphology".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEMorphologyElement".toCharArray()}); 

		/**
		* The <a>SVGFEOffsetElement</a> interface corresponds to the
		* <a>'feOffset'</a> element.
		*/
		tagMaps.put("feOffset".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFEOffsetElement".toCharArray()});

		/**
		* The <a>SVGFESpecularLightingElement</a> interface corresponds to the
		* <a>'feSpecularLighting'</a> element.
		*/
		tagMaps.put("feSpecularLighting".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFESpecularLightingElement".toCharArray()});

		/**
		* The <a>SVGFETileElement</a> interface corresponds to the
		* <a>'feTile'</a> element.
		*/
		tagMaps.put("feTile".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFETileElement".toCharArray()});

		/**
		* The <a>SVGFETurbulenceElement</a> interface corresponds to the
		* <a>'feTurbulence'</a> element.
		*/
		tagMaps.put("feTurbulence".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFETurbulenceElement".toCharArray()});

		/**
		* The <a>SVGCursorElement</a> interface corresponds to the
		* <a>'cursor element'</a> element.
		*/
		tagMaps.put("cursor".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGCursorElement".toCharArray()});

		/**
		* The <a>SVGAElement</a> interface corresponds to the <a>'a'</a> element.
		*/
		tagMaps.put("a".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGAElement".toCharArray()});

		/**
		* The <a>SVGViewElement</a> interface corresponds to the <a>'view'</a> element.
		*/
		tagMaps.put("view".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGViewElement".toCharArray()});

		/**
		* The <a>SVGScriptElement</a> interface corresponds to the <a>'script'</a>
		* element.
		*/
		tagMaps.put("script".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGScriptElement".toCharArray()});

		/**
		* <p>The <a>SVGAnimateElement</a> interface corresponds to the <a>'animate'</a>
		* element.</p>
		*
		* <p>Object-oriented access to the attributes of the <a>'animate'</a> element
		* via the SVG DOM is not available.</p>
		*/
		tagMaps.put("animate".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGAnimateElement".toCharArray()});

		/**
		* <p>The <a>SVGSetElement</a> interface corresponds to the <a>'set'</a>
		* element.</p>
		*
		* <p>Object-oriented access to the attributes of the <a>'set'</a> element
		* via the SVG DOM is not available.</p>
		*/
		tagMaps.put("set".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGSetElement".toCharArray()});

		/**
		* <p>The <a>SVGAnimateMotionElement</a> interface corresponds to the
		* <a>'animateMotion'</a> element.</p>
		*
		* <p>Object-oriented access to the attributes of the <a>'animateMotion'</a>
		* element via the SVG DOM is not available.</p>
		*/
		tagMaps.put("animateMotion".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGAnimateMotionElement".toCharArray()});

		/**
		* <p>The <a>SVGMPathElement</a> interface corresponds to the <a>'mpath'</a>
		* element.</p>
		*/
		tagMaps.put("mpath".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGMPathElement".toCharArray()});

		/**
		* <p>The <a>SVGAnimateColorElement</a> interface corresponds to the
		* <a>'animateColor'</a> element.</p>
		*
		* <p>Object-oriented access to the attributes of the <a>'animateColor'</a>
		* element via the SVG DOM is not available.</p>
		*/
		tagMaps.put("animateColor".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGAnimateColorElement".toCharArray()});
		
		/**
		* <p>The <a>SVGAnimateTransformElement</a> interface corresponds to the
		* <a>'animateTransform'</a> element.</p>
		*
		* <p>Object-oriented access to the attributes of the
		* <a>'animateTransform'</a> element via the SVG DOM is not available.</p>
		*/
		tagMaps.put("animateTransform".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGAnimateTransformElement".toCharArray()});

		/**
		* <p>The <a>SVGFontElement</a> interface corresponds to the
		* <a>'font element'</a> element.</p>
		*
		* <p>Object-oriented access to the attributes of the <a>'font element'</a> element
		* via the SVG DOM is not available.</p>
		*/
		tagMaps.put("font".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFontElement".toCharArray()});

		/**
		* <p>The <a>SVGGlyphElement</a> interface corresponds to the
		* <a>'glyph'</a> element.</p>
		*
		* <p>Object-oriented access to the attributes of the <a>'glyph'</a> element
		* via the SVG DOM is not available.</p>
		*/
		tagMaps.put("glyph".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGGlyphElement".toCharArray()});

		/**
		* <p>The <a>SVGMissingGlyphElement</a> interface corresponds to the
		* <a>'missing-glyph'</a> element.</p>
		*
		* <p>Object-oriented access to the attributes of the <a>'missing-glyph'</a> element
		* via the SVG DOM is not available.</p>
		*/
		tagMaps.put("missing-glyph".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGMissingGlyphElement".toCharArray()});

		/**
		* <p>The <a>SVGHKernElement</a> interface corresponds to the
		* <a>'hkern'</a> element.</p>
		*
		* <p>Object-oriented access to the attributes of the <a>'hkern'</a> element
		* via the SVG DOM is not available.</p>
		*/
		tagMaps.put("hkern".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGHKernElement".toCharArray()});
		
		/**
		* <p>The <a>SVGVKernElement</a> interface corresponds to the
		* <a>'vkern'</a> element.</p>
		*
		* <p>Object-oriented access to the attributes of the <a>'vkern'</a> element
		* via the SVG DOM is not available.</p>
		*/
		tagMaps.put("vkern".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGVKernElement".toCharArray()});
		
		/**
		* <p>The <a>SVGFontFaceElement</a> interface corresponds to the
		* <a>'font-face'</a> element.</p>
		*
		* <p>Object-oriented access to the attributes of the <a>'font-face'</a> element
		* via the SVG DOM is not available.</p>
		*/
		tagMaps.put("font-face".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFontFaceElement".toCharArray()});
		
		/**
		* <p>The <a>SVGFontFaceSrcElement</a> interface corresponds to the
		* <a>'font-face-src'</a> element.</p>
		*
		* <p>Object-oriented access to the attributes of the <a>'font-face-src'</a> element
		* via the SVG DOM is not available.</p>
		*/
		tagMaps.put("font-face-src".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFontFaceSrcElement".toCharArray()});
		
		/**
		* <p>The <a>SVGFontFaceUriElement</a> interface corresponds to the
		* <a>'font-face-uri'</a> element.</p>
		*
		* <p>Object-oriented access to the attributes of the <a>'font-face-uri'</a> element
		* via the SVG DOM is not available.</p>
		*/
		tagMaps.put("font-face-uri".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFontFaceUriElement".toCharArray()});
		
		/**
		* <p>The <a>SVGFontFaceFormatElement</a> interface corresponds to the
		* <a>'font-face-format'</a> element.</p>
		*
		* <p>Object-oriented access to the attributes of the <a>'font-face-format'</a> element
		* via the SVG DOM is not available.</p>
		*/
		tagMaps.put("font-face-format".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFontFaceFormatElement".toCharArray()});
		
		/**
		* <p>The <a>SVGFontFaceNameElement</a> interface corresponds to the
		* <a>'font-face-name'</a> element.</p>
		*
		* <p>Object-oriented access to the attributes of the <a>'font-face-name'</a> element
		* via the SVG DOM is not available.</p>
		*/
		tagMaps.put("font-face-name".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGFontFaceNameElement".toCharArray()});
		
		/**
		* The <a>SVGMetadataElement</a> interface corresponds to the <a>'metadata'</a>
		* element.
		*/
		tagMaps.put("metadata".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGMetadataElement".toCharArray()});

		/**
		* The <a>SVGForeignObjectElement</a> interface corresponds to the
		* <a>'foreignObject'</a> element.
		*/
		tagMaps.put("foreignObject".toCharArray(), new char[][]{ORG, W3C, SVGPKG, "SVGForeignObjectElement".toCharArray()});

	}
	
	public char[][] getMappingClass(char[] name){
		char[][] result = tagMaps.get(name);
		return result;
	}
}