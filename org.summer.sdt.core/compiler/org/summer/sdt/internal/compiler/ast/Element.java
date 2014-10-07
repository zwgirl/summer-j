package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.flow.FlowContext;
import org.summer.sdt.internal.compiler.flow.FlowInfo;
import org.summer.sdt.internal.compiler.impl.CompilerOptions;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.ArrayBinding;
import org.summer.sdt.internal.compiler.lookup.Binding;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.ElementScope;
import org.summer.sdt.internal.compiler.lookup.LocalVariableBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemReasons;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.SourceTypeBinding;
import org.summer.sdt.internal.compiler.lookup.TagBits;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeVariableBinding;
import org.summer.sdt.internal.compiler.lookup.VariableBinding;
import org.summer.sdt.internal.compiler.problem.ProblemSeverities;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public abstract class Element extends NameReference {
	public int closeMode;
	public static final int CLOSE_TAG = 1;   // "/>"
	public static final int CLOSE_ELEMENT = 2;		 // "</"
	public static final char[][] noName = new char[0][0];
	
	public static final Attribute[] noAttribute = new Attribute[0];
	
	public TypeReference type;
	public Attribute[] attributes = noAttribute;
	public Attribute name;   //named attribute
	public static final Element[] noChild = new Element[0];
	public Element[] children = noChild;
	public int declarationSourceStart;
	public int declarationSourceEnd;
	public int bodyStart;
	public int bodyEnd; // doesn't include the trailing comment if any.
	public ElementScope scope;

	public StringBuffer print(int indent, StringBuffer output) {
		return printStatement(indent, output);
	}
	
	@Override
	public StringBuffer printStatement(int indent, StringBuffer output) {
		printIndent(indent, output);
		
		output.append("<");
		printTagName(indent, output);
		printProperties(indent, output);
		
		if(this.closeMode == CLOSE_TAG){
			return output.append("/>");
		} else {
			output.append(">");
		}
		
		printChildElement(indent, output);
		
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
	
	protected abstract void printTagName(int indent, StringBuffer output);
	
	protected void printProperties(int indent, StringBuffer output){
		for(Attribute attribute : attributes){
			output.append(" ");
			attribute.print(indent, output);
		}
	}
	
	public StringBuffer printChildElement(int indent, StringBuffer output) {
		for(Element element : children){
			output.append('\n');
			element.print(indent + 1, output);
		}
		return output;
	}
	
	
	/**
	 * Resolve the type of this expression in the context of a blockScope
	 *
	 * @param scope
	 * @return
	 * 	Return the actual type of this expression after resolution
	 */
	public TypeBinding resolveType(ClassScope scope) {
		if (this.resolvedType != null)
			return this.resolvedType;

		this.resolvedType = scope.getType(this.type.getLastToken());
		
		if (this.resolvedType instanceof TypeVariableBinding) {
			TypeVariableBinding typeVariable = (TypeVariableBinding) this.resolvedType;
			if (typeVariable.declaringElement instanceof SourceTypeBinding) {
				scope.tagAsAccessingEnclosingInstanceStateOf((ReferenceBinding) typeVariable.declaringElement, true /* type variable access */);
			}
		}

		if (scope.kind == Scope.CLASS_SCOPE && this.resolvedType.isValidBinding())
			if (((ClassScope) scope).detectHierarchyCycle(this.resolvedType, this.type))
				return null;
		
		boolean hasError = false;
		if (resolvedType == null) {
			return null; // detected cycle while resolving hierarchy
		} else if ((hasError = !resolvedType.isValidBinding()) == true) {
			reportInvalidType(scope);
			switch (resolvedType.problemId()) {
				case ProblemReasons.NotFound :
				case ProblemReasons.NotVisible :
				case ProblemReasons.InheritedNameHidesEnclosingName :
					resolvedType = resolvedType.closestMatch();
					if (type == null) return null;
					break;
				default :
					return null;
			}
		}
		if (resolvedType.isArrayType() && ((ArrayBinding) resolvedType).leafComponentType == TypeBinding.VOID) {
			scope.problemReporter().cannotAllocateVoidArray(this);
			return null;
		}
//		if (!(this instanceof QualifiedTypeReference)   // QualifiedTypeReference#getTypeBinding called above will have already checked deprecation
//				&& isTypeUseDeprecated(type, scope)) {
//			reportDeprecatedType(type, scope);
//		}
		resolvedType = scope.environment().convertToRawType(resolvedType, false /*do not force conversion of enclosing types*/);
		if (resolvedType.leafComponentType().isRawType()
				&& (this.bits & ASTNode.IgnoreRawTypeCheck) == 0
				&& scope.compilerOptions().getSeverity(CompilerOptions.RawTypeReference) != ProblemSeverities.Ignore) {
			scope.problemReporter().rawTypeReference(this, resolvedType);
		}
		return resolvedType;
	}
	
	protected void reportDeprecatedType(TypeBinding type, Scope scope, int index) {
		scope.problemReporter().deprecatedType(type, this, index);
	}
	
	protected void reportDeprecatedType(TypeBinding type, Scope scope) {
		scope.problemReporter().deprecatedType(type, this, Integer.MAX_VALUE);
	}
	
	protected void reportInvalidType(Scope scope) {
		scope.problemReporter().invalidType(this, this.resolvedType);
	}

	public void resolve(ClassScope scope) {
		this.constant = Constant.NotAConstant;
		this.resolvedType = type.resolveType(scope);
//		resolveType(scope);
		resolveAttribute(scope);
		resolveChild(scope);
	}
	
	public TypeBinding resolveType(BlockScope scope) {
		this.constant = Constant.NotAConstant;
		if(this.actualReceiverType != null){
			return this.actualReceiverType;
		}
		return this.actualReceiverType = this.resolvedType; //scope.enclosingSourceType();
	}
	
	protected void resolveChild(ClassScope scope){
		for(Element child: children){
			child.resolve(scope);
		}
	}
	
	protected void resolveAttribute(ClassScope scope){
		for(Attribute attr : attributes){
			attr.resolve(new ElementScope(Scope.BLOCK_SCOPE, scope));
		}
	}

	@Override
	public TypeBinding[] genericTypeArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String unboundReferenceErrorName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[][] getName() {
		return noName;
	}

	@Override
	public FlowInfo analyseAssignment(BlockScope currentScope,
			FlowContext flowContext, FlowInfo flowInfo, Assignment assignment,
			boolean isCompound) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateAssignment(BlockScope currentScope,
			CodeStream codeStream, Assignment assignment, boolean valueRequired) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateCompoundAssignment(BlockScope currentScope,
			CodeStream codeStream, Expression expression, int operator,
			int assignmentImplicitConversion, boolean valueRequired) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generatePostIncrement(BlockScope currentScope,
			CodeStream codeStream, CompoundAssignment postIncrement,
			boolean valueRequired) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
