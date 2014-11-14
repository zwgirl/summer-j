/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.summer.sdt.internal.compiler.parser;

/**
 * Internal field structure for parsing recovery
 */
import java.util.HashSet;
import java.util.Set;

import org.summer.sdt.internal.compiler.ast.ASTNode;
import org.summer.sdt.internal.compiler.ast.AbstractVariableDeclaration;
import org.summer.sdt.internal.compiler.ast.Annotation;
import org.summer.sdt.internal.compiler.ast.ArrayInitializer;
import org.summer.sdt.internal.compiler.ast.ArrayQualifiedTypeReference;
import org.summer.sdt.internal.compiler.ast.ArrayTypeReference;
import org.summer.sdt.internal.compiler.ast.EventDeclaration;
import org.summer.sdt.internal.compiler.ast.Expression;
import org.summer.sdt.internal.compiler.ast.FieldDeclaration;
import org.summer.sdt.internal.compiler.ast.Statement;
import org.summer.sdt.internal.compiler.ast.TypeDeclaration;

@SuppressWarnings("rawtypes")
public class RecoveredEvent extends RecoveredElement {

	public EventDeclaration eventDeclaration;
	boolean alreadyCompletedFieldInitialization;

	public RecoveredAnnotation[] annotations;
	public int annotationCount;
	
	public int modifiers;
	public int modifiersStart;

