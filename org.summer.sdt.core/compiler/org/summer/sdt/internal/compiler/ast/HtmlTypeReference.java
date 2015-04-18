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
import org.summer.sdt.internal.compiler.html.LarkTags;
import org.summer.sdt.internal.compiler.html.SvgTags;
import org.summer.sdt.internal.compiler.impl.CompilerOptions;
import org.summer.sdt.internal.compiler.lookup.Binding;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.LocalTypeBinding;
import org.summer.sdt.internal.compiler.lookup.LookupEnvironment;
import org.summer.sdt.internal.compiler.lookup.MethodScope;
import org.summer.sdt.internal.compiler.lookup.PackageBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemReasons;
import org.summer.sdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.SourceTypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeIds;
import org.summer.sdt.internal.compiler.lookup.TypeVariableBinding;
import org.summer.sdt.internal.compiler.problem.AbortCompilation;
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

		if(!(scope instanceof BlockScope)){
			return null;
		}
		
		char[] selector = CharOperation.concatWith(this.tokens, '-');
		
		char[][] compoundName;
		if(((BlockScope)scope).inSVG){
			compoundName = SvgTags.getMappingClass(selector);
		} else {
			compoundName = HtmlTags.getMappingClass(selector);
			if(compoundName == null){
				compoundName = LarkTags.getMappingClass(selector);
			}
		} 
		
		if(compoundName == null){
			return new ProblemReferenceBinding(new char[][]{getLastToken()}, scope.environment().createMissingType(null, new char[][]{selector}), ProblemReasons.NotFound);
		}
		
		Binding binding = scope.getOnlyPackage(CharOperation.subarray(compoundName, 0, compoundName.length-1));
		if (binding != null && !binding.isValidBinding()) {
			if (binding instanceof ProblemReferenceBinding && binding.problemId() == ProblemReasons.NotFound) {
				ProblemReferenceBinding problemBinding = (ProblemReferenceBinding) binding;
				return new ProblemReferenceBinding(problemBinding.compoundName, scope.environment().createMissingType(null, compoundName), ProblemReasons.NotFound);
			}
			return (ReferenceBinding) binding; // not found
		}
	    PackageBinding packageBinding = binding == null ? null : (PackageBinding) binding;
	    this.resolvedType = scope.getType(compoundName[compoundName.length-1], packageBinding);
		return this.resolvedType;
	}
	
	protected TypeBinding findNextTypeBinding(int tokenIndex, Scope scope, PackageBinding packageBinding) {
		LookupEnvironment env = scope.environment();
		try {
			env.missingClassFileLocation = this;
			if (this.resolvedType == null) {
				this.resolvedType = scope.getType(this.tokens[tokenIndex], packageBinding);
			} else {
				this.resolvedType = scope.getMemberType(this.tokens[tokenIndex], (ReferenceBinding) this.resolvedType);
				if (!this.resolvedType.isValidBinding()) {
					this.resolvedType = new ProblemReferenceBinding(
						CharOperation.subarray(this.tokens, 0, tokenIndex + 1),
						(ReferenceBinding)this.resolvedType.closestMatch(),
						this.resolvedType.problemId());
				}
			}
			return this.resolvedType;
		} catch (AbortCompilation e) {
			e.updateContext(this, scope.referenceCompilationUnit().compilationResult);
			throw e;
		} finally {
			env.missingClassFileLocation = null;
		}
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
