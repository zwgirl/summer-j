package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.codegen.CodeStream;
import org.summer.sdt.internal.compiler.html.Html2JsAttributeMapping;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
import org.summer.sdt.internal.compiler.lookup.ClassScope;
import org.summer.sdt.internal.compiler.lookup.ElementScope;
import org.summer.sdt.internal.compiler.lookup.FieldBinding;
import org.summer.sdt.internal.compiler.lookup.InferenceContext18;
import org.summer.sdt.internal.compiler.lookup.InvocationSite;
import org.summer.sdt.internal.compiler.lookup.MethodBinding;
import org.summer.sdt.internal.compiler.lookup.MissingTypeBinding;
import org.summer.sdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemFieldBinding;
import org.summer.sdt.internal.compiler.lookup.ProblemReasons;
import org.summer.sdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.ReferenceBinding;
import org.summer.sdt.internal.compiler.lookup.Scope;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeConstants;
import org.summer.sdt.internal.compiler.lookup.TypeIds;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public class Attribute extends XAMLNode implements InvocationSite{
	public PropertyReference property;
	public Expression value;
	
	public TypeBinding type;   //type of property 
	public MethodBinding method;
	public ReferenceBinding template;
	public FieldBinding field;
	
	public static final int ENUM = 1;
	public static final int FUNCTION = 2;
	public static final int CLASS = 3;
	public static final int STRING = 4;
	public static final int PRIMITIVE = 5;
	public static final int TEMPLATE = 6;
	public static final int REFERENCE = 7;
	private int propertyKind;
	
	public final static char[] NAME = "name".toCharArray();
	
	public static final int NamedFlag = 1;
	
	public Attribute(PropertyReference property) {
		super();
		this.property = property;
	}

	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		return printStatement(indent, output);
	}
	
	@Override
	public StringBuffer printStatement(int indent, StringBuffer output) {
		printPropertyName(indent, output);
		output.append(" = ");
		value.printExpression(indent, output);
		return output;
	}
	
	protected void printPropertyName(int indent, StringBuffer output) {
		output.append(property.token);
	}

	public void resolve(BlockScope scope) {
//		this.constant = Constant.NotAConstant;
//		property.resolveType(scope);
		internalResolve(scope);
	}
	
	public void resolve(ElementScope scope){
		internalResolve(scope);
//		this.type = property.resolveType(scope);
//		ClassScope classScope = scope.enclosingClassScope();
//
//		if((this.bits & ASTNode.IsNamedAttribute) != 0){
//			classScope.referenceContext.element.bits |= ASTNode.HasDynamicContent;
//		}
//		
//		if(this.value instanceof MarkupExtension){
//			MarkupExtension markExt = (MarkupExtension) this.value;
//			markExt.resolve(scope);
//			classScope.referenceContext.element.bits |= ASTNode.HasDynamicContent;
//		} else {
//			if(this.type == null){
//				return;
//			}
//
//			if(this.type.isPrimitiveType()){
//				kind = PRIMITIVE;
//				return;
//			} 
//			
//			if(this.type == scope.environment().getType(TypeConstants.JAVA_LANG_TEMPLATE)){
//				kind = TEMPLATE;
//				this.template = (ReferenceBinding) scope.getType(((StringLiteral) this.value).source);
//				return;
//			}
//			
//			ReferenceBinding refType = (ReferenceBinding) this.type;
//			if((refType.modifiers & ClassFileConstants.AccFunction) != 0 ){
//				kind = FUNCTION;
//				this.bits |= ASTNode.IsEventCallback;
//				StringLiteral stringLiteral = (StringLiteral) this.value;
////				this.method = scope.findMethod(scope.classScope().referenceContext.binding, stringLiteral.source, new TypeBinding[0], this, false);
////				if(this.method == null){
//					this.field = scope.findField(scope.classScope().referenceContext.binding, stringLiteral.source, this, true);
//					if(field == null){
//						scope.problemReporter().errorNoMethodFor(stringLiteral, this.type, new TypeBinding[0]); 
//					}
////				}
//			} else if(refType.isEnum()){
//				kind = ENUM;
//				this.field = scope.getField(refType, ((StringLiteral) this.value).source, this);
//				if((field.modifiers & ClassFileConstants.AccEnum) == 0) {
//					scope.problemReporter().invalidProperty(property, property.fieldBinding.type);   //TODO  cym 2015-02-27
//				}
//				if(field == null){
//					scope.problemReporter().invalidProperty(property, property.fieldBinding.type);    //TODO  cym 2015-02-27
//				}
//				
//				if (!field.isValidBinding()) {
//					this.constant = Constant.NotAConstant;
//					if (refType instanceof ProblemReferenceBinding) {
//						// problem already got signaled on receiver, do not report secondary problem
//						return;
//					}
//					// https://bugs.eclipse.org/bugs/show_bug.cgi?id=245007 avoid secondary errors in case of
//					// missing super type for anonymous classes ... 
//					ReferenceBinding declaringClass = field.declaringClass;
//					boolean avoidSecondary = declaringClass != null &&
//											 declaringClass.isAnonymousType() &&
//											 declaringClass.superclass() instanceof MissingTypeBinding;
//					if (!avoidSecondary) {
//						scope.problemReporter().invalidProperty(property, property.fieldBinding.type);  //TODO cym 2015-02-27
//					}
//					if (field instanceof ProblemFieldBinding) {
//						ProblemFieldBinding problemFieldBinding = (ProblemFieldBinding) field;
//						FieldBinding closestMatch = problemFieldBinding.closestMatch;
//						switch(problemFieldBinding.problemId()) {
//							case ProblemReasons.InheritedNameHidesEnclosingName :
//							case ProblemReasons.NotVisible :
//							case ProblemReasons.NonStaticReferenceInConstructorInvocation :
//							case ProblemReasons.NonStaticReferenceInStaticContext :
//								if (closestMatch != null) {
//									field = closestMatch;
//								}
//						}
//					}
//					if (!field.isValidBinding()) {
//						return;
//					}
//				}
//			} else if(this.type.id == TypeIds.T_JavaLangClass || 
//					(this.type instanceof ParameterizedTypeBinding && ((ParameterizedTypeBinding)this.type).original().id == TypeIds.T_JavaLangClass)){
//				kind = CLASS;
//				this.template = (ReferenceBinding) scope.getType(((StringLiteral) this.value).source);
//			} else if(this.type != scope.environment().getType(TypeConstants.JAVA_LANG_STRING)){
//				kind = REFERENCE;
//				this.template = (ReferenceBinding) scope.getType(((StringLiteral) this.value).source);
//			} else {
//				kind = STRING;
//				ReferenceBinding temBinding = scope.getJavaLangTemplate();
//				if(this.type.isSubtypeOf(temBinding)) {
//					this.bits |= ASTNode.IsTemplate;
//					StringLiteral stringLiteral = (StringLiteral) this.value;
//					this.template = (ReferenceBinding) scope.getType(stringLiteral.source);
//					if(this.template == null){
//						scope.problemReporter().errorNoMethodFor(stringLiteral, property.fieldBinding.type, new TypeBinding[0]); 
//					}
//					
//					classScope.referenceContext.element.bits |= ASTNode.HasDynamicContent;
//				}
//			}
//		}
	}
	
	private void internalResolve(BlockScope scope){
		this.type = property.resolveType(scope);
		
		ClassScope classScope = scope.enclosingClassScope();

		if((this.bits & ASTNode.IsNamedAttribute) != 0){
			classScope.referenceContext.element.bits |= ASTNode.HasDynamicContent;
		}
		if(this.value instanceof MarkupExtension){
			MarkupExtension markExt = (MarkupExtension) this.value;
			markExt.resolve(scope);
			classScope.referenceContext.element.bits |= ASTNode.HasDynamicContent;
		}
		
		if(this.type == null){
			return;
		}

		if(this.type.isPrimitiveType()){
			propertyKind = PRIMITIVE;
			return;
		} 
		
		if(this.type == scope.environment().getType(TypeConstants.JAVA_LANG_TEMPLATE)){
			propertyKind = TEMPLATE;
			this.template = (ReferenceBinding) scope.getType(((StringLiteral) this.value).source);
			return;
		}
		
		ReferenceBinding refType = (ReferenceBinding) this.type;
		if((refType.modifiers & ClassFileConstants.AccFunction) != 0 ){
			propertyKind = FUNCTION;
			this.bits |= ASTNode.IsEventCallback;
			StringLiteral stringLiteral = (StringLiteral) this.value;
//				this.method = scope.findMethod(scope.classScope().referenceContext.binding, stringLiteral.source, new TypeBinding[0], this, false);
//				if(this.method == null){
				this.field = scope.findField(scope.classScope().referenceContext.binding, stringLiteral.source, this, true);
				if(field == null){
					scope.problemReporter().errorNoMethodFor(stringLiteral, this.type, new TypeBinding[0]); 
				}
//				}
		} else if(refType.isEnum()){
			propertyKind = ENUM;
			this.field = scope.getField(refType, ((StringLiteral) this.value).source, this);
			if((field.modifiers & ClassFileConstants.AccEnum) == 0) {
				scope.problemReporter().invalidProperty(property, property.fieldBinding.type);   //TODO  cym 2015-02-27
			}
			if(field == null){
				scope.problemReporter().invalidProperty(property, property.fieldBinding.type);    //TODO  cym 2015-02-27
			}
			
			if (!field.isValidBinding()) {
				this.constant = Constant.NotAConstant;
				if (refType instanceof ProblemReferenceBinding) {
					// problem already got signaled on receiver, do not report secondary problem
					return;
				}
				// https://bugs.eclipse.org/bugs/show_bug.cgi?id=245007 avoid secondary errors in case of
				// missing super type for anonymous classes ... 
				ReferenceBinding declaringClass = field.declaringClass;
				boolean avoidSecondary = declaringClass != null &&
										 declaringClass.isAnonymousType() &&
										 declaringClass.superclass() instanceof MissingTypeBinding;
				if (!avoidSecondary) {
					scope.problemReporter().invalidProperty(property, property.fieldBinding.type);  //TODO cym 2015-02-27
				}
				if (field instanceof ProblemFieldBinding) {
					ProblemFieldBinding problemFieldBinding = (ProblemFieldBinding) field;
					FieldBinding closestMatch = problemFieldBinding.closestMatch;
					switch(problemFieldBinding.problemId()) {
						case ProblemReasons.InheritedNameHidesEnclosingName :
						case ProblemReasons.NotVisible :
						case ProblemReasons.NonStaticReferenceInConstructorInvocation :
						case ProblemReasons.NonStaticReferenceInStaticContext :
							if (closestMatch != null) {
								field = closestMatch;
							}
					}
				}
				if (!field.isValidBinding()) {
					return;
				}
			}
		} else if(this.type.id == TypeIds.T_JavaLangClass || 
				(this.type instanceof ParameterizedTypeBinding && ((ParameterizedTypeBinding)this.type).original().id == TypeIds.T_JavaLangClass)){
			propertyKind = CLASS;
			this.template = (ReferenceBinding) scope.getType(((StringLiteral) this.value).source);
		} else if(this.type == scope.environment().getType(TypeConstants.JAVA_LANG_STRING)){
			propertyKind = STRING;
//			ReferenceBinding temBinding = scope.getJavaLangTemplate();
//			if(this.type.isSubtypeOf(temBinding)) {
//				this.bits |= ASTNode.IsTemplate;
//				StringLiteral stringLiteral = (StringLiteral) this.value;
//				this.template = (ReferenceBinding) scope.getType(stringLiteral.source);
//				if(this.template == null){
//					scope.problemReporter().errorNoMethodFor(stringLiteral, property.fieldBinding.type, new TypeBinding[0]); 
//				}
//				
//				classScope.referenceContext.element.bits |= ASTNode.HasDynamicContent;
//			}
		} else {
			propertyKind = REFERENCE;
			if(this.value instanceof MarkupExtension){
				
			} else {
				this.template = (ReferenceBinding) scope.getType(((StringLiteral) this.value).source);
			}
		}
	}
	
	/**
	 * Every expression is responsible for generating its implicit conversion when necessary.
	 *
	 * @param currentScope org.summer.sdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.summer.sdt.internal.compiler.codegen.CodeStream
	 * @param valueRequired boolean
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {

	}
	
	@Override
	protected StringBuffer doGenerateExpression(Scope scope, int indent, StringBuffer output) {
		output.append(Html2JsAttributeMapping.getHtmlAttributeName(new String(property.token))).append("=");
		
		if(this.property.fieldBinding != null && this.property.fieldBinding.type instanceof ReferenceBinding 
				&& (((ReferenceBinding)this.property.fieldBinding.type).modifiers & ClassFileConstants.AccFunction) != 0){
			output.append("\"this.");
			if(this.value instanceof StringLiteral){
				output.append(((StringLiteral)this.value).source);
			}
			output.append("(event);\"");
		} else {
			value.generateExpression(scope, indent, output);
		}
		
		return output;
	}
	
//	public StringBuffer buildPair(Scope scope, int indent, StringBuffer output){
//		System.out.println("type = " + new String(this.type.constantPoolName()) + " id=" + 
//				(this.type instanceof ParameterizedTypeBinding ? ((ParameterizedTypeBinding)this.type).original().id + " nn" : this.type.id )+
//				" property = " + new String(this.property.toString()));
//		this.property.doGenerateExpression(scope, indent, output).append(" : ");
//		if(this.type.id == TypeIds.T_JavaLangClass || 
//				(this.type instanceof ParameterizedTypeBinding && ((ParameterizedTypeBinding)this.type).original().id == TypeIds.T_JavaLangClass)){//scope.environment().getType(TypeConstants.JAVA_LANG_CLASS).id){
//			this.template.generate(output);
//			output.append("prototype.__class");
//			return output;
//		} else if(this.property.fieldBinding.type.isEnum()){
//			((ReferenceBinding)this.property.fieldBinding.type).generate(output);
//			output.append('.').append(((StringLiteral) this.value).source);
//			return output;
//		} if(this.property.fieldBinding.type.isSubtypeOf(scope.environment().getType(TypeConstants.JAVA_LANG_CLASS))){ 
//			((ReferenceBinding)this.property.fieldBinding.type).generate(output);
//			output.append("prototype.__class");
//			return output;
//		} if((((ReferenceBinding)this.property.fieldBinding.type).modifiers & ClassFileConstants.AccFunction) != 0){ 
//			if(this.method != null){
//				if(this.method.isStatic()){
//					output.append(this.method.declaringClass.sourceName).append('.');
//					if(this.value instanceof StringLiteral){
//						output.append(((StringLiteral)this.value).source);
//					}
//				} else {
//					output.append("__this.");
//					if(this.value instanceof StringLiteral){
//						output.append(((StringLiteral)this.value).source);
//					}
//				}
//			} else if(this.field != null){
//				if(this.field.isStatic()){
//					output.append(this.field.declaringClass.sourceName).append('.');
//					if(this.value instanceof StringLiteral){
//						output.append(((StringLiteral)this.value).source);
//					}
//				} else {
//					output.append("__this.");
//					if(this.value instanceof StringLiteral){
//						output.append(((StringLiteral)this.value).source);
//					}
//				}
//			}
//			return output;
//		}if(this.property.fieldBinding.type.isSubtypeOf(scope.environment().getType(TypeConstants.JAVA_LANG_ITEMTEMPLATE))){ 
//			output.append("new (");
//			this.template.generate(output);
//			output.append(")()");
//			return output;
//		} else {
//			if(this.value instanceof MarkupExtension){
//				MarkupExtension me = (MarkupExtension) value;
//				me.doGenerateExpression(scope, indent, output);  //TODO 2015-03-03 cym
//				output.append(".provideValue(");
//				output.append("_n, \""); 
//				this.property.buildInMarkupExtension(scope, indent, output);
//				output.append(");");
//			} else {
//				StringLiteral s = (StringLiteral) this.value;
//				output.append("\"").append(s.source).append("\"");
//			}
//		}
//		return output;
//	}
	
	public StringBuffer buildPair(Scope scope, int indent, StringBuffer output){
		
		this.property.doGenerateExpression(scope, indent, output).append(" : ");
		switch(propertyKind){
		case CLASS:
			this.template.generate(output);
			output.append(".prototype.__class");
			return output;
		case ENUM:
			((ReferenceBinding)this.property.fieldBinding.type).generate(output);
			output.append('.').append(((StringLiteral) this.value).source);
			return output;
		case FUNCTION:
			if(this.method != null){
				if(this.method.isStatic()){
					output.append(this.method.declaringClass.sourceName).append('.');
					if(this.value instanceof StringLiteral){
						output.append(((StringLiteral)this.value).source);
					}
				} else {
					output.append("this.");
					if(this.value instanceof StringLiteral){
						output.append(((StringLiteral)this.value).source);
					}
				}
			} else if(this.field != null){
				if(this.field.isStatic()){
					output.append(this.field.declaringClass.sourceName).append('.');
					if(this.value instanceof StringLiteral){
						output.append(((StringLiteral)this.value).source);
					}
				} else {
					output.append("this.");
					if(this.value instanceof StringLiteral){
						output.append(((StringLiteral)this.value).source);
					}
				}
			}
			return output;
		case TEMPLATE:
			output.append("new (");
			this.template.generate(output);
			output.append(")()");
			return output;
		case REFERENCE:
		case PRIMITIVE:
		case STRING:
			if(this.value instanceof MarkupExtension){
				MarkupExtension me = (MarkupExtension) value;
				me.doGenerateExpression(scope, indent, output);  //TODO 2015-03-03 cym
				output.append(".provideValue(");
				output.append("_n, \""); 
				this.property.buildInMarkupExtension(scope, indent, output);
				output.append(");");
			} else {
				StringLiteral s = (StringLiteral) this.value;
				output.append("\"").append(s.source).append("\"");
			}
		
		}
		return output;
	}
	
//	public void buildContent(Scope scope, int indent, StringBuffer output, String node){
//		if(CharOperation.equals(this.property.token, TEMPLATE)){
//			output.append("\n");
//			printIndent(indent + 1, output);
//			output.append("var _t = new (");
//			this.template.generate(output);
//			output.append(")();");
//			output.append("\n");
//			printIndent(indent + 1, output);
//			output.append("_t.create(_n);");
//			output.append("\n");
//			printIndent(indent + 1, output);
//			output.append("_c.template = _t;");
//		} else if(this.value instanceof MarkupExtension){
//			output.append("\n");
//			printIndent(indent + 1, output);
//			output.append("_n.");
//			if(this.property.receiver instanceof PropertyReference){
//				output.append(((PropertyReference)this.property.receiver).token);
//				output.append('.').append(this.property.token);
//			} else if(this.property.receiver == null){
//				output.append(this.property.token);
//			}
//			output.append(" = ");
//			MarkupExtension me = (MarkupExtension) this.value;
//			me.doGenerateExpression(scope, indent, output);
//			
//			output.append(".provideValue(");
//			output.append("_n, ");
//			this.property.buildInMarkupExtension(scope, indent, output);
//			output.append(");");
//		} else if(this.property.fieldBinding != null && this.property.fieldBinding.type instanceof ReferenceBinding){
//			ReferenceBinding ref = (ReferenceBinding) this.property.fieldBinding.type;
//			if((ref.modifiers & ClassFileConstants.AccFunction) != 0){
//				output.append("\n");
//				printIndent(indent + 1, output);
//				output.append("_n.addEventListener('").append(CharOperation.subarray(this.property.token, 2, -1)).append("', ");
//				if(this.method != null){
//					if(this.method.isStatic()){
//						output.append(this.method.declaringClass.sourceName).append('.');
//						if(this.value instanceof StringLiteral){
//							output.append(((StringLiteral)this.value).source);
//						}
//					} else {
//						output.append("this.");
//						if(this.value instanceof StringLiteral){
//							output.append(((StringLiteral)this.value).source);
//						}
//					}
//				} else if(this.field != null){
//					if(this.field.isStatic()){
//						output.append(this.field.declaringClass.sourceName).append('.');
//						if(this.value instanceof StringLiteral){
//							output.append(((StringLiteral)this.value).source);
//						}
//					} else {
//						output.append("this.");
//						if(this.value instanceof StringLiteral){
//							output.append(((StringLiteral)this.value).source);
//						}
//					}
//				}
//				output.append(", false);");
//			} else if(ref == scope.environment().getType(TypeConstants.JAVA_LANG_CLASS)){
//				output.append("\n");
//				printIndent(indent + 1, output);
//				output.append("_n.");
//				if(this.property.receiver instanceof PropertyReference){
//					output.append(((PropertyReference)this.property.receiver).token);
//					output.append('.').append(this.property.token);
//				} else if(this.property.receiver == null){
//					output.append(this.property.token);
//				}
//				output.append(" = ");
//				((ReferenceBinding)this.property.fieldBinding.type).generate(output);
//				output.append(".prototype.__class;");
//			} else {
//				output.append("\n");
//				printIndent(indent + 1, output);
//				output.append("_n.");
//				if(this.property.receiver instanceof PropertyReference){
//					output.append(((PropertyReference)this.property.receiver).token);
//					output.append('.').append(this.property.token);
//				} else if(this.property.receiver == null){
//					output.append(this.property.token);
//				} else {
//					output.append(this.property.token);
//				}
//				output.append(" = ");
//				
//				this.value.generateExpression(scope, indent, output).append(";");
//			}
//		} else if(this.type != null && this.type == scope.environment().getType(TypeConstants.JAVA_LANG_CLASS)){
//			output.append("\n");
//			printIndent(indent + 1, output);
//			output.append("_n.");
//			if(this.property.receiver instanceof PropertyReference){
//				output.append(((PropertyReference)this.property.receiver).token);
//				output.append('.').append(this.property.token);
//			} else if(this.property.receiver == null){
//				output.append(this.property.token);
//			}
//			output.append(" = ");
//			((ReferenceBinding)this.property.fieldBinding.type).generate(output);
//			output.append(".prototype.__class;");
//		} else {
//			output.append("\n");
//			printIndent(indent + 1, output);
//			output.append("_n.");
//			if(this.property.receiver instanceof PropertyReference){
//				output.append(((PropertyReference)this.property.receiver).token);
//				output.append('.').append(this.property.token);
//			} else if(this.property.receiver == null){
//				output.append(this.property.token);
//			} else {
//				output.append(this.property.token);
//			}
//			output.append(" = ");
//			
//			this.value.generateExpression(scope, indent, output).append(";");
//		}
//	}
	
	public void buildContent(Scope scope, int indent, StringBuffer output, String node){
		switch(propertyKind){
		case TEMPLATE:
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("var _t = new (");
			this.template.generate(output);
			output.append(")();");
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("_t.create(_n);");
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("_c.template = _t;");
			break; 
		case FUNCTION:
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("_n.addEventListener('").append(CharOperation.subarray(this.property.token, 2, -1)).append("', ");
			if(this.method != null){
				if(this.method.isStatic()){
					output.append(this.method.declaringClass.sourceName).append('.');
					if(this.value instanceof StringLiteral){
						output.append(((StringLiteral)this.value).source);
					}
				} else {
					output.append("this.");
					if(this.value instanceof StringLiteral){
						output.append(((StringLiteral)this.value).source);
					}
				}
			} else if(this.field != null){
				if(this.field.isStatic()){
					output.append(this.field.declaringClass.sourceName).append('.');
					if(this.value instanceof StringLiteral){
						output.append(((StringLiteral)this.value).source);
					}
				} else {
					output.append("this.");
					if(this.value instanceof StringLiteral){
						output.append(((StringLiteral)this.value).source);
					}
				}
			}
			output.append(", false);");
			break;
		case CLASS:
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("_n.");
			if(this.property.receiver instanceof PropertyReference){
				output.append(((PropertyReference)this.property.receiver).token);
				output.append('.').append(this.property.token);
			} else if(this.property.receiver == null){
				output.append(this.property.token);
			}
			output.append(" = ");
			((ReferenceBinding)this.property.fieldBinding.type).generate(output);
			output.append(".prototype.__class;");
			break;
		case ENUM:
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("_n.");
			if(this.property.receiver instanceof PropertyReference){
				output.append(((PropertyReference)this.property.receiver).token);
				output.append('.').append(this.property.token);
			} else if(this.property.receiver == null){
				output.append(this.property.token);
			}
			output.append(" = ");
			((ReferenceBinding)this.property.fieldBinding.type).generate(output);
			output.append('.').append(((StringLiteral) this.value).source);
			break;
		case REFERENCE:
		case STRING:
		case PRIMITIVE:
			output.append("\n");
			printIndent(indent + 1, output);
			output.append("_n.");
			if(this.property.receiver instanceof PropertyReference){
				output.append(((PropertyReference)this.property.receiver).token);
				output.append('.').append(this.property.token);
			} else if(this.property.receiver == null){
				output.append(this.property.token);
			}
			output.append(" = ");
			if(this.value instanceof MarkupExtension){
				MarkupExtension me = (MarkupExtension) this.value;
				me.doGenerateExpression(scope, indent, output);
				
				output.append(".provideValue(");
				output.append("_n, ");
				this.property.buildInMarkupExtension(scope, indent, output);
				output.append(");");
			} else {
				this.value.generateExpression(scope, indent, output).append(";");
			}
		}
	}
	
	@Override
	public StringBuffer generateStatement(Scope scope, int indent, StringBuffer output) {
		generateExpression(scope, indent, output);
		return output;
	}
	
	@Override
	public StringBuffer generateExpression(Scope scope, int indent, StringBuffer output) {
		return doGenerateExpression(scope, indent, output);
	}
	
	public TypeBinding[] genericTypeArguments() { return null;}
	public boolean isSuperAccess() {return false;}
	public boolean isTypeAccess() {return false;}
	public void setActualReceiverType(ReferenceBinding receiverType) {/* empty */}
	public void setDepth(int depth) {/* empty */ }
	public void setFieldIndex(int depth) {/* empty */ }
	public int sourceEnd() {return this.sourceEnd; }
	public int sourceStart() {return this.sourceStart; }
	public TypeBinding invocationTargetType() { return null; }
	public boolean receiverIsImplicitThis() { return false; }
	public InferenceContext18 freshInferenceContext(Scope scope) { return null; }
	public ExpressionContext getExpressionContext() { return ExpressionContext.VANILLA_CONTEXT; }
	@Override
	public boolean isQualifiedSuper() { return false; }
	public boolean checkingPotentialCompatibility() { return false; }
	public void acceptPotentiallyCompatibleMethods(MethodBinding[] methods) { /* ignore */ }

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		return output;
	}
}
