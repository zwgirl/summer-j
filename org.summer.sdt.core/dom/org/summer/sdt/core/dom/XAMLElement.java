package org.summer.sdt.core.dom;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class XAMLElement extends XAMLNode {
	
	/**
	 * The "type" structural property of this node type (child type: {@link Type}).
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor TYPE_PROPERTY =
		new ChildPropertyDescriptor(XAMLElement.class, "type", Type.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "attributes" structural property of this node type (element type: {@link XAMLAttribute}).
	 * @since 3.0
	 */
	public static final ChildListPropertyDescriptor ATTRIBUTES_PROPERTY =
		new ChildListPropertyDescriptor(XAMLElement.class, "attributes", Attribute.class, CYCLE_RISK); //$NON-NLS-1$
	
	/**
	 * The "children" structural property of this node type (element type: {@link XAMLAttribute}).
	 * @since 3.0
	 */
	public static final ChildListPropertyDescriptor CHILDREN_PROPERTY =
		new ChildListPropertyDescriptor(XAMLElement.class, "children", XAMLElement.class, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List PROPERTY_DESCRIPTORS;

	static {
		List propertyList = new ArrayList(3);
		createPropertyList(XAMLElement.class, propertyList);
		addProperty(TYPE_PROPERTY, propertyList);
		addProperty(ATTRIBUTES_PROPERTY, propertyList);
		addProperty(CHILDREN_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS = reapPropertyList(propertyList);
	}

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
	 * The expression; lazily initialized; defaults to a unspecified, but legal,
	 * expression.
	 */
	private Type type = null;

	/**
	 * The statements and SwitchCase nodes
	 * (element type: {@link Statement}).
	 * Defaults to an empty list.
	 */
	private ASTNode.NodeList attributes =
		new ASTNode.NodeList(ATTRIBUTES_PROPERTY);
	
	private ASTNode.NodeList children =
			new ASTNode.NodeList(CHILDREN_PROPERTY);
	
	XAMLElement(AST ast) {
		super(ast);
		// TODO Auto-generated constructor stub
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == TYPE_PROPERTY) {
			if (get) {
				return getType();
			} else {
				setType((Type) child);
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
		if (property == ATTRIBUTES_PROPERTY) {
			return attributes();
		} else if (property == CHILDREN_PROPERTY) {
			return attributes();
		}
		
		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int getNodeType0() {
		return XAML_ELEMENT;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone0(AST target) {
		XAMLElement result = new XAMLElement(target);
		result.setSourceRange(getStartPosition(), getLength());
		result.setType(
				(Type) ASTNode.copySubtree(target, getType()));
		result.attributes().addAll(
			ASTNode.copySubtrees(target, attributes()));
		result.children().addAll(
				ASTNode.copySubtrees(target, children()));
		return result;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final boolean subtreeMatch0(ASTMatcher matcher, Object other) {
		// dispatch to correct overloaded match method
		return matcher.match(this, other);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			// visit children in normal left to right reading order
			acceptChild(visitor, getType());
			acceptChildren(visitor, this.attributes);
			acceptChildren(visitor, this.children);
		}
		visitor.endVisit(this);
	}

	/**
	 * Returns the expression of this switch statement.
	 *
	 * @return the expression node
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Sets the expression of this switch statement.
	 *
	 * @param expression the new expression node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */
	public void setType(Type type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.type;
		preReplaceChild(oldChild, type, TYPE_PROPERTY);
		this.type = type;
		postReplaceChild(oldChild, type, TYPE_PROPERTY);
	}

	/**
	 * Returns the live ordered list of statements for this switch statement.
	 * Within this list, <code>SwitchCase</code> nodes mark the start of
	 * the switch groups.
	 *
	 * @return the live list of statement nodes
	 *    (element type: {@link Statement})
	 */
	public List attributes() {
		return this.attributes;
	}
	
	public List children() {
		return this.children;
	}


	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return super.memSize() + 3 * 4;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return
			memSize()
			+ (this.attributes == null ? 0 : this.attributes.listSize())
			+ (this.children == null ? 0 : this.children.listSize())
			+ (this.type == null ? 0 : getType().treeSize());
	}
}
