/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Stephan Herrmann - Contribution for
 *								Bug 392238 - [1.8][compiler][null] Detect semantically invalid null type annotations
 *								Bug 429958 - [1.8][null] evaluate new DefaultLocation attribute of @NonNullByDefault
 *******************************************************************************/
package org.summer.sdt.internal.compiler.ast;

import java.util.Stack;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.html.HtmlTags;
import org.summer.sdt.internal.compiler.html.SvgTags;
import org.summer.sdt.internal.compiler.impl.CompilerOptions;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.LocalTypeBinding;
import org.summer.sdt.internal.compiler.lookup.MethodScope;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.SourceTypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeIds;
import org.summer.sdt.internal.compiler.lookup.TypeVariableBinding;
import org.summer.sdt.internal.compiler.problem.ProblemSeverities;

public class HtmlTypeReference extends TypeReference {

	public char[][] tokens;
	public long[] positions;

	public HtmlTypeReference(char[][] source, long[] positions) {

			this.tokens = source;
			this.positions = positions;
//			this.sourceStart = (int) (pos>>>32)  ;
//			this.sourceEnd = (int) (pos & 0x00000000FFFFFFFFL) ;

	}

	public TypeReference augmentTypeWithAdditionalDimensions(int additionalDimensions, Annotation[][] additionalAnnotations, boolean isVarargs) {
//		int totalDimensions = this.dimensions() + additionalDimensions;
//		Annotation [][] allAnnotations = getMergedAnnotationsOnDimensions(additionalDimensions, additionalAnnotations);
//		ArrayTypeReference arrayTypeReference = new ArrayTypeReference(this.token, totalDimensions, allAnnotations, (((long) this.sourceStart) << 32) + this.sourceEnd);
//		arrayTypeReference.annotations = this.annotations;
//		arrayTypeReference.bits |= (this.bits & ASTNode.HasTypeAnnotations);
//		if (!isVarargs)
//			arrayTypeReference.extendedDimensions = additionalDimensions;
//		return arrayTypeReference;
		
		return this;
	}

	public char[] getLastToken() {
		return CharOperation.concatWith(this.tokens, '-');
	}
	protected TypeBinding getTypeBinding(Scope scope) {
		if (this.resolvedType != null)
			return this.resolvedType;

//		this.resolvedType = scope.getType(this.token);
		if(!(scope instanceof BlockScope)){
			return null;
		}
		if(((BlockScope)scope).inSVG){
			this.resolvedType = scope.getType(SvgTags.getMappingClass(CharOperation.concatWith(this.tokens, '-')));
		} else {
			this.resolvedType = scope.getType(HtmlTags.getMappingClass(CharOperation.concatWith(this.tokens, '-')));
		}
		
		if (this.resolvedType instanceof TypeVariableBinding) {
			TypeVariableBinding typeVariable = (TypeVariableBinding) this.resolvedType;
			if (typeVariable.declaringElement instanceof SourceTypeBinding) {
				scope.tagAsAccessingEnclosingInstanceStateOf((ReferenceBinding) typeVariable.declaringElement, true /* type variable access */);
			}
		} else if (this.resolvedType instanceof LocalTypeBinding) {
			LocalTypeBinding localType = (LocalTypeBinding) this.resolvedType;
			MethodScope methodScope = scope.methodScope();
			if (methodScope != null && !methodScope.isStatic) {
				methodScope.tagAsAccessingEnclosingInstanceStateOf(localType, false /* ! type variable access */);
			}
		}

		if (scope.kind == Scope.CLASS_SCOPE && this.resolvedType.isValidBinding())
			if (((ClassScope) scope).detectHierarchyCycle(this.resolvedType, this))
				return null;
		return this.resolvedType;
	}

	public char [][] getTypeName() {
//		return new char[][] { this.token };
		return this.tokens;
	}

	@Override
	public boolean isBaseTypeReference() {
		if(this.tokens.length>1){
			return false;
		}
		return this.tokens[0] == BYTE    ||
			   this.tokens[0] == SHORT   ||
			   this.tokens[0] == INT     ||
			   this.tokens[0] == LONG    ||
			   this.tokens[0] == FLOAT   ||
			   this.tokens[0] == DOUBLE  ||
			   this.tokens[0] == CHAR    ||
			   this.tokens[0] == BOOLEAN ||
			   this.tokens[0] == NULL    ||
			   this.tokens[0] == VOID;	    
	}
	
	public char[] name(){
		return CharOperation.concatWith(tokens, '-');
	}
	
	public StringBuffer printExpression(int indent, StringBuffer output){
		if (this.annotations != null && this.annotations[0] != null) {
			printAnnotations(this.annotations[0], output);
			output.append(' ');
		}
//		return output.append(this.token);
		return output.append(CharOperation.concatWith(this.tokens, '-'));
	}

	public TypeBinding resolveTypeEnclosing(BlockScope scope, ReferenceBinding enclosingType) {
//		this.resolvedType = scope.getMemberType(this.token, enclosingType);
//		boolean hasError = false;
//		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=391500
//		resolveAnnotations(scope, 0); // defaultNullness not relevant, the only caller within the compiler: QAE
//		TypeBinding memberType = this.resolvedType; // load after possible update in resolveAnnotations()
//		if (!memberType.isValidBinding()) {
//			hasError = true;
//			scope.problemReporter().invalidEnclosingType(this, memberType, enclosingType);
//			memberType = ((ReferenceBinding)memberType).closestMatch();
//			if (memberType == null) {
				return null;
//			}
//		}
//		if (isTypeUseDeprecated(memberType, scope))
//			reportDeprecatedType(memberType, scope);
//		memberType = scope.environment().convertToRawType(memberType, false /*do not force conversion of enclosing types*/);
//		if (memberType.isRawType()
//				&& (this.bits & IgnoreRawTypeCheck) == 0
//				&& scope.compilerOptions().getSeverity(CompilerOptions.RawTypeReference) != ProblemSeverities.Ignore){
//			scope.problemReporter().rawTypeReference(this, memberType);
//		}
//		if (hasError) {
//			// do not store the computed type, keep the problem type instead
//			return memberType;
//		}
//		return this.resolvedType = memberType;
	}

	public void traverse(ASTVisitor visitor, BlockScope scope) {
//		if (visitor.visit(this, scope)) {
//			if (this.annotations != null) {
//				Annotation [] typeAnnotations = this.annotations[0];
//				for (int i = 0, length = typeAnnotations == null ? 0 : typeAnnotations.length; i < length; i++)
//					typeAnnotations[i].traverse(visitor, scope);
//			}
//		}
//		visitor.endVisit(this, scope);
	}

	public void traverse(ASTVisitor visitor, ClassScope scope) {
//		if (visitor.visit(this, scope)) {
//			if (this.annotations != null) {
//				Annotation [] typeAnnotations = this.annotations[0];
//				for (int i = 0, length = typeAnnotations == null ? 0 : typeAnnotations.length; i < length; i++)
//					typeAnnotations[i].traverse(visitor, scope);
//			}
//		}
//		visitor.endVisit(this, scope);
	}

	public StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output){
		output.append(CharOperation.concatWith(this.tokens, '-'));
		return output;
	}
}
