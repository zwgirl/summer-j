package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public class ObjectElement extends XAMLElement {
	public ObjectElement() {
		super();
	}

	public Attribute name;

	@Override
	protected void printTagName(int indent, StringBuffer output) {
		output.append(type.getLastToken());
	}

	@Override
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {
		// TODO Auto-generated method stub
	}

	@Override
	public void resolve(BlockScope scope) {

	}
	
	@Override
	public void resolve(ClassScope scope) {
		// TODO Auto-generated method stub
		super.resolve(scope);

	}

	private static final char[] body = "body".toCharArray();
	private static final char[] head = "head".toCharArray();
	public StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		output.append('<').append(type.getLastToken());
		for(Attribute attr : this.attributes){
			output.append(" ");
			attr.generateExpression(scope, indent, output);
		}
		output.append('>');
		
		if(CharOperation.endsWith(type.getLastToken(), body)) {
			output.append("\n");
			printIndent(indent, output);
			output.append("<script type = 'text/javascript' >");
			output.append("\n");
			printIndent(indent, output);
			output.append("var _c = document.createElement.bind(document);");
			
			output.append("\n");
			printIndent(indent, output);
			output.append("var _a = Node.prototype.appendChild;");
			
			buildDOMScript(scope, indent + 1, output, "document.body");
			output.append("</script>");
		} else if(CharOperation.endsWith(type.getLastToken(), head)) {
			output.append("<script type = 'text/javascript' src = 'js/stub.js'> </script>");
			//build class definition
			ClassScope classScope = scope.enclosingClassScope();
			TypeDeclaration typeDecl = classScope.referenceContext;
			output.append("\n");
			printIndent(indent, output);
			output.append("<script type = 'text/javascript' >");
			output.append("\n");
			printIndent(indent+1, output);
			output.append("var __this = new (");
			typeDecl.generatea(classScope, indent + 1, output);
			output.append(")();");
			
			output.append("\n");
			printIndent(indent, output);
			output.append("</script>");
		} else {
			for(XAMLElement child : this.children){
				output.append("\n");
				child.generateStatement(scope, indent + 1, output);
			}
		}
		output.append("\n");
		printIndent(indent, output);
		output.append("</").append(type.getLastToken()).append('>');
		return output;
	}
	
	protected StringBuffer buildDOMScript(Scope scope, int indent, StringBuffer output, String parent){
		//
		output.append("\n");
		printIndent(indent, output);
		output.append("(function(_p){");
		
		for(XAMLElement child : this.children){
			if(child instanceof Script){
				continue;
			}
			
			output.append("\n");
			printIndent(indent + 1, output);
			
			if(child instanceof PCDATA ){
				output.append("_p.appendChild(document.createTextNode(");
				PCDATA pcdata = (PCDATA)child;
				String data = new String(pcdata.source);
				data = data.replace("\r", "\\r").replace("\n", "\\n");
				output.append("\"").append(data).append("\"));");
			} else {
				ObjectElement oe = (ObjectElement) child;
				if(oe.name != null){
					output.append("var _n = __this[");
					oe.name.value.doGenerateExpression(scope, indent, output);
					output.append("]");
				} else {
					output.append("var _n"); 
				}
				
				output.append(" = _c(\"").append(child.type.getLastToken()).append("\");");
				
				for(Attribute attr : this.attributes){
					output.append("\n");
					printIndent(indent + 1, output);
					output.append("_n.").append(attr.property.token).append(" = ");
					attr.value.generateExpression(scope, indent, output).append(";");
				}
				if(child.children != null && child.children.length > 0){
					child.buildDOMScript(scope, indent + 1, output, "_n");
				}
				output.append('\n');
				printIndent(indent + 1, output).append("_p.appendChild(_n);");
			}
		}
			
		output.append("\n");
		printIndent(indent, output);
		output.append("})(").append(parent).append(");");
		return output;
	}

}
