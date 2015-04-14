package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.javascript.JsConstant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public class HtmlElement extends HtmlNode {
	public static final HtmlAttribute[] NO_ATTRIBUTES = new HtmlAttribute[0];
	public static final HtmlNode[] NO_ELEMENTS= new HtmlNode[0];

	public TypeReference type;
	public TypeReference closeType;
	public HtmlAttribute[] attributes = NO_ATTRIBUTES;

	public HtmlNode[] children = NO_ELEMENTS;
	public int declarationSourceStart;
	public int declarationSourceEnd;
	public int bodyStart;
	public int bodyEnd; // doesn't include the trailing comment if any.
	public BlockScope scope;
	
	
	public HtmlElement(){
		super();
	}
	

	public StringBuffer print(int indent, StringBuffer output) {
		return printStatement(indent, output);
	}
	
	@Override
	public StringBuffer printStatement(int indent, StringBuffer output) {
		printIndent(indent, output);
		
		output.append("<");
		printTagName(indent, output);
//		printProperties(indent, output);
		
		if(this.kind == EMPTY_ELEMENT){
			return output.append("/>");
		} else {
			output.append(">");
		}
		
//		printChildElement(indent, output);
		
		output.append("\n");
		printIndent(indent, output).append("</");
		
		printTagName(indent, output);
		
		return output.append(">");
	}
	
	public void traverse(ASTVisitor visitor, ClassScope scope) {
		if (visitor.visit(this, scope)) {
			if (this.children != null) {
				for (int i = 0, length = this.children.length; i < length; i++)
					children[i].traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, scope);
	}
	
	protected void printTagName(int indent, StringBuffer output){
		
	}
	
	/**
	 * Resolve the type of this expression in the context of a blockScope
	 *
	 * @param scope
	 * @return
	 * 	Return the actual type of this expression after resolution
	 */
	public TypeBinding resolveType(BlockScope scope) {
		// by default... subclasses should implement a better TB if required.
		return null;
	}
	
	/**
	 * Resolve the type of this expression in the context of a classScope
	 *
	 * @param scope
	 * @return
	 * 	Return the actual type of this expression after resolution
	 */
	public TypeBinding resolveType(ClassScope scope) {
		// by default... subclasses should implement a better TB if required.
		return null;
	}
	
	public void resolve(BlockScope scope) {
		this.constant = Constant.NotAConstant;
		if(this.type != null){
			this.resolvedType = type.resolveType(scope);
		}
		
		if(this.closeType != null){
			closeType.resolveType(scope);
		}
		
		BlockScope eleScope = new BlockScope(scope, this);
		resolveAttribute(eleScope);
		resolveChild(eleScope);
	}
	
	protected void resolveChild(BlockScope scope){
		for(HtmlNode child: children){
			if(child instanceof HtmlText || child instanceof HtmlComment){
				continue;
			}
			child.resolve(scope);
		}
	}
	
	protected void resolveAttribute(BlockScope scope){
		for(HtmlAttribute attr : attributes){
			attr.resolve(scope);
		}
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return output;
	}
	
	protected StringBuffer buildDOMScript(Scope parentScope, int indent, StringBuffer output, String parent, String context){
		output.append("\n");
		printIndent(indent, output);
		output.append("(function(p){");
		
		output.append("var n"); 
		output.append(" = $.n(\"").append(type.getLastToken()).append("\");");
		output.append('\n');
		printIndent(indent + 1, output).append("$.a(p, n);");
		
		for(HtmlAttribute attr : this.attributes){
			attr.buildScript(scope, indent, output, "n");
		}
		
		for(HtmlNode child : this.children){
			child.buildDOMScript(scope, indent + 1, output, "n", context);
		}
			
		output.append("\n");
		printIndent(indent, output);
		output.append("}).call(").append(context).append(", ").append(parent).append(");");
		return output;
	}
	
//	//used in create method of template
//	public void buildElement(Scope scope, int indent, StringBuffer output, String parent, String context){
//		output.append("\n");
//		printIndent(indent + 1, output);
//		output.append("var n"); 
//		
//		output.append(" = $.c(\"").append(this.type.getLastToken()).append("\");");
//		
//		output.append("$.a(parent, n);");
//		
//		for(HtmlAttribute attr : this.attributes){
//			attr.generateExpression(scope, indent, output).append(";");
//		}
//	
//		if(this.children != null && this.children.length > 0){
//			this.buildDOMScript(scope, indent + 1, output, "_n", context);
//		}
//	}
	
	//used in createRoot method of itemTemplate
	public void buildRootElement(Scope parentScope, int indent, StringBuffer output, String parent, String context){
		output.append('\n');
		printIndent(indent+1, output);
		output.append("this.before(data);");
		
		output.append("\n");
		printIndent(indent + 1, output);
		output.append("var ").append(JsConstant.NODE_NAME).append(" = ").append(JsConstant.CREATE_ELEMENT).append("(\"").append(this.type.getLastToken()).append("\");");
		output.append('\n');
		printIndent(indent + 1, output).append(JsConstant.APPEND_CHILD).append("(parent, n);");
		
		output.append("\n");
		printIndent(indent + 1, output);
		output.append("this.setupDataContext(n, data, index);");
		
		for(HtmlAttribute attr : this.attributes){
			attr.buildScript(scope, indent, output, JsConstant.NODE_NAME);
		}
	}
	
	private static final char[] body = "body".toCharArray();
	private static final char[] head = "head".toCharArray();
	private static final char[] meta = "meta".toCharArray();
	private static final char[] TEXT = "Text".toCharArray();
	public StringBuffer html(BlockScope parentScope, int indent, StringBuffer output) {
		output.append('<').append(type.getLastToken());
		for(HtmlAttribute attr : this.attributes){
			if((attr.tagBits & HtmlBits.HasDynamicContent) != 0)
				continue;
			output.append(" ");
			attr.html(scope, indent, output);
		}
		if(CharOperation.equals(type.getLastToken(), head)) {
			output.append('>');
			for(HtmlNode child : this.children){
				child.html(scope, indent + 1, output);
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
			ClassScope classScope = scope.enclosingClassScope();
			TypeDeclaration typeDecl = classScope.referenceContext;
			output.append("\n");
			printIndent(indent, output);
			output.append("<script type = 'text/javascript' >");
			output.append("\n");
			printIndent(indent+1, output);
			output.append("var __this = new (");
			typeDecl.generateInternal(classScope, indent + 1, output);
			output.append(")();");
			
			output.append("\n");
			printIndent(indent, output);
			output.append("</script>");
		} else {
			if((this.tagBits & HtmlBits.IsSimpleElement) != 0){
				output.append(" />");
				createEmbedScript(scope, indent, output, true);
			} else {
				output.append('>');
				createEmbedScript(scope, indent, output, false);
				for(HtmlNode child : this.children){
					child.html(scope, indent + 1, output);
				}
				output.append("</").append(type.getLastToken()).append('>');
			}
		}

		return output;
	}
	
	private StringBuffer createEmbedScript(BlockScope scope, int indent, StringBuffer output, boolean sibling){
		if((this.tagBits & HtmlBits.HasDynamicContent) != 0){
			ClassScope classScope = scope.classScope();
			String scriptId = "_" + classScope.scriptId++;
			output.append("<script id='").append(scriptId).append("' type = 'text/javascript'").append('>');
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("(function(n){");
			
			for(HtmlAttribute attr : this.attributes){
				if((attr.tagBits & HtmlBits.NamedField) != 0){
					output.append("\n");
					printIndent(indent + 2, output);
					output.append("n").append("[\"");
					attr.property.generateExpression(scope, indent, output);
					output.append("\"]");
					output.append(" = ");
					if(attr.value instanceof Literal){
						attr.value.generateExpression(scope, indent, output);
					}
					output.append(";");
					output.append("\n");
					printIndent(indent + 2, output);
					output.append("this");
					output.append('.').append(((Literal) attr.value).source());
					output.append(" = ").append("n").append(";");
				}
				if((attr.tagBits & HtmlBits.HasMarkupExtension) != 0){
					output.append("\n");
					printIndent(indent + 2, output);
					if(attr.value instanceof HtmlMarkupExtension){
						HtmlMarkupExtension me = (HtmlMarkupExtension) attr.value;
						me.doGenerateExpression(scope, indent, output);
						
						output.append(".inject(");
						output.append("n, "); 
						attr.property.buildInjectPart(classScope, indent, output);
						output.append(");");
					}
				}
			}
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("})($('").append(scriptId).append("')").append(sibling ? ".previousSibling" : ".parentNode").append(");");
			
			output.append("\n");
			printIndent(indent, output);
			output.append("</script>");
		}
		return output;
	}
	
	//cym 2015-02-22
	public ASTNode parserElement(int position){
		for(HtmlAttribute attribute : this.attributes){
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
			if(child.sourceStart < position && child.sourceEnd >= position){
				return child.parserElement(position);
			}
		}
		return null;
		
	}
	
	public StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		output.append('<').append(type.getLastToken());
		for(HtmlAttribute attr : this.attributes){
			if((attr.tagBits & HtmlBits.HasTemplate) != 0){
				continue;
			}
			output.append(" ");
			attr.buildProperty(scope, indent, output, "__this");
		}
		
		if(CharOperation.endsWith(type.getLastToken(), body)) {
			output.append('>');
			
			output.append("\n");
			printIndent(indent, output);
			output.append("<script type = 'text/javascript' >");
			 
			output.append("\n");
			printIndent(indent, output);
			output.append("var _c = document.createElement.bind(document);");
			
			output.append("\n");
			printIndent(indent, output);
			output.append("var _a = Node.prototype.appendChild;");
			
			buildDOMScript(scope, indent + 1, output, "document.body", "__this");
			output.append("</script>");
		} else if(CharOperation.equals(type.getLastToken(), head)) {
			output.append('>');
			
			for(HtmlNode child : this.children){
				output.append("\n");
				child.generateStatement(scope, indent + 1, output);
			}
			//output meta
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
			ClassScope classScope = scope.enclosingClassScope();
			TypeDeclaration typeDecl = classScope.referenceContext;
			output.append("\n");
			printIndent(indent, output);
			output.append("<script type = 'text/javascript' >");
			output.append("\n");
			printIndent(indent+1, output);
			output.append("var __this = new (");
			typeDecl.generateInternal(classScope, indent + 1, output);
			output.append(")();");
			
			output.append("\n");
			printIndent(indent, output);
			output.append("</script>");
		} else if(CharOperation.endsWith(type.getLastToken(), meta)){ 
			return output.append(">");
		} else if(CharOperation.endsWith(type.getLastToken(), TEXT)){ 
//			return output.append(">");
		} else {
			output.append('>');
			for(HtmlNode child : this.children){
				output.append("\n");
				child.generateStatement(scope, indent + 1, output);
			}
		}
		output.append("\n");
		printIndent(indent, output);
		output.append("</").append(type.getLastToken()).append('>');
		return output;
	}
	
//	public StringBuffer generateDynamicHTML(Scope scope, int indent, StringBuffer output) {
//		output.append('<').append(type.getLastToken());
//		for(HtmlAttribute attr : this.attributes){
//			if((attr.tagBits & HtmlBits.HasTemplate) != 0){
//				continue;
//			}
//			output.append(" ");
//			attr.buildProperty(scope, indent, output, "__this");
//		}
//		
//		if(CharOperation.endsWith(type.getLastToken(), body)) {
//			output.append('>');
//			
//			output.append("\n");
//			printIndent(indent, output);
//			output.append("<script type = 'text/javascript' >");
//			 
//			output.append("\n");
//			printIndent(indent, output);
//			output.append("var _c = document.createElement.bind(document);");
//			
//			output.append("\n");
//			printIndent(indent, output);
//			output.append("var _a = Node.prototype.appendChild;");
//			
//			buildDOMScript(scope, indent + 1, output, "document.body", "__this");
//			output.append("</script>");
//		} else if(CharOperation.equals(type.getLastToken(), head)) {
//			output.append('>');
//			
//			for(HtmlNode child : this.children){
//				output.append("\n");
//				child.generateStatement(scope, indent + 1, output);
//			}
//			//output meta
//			output.append("<script type = 'text/javascript' src = '/lark/js/stub.js'> </script>");
//			output.append("\n");
//			printIndent(indent, output);
//			output.append("<script type = 'text/javascript' src = '/lark/java/lang/buildins.js'> </script>");
//			output.append("\n");
//			printIndent(indent, output);
//			output.append("<script type = 'text/javascript' src = '/lark/org/w3c/dom/dom.js'> </script>");
//			output.append("\n");
//			printIndent(indent, output);
//			output.append("<script type = 'text/javascript' src = '/lark/java/lang/bindings.js'> </script>");
//			//build class definition
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
//			
//			output.append("\n");
//			printIndent(indent, output);
//			output.append("</script>");
//		} else if(CharOperation.endsWith(type.getLastToken(), meta)){ 
//			return output.append(">");
//		} else if(CharOperation.endsWith(type.getLastToken(), TEXT)){ 
////			return output.append(">");
//		} else {
//			output.append('>');
//			for(HtmlNode child : this.children){
//				output.append("\n");
//				child.generateStatement(scope, indent + 1, output);
//			}
//		}
//		output.append("\n");
//		printIndent(indent, output);
//		output.append("</").append(type.getLastToken()).append('>');
//		return output;
//	}
}
