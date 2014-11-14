package org.summer.sdt.internal.compiler.lookup;

import org.summer.sdt.internal.compiler.problem.ProblemReporter;

public class ElementScope extends BlockScope {
//	public final Element context;
	public ElementScope(int kind, Scope parent) {
		super(kind, parent);
//		this.context = context;
	}

	@Override
	public ProblemReporter problemReporter() {
		return parent.problemReporter();
	}

	@Override
	public boolean hasDefaultNullnessFor(int location) {
		// TODO Auto-generated method stub
		return false;
	}

}
