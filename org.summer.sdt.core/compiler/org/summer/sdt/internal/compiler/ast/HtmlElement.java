package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.html.HtmlTagConstants;
import org.summer.sdt.internal.compiler.html.Namespace;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.javascript.JsConstant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TypeIds;
import org.summer.sdt.internal.compiler.util.HashtableOfCharArray;

/**
 * 
 * @author cym
 * 
 *         using by HTML
 */
public class HtmlElement extends HtmlNode {
	public static final HtmlAttribute[] NO_ATTRIBUTES = new HtmlAttribute[0];
	public static final HtmlNode[] NO_ELEMENTS= new HtmlNode[0];

	public char[][] tokens;
	public long[] positions;
	
	public TypeReference type;
	public TypeReference closeType;
	public HtmlAttribute[] attributes = NO_ATTRIBUTES;

	public HtmlNode[] children = NO_ELEMENTS;
	public int declarationSourceStart;
	public int declarationSourceEnd;
	public BlockScope scope;
	
	public char[] defaultNamespace;
	public char[] namespace;
	public HashtableOfCharArray/*<char[], char[]>*/ namespaces;
	
	public FieldDeclaration namedField;
	public HtmlAttribute name;   //the name of current element
	
	public HtmlElement(char[][] tokens, long[] positions, TypeReference type){
		super();
		
		this.tokens = tokens; 
		this.positions = positions;
		this.type = type;
	}
	
	public HtmlElement(char[][] tokens, long[] positions, char[] prefix, long prefixPos, TypeReference type){
		super(prefix, prefixPos);
		
		this.tokens = tokens;
		this.positions = positions;
		this.type = type;
	}
	
	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		printIndent(indent, output);
		
		output.append("<");
		printTagName(indent, output);
		
		for(HtmlAttribute attribute: this.attributes){
			output.append(" ");
			attribute.print(indent, output);
		}
		
		if((this.tagBits & HtmlBits.SimpleElement) != 0){
			return output.append("/>");
		} else {
			output.append(">");
		}
		
		for(HtmlNode child : this.children){
			child.print(indent, output);
		}
		
		output.append("\n");
		printIndent(indent, output).append("</");
		
		printTagName(indent, output);
		
