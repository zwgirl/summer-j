package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.html.Js2HtmlMapping;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.Binding;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.FieldBinding;
import org.summer.sdt.internal.compiler.lookup.InferenceContext18;
import org.summer.sdt.internal.compiler.lookup.MissingTypeBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemFieldBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemReasons;
import org.summer.sdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TagBits;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.VariableBinding;

/**
 * 
 * @author cym
 *
 */
public class HtmlPropertyReference extends Expression{
	public char[][] tokens;
	public long[] positions;
	public HtmlPropertyReference receiver;
	public char[] prefix;   //namespace
	public long prefixPos;
	
	public FieldBinding binding;
	public ReferenceBinding receiverType;// exact binding resulting from lookup

	public HtmlPropertyReference(char[][] source, long[] pos, HtmlPropertyReference receiver) {
		this.tokens = source;
		this.positions = pos;
		this.receiver = receiver;
		
		this.bits |= Binding.FIELD;
	}
	
	public HtmlPropertyReference(char[][] source, long[] pos) {
		this(source, pos, null);
	}
	
	public HtmlPropertyReference(char[][] source, long[] postions, char[] prefix, long prefixPos) {
		this.tokens = source;
		this.positions = postions;
		
		this.prefix = prefix;
		this.prefixPos = prefixPos;
		
		this.bits |= Binding.FIELD;
	}
	
	public char[] name(){
		return CharOperation.concatWith(this.tokens, '-');
	}
	
	public char[] nameWithNS(){
		if(prefix == null){
			return name();
		}
		
		return CharOperation.concatWith(new char[][]{prefix, name()}, ':');
	}
	
	/**
	 * Field reference code generation
	 *
	 * @param currentScope org.summer.sdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.summer.sdt.internal.compiler.codegen.CodeStream
	 * @param valueRequired boolean
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
		
	}
	
	/**
	 * @see org.summer.sdt.internal.compiler.lookup.InvocationSite#genericTypeArguments()
	 */
	public TypeBinding[] genericTypeArguments() {
		return null;
	}
	
	public InferenceContext18 freshInferenceContext(Scope scope) {
		return null;
	}
	
	public FieldBinding lastFieldBinding() {
		return this.binding;
	}
	
	public Constant optimizedBooleanConstant() {
		switch (this.resolvedType.id) {
			case T_boolean :
			case T_JavaLangBoolean :
				return this.constant != Constant.NotAConstant ? this.constant : this.binding.constant();
			default :
				return Constant.NotAConstant;
		}
	}
	
	public StringBuffer printExpression(int indent, StringBuffer output) {
		return output.append('.').append(CharOperation.concatWith(this.tokens, '-'));
	}
	
	public TypeBinding resolveType(BlockScope scope){
		return interalResolveType(scope);
		
	}
	
