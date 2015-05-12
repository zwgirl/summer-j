package org.summer.sdt.internal.compiler.ast;

import org.summer.sdt.core.compiler.CharOperation;
import org.summer.sdt.internal.compiler.ASTVisitor;
import org.summer.sdt.internal.compiler.classfmt.ClassFileConstants;
import org.summer.sdt.internal.compiler.html.Namespace;
import org.summer.sdt.internal.compiler.impl.Constant;
import org.summer.sdt.internal.compiler.javascript.JsConstant;
import org.summer.sdt.internal.compiler.lookup.BlockScope;
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
import org.summer.sdt.internal.compiler.lookup.TagBits;
import org.summer.sdt.internal.compiler.lookup.TypeBinding;
import org.summer.sdt.internal.compiler.lookup.TypeIds;

/**
 * 
 * @author cym
 * 
 *         using by XAML
 */
public class HtmlAttribute extends HtmlNode implements InvocationSite{
	public HtmlPropertyReference property;
	public Expression value;
	
	public ReferenceBinding binding;
	public FieldBinding field;
	
	public static final int ENUM = 11;
	public static final int FUNCTION = 12;
	public static final int CLASS = 13;
//	public static final int TEMPLATESETTING =16;
	public static final int INSTANCE = 17;
	
	public static final int BYTE = 1;
	public static final int SHORT =2;
	public static final int INT = 3;
	public static final int LONG = 4;
	public static final int FLOAT = 5;
	public static final int DOUBLE = 6;
	public static final int BOOLEAN = 7;
	public static final int STRING = 8;
	
	public static final char[] NAME = "name".toCharArray();
	
	private int propertyKind;
	
	public char[] namespace;
	
	public HtmlAttribute(HtmlPropertyReference property) {
		super();
		this.property = property;
	}
	
	public HtmlAttribute(char[] prefix, long prefixPos, HtmlPropertyReference property) {
		super(prefix, prefixPos);
		this.property = property;
	}
	
	protected void processNamespace(HtmlElement element) {
		if(isXmlns()){
			element.storeNS(property.tokens[0], ((StringLiteral)value).source);
		}
		
		if(this.prefix == null && this.property.isDefaultXmlns()){
			element.defaultNamespace = ((StringLiteral)value).source;
		}
	}

	@Override
	public StringBuffer print(int indent, StringBuffer output) {
		return printStatement(indent, output);
	}
	
	@Override
	public StringBuffer printStatement(int indent, StringBuffer output) {
		output.append(property.hyphenName());
		output.append(" = ");
		value.printExpression(indent, output);
		return output;
	}

	public void resolve(BlockScope scope) {
		this.constant = Constant.NotAConstant;
		internalResolve(scope);
	}
	
