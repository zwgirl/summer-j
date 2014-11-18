package org.summer.sdt.internal.compiler.javascript;

import java.util.ArrayList;
import java.util.List;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.ast.QualifiedTypeReference;
import org.summer.sdt.internal.compiler.ast.SingleTypeReference;
import org.summer.sdt.internal.compiler.ast.TypeDeclaration;
import org.summer.sdt.internal.compiler.ast.TypeReference;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;

public final class ImportManager {
	private char[][] JavaLang = new char[][] {"java".toCharArray(), "lang".toCharArray()};
	private List<TypeBinding> types;
	private final TypeDeclaration typeDeclaration;
	
	public ImportManager(TypeDeclaration typeDeclaration){
		this.typeDeclaration = typeDeclaration;
	}

	private void add(TypeBinding binding) {
		if (types.contains(binding)) {
			return;
		}

		types.add(binding);
	}
	
	private void ensureCollect(ClassScope scope){
		if(types != null){
			return;
		}
		types = new ArrayList<TypeBinding>();
		collect(typeDeclaration, scope);
	}

	public StringBuffer generateDependency(ClassScope scope, int indent,
			StringBuffer output) {
		ensureCollect(scope);
		boolean comma = false;
		for (TypeBinding binding : types) {
			if(binding.getPackage() == null){
				continue;
			}
			
//			if(binding instanceof TypeVariableBinding){
//				continue;
//			}
//			
//			if(CharOperation.equals(binding.getPackage().compoundName, JavaLang)){
//				continue;
//			}
			if(comma)
				output.append(", ");

			output.append("\"").append(CharOperation.concatWith(binding.getPackage().compoundName, '.'));
			output.append('.').append(binding.sourceName()).append("\"");
			comma = true;
		}
		return output;

	}
	
	public StringBuffer generateParameters(ClassScope scope, int indent,
			StringBuffer output) {
		ensureCollect(scope);
		boolean comma = false;
		for (TypeBinding binding : types) {
			if(binding.getPackage() == null){
				continue;
			}
			
//			if(CharOperation.equals(binding.getPackage().compoundName, JavaLang)){
//				continue;
//			}
			if(comma)
				output.append(", ");

//			output.append("\"").append(CharOperation.concatWith(binding.getPackage().compoundName, '.'));
			output.append(binding.sourceName());
			comma = true;
		}
		return output;

	}
	
	private void collect(TypeDeclaration type, ClassScope scope){
		TypeReferenceCollecor collector = new TypeReferenceCollecor();
		collector.collect(type, scope);
	}
	
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
		
		public void collect(TypeDeclaration type, ClassScope scope){
			type.traverse(new TypeReferenceVisitor(), scope);
		}
		
		public void collect(TypeDeclaration type, BlockScope scope){
			type.traverse(new TypeReferenceVisitor(), scope);
		}

		void add(TypeReference typeRef, BlockScope scope) {
			TypeBinding binding = typeRef.resolveType(scope);
			if (binding != null) {
				ImportManager.this.add(binding);
			}
		}

		void add(TypeReference typeRef, ClassScope scope) {
			TypeBinding binding = typeRef.resolveType(scope);
			if (binding != null) {
				ImportManager.this.add(binding);
			}
		}
	}
}
