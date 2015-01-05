package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.javascript.Dependency;
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

	public StringBuffer doGenerateExpression(Scope scope, Dependency dependency, int indent,
			StringBuffer output) {
		output.append('<').append(type.getLastToken());
		for(Attribute attr : this.attributes){
			output.append(" ");
			attr.generateExpression(scope, dependency, indent, output);
		}
		output.append('>');
			for(XAMLElement child : this.children){
				output.append("\n");
				child.generateStatement(scope, dependency, indent + 1, output);
			}
		output.append("\n");
		printIndent(indent, output);
		output.append("</").append(type.getLastToken()).append('>');
		return output;
	}
	
	protected StringBuffer generateDynamicScript(Scope scope, Dependency dependency, int indent, StringBuffer output){
		//
		return output;
		
	}

}