	public RecoveredType[] anonymousTypes;
	public int anonymousTypeCount;
	public RecoveredEvent(EventDeclaration fieldDeclaration, RecoveredElement parent, int bracketBalance){
		this(fieldDeclaration, parent, bracketBalance, null);
	}
	public RecoveredEvent(EventDeclaration eventDeclaration, RecoveredElement parent, int bracketBalance, Parser parser){
		super(parent, bracketBalance, parser);
		this.eventDeclaration = eventDeclaration;
		this.alreadyCompletedFieldInitialization = eventDeclaration.initialization != null;
	}
	/*
	 * Record a field declaration
	 */
	public RecoveredElement add(EventDeclaration addedEventDeclaration, int bracketBalanceValue) {
	
		/* default behavior is to delegate recording to parent if any */
		resetPendingModifiers();
		if (this.parent == null) return this; // ignore
		
		if (this.eventDeclaration.declarationSourceStart == addedEventDeclaration.declarationSourceStart) {
			if (this.eventDeclaration.initialization != null) {
				this.updateSourceEndIfNecessary(this.eventDeclaration.initialization.sourceEnd);
			} else {
				this.updateSourceEndIfNecessary(this.eventDeclaration.sourceEnd);
			}
		} else {
			this.updateSourceEndIfNecessary(previousAvailableLineEnd(addedEventDeclaration.declarationSourceStart - 1));
		}
		return this.parent.add(addedEventDeclaration, bracketBalanceValue);
	}
	/*
	 * Record an expression statement if field is expecting an initialization expression,
	 * used for completion inside field initializers.
	 */
	public RecoveredElement add(Statement statement, int bracketBalanceValue) {
	
		if (this.alreadyCompletedFieldInitialization || !(statement instanceof Expression)) {
			return super.add(statement, bracketBalanceValue);
		} else {
			if (statement.sourceEnd > 0)
					this.alreadyCompletedFieldInitialization = true;
			// else we may still be inside the initialization, having parsed only a part of it yet
			this.eventDeclaration.initialization = (Expression)statement;
			this.eventDeclaration.declarationSourceEnd = statement.sourceEnd;
			this.eventDeclaration.declarationEnd = statement.sourceEnd;
			return this;
		}
	}
	/*
	 * Record a type declaration if this field is expecting an initialization expression
	 * and the type is an anonymous type.
	 * Used for completion inside field initializers.
	 */
	public RecoveredElement add(TypeDeclaration typeDeclaration, int bracketBalanceValue) {
	
		if (this.alreadyCompletedFieldInitialization
				|| ((typeDeclaration.bits & ASTNode.IsAnonymousType) == 0)
				|| (this.eventDeclaration.declarationSourceEnd != 0 && typeDeclaration.sourceStart > this.eventDeclaration.declarationSourceEnd)) {
			return super.add(typeDeclaration, bracketBalanceValue);
		} else {
			// Prepare anonymous type list
			if (this.anonymousTypes == null) {
				this.anonymousTypes = new RecoveredType[5];
				this.anonymousTypeCount = 0;
			} else {
				if (this.anonymousTypeCount == this.anonymousTypes.length) {
					System.arraycopy(
						this.anonymousTypes,
						0,
						(this.anonymousTypes = new RecoveredType[2 * this.anonymousTypeCount]),
						0,
						this.anonymousTypeCount);
				}
			}
			// Store type declaration as an anonymous type
			RecoveredType element = new RecoveredType(typeDeclaration, this, bracketBalanceValue);
			this.anonymousTypes[this.anonymousTypeCount++] = element;
			return element;
		}
	}
	public void attach(RecoveredAnnotation[] annots, int annotCount, int mods, int modsSourceStart) {
		if (annotCount > 0) {
			Annotation[] existingAnnotations = this.eventDeclaration.annotations;
			if (existingAnnotations != null) {
				this.annotations = new RecoveredAnnotation[annotCount];
				this.annotationCount = 0;
				next : for (int i = 0; i < annotCount; i++) {
					for (int j = 0; j < existingAnnotations.length; j++) {
						if (annots[i].annotation == existingAnnotations[j]) continue next;
					}
					this.annotations[this.annotationCount++] = annots[i];
				}
			} else {
				this.annotations = annots;
				this.annotationCount = annotCount;
			}
		}
	
		if (mods != 0) {
			this.modifiers = mods;
			this.modifiersStart = modsSourceStart;
		}
	}
	/*
	 * Answer the associated parsed structure
	 */
	public ASTNode parseTree(){
		return this.eventDeclaration;
	}
	/*
	 * Answer the very source end of the corresponding parse node
	 */
	public int sourceEnd(){
		return this.eventDeclaration.declarationSourceEnd;
	}
	public String toString(int tab){
		StringBuffer buffer = new StringBuffer(tabString(tab));
		buffer.append("Recovered Event:\n"); //$NON-NLS-1$
		this.eventDeclaration.print(tab + 1, buffer);
		if (this.annotations != null) {
			for (int i = 0; i < this.annotationCount; i++) {
				buffer.append("\n"); //$NON-NLS-1$
				buffer.append(this.annotations[i].toString(tab + 1));
			}
		}
		if (this.anonymousTypes != null) {
			for (int i = 0; i < this.anonymousTypeCount; i++){
				buffer.append("\n"); //$NON-NLS-1$
				buffer.append(this.anonymousTypes[i].toString(tab + 1));
			}
		}
		return buffer.toString();
	}
	public FieldDeclaration updatedFieldDeclaration(int depth, Set knownTypes){
		/* update annotations */
		if (this.modifiers != 0) {
			this.eventDeclaration.modifiers |= this.modifiers;
			if (this.modifiersStart < this.eventDeclaration.declarationSourceStart) {
				this.eventDeclaration.declarationSourceStart = this.modifiersStart;
			}
		}
		/* update annotations */
		if (this.annotationCount > 0){
			int existingCount = this.eventDeclaration.annotations == null ? 0 : this.eventDeclaration.annotations.length;
			Annotation[] annotationReferences = new Annotation[existingCount + this.annotationCount];
			if (existingCount > 0){
				System.arraycopy(this.eventDeclaration.annotations, 0, annotationReferences, this.annotationCount, existingCount);
			}
			for (int i = 0; i < this.annotationCount; i++){
				annotationReferences[i] = this.annotations[i].updatedAnnotationReference();
			}
			this.eventDeclaration.annotations = annotationReferences;
	
			int start = this.annotations[0].annotation.sourceStart;
			if (start < this.eventDeclaration.declarationSourceStart) {
				this.eventDeclaration.declarationSourceStart = start;
			}
		}
	
		if (this.anonymousTypes != null) {
			if(this.eventDeclaration.initialization == null) {
				ArrayInitializer recoveredInitializers = null;
				int recoveredInitializersCount = 0;
				if (this.anonymousTypeCount > 1) {
					recoveredInitializers = new ArrayInitializer();
					recoveredInitializers.expressions = new Expression[this.anonymousTypeCount];
				}
				for (int i = 0; i < this.anonymousTypeCount; i++){
					RecoveredType recoveredType = this.anonymousTypes[i];
					TypeDeclaration typeDeclaration = recoveredType.typeDeclaration;
					if(typeDeclaration.declarationSourceEnd == 0) {
						typeDeclaration.declarationSourceEnd = this.eventDeclaration.declarationSourceEnd;
						typeDeclaration.bodyEnd = this.eventDeclaration.declarationSourceEnd;
					}
					if (recoveredType.preserveContent){
						TypeDeclaration anonymousType = recoveredType.updatedTypeDeclaration(depth + 1, knownTypes);
						if (anonymousType != null) {
							if (this.anonymousTypeCount > 1) {
								if (recoveredInitializersCount == 0) {
									this.eventDeclaration.initialization = recoveredInitializers;
								}
								recoveredInitializers.expressions[recoveredInitializersCount++] = anonymousType.allocation;
							}
							else {
								this.eventDeclaration.initialization = anonymousType.allocation;							
							}
							int end = anonymousType.declarationSourceEnd;
							if (end > this.eventDeclaration.declarationSourceEnd) { // https://bugs.eclipse.org/bugs/show_bug.cgi?id=307337
								this.eventDeclaration.declarationSourceEnd = end;
								this.eventDeclaration.declarationEnd = end;
							}
						}
					}
				}
				if (this.anonymousTypeCount > 0) this.eventDeclaration.bits |= ASTNode.HasLocalType;
			} else if(this.eventDeclaration.getKind() == AbstractVariableDeclaration.ENUM_CONSTANT) {
				// fieldDeclaration is an enum constant
				for (int i = 0; i < this.anonymousTypeCount; i++){
					RecoveredType recoveredType = this.anonymousTypes[i];
					TypeDeclaration typeDeclaration = recoveredType.typeDeclaration;
					if(typeDeclaration.declarationSourceEnd == 0) {
						typeDeclaration.declarationSourceEnd = this.eventDeclaration.declarationSourceEnd;
						typeDeclaration.bodyEnd = this.eventDeclaration.declarationSourceEnd;
					}
					// if the enum is recovered then enum constants must be recovered too.
					// depth is considered as the same as the depth of the enum
					recoveredType.updatedTypeDeclaration(depth, knownTypes);
				}
			}
		}
		return this.eventDeclaration;
	}
	/*
	 * A closing brace got consumed, might have closed the current element,
	 * in which case both the currentElement is exited.
	 *
	 * Fields have no associated braces, thus if matches, then update parent.
	 */
	public RecoveredElement updateOnClosingBrace(int braceStart, int braceEnd){
		if (this.bracketBalance > 0){ // was an array initializer
			this.bracketBalance--;
			if (this.bracketBalance == 0) {
				if(this.eventDeclaration.getKind() == AbstractVariableDeclaration.ENUM_CONSTANT) {
					updateSourceEndIfNecessary(braceEnd - 1);
					return this.parent;
				} else {
					if (this.eventDeclaration.declarationSourceEnd > 0)
						this.alreadyCompletedFieldInitialization = true;
				}
			}
			return this;
		} else if (this.bracketBalance == 0) {
			this.alreadyCompletedFieldInitialization = true;
			updateSourceEndIfNecessary(braceEnd - 1);
		}
		if (this.parent != null){
			return this.parent.updateOnClosingBrace(braceStart, braceEnd);
		}
		return this;
	}
	/*
	 * An opening brace got consumed, might be the expected opening one of the current element,
	 * in which case the bodyStart is updated.
	 */
	public RecoveredElement updateOnOpeningBrace(int braceStart, int braceEnd){
		if (this.eventDeclaration.declarationSourceEnd == 0) {
			if (this.eventDeclaration.type instanceof ArrayTypeReference || this.eventDeclaration.type instanceof ArrayQualifiedTypeReference) {
				if (!this.alreadyCompletedFieldInitialization) {
					this.bracketBalance++;
					return null; // no update is necessary	(array initializer)
				}
			} else {  // https://bugs.eclipse.org/bugs/show_bug.cgi?id=308980
				// in case an initializer bracket is opened in a non-array field
				// e.g. int field = {..
				this.bracketBalance++;
				return null; // no update is necessary	(array initializer)
			}
		}
		if (this.eventDeclaration.declarationSourceEnd == 0
			&& this.eventDeclaration.getKind() == AbstractVariableDeclaration.ENUM_CONSTANT){
			this.bracketBalance++;
			return null; // no update is necessary	(enum constant)
		}
		// might be an array initializer
		this.updateSourceEndIfNecessary(braceStart - 1, braceEnd - 1);
		return this.parent.updateOnOpeningBrace(braceStart, braceEnd);
	}
	public void updateParseTree(){
		updatedFieldDeclaration(0, new HashSet());
	}
	/*
	 * Update the declarationSourceEnd of the corresponding parse node
	 */
	public void updateSourceEndIfNecessary(int bodyStart, int bodyEnd){
		if (this.eventDeclaration.declarationSourceEnd == 0) {
			this.eventDeclaration.declarationSourceEnd = bodyEnd;
			this.eventDeclaration.declarationEnd = bodyEnd;
		}
	}
}
