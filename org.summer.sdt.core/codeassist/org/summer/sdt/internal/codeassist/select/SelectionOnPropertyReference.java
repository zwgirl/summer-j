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
package org.summer.sdt.internal.codeassist.select;

/*
 * Selection node build by the parser in any case it was intending to
 * reduce a field reference containing the cursor.
 * e.g.
 *
 *	class X {
 *    void foo() {
 *      bar().[start]fred[end]
 *    }
 *  }
 *
 *	---> class X {
 *         void foo() {
 *           <SelectOnFieldReference:bar().fred>
 *         }
 *       }
 *
 */

import org.summer.sdt.internal.compiler.ast.Expression;
import org.summer.sdt.internal.compiler.ast.HtmlPropertyReference;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ProblemReasons;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;

public class SelectionOnPropertyReference extends HtmlPropertyReference {

	public SelectionOnPropertyReference(char[] source , long pos) {
		super(source, pos);
	}
	
	public SelectionOnPropertyReference(char[] source, long pos, Expression receiver) {
		super(source, pos, receiver);
	}

	public StringBuffer printExpression(int indent, StringBuffer output){

		output.append("<SelectionOnFieldReference:");  //$NON-NLS-1$
		return super.printExpression(0, output).append('>');
	}

	//cym 2015-04-12
	public TypeBinding resolveType(BlockScope scope){

		super.resolveType(scope);
		// tolerate some error cases
		if (this.binding == null ||
				!(this.binding.isValidBinding() ||
					this.binding.problemId() == ProblemReasons.NotVisible
					|| this.binding.problemId() == ProblemReasons.InheritedNameHidesEnclosingName
					|| this.binding.problemId() == ProblemReasons.NonStaticReferenceInConstructorInvocation
					|| this.binding.problemId() == ProblemReasons.NonStaticReferenceInStaticContext))
			throw new SelectionNodeFound();
		else
			throw new SelectionNodeFound(this.binding);
	}
}