	private void internalResolve(BlockScope scope){
		if(isXml()){
			this.resolvedType = scope.getJavaLangString();
		} else if(isXmlns() || property.isDefaultXmlns()){
			this.resolvedType = scope.getJavaLangString();
		} else if(this.prefix != null){
			this.namespace = scope.getNamespace(this.prefix);
		} else {
			this.resolvedType = property.resolveType(scope);
		}
		
		if(isName()){
			this.resolvedType = scope.getJavaLangString();
			scope.element.name = this;
		}

		if(this.resolvedType == null){
			this.resolvedType = scope.getJavaLangString();
		}

		switch(this.resolvedType.id){
			case TypeIds.T_byte:
			case TypeIds.T_JavaLangByte:
				propertyKind = BYTE;
				break;
			case TypeIds.T_short:
			case TypeIds.T_JavaLangShort:
				propertyKind = SHORT;
				break;
			case TypeIds.T_int:
			case TypeIds.T_JavaLangInteger:
				propertyKind = INT;
				break;
			case TypeIds.T_long:
			case TypeIds.T_JavaLangLong:
				propertyKind = LONG;
				break;
			case TypeIds.T_float:
			case TypeIds.T_JavaLangFloat:
				propertyKind = FLOAT;
				break;
			case TypeIds.T_double:
			case TypeIds.T_JavaLangDouble:
				propertyKind = DOUBLE;
				break;
			case TypeIds.T_boolean:
			case TypeIds.T_JavaLangBoolean:
				propertyKind = BOOLEAN;
				break;
			case TypeIds.T_JavaLangString:
				propertyKind = STRING;
				break;
		}
		
		if(this.propertyKind < 9 && this.propertyKind >= 1){
			value.resolveTypeExpecting(scope, this.resolvedType);
			return;
		}
		
		ReferenceBinding refType = (ReferenceBinding) this.resolvedType;
		
		if((refType.modifiers & ClassFileConstants.AccFunction) != 0 ){
			this.propertyKind = FUNCTION;
			this.tagBits |= HtmlBits.EventCallback;
			if(this.value instanceof StringLiteral){
				StringLiteral stringLiteral = (StringLiteral) this.value;
				this.field = scope.findField(scope.classScope().referenceContext.binding, stringLiteral.source(), this, true);
				if(field == null){
					scope.problemReporter().errorNoMethodFor(stringLiteral, this.resolvedType, new TypeBinding[0]); 
				} else {
					if(!this.resolvedType.isCompatibleWith(this.field.type)){
						scope.problemReporter().errorNoMethodFor(stringLiteral, this.resolvedType, new TypeBinding[0]); 
					}
				}
			} else if(this.value instanceof HtmlFunctionExpression){
				
			} else {
				scope.problemReporter().invalidEnumLiternal((Literal)this.value);
			}
		} else if(refType.isEnum()){
			this.propertyKind = ENUM;
			if(!(this.value instanceof StringLiteral)){
				if(this.value instanceof Literal){
					scope.problemReporter().invalidEnumLiternal((Literal)this.value);
				} else {
					
				}

				return;
			}
			this.field = scope.getField(refType, ((Literal) this.value).source(), this);
			if((field.modifiers & ClassFileConstants.AccEnum) == 0) {
				scope.problemReporter().invalidEnumLiternal((Literal) this.value); 
			}
			if(field == null){
				scope.problemReporter().invalidEnumLiternal((Literal) this.value); 
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
					scope.problemReporter().invalidEnumLiternal((Literal) this.value); 
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
		} else if(this.resolvedType instanceof ParameterizedTypeBinding && ((ParameterizedTypeBinding)this.resolvedType).original().id == TypeIds.T_JavaLangClass) {
			this.propertyKind = CLASS;
			ParameterizedTypeBinding pType = (ParameterizedTypeBinding) this.resolvedType;
			TypeBinding orignal = pType.arguments[0];
			this.binding = (ReferenceBinding) scope.getType(((Literal) this.value).source());
			if(!this.binding.isCompatibleWith(orignal)){
				scope.problemReporter().typeMismatchError(this.binding, orignal, this.value, this.property);
			}
		}  else {
			this.propertyKind = INSTANCE;
			if(this.value instanceof HtmlMarkupExtension){
				this.value.resolve(scope);
			}
		}
	}
	
	private boolean isName() {
		return this.property.isName();
	}

	private boolean isXmlns() {
		return this.prefix != null && CharOperation.equals(this.prefix, Namespace.XMLNS);
	}

	private boolean isXml() {
		return this.prefix != null && CharOperation.equals(this.prefix, Namespace.XML);
	}
	
	protected boolean hasMarkupExtension(){
		return this.value instanceof HtmlMarkupExtension;
	}
	
	protected boolean isEventHandler(){
		if(this.resolvedType == null || !(this.resolvedType instanceof ReferenceBinding)){
			return false;
		}
		
		return (((ReferenceBinding)this.resolvedType).modifiers & ClassFileConstants.AccFunction) != 0;
	}
	
	@Override
	public StringBuffer scriptDom(BlockScope scope, int indent, StringBuffer output, String parentNode, String logicParent, String context){
		if(this.value instanceof HtmlMarkupExtension){
			printIndent(indent + 1, output.append("\n"));
			HtmlMarkupExtension me = (HtmlMarkupExtension) this.value;
			
			me.newMarkupExtension(output, (ReferenceBinding) me.resolvedType);
			
			output.append("{");
			boolean comma = me.buildOptions(scope, indent, output, context);
			if((property.tagBits & TagBits.AnnotationAttribute) != 0){
				if(comma){
					output.append(", ");
				} 
				output.append("attribute : true");
			}
			
			output.append("}").append(")");
			
			output.append(".inject(");
			output.append(parentNode).append(", ");
			this.property.buildInjectPart(scope, indent, output);
			output.append(");");
			return output;
		} else if((this.tagBits & HtmlBits.NamedField) != 0){
			output.append("\n");
			printIndent(indent + 1, output);
			output.append(parentNode).append("[\"");
			this.property.generateExpression(scope, indent, output);
			output.append("\"]");
			output.append(" = ");
			if(this.value instanceof Literal){
				this.value.generateExpression(scope, indent, output);
			}
			output.append(";");
			output.append("\n");
			printIndent(indent + 1, output);
			output.append(context);
			output.append('.').append(((Literal) this.value).source());
			output.append(" = ").append(parentNode).append(";");
			return output;
		} else if(this.prefix != null){
			output.append("$.attrNS(\"");
			if(this.namespace != null){
				output.append(this.namespace);
			} else {
				output.append(this.prefix);
			}
			output.append("\", ").append(parentNode).append(", \"");
			this.property.doGenerateExpression(scope, indent, output).append("\", ");
			this.value.generateExpression(scope, indent, output).append(");");
			return output;
		}  else if((this.tagBits & HtmlBits.NS_ATTRIBUTE) != 0){
			HtmlPropertyReference attachProp = this.property;
			if(scope.element.resolvedType.isSubtypeOf(scope.getOrgW3cDomElement())){
				output.append("$.attrNS(");
				attachProp.receiverType.generate(output, scope.enclosingSourceType());
				output.append(".xmlns, ").append(parentNode).append(", \"");
				this.property.doGenerateExpression(scope, indent, output).append("\", ");
				this.value.generateExpression(scope, indent, output).append(");");
			}

			return output;
		}
		
		switch(propertyKind){
		case FUNCTION:
			output.append("\n");
			printIndent(indent + 1, output);
			output.append(parentNode).append(".addEventListener('").append(CharOperation.subarray(this.property.hyphenName(), 2, -1)).append("', ");
			if(this.field != null){
				if(this.field.isStatic()){
					output.append(this.field.declaringClass.sourceName).append('.');
					if(this.value instanceof Literal){
						output.append(((Literal)this.value).source());
					}
				} else {
					output.append(context).append(".");
					if(this.value instanceof Literal){
						output.append(((Literal)this.value).source());
					}
				}
			}
			output.append(", false);");
			break;
		case CLASS:
			output.append("\n");
			printIndent(indent + 1, output);
			output.append(parentNode).append('.');
			this.property.doGenerateExpression(scope, indent, output);
			
			output.append(" = ");
			((ReferenceBinding)this.property.binding.type).generate(output, null);
			output.append(".prototype.__class;");
			break;
		case ENUM:
			output.append("\n");
			printIndent(indent + 1, output);
			output.append(parentNode).append('.');
			this.property.doGenerateExpression(scope, indent, output);
			output.append(" = ");
			((ReferenceBinding)this.property.binding.type).generate(output, null);
			output.append('.').append(((Literal) this.value).source());
			break;
		case INSTANCE:
		case STRING:
		case BYTE:
		case SHORT:
		case INT:
		case LONG:
		case FLOAT:
		case DOUBLE:
		case BOOLEAN:
			if(scope.element.resolvedType.isSubtypeOf(scope.getJavaLangTag())){
				printIndent(indent + 1, output.append("\n"));
				output.append(parentNode).append('.');
				this.property.doGenerateExpression(scope, indent, output);
				output.append(" = ");
				this.value.generateExpression(scope, indent, output).append(";");
			} else if(scope.element.resolvedType.id == TypeIds.T_OrgW3cDomText){
				printIndent(indent + 1, output.append("\n"));
				output.append(parentNode).append('.');
				this.property.doGenerateExpression(scope, indent, output);
				output.append(" = ");
				this.value.generateExpression(scope, indent, output).append(";");
			} else {
				printIndent(indent + 1, output.append("\n"));
				output.append("$.attr(").append(parentNode).append(',');
				output.append(" \"");
				this.property.doGenerateExpression(scope, indent, output).append("\", ");
				this.value.generateExpression(scope, indent, output).append(");");
			}
		}
		return output;
	}
	
	public StringBuffer scriptElement(BlockScope scope, int indent, StringBuffer output, String parentNode, String logicParent, String context) {
		if(this.value instanceof HtmlMarkupExtension){
			output.append("\n");
			printIndent(indent + 1, output);
			HtmlMarkupExtension me = (HtmlMarkupExtension) this.value;
			me.scriptDom(scope, indent, output, parentNode, logicParent, context);
			
			output.append(".inject(");
			output.append(JsConstant.NODE_NAME).append(", ");
			this.property.buildInjectPart(scope, indent, output);
			output.append(");");
			return output;
		} 
		
		if(this.field != null){
			printIndent(indent + 1, output.append("\n"));
			output.append(parentNode).append("[\"");
			this.property.generateExpression(scope, indent, output);
			output.append("\"]");
			output.append(" = ");
			if(this.value instanceof Literal){
				this.value.generateExpression(scope, indent, output);
			}
			output.append(";");
			printIndent(indent + 1, output.append("\n")).append("_this");
			output.append('.').append(((Literal) this.value).source());
			output.append(" = ").append(parentNode).append(";");
			return output;
		}
		return output;
	}
	
	@Override
	public StringBuffer html(BlockScope scope, int indent, StringBuffer output, String context) {
		this.property.html(scope, indent, output);
		output.append("=");
		switch(propertyKind){
		case FUNCTION:
			if(this.field != null){
				output.append("\"");
				if(this.field.isStatic()){
					this.field.declaringClass.generate(output, scope.classScope().enclosingSourceType());
					output.append('.');
					if(this.value instanceof Literal){
						output.append(((Literal)this.value).source());
					}
				} else {
					output.append(context).append('.');
					if(this.value instanceof Literal){
						output.append(((Literal)this.value).source());
					}
				}
				output.append("(event);\" ");
			} else if(this.value != null){
				this.value.generateExpression(scope, indent, output);
			}
			break;
		default:
			this.value.generateExpression(scope, indent, output);
		}
		return output;
	}
	
	public ASTNode parserElement(int position){
		if(this.property.sourceStart < position && this.property.sourceEnd >= position){
			return this.property;
		} 
		if(this.value.sourceStart < position && this.value.sourceEnd >= position){
			if(this.value instanceof HtmlMarkupExtension){
				HtmlMarkupExtension markup = (HtmlMarkupExtension) this.value;
				return markup.parserElement(position);
			}
			return this.value;
		}
		return null;
	}
	
	@Override
	public StringBuffer option(BlockScope scope, int indent, StringBuffer output, String context) {
		
		this.property.option(scope, indent, output).append(" : ");
		switch(propertyKind){
		case CLASS:
			this.binding.generate(output, null);
			output.append(".prototype.__class");
			return output;
		case ENUM:
			((ReferenceBinding)this.property.binding.type).generate(output, null);
			output.append('.').append(((Literal) this.value).source());
			return output;
		case FUNCTION:
			if(this.field != null){
				if(this.field.isStatic()){
					output.append(this.field.declaringClass.sourceName).append('.');
					if(this.value instanceof Literal){
						output.append(((Literal)this.value).source());
					}
				} else {
					output.append(context).append('.');
					if(this.value instanceof Literal){
						output.append(((Literal)this.value).source());
					}
				}
			}
			return output;
//		case TEMPLATESETTING:
//			output.append("new (");
//			this.binding.generate(output, null);
//			output.append(")()");
//			return output;
		case INSTANCE:
		case BYTE:
		case SHORT:
		case INT:
		case LONG:
		case FLOAT:
		case DOUBLE:
		case BOOLEAN:
		case STRING:
			if(this.value instanceof HtmlMarkupExtension){
				HtmlMarkupExtension me = (HtmlMarkupExtension) value;
				me.optionValue(scope, indent, output, context);
			} else {
				Literal s = (Literal) this.value;
				if(s instanceof StringLiteral){
					output.append("\"").append(s.source()).append("\"");
				} else if(s instanceof NumberLiteral){
					if(s instanceof IntLiteral){
						output.append(((IntLiteral)s).value);
					} else if(s instanceof LongLiteral){
						output.append(s.constant.longValue());
					} else if(s instanceof FloatLiteral){
						output.append(((FloatLiteral)s).value);
					} else if(s instanceof DoubleLiteral){
						output.append(((DoubleLiteral)s).value);
					}
				} else {
					output.append(s.source());
				}
			}
		}
			
		return output;
	}
	
	@Override
	public void traverse(ASTVisitor visitor, BlockScope scope) {
		super.traverse(visitor, scope);
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

	public char[] value() {
		if(this.value instanceof Literal){
			return ((Literal)value).source();
		}
		throw new IllegalAccessError();
	}
}
