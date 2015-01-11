package org.summer.sdt.internal.compiler.javascript;

import java.util.HashMap;
import java.util.Map;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.summer.sdt.internal.compiler.ast.QualifiedTypeReference;
import org.summer.sdt.internal.compiler.ast.SingleTypeReference;
import org.summer.sdt.internal.compiler.ast.TypeDeclaration;
import org.summer.sdt.internal.compiler.ast.TypeReference;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.CompilationUnitScope;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeVariableBinding;

public abstract class Dependency {
	private Map<char[][], ReferenceBinding> typeMaps = new HashMap<char[][], ReferenceBinding>();
	private Map<Integer, TypeBinding> baseTypes = new HashMap<Integer, TypeBinding>();
	
	public Dependency(){
	}

	private void add(TypeBinding binding) {
		if(binding instanceof TypeVariableBinding){
			return;
		}
		
		if(binding.isBaseType()){
			baseTypes.put(binding.id, binding);
			return;
		}
		
		ReferenceBinding refBinding = (ReferenceBinding) binding;
		
		if(refBinding.isMemberType()){
			refBinding = outestEnclosingType(refBinding);
		}

		if (typeMaps.containsKey(refBinding.compoundName)) {
			return;
		}

		typeMaps.put(refBinding.compoundName, refBinding);
	}
	
	public static char[] getFileName(char[] fileName){
		char[] fileNameWithExtension = CharOperation.lastSegment(fileName, '/');
		char[] result = CharOperation.splitOn('.', fileNameWithExtension)[0];
		return result;
	}
	
	private ReferenceBinding outestEnclosingType(ReferenceBinding binding){
		ReferenceBinding enclosing = binding;
		while(enclosing != null){
			ReferenceBinding temp = enclosing.enclosingType();
			if(temp == null ){
				return enclosing;
			}
			enclosing = temp;
		}
		return enclosing;
	}
	
	public static StringBuffer printIndent(int indent, StringBuffer output) {
		for (int i = indent; i > 0; i--) output.append("  "); //$NON-NLS-1$
		return output;
	}
	
	public StringBuffer generateDependency(int indent, StringBuffer output) {
		if(typeMaps.size() <= 0){
			return output;
		}
		
		printIndent(indent, output);
		output.append("var __deps = Object.defineProperties({}, {");
		boolean comma = false;
		for (ReferenceBinding binding : typeMaps.values()) {
			if(comma) output.append(", ");
			
			output.append("\n");
			printIndent(indent + 1, output);
			output.append(binding.sourceName).append(": { \n");
			
			printIndent(indent + 2, output);
			
			output.append("get : function(){ \n");
			
			printIndent(indent + 3, output);
			output.append("if(this._" ).append(binding.sourceName).append(") return this._").append(binding.sourceName).append("; \n");
			
			printIndent(indent + 3, output);
			output.append("else return this._").append(binding.sourceName).append(" = ")
				.append("_loadClass(").append("\"");
			
			char[][] classFileName = null;
			if((binding.modifiers & ClassFileConstants.AccModule) != 0){
				classFileName = CharOperation.arrayConcat(binding.fPackage.compoundName, getFileName(binding.getFileName()));
			} else{
				classFileName = binding.compoundName;
			}
			
			output.append(CharOperation.concatWith(classFileName, '.')).append("\"); \n");
			printIndent(indent + 2, output);
			output.append("} \n");
			
			printIndent(indent + 1, output);
			output.append("} ");
			
			comma = true;
		}
		output.append("\n");
		printIndent(indent, output);
		output.append("});");
		return output;
	
	}

	
	public abstract void collect();
	
	class TypeReferenceCollecor {

		public TypeReferenceCollecor() {
		}

		class TypeReferenceVisitor extends ASTVisitor {

			@Override
			public void endVisit(QualifiedTypeReference typeRef, BlockScope scope) {
				add(typeRef, scope);
			}

			@Override
			public void endVisit(SingleTypeReference typeRef, BlockScope scope) {
				add(typeRef, scope);
			}

			@Override
			public void endVisit(QualifiedTypeReference typeRef, ClassScope scope) {
				add(typeRef, scope);
			}

			@Override
			public void endVisit(SingleTypeReference typeRef, ClassScope scope) {
				add(typeRef, scope);
			}
		}
		
		protected void collect(TypeDeclaration type, ClassScope scope){
			type.traverse(new TypeReferenceVisitor(), scope);
		}
		
		protected void collect(CompilationUnitDeclaration unit, CompilationUnitScope scope){
			unit.traverse(new TypeReferenceVisitor(), scope);
		}

		void add(TypeReference typeRef, BlockScope scope) {
			TypeBinding binding = typeRef.resolveType(scope);
			if (binding != null) {
				Dependency.this.add(binding);
			}
		}

		void add(TypeReference typeRef, ClassScope scope) {
			TypeBinding binding = typeRef.resolveType(scope);
			if (binding != null) {
				Dependency.this.add(binding);
			}
		}
	}
}
