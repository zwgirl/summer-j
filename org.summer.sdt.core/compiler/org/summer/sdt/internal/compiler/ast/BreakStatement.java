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
 *								bug 345305 - [compiler][null] Compiler misidentifies a case of "variable can only be null"
 *******************************************************************************/
package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.flow.FlowContext;
import org.summer.sdt.internal.compiler.flow.FlowInfo;
import org.summer.sdt.internal.compiler.flow.InsideSubRoutineFlowContext;
import org.summer.sdt.internal.compiler.javascript.Javascript;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.Scope;

public class BreakStatement extends BranchStatement {

	public BreakStatement(char[] label, int sourceStart, int e) {
		super(label, sourceStart, e);
	}
	
	public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
	
		// here requires to generate a sequence of finally blocks invocations depending corresponding
		// to each of the traversed try statements, so that execution will terminate properly.
	
		// lookup the label, this should answer the returnContext
		FlowContext targetContext = (this.label == null)
			? flowContext.getTargetContextForDefaultBreak()
			: flowContext.getTargetContextForBreakLabel(this.label);
	
		if (targetContext == null) {
			if (this.label == null) {
				currentScope.problemReporter().invalidBreak(this);
			} else {
				currentScope.problemReporter().undefinedLabel(this);
			}
			return flowInfo; // pretend it did not break since no actual target
		}
	
		targetContext.recordAbruptExit();
	
		this.initStateIndex =
			currentScope.methodScope().recordInitializationStates(flowInfo);
	
		this.targetLabel = targetContext.breakLabel();
		FlowContext traversedContext = flowContext;
		int subCount = 0;
		this.subroutines = new SubRoutineStatement[5];
	
		do {
			SubRoutineStatement sub;
			if ((sub = traversedContext.subroutine()) != null) {
				if (subCount == this.subroutines.length) {
					System.arraycopy(this.subroutines, 0, (this.subroutines = new SubRoutineStatement[subCount*2]), 0, subCount); // grow
				}
				this.subroutines[subCount++] = sub;
				if (sub.isSubRoutineEscaping()) {
					break;
				}
			}
			traversedContext.recordReturnFrom(flowInfo.unconditionalInits());
			traversedContext.recordBreakTo(targetContext);
	
			if (traversedContext instanceof InsideSubRoutineFlowContext) {
				ASTNode node = traversedContext.associatedNode;
				if (node instanceof TryStatement) {
					TryStatement tryStatement = (TryStatement) node;
					flowInfo.addInitializationsFrom(tryStatement.subRoutineInits); // collect inits
				}
			} else if (traversedContext == targetContext) {
				// only record break info once accumulated through subroutines, and only against target context
				targetContext.recordBreakFrom(flowInfo);
				break;
			}
		} while ((traversedContext = traversedContext.getLocalParent()) != null);
	
		// resize subroutines
		if (subCount != this.subroutines.length) {
			System.arraycopy(this.subroutines, 0, (this.subroutines = new SubRoutineStatement[subCount]), 0, subCount);
		}
		return FlowInfo.DEAD_END;
	}
	
	public StringBuffer printStatement(int tab, StringBuffer output) {
		printIndent(tab, output).append("break"); //$NON-NLS-1$
		if (this.label != null) output.append(' ').append(this.label);
		return output.append(';');
	}
	
	public void traverse(ASTVisitor visitor, BlockScope blockscope) {
		visitor.visit(this, blockscope);
		visitor.endVisit(this, blockscope);
	}

	@Override
	public void generateJavascript(Scope scope, int indent, StringBuffer buffer) {
		indent(indent, buffer);
		buffer.append(Javascript.BREAK);
		if(label !=null ){
			buffer.append(label);
		}
		
		buffer.append(Javascript.SEMICOLON);
	}
}
