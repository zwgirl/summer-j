package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.impl.CompilerOptions;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.SourceTypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeVariableBinding;
import org.summer.sdt.internal.compiler.problem.ProblemSeverities;

public class FunctionType extends TypeReference {
	public char[] token;
	public TypeReference returnType;
	public TypeReference[] arguments;
	
	public FunctionType(char[] token, long pos) {
		this.token = token;
		this.sourceStart = (int) (pos>>>32)  ;
		this.sourceEnd = (int) (pos & 0x00000000FFFFFFFFL) ;
	}
	
	@Override
	public TypeReference augmentTypeWithAdditionalDimensions(
			int additionalDimensions, Annotation[][] additionalAnnotations,
			boolean isVarargs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getLastToken() {
		return this.token;
	}

	protected TypeBinding getTypeBinding(Scope scope) {
		if (this.resolvedType != null)
			return this.resolvedType;

		this.resolvedType = scope.getType(this.token);
		
		if (this.resolvedType instanceof TypeVariableBinding) {
			TypeVariableBinding typeVariable = (TypeVariableBinding) this.resolvedType;
			if (typeVariable.declaringElement instanceof SourceTypeBinding) {
				scope.tagAsAccessingEnclosingInstanceStateOf((ReferenceBinding) typeVariable.declaringElement, true /* type variable access */);
			}
		}

		if (scope.kind == Scope.CLASS_SCOPE && this.resolvedType.isValidBinding())
			if (((ClassScope) scope).detectHierarchyCycle(this.resolvedType, this))
				return null;
		return this.resolvedType;
	}

	public char [][] getTypeName() {
		return new char[][] { this.token };
	}

	public StringBuffer printExpression(int indent, StringBuffer output){
		if (this.annotations != null && this.annotations[0] != null) {
			printAnnotations(this.annotations[0], output);
			output.append(' ');
		}
		return output.append(this.token);
	}

	public TypeBinding resolveTypeEnclosing(BlockScope scope, ReferenceBinding enclosingType) {
		this.resolvedType = scope.getMemberType(this.token, enclosingType);
		boolean hasError = false;
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=391500
		resolveAnnotations(scope, 0); // defaultNullness not relevant, the only caller within the compiler: QAE
		TypeBinding memberType = this.resolvedType; // load after possible update in resolveAnnotations()
		if (!memberType.isValidBinding()) {
			hasError = true;
			scope.problemReporter().invalidEnclosingType(this, memberType, enclosingType);
			memberType = ((ReferenceBinding)memberType).closestMatch();
			if (memberType == null) {
				return null;
			}
		}
		if (isTypeUseDeprecated(memberType, scope))
			reportDeprecatedType(memberType, scope);
		memberType = scope.environment().convertToRawType(memberType, false /*do not force conversion of enclosing types*/);
		if (memberType.isRawType()
				&& (this.bits & IgnoreRawTypeCheck) == 0
				&& scope.compilerOptions().getSeverity(CompilerOptions.RawTypeReference) != ProblemSeverities.Ignore){
			scope.problemReporter().rawTypeReference(this, memberType);
		}
		if (hasError) {
			// do not store the computed type, keep the problem type instead
			return memberType;
		}
		return this.resolvedType = memberType;
	}

	public void traverse(ASTVisitor visitor, BlockScope scope) {
		if (visitor.visit(this, scope)) {
			if (this.annotations != null) {
				Annotation [] typeAnnotations = this.annotations[0];
				for (int i = 0, length = typeAnnotations == null ? 0 : typeAnnotations.length; i < length; i++)
					typeAnnotations[i].traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, scope);
	}

	public void traverse(ASTVisitor visitor, ClassScope scope) {
		if (visitor.visit(this, scope)) {
			if (this.annotations != null) {
				Annotation [] typeAnnotations = this.annotations[0];
				for (int i = 0, length = typeAnnotations == null ? 0 : typeAnnotations.length; i < length; i++)
					typeAnnotations[i].traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, scope);
	}

	@Override
	public StringBuffer generateExpression(Scope scope, int indent,
			StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

}
