/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Stephan Herrmann - Contribution for
 *								bug 345305 - [compiler][null] Compiler misidentifies a case of "variable can only be null"
 *								bug 392862 - [1.8][compiler][null] Evaluate null annotations on array types
 *								bug 383368 - [compiler][null] syntactic null analysis for field references
 *								bug 403147 - [compiler][null] FUP of bug 400761: consolidate interaction between unboxing, NPE, and deferred checking
 *******************************************************************************/
package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.flow.FlowContext;
import org.summer.sdt.internal.compiler.flow.FlowInfo;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.FieldBinding;
import org.summer.sdt.internal.compiler.lookup.MissingTypeBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemFieldBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemReasons;
import org.summer.sdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TagBits;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeIds;

public class ArrayReference extends Reference {

	public static final char[] indexer = "indexer".toCharArray();
	public Expression receiver;
	public Expression position;
	
	public FieldBinding binding;

	public ArrayReference(Expression rec, Expression pos) {
		this.receiver = rec;
		this.position = pos;
		this.sourceStart = rec.sourceStart;
	}
	
	public FlowInfo analyseAssignment(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo, Assignment assignment, boolean compoundAssignment) {
		// TODO (maxime) optimization: unconditionalInits is applied to all existing calls
		// account for potential ArrayIndexOutOfBoundsException:
		flowContext.recordAbruptExit();
		if (assignment.expression == null) {
			return analyseCode(currentScope, flowContext, flowInfo);
		}
		flowInfo = assignment
			.expression
			.analyseCode(
				currentScope,
				flowContext,
				analyseCode(currentScope, flowContext, flowInfo).unconditionalInits());
		if ((this.resolvedType.tagBits & TagBits.AnnotationNonNull) != 0) {
			int nullStatus = assignment.expression.nullStatus(flowInfo, flowContext);
			if (nullStatus != FlowInfo.NON_NULL) {
				currentScope.problemReporter().nullityMismatch(this, assignment.expression.resolvedType, this.resolvedType, nullStatus, currentScope.environment().getNonNullAnnotationName());
			}
		}
		return flowInfo;
	}
	
