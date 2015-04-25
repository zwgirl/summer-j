/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
 * reduce a type reference containing the selection identifier as a single
 * name reference.
 * e.g.
 *
 *	class X extends [start]Object[end]
 *
 *	---> class X extends <SelectOnType:Object>
 *
 */
import org.summer.sdt.internal.compiler.ast.HtmlTypeReference;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;

public class SelectionOnHtmlTypeReference extends HtmlTypeReference {
	public SelectionOnHtmlTypeReference(char[][] source, long[] pos) {
		super(source, pos);
	}
	public void aboutToResolve(Scope scope) {
		getTypeBinding(scope.parent); // step up from the ClassScope
	}
	
	protected TypeBinding getTypeBinding(Scope scope) {
		super.getTypeBinding(scope);
		if (!this.resolvedType.isValidBinding()) {
			throw new SelectionNodeFound();
		}
		throw new SelectionNodeFound(this.resolvedType);
	}
	public StringBuffer printExpression(int indent, StringBuffer output) {
	
		return output.append("<SelectOnType:").append(this.name()).append('>');//$NON-NLS-1$
	}
}
