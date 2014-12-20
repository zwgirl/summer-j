package org.summer.sdt.internal.compiler.lookup;

import org.summer.sdt.internal.compiler.ast.QualifiedTypeReference;
import org.summer.sdt.internal.compiler.problem.ProblemReporter;
/**
 * 
 * @author cym
 * @since 2014-12-12
 *
 */
public class GlobalScope extends Scope{
	private ReferenceBinding global;
	
	protected GlobalScope(Scope parent) {
		super(GLOBAL_SCOPE, parent);
	} 
	
	public static final char[][] JAVA_LANG_GLOBAL = new char[][] {"java".toCharArray(), "lang".toCharArray(), "Global".toCharArray()};

	@Override
	public ProblemReporter problemReporter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasDefaultNullnessFor(int location) {
		// TODO Auto-generated method stub
		return false;
	}

	public ReferenceBinding global() {
		if(global == null){
			QualifiedTypeReference winType = new QualifiedTypeReference(JAVA_LANG_GLOBAL, new long[]{0});
			global = (ReferenceBinding) winType.resolveType((CompilationUnitScope)this.parent);
		}
		
		return global;
	}

}
