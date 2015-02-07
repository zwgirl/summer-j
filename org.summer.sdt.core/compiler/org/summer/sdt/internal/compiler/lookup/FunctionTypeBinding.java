package org.summer.sdt.internal.compiler.lookup;

import org.summer.sdt.core.compiler.CharOperation;

/**
 * 
 * @author cym
 *
 */
public class FunctionTypeBinding extends ReferenceBinding{
	private static final char[] FUNCTION = "function".toCharArray();
	public TypeBinding function;
	LookupEnvironment environment;
	char[] constantPoolName;
	char[] genericTypeSignature;
	
	public TypeBinding[] parameterTypes;
	public TypeBinding returnType;
	
	public FunctionTypeBinding(TypeBinding returnType, TypeBinding[] parameterTypes, LookupEnvironment environment){
		this.returnType = returnType;
		this.parameterTypes = parameterTypes;
		this.environment = environment;
	}

	@Override
	public char[] constantPoolName() {
		return function.constantPoolName();
	}

	@Override
	public PackageBinding getPackage() {
		return function.getPackage();
	}

	@Override
	public boolean isCompatibleWith(TypeBinding right, Scope scope) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public char[] qualifiedSourceName() {
		return FUNCTION;
	}
	
	@Override
	public int hashCode() {
		int hashCode = this.returnType.hashCode();
		for (int i = 0, length = this.parameterTypes == null ? 0 : this.parameterTypes.length; i < length; i++) {
			hashCode += (i + 1) * this.parameterTypes[i].id * this.parameterTypes[i].hashCode();
		}
		return hashCode;
	}

	@Override
	public char[] sourceName() {
		char[][] parameterNames = new char[parameterTypes.length][];
		for (int i = 0, length = parameterTypes.length; i < length; i++) {
			parameterNames[i] = parameterTypes[i].sourceName();
		}
		
		char[] pars = CharOperation.concatWith(parameterNames, ',');
		return CharOperation.concat(FUNCTION, pars);
	}

	@Override
	public char[] readableName() {
		return FUNCTION;
	}
	
	@Override
	public ReferenceBinding superclass() {
		return environment.getType(TypeConstants.JAVA_LANG_FUNCTION);
	}
	
	@Override
	public TypeBinding returnType() {
		return returnType;
	}
	
	@Override
	public TypeBinding[] parameterTypes() {
		return parameterTypes;
	}

}
