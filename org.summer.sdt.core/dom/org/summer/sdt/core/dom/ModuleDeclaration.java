package org.summer.sdt.core.dom;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ModuleDeclaration extends ASTNode{

	/**
	 * The module name; lazily initialized; defaults to a unspecified,
	 * legal Java class identifier.
	 */
	SimpleName name = null;
	
	/**
	 * The doc comment, or <code>null</code> if none.
	 * Defaults to none.
	 */
	Javadoc optionalDocComment = null;
	
	public static final ChildPropertyDescriptor NAME_PROPERTY =
			new ChildPropertyDescriptor(ModuleDeclaration.class, "name", SimpleName.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$
	
	/**
	 * The "javadoc" structural property of this node type (child type: {@link Javadoc}).
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor JAVADOC_PROPERTY =
			new ChildPropertyDescriptor(ModuleDeclaration.class, "javadoc", Javadoc.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$
	
	/**
	 * The "types" structural property of this node type (element type: {@link AbstractTypeDeclaration}).
	 *
	 * @since 3.0
	 */
	public static final ChildListPropertyDescriptor TYPES_PROPERTY =
		new ChildListPropertyDescriptor(ModuleDeclaration.class, "types", AbstractTypeDeclaration.class, CYCLE_RISK); //$NON-NLS-1$
	
	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 * @since 3.0
	 */
	private static final List PROPERTY_DESCRIPTORS;

	static {
		List properyList = new ArrayList(4);
		createPropertyList(ModuleDeclaration.class, properyList);
		addProperty(NAME_PROPERTY, properyList);
		addProperty(JAVADOC_PROPERTY, properyList);
		addProperty(TYPES_PROPERTY, properyList);
		PROPERTY_DESCRIPTORS = reapPropertyList(properyList);
	}
	
	ModuleDeclaration(AST ast) {
		super(ast);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * The list of type declarations in textual order order;
	 * initially none (elementType: <code>AbstractTypeDeclaration</code>)
	 */
	private ASTNode.NodeList types =
		new ASTNode.NodeList(TYPES_PROPERTY);

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 *
	 * @param apiLevel the API level; one of the
	 * <code>AST.JLS*</code> constants

	 * @return a list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor})
	 * @since 3.0
	 */
	public static List propertyDescriptors(int apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}
	
	/**
	 * Returns the name of the type declared in this type declaration.
	 *
	 * @return the type name node
	 * @since 2.0 (originally declared on {@link TypeDeclaration})
	 */
	public SimpleName getName() {
		if (this.name == null) {
			// lazy init must be thread-safe for readers
			synchronized (this) {
				if (this.name == null) {
					preLazyInit();
					this.name = new SimpleName(this.ast);
					postLazyInit(this.name, internalNameProperty());
				}
			}
		}
		return this.name;
	}

	/**
	 * Sets the name of the type declared in this type declaration to the
	 * given name.
	 *
	 * @param moduleName the new type name
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 * @since 2.0 (originally declared on {@link TypeDeclaration})
	 */
	public void setName(SimpleName moduleName) {
		if (moduleName == null) {
			throw new IllegalArgumentException();
		}
		ChildPropertyDescriptor p = internalNameProperty();
		ASTNode oldChild = this.name;
		preReplaceChild(oldChild, moduleName, p);
		this.name = moduleName;
		postReplaceChild(oldChild, moduleName, p);
	}
	
	/**
	 * Returns the doc comment node.
	 *
	 * @return the doc comment node, or <code>null</code> if none
	 */
	public Javadoc getJavadoc() {
		return this.optionalDocComment;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on BodyDeclaration.
	 */
	final ChildPropertyDescriptor internalJavadocProperty() {
		return JAVADOC_PROPERTY;
	}

	/**
	 * Sets or clears the doc comment node.
	 *
	 * @param docComment the doc comment node, or <code>null</code> if none
	 * @exception IllegalArgumentException if the doc comment string is invalid
	 */
	public void setJavadoc(Javadoc docComment) {
		ChildPropertyDescriptor p = internalJavadocProperty();
		ASTNode oldChild = this.optionalDocComment;
		preReplaceChild(oldChild, docComment, p);
		this.optionalDocComment = docComment;
		postReplaceChild(oldChild, docComment, p);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			// visit children in normal left to right reading order
			acceptChild(visitor, this.name);
			acceptChildren(visitor, this.types);
		}
		visitor.endVisit(this);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone0(AST target) {
		CompilationUnit result = new CompilationUnit(target);
		// n.b do not copy line number table or messages
		result.setSourceRange(getStartPosition(), getLength());
		result.setPackage(
			(PackageDeclaration) ASTNode.copySubtree(target, getName()));
		result.types().addAll(ASTNode.copySubtrees(target, types));
		return result;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == JAVADOC_PROPERTY) {
			if (get) {
				return getJavadoc();
			} else {
				setJavadoc((Javadoc) child);
				return null;
			}
		}
		if (property == NAME_PROPERTY) {
			if (get) {
				return getName();
			} else {
				setName((SimpleName) child);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final List internalGetChildListProperty(ChildListPropertyDescriptor property) {
		if (property == TYPES_PROPERTY) {
			return types;
		}

		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}
	
	/**
	 * Returns the live list of nodes for the top-level type declarations of this
	 * compilation unit, in order of appearance.
     * <p>
     * Note that in JLS3, the types may include both enum declarations
     * and annotation type declarations introduced in J2SE 5.
     * For JLS2, the elements are always <code>TypeDeclaration</code>.
     * </p>
	 *
	 * @return the live list of top-level type declaration
	 *    nodes (element type: {@link AbstractTypeDeclaration})
	 */
	public List getTypes() {
		return this.types;
	}
	
	
	/* (omit javadoc for this method)
	 * Method declared on AbstractTypeDeclaration.
	 */
	final ChildPropertyDescriptor internalNameProperty() {
		return NAME_PROPERTY;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final int getNodeType0() {
		return MODULE_DECLARATION;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 * @since 3.0
	 */
	final List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}

	@Override
	boolean subtreeMatch0(ASTMatcher matcher, Object other) {
		// dispatch to correct overloaded match method
		return matcher.match(this, other);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return BASE_NODE_SIZE + 3 * 4;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return memSize()
			+ (this.optionalDocComment == null ? 0 : getJavadoc().treeSize())
			+ (this.name == null ? 0 : getName().treeSize())
			+ (this.types == null ? 0 : types.listSize());
	}

	public void resolveBinding() {
		// TODO Auto-generated method stub
		
	}

}
