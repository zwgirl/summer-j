package org.summer.sdt.internal.compiler.javascript;

import org.summer.sdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.summer.sdt.internal.compiler.lookup.CompilationUnitScope;

/**
 * 
 * @author cym
 * @since 2014-12-15
 *
 */
public class UnitDependency extends Dependency {
	private final CompilationUnitDeclaration unit;
	private final CompilationUnitScope scope;

	public UnitDependency(CompilationUnitDeclaration unit,
			CompilationUnitScope scope) {
		this.unit = unit;
		this.scope = scope;
	}

	public void collect() {
		TypeReferenceCollecor collector = new TypeReferenceCollecor();
		collector.collect(unit, scope);
	}
}
