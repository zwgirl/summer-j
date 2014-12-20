package org.summer.sdt.internal.compiler.javascript;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.ast.ASTNode;
import org.summer.sdt.internal.compiler.ast.Argument;
import org.summer.sdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.summer.sdt.internal.compiler.ast.FieldDeclaration;
import org.summer.sdt.internal.compiler.ast.Initializer;
import org.summer.sdt.internal.compiler.ast.MethodDeclaration;
import org.summer.sdt.internal.compiler.ast.QualifiedTypeReference;
import org.summer.sdt.internal.compiler.ast.SingleTypeReference;
import org.summer.sdt.internal.compiler.ast.TypeDeclaration;
import org.summer.sdt.internal.compiler.ast.TypeReference;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.CompilationUnitScope;
import org.summer.sdt.internal.compiler.lookup.MethodScope;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeConstants;
import org.summer.sdt.internal.compiler.lookup.TypeVariableBinding;

public abstract class Dependency {
	private Map<char[][], ReferenceBinding> typeMaps = new HashMap<char[][], ReferenceBinding>();
	private Map<Integer, TypeBinding> baseTypes = new HashMap<Integer, TypeBinding>();
	private Map<char[][], List<ReferenceBinding>> moduleTypes = new HashMap<char[][], List<ReferenceBinding>>();
	private Map<char[][], List<ReferenceBinding>> mergeToduleTypes = new HashMap<char[][], List<ReferenceBinding>>();
	private Map<char[][], String> parameters = new HashMap<char[][], String>();
	
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
		
		if((refBinding.modifiers & ClassFileConstants.AccModule) != 0){
			char[][] key = CharOperation.arrayConcat(refBinding.fPackage.compoundName, getFileName(refBinding.getFileName()));
			if((refBinding.modifiers & ClassFileConstants.AccModuleMerge) != 0){
				List<ReferenceBinding> types = mergeToduleTypes.get(key);
				if(types == null){	
					types = new LinkedList<ReferenceBinding>();
					mergeToduleTypes.put(key, types);
				}
				types.add(refBinding);
			} else {
				List<ReferenceBinding> types = moduleTypes.get(key);
				if(types == null){	
					types = new LinkedList<ReferenceBinding>();
					moduleTypes.put(key, types);
				}
				types.add(refBinding);
			}
			return;
		}
		
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

	private StringBuffer generateDependency(int indent, StringBuffer output) {
		boolean comma = false;
		for (ReferenceBinding binding : typeMaps.values()) {
			if((binding.modifiers & ClassFileConstants.AccCompleteNative) != 0){
				continue;
			}
			
			if(comma){
				output.append(", ");
			}

			
			output.append("\"");
			output.append(CharOperation.concatWith(binding.compoundName, '.'));
			output.append("\"");
			
			comma = true;
		}
		
		for(char[][] key : moduleTypes.keySet()){
			List<ReferenceBinding> types = moduleTypes.get(key);
			
			if(types.isEmpty()){
				continue;
			}
			
			if(comma){
				output.append(", ");
			}
			output.append("[").append(key);
			for(ReferenceBinding type : types){
				output.append(", \"").append(CharOperation.concatWith(type.compoundName, '.')).append("\"");
			}
			
			output.append("]");
			comma = true;
		}
		
		for(char[][] key : moduleTypes.keySet()){
			List<ReferenceBinding> types = moduleTypes.get(key);
			
			if(types.isEmpty()){
				continue;
			}
			
			if(comma){
				output.append(", ");
			}
			
			output.append("\"").append(CharOperation.concatWith(key, '.')).append("\"");
			comma = true;
		}
		
		return output;

	}
	
	private StringBuffer generateParameters(int indent, StringBuffer output) {
		boolean comma = false;
		for (ReferenceBinding binding : typeMaps.values()) {
			if((binding.modifiers & ClassFileConstants.AccCompleteNative) != 0){
				continue;
			}
			
			if(comma){
				output.append(", ");
			}
			
			output.append(binding.sourceName);
			comma = true;
		}
		
		for(char[][] key : moduleTypes.keySet()){
			List<ReferenceBinding> types = moduleTypes.get(key);
			
			if(types.isEmpty()){
				continue;
			}
			
			for(ReferenceBinding type : types){
				if(comma){
					output.append(", ");
				}
				output.append(type.sourceName);
				comma = true;
			}
		}
		
		for(char[][] key : moduleTypes.keySet()){
			List<ReferenceBinding> types = moduleTypes.get(key);
			
			if(types.isEmpty()){
				continue;
			}
			
			if(comma){
				output.append(", ");
			}
			
			output.append(key);
			comma = true;
		}
		
		return output;

	}
	
	abstract protected char[] getAMDModuleId();
	
	public void generateAMDHeader(char[] mid, int indent, StringBuffer output, String funcName) {
		output.append(funcName).append(Javascript.LPAREN).append(Javascript.DOUBLE_QUOTE)
		.append(mid).append(Javascript.DOUBLE_QUOTE);
		output.append(Javascript.COMMA).append(Javascript.WHITESPACE);
		output.append(Javascript.LBRACKET);
		
		this.generateDependency(indent, output);

		output.append(Javascript.RBRACKET);
		output.append(Javascript.COMMA).append(Javascript.WHITESPACE);;
		output.append(Javascript.FUNCTION).append(Javascript.LPAREN);
		
		this.generateParameters(indent, output);
		output.append(Javascript.RPAREN).append(Javascript.LBRACE);
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
