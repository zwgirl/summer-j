/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.codegen.BranchLabel;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.impl.BooleanConstant;
import org.summer.sdt.internal.compiler.javascript.Javascript;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;

public class TrueLiteral extends MagicLiteral {
	
	static final char[] source = {'t' , 'r' , 'u' , 'e'};
	
	public TrueLiteral(int s , int e) {
		super(s,e);
	}
	public void computeConstant() {
		this.constant = BooleanConstant.fromValue(true);
	}
	/**
	 * Code generation for the true literal
	 *
	 * @param currentScope org.summer.sdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.summer.sdt.internal.compiler.codegen.CodeStream
	 * @param valueRequired boolean
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
		int pc = codeStream.position;
		if (valueRequired) {
			codeStream.generateConstant(this.constant, this.implicitConversion);
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}
	public void generateOptimizedBoolean(BlockScope currentScope, CodeStream codeStream, BranchLabel trueLabel, BranchLabel falseLabel, boolean valueRequired) {
	
		// trueLabel being not nil means that we will not fall through into the TRUE case
	
		int pc = codeStream.position;
		// constant == true
		if (valueRequired) {
			if (falseLabel == null) {
				// implicit falling through the FALSE case
				if (trueLabel != null) {
					codeStream.goto_(trueLabel);
				}
			}
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}
	public TypeBinding literalType(BlockScope scope) {
		return TypeBinding.BOOLEAN;
	}
	/**
	 *
	 */
	public char[] source() {
		return source;
	}
	public void traverse(ASTVisitor visitor, BlockScope scope) {
		visitor.visit(this, scope);
		visitor.endVisit(this, scope);
	}
	
	@Override
	public void generateJavascript(Scope scope, int indent, StringBuffer buffer) {
		buffer.append(Javascript.TRUE);
	}
}
