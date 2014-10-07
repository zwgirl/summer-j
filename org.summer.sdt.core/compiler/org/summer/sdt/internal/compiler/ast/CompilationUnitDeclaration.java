/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Stephan Herrmann  - Contribution for bug 295551
 *     Jesper S Moller   - Contributions for
 *							  Bug 405066 - [1.8][compiler][codegen] Implement code generation infrastructure for JSR335             
 *******************************************************************************/
package org.summer.sdt.internal.compiler.ast;

import java.util.Arrays;
import java.util.Comparator;

import org.summer.sdt.core.compiler.CategorizedProblem;
import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.ClassFile;
import org.summer.sdt.internal.compiler.CompilationResult;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.impl.CompilerOptions;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.impl.IrritantSet;
import org.summer.sdt.internal.compiler.impl.ReferenceContext;
import org.summer.sdt.internal.compiler.javascript.Javascript;
import org.summer.sdt.internal.compiler.javascript.JavascriptFile;
import org.summer.sdt.internal.compiler.lookup.CompilationUnitScope;
import org.summer.sdt.internal.compiler.lookup.ImportBinding;
import org.summer.sdt.internal.compiler.lookup.LocalTypeBinding;
import org.summer.sdt.internal.compiler.lookup.MethodScope;
import org.summer.sdt.internal.compiler.lookup.TypeConstants;
import org.summer.sdt.internal.compiler.lookup.TypeIds;
import org.summer.sdt.internal.compiler.parser.NLSTag;
import org.summer.sdt.internal.compiler.problem.AbortCompilationUnit;
import org.summer.sdt.internal.compiler.problem.AbortMethod;
import org.summer.sdt.internal.compiler.problem.AbortType;
import org.summer.sdt.internal.compiler.problem.ProblemReporter;
import org.summer.sdt.internal.compiler.problem.ProblemSeverities;
import org.summer.sdt.internal.compiler.util.HashSetOfInt;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CompilationUnitDeclaration extends ASTNode implements ProblemSeverities, ReferenceContext {

	private static final Comparator STRING_LITERAL_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			StringLiteral literal1 = (StringLiteral) o1;
			StringLiteral literal2 = (StringLiteral) o2;
			return literal1.sourceStart - literal2.sourceStart;
		}
	};
	private static final int STRING_LITERALS_INCREMENT = 10;

	public ImportReference currentPackage;
	public ImportReference[] imports;
	public TypeDeclaration[] types;
	public int[][] comments;

	public boolean ignoreFurtherInvestigation = false; // once pointless to investigate due to errors
	public boolean ignoreMethodBodies = false;
	public CompilationUnitScope scope;
	public ProblemReporter problemReporter;
	public CompilationResult compilationResult;

	public LocalTypeBinding[] localTypes;
	public int localTypeCount = 0;

	public boolean isPropagatingInnerClassEmulation;

	public Javadoc javadoc; // 1.5 addition for package-info.java

	public NLSTag[] nlsTags;
	private StringLiteral[] stringLiterals;
	private int stringLiteralsPtr;
	private HashSetOfInt stringLiteralsStart;

	public boolean[] validIdentityComparisonLines;

	IrritantSet[] suppressWarningIrritants;  // irritant for suppressed warnings
	Annotation[] suppressWarningAnnotations;
	long[] suppressWarningScopePositions; // (start << 32) + end
	int suppressWarningsCount;
	public int functionalExpressionsCount;
	public FunctionalExpression[] functionalExpressions;
	
	public CompilationUnitDeclaration(ProblemReporter problemReporter, CompilationResult compilationResult, int sourceLength) {
		this.problemReporter = problemReporter;
		this.compilationResult = compilationResult;
		//by definition of a compilation unit....
		this.sourceStart = 0;
		this.sourceEnd = sourceLength - 1;
	}
	
	/*
	 *	We cause the compilation task to abort to a given extent.
	 */
	public void abort(int abortLevel, CategorizedProblem problem) {
		switch (abortLevel) {
			case AbortType :
				throw new AbortType(this.compilationResult, problem);
			case AbortMethod :
				throw new AbortMethod(this.compilationResult, problem);
			default :
				throw new AbortCompilationUnit(this.compilationResult, problem);
		}
	}
	
	/*
	 * Dispatch code analysis AND request saturation of inner emulation
	 */
	public void analyseCode() {
		if (this.ignoreFurtherInvestigation)
			return;
		try {
			if (this.types != null) {
				for (int i = 0, count = this.types.length; i < count; i++) {
					this.types[i].analyseCode(this.scope);
				}
			}
			// request inner emulation propagation
			propagateInnerEmulationForAllLocalTypes();
		} catch (AbortCompilationUnit e) {
			this.ignoreFurtherInvestigation = true;
			return;
		}
	}
	
	/*
	 * When unit result is about to be accepted, removed back pointers
	 * to compiler structures.
	 */
	public void cleanUp() {
		if (this.types != null) {
			for (int i = 0, max = this.types.length; i < max; i++) {
				cleanUp(this.types[i]);
			}
			for (int i = 0, max = this.localTypeCount; i < max; i++) {
			    LocalTypeBinding localType = this.localTypes[i];
				// null out the type's scope backpointers
				localType.scope = null; // local members are already in the list
				localType.enclosingCase = null;
			}
		}
	
		this.compilationResult.recoveryScannerData = null; // recovery is already done
	
		ClassFile[] classFiles = this.compilationResult.getClassFiles();
		for (int i = 0, max = classFiles.length; i < max; i++) {
			// clear the classFile back pointer to the bindings
			ClassFile classFile = classFiles[i];
			// null out the classfile backpointer to a type binding
			classFile.referenceBinding = null;
			classFile.innerClassesBindings = null;
			classFile.bootstrapMethods = null;
			classFile.missingTypes = null;
			classFile.visitedTypes = null;
		}
	
		this.suppressWarningAnnotations = null;
	}
	
	private void cleanUp(TypeDeclaration type) {
		if (type.memberTypes != null) {
			for (int i = 0, max = type.memberTypes.length; i < max; i++){
				cleanUp(type.memberTypes[i]);
			}
		}
		if (type.binding != null && type.binding.isAnnotationType())
			this.compilationResult.hasAnnotations = true;
		if (type.binding != null) {
			// null out the type's scope backpointers
			type.binding.scope = null;
		}
	}
	
	public void checkUnusedImports(){
		if (this.scope.imports != null){
			for (int i = 0, max = this.scope.imports.length; i < max; i++){
				ImportBinding importBinding = this.scope.imports[i];
				ImportReference importReference = importBinding.reference;
				if (importReference != null && ((importReference.bits & ASTNode.Used) == 0)){
					this.scope.problemReporter().unusedImport(importReference);
				}
			}
		}
	}
	
	public CompilationResult compilationResult() {
		return this.compilationResult;
	}
	
	public void createPackageInfoType() {
		TypeDeclaration declaration = new TypeDeclaration(this.compilationResult);
		declaration.name = TypeConstants.PACKAGE_INFO_NAME;
		declaration.modifiers = ClassFileConstants.AccDefault | ClassFileConstants.AccInterface;
		declaration.javadoc = this.javadoc;
		this.types[0] = declaration; // Assumes the first slot is meant for this type
	}
	
	/*
	 * Finds the matching type amoung this compilation unit types.
	 * Returns null if no type with this name is found.
	 * The type name is a compound name
	 * e.g. if we're looking for X.A.B then a type name would be {X, A, B}
	 */
	public TypeDeclaration declarationOfType(char[][] typeName) {
		for (int i = 0; i < this.types.length; i++) {
			TypeDeclaration typeDecl = this.types[i].declarationOfType(typeName);
			if (typeDecl != null) {
				return typeDecl;
			}
		}
		return null;
	}
	
	public void finalizeProblems() {
		if (this.suppressWarningsCount == 0) return;
		int removed = 0;
		CategorizedProblem[] problems = this.compilationResult.problems;
		int problemCount = this.compilationResult.problemCount;
		IrritantSet[] foundIrritants = new IrritantSet[this.suppressWarningsCount];
		CompilerOptions options = this.scope.compilerOptions();
		boolean hasMandatoryErrors = false;
		nextProblem: for (int iProblem = 0, length = problemCount; iProblem < length; iProblem++) {
			CategorizedProblem problem = problems[iProblem];
			int problemID = problem.getID();
			int irritant = ProblemReporter.getIrritant(problemID);
			boolean isError = problem.isError();
			if (isError) {
				if (irritant == 0) {
					// tolerate unused warning tokens when mandatory errors
					hasMandatoryErrors = true;
					continue;
				}
				if (!options.suppressOptionalErrors) {
					continue;
				}
			}
			int start = problem.getSourceStart();
			int end = problem.getSourceEnd();
			nextSuppress: for (int iSuppress = 0, suppressCount = this.suppressWarningsCount; iSuppress < suppressCount; iSuppress++) {
				long position = this.suppressWarningScopePositions[iSuppress];
				int startSuppress = (int) (position >>> 32);
				int endSuppress = (int) position;
				if (start < startSuppress) continue nextSuppress;
				if (end > endSuppress) continue nextSuppress;
				if (!this.suppressWarningIrritants[iSuppress].isSet(irritant))
					continue nextSuppress;
				// discard suppressed warning
				removed++;
				problems[iProblem] = null;
				this.compilationResult.removeProblem(problem);
				if (foundIrritants[iSuppress] == null){
					foundIrritants[iSuppress] = new IrritantSet(irritant);
				} else {
					foundIrritants[iSuppress].set(irritant);
				}
				continue nextProblem;
			}
		}
		// compact remaining problems
		if (removed > 0) {
			for (int i = 0, index = 0; i < problemCount; i++) {
				CategorizedProblem problem;
				if ((problem = problems[i]) != null) {
					if (i > index) {
						problems[index++] = problem;
					} else {
						index++;
					}
				}
			}
		}
		// flag SuppressWarnings which had no effect (only if no (mandatory) error got detected within unit
		if (!hasMandatoryErrors) {
			int severity = options.getSeverity(CompilerOptions.UnusedWarningToken);
			if (severity != ProblemSeverities.Ignore) {
				boolean unusedWarningTokenIsWarning = (severity & ProblemSeverities.Error) == 0;
				for (int iSuppress = 0, suppressCount = this.suppressWarningsCount; iSuppress < suppressCount; iSuppress++) {
					Annotation annotation = this.suppressWarningAnnotations[iSuppress];
					if (annotation == null) continue; // implicit annotation
					IrritantSet irritants = this.suppressWarningIrritants[iSuppress];
					if (unusedWarningTokenIsWarning && irritants.areAllSet()) continue; // @SuppressWarnings("all") also suppresses unused warning token
					if (irritants != foundIrritants[iSuppress]) { // mismatch, some warning tokens were unused
						MemberValuePair[] pairs = annotation.memberValuePairs();
						pairLoop: for (int iPair = 0, pairCount = pairs.length; iPair < pairCount; iPair++) {
							MemberValuePair pair = pairs[iPair];
							if (CharOperation.equals(pair.name, TypeConstants.VALUE)) {
								Expression value = pair.value;
								if (value instanceof ArrayInitializer) {
									ArrayInitializer initializer = (ArrayInitializer) value;
									Expression[] inits = initializer.expressions;
									if (inits != null) {
										for (int iToken = 0, tokenCount = inits.length; iToken < tokenCount; iToken++) {
											Constant cst = inits[iToken].constant;
											if (cst != Constant.NotAConstant && cst.typeID() == TypeIds.T_JavaLangString) {
												IrritantSet tokenIrritants = CompilerOptions.warningTokenToIrritants(cst.stringValue());
												if (tokenIrritants != null
														&& !tokenIrritants.areAllSet() // no complaint against @SuppressWarnings("all")
														&& options.isAnyEnabled(tokenIrritants) // if irritant is effectively enabled
														&& (foundIrritants[iSuppress] == null || !foundIrritants[iSuppress].isAnySet(tokenIrritants))) { // if irritant had no matching problem
													if (unusedWarningTokenIsWarning) {
														int start = value.sourceStart, end = value.sourceEnd;
														nextSuppress: for (int jSuppress = iSuppress - 1; jSuppress >= 0; jSuppress--) {
															long position = this.suppressWarningScopePositions[jSuppress];
															int startSuppress = (int) (position >>> 32);
															int endSuppress = (int) position;
															if (start < startSuppress) continue nextSuppress;
															if (end > endSuppress) continue nextSuppress;
															if (this.suppressWarningIrritants[jSuppress].areAllSet()) break pairLoop; // suppress all?
														}
													}
													this.scope.problemReporter().unusedWarningToken(inits[iToken]);
												}
											}
										}
									}
								} else {
									Constant cst = value.constant;
									if (cst != Constant.NotAConstant && cst.typeID() == T_JavaLangString) {
										IrritantSet tokenIrritants = CompilerOptions.warningTokenToIrritants(cst.stringValue());
										if (tokenIrritants != null
												&& !tokenIrritants.areAllSet() // no complaint against @SuppressWarnings("all")
												&& options.isAnyEnabled(tokenIrritants) // if irritant is effectively enabled
												&& (foundIrritants[iSuppress] == null || !foundIrritants[iSuppress].isAnySet(tokenIrritants))) { // if irritant had no matching problem
											if (unusedWarningTokenIsWarning) {
												int start = value.sourceStart, end = value.sourceEnd;
												nextSuppress: for (int jSuppress = iSuppress - 1; jSuppress >= 0; jSuppress--) {
													long position = this.suppressWarningScopePositions[jSuppress];
													int startSuppress = (int) (position >>> 32);
													int endSuppress = (int) position;
													if (start < startSuppress) continue nextSuppress;
													if (end > endSuppress) continue nextSuppress;
													if (this.suppressWarningIrritants[jSuppress].areAllSet()) break pairLoop; // suppress all?
												}
											}
											this.scope.problemReporter().unusedWarningToken(value);
										}
									}
								}
								break pairLoop;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Bytecode generation
	 */
	public void generateCode() {
		if (this.ignoreFurtherInvestigation) {
			if (this.types != null) {
				for (int i = 0, count = this.types.length; i < count; i++) {
					this.types[i].ignoreFurtherInvestigation = true;
					// propagate the flag to request problem type creation
					this.types[i].generateCode(this.scope);
				}
			}
			return;
		}
		try {
			if (this.types != null) {
				for (int i = 0, count = this.types.length; i < count; i++)
					this.types[i].generateCode(this.scope);
			}
		} catch (AbortCompilationUnit e) {
			// ignore
		}
		
		generateJavascript();   //cym add
	}
	
	//cym add
	/**
	 * javascript generation
	 */
	public void generateJavascript() {
		compilationResult.javascriptFile = new JavascriptFile();
		StringBuffer buffer = compilationResult.javascriptFile.content;
		
		generateAMDHeader(buffer);
		
		if (this.ignoreFurtherInvestigation) {
			if (this.types != null) {
				for (int i = 0, count = this.types.length; i < count; i++) {
					this.types[i].ignoreFurtherInvestigation = true;
					// propagate the flag to request problem type creation
					this.types[i].generateJavascript(this.scope, 0, buffer);
				}
			}
			return;
		}
		try {
			if (this.types != null) {
				for (int i = 0, count = this.types.length; i < count; i++)
//					this.types[i].generateJavascript(this.scope, 0, buffer);
				generateModuleBody(buffer, 1, this.types[i]);
			}
		} catch (AbortCompilationUnit e) {
			// ignore
		}
		
		if (this.types != null) {
			generateExportObject(buffer, this.types[0]);
		}
		
		buffer.append(Javascript.CR);
		buffer.append(Javascript.RPAREN).append(Javascript.RBRACE).append(Javascript.SEMICOLON);
	}
	
	private void generateExportObject(StringBuffer buffer, TypeDeclaration type) {
		buffer.append(Javascript.CR);
		buffer.append(Javascript.RETURN).append(Javascript.WHITESPACE).append(Javascript.LBRACE);
		
		if(type.fields != null){
			for(FieldDeclaration field : type.fields){
				if(field instanceof Initializer){
					continue;
				}
				buffer.append(Javascript.CR);
				buffer.append(field.name).append(Javascript.WHITESPACE).append(Javascript.COLON).append(Javascript.WHITESPACE);
				buffer.append(field.name);
				buffer.append(Javascript.COMMA);
			}
		}

		if(type.methods != null){
			for(AbstractMethodDeclaration method : type.methods){
				buffer.append(Javascript.CR);
				buffer.append(method.selector).append(Javascript.WHITESPACE).append(Javascript.COLON).append(Javascript.WHITESPACE);
				buffer.append(method.selector);
				buffer.append(Javascript.COMMA);
			}
		}
		
		buffer.append(Javascript.CR);
		buffer.append(Javascript.RBRACE).append(Javascript.SEMICOLON);
	}

	private void generateModuleBody(StringBuffer buffer, int indent, TypeDeclaration type){
		if(type.fields != null){
			for(FieldDeclaration field : type.fields){
				if(field instanceof Initializer){
					continue;
				}
				buffer.append(Javascript.CR);
				if(field.isFinal())
					buffer.append(Javascript.CONST);
				else 
					buffer.append(Javascript.VAR);
				buffer.append(Javascript.WHITESPACE).append(field.name);
				
				if(field.initialization != null){
					buffer.append(Javascript.WHITESPACE).append(Javascript.EQUAL).append(Javascript.WHITESPACE);
					field.initialization.generateJavascript(scope, 0, buffer);
				}
				buffer.append(Javascript.SEMICOLON);
			}
		}

		if(type.methods != null){
			for(AbstractMethodDeclaration method : type.methods){
				buffer.append(Javascript.CR);
				buffer.append(Javascript.FUNCTION).append(Javascript.WHITESPACE).append(method.selector).append(Javascript.LPAREN);
				if(method.arguments != null){
					boolean commaSet = false;
					for(Argument arg : method.arguments){
						if(commaSet){
							buffer.append(Javascript.COMMA);
						}
						buffer.append(arg.name);
					}
				}
				
				buffer.append(Javascript.RPAREN).append(Javascript.LBRACE);
				buffer.append(Javascript.CR);
				
				buffer.append(Javascript.RBRACE);
			}
		}
		
		if(type.memberTypes != null){
			for(TypeDeclaration member : type.memberTypes){
				member.generateJavascript(scope, indent, buffer);
			}
		}

	}
	
	private void generateAMDHeader(StringBuffer buffer) {
		buffer.append(Javascript.DEFINE).append(Javascript.LPAREN).append(Javascript.DOUBLE_QUOTE).append(Javascript.DOUBLE_QUOTE);
		buffer.append(Javascript.COMMA).append(Javascript.WHITESPACE);
		buffer.append(Javascript.LBRACKET);
		
		boolean commaSet = false;
		if(imports != null){
			for(ImportReference imp : imports){
				if(commaSet){
					buffer.append(Javascript.COMMA).append(Javascript.WHITESPACE);
				}
				buffer.append(Javascript.DOUBLE_QUOTE);
				for (int i = 0; i < imp.tokens.length; i++) {
					if (i > 0) buffer.append('.');
					buffer.append(imp.tokens[i]);
				}
				buffer.append(Javascript.DOUBLE_QUOTE);
				
				commaSet = true;
			}
		}

		buffer.append(Javascript.RBRACKET);
		buffer.append(Javascript.COMMA).append(Javascript.WHITESPACE);;
		buffer.append(Javascript.FUNCTION).append(Javascript.LPAREN);
		
		if(imports != null){
			commaSet = false;
			for(ImportReference imp : imports){
				if(commaSet){
					buffer.append(Javascript.COMMA).append(Javascript.WHITESPACE);
				}
				buffer.append(imp.tokens[imp.tokens.length - 1]);
				commaSet = true;
			}
		}
		buffer.append(Javascript.RPAREN).append(Javascript.LBRACE);
	}

//	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
//	    JvmModule module = (JvmModule) input.getContents().get(0);
//		GeneratorConfig config = this.generatorConfigProvider.get(module);
//		ImportManager importManager = new ImportManager(true, module);
//		
//		final TreeAppendable header = this.createAppendable(module, importManager);
//		header.append("define([\'org.summer.lang\'");
//		
//		final TreeAppendable importAppend = this.createAppendable(module, importManager);
//		
//		TreeAppendable body  = this.createAppendable(module, null);
//		body.append(", function(lang");
//		
//		XImportSection importSection = module.getImportSection();
//		boolean varFlag = false, comma = false;;
//		if(importSection != null){
//		    for(XImportDeclaration decl : importSection.getImportDeclarations()){
//		    	header.append(", ");
//		    	header.append("\'").append(decl.getImportedNamespace()).append("\'");
//		    	body.append(", ");
//		    	body.append(decl.getModuleName());
//		    	
//		    	for(XImportItem item : decl.getImportItems()){
//		    		if(!varFlag){
//		    			importAppend.append("var ");
//		    			varFlag = true;
//		    		}
//		    		if(comma){
//		    			importAppend.append(", ");
//		    		}
//		    		importAppend.append(item.getIdentifier()).append("=")
//		    			.append(decl.getIdentifier()).append(".")
//		    			.append(item.getImportedId().getSimpleName());
//					comma = true;
//		    	}
//		    }
//		}
//		
//		if(varFlag){
//			importAppend.append(";");
//		}
//		
//		header.append("]");
//		body.append(") {");
//		body.increaseIndentation();
//		body.newLine();
//		body.append(importAppend);
//		
//		List<JvmIdentifiableElement> exportObjects = new LinkedList<JvmIdentifiableElement>();
//		for (final EObject obj : module.getContents()) {
//			if(obj instanceof XVariableDeclarationList){
//				XVariableDeclarationList declList = (XVariableDeclarationList) obj;
//				if(declList.isExported()){
//					for(XExpression varDecl : declList.getDeclarations()){
//		    			exportObjects.add((JvmIdentifiableElement) varDecl);
//					}
//				}
//			}else if (obj instanceof JvmDeclaredType){
//				JvmDeclaredType jvmType = (JvmDeclaredType) obj;
//				if(jvmType.isExported()){
//					exportObjects.add(jvmType);
//				}
//			} else if(obj instanceof XFunctionDeclaration){
//				XFunctionDeclaration function = (XFunctionDeclaration) obj;
//				if(function.isExported()){
//					exportObjects.add(function);
//				}
//			}
//			this.internalDoGenerate(obj, body);
//		}
//		
//		XExportSection exportSection = module.getExportSection();
//		if(exportSection != null){
//		    for(XExportDeclaration expDecl :exportSection.getExportDeclarations()){
//		    	List<XExportItem> items = expDecl.getExportItems();
//		    	for(XExportItem item : items){
//		    		exportObjects.add(item.getExportedId());
//		    	}
//		    }
//		}
//		
//		body.newLine();
//		body.append("return {");
//		body.increaseIndentation();
//		boolean comaFlag = false;
//		for(JvmIdentifiableElement idElement : exportObjects){
//			if(comaFlag){
//				body.append(",");
//				body.newLine();
//			}
//			comaFlag = true;
//			
//			body.append(idElement.getSimpleName()).append(":").append(idElement.getSimpleName());
//		}
//		body.decreaseIndentation();
//		body.newLine();
//		body.append("};");
//		
//		body.decreaseIndentation();
//		body.newLine();
//		body.append("});");
//		
//	 	String fileName = input.getURI().lastSegment();
//	 	String jsFile = fileName + ".js";
//		
//	 	header.append(body);
//	 	if(module.getRoot()!=null){
//	 		TreeAppendable uiAppend = this.createAppendable(module, importManager);
//	 		generateUI(uiAppend, module.getRoot());
//	 		
//	 		header.append(uiAppend);
//	 		
//	 	}
//		 
//		fsa.generateFile(jsFile, header);
//		
//		generateHtml(fsa, module, importManager, fileName);
//  	}
////	(function(){}){
////		銆��var t = new Person1();
////		銆��t.begin();
////		銆��t.setName();
////		銆��t.setAge();
////		銆��t.content = (function(){
////		銆��var t = new XXX():
////		銆��t.beginInit();
////		銆��t.xxx = ;
////		銆��...
////		銆��t.AdChild((function(){
////		銆��銆傘�銆�////		銆��})锛堬級)
////		銆��t.endInit();
////		銆��})()
////		銆��addChild();
////		銆��for(){
////		銆��
////		銆��}
////		}
//	private void generateUI(TreeAppendable b, XElement ele) {
//		b.newLine();
//		b.append("(function(){");
//		b.increaseIndentation();
//		b.newLine();
//		b.append("var t = new ").append(ele.getType().getSimpleName()).append("();").newLine();
//		for(XAbstractAttribute attribute : ele.getProperties()){
//			b.append("t.");
//			b.append(attribute.getField().getSimpleName()).append(" = ");
//			compiler.toJavaExpression(attribute.getValue(), b);
//			b.append(";").newLine();
//		}
//		
//		for(XElement element : ele.getContents()){
//			if(element instanceof XAttributeElement){
//				XAttributeElement attrEle = (XAttributeElement) element;
//				b.append("t.");
//				b.append(attrEle.getField().getSimpleName()).append(" = ");
//				
//				generateUI(b, attrEle.getContents().get(0));
//			}
//		}
//		b.decreaseIndentation();
//		b.newLine();
//		b.append("})();");
//	}
//
//
//	private void generateHtml(final IFileSystemAccess fsa, JvmModule module,
//			ImportManager importManager, String fileName) {
//		XObjectElement rootUi = module.getRoot();
//		if(rootUi!=null){
//			final TreeAppendable html = this.createAppendable(module, importManager);
//			html.append("<html>");
//			html.increaseIndentation();
//			html.newLine();
//			html.append("<head>");
//			html.increaseIndentation();
//			html.newLine();
//			html.append("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
//			html.newLine();
//			html.append("<script src='dojo/lsjs.js' data-dojo-config='async: true'></script>");
//			html.newLine();
//			html.append("<script src='dojo/IndexedDBStorage.js'></script>");
//			html.decreaseIndentation();
//			html.newLine();
//			html.append("</head>");
//			html.newLine();
//			html.append("<body>");
//			html.newLine();
//			html.append("</body>");
//			html.decreaseIndentation();
//			html.newLine();
//			html.append("</html>");
//			String htmlFile = fileName + ".html";
//			fsa.generateFile(htmlFile, html);
//		}
//	}
	
	public CompilationUnitDeclaration getCompilationUnitDeclaration() {
		return this;
	}
	
	public char[] getFileName() {
		return this.compilationResult.getFileName();
	}
	
	public char[] getMainTypeName() {
		if (this.compilationResult.compilationUnit == null) {
			char[] fileName = this.compilationResult.getFileName();
	
			int start = CharOperation.lastIndexOf('/', fileName) + 1;
			if (start == 0 || start < CharOperation.lastIndexOf('\\', fileName))
				start = CharOperation.lastIndexOf('\\', fileName) + 1;
	
			int end = CharOperation.lastIndexOf('.', fileName);
			if (end == -1)
				end = fileName.length;
	
			return CharOperation.subarray(fileName, start, end);
		} else {
			return this.compilationResult.compilationUnit.getMainTypeName();
		}
	}
	
	public boolean isEmpty() {
		return (this.currentPackage == null) && (this.imports == null) && (this.types == null);
	}
	
	public boolean isPackageInfo() {
		return CharOperation.equals(getMainTypeName(), TypeConstants.PACKAGE_INFO_NAME);
	}
	
	public boolean isSuppressed(CategorizedProblem problem) {
		if (this.suppressWarningsCount == 0) return false;
		int irritant = ProblemReporter.getIrritant(problem.getID());
		if (irritant == 0) return false;
		int start = problem.getSourceStart();
		int end = problem.getSourceEnd();
		nextSuppress: for (int iSuppress = 0, suppressCount = this.suppressWarningsCount; iSuppress < suppressCount; iSuppress++) {
			long position = this.suppressWarningScopePositions[iSuppress];
			int startSuppress = (int) (position >>> 32);
			int endSuppress = (int) position;
			if (start < startSuppress) continue nextSuppress;
			if (end > endSuppress) continue nextSuppress;
			if (this.suppressWarningIrritants[iSuppress].isSet(irritant))
				return true;
		}
		return false;
	}
	
	public boolean hasFunctionalTypes() {
		return this.compilationResult.hasFunctionalTypes;
	}
	
	public boolean hasErrors() {
		return this.ignoreFurtherInvestigation;
	}
	
	public StringBuffer print(int indent, StringBuffer output) {
		if (this.currentPackage != null) {
			printIndent(indent, output).append("package "); //$NON-NLS-1$
			this.currentPackage.print(0, output, false).append(";\n"); //$NON-NLS-1$
		}
		if (this.imports != null)
			for (int i = 0; i < this.imports.length; i++) {
				printIndent(indent, output).append("import "); //$NON-NLS-1$
				ImportReference currentImport = this.imports[i];
				if (currentImport.isStatic()) {
					output.append("static "); //$NON-NLS-1$
				}
				currentImport.print(0, output).append(";\n"); //$NON-NLS-1$
			}
		
		if (this.types != null) {
			for (int i = 0; i < this.types.length; i++) {
				this.types[i].print(indent, output).append("\n"); //$NON-NLS-1$
			}
		}
		return output;
	}
	
	/*
	 * Force inner local types to update their innerclass emulation
	 */
	public void propagateInnerEmulationForAllLocalTypes() {
		this.isPropagatingInnerClassEmulation = true;
		for (int i = 0, max = this.localTypeCount; i < max; i++) {
			LocalTypeBinding localType = this.localTypes[i];
			// only propagate for reachable local types
			if ((localType.scope.referenceType().bits & IsReachable) != 0) {
				localType.updateInnerEmulationDependents();
			}
		}
	}
	
	public void recordStringLiteral(StringLiteral literal, boolean fromRecovery) {
		if (this.stringLiteralsStart != null) {
			if (this.stringLiteralsStart.contains(literal.sourceStart)) return;
			this.stringLiteralsStart.add(literal.sourceStart);
		} else if (fromRecovery) {
			this.stringLiteralsStart = new HashSetOfInt(this.stringLiteralsPtr + STRING_LITERALS_INCREMENT);
			for (int i = 0; i < this.stringLiteralsPtr; i++) {
				this.stringLiteralsStart.add(this.stringLiterals[i].sourceStart);
			}
	
			if (this.stringLiteralsStart.contains(literal.sourceStart)) return;
			this.stringLiteralsStart.add(literal.sourceStart);
		}
	
		if (this.stringLiterals == null) {
			this.stringLiterals = new StringLiteral[STRING_LITERALS_INCREMENT];
			this.stringLiteralsPtr = 0;
		} else {
			int stackLength = this.stringLiterals.length;
			if (this.stringLiteralsPtr == stackLength) {
				System.arraycopy(
					this.stringLiterals,
					0,
					this.stringLiterals = new StringLiteral[stackLength + STRING_LITERALS_INCREMENT],
					0,
					stackLength);
			}
		}
		this.stringLiterals[this.stringLiteralsPtr++] = literal;
	}
	
	public void recordSuppressWarnings(IrritantSet irritants, Annotation annotation, int scopeStart, int scopeEnd) {
		if (this.suppressWarningIrritants == null) {
			this.suppressWarningIrritants = new IrritantSet[3];
			this.suppressWarningAnnotations = new Annotation[3];
			this.suppressWarningScopePositions = new long[3];
		} else if (this.suppressWarningIrritants.length == this.suppressWarningsCount) {
			System.arraycopy(this.suppressWarningIrritants, 0,this.suppressWarningIrritants = new IrritantSet[2*this.suppressWarningsCount], 0, this.suppressWarningsCount);
			System.arraycopy(this.suppressWarningAnnotations, 0,this.suppressWarningAnnotations = new Annotation[2*this.suppressWarningsCount], 0, this.suppressWarningsCount);
			System.arraycopy(this.suppressWarningScopePositions, 0,this.suppressWarningScopePositions = new long[2*this.suppressWarningsCount], 0, this.suppressWarningsCount);
		}
		final long scopePositions = ((long)scopeStart<<32) + scopeEnd;
		for (int i = 0, max = this.suppressWarningsCount; i < max; i++) {
			if (this.suppressWarningAnnotations[i] == annotation
					&& this.suppressWarningScopePositions[i] == scopePositions
					&& this.suppressWarningIrritants[i].hasSameIrritants(irritants)) {
				// annotation data already recorded
				return;
			}
		}
		this.suppressWarningIrritants[this.suppressWarningsCount] = irritants;
		this.suppressWarningAnnotations[this.suppressWarningsCount] = annotation;
		this.suppressWarningScopePositions[this.suppressWarningsCount++] = scopePositions;
	}
	
	/*
	 * Keep track of all local types, so as to update their innerclass
	 * emulation later on.
	 */
	public void record(LocalTypeBinding localType) {
		if (this.localTypeCount == 0) {
			this.localTypes = new LocalTypeBinding[5];
		} else if (this.localTypeCount == this.localTypes.length) {
			System.arraycopy(this.localTypes, 0, (this.localTypes = new LocalTypeBinding[this.localTypeCount * 2]), 0, this.localTypeCount);
		}
		this.localTypes[this.localTypeCount++] = localType;
	}
	
	/*
	 * Keep track of all lambda/method reference expressions, so as to be able to look it up later without 
	 * having to traverse AST. Return the 1 based "ordinal" in the CUD.
	 */
	public int record(FunctionalExpression expression) {
		if (this.functionalExpressionsCount == 0) {
			this.functionalExpressions = new FunctionalExpression[5];
		} else if (this.functionalExpressionsCount == this.functionalExpressions.length) {
			System.arraycopy(this.functionalExpressions, 0, (this.functionalExpressions = new FunctionalExpression[this.functionalExpressionsCount * 2]), 0, this.functionalExpressionsCount);
		}
		this.functionalExpressions[this.functionalExpressionsCount] = expression;
		return ++this.functionalExpressionsCount;
	}
	
	public void resolve() {
		int startingTypeIndex = 0;
		boolean isPackageInfo = isPackageInfo();
		if (this.types != null && isPackageInfo) {
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
		if (this.currentPackage != null && this.currentPackage.annotations != null && !isPackageInfo) {
			this.scope.problemReporter().invalidFileNameForPackageAnnotations(this.currentPackage.annotations[0]);
		}
		try {
			if (this.types != null) {
				for (int i = startingTypeIndex, count = this.types.length; i < count; i++) {
					this.types[i].resolve(this.scope);
				}
			}
			
			if (!this.compilationResult.hasMandatoryErrors()) checkUnusedImports();
			reportNLSProblems();
		} catch (AbortCompilationUnit e) {
			this.ignoreFurtherInvestigation = true;
			return;
		}
	}
	
	
	private void reportNLSProblems() {
		if (this.nlsTags != null || this.stringLiterals != null) {
			final int stringLiteralsLength = this.stringLiteralsPtr;
			final int nlsTagsLength = this.nlsTags == null ? 0 : this.nlsTags.length;
			if (stringLiteralsLength == 0) {
				if (nlsTagsLength != 0) {
					for (int i = 0; i < nlsTagsLength; i++) {
						NLSTag tag = this.nlsTags[i];
						if (tag != null) {
							this.scope.problemReporter().unnecessaryNLSTags(tag.start, tag.end);
						}
					}
				}
			} else if (nlsTagsLength == 0) {
				// resize string literals
				if (this.stringLiterals.length != stringLiteralsLength) {
					System.arraycopy(this.stringLiterals, 0, (this.stringLiterals = new StringLiteral[stringLiteralsLength]), 0, stringLiteralsLength);
				}
				Arrays.sort(this.stringLiterals, STRING_LITERAL_COMPARATOR);
				for (int i = 0; i < stringLiteralsLength; i++) {
					this.scope.problemReporter().nonExternalizedStringLiteral(this.stringLiterals[i]);
				}
			} else {
				// need to iterate both arrays to find non matching elements
				if (this.stringLiterals.length != stringLiteralsLength) {
					System.arraycopy(this.stringLiterals, 0, (this.stringLiterals = new StringLiteral[stringLiteralsLength]), 0, stringLiteralsLength);
				}
				Arrays.sort(this.stringLiterals, STRING_LITERAL_COMPARATOR);
				int indexInLine = 1;
				int lastLineNumber = -1;
				StringLiteral literal = null;
				int index = 0;
				int i = 0;
				stringLiteralsLoop: for (; i < stringLiteralsLength; i++) {
					literal = this.stringLiterals[i];
					final int literalLineNumber = literal.lineNumber;
					if (lastLineNumber != literalLineNumber) {
						indexInLine = 1;
						lastLineNumber = literalLineNumber;
					} else {
						indexInLine++;
					}
					if (index < nlsTagsLength) {
						nlsTagsLoop: for (; index < nlsTagsLength; index++) {
							NLSTag tag = this.nlsTags[index];
							if (tag == null) continue nlsTagsLoop;
							int tagLineNumber = tag.lineNumber;
							if (literalLineNumber < tagLineNumber) {
								this.scope.problemReporter().nonExternalizedStringLiteral(literal);
								continue stringLiteralsLoop;
							} else if (literalLineNumber == tagLineNumber) {
								if (tag.index == indexInLine) {
									this.nlsTags[index] = null;
									index++;
									continue stringLiteralsLoop;
								} else {
									nlsTagsLoop2: for (int index2 = index + 1; index2 < nlsTagsLength; index2++) {
										NLSTag tag2 = this.nlsTags[index2];
										if (tag2 == null) continue nlsTagsLoop2;
										int tagLineNumber2 = tag2.lineNumber;
										if (literalLineNumber == tagLineNumber2) {
											if (tag2.index == indexInLine) {
												this.nlsTags[index2] = null;
												continue stringLiteralsLoop;
											} else {
												continue nlsTagsLoop2;
											}
										} else {
											this.scope.problemReporter().nonExternalizedStringLiteral(literal);
											continue stringLiteralsLoop;
										}
									}
									this.scope.problemReporter().nonExternalizedStringLiteral(literal);
									continue stringLiteralsLoop;
								}
							} else {
								this.scope.problemReporter().unnecessaryNLSTags(tag.start, tag.end);
								continue nlsTagsLoop;
							}
						}
					}
					// all nls tags have been processed, so remaining string literals are not externalized
					break stringLiteralsLoop;
				}
				for (; i < stringLiteralsLength; i++) {
					this.scope.problemReporter().nonExternalizedStringLiteral(this.stringLiterals[i]);
				}
				if (index < nlsTagsLength) {
					for (; index < nlsTagsLength; index++) {
						NLSTag tag = this.nlsTags[index];
						if (tag != null) {
							this.scope.problemReporter().unnecessaryNLSTags(tag.start, tag.end);
						}
					}
				}
			}
		}
	}
	
	public void tagAsHavingErrors() {
		this.ignoreFurtherInvestigation = true;
	}
	
	public void tagAsHavingIgnoredMandatoryErrors(int problemId) {
		// Nothing to do for this context;
	}
	
	public void traverse(ASTVisitor visitor, CompilationUnitScope unitScope) {
		traverse(visitor, unitScope, true);
	}
	public void traverse(ASTVisitor visitor, CompilationUnitScope unitScope, boolean skipOnError) {
		if (skipOnError && this.ignoreFurtherInvestigation)
			return;
		try {
			if (visitor.visit(this, this.scope)) {
				if (this.types != null && isPackageInfo()) {
		            // resolve synthetic type declaration
					final TypeDeclaration syntheticTypeDeclaration = this.types[0];
					// resolve javadoc package if any
					final MethodScope methodScope = syntheticTypeDeclaration.staticInitializerScope;
					// Don't traverse in null scope and invite trouble a la bug 252555.
					if (this.javadoc != null && methodScope != null) {
						this.javadoc.traverse(visitor, methodScope);
					}
					// Don't traverse in null scope and invite trouble a la bug 252555.
					if (this.currentPackage != null && methodScope != null) {
						final Annotation[] annotations = this.currentPackage.annotations;
						if (annotations != null) {
							int annotationsLength = annotations.length;
							for (int i = 0; i < annotationsLength; i++) {
								annotations[i].traverse(visitor, methodScope);
							}
						}
					}
				}
				if (this.currentPackage != null) {
					this.currentPackage.traverse(visitor, this.scope);
				}
				if (this.imports != null) {
					int importLength = this.imports.length;
					for (int i = 0; i < importLength; i++) {
						this.imports[i].traverse(visitor, this.scope);
					}
				}
				if (this.types != null) {
					int typesLength = this.types.length;
					for (int i = 0; i < typesLength; i++) {
						this.types[i].traverse(visitor, this.scope);
					}
				}
			}
			visitor.endVisit(this, this.scope);
		} catch (AbortCompilationUnit e) {
			// ignore
		}
	}
}
