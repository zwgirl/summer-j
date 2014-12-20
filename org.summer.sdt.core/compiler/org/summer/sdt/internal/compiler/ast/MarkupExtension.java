package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.javascript.Dependency;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public class MarkupExtension extends XAMLElement {
	public TypeReference type;
	public AllocationExpression allocation;
	
	public MarkupExtension() {
		super(null);
	}

	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		return printExpression(indent, output);
	}
	
	protected void printProperties(int indent, StringBuffer output){
		for(Attribute attribute : this.attributes){
			output.append(" ");
			attribute.print(indent, output);
		}
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		output.append("{").append(type.getLastToken());
		printProperties(indent, output);
		return output.append(" }");
	}

	@Override
	public StringBuffer doGenerateExpression(Scope scope, Dependency depsManager, int indent,
			StringBuffer output) {
		if(allocation != null){
			return allocation.doGenerateExpression(scope, depsManager, indent, output);
		}
		return output;
	}

	@Override
	protected void printTagName(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resolve(BlockScope scope) {
		this.type.resolve(scope);
//		for(Attribute attr : this.attributes){
//			attr.resolve(scope);
//		}
		if(allocation != null){
			allocation.resolve(scope);
		}
	}

}
