package org.summer.sdt.internal.compiler.ast;

import java.util.Stack;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.html.CssProperties;
import org.summer.sdt.internal.compiler.html.HtmlTagConstants;
import org.summer.sdt.internal.compiler.html.Namespace;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.Binding;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.FieldBinding;
import org.summer.sdt.internal.compiler.lookup.MissingTypeBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemFieldBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemReasons;
import org.summer.sdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TagBits;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;

/**
 * 
 * @author cym
 *
 */
public class HtmlPropertyReference extends Expression{
	public char[][] tokens;
	public long[] positions;
	public HtmlPropertyReference receiver;
	
	public FieldBinding binding;
	public ReferenceBinding receiverType;// exact binding resulting from lookup
	
	public long tagBits;

	public HtmlPropertyReference(char[][] source, long[] postions, HtmlPropertyReference receiver) {
		this.tokens = source;
		this.positions = postions;
		this.receiver = receiver;
		
		this.sourceStart = (int)this.positions[0] >>> 32;
		this.sourceEnd = (int)this.positions[this.positions.length-1];
		
		this.bits |= Binding.FIELD;
	}
	
	public HtmlPropertyReference(char[][] source, long[] postions) {
		this(source, postions, null);
	}
	
	public char[] hyphenName(){
		return CharOperation.concatWith(this.tokens, '-');
	}
	
	public char[] camelName(){
		return CssProperties.toCamel(CharOperation.concatWith(this.tokens, '-'));
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
		FieldBinding fieldBinding = this.binding = scope.getField(this.receiverType, this.camelName(), null);
		
		//cym 2015-02-12
		if(fieldBinding.isValidBinding() && (fieldBinding.modifiers & ClassFileConstants.AccProperty) == 0){
			scope.problemReporter().invalidProperty(this, this.receiverType);   //TODO need more clear  //cym 2015-02-27
		}
		
		//cym 2015-05-11
		if((fieldBinding.tagBits & TagBits.AnnotationAttribute) != 0){
			this.tagBits |= TagBits.AnnotationAttribute;
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

	public void traverse(ASTVisitor visitor, BlockScope scope) {
		if (visitor.visit(this, scope)) {
			this.receiver.traverse(visitor, scope);
		}
		visitor.endVisit(this, scope);
	}

	@Override
	protected StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		if(this.receiver != null){
			output.append(this.receiver.hyphenName());
			output.append('.');
		}
		output.append(this.hyphenName());
		return output;
	}
	
	public StringBuffer html(Scope scope, int indent, StringBuffer output){
		output.append(this.hyphenName());
		return output;
	}
	
	public StringBuffer buildInjectPart(Scope scope, int indent, StringBuffer output){
		output.append("[\""); 
		HtmlPropertyReference r = this.receiver;
		Stack<HtmlPropertyReference> s = new Stack<HtmlPropertyReference>();
		while(r != null){
			s.push(r);
			r = r.receiver;
		}
		
		while(!s.isEmpty()){
			output.append(s.pop().hyphenName()).append("\"");
			output.append(", \"");
		}
		
		output.append(this.hyphenName()).append("\"");
		output.append("]");
		
		return output;
	}

	public StringBuffer option(BlockScope scope, int indent, StringBuffer output) {
		if(this.receiver != null){
			output.append(this.receiver.hyphenName());
			output.append('.');
		}
		output.append(this.hyphenName());
		return output;
	}

	public boolean isDefaultXmlns() {
		return this.tokens.length == 1 && tokens[0][0] == 'x' &&
				CharOperation.equals(tokens[0], Namespace.XMLNS);
	}

	public boolean isName() {
		return this.tokens.length == 1 && tokens[0][0] == 'n' &&
				CharOperation.equals(tokens[0], HtmlTagConstants.NAME);
	}
}
