package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.CategorizedProblem;
import org.summer.sdt.internal.compiler.CompilationResult;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.impl.ReferenceContext;
import org.summer.sdt.internal.compiler.lookup.CompilationUnitScope;
import org.summer.sdt.internal.compiler.lookup.ModuleScope;
import org.summer.sdt.internal.compiler.problem.AbortCompilation;
import org.summer.sdt.internal.compiler.problem.AbortCompilationUnit;
import org.summer.sdt.internal.compiler.problem.AbortMethod;
import org.summer.sdt.internal.compiler.problem.AbortType;
import org.summer.sdt.internal.compiler.problem.ProblemSeverities;

public class ModuleDeclaration extends ASTNode implements ProblemSeverities, ReferenceContext{
	
	public int declarationSourceStart;
	public int declarationSourceEnd;
	public int bodyStart;
	public int bodyEnd; // doesn't include the trailing comment if any.
	
	public TypeDeclaration[] types;
	public int[][] comments;

	public ModuleScope scope;
	
	public int modifiers = ClassFileConstants.AccDefault;
	public int modifiersSourceStart;
	public Annotation[] annotations;
	
	public Javadoc javadoc; // 1.5 addition for package-info.java

	public char[] name;
	
	public boolean ignoreFurtherInvestigation = false; // once pointless to investigate due to errors
	public boolean ignoreMethodBodies = false;
	
	public CompilationResult compilationResult;

	public ModuleDeclaration(CompilationResult compilationResult/*
							 * ProblemReporter problemReporter,
							 * CompilationResult compilationResult, int
							 * sourceLength
							 */) {
		// this.problemReporter = problemReporter;
		this.compilationResult = compilationResult;
		// by definition of a compilation unit....
		this.sourceStart = 0;
		// this.sourceEnd = sourceLength - 1;
	}
	
//	public boolean isPackageInfo() {
//		return CharOperation.equals(getMainTypeName(), TypeConstants.PACKAGE_INFO_NAME);
//	}
//
//	public char[] getMainTypeName() {
//		if (this.compilationResult.compilationUnit == null) {
//			char[] fileName = this.compilationResult.getFileName();
//	
//			int start = CharOperation.lastIndexOf('/', fileName) + 1;
//			if (start == 0 || start < CharOperation.lastIndexOf('\\', fileName))
//				start = CharOperation.lastIndexOf('\\', fileName) + 1;
//	
//			int end = CharOperation.lastIndexOf('.', fileName);
//			if (end == -1)
//				end = fileName.length;
//	
//			return CharOperation.subarray(fileName, start, end);
//		} else {
//			return this.compilationResult.compilationUnit.getMainTypeName();
//		}
//	}
	
	public void resolve(CompilationUnitScope scope) {
		int startingTypeIndex = 0;
		if (this.types != null /*&& isPackageInfo()*/) {
			// resolve synthetic type declaration
			final TypeDeclaration syntheticTypeDeclaration = this.types[0];
			// set empty javadoc to avoid missing warning (see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=95286)
			if (syntheticTypeDeclaration.javadoc == null) {
				syntheticTypeDeclaration.javadoc = new Javadoc(syntheticTypeDeclaration.declarationSourceStart, syntheticTypeDeclaration.declarationSourceStart);
			}
			syntheticTypeDeclaration.resolve(this.scope);
			/*
			 * resolve javadoc package if any, skip this step if we don't have a valid scope due to an earlier error (bug 252555)
			 * we do it now as the javadoc in the fake type won't be resolved. The peculiar usage of MethodScope to resolve the
			 * package level javadoc is because the CU level resolve method	is a NOP to mimic Javadoc's behavior and can't be used
			 * as such.
			 */
			if (this.javadoc != null && syntheticTypeDeclaration.staticInitializerScope != null) {
				this.javadoc.resolve(syntheticTypeDeclaration.staticInitializerScope);
			}
			startingTypeIndex = 1;
		} else {
			// resolve compilation unit javadoc package if any
			if (this.javadoc != null) {
				this.javadoc.resolve(this.scope);
			}
		}
//		if (this.currentPackage != null && this.currentPackage.annotations != null && !isPackageInfo) {
//			this.scope.problemReporter().invalidFileNameForPackageAnnotations(this.currentPackage.annotations[0]);
//		}
		try {
			if (this.types != null) {
				for (int i = startingTypeIndex, count = this.types.length; i < count; i++) {
					this.types[i].resolve(this.scope);
				}
			}
//			if (!this.compilationResult.hasMandatoryErrors()) checkUnusedImports();
//			reportNLSProblems();
		} catch (AbortCompilationUnit e) {
//			this.ignoreFurtherInvestigation = true;
			return;
		}
	}
	
	/**
	 * javascript generation
	 */
	public void generateCode() {
//		if (this.ignoreFurtherInvestigation) {
//			if (this.types != null) {
//				for (int i = 0, count = this.types.length; i < count; i++) {
//					this.types[i].ignoreFurtherInvestigation = true;
//					// propagate the flag to request problem type creation
//					this.types[i].generateCode(this.scope);
//				}
//			}
//			return;
//		}
//		try {
//			if (this.types != null) {
//				for (int i = 0, count = this.types.length; i < count; i++)
//					this.types[i].generateCode(this.scope);
//			}
//		} catch (AbortCompilationUnit e) {
//			// ignore
//		}
	}

	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		if (this.javadoc != null) {
			this.javadoc.print(indent, output);
		}
		if (this.types != null) {
			for (int i = 0; i < this.types.length; i++) {
				this.types[i].print(indent, output).append("\n"); //$NON-NLS-1$
			}
		}
		return output;
	}

	@Override
	public void abort(int abortLevel, CategorizedProblem problem) {
		switch (abortLevel) {
		case AbortCompilation :
			throw new AbortCompilation(this.compilationResult, problem);
		case AbortCompilationUnit :
			throw new AbortCompilationUnit(this.compilationResult, problem);
		case AbortMethod :
			throw new AbortMethod(this.compilationResult, problem);
		default :
			throw new AbortType(this.compilationResult, problem);
		}
	}

	@Override
	public CompilationResult compilationResult() {
		return this.compilationResult;
	}

	@Override
	public CompilationUnitDeclaration getCompilationUnitDeclaration() {
		if (this.scope != null) {
			return this.scope.compilationUnitScope().referenceContext;
		}
		return null;
	}

	@Override
	public boolean hasErrors() {
		return this.ignoreFurtherInvestigation;
	}

	@Override
	public void tagAsHavingErrors() {
		this.ignoreFurtherInvestigation = true;
		
	}

	@Override
	public void tagAsHavingIgnoredMandatoryErrors(int problemId) {
		// TODO Auto-generated method stub
		
	}
}
