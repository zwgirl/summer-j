/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Stephan Herrmann - Contribution for
 *								Bug 400874 - [1.8][compiler] Inference infrastructure should evolve to meet JLS8 18.x (Part G of JSR335 spec)
 *								Bug 427438 - [1.8][compiler] NPE at org.summer.sdt.internal.compiler.ast.ConditionalExpression.generateCode(ConditionalExpression.java:280)
 *******************************************************************************/
package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.flow.FlowContext;
import org.summer.sdt.internal.compiler.flow.FlowInfo;
import org.summer.sdt.internal.compiler.javascript.Dependency;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.InferenceContext18;
import org.summer.sdt.internal.compiler.lookup.InvocationSite;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;

public abstract class AbstractVariableDeclaration extends Statement implements InvocationSite {
	public int declarationEnd;
	/**
	 * For local declarations (outside of for statement initialization) and field declarations,
	 * the declarationSourceEnd covers multiple locals if any.
	 * For local declarations inside for statement initialization, this is not the case.
	 */
	public int declarationSourceEnd;
	public int declarationSourceStart;
	public int hiddenVariableDepth; // used to diagnose hiding scenarii
	public Expression initialization;
	public int modifiers;
	public int modifiersSourceStart;
	public Annotation[] annotations;

	public char[] name;

	public TypeReference type;

	public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
		return flowInfo;
	}

	public static final int FIELD = 1;
	public static final int INITIALIZER = 2;
	public static final int ENUM_CONSTANT = 3;
	public static final int LOCAL_VARIABLE = 4;
	public static final int PARAMETER = 5;
	public static final int TYPE_PARAMETER = 6;


	/**
	 * @see org.summer.sdt.internal.compiler.lookup.InvocationSite#genericTypeArguments()
	 */
	public TypeBinding[] genericTypeArguments() {
		return null;
	}

	/**
	 * Returns the constant kind of this variable declaration
	 */
	public abstract int getKind();

	public InferenceContext18 freshInferenceContext(Scope scope) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.summer.sdt.internal.compiler.lookup.InvocationSite#isSuperAccess()
	 */
	public boolean isSuperAccess() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.summer.sdt.internal.compiler.lookup.InvocationSite#isTypeAccess()
	 */
	public boolean isTypeAccess() {
		return false;
	}

	public StringBuffer printStatement(int indent, StringBuffer output) {
		printAsExpression(indent, output);
		switch(getKind()) {
			case ENUM_CONSTANT:
				return output.append(',');
			default:
				return output.append(';');
		}
	}

	public StringBuffer printAsExpression(int indent, StringBuffer output) {
		printIndent(indent, output);
		printModifiers(this.modifiers, output);
		if (this.annotations != null) {
			printAnnotations(this.annotations, output);
			output.append(' ');
		}

		if (this.type != null) {
			this.type.print(0, output).append(' ');
		}
		output.append(this.name);
		switch(getKind()) {
			case ENUM_CONSTANT:
				if (this.initialization != null) {
					this.initialization.printExpression(indent, output);
				}
				break;
			default:
				if (this.initialization != null) {
					output.append(" = "); //$NON-NLS-1$
					this.initialization.printExpression(indent, output);
				}
		}
		return output;
	}

	public void resolve(BlockScope scope) {
		// do nothing by default (redefined for local variables)
	}

	/* (non-Javadoc)
	 * @see org.summer.sdt.internal.compiler.lookup.InvocationSite#setActualReceiverType(org.summer.sdt.internal.compiler.lookup.ReferenceBinding)
	 */
	public void setActualReceiverType(ReferenceBinding receiverType) {
		// do nothing by default
	}

	/* (non-Javadoc)
	 * @see org.summer.sdt.internal.compiler.lookup.InvocationSite#setDepth(int)
	 */
	public void setDepth(int depth) {

		this.hiddenVariableDepth = depth;
	}

	/* (non-Javadoc)
	 * @see org.summer.sdt.internal.compiler.lookup.InvocationSite#setFieldIndex(int)
	 */
	public void setFieldIndex(int depth) {
		// do nothing by default
	}
	
	public StringBuffer generateStatement(Scope scope, Dependency depsManager, int indent, StringBuffer output) {
		doGenerateExpression(scope, depsManager, indent, output);
		switch(getKind()) {
			case ENUM_CONSTANT:
				return output.append(',');
			default:
				return output.append(';');
		}
	}

	protected StringBuffer doGenerateExpression(Scope scope, Dependency depsManager, int indent, StringBuffer output) {
		printIndent(indent, output);
		output.append("var ");
		output.append(this.name);
		switch(getKind()) {
			case ENUM_CONSTANT:
				if (this.initialization != null) {
					this.initialization.doGenerateExpression(scope, depsManager, indent, output);
				}
				break;
			default:
				if (this.initialization != null) {
					output.append(" = "); //$NON-NLS-1$
					this.initialization.doGenerateExpression(scope, depsManager, indent, output);
				}
		}
		return output;
	}
}
