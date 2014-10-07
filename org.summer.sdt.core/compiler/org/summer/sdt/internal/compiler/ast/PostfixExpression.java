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
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.javascript.Javascript;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

public class PostfixExpression extends CompoundAssignment {

	public PostfixExpression(Expression lhs, Expression expression, int operator, int pos) {
		super(lhs, expression, operator, pos);
		this.sourceStart = lhs.sourceStart;
		this.sourceEnd = pos;
	}
	public boolean checkCastCompatibility() {
		return false;
	}
	/**
	 * Code generation for PostfixExpression
	 *
	 * @param currentScope org.summer.sdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.summer.sdt.internal.compiler.codegen.CodeStream
	 * @param valueRequired boolean
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
		// various scenarii are possible, setting an array reference,
		// a field reference, a blank final field reference, a field of an enclosing instance or
		// just a local variable.
	
		int pc = codeStream.position;
		 ((Reference) this.lhs).generatePostIncrement(currentScope, codeStream, this, valueRequired);
		if (valueRequired) {
			codeStream.generateImplicitConversion(this.implicitConversion);
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}
	
	public String operatorToString() {
		switch (this.operator) {
			case PLUS :
				return "++"; //$NON-NLS-1$
			case MINUS :
				return "--"; //$NON-NLS-1$
		}
		return "unknown operator"; //$NON-NLS-1$
	}
	
	public StringBuffer printExpressionNoParenthesis(int indent, StringBuffer output) {
		return this.lhs.printExpression(indent, output).append(' ').append(operatorToString());
	}
	
	public boolean restrainUsageToNumericTypes() {
		return true;
	}
	
	public void traverse(ASTVisitor visitor, BlockScope scope) {
	
		if (visitor.visit(this, scope)) {
			this.lhs.traverse(visitor, scope);
		}
		visitor.endVisit(this, scope);
	}
	
	@Override
	public void generateJavascript(Scope scope, int indent, StringBuffer buffer) {
		lhs.generateJavascript(scope, indent, buffer);
		switch (this.operator) {
		case PLUS :
			buffer.append(Javascript.PLUS_PLUS);
			break;
		case MINUS :
			buffer.append(Javascript.MINUS_MINUS);
			break;
		}
	}
}