	public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
		this.receiver.checkNPE(currentScope, flowContext, flowInfo);
		flowInfo = this.receiver.analyseCode(currentScope, flowContext, flowInfo);
		flowInfo = this.position.analyseCode(currentScope, flowContext, flowInfo);
		this.position.checkNPEbyUnboxing(currentScope, flowContext, flowInfo);
		// account for potential ArrayIndexOutOfBoundsException:
		flowContext.recordAbruptExit();
		return flowInfo;
	}
	
	public boolean checkNPE(BlockScope scope, FlowContext flowContext, FlowInfo flowInfo) {
		if ((this.resolvedType.tagBits & TagBits.AnnotationNullable) != 0) {
			scope.problemReporter().arrayReferencePotentialNullReference(this);
			return true;
		} else {
			return super.checkNPE(scope, flowContext, flowInfo);
		}
	}
	
	public void generateAssignment(BlockScope currentScope, CodeStream codeStream, Assignment assignment, boolean valueRequired) {
		int pc = codeStream.position;
		this.receiver.generateCode(currentScope, codeStream, true);
		if (this.receiver instanceof CastExpression	// ((type[])null)[0]
				&& ((CastExpression)this.receiver).innermostCastedExpression().resolvedType == TypeBinding.NULL){
			codeStream.checkcast(this.receiver.resolvedType);
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
		this.position.generateCode(currentScope, codeStream, true);
		assignment.expression.generateCode(currentScope, codeStream, true);
		codeStream.arrayAtPut(this.resolvedType.id, valueRequired);
		if (valueRequired) {
			codeStream.generateImplicitConversion(assignment.implicitConversion);
		}
	}
	
	/**
	 * Code generation for a array reference
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
		int pc = codeStream.position;
		this.receiver.generateCode(currentScope, codeStream, true);
		if (this.receiver instanceof CastExpression	// ((type[])null)[0]
				&& ((CastExpression)this.receiver).innermostCastedExpression().resolvedType == TypeBinding.NULL){
			codeStream.checkcast(this.receiver.resolvedType);
		}
		this.position.generateCode(currentScope, codeStream, true);
		codeStream.arrayAt(this.resolvedType.id);
		// Generating code for the potential runtime type checking
		if (valueRequired) {
			codeStream.generateImplicitConversion(this.implicitConversion);
		} else {
			boolean isUnboxing = (this.implicitConversion & TypeIds.UNBOXING) != 0;
			// conversion only generated if unboxing
			if (isUnboxing) codeStream.generateImplicitConversion(this.implicitConversion);
			switch (isUnboxing ? postConversionType(currentScope).id : this.resolvedType.id) {
				case T_long :
				case T_double :
					codeStream.pop2();
					break;
				default :
					codeStream.pop();
			}
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}
	
	public void generateCompoundAssignment(BlockScope currentScope, CodeStream codeStream, Expression expression, int operator, int assignmentImplicitConversion, boolean valueRequired) {
		this.receiver.generateCode(currentScope, codeStream, true);
		if (this.receiver instanceof CastExpression	// ((type[])null)[0]
				&& ((CastExpression)this.receiver).innermostCastedExpression().resolvedType == TypeBinding.NULL){
			codeStream.checkcast(this.receiver.resolvedType);
		}
		this.position.generateCode(currentScope, codeStream, true);
		codeStream.dup2();
		codeStream.arrayAt(this.resolvedType.id);
		int operationTypeID;
		switch(operationTypeID = (this.implicitConversion & TypeIds.IMPLICIT_CONVERSION_MASK) >> 4) {
			case T_JavaLangString :
			case T_JavaLangObject :
			case T_undefined :
				codeStream.generateStringConcatenationAppend(currentScope, null, expression);
				break;
			default :
				// promote the array reference to the suitable operation type
				codeStream.generateImplicitConversion(this.implicitConversion);
				// generate the increment value (will by itself  be promoted to the operation value)
				if (expression == IntLiteral.One) { // prefix operation
					codeStream.generateConstant(expression.constant, this.implicitConversion);
				} else {
					expression.generateCode(currentScope, codeStream, true);
				}
				// perform the operation
				codeStream.sendOperator(operator, operationTypeID);
				// cast the value back to the array reference type
				codeStream.generateImplicitConversion(assignmentImplicitConversion);
		}
		codeStream.arrayAtPut(this.resolvedType.id, valueRequired);
	}
	
	public void generatePostIncrement(BlockScope currentScope, CodeStream codeStream, CompoundAssignment postIncrement, boolean valueRequired) {
		this.receiver.generateCode(currentScope, codeStream, true);
		if (this.receiver instanceof CastExpression	// ((type[])null)[0]
				&& ((CastExpression)this.receiver).innermostCastedExpression().resolvedType == TypeBinding.NULL){
			codeStream.checkcast(this.receiver.resolvedType);
		}
		this.position.generateCode(currentScope, codeStream, true);
		codeStream.dup2();
		codeStream.arrayAt(this.resolvedType.id);
		if (valueRequired) {
			switch(this.resolvedType.id) {
				case TypeIds.T_long :
				case TypeIds.T_double :
					codeStream.dup2_x2();
					break;
				default :
					codeStream.dup_x2();
					break;
			}
		}
		codeStream.generateImplicitConversion(this.implicitConversion);
		codeStream.generateConstant(
			postIncrement.expression.constant,
			this.implicitConversion);
		codeStream.sendOperator(postIncrement.operator, this.implicitConversion & TypeIds.COMPILE_TYPE_MASK);
		codeStream.generateImplicitConversion(
			postIncrement.preAssignImplicitConversion);
		codeStream.arrayAtPut(this.resolvedType.id, false);
	}
	
	public StringBuffer printExpression(int indent, StringBuffer output) {
		this.receiver.printExpression(0, output).append('[');
		return this.position.printExpression(0, output).append(']');
	}
	
	public TypeBinding resolveType(BlockScope scope) {
		this.constant = Constant.NotAConstant;
		if (this.receiver instanceof CastExpression	// no cast check for ((type[])null)[0]
				&& ((CastExpression)this.receiver).innermostCastedExpression() instanceof NullLiteral) {
			this.receiver.bits |= ASTNode.DisableUnnecessaryCastCheck; // will check later on
		}
		this.resolvedType = this.receiver.resolveType(scope);
		if (this.resolvedType == null) {
			this.constant = Constant.NotAConstant;
			return null;
		}
		
		TypeBinding positionType = this.position.resolveType(scope);
		
		if (positionType == null) {
			scope.problemReporter().invalidType(this.position, this.resolvedType);
		}
		
		//cym 2014-11-24
		FieldBinding indexerBinding = this.binding = scope.getIndexer((TypeBinding) this.resolvedType, positionType, null);
		if (!indexerBinding.isValidBinding()) {
			this.constant = Constant.NotAConstant;
			if (this.receiver.resolvedType instanceof ProblemReferenceBinding) {
				// problem already got signaled on receiver, do not report secondary problem
				return null;
			}
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=245007 avoid secondary errors in case of
			// missing super type for anonymous classes ... 
			ReferenceBinding declaringClass = indexerBinding.declaringClass;
			boolean avoidSecondary = declaringClass != null &&
									 declaringClass.isAnonymousType() &&
									 declaringClass.superclass() instanceof MissingTypeBinding;
			if (!avoidSecondary) {
				scope.problemReporter().invalidIndexer(this, this.resolvedType);
			}
			if (indexerBinding instanceof ProblemFieldBinding) {
				ProblemFieldBinding problemFieldBinding = (ProblemFieldBinding) indexerBinding;
				FieldBinding closestMatch = problemFieldBinding.closestMatch;
				switch(problemFieldBinding.problemId()) {
					case ProblemReasons.InheritedNameHidesEnclosingName :
					case ProblemReasons.NotVisible :
					case ProblemReasons.NonStaticReferenceInConstructorInvocation :
					case ProblemReasons.NonStaticReferenceInStaticContext :
						if (closestMatch != null) {
							indexerBinding = closestMatch;
						}
				}
			}
			if (!indexerBinding.isValidBinding()) {
				return null;
			}
		}
		
		return indexerBinding.type;
	}
	
	public void traverse(ASTVisitor visitor, BlockScope scope) {
		if (visitor.visit(this, scope)) {
			this.receiver.traverse(visitor, scope);
			this.position.traverse(visitor, scope);
		}
		visitor.endVisit(this, scope);
	}
	
	protected StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		this.receiver.doGenerateExpression(scope, 0, output).append('[');
		return this.position.doGenerateExpression(scope, 0, output).append(']');
	}
}