		return output.append(">");
	}
	
	@Override
	public StringBuffer printStatement(int indent, StringBuffer output) {
		return print(indent, output); //$NON-NLS-1$
	}
	
	public void traverse(ASTVisitor visitor, ClassScope scope) {
		if (visitor.visit(this, scope)) {
			if (this.children != null) {
				for (int i = 0, length = this.children.length; i < length; i++)
					children[i].traverse(visitor, scope);
			}
			
			if(this.attributes != null){
				for (int i = 0, length = this.attributes.length; i < length; i++)
					attributes[i].traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, scope);
	}
	
	protected void printTagName(int indent, StringBuffer output){
		output.append(CharOperation.concatWith(tokens, '-'));
	}
	
	public void resolve(BlockScope parentScope) {
		internalResolve(parentScope);
	}
	
	public BlockScope createScope(Scope parent){
		return this.scope = new BlockScope(parent, this);
	}
	
	protected void internalResolve(BlockScope parent) {
		if(this.resolvedType != null) return;
		
		if(scope == null) {
			createScope(parent);
		}
			
		processNamespace(this.scope);
		
		char[] selector = CharOperation.concatWith(this.tokens, '-');
		switch(selector.length){
		case 8:
			if(selector[0] == 'f'){
				if(CharOperation.equals(selector, HtmlTagConstants.FRAGMENT)){
					this.tagBits |= HtmlBits.Fragment;
				}
			} else if(selector[0] == 't'){
				if(CharOperation.equals(selector, HtmlTagConstants.TEMPLATE)){
					this.tagBits |= HtmlBits.Template;
				}
			}
			break;
		case 19: //collection-template
			if(selector[0] == 'c'){
				if(CharOperation.equals(selector, HtmlTagConstants.COLLECTION_TEMPLATE)){
					this.tagBits |= HtmlBits.CollectionTemplate;
				}
			}
			break;
		}
		
		this.constant = Constant.NotAConstant;
		if(this.type != null){
			this.resolvedType = type.resolveType(scope);
		}
		
//		HtmlTypeReference hRef = (HtmlTypeReference) type;
//		if(namedField != null){
//			this.namedField.type = new QualifiedTypeReference(hRef.actualTokens, hRef.positions);
//		}
		
		if(this.closeType != null){
			closeType.resolveType(this.scope);
		}
		
		resolveAttribute(this.scope);
		resolveChild(this.scope);
	}
	
	protected void resolveChild(BlockScope scope){
		for(HtmlNode child: children){
			if(child instanceof HtmlText || child instanceof HtmlComment){
				continue;
			}
			child.resolve(scope);
		}
	}
	
	protected void processNamespace(BlockScope scope){
		for(HtmlAttribute attr : attributes){
			attr.processNamespace(this);
		}
		
		if(this.prefix != null){
			this.namespace = scope.getNamespace(this.prefix);
			if(namespace == null){
//				scope.problemReporter().invalidEnumLiternal(liternal);   //TODO
			}
		}
		
		if(this.tokens.length == 1 && this.defaultNamespace == null){
			switch(tokens[0].length){
			case 4:
				if(tokens[0][0] == 'h' && CharOperation.equals(Namespace.HTML_TAG, this.tokens[0])){
					this.defaultNamespace = Namespace.HTML5;
					return;
				}
				break;
			case 3:
				if(tokens[0][0] == 's' && CharOperation.equals(Namespace.SVG_TAG, this.tokens[0])) {
					this.namespace = this.defaultNamespace = Namespace.SVG;
					return;
				}
				break;
			}
		}
		
		this.defaultNamespace = scope.getDefaultNamespace();
	}
	
	public char[] namespace(){
		return this.namespace != null ? this.namespace : this.defaultNamespace;
	}
	
	/**
	 * get the name of current element
	 * @return
	 */
	public char[] name(){
		if(this.name != null){
			return this.name.value();
		}
		return null;
	}
	
	protected void resolveAttribute(BlockScope scope){
		for(HtmlAttribute attr : attributes){
			attr.resolve(scope);
		}
	}
	
	protected StringBuffer scriptDom(BlockScope parent, int indent, StringBuffer output, String node, String _this){
		output.append("\n");
		printIndent(indent, output);
		output.append("(function(p){");
		
		switch(this.resolvedType.id){
		case TypeIds.T_JavaLangTemplateSetting:
		case TypeIds.T_JavaLangCollectionTemplateSetting:
		case TypeIds.T_JavaLangFragmentSetting:
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("p.setTemplate(\"");
			if(this.name == null){
				output.append(name.value());
			} else {
				ClassScope classScope = scope.classScope();
				String scriptId = "_" + classScope.scriptId++;
				output.append(scriptId);
			}
			output.append("\", new (");
			this.type.resolvedType.generate(output, scope.classScope().enclosingSourceType());
			output.append(")(");
			
			this.option(scope, indent, output, _this);
			
			output.append("));");
			break;
		default: 
			output.append("var n = "); 
			if(this.resolvedType.id == TypeIds.T_OrgW3cDomText){
				output.append("$.t();");
			} else {
				if(this.namespace != null){
					if((namespace.length == 26 && CharOperation.equals(namespace, Namespace.SVG))){
						output.append("$.svg(");
					} else {
						output.append("$.ns(\"").append(namespace).append("\", ");
					}
				} else if(defaultNamespace != null){
					if(defaultNamespace.length == 26 && CharOperation.equals(defaultNamespace, Namespace.SVG)){
						output.append("$.svg(");
					}
				} else {
					output.append("$.n(");
				}
				output.append("\"").append(type.getLastToken()).append("\");");
			}
			output.append('\n');
			printIndent(indent + 1, output).append("$.a(p, n);");
			
			for(HtmlAttribute attr : this.attributes){
				attr.scriptDom(this.scope, indent, output, "n", _this);
			}
			
			for(HtmlNode child : this.children){
				child.scriptDom(this.scope, indent + 1, output, "n", _this);
			}
		}
		output.append("\n");
		printIndent(indent, output);
		output.append("}).call(").append(_this).append(", ").append(node).append(");");
		return output;
	}
	
	//used in createRoot method of itemTemplate
	public void templateRootElement(Scope parentScope, int indent, StringBuffer output, String parent, String _this){
		switch(this.resolvedType.id){
		case TypeIds.T_JavaLangTemplateSetting:
		case TypeIds.T_JavaLangCollectionTemplateSetting:
		case TypeIds.T_JavaLangFragmentSetting:
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("p.setTemplate(\"");
			if(this.name != null){
				output.append(name.value());
			} else {
				ClassScope classScope = scope.classScope();
				String scriptId = "_" + classScope.scriptId++;
				output.append(scriptId);
			}
			output.append("\", new (");
			this.type.resolvedType.generate(output, scope.classScope().enclosingSourceType());
			output.append(")(");
			
			this.option(scope, indent, output, _this);
			
			output.append("));");
			break;
		default:
			output.append("this.before(data);");
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("var n = "); 
			if(this.resolvedType.id == TypeIds.T_OrgW3cDomText){
				output.append("$.t();");
			} else {
				if(this.namespace != null){
					if(namespace.length == 26 && CharOperation.equals(namespace, Namespace.SVG)){
						output.append("$.nSVG(\"").append("\", ");
					} else {
						output.append("$.ns(\"").append(namespace).append("\", ");
					}
				} else {
					output.append("$.n(");
				}
				output.append("\"").append(type.getLastToken()).append("\");");
			}
			
			output.append('\n');
			printIndent(indent + 1, output).append(JsConstant.APPEND_CHILD).append("(parent, n);");
			
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("this._rootNodes.push(").append(JsConstant.NODE_NAME).append(");");
			
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("this.setupDataContext(n, data, index);");
			
			for(HtmlAttribute attr : this.attributes){
				attr.scriptDom(scope, indent, output, JsConstant.NODE_NAME, _this);
			}
			
			for(HtmlNode child : this.children){
				child.scriptDom(scope, indent + 1, output, "n", _this);
			}
			
			output.append('\n');
			printIndent(indent+3, output);
			output.append("this.after(data, index);");
		}
	}
	
	public StringBuffer html(BlockScope parent, int indent, StringBuffer output, String _this) {
		if(this.resolvedType == null){
			return output;
		}
		if((this.tagBits & HtmlBits.Lark_Element_Mask) !=0){
			if((this.tagBits & HtmlBits.Fragment) != 0){
				if(this.type.resolvedType != null){

				}
				return output;
			} else if((this.tagBits & HtmlBits.Template) != 0){
				if(this.type.resolvedType != null){
					output.append("<!--template -->\n");
					larkElementScript(scope, indent, output, _this, false);
				}
				return output;
			}else if((this.tagBits & HtmlBits.CollectionTemplate) != 0){
				if(this.type.resolvedType != null){
					output.append("<!--collection-template -->\n");
					larkElementScript(scope, indent, output, _this, false);
				}
				return output;
			} 
		} 
		
		if(this.resolvedType.id == TypeIds.T_OrgW3cDomText){
			processTextNode(scope, indent, output, _this);
			return output;
		}
		
		boolean script = false;
		output.append('<').append(type.getLastToken());
		for(HtmlAttribute attr : this.attributes){
			if(attr.hasMarkupExtension()){
				script  = true;
				continue;
			}
				
			output.append(" ");
			attr.html(scope, indent, output, _this);
		}
		if(this.resolvedType.id == TypeIds.T_OrgW3cHtmlHead){
			output.append('>');
			for(HtmlNode child : this.children){
				child.html(scope, indent + 1, output, _this);
			}
			//output meta
			output.append("\n");
			printIndent(indent, output);
			output.append("<script type = 'text/javascript' src = '/lark/js/stub.js'> </script>");
			output.append("\n");
			printIndent(indent, output);
			output.append("<script type = 'text/javascript' src = '/lark/java/lang/buildins.js'> </script>");
			output.append("\n");
			printIndent(indent, output);
			output.append("<script type = 'text/javascript' src = '/lark/org/w3c/dom/dom.js'> </script>");
			output.append("\n");
			printIndent(indent, output);
			output.append("<script type = 'text/javascript' src = '/lark/java/lang/bindings.js'> </script>");
			//build class definition
//			ClassScope classScope = scope.enclosingClassScope();
//			TypeDeclaration typeDecl = classScope.referenceContext;
//			output.append("\n");
//			printIndent(indent, output);
//			output.append("<script type = 'text/javascript' >");
//			output.append("\n");
//			printIndent(indent+1, output);
//			output.append("var __this = new (");
//			typeDecl.generateInternal(classScope, indent + 1, output);
//			output.append(")();");
			
			ClassScope classScope = scope.enclosingClassScope();
			TypeDeclaration typeDecl = classScope.referenceContext;
			output.append("\n");
			printIndent(indent, output);
			output.append("<script type = 'text/javascript' >");
			output.append("\n");
			printIndent(indent+1, output);
			output.append("var __this = new (");
			typeDecl.binding.generate(output, null);
			output.append(")();");
			
			output.append("\n");
			printIndent(indent+1, output);
			output.append("document.documentElement.__dataContext = new (__lc(\"java.lang.DataContext\"))({dataItem:__this, mode:__lc(\"java.lang.DataContextMode\").Root});");
			
			output.append("\n");
			printIndent(indent, output);
			output.append("</script>");
		} else {
			if((this.tagBits & HtmlBits.SimpleElement) != 0){
				output.append(" />");
				if(script){
					markupExtensionScript(scope, indent, output, true);
				}
			} else {
				output.append('>');
				if(script){
					markupExtensionScript(scope, indent, output, false);
				}
				for(HtmlNode child : this.children){
					child.html(scope, indent + 1, output, _this);
				}
				output.append("</").append(type.getLastToken()).append('>');
			}
		}

		return output;
	}
	
	private void processTextNode(BlockScope scope2, int indent, StringBuffer output, String _this) {
		ClassScope classScope = scope.classScope();
		String scriptId = "_" + classScope.scriptId++;
		output.append("<script id='").append(scriptId).append("' type = 'text/javascript'").append('>');
		StringBuffer node = new StringBuffer();
		node.append("$('").append(scriptId).append("')").append(".parentNode");
		scriptDom(scope2, indent, output, node.toString(), _this);
		output.append("\n");
		printIndent(indent, output);
		output.append("</script>");
	}

	private StringBuffer larkElementScript(BlockScope scope, int indent, StringBuffer output, String _this, boolean sibling){
			ClassScope classScope = scope.classScope();
			String scriptId = "_" + classScope.scriptId++;
			output.append("<script id='").append(scriptId).append("' type = 'text/javascript'").append('>');
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("$('").append(scriptId).append("')").append(".parentNode.addTemplate(");
			output.append("new (");
			this.type.resolvedType.generate(output, scope.classScope().enclosingSourceType());
			output.append(")(");
			
			this.option(scope, indent, output, _this);
			
			output.append("));");
			
			output.append("\n");
			printIndent(indent, output);
			output.append("</script>");
		return output;
	}
	
	private StringBuffer markupExtensionScript(BlockScope scope, int indent, StringBuffer output, boolean sibling){
		ClassScope classScope = scope.classScope();
		String scriptId = "_" + classScope.scriptId++;
		output.append("<script id='").append(scriptId).append("' type = 'text/javascript'").append('>');
		output.append("\n");
		printIndent(indent + 1, output);
		output.append("(function(n){");
		
		for(HtmlAttribute attr : this.attributes){
			attr.scriptElement(scope, indent, output, "n", "__this");
		}
		output.append("\n");
		printIndent(indent + 1, output);
		output.append("})($('").append(scriptId).append("')").append(sibling ? ".previousSibling" : ".parentNode").append(");");
		
		output.append("\n");
		printIndent(indent, output);
		output.append("</script>");
		return output;
	}
	
	//cym 2015-02-22
	public ASTNode parserElement(int position){
		if (this.bodyStart > position)
			return null;
			
		
		for(HtmlAttribute attribute : this.attributes){
			if (attribute.bodyStart > position)
				return null;
			
			if(attribute.property.sourceStart < position && attribute.property.sourceEnd >= position){
				return attribute.property;
			} 
			if(attribute.value.sourceStart < position && attribute.value.sourceEnd >= position){
				if(attribute.value instanceof HtmlMarkupExtension){
					HtmlMarkupExtension markup = (HtmlMarkupExtension) attribute.value;
					return markup.parserElement(position);
				}
				return attribute.value;
			}
//			attribute.parserElement(position);
		}
		
		for(HtmlNode child : this.children){
			if (child.bodyStart > position)
				continue;
			
			return child.parserElement(position);
		}
		return null;
		
	}
	 
	public StringBuffer option(BlockScope scope, int indent, StringBuffer output, String _this) {
		output.append("{");
		boolean comma = false;
		for(HtmlAttribute attribute : this.attributes){
			if(comma){
				output.append(", ");
			}
			
			attribute.option(scope, indent, output, _this);
			comma = true;
		}
		return output.append("}");
		
	}

	public void storeNS(char[] prefix, char[] namespace) {
		if(namespaces == null){
			namespaces = new HashtableOfCharArray();
		}
		namespaces.put(prefix, namespace);
	}
	
	public char[] getNamespace(char[] prefix) {
		if(this.namespaces != null){
			return this.namespaces.get(prefix);
		}
		
		return null;
	}

	public char[] getDefaultNamesapce() {
		if(this.defaultNamespace != null){
			return this.defaultNamespace;
		}
		Scope parent = scope.parent;
		if(parent instanceof BlockScope){
			return ((BlockScope)parent).getDefaultNamespace();
		}
		return null;
	}
}
