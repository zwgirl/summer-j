package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.MethodScope;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TagBits;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeConstants;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public class HtmlObjectElement extends HtmlElement {
	public HtmlObjectElement() {
		super();
	}

	@Override
	protected void printTagName(int indent, StringBuffer output) {
		output.append(type.getLastToken());
	}

	@Override
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {
		// TODO Auto-generated method stub
	}

	private static final char[] body = "body".toCharArray();
	private static final char[] head = "head".toCharArray();
	private static final char[] meta = "meta".toCharArray();
	private static final char[] TEXT = "Text".toCharArray();
	public StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		output.append('<').append(type.getLastToken());
		for(HtmlAttribute attr : this.attributes){
			if((attr.bits & ASTNode.IsTemplate) != 0){
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
		} else if(CharOperation.endsWith(type.getLastToken(), head)) {
			output.append('>');
			
			for(HtmlElement child : this.children){
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
			output.append("<script type = 'text/javascript' src = '/lark/java/lang/beans.js'> </script>");
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
			for(HtmlElement child : this.children){
				output.append("\n");
				child.generateStatement(scope, indent + 1, output);
			}
		}
		output.append("\n");
		printIndent(indent, output);
		output.append("</").append(type.getLastToken()).append('>');
		return output;
	}
	
	public StringBuffer generateDynamicHTML(Scope initializerScope, int indent, StringBuffer output) {
		output.append('<').append(type.getLastToken());
		for(HtmlAttribute attr : this.attributes){
			if((attr.bits & ASTNode.IsTemplate) != 0){
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
		} else if(CharOperation.endsWith(type.getLastToken(), head)) {
			output.append('>');
			
			for(HtmlElement child : this.children){
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
			output.append("<script type = 'text/javascript' src = '/lark/java/lang/beans.js'> </script>");
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
			for(HtmlElement child : this.children){
				output.append("\n");
				child.generateStatement(scope, indent + 1, output);
			}
		}
		output.append("\n");
		printIndent(indent, output);
		output.append("</").append(type.getLastToken()).append('>');
		return output;
	}
	
	protected StringBuffer buildDOMScript(Scope scope, int indent, StringBuffer output, String parent, String context){
		//
		output.append("\n");
		printIndent(indent, output);
		output.append("(function(_p){");
		
		for(HtmlElement child : this.children){
			if(child instanceof HtmlScript){
				continue;
			}
			
			output.append("\n");
			printIndent(indent + 1, output);
			
			if(child instanceof HtmlPCDATA ){
				output.append("_p.appendChild(document.createTextNode(");
				HtmlPCDATA pcdata = (HtmlPCDATA)child;
//				String data = new String(pcdata.source);
				output.append("\"").append(pcdata.translateEntity(false)).append("\"));");
				
			} else if(child instanceof HtmlComment){
				
			} else {
				HtmlObjectElement oe = (HtmlObjectElement) child;
				output.append("var _n"); 
				
				if(CharOperation.equals(oe.type.getLastToken(), "Text".toCharArray())){   //TextNode
					output.append(" = document.createTextNode('');");
				} else {
					output.append(" = _c(\"").append(child.type.getLastToken()).append("\");");
				}
				
				output.append('\n');
				printIndent(indent + 1, output).append("_p.appendChild(_n);");
				
				for(HtmlAttribute attr : child.attributes){
					attr.buildScript(scope, indent, output, "_n");
				}
				if(child.children != null && child.children.length > 0){
					child.buildDOMScript(scope, indent + 1, output, "_n", context);
				}
			}
		}
			
		output.append("\n");
		printIndent(indent, output);
		output.append("}).call(").append(context).append(", ").append(parent).append(");");
		return output;
	}
	
	//used in create method of template
	public void buildElement(Scope scope, int indent, StringBuffer output, String parent, String context){
		output.append("\n");
		printIndent(indent + 1, output);
		output.append("var _n"); 
		
		output.append(" = _c(\"").append(this.type.getLastToken()).append("\");");
		
		output.append(parent).append(".appendChild(_n);");
		
		for(HtmlAttribute attr : this.attributes){
			attr.generateExpression(scope, indent, output).append(";");
		}
	
		if(this.children != null && this.children.length > 0){
			this.buildDOMScript(scope, indent + 1, output, "_n", context);
		}
	}
	
	//used in createRoot method of itemTemplate
	public void buildRootElement(Scope scope, int indent, StringBuffer output, String parent, String context){
		output.append("\n");
		printIndent(indent + 1, output);
		output.append("var _n"); 
		
		output.append(" = _c(\"").append(this.type.getLastToken()).append("\");");
		output.append('\n');
		printIndent(indent + 1, output).append(parent).append(".appendChild(_n);");
		
		output.append("\n");
		printIndent(indent + 1, output);
		output.append("_n.dataContext = new (__lc('java.lang.DataContext'))(item, '12');");
		
		for(HtmlAttribute attr : this.attributes){
			attr.buildScript(scope, indent, output, "_n");
		}
	}
	
	public StringBuffer generateStaticHTML(Scope scope, int indent, StringBuffer output) {
		output.append("\n");
		printIndent(indent, output);
		output.append('<').append(type.getLastToken());
		for(HtmlAttribute attr : this.attributes){
			output.append(" ");
			attr.generateExpression(scope, indent, output);
		}
		
		if(CharOperation.endsWith(type.getLastToken(), head)) {
			output.append('>');
			
			for(HtmlElement child : this.children){
				child.generateStaticHTML(scope, indent + 1, output);
			}
			//output meta
			output.append("<script type = 'text/javascript' src = '/lark/js/stub.js'> </script>");
			output.append("\n");
			printIndent(indent, output);
			output.append("<script type = 'text/javascript' src = '/lark/java/lang/buildins.js'> </script>");
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
		} else {
			output.append('>');
			for(HtmlElement child : this.children){
				child.generateStaticHTML(scope, indent + 1, output);
			}
		}
		output.append("\n");
		printIndent(indent, output);
		output.append("</").append(type.getLastToken()).append('>');
		return output;
	}

}
