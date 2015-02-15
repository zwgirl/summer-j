/*******************************************************************************
 * Copyright (c) 2013 Jesper S Moller and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jesper S Moller <jesper@selskabet.org> - initial API and implementation
 ********************************************************************************/
package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.MethodBinding;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeConstants;

public class ContainerAnnotation extends SingleMemberAnnotation {
	
	private Annotation [] containees;
	private ArrayInitializer memberValues;
	
	public ContainerAnnotation(Annotation repeatingAnnotation, ReferenceBinding containerAnnotationType, BlockScope scope) {
		
		char [][] containerTypeName = containerAnnotationType.compoundName;
		if (containerTypeName.length == 1) {
			this.type = new SingleTypeReference(containerTypeName[0], 0);
		} else {
			this.type = new QualifiedTypeReference(containerTypeName, new long [containerTypeName.length]);
		}
		
		this.sourceStart = repeatingAnnotation.sourceStart;
		this.sourceEnd = repeatingAnnotation.sourceEnd;
		
		this.resolvedType = containerAnnotationType;
		this.recipient = repeatingAnnotation.recipient;
		this.containees = new Annotation[0];
		this.memberValue = this.memberValues = new ArrayInitializer();
		addContainee(repeatingAnnotation);
	}
	
	public void addContainee(Annotation repeatingAnnotation) {
		final int length = this.containees.length;
		System.arraycopy(this.containees, 0, this.containees = new Annotation[length + 1], 0, length);
		this.containees[length] = repeatingAnnotation;
		this.memberValues.expressions = this.containees;
		repeatingAnnotation.setPersistibleAnnotation(length == 0 ? this : null);
	}
	
	// Resolve the compiler synthesized container annotation.
	public TypeBinding resolveType(BlockScope scope) {

		if (this.compilerAnnotation != null)
			return this.resolvedType;

		this.constant = Constant.NotAConstant;

		ReferenceBinding containerAnnotationType = (ReferenceBinding) this.resolvedType;
		if (!containerAnnotationType.isValidBinding())
			containerAnnotationType = (ReferenceBinding) containerAnnotationType.closestMatch();
		Annotation repeatingAnnotation = this.containees[0];
		ReferenceBinding repeatingAnnotationType = (ReferenceBinding) repeatingAnnotation.resolvedType;
		if (!repeatingAnnotationType.isDeprecated() && isTypeUseDeprecated(containerAnnotationType, scope)) {
			scope.problemReporter().deprecatedType(containerAnnotationType, repeatingAnnotation);
		}
		checkContainerAnnotationType(repeatingAnnotation, scope, containerAnnotationType, repeatingAnnotationType, true); // true => repeated *use* site error reporting requested.
		this.resolvedType = containerAnnotationType = repeatingAnnotationType.containerAnnotationType();
		if (!this.resolvedType.isValidBinding())
			return this.resolvedType;
		
		// OK, the declaration site of the repeating annotation type as well as the use site where the annotations actually repeat pass muster. 
		MethodBinding[] methods = containerAnnotationType.methods();
		MemberValuePair pair = memberValuePairs()[0];
		
		for (int i = 0, length = methods.length; i < length; i++) {
			MethodBinding method = methods[i];
			if (CharOperation.equals(method.name, TypeConstants.VALUE)) {
				pair.binding = method;
				pair.resolveTypeExpecting(scope, method.returnType);
			}
		}
		this.compilerAnnotation = scope.environment().createAnnotation((ReferenceBinding) this.resolvedType, computeElementValuePairs());
		return this.resolvedType;
	}
}
