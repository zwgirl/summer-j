package org.summer.sdt.internal.compiler.html;

import org.summer.sdt.internal.compiler.util.HashtableOfCharArray;
/**
 * 2015-05-26
 * @author cym
 *
 */
public class CssProperties {
	private static final HashtableOfCharArray toHyphen = new HashtableOfCharArray();
	private static final HashtableOfCharArray toCamel = new HashtableOfCharArray();
	
	static{
		toHyphen.put("azimuth".toCharArray(), "azimuth".toCharArray());
		toHyphen.put("background".toCharArray(), "background".toCharArray());
		toHyphen.put("backgroundAttachment".toCharArray(), "background-attachment".toCharArray());	//background-attachment
		toHyphen.put("backgroundColor".toCharArray(), "background-color".toCharArray());		//background-color
		toHyphen.put("backgroundImage".toCharArray(), "background-image".toCharArray());		//background-image
		toHyphen.put("backgroundPosition".toCharArray(), "background-position".toCharArray());		//background-position
		toHyphen.put("backgroundRepeat".toCharArray(), "background-repeat".toCharArray());	//background-repeat
		toHyphen.put("border".toCharArray(), "border".toCharArray());
		toHyphen.put("borderCollapse".toCharArray(), "border-collapse".toCharArray());		//border-collapse
		toHyphen.put("borderColor".toCharArray(), "border-color".toCharArray());	//border-color
		toHyphen.put("borderSpacing".toCharArray(), "border-spacing".toCharArray());	//border-spacing
		toHyphen.put("borderStyle".toCharArray(), "border-style".toCharArray());	//border-style
		toHyphen.put("borderTop".toCharArray(), "border-top".toCharArray());	//border-top
		toHyphen.put("borderRight".toCharArray(), "border-right".toCharArray());	//border-right
		toHyphen.put("borderBottom".toCharArray(), "border-bottom".toCharArray());	//border-bottom
		toHyphen.put("borderLeft".toCharArray(), "border-left".toCharArray());	//border-left
		toHyphen.put("borderTopColor".toCharArray(), "border-top-color".toCharArray());	//border-top-color
		toHyphen.put("borderRightColor".toCharArray(), "border-right-color".toCharArray());	//border-right-color
		toHyphen.put("borderBottomColor".toCharArray(), "border-bottom-color".toCharArray());	//border-bottom-color
		toHyphen.put("borderLeftColor".toCharArray(), "border-left-color".toCharArray());		//border-left-color
		toHyphen.put("borderTopStyle".toCharArray(), "border-top-style".toCharArray());		//border-top-style
		toHyphen.put("borderRightStyle".toCharArray(), "border-right-style".toCharArray());		//border-right-style
		toHyphen.put("borderBottomStyle".toCharArray(), "border-bottom-style".toCharArray());	//border-bottom-style
		toHyphen.put("borderLeftStyle".toCharArray(), "border-left-style".toCharArray());		//border-left-style
		toHyphen.put("borderTopWidth".toCharArray(), "border-top-width".toCharArray());		//border-top-width
		toHyphen.put("borderRightWidth".toCharArray(), "border-right-width".toCharArray());		//border-right-width
		toHyphen.put("borderBottomWidth".toCharArray(), "border-bottom-width".toCharArray());		//border-bottom-width
		toHyphen.put("borderLeftWidth".toCharArray(), "border-left-width".toCharArray());	//border-left-width
		toHyphen.put("borderWidth".toCharArray(), "border-width".toCharArray());		//border-width
		toHyphen.put("bottom".toCharArray(), "bottom".toCharArray());
		toHyphen.put("captionSide".toCharArray(), "caption-side".toCharArray());		//caption-side
		toHyphen.put("clear".toCharArray(), "clear".toCharArray());
		toHyphen.put("clip".toCharArray(), "clip".toCharArray());
		toHyphen.put("color".toCharArray(), "color".toCharArray());
		toHyphen.put("content".toCharArray(), "content".toCharArray());
		toHyphen.put("counterIncrement".toCharArray(), "counter-increment".toCharArray());	//counter-increment
		toHyphen.put("counterReset".toCharArray(), "counter-reset".toCharArray());	//counter-reset
		toHyphen.put("cue".toCharArray(), "cue".toCharArray());
		toHyphen.put("cueAfter".toCharArray(), "cue-after".toCharArray());		//cue-after
		toHyphen.put("cueBefore".toCharArray(), "cue-before".toCharArray());		//cue-before
		toHyphen.put("cursor".toCharArray(), "cursor".toCharArray());
		toHyphen.put("direction".toCharArray(), "direction".toCharArray());
		toHyphen.put("display".toCharArray(), "display".toCharArray());
		toHyphen.put("elevation".toCharArray(), "elevation".toCharArray());
		toHyphen.put("emptyCells".toCharArray(), "empty-cells".toCharArray());		//empty-cells
		toHyphen.put("cssFloat".toCharArray(), "css-float".toCharArray());		//css-float
		toHyphen.put("font".toCharArray(), "font".toCharArray());
		toHyphen.put("fontFamily".toCharArray(), "font-family".toCharArray());		//font-family
		toHyphen.put("fontSize".toCharArray(), "font-size".toCharArray());		//font-size
		toHyphen.put("fontSizeAdjust".toCharArray(), "font-size-adjust".toCharArray());		//font-size-adjust
		toHyphen.put("fontStretch".toCharArray(), "font-stretch".toCharArray());		//font-stretch
		toHyphen.put("fontStyle".toCharArray(), "font-style".toCharArray());		//font-style
		toHyphen.put("fontVariant".toCharArray(), "font-variant".toCharArray());		//font-variant
		toHyphen.put("fontWeight".toCharArray(), "font-weight".toCharArray());		//font-weight
		toHyphen.put("height".toCharArray(), "height".toCharArray());
		toHyphen.put("left".toCharArray(), "left".toCharArray());
		toHyphen.put("letterSpacing".toCharArray(), "letter-spacing".toCharArray());	//letter-spacing
		toHyphen.put("lineHeight".toCharArray(), "line-height".toCharArray());		//line-height
		toHyphen.put("listStyle".toCharArray(), "list-style".toCharArray());		//list-style
		toHyphen.put("listStyleImage".toCharArray(), "list-style-image".toCharArray());		//list-style-image
		toHyphen.put("listStylePosition".toCharArray(), "list-style-position".toCharArray());	//list-style-position
		toHyphen.put("listStyleType".toCharArray(), "list-style-type".toCharArray());	//list-style-type
		toHyphen.put("margin".toCharArray(), "margin".toCharArray());
		toHyphen.put("marginTop".toCharArray(), "margin-top".toCharArray());	//margin-top
		toHyphen.put("marginRight".toCharArray(), "margin-right".toCharArray());	//margin-right
		toHyphen.put("marginBottom".toCharArray(), "margin-bottom".toCharArray());	//margin-bottom
		toHyphen.put("marginLeft".toCharArray(), "margin-left".toCharArray());		//margin-left
		toHyphen.put("markerOffset".toCharArray(), "marker-offset".toCharArray());	//marker-offset
		toHyphen.put("marks".toCharArray(), "marks".toCharArray());
		toHyphen.put("maxHeight".toCharArray(), "max-height".toCharArray());	//max-height
		toHyphen.put("maxWidth".toCharArray(), "max-width".toCharArray());	//max-width
		toHyphen.put("minHeight".toCharArray(), "min-height".toCharArray());	//min-height
		toHyphen.put("minWidth".toCharArray(), "min-width".toCharArray());	//min-width
		toHyphen.put("orphans".toCharArray(), "orphans".toCharArray());
		toHyphen.put("outline".toCharArray(), "outline".toCharArray());
		toHyphen.put("outlineColor".toCharArray(), "outline-color".toCharArray());	//outline-color
		toHyphen.put("outlineStyle".toCharArray(), "outline-style".toCharArray());		//outline-style
		toHyphen.put("outlineWidth".toCharArray(), "outline-width".toCharArray());	//outline-width
		toHyphen.put("overflow".toCharArray(), "overflow".toCharArray());
		toHyphen.put("padding".toCharArray(), "padding".toCharArray());
		toHyphen.put("paddingTop".toCharArray(), "padding-top".toCharArray());	//padding-top
		toHyphen.put("paddingRight".toCharArray(), "padding-right".toCharArray());	//padding-right
		toHyphen.put("paddingBottom".toCharArray(), "padding-bottom".toCharArray());	//padding-bottom
		toHyphen.put("paddingLeft".toCharArray(), "padding-left".toCharArray());	//padding-left
		toHyphen.put("page".toCharArray(), "page".toCharArray());
		toHyphen.put("pageBreakAfter".toCharArray(), "page-break-after".toCharArray());		//page-break-after
		toHyphen.put("pageBreakBefore".toCharArray(), "page-break-before".toCharArray());	//page-break-before
		toHyphen.put("pageBreakInside".toCharArray(), "page-break-inside".toCharArray());	//page-break-inside
		toHyphen.put("pause".toCharArray(), "pause".toCharArray());
		toHyphen.put("pauseAfter".toCharArray(), "pause-after".toCharArray());		//pause-after
		toHyphen.put("pauseBefore".toCharArray(), "pause-before".toCharArray());		//pause-before
		toHyphen.put("pitch".toCharArray(), "pitch".toCharArray());
		toHyphen.put("pitchRange".toCharArray(), "pitch-range".toCharArray());		//pitch-range
		toHyphen.put("playDuring".toCharArray(), "play-during".toCharArray());		//play-during
		toHyphen.put("position".toCharArray(), "position".toCharArray());
		toHyphen.put("quotes".toCharArray(), "quotes".toCharArray());
		toHyphen.put("richness".toCharArray(), "richness".toCharArray());
		toHyphen.put("right".toCharArray(), "right".toCharArray());
		toHyphen.put("size".toCharArray(), "size".toCharArray());
		toHyphen.put("speak".toCharArray(), "speak".toCharArray());
		toHyphen.put("speakHeader".toCharArray(), "speak-header".toCharArray());	//speak-header
		toHyphen.put("speakNumeral".toCharArray(), "speak-numeral".toCharArray());	//speak-numeral
		toHyphen.put("speakPunctuation".toCharArray(), "speak-punctuation".toCharArray());	//speak-punctuation
		toHyphen.put("speechRate".toCharArray(), "speech-rate".toCharArray());		//speech-rate
		toHyphen.put("stress".toCharArray(), "stress".toCharArray());
		toHyphen.put("tableLayout".toCharArray(), "table-layout".toCharArray());	//table-layout
		toHyphen.put("textAlign".toCharArray(), "text-align".toCharArray());	//text-align
		toHyphen.put("textDecoration".toCharArray(), "text-decoration".toCharArray());	//text-decoration
		toHyphen.put("textIndent".toCharArray(), "text-indent".toCharArray());		//text-indent
		toHyphen.put("textShadow".toCharArray(), "text-show".toCharArray());		//text-show
		toHyphen.put("textTransform".toCharArray(), "text-transform".toCharArray());			//text-transform
		toHyphen.put("top".toCharArray(), "top".toCharArray());
		toHyphen.put("unicodeBidi".toCharArray(), "unicode-bidi".toCharArray());		//unicode-bidi
		toHyphen.put("verticalAlign".toCharArray(), "vertical-align".toCharArray());		//vertical-align
		toHyphen.put("visibility".toCharArray(), "visibility".toCharArray());
		toHyphen.put("voiceFamily".toCharArray(), "voice-family".toCharArray());   //voice-family
		toHyphen.put("volume".toCharArray(), "volume".toCharArray());
		toHyphen.put("whiteSpace".toCharArray(), "white-space".toCharArray());		//white-space
		toHyphen.put("widows".toCharArray(), "widows".toCharArray());
		toHyphen.put("width".toCharArray(), "width".toCharArray());
		toHyphen.put("wordSpacing".toCharArray(), "word-spacing".toCharArray());	//word-spacing
		toHyphen.put("zIndex".toCharArray(), "z-index".toCharArray());			//z-index
		
		//SVG
//		Font properties: 
//		toHyphen.put(" font{ }
//		public native String  fontFamily{ }  //font-family
//		public native String  fontSize{ }  	//font-size
//		public native String  fontSizeAdjust{ } //font-size-adjust
//		public native String  fontStretch{ }	//font-stretch
//		public native String  fontStyle{ }		//font-style
//		public native String  fontVariant{ }	//font-variant
//		public native String  fontWeight{ }	//font-weight
	//
////		• Text properties: 
//		public native String  direction{ }
//		public native String  letterSpacing{ }		//letter-spacing
//		public native String  textDecoration{ }		//text-decoration
//		public native String  unicodeBidi{ }		//unicode-bidi
//		public native String  wordSpacing{ }		//word-spacing
	//
////		• Other properties for visual media: 
//		public native String  clip{ }	//clip //only applicable to outermost svg element.
//		public native String  color{ } //used to provide a potential indirect value (currentColor) for the ‘fill’, ‘stroke’, ‘stop-color’, ‘flood-color’ and ‘l{ }ghting-color’ properties. (The SVG properties which support color allow a color specification which is extended from CSS2 to accommodate color definitions in arbitrary color spaces. See Color profile descriptions.)
//		public native String  cursor{ }
//		public native String  display{ }
//		public native String  overflow{ } //only applicable to elements which establish a new viewport.
//		public native String  visibility{ }


//		The following SVG properties are not defined in CSS2. The complete normative definitions for these properties are found in this specification:
//		Clipping, //Masking and Compositing properties: public native String  clip-path’
		toHyphen.put("clipRule".toCharArray(), "clip-rule".toCharArray());		//clip-rule
		toHyphen.put("mask".toCharArray(), "mask".toCharArray());
		toHyphen.put("opacity".toCharArray(), "opacity".toCharArray());

//		• Filter Effects properties: 
		toHyphen.put("enableBackground".toCharArray(), "enable-background".toCharArray());	//enable-background
		toHyphen.put("filter".toCharArray(), "filter".toCharArray());
		toHyphen.put("floodColor".toCharArray(), "flood-color".toCharArray());			//flood-color
		toHyphen.put("floodOpacity".toCharArray(), "flood-opacity".toCharArray());		//flood-opacity
		toHyphen.put("lightingColor".toCharArray(), "lighting-color".toCharArray());		//lighting-color

//		• Gradient properties: 
		toHyphen.put("stopColor".toCharArray(), "stop-color".toCharArray());			//stop-color
		toHyphen.put("stopOpacity".toCharArray(), "stop-opacity".toCharArray());		//stop-opacity

//		• Interactivity properties: 
		toHyphen.put("pointerEvents".toCharArray(), "pointer-events".toCharArray());		//pointer-events

//		• Color and Painting properties: 
		toHyphen.put("colorInterpolation".toCharArray(), "color-interpolation".toCharArray());	//color-interpolation
		toHyphen.put("colorInterpolationFilters".toCharArray(), "color-interpolation-filters".toCharArray());		//color-interpolation-filters
		toHyphen.put("colorProfile".toCharArray(), "color-profile".toCharArray());		//color-profile
		toHyphen.put("colorRendering".toCharArray(), "color-rendering".toCharArray());		//color-rendering
		toHyphen.put("fill".toCharArray(), "fill".toCharArray());				//fill
		toHyphen.put("fillOpacity".toCharArray(), "fill-opacity".toCharArray());		//fill-opacity
		toHyphen.put("fillRule".toCharArray(), "fill-rule".toCharArray());			//fill-rule
		toHyphen.put("imageRendering".toCharArray(), "image-rendering".toCharArray());	//image-rendering
		toHyphen.put("marker".toCharArray(), "marker".toCharArray());				//marker
		toHyphen.put("markerEnd".toCharArray(), "marker-end".toCharArray());			//marker-end
		toHyphen.put("markerMid".toCharArray(), "marker-mid".toCharArray());			//marker-mid
		toHyphen.put("markerStart".toCharArray(), "marker-start".toCharArray());		//marker-start
		toHyphen.put("shapeRendering".toCharArray(), "shape-rendering".toCharArray());	//shape-rendering
		toHyphen.put("stroke".toCharArray(), "stroke".toCharArray());				//stroke
		toHyphen.put("strokeDasharray".toCharArray(), "stroke-dasharray".toCharArray());	//stroke-dasharray
		toHyphen.put("strokeDashoffset".toCharArray(), "stroke-dashoffset".toCharArray());	//stroke-dashoffset
		toHyphen.put("strokeLinecap".toCharArray(), "stroke-linecap".toCharArray());		//stroke-linecap
		toHyphen.put("strokeLinejoin".toCharArray(), "stroke-linejoin".toCharArray());	//stroke-linejoin
		toHyphen.put("strokeMiterlimit".toCharArray(), "stroke-miterlimit".toCharArray());	//stroke-miterlimit
		toHyphen.put("strokeOpacity".toCharArray(), "stroke-opacity".toCharArray());		//stroke-opacity
		toHyphen.put("strokeWidth".toCharArray(), "stroke-width".toCharArray());		//stroke-width
		toHyphen.put("textRendering".toCharArray(), "text-rendering".toCharArray());		//text-rendering

//		• Text properties: 
		toHyphen.put("alignmentBaseline".toCharArray(), "alignment-baseline".toCharArray());	//alignment-baseline
		toHyphen.put("baselineShift".toCharArray(), "baseline-shift".toCharArray());		//baseline-shift
		toHyphen.put("dominantBaseline".toCharArray(), "dominant-baseline".toCharArray());	//dominant-baseline
		toHyphen.put("glyphOrientationHorizontal".toCharArray(), "glyph-orientation-horizontal".toCharArray()); //glyph-orientation-horizontal
		toHyphen.put("glyphOrientationVertical".toCharArray(), "glyph-orientation-vertical".toCharArray());	//glyph-orientation-vertical
		toHyphen.put("kerning".toCharArray(), "kerning".toCharArray());
		toHyphen.put("textAnchor".toCharArray(), "text-anchor".toCharArray());	//text-anchor
		toHyphen.put("writingMode".toCharArray(), "writing-mode".toCharArray());	//writing-mode
		
//		SVG font
		toHyphen.put("horizOriginX".toCharArray(), "horiz-origin-x".toCharArray()); // %Number.datatype; #IMPLIED
		toHyphen.put("horizOriginY".toCharArray(), "horiz-origin-y".toCharArray()); // %Number.datatype; #IMPLIED
		toHyphen.put("horizAdvX".toCharArray(), "horiz-adv-x".toCharArray()); // %Number.datatype; #REQUIRED
		toHyphen.put("vertOriginX".toCharArray(), "vert-origin-x".toCharArray()); // %Number.datatype; #IMPLIED
		toHyphen.put("vertOriginY".toCharArray(), "vert-origin-y".toCharArray()); // %Number.datatype; #IMPLIED
		toHyphen.put("vertAdvY".toCharArray(), "vert-adv-y".toCharArray()); // %Number.datatype; #IMPLIED
		
//	    toCamel.put("font-family".toCharArray(), "fontFamily".toCharArray());
//	    toCamel.put("font-style".toCharArray(), "aaa".toCharArray());
//	    toCamel.put("font-variant".toCharArray(), "aaa".toCharArray());
//	    toCamel.put("font-weight".toCharArray(), "aaa".toCharArray());
//	    toCamel.put("font-stretch".toCharArray(), "aaa".toCharArray());
//	    toCamel.put("font-size".toCharArray(), "aaa".toCharArray());
	    toHyphen.put("unicodeRange".toCharArray(), "unicode-range".toCharArray());
	    toHyphen.put("unitsRerEm".toCharArray(), "units-per-em".toCharArray());
	    toHyphen.put("panose1".toCharArray(), "aaa".toCharArray());
	    toHyphen.put("stemv".toCharArray(), "panose-1".toCharArray());
	    toHyphen.put("stemh".toCharArray(), "stemh".toCharArray());
	    toHyphen.put("slope".toCharArray(), "slope".toCharArray());
	    toHyphen.put("capHeight".toCharArray(), "cap-height".toCharArray());
	    toHyphen.put("xHeight".toCharArray(), "x-height".toCharArray());
	    toHyphen.put("accentHeight".toCharArray(), "accent-height".toCharArray());
	    toHyphen.put("ascent".toCharArray(), "ascent".toCharArray());
	    toHyphen.put("descent".toCharArray(), "descent".toCharArray());
	    toHyphen.put("widths".toCharArray(), "widths".toCharArray());
	    toHyphen.put("bbox".toCharArray(), "bbox".toCharArray());
	    toHyphen.put("ideographic".toCharArray(), "ideographic".toCharArray());
	    toHyphen.put("alphabetic".toCharArray(), "alphabetic".toCharArray());
	    toHyphen.put("mathematical".toCharArray(), "mathematical".toCharArray());
	    toHyphen.put("hanging".toCharArray(), "hanging".toCharArray());
	    toHyphen.put("vIdeographic".toCharArray(), "v-ideographic".toCharArray());
	    toHyphen.put("vAlphabetic".toCharArray(), "v-alphabetic".toCharArray());
	    toHyphen.put("vMathematical".toCharArray(), "v-mathematical".toCharArray());
	    toHyphen.put("vHanging".toCharArray(), "v-hanging".toCharArray());
	    toHyphen.put("underlinePosition".toCharArray(), "underline-position".toCharArray());
	    toHyphen.put("underlineThickness".toCharArray(), "underline-thickness".toCharArray());
	    toHyphen.put("strikethroughPosition".toCharArray(), "strikethrough-position".toCharArray());
	    toHyphen.put("strikethroughThickness".toCharArray(), "strikethrough-thickness".toCharArray());
	    toHyphen.put("overlinePosition".toCharArray(), "overline-position".toCharArray());
	    toHyphen.put("overlineThickness".toCharArray(), "overline-thickness".toCharArray());
	}
	
	
	static{
		toCamel.put("azimuth".toCharArray(), "azimuth".toCharArray());
		toCamel.put("background".toCharArray(), "background".toCharArray());
		toCamel.put("background-attachment".toCharArray(), "backgroundAttachment".toCharArray());	//background-attachment
		toCamel.put("background-color".toCharArray(), "backgroundColor".toCharArray());		//background-color
		toCamel.put("background-image".toCharArray(), "backgroundImage".toCharArray());		//background-image
		toCamel.put("background-position".toCharArray(), "backgroundPosition".toCharArray());		//background-position
		toCamel.put("background-repeat".toCharArray(), "backgroundRepeat".toCharArray());	//background-repeat
		toCamel.put("border".toCharArray(), "border".toCharArray());
		toCamel.put("border-collapse".toCharArray(), "borderCollapse".toCharArray());		//border-collapse
		toCamel.put("border-color".toCharArray(), "borderColor".toCharArray());	//border-color
		toCamel.put("border-spacing".toCharArray(), "borderSpacing".toCharArray());	//border-spacing
		toCamel.put("border-style".toCharArray(), "borderStyle".toCharArray());	//border-style
		toCamel.put("border-top".toCharArray(), "borderTop".toCharArray());	//border-top
		toCamel.put("border-right".toCharArray(), "borderRight".toCharArray());	//border-right
		toCamel.put("border-bottom".toCharArray(), "borderBottom".toCharArray());	//border-bottom
		toCamel.put("border-left".toCharArray(), "borderLeft".toCharArray());	//border-left
		toCamel.put("border-top-color".toCharArray(), "borderTopColor".toCharArray());	//border-top-color
		toCamel.put("border-right-color".toCharArray(), "borderRightColor".toCharArray());	//border-right-color
		toCamel.put("border-bottom-color".toCharArray(), "borderBottomColor".toCharArray());	//border-bottom-color
		toCamel.put("border-left-color".toCharArray(), "borderLeftColor".toCharArray());		//border-left-color
		toCamel.put("border-top-style".toCharArray(), "borderTopStyle".toCharArray());		//border-top-style
		toCamel.put("border-right-style".toCharArray(), "borderRightStyle".toCharArray());		//border-right-style
		toCamel.put("border-bottom-style".toCharArray(), "borderBottomStyle".toCharArray());	//border-bottom-style
		toCamel.put("border-left-style".toCharArray(), "borderLeftStyle".toCharArray());		//border-left-style
		toCamel.put("border-top-width".toCharArray(), "borderTopWidth".toCharArray());		//border-top-width
		toCamel.put("border-right-width".toCharArray(), "borderRightWidth".toCharArray());		//border-right-width
		toCamel.put("border-bottom-width".toCharArray(), "borderBottomWidth".toCharArray());		//border-bottom-width
		toCamel.put("border-left-width".toCharArray(), "borderLeftWidth".toCharArray());	//border-left-width
		toCamel.put("border-width".toCharArray(), "borderWidth".toCharArray());		//border-width
		toCamel.put("bottom".toCharArray(), "bottom".toCharArray());
		toCamel.put("caption-side".toCharArray(), "captionSide".toCharArray());		//caption-side
		toCamel.put("clear".toCharArray(), "clear".toCharArray());
		toCamel.put("clip".toCharArray(), "clip".toCharArray());
		toCamel.put("color".toCharArray(), "color".toCharArray());
		toCamel.put("content".toCharArray(), "content".toCharArray());
		toCamel.put("counter-increment".toCharArray(), "counterIncrement".toCharArray());	//counter-increment
		toCamel.put("counter-reset".toCharArray(), "counterReset".toCharArray());	//counter-reset
		toCamel.put("cue".toCharArray(), "cue".toCharArray());
		toCamel.put("cue-after".toCharArray(), "cueAfter".toCharArray());		//cue-after
		toCamel.put("cue-before".toCharArray(), "cueBefore".toCharArray());		//cue-before
		toCamel.put("cursor".toCharArray(), "cursor".toCharArray());
		toCamel.put("direction".toCharArray(), "direction".toCharArray());
		toCamel.put("display".toCharArray(), "display".toCharArray());
		toCamel.put("elevation".toCharArray(), "elevation".toCharArray());
		toCamel.put("empty-cells".toCharArray(), "emptyCells".toCharArray());		//empty-cells
		toCamel.put("css-float".toCharArray(), "cssFloat".toCharArray());		//css-float
		toCamel.put("font".toCharArray(), "font".toCharArray());
		toCamel.put("font-family".toCharArray(), "fontFamily".toCharArray());		//font-family
		toCamel.put("font-size".toCharArray(), "fontSize".toCharArray());		//font-size
		toCamel.put("font-size-adjust".toCharArray(), "fontSizeAdjust".toCharArray());		//font-size-adjust
		toCamel.put("font-stretch".toCharArray(), "fontStretch".toCharArray());		//font-stretch
		toCamel.put("font-style".toCharArray(), "fontStyle".toCharArray());		//font-style
		toCamel.put("font-variant".toCharArray(), "fontVariant".toCharArray());		//font-variant
		toCamel.put("font-weight".toCharArray(), "fontWeight".toCharArray());		//font-weight
		toCamel.put("height".toCharArray(), "height".toCharArray());
		toCamel.put("left".toCharArray(), "left".toCharArray());
		toCamel.put("letter-spacing".toCharArray(), "letterSpacing".toCharArray());	//letter-spacing
		toCamel.put("line-height".toCharArray(), "lineHeight".toCharArray());		//line-height
		toCamel.put("list-style".toCharArray(), "listStyle".toCharArray());		//list-style
		toCamel.put("list-style-image".toCharArray(), "listStyleImage".toCharArray());		//list-style-image
		toCamel.put("list-style-position".toCharArray(), "listStylePosition".toCharArray());	//list-style-position
		toCamel.put("list-style-type".toCharArray(), "listStyleType".toCharArray());	//list-style-type
		toCamel.put("margin".toCharArray(), "margin".toCharArray());
		toCamel.put("margin-top".toCharArray(), "marginTop".toCharArray());	//margin-top
		toCamel.put("margin-right".toCharArray(), "marginRight".toCharArray());	//margin-right
		toCamel.put("margin-bottom".toCharArray(), "marginBottom".toCharArray());	//margin-bottom
		toCamel.put("margin-left".toCharArray(), "marginLeft".toCharArray());		//margin-left
		toCamel.put("marker-offset".toCharArray(), "markerOffset".toCharArray());	//marker-offset
		toCamel.put("marks".toCharArray(), "marks".toCharArray());
		toCamel.put("max-height".toCharArray(), "maxHeight".toCharArray());	//max-height
		toCamel.put("max-width".toCharArray(), "maxWidth".toCharArray());	//max-width
		toCamel.put("min-height".toCharArray(), "minHeight".toCharArray());	//min-height
		toCamel.put("min-width".toCharArray(), "minWidth".toCharArray());	//min-width
		toCamel.put("orphans".toCharArray(), "orphans".toCharArray());
		toCamel.put("outline".toCharArray(), "outline".toCharArray());
		toCamel.put("outline-color".toCharArray(), "outlineColor".toCharArray());	//outline-color
		toCamel.put("outline-style".toCharArray(), "outlineStyle".toCharArray());		//outline-style
		toCamel.put("outline-width".toCharArray(), "outlineWidth".toCharArray());	//outline-width
		toCamel.put("overflow".toCharArray(), "overflow".toCharArray());
		toCamel.put("padding".toCharArray(), "padding".toCharArray());
		toCamel.put("padding-top".toCharArray(), "paddingTop".toCharArray());	//padding-top
		toCamel.put("padding-right".toCharArray(), "paddingRight".toCharArray());	//padding-right
		toCamel.put("padding-bottom".toCharArray(), "paddingBottom".toCharArray());	//padding-bottom
		toCamel.put("padding-left".toCharArray(), "paddingLeft".toCharArray());	//padding-left
		toCamel.put("page".toCharArray(), "page".toCharArray());
		toCamel.put("page-break-after".toCharArray(), "pageBreakAfter".toCharArray());		//page-break-after
		toCamel.put("page-break-before".toCharArray(), "pageBreakBefore".toCharArray());	//page-break-before
		toCamel.put("page-break-inside".toCharArray(), "pageBreakInside".toCharArray());	//page-break-inside
		toCamel.put("pause".toCharArray(), "pause".toCharArray());
		toCamel.put("pause-after".toCharArray(), "pauseAfter".toCharArray());		//pause-after
		toCamel.put("pause-before".toCharArray(), "pauseBefore".toCharArray());		//pause-before
		toCamel.put("pitch".toCharArray(), "pitch".toCharArray());
		toCamel.put("pitch-range".toCharArray(), "pitchRange".toCharArray());		//pitch-range
		toCamel.put("play-during".toCharArray(), "playDuring".toCharArray());		//play-during
		toCamel.put("position".toCharArray(), "position".toCharArray());
		toCamel.put("quotes".toCharArray(), "quotes".toCharArray());
		toCamel.put("richness".toCharArray(), "richness".toCharArray());
		toCamel.put("right".toCharArray(), "right".toCharArray());
		toCamel.put("size".toCharArray(), "size".toCharArray());
		toCamel.put("speak".toCharArray(), "speak".toCharArray());
		toCamel.put("speak-header".toCharArray(), "speakHeader".toCharArray());	//speak-header
		toCamel.put("speak-numeral".toCharArray(), "speakNumeral".toCharArray());	//speak-numeral
		toCamel.put("speak-punctuation".toCharArray(), "speakPunctuation".toCharArray());	//speak-punctuation
		toCamel.put("speech-rate".toCharArray(), "speechRate".toCharArray());		//speech-rate
		toCamel.put("stress".toCharArray(), "stress".toCharArray());
		toCamel.put("table-layout".toCharArray(), "tableLayout".toCharArray());	//table-layout
		toCamel.put("text-align".toCharArray(), "textAlign".toCharArray());	//text-align
		toCamel.put("text-decoration".toCharArray(), "textDecoration".toCharArray());	//text-decoration
		toCamel.put("text-indent".toCharArray(), "textIndent".toCharArray());		//text-indent
		toCamel.put("text-show".toCharArray(), "textShadow".toCharArray());		//text-show
		toCamel.put("text-transform".toCharArray(), "textTransform".toCharArray());			//
		toCamel.put("top".toCharArray(), "top".toCharArray());
		toCamel.put("unicode-bidi".toCharArray(), "unicodeBidi".toCharArray());		//unicode-bidi
		toCamel.put("vertical-align".toCharArray(), "verticalAlign".toCharArray());		//vertical-align
		toCamel.put("visibility".toCharArray(), "visibility".toCharArray());
		toCamel.put("voice-family".toCharArray(), "voiceFamily".toCharArray());   //voice-family
		toCamel.put("volume".toCharArray(), "volume".toCharArray());
		toCamel.put("white-space".toCharArray(), "whiteSpace".toCharArray());		//white-space
		toCamel.put("widows".toCharArray(), "widows".toCharArray());
		toCamel.put("width".toCharArray(), "width".toCharArray());
		toCamel.put("word-spacing".toCharArray(), "wordSpacing".toCharArray());	//word-spacing
		toCamel.put("z-index".toCharArray(), "zIndex".toCharArray());			//z-index
		
		//SVG
//		Font properties: 
//		toCamel.put(" font{ }
//		public native String  fontFamily{ }  //font-family
//		public native String  fontSize{ }  	//font-size
//		public native String  fontSizeAdjust{ } //font-size-adjust
//		public native String  fontStretch{ }	//font-stretch
//		public native String  fontStyle{ }		//font-style
//		public native String  fontVariant{ }	//font-variant
//		public native String  fontWeight{ }	//font-weight
	//
////		• Text properties: 
//		public native String  direction{ }
//		public native String  letterSpacing{ }		//letter-spacing
//		public native String  textDecoration{ }		//text-decoration
//		public native String  unicodeBidi{ }		//unicode-bidi
//		public native String  wordSpacing{ }		//word-spacing
	//
////		• Other properties for visual media: 
//		public native String  clip{ }	//clip //only applicable to outermost svg element.
//		public native String  color{ } //used to provide a potential indirect value (currentColor) for the ‘fill’, ‘stroke’, ‘stop-color’, ‘flood-color’ and ‘l{ }ghting-color’ properties. (The SVG properties which support color allow a color specification which is extended from CSS2 to accommodate color definitions in arbitrary color spaces. See Color profile descriptions.)
//		public native String  cursor{ }
//		public native String  display{ }
//		public native String  overflow{ } //only applicable to elements which establish a new viewport.
//		public native String  visibility{ }


//		The following SVG properties are not defined in CSS2. The complete normative definitions for these properties are found in this specification:
//		Clipping, //Masking and Compositing properties: public native String  clip-path’
		toCamel.put("clip-rule".toCharArray(), "clipRule".toCharArray());		//clip-rule
		toCamel.put("mask".toCharArray(), "mask".toCharArray());
		toCamel.put("opacity".toCharArray(), "opacity".toCharArray());

//		• Filter Effects properties: 
		toCamel.put("enable-background".toCharArray(), "enableBackground".toCharArray());	//enable-background
		toCamel.put("filter".toCharArray(), "filter".toCharArray());
		toCamel.put("flood-color".toCharArray(), "floodColor".toCharArray());			//flood-color
		toCamel.put("flood-opacity".toCharArray(), "floodOpacity".toCharArray());		//flood-opacity
		toCamel.put("lighting-color".toCharArray(), "lightingColor".toCharArray());		//lighting-color

//		• Gradient properties: 
		toCamel.put("stop-color".toCharArray(), "stopColor".toCharArray());			//stop-color
		toCamel.put("stop-opacity".toCharArray(), "stopOpacity".toCharArray());		//stop-opacity

//		• Interactivity properties: 
		toCamel.put("pointer-events".toCharArray(), "pointerEvents".toCharArray());		//pointer-events

//		• Color and Painting properties: 
		toCamel.put("color-interpolation".toCharArray(), "colorInterpolation".toCharArray());	//color-interpolation
		toCamel.put("color-interpolation-filters".toCharArray(), "colorInterpolationFilters".toCharArray());		//color-interpolation-filters
		toCamel.put("color-profile".toCharArray(), "colorProfile".toCharArray());		//color-profile
		toCamel.put("color-rendering".toCharArray(), "colorRendering".toCharArray());		//color-rendering
		toCamel.put("fill".toCharArray(), "fill".toCharArray());				//fill
		toCamel.put("fill-opacity".toCharArray(), "fillOpacity".toCharArray());		//fill-opacity
		toCamel.put("fill-rule".toCharArray(), "fillRule".toCharArray());			//fill-rule
		toCamel.put("image-rendering".toCharArray(), "imageRendering".toCharArray());	//image-rendering
		toCamel.put("marker".toCharArray(), "marker".toCharArray());				//marker
		toCamel.put("marker-end".toCharArray(), "markerEnd".toCharArray());			//marker-end
		toCamel.put("marker-mid".toCharArray(), "markerMid".toCharArray());			//marker-mid
		toCamel.put("marker-start".toCharArray(), "markerStart".toCharArray());		//marker-start
		toCamel.put("shape-rendering".toCharArray(), "shapeRendering".toCharArray());	//shape-rendering
		toCamel.put("stroke".toCharArray(), "stroke".toCharArray());				//stroke
		toCamel.put("stroke-dasharray".toCharArray(), "strokeDasharray".toCharArray());	//stroke-dasharray
		toCamel.put("stroke-dashoffset".toCharArray(), "strokeDashoffset".toCharArray());	//stroke-dashoffset
		toCamel.put("stroke-linecap".toCharArray(), "strokeLinecap".toCharArray());		//stroke-linecap
		toCamel.put("stroke-linejoin".toCharArray(), "strokeLinejoin".toCharArray());	//stroke-linejoin
		toCamel.put("stroke-miterlimit".toCharArray(), "strokeMiterlimit".toCharArray());	//stroke-miterlimit
		toCamel.put("stroke-opacity".toCharArray(), "strokeOpacity".toCharArray());		//stroke-opacity
		toCamel.put("stroke-width".toCharArray(), "strokeWidth".toCharArray());		//stroke-width
		toCamel.put("text-rendering".toCharArray(), "textRendering".toCharArray());		//text-rendering

//		• Text properties: 
		toCamel.put("alignment-baseline".toCharArray(), "alignmentBaseline".toCharArray());	//alignment-baseline
		toCamel.put("baseline-shift".toCharArray(), "baselineShift".toCharArray());		//baseline-shift
		toCamel.put("dominant-baseline".toCharArray(), "dominantBaseline".toCharArray());	//dominant-baseline
		toCamel.put("glyph-orientation-horizontal".toCharArray(), "glyphOrientationHorizontal".toCharArray()); //glyph-orientation-horizontal
		toCamel.put("glyph-orientation-vertical".toCharArray(), "glyphOrientationVertical".toCharArray());	//glyph-orientation-vertical
		toCamel.put("kerning".toCharArray(), "kerning".toCharArray());
		toCamel.put("text-anchor".toCharArray(), "textAnchor".toCharArray());	//text-anchor
		toCamel.put("writing-mode".toCharArray(), "writingMode".toCharArray());	//writing-mode
		
//		SVG font
		toCamel.put("horiz-origin-x".toCharArray(), "horizOriginX".toCharArray()); // %Number.datatype; #IMPLIED
	    toCamel.put("horiz-origin-y".toCharArray(), "horizOriginY".toCharArray()); // %Number.datatype; #IMPLIED
	    toCamel.put("horiz-adv-x".toCharArray(), "horizAdvX".toCharArray()); // %Number.datatype; #REQUIRED
	    toCamel.put("vert-origin-x".toCharArray(), "vertOriginX".toCharArray()); // %Number.datatype; #IMPLIED
	    toCamel.put("vert-origin-y".toCharArray(), "vertOriginY".toCharArray()); // %Number.datatype; #IMPLIED
	    toCamel.put("vert-adv-y".toCharArray(), "vertAdvY".toCharArray()); // %Number.datatype; #IMPLIED
	    
	    
//	    toCamel.put("font-family".toCharArray(), "fontFamily".toCharArray());
//	    toCamel.put("font-style".toCharArray(), "aaa".toCharArray());
//	    toCamel.put("font-variant".toCharArray(), "aaa".toCharArray());
//	    toCamel.put("font-weight".toCharArray(), "aaa".toCharArray());
//	    toCamel.put("font-stretch".toCharArray(), "aaa".toCharArray());
//	    toCamel.put("font-size".toCharArray(), "aaa".toCharArray());
	    toCamel.put("unicode-range".toCharArray(), "unicodeRange".toCharArray());
	    toCamel.put("units-per-em".toCharArray(), "unitsPerEm".toCharArray());
	    toCamel.put("panose-1".toCharArray(), "panose1".toCharArray());
	    toCamel.put("stemv".toCharArray(), "stemv".toCharArray());
	    toCamel.put("stemh".toCharArray(), "stemh".toCharArray());
	    toCamel.put("slope".toCharArray(), "slope".toCharArray());
	    toCamel.put("cap-height".toCharArray(), "capHeight".toCharArray());
	    toCamel.put("x-height".toCharArray(), "xHeight".toCharArray());
	    toCamel.put("accent-height".toCharArray(), "accentHeight".toCharArray());
	    toCamel.put("ascent".toCharArray(), "ascent".toCharArray());
	    toCamel.put("descent".toCharArray(), "descent".toCharArray());
	    toCamel.put("widths".toCharArray(), "widths".toCharArray());
	    toCamel.put("bbox".toCharArray(), "bbox".toCharArray());
	    toCamel.put("ideographic".toCharArray(), "ideographic".toCharArray());
	    toCamel.put("alphabetic".toCharArray(), "alphabetic".toCharArray());
	    toCamel.put("mathematical".toCharArray(), "mathematical".toCharArray());
	    toCamel.put("hanging".toCharArray(), "hanging".toCharArray());
	    toCamel.put("v-ideographic".toCharArray(), "vIdeographic".toCharArray());
	    toCamel.put("v-alphabetic".toCharArray(), "vAlphabetic".toCharArray());
	    toCamel.put("v-mathematical".toCharArray(), "vMathematical".toCharArray());
	    toCamel.put("v-hanging".toCharArray(), "vHanging".toCharArray());
	    toCamel.put("underline-position".toCharArray(), "underlinePosition".toCharArray());
	    toCamel.put("underline-thickness".toCharArray(), "underlineThickness".toCharArray());
	    toCamel.put("strikethrough-position".toCharArray(), "strikethroughPosition".toCharArray());
	    toCamel.put("strikethrough-thickness".toCharArray(), "strikethroughThickness".toCharArray());
	    toCamel.put("overline-position".toCharArray(), "overlinePosition".toCharArray());
	    toCamel.put("overline-thickness".toCharArray(), "overlineThickness".toCharArray());

	}
	
	public static final char[] toCamel(char[] hypen){
		char[] camel = toCamel.get(hypen);
		return camel == null ? hypen : camel;
	}
	
	public static final char[] toHyphen(char[] camel){
		char[] hyphen = toHyphen.get(camel);
		return hyphen == null ? camel : hyphen;
	}
	
}
