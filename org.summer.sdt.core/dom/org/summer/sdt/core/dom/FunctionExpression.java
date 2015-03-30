package org.summer.sdt.core.dom;

import java.util.List;

public class FunctionExpression extends Expression{

	FunctionExpression(AST ast) {
		super(ast);
		// TODO Auto-generated constructor stub
	}

	@Override
	List internalStructuralPropertiesForType(int apiLevel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	int getNodeType0() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	boolean subtreeMatch0(ASTMatcher matcher, Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	ASTNode clone0(AST target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void accept0(ASTVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	int treeSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	int memSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
