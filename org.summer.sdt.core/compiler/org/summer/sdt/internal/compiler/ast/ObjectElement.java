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
		super(null);
	}

	public Attribute name;
	public AllocationExpression allocation;

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
		if (allocation != null) {
			allocation.resolve(scope);
		}

		if (this.initializer != null) {
			this.initializer.resolve(initializeScope);
		}
	}
	
//	private void buildAllocation(){
////		if(this.bits | ASTNode.){
////			
////		}
//		//ObjectElement ::= ElementTag AttributeListopt '>' 
//		//	 ElementListopt
//		//'</' SimpleName '>'
//	
//		//copy element child
////		XAMLElement[] elements = new XAMLElement[0];
////		int length;
////		if ((length = this.elementLengthStack[this.elementLengthPtr--]) != 0) {
////			this.elementPtr -= length;
////			System.arraycopy(this.elementStack, this.elementPtr + 1, elements = new XAMLElement[length], 0, length);
////		}
////		
////		ObjectElement element = (ObjectElement) this.elementStack[this.elementPtr];
////		element.children = elements;
////		
////		length = this.attributeLengthStack[this.attributeLengthPtr--];
////		if(length != 0){
////			this.attributePtr = this.attributePtr - length;
////			System.arraycopy(this.attributeStack, this.attributePtr +1 , element.attributes = new Attribute[length], 0, length);
////		}
////		
////		element.statementEnd = this.scanner.currentPosition - 1;
////		element.sourceEnd = this.scanner.currentPosition - 1;
//		
//		//annonymous class
//		TypeDeclaration anonymousType = new TypeDeclaration(this.compilationUnit.compilationResult);
//		anonymousType.name = CharOperation.NO_CHAR;
//		anonymousType.bits |= (ASTNode.IsAnonymousType|ASTNode.IsLocalType);
//		anonymousType.bits |= (this.type.bits & ASTNode.HasTypeAnnotations);
//		QualifiedAllocationExpression allocation = new QualifiedAllocationExpression(anonymousType);
//		markEnclosingMemberWithLocalType();
//		
//		allocation.type = this.type;
//	
//		anonymousType.sourceEnd = allocation.sourceEnd;
//		//position at the type while it impacts the anonymous declaration
//		anonymousType.sourceStart = anonymousType.declarationSourceStart = allocation.type.sourceStart;
//		allocation.sourceStart = this.sourceStart;
//	
//		anonymousType.bodyStart = this.sourceEnd;
//		
//		//create block
//		int attrLength = this.attributes.length;
//		int eleLength = this.children.length;
//		Block block = new Block(0, false);
//		block.statements = new Statement[attrLength + eleLength];
//		if(attrLength > 0){
//			System.arraycopy(this.attributes, 0, block.statements, 0, attrLength);
//		}
//		if(eleLength > 0){
//			System.arraycopy(this.children, 0, block.statements, attrLength, eleLength);
//		}
//		//create Initializer
//		Initializer init = new Initializer(block, 0);
//		init.sourceStart = this.sourceStart;
//		init.sourceEnd = this.sourceEnd;
//		
//		init.bodyStart = block.sourceStart = init.sourceStart;
//		init.bodyEnd = block.sourceEnd = init.sourceEnd;
//		anonymousType.fields = new FieldDeclaration[]{ init };
//		
//		this.allocation = allocation;
//	}

	@Override
	public void resolve(ClassScope scope) {
		// TODO Auto-generated method stub
		super.resolve(scope);

		this.initializer.resolve(initializeScope);
	}

	@Override
	protected void resolveAttribute(ClassScope scope) {
		// TODO Auto-generated method stub
		super.resolveAttribute(scope);
	}

	public StringBuffer doGenerateExpression(Scope scope, Dependency depsManager, int tab,
			StringBuffer output) {
		if (allocation != null) {
			return allocation.doGenerateExpression(scope, depsManager, tab, output);
		}
		return output;
	}

}
