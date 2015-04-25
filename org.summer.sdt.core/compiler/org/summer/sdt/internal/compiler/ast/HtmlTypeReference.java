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

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.html.Namespace;
import org.summer.sdt.internal.compiler.lookup.Binding;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.LookupEnvironment;
import org.summer.sdt.internal.compiler.lookup.PackageBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemReasons;
import org.summer.sdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.problem.AbortCompilation;

public class HtmlTypeReference extends TypeReference {

	public char[][] tokens;
	public long[] positions;
	public char[][] actualTokens;

	public HtmlTypeReference(char[][] source, long[] positions) {

			this.tokens = source;
			this.positions = positions;
			this.sourceStart = (int) (positions[0]>>>32)  ;
			this.sourceEnd = (int) positions[positions.length-1];
	}

	public TypeReference augmentTypeWithAdditionalDimensions(int additionalDimensions, Annotation[][] additionalAnnotations, boolean isVarargs) {
		return this;
	}

	public char[] getLastToken() {
		return CharOperation.concatWith(this.tokens, '-');
	}
	
	private void caculateActualTokens(Scope scope){
		if(actualTokens == null){
			HtmlElement element = ((BlockScope)scope).element;
			char[] selector = CharOperation.concatWith(this.tokens, '-');
			
			char[] ns = element.namespace();
			if(ns == null){
				ns = Namespace.UNKNOWN;
			}
			this.actualTokens = Namespace.getMappingClass(ns, selector);
			if(this.actualTokens == null){
				this.actualTokens = new char[][] {selector};
			}
		}
	}
	
	protected TypeBinding getTypeBinding(Scope scope) {
		if (this.resolvedType != null)
			return this.resolvedType;
		if(!(scope instanceof BlockScope)){
			return null;
		}
		
		if(((BlockScope)scope).element == null){
			return null;
		}
		
		caculateActualTokens(scope);
		try{
		if(this.actualTokens.length == 1){
		    this.resolvedType = scope.getType(this.actualTokens[this.actualTokens.length-1]);
			return this.resolvedType;
		} else {
			Binding binding = scope.getOnlyPackage(CharOperation.subarray(this.actualTokens, 0, this.actualTokens.length-1));
			if (binding != null && !binding.isValidBinding()) {
				if (binding instanceof ProblemReferenceBinding && binding.problemId() == ProblemReasons.NotFound) {
					ProblemReferenceBinding problemBinding = (ProblemReferenceBinding) binding;
					return new ProblemReferenceBinding(problemBinding.compoundName, scope.environment().createMissingType(null, this.actualTokens), ProblemReasons.NotFound);
				}
				return (ReferenceBinding) binding; // not found
			}
		    PackageBinding packageBinding = binding == null ? null : (PackageBinding) binding;
		    this.resolvedType = scope.getType(this.actualTokens[this.actualTokens.length-1], packageBinding);
			return this.resolvedType;
		}
		}catch (ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
		return resolvedType;
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
		return output.append(CharOperation.concatWith(this.tokens, '-'));
	}

	public void traverse(ASTVisitor visitor, BlockScope scope) {
	}

	public void traverse(ASTVisitor visitor, ClassScope scope) {
	}

	public StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output){
		output.append(CharOperation.concatWith(this.tokens, '-'));
		return output;
	}
}
