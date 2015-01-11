package org.summer.sdt.internal.compiler.javascript;

import org.summer.sdt.internal.compiler.ast.TypeDeclaration;

/**
 * 
 * @author cym
 * @since 2014-12-15
 *
 */
public class TypeDependency extends Dependency {
	private final TypeDeclaration type;

	public TypeDependency(TypeDeclaration type) {
		this.type = type;
	}

	public void collect() {
		TypeReferenceCollecor collector = new TypeReferenceCollecor();
		collector.collect(type, type.scope);
	}
}