	private TypeBinding interalResolveType(BlockScope scope) {
		//always ignore receiver cast, since may affect constant pool reference
		if(this.receiver == null){
			this.receiverType = (ReferenceBinding) scope.element.resolvedType;
		} else {
			this.receiverType = (ReferenceBinding) this.receiver.resolveType(scope);
			if (this.receiverType == null) {
				this.constant = Constant.NotAConstant;
			}
		}
		
		if(this.receiverType == null){
			return null;
		}
		
		// the case receiverType.isArrayType and token = 'length' is handled by the scope API
		FieldBinding fieldBinding = this.binding = scope.getField(this.receiverType, this.name(), null);
		
		//cym 2012-02-12
		if(fieldBinding.isValidBinding() && (fieldBinding.modifiers & ClassFileConstants.AccProperty) == 0){
			scope.problemReporter().invalidProperty(this, this.receiverType);   //TODO need more clear  //cym 2015-02-27
		}
		if (!fieldBinding.isValidBinding()) {
			this.constant = Constant.NotAConstant;
			if (this.receiverType instanceof ProblemReferenceBinding) {
				// problem already got signaled on receiver, do not report secondary problem
				return null;
			}
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=245007 avoid secondary errors in case of
			// missing super type for anonymous classes ... 
			ReferenceBinding declaringClass = fieldBinding.declaringClass;
			boolean avoidSecondary = declaringClass != null &&
									 declaringClass.isAnonymousType() &&
									 declaringClass.superclass() instanceof MissingTypeBinding;
			
			//cym add 2015-04-02
			if (fieldBinding instanceof ProblemFieldBinding && fieldBinding.problemId() == ProblemReasons.NotFound) {
				scope.problemReporter().noPropertyDefineInElement(this);
				return scope.getJavaLangString();
			}
			//cym add 2015-04-02
		
			if (!avoidSecondary) {
				scope.problemReporter().invalidProperty(this, this.receiverType);
			}
			if (fieldBinding instanceof ProblemFieldBinding) {
				ProblemFieldBinding problemFieldBinding = (ProblemFieldBinding) fieldBinding;
				FieldBinding closestMatch = problemFieldBinding.closestMatch;
				switch(problemFieldBinding.problemId()) {
					case ProblemReasons.InheritedNameHidesEnclosingName :
					case ProblemReasons.NotVisible :
					case ProblemReasons.NonStaticReferenceInConstructorInvocation :
					case ProblemReasons.NonStaticReferenceInStaticContext :
						if (closestMatch != null) {
							fieldBinding = closestMatch;
						}
				}
			}
			if (!fieldBinding.isValidBinding()) {
				return null;
			}
		}
		// handle indirect inheritance thru variable secondary bound
		// receiver may receive generic cast, as part of implicit conversion
		TypeBinding oldReceiverType = this.receiverType;
		this.receiverType = (ReferenceBinding) this.receiverType.getErasureCompatibleType(fieldBinding.declaringClass);
//		this.receiver.computeConversion(scope, this.actualReceiverType, this.actualReceiverType);
//		if (TypeBinding.notEquals(this.actualReceiverType, oldReceiverType) && TypeBinding.notEquals(this.receiver.postConversionType(scope), this.actualReceiverType)) { // record need for explicit cast at codegen since receiver could not handle it
//			this.bits |= NeedReceiverGenericCast;
//		}
		if (isFieldUseDeprecated(fieldBinding, scope, this.bits)) {
			scope.problemReporter().deprecatedField(fieldBinding, this);
		}
		this.constant = Constant.NotAConstant;
		if (fieldBinding.isStatic()) {
			scope.problemReporter().indirectAccessToStaticField(this, fieldBinding);
		}
		TypeBinding fieldType = fieldBinding.type;
		if (fieldType != null) {
			if ((this.bits & ASTNode.IsStrictlyAssigned) == 0) {
				fieldType = fieldType.capture(scope, this.sourceStart, this.sourceEnd);	// perform capture conversion if read access
			}
			this.resolvedType = fieldType;
			if ((fieldType.tagBits & TagBits.HasMissingType) != 0) {
				scope.problemReporter().invalidType(this, fieldType);
				return null;
			}
		}
		return fieldType;
	}
	
	public void setDepth(int depth) {
		this.bits &= ~ASTNode.DepthMASK; // flush previous depth if any
		if (depth > 0) {
			this.bits |= (depth & 0xFF) << ASTNode.DepthSHIFT; // encoded on 8 bits
		}
	}
	
	public void traverse(ASTVisitor visitor, BlockScope scope) {
		if (visitor.visit(this, scope)) {
			this.receiver.traverse(visitor, scope);
		}
		visitor.endVisit(this, scope);
	}
	
	public VariableBinding nullAnnotatedVariableBinding(boolean supportTypeAnnotations) {
		if (this.binding != null) {
			if (supportTypeAnnotations
					|| ((this.binding.tagBits & TagBits.AnnotationNullMASK) != 0)) {
				return this.binding;
			}
		}
		return null;
	}

	@Override
	protected StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		if(this.receiver != null){
			output.append(this.receiver.name());
			output.append('.');
		}
		output.append(this.name());
		return output;
	}
	
	public StringBuffer html(Scope scope, int indent, StringBuffer output){
		if(this.receiver != null){
			Js2HtmlMapping.getHtmlName(new String(this.receiver.name()));
			output.append(Js2HtmlMapping.getHtmlName(new String(this.receiver.name())));
			output.append('.');
		}
		output.append(Js2HtmlMapping.getHtmlName(new String(this.name())));
		return output;
	}
	
	public StringBuffer buildInjectPart(Scope scope, int indent, StringBuffer output){
		output.append("[\"");
		if(this.receiver != null){
			output.append(this.receiver.name()).append("\"");
			output.append(", \"");
		}
		output.append(this.name()).append("\"");
		output.append("]");
		
		return output;
	}
}
