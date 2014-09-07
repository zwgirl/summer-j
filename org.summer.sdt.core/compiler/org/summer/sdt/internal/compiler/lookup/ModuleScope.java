package org.summer.sdt.internal.compiler.lookup;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.ast.ModuleDeclaration;
import org.summer.sdt.internal.compiler.ast.Statement;
import org.summer.sdt.internal.compiler.ast.TypeDeclaration;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.env.AccessRestriction;

/**
 * 
 * @author cym
 *
 */
public class ModuleScope extends BlockScope {
	public ModuleDeclaration referenceContext;
	
	public SourceTypeBinding[] topLevelTypes = new SourceTypeBinding[0];
	
	public FunctionBinding[] functionBindings;
	
	public ModuleScope(Scope parent, ModuleDeclaration referenceContext){
		this(MODULE_SCOPE, parent);
		this.referenceContext = referenceContext;
		this.locals = new LocalVariableBinding[5];
	}
	
	protected ModuleScope(int kind, Scope parent) {
		super(kind, parent);
	}

	public ModuleBinding buildBindings(AccessRestriction accessRestriction) {
		// Skip typeDeclarations which know of previously reported errors
//		TypeDeclaration[] types = this.referenceContext.types;
//		int typeLength = (types == null) ? 0 : types.length;
//		this.topLevelTypes = new SourceTypeBinding[typeLength];
//		int count = 0;
//		nextType: for (int i = 0; i < typeLength; i++) {
//			TypeDeclaration typeDecl = types[i];
//			if (this.environment.isProcessingAnnotations && this.environment.isMissingType(typeDecl.name))
//				throw new SourceTypeCollisionException(); // resolved a type ref before APT generated the type
//			ReferenceBinding typeBinding = this.fPackage.getType0(typeDecl.name);
//			recordSimpleReference(typeDecl.name); // needed to detect collision cases
//			if (typeBinding != null && typeBinding.isValidBinding() && !(typeBinding instanceof UnresolvedReferenceBinding)) {
//				// if its an unresolved binding - its fixed up whenever its needed, see UnresolvedReferenceBinding.resolve()
//				if (this.environment.isProcessingAnnotations)
//					throw new SourceTypeCollisionException(); // resolved a type ref before APT generated the type
//				// if a type exists, check that its a valid type
//				// it can be a NotFound problem type if its a secondary type referenced before its primary type found in additional units
//				// and it can be an unresolved type which is now being defined
//				problemReporter().duplicateTypes(this.referenceContext, typeDecl);
//				continue nextType;
//			}
//			if (this.fPackage != this.environment.defaultPackage && this.fPackage.getPackage(typeDecl.name) != null) {
//				// if a package exists, it must be a valid package - cannot be a NotFound problem package
//				// this is now a warning since a package does not really 'exist' until it contains a type, see JLS v2, 7.4.3
//				problemReporter().typeCollidesWithPackage(this.referenceContext, typeDecl);
//			}
//	
//			if ((typeDecl.modifiers & ClassFileConstants.AccPublic) != 0) {
//				char[] mainTypeName;
//				if ((mainTypeName = this.referenceContext.getMainTypeName()) != null // mainTypeName == null means that implementor of ICompilationUnit decided to return null
//						&& !CharOperation.equals(mainTypeName, typeDecl.name)) {
//					problemReporter().publicClassMustMatchFileName(this.referenceContext, typeDecl);
//					// tolerate faulty main type name (91091), allow to proceed into type construction
//				}
//			}
//	
//			ClassScope child = new ClassScope(this, typeDecl);
//			SourceTypeBinding type = child.buildType(null, this.fPackage, accessRestriction);
//			if (firstIsSynthetic && i == 0)
//				type.modifiers |= ClassFileConstants.AccSynthetic;
//			if (type != null)
//				this.topLevelTypes[count++] = type;
//		}
//	
//		// shrink topLevelTypes... only happens if an error was reported
//		if (count != this.topLevelTypes.length)
//			System.arraycopy(this.topLevelTypes, 0, this.topLevelTypes = new SourceTypeBinding[count], 0, count);
		referenceContext.scope = this;
		CompilationUnitScope parentScope = (CompilationUnitScope) parent;
		if(referenceContext != null){
			if(referenceContext.statements != null){
				Statement[] statements = referenceContext.statements;
				for(int i =0, length = statements.length;  i < length; i++){
					if(statements[i] instanceof TypeDeclaration){
						ClassScope child = new ClassScope(this, (TypeDeclaration) statements[i]);
						int count = topLevelTypes.length;
						System.arraycopy(topLevelTypes, 0, topLevelTypes = new SourceTypeBinding[count+1], 0, count);
						topLevelTypes[count] = child.buildType(null, parentScope.fPackage, accessRestriction);
					}
				}
			}
		}
		
		ModuleBinding result = new ModuleBinding();
		referenceContext.binding = result;
		result.scope = this;
		
		return result;
	}
	
	void buildFieldsAndMethods() {
		for (int i = 0, length = this.topLevelTypes.length; i < length; i++)
			this.topLevelTypes[i].scope.buildFieldsAndMethods();
	}

}
