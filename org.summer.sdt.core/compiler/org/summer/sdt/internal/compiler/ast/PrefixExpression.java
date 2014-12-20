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
import org.summer.sdt.internal.compiler.javascript.Dependency;
import org.summer.sdt.internal.compiler.javascript.Javascript;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

public class PrefixExpression extends CompoundAssignment {

	/**
	 * PrefixExpression constructor comment.
	 * @param lhs org.summer.sdt.internal.compiler.ast.Expression
	 * @param expression org.summer.sdt.internal.compiler.ast.Expression
	 * @param operator int
	 */
	public PrefixExpression(Expression lhs, Expression expression, int operator, int pos) {
		super(lhs, expression, operator, lhs.sourceEnd);
		this.sourceStart = pos;
		this.sourceEnd = lhs.sourceEnd;
	}
	public boolean checkCastCompatibility() {
		return false;
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
	
		output.append(operatorToString()).append(' ');
		return this.lhs.printExpression(0, output);
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
	
	protected StringBuffer doGenerateExpression(Scope scope, Dependency dependency, int indent, StringBuffer output) {
		output.append(operatorToString()).append(' ');
		return this.lhs.generateExpression(scope, dependency, 0, output);
	}
}
