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
	private static final char[] meta = "meta".toCharArray();
	public StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		output.append('<').append(type.getLastToken());
		Attribute template = null;
		for(Attribute attr : this.attributes){
			if((attr.bits & ASTNode.IsTemplate) != 0){
				template = attr;
				continue;
			}
			output.append(" ");
			attr.generateExpression(scope, indent, output);
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
			
			buildDOMScript(scope, indent + 1, output, "document.body");
			output.append("</script>");
		} else if(CharOperation.endsWith(type.getLastToken(), head)) {
			output.append('>');
			
			for(XAMLElement child : this.children){
				output.append("\n");
				child.generateStatement(scope, indent + 1, output);
			}
			//output meta
			output.append("<script type = 'text/javascript' src = 'js/stub.js'> </script>");
			output.append("\n");
			printIndent(indent, output);
			output.append("<script type = 'text/javascript' src = 'java/lang/buildins.js'> </script>");
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
	
	private static final char[] TEMPLATE = "template".toCharArray();
	private static final char[] ITEM_TEMPLATE = "itemTemplate".toCharArray();
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
				output.append("\"").append(pcdata.translateEntity(false)).append("\"));");
				
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
				Attribute attrHasTem = null;
				Attribute itemTemplate = null;
				for(Attribute attr : child.attributes){
					if((attr.bits & ASTNode.IsTemplate) != 0 ){
						if(CharOperation.equals(attr.property.token, TEMPLATE)){
							attrHasTem = attr;
						}

						if(CharOperation.equals(attr.property.token, ITEM_TEMPLATE)){
							itemTemplate = attr;
						}
						continue;
					}
					if(attr.value instanceof MarkupExtension){
						output.append("\n");
						printIndent(indent + 1, output);
//						output.append("__this.add(new (__lc('java.lang.BindingExpression'))(_n, null));");
						output.append("_n.").append(attr.property.token).append(" = ");
						ReferenceBinding rb = (ReferenceBinding) attr.value.resolvedType;
						output.append("new (__lc('").append(CharOperation.concatWith(rb.compoundName, '.')).append("', '").append(TypeDeclaration.getFileName(rb)).append("'))(");
						
						output.append("{");
						MarkupExtension markup = (MarkupExtension) attr.value;
						boolean comma = false;
						for(Attribute mAttr : markup.attributes){
							if(comma){
								output.append(',');
							}
							output.append("\"").append(mAttr.property.token).append("\" : ");
							if(mAttr.value instanceof StringLiteral){
								StringLiteral s = (StringLiteral) mAttr.value;
								output.append("\"").append(s.source).append("\"");
							}
//							output.append("\n");
//							printIndent(indent + 2, output);
							comma = true;
						}
						output.append("}");
						
						output.append(")");
						output.append(".providerValue(");
						output.append("_n, \"").append(attr.property.token).append("\")");
						output.append(";");
						continue;
					}
					
					if((attr.bits & TagBits.AnnotationEventCallback) != 0){
						if(attr.method == null){
							continue;
						}
						output.append("\n");
						printIndent(indent + 1, output);
						output.append("_n.").append(attr.property.token).append(" = ");
						if(attr.method.isStatic()){
							output.append(attr.method.declaringClass.name).append('.');
							if(attr.value instanceof StringLiteral){
								output.append(((StringLiteral)attr.value).source).append(".bind(").append(attr.method.declaringClass.name)
								.append("); ");
							}
						} else {
							output.append("__this.");
							if(attr.value instanceof StringLiteral){
								output.append(((StringLiteral)attr.value).source).append(".bind(__this); ");
							}
						}
						
						continue;
					}
					output.append("_n.").append(attr.property.token).append(" = ");
					output.append("\n");
					printIndent(indent + 1, output);
					attr.value.generateExpression(scope, indent, output).append(";");
				}
				if(attrHasTem != null){
					output.append("\n");
					printIndent(indent + 1, output);
					if(attrHasTem.template != null){
						output.append("var _t = new (__lc('");
						output.append(CharOperation.concatWith(attrHasTem.template.compoundName, '.'));
						output.append("'");
						if((attrHasTem.template.modifiers & ClassFileConstants.AccModule) != 0){
							output.append(", ").append(TypeDeclaration.getFileName(attrHasTem.template)).append(",");
						}
						output.append("))();");
						output.append("\n");
						printIndent(indent + 1, output);
						output.append("_t.create(_n);");
						output.append("\n");
						printIndent(indent + 1, output);
						output.append("_c.template = _t;");
					}
				}
				
				if(itemTemplate != null){
					for(int i = 0; i<10; i++){
						output.append("\n");
						printIndent(indent + 1, output);
						if(itemTemplate.template != null){
							output.append("var _t = new (__lc('");
							output.append(CharOperation.concatWith(itemTemplate.template.compoundName, '.'));
							output.append("'");
							if((itemTemplate.template.modifiers & ClassFileConstants.AccModule) != 0){
								output.append(", ").append(TypeDeclaration.getFileName(itemTemplate.template)).append(",");
							}
							output.append("))();");
							output.append("\n");
							printIndent(indent + 1, output);
							output.append("_t.create(_n);");
							output.append("\n");
							printIndent(indent + 1, output);
							output.append("_c.template = _t;");
						}
					}
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
	
	public void buildElement(Scope scope, int indent, StringBuffer output, String parent){
		output.append("\n");
		printIndent(indent + 1, output);
//		if(this.name != null){
//			output.append("var _n = __this[");
//			oe.name.value.doGenerateExpression(scope, indent, output);
//			output.append("]"); 
//		} else {
			output.append("var _n"); 
//		}
		
		output.append(" = _c(\"").append(this.type.getLastToken()).append("\");");
		Attribute attrHasTem = null;
		Attribute itemTemplate = null;
		for(Attribute attr : this.attributes){
			if((attr.bits & ASTNode.IsTemplate) != 0 ){
				if(CharOperation.equals(attr.property.token, TEMPLATE)){
					attrHasTem = attr;
				}

				if(CharOperation.equals(attr.property.token, ITEM_TEMPLATE)){
					itemTemplate = attr;
				}
				continue;
			}
			if(attr.value instanceof MarkupExtension){
				continue;
			}
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("_n.").append(attr.property.token).append(" = ");
			attr.value.generateExpression(scope, indent, output).append(";");
		}
		if(attrHasTem != null){
			output.append("\n");
			printIndent(indent + 1, output);
			if(attrHasTem.template != null){
				output.append("var _t = new (__lc('");
				output.append(CharOperation.concatWith(attrHasTem.template.compoundName, '.'));
				output.append("'");
				if((attrHasTem.template.modifiers & ClassFileConstants.AccModule) != 0){
					output.append(", ").append(TypeDeclaration.getFileName(attrHasTem.template)).append(",");
				}
				output.append("))();");
				output.append("\n");
				printIndent(indent + 1, output);
				output.append("_t.create(_n);");
				output.append("\n");
				printIndent(indent + 1, output);
				output.append("_c.template = _t;");
			}
		}
		
		if(itemTemplate != null){
			for(int i = 0; i<10; i++){
				output.append("\n");
				printIndent(indent + 1, output);
				if(itemTemplate.template != null){
					output.append("var _t = new (__lc('");
					output.append(CharOperation.concatWith(itemTemplate.template.compoundName, '.'));
					output.append("'");
					if((itemTemplate.template.modifiers & ClassFileConstants.AccModule) != 0){
						output.append(", ").append(TypeDeclaration.getFileName(itemTemplate.template)).append(",");
					}
					output.append("))();");
					output.append("\n");
					printIndent(indent + 1, output);
					output.append("_t.create(_n);");
					output.append("\n");
					printIndent(indent + 1, output);
					output.append("_c.template = _t;");
				}
			}
		}
		if(this.children != null && this.children.length > 0){
			this.buildDOMScript(scope, indent + 1, output, "_n");
		}
		output.append('\n');
		printIndent(indent + 1, output).append(parent).append(".appendChild(_n);");
	}
	
	public StringBuffer generateHTML(Scope scope, int indent, StringBuffer output) {
		output.append("\n");
		printIndent(indent, output);
		output.append('<').append(type.getLastToken());
		for(Attribute attr : this.attributes){
			output.append(" ");
			attr.generateExpression(scope, indent, output);
		}
		
		if(CharOperation.endsWith(type.getLastToken(), head)) {
			output.append('>');
			
			for(XAMLElement child : this.children){
				child.generateHTML(scope, indent + 1, output);
			}
			//output meta
			output.append("<script type = 'text/javascript' src = 'js/stub.js'> </script>");
			output.append("\n");
			printIndent(indent, output);
			output.append("<script type = 'text/javascript' src = 'java/lang/buildins.js'> </script>");
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
			for(XAMLElement child : this.children){
				child.generateHTML(scope, indent + 1, output);
			}
		}
		output.append("\n");
		printIndent(indent, output);
		output.append("</").append(type.getLastToken()).append('>');
		return output;
	}

}
